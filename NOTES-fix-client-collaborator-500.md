# Fix — 2 erreurs 500 backend (POST client, DELETE collaborateur)

Branche `fix/client-collaborator-500`. Deux vrais bugs fonctionnels trouvés pendant les tests
du chantier `formulaires-premium`. Diagnostic fait sur **stack trace complète** (instance
backend relancée avec `server.error.include-stacktrace=always` sur un port dédié, base MySQL
partagée), pas par supposition.

---

## Bug 1 — `POST /api/admin/client/` → 500 (impossible de créer un client)

### Stack trace
```
java.lang.NullPointerException: Cannot invoke "java.lang.Boolean.booleanValue()"
  because the return value of "ma.zyn.app.ws.dto.client.ClientDto.getEnabled()" is null
    at ma.zyn.app.ws.converter.client.ClientConverter.toItem(ClientConverter.java:88)
    at ma.zyn.app.ws.facade.admin.client.ClientRestAdmin.save(ClientRestAdmin.java:104)
```

### Cause
`ClientConverter.toItem()` appelait, **sans garde de nullité** :
```java
item.setEnabled(dto.getEnabled());
item.setCredentialsNonExpired(dto.getCredentialsNonExpired());
item.setAccountNonExpired(dto.getAccountNonExpired());
item.setPasswordChanged(dto.getPasswordChanged());
item.setAccountNonLocked(dto.getAccountNonLocked());
```
Côté entité, `User.enabled` (etc.) est un **`boolean` primitif** ; côté DTO, `UserDto.getEnabled()`
renvoie un **`Boolean`**. Le front (comme l'ancien front Angular) n'envoie pas ces flags de
compte — ils arrivent à `null` → NPE à l'unboxing. Tous les autres champs de `toItem()` (String,
Long) **sont** déjà gardés par `if (StringUtil.isNotEmpty(...))`, et `toDto()` garde même ces
booléens-là (`StringUtil` a une surcharge `isEmpty(Boolean) = value == null`). Seuls les 5
setters booléens de `toItem()` avaient été générés sans garde.

### Correctif
[`ws/converter/client/ClientConverter.java`](backend-ms1-PROF-FINAL/backend-ms1/src/main/java/ma/zyn/app/ws/converter/client/ClientConverter.java) —
les 5 setters booléens passent sous `if (StringUtil.isNotEmpty(dto.getXxx()))`, exactement comme
le reste de `toItem()` et comme `toDto()`. Quand le DTO ne fournit rien, l'entité garde sa
valeur par défaut (`true`), et `ClientAdminServiceImpl.create()` force de toute façon
`enabled/accountNonExpired/...` à `true` juste après. Aucune logique métier changée.

---

## Bug 2 — `DELETE /api/admin/collaborator/id/{id}` → 500 (impossible de supprimer un collaborateur créé via l'app)

### Stack trace
```
org.springframework.dao.DataIntegrityViolationException: could not execute statement
  [Cannot delete or update a parent row: a foreign key constraint fails
   (`darirentals`.`role_app_user_app`, CONSTRAINT `FKg453g3aqxrweeeqml9d5wfipn`
    FOREIGN KEY (`user_app`) REFERENCES `user_app` (`id`))]
  [delete from user_app where id=?]
    at ma.zyn.app.service.impl.admin.auth.CollaboratorAdminServiceImpl$$SpringCGLIB$$0.deleteById
    at ma.zyn.app.ws.facade.admin.auth.CollaboratorRestAdmin.deleteById(CollaboratorRestAdmin.java:160)
```

### Cause
`CollaboratorAdminServiceImpl.create()` insère, pour **chaque** collaborateur :
- une ligne `role_app_user_app` (`RoleUser`, FK `user_app`),
- N lignes `model_permission_utilisateur` (`ModelPermissionUser`, FK `user_app`).

`deleteById()` → `deleteAssociatedLists()` nettoyait `enterprise_membership`, `ai_usage_log`,
`task.assigned_to`, `reservation_request.reviewed_by`, `collaborator_property_access` — **mais
pas** `role_app_user_app` ni `model_permission_utilisateur`. La suppression en cascade JPA
(`DELETE collaborator` puis `DELETE user_app`) échouait donc sur la contrainte FK de
`role_app_user_app.user_app`.

FK référençant `user_app` (relevé en base) : `client.id`, `collaborator.id` (héritage joined),
`role_app_user_app.user_app`, `model_permission_utilisateur.user_app`. Les deux dernières
doivent être nettoyées avant le delete.

### Correctif
[`service/impl/admin/auth/CollaboratorAdminServiceImpl.java`](backend-ms1-PROF-FINAL/backend-ms1/src/main/java/ma/zyn/app/service/impl/admin/auth/CollaboratorAdminServiceImpl.java) —
`deleteAssociatedLists()` appelle en plus :
```java
roleUserService.deleteByUserId(id);
modelPermissionUserService.deleteByUserId(id);
```
(méthodes déjà présentes sur ces services, déjà `@Autowired` dans la classe). Symétrique de
ce que fait `create()`.

### Correctif compagnon — `DELETE /api/admin/client/id/{id}`
`ClientAdminServiceImpl.create()` insère **les mêmes** lignes `RoleUser` + `ModelPermissionUser`,
et son `deleteAssociatedLists()` ne les nettoyait pas non plus. Le bug n'avait jamais été vu
parce que la création de client était elle-même cassée (bug 1). En corrigeant le bug 1, on
activait immédiatement un `DELETE client` cassé à l'identique. Même correctif appliqué à
[`ClientAdminServiceImpl.java`](backend-ms1-PROF-FINAL/backend-ms1/src/main/java/ma/zyn/app/service/impl/admin/client/ClientAdminServiceImpl.java).

---

## Vérification (vraies données, base MySQL réelle)

Backend recompilé (`mvnw package -DskipTests`) et lancé sur un port dédié, base partagée.

| Test | Avant | Après |
|---|---|---|
| `POST /api/admin/client/` avec flags à `null` (payload exact du front) | 500 NPE | **201** — client id 26 créé, `enabled:true` etc. appliqués |
| `DELETE /api/admin/client/id/26` | (n'existait pas — création cassée) | **200** |
| `DELETE /api/admin/collaborator/id/25` (le collaborateur de test resté bloqué) | 500 FK | **200** |
| Cycle complet `POST` + `DELETE` collaborateur (id 27) | — | **201** puis **200** |
| `GET` listes clients / collaborateurs | OK | OK (pas de régression) |

Inspection SQL après coup : `client`/`collaborator`/`user_app` pour id 25 et 26 → **0 ligne**,
`role_app_user_app` et `model_permission_utilisateur` pour ces ids → **0 ligne** (aucun orphelin).

## Point connexe non modifié (signalé)

`CollaboratorConverter.toItem()` a le même pattern de setters booléens non gardés que
`ClientConverter` avait. Il ne casse pas aujourd'hui uniquement parce que le formulaire
Collaborator du front envoie explicitement `enabled: true` etc. À harmoniser si l'occasion se
présente (même correctif d'une ligne), hors périmètre de ce fix ciblé.

## Fichiers modifiés

- `ws/converter/client/ClientConverter.java` — garde de nullité sur 5 setters booléens.
- `service/impl/admin/auth/CollaboratorAdminServiceImpl.java` — nettoyage RoleUser +
  ModelPermissionUser dans `deleteAssociatedLists`.
- `service/impl/admin/client/ClientAdminServiceImpl.java` — idem (correctif compagnon).
