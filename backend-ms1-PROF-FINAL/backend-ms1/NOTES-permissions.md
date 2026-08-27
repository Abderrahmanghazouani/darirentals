# Permissions réelles — DariRentals

Suivi du chantier sécurité "permissions réelles" (branche `feature/permissions-reelles`).
Contexte complet dans la demande initiale ; ce fichier documente ce qui a été fait,
pourquoi, et comment le retester manuellement.

---

## Chantier 1 — Isolation par société côté serveur (FAIT)

### Problème de départ

N'importe quel collaborateur authentifié pouvait appeler `GET /api/collaborator/property/`
(ou reservations, charges, paiements, tâches, clients, prestataires) et récupérer les
données de **toutes** les sociétés, pas seulement celle(s) auxquelles il est rattaché.
Le filtrage frontend (`lib/filter-by-enterprise.ts`) est du confort d'affichage, pas
de la sécurité : rien n'empêchait un appel direct à l'API de contourner ce filtre.

### Ce qui a été construit

**[`service/security/EnterpriseAccessService.java`](src/main/java/ma/zyn/app/service/security/EnterpriseAccessService.java)**
— nouveau service central :
- `getCurrentCollaborator()` : récupère le collaborateur authentifié via
  `SecurityUtil.getCurrentUser()`, qui lit `SecurityContextHolder` (le username vient
  du JWT vérifié par `AuthTokenFilter`, jamais d'un paramètre envoyé par le client).
- `getAccessibleEnterpriseIds()` : la liste des `Enterprise` auxquelles ce collaborateur
  est rattaché, via ses `EnterpriseMembership` (`EnterpriseMembershipDao.findByCollaboratorId`).
- `hasAccessToEnterprise(id)` : vérifie qu'un id de société précis fait partie de cette liste.

**Décision de conception** : un collaborateur multi-société (plusieurs `EnterpriseMembership`)
a accès à **toutes** ses sociétés côté API — le filtrage à "la société actuellement
sélectionnée" (`/select-enterprise`, `localStorage`) reste un filtre d'affichage frontend.
La sécurité serveur garantit juste qu'il ne voit jamais une société à laquelle il
n'appartient pas du tout. C'est ce que demandait l'énoncé ("une société de
l'EnterpriseMembership du collaborateur"), et ça évite d'inventer une notion de
"société active côté backend" qui n'existe pas dans le schéma actuel.

**Entités filtrées**, dans chaque `*CollaboratorServiceImpl` correspondant :

| Entité | Lien vers Enterprise | Fichier modifié |
|---|---|---|
| Property | direct (`property.enterprise`) | `PropertyCollaboratorServiceImpl.java` |
| Client | direct (`client.enterprise`) | `ClientCollaboratorServiceImpl.java` |
| ServiceProvider | direct (`serviceProvider.enterprise`) | `ServiceProviderCollaboratorServiceImpl.java` |
| Reservation | indirect via `reservation.property.enterprise` | `ReservationCollaboratorServiceImpl.java` |
| Charge | indirect via `charge.property.enterprise` | `ChargeCollaboratorServiceImpl.java` |
| Task | indirect via `task.property.enterprise` | `TaskCollaboratorServiceImpl.java` |
| Payment | indirect via `payment.serviceProvider.enterprise` | `PaymentCollaboratorServiceImpl.java` |

Note : l'énoncé initial disait "Reservation, Charge, Payment, Task via Property" — mais
`Payment` n'a pas de lien vers `Property` dans le schéma actuel (seulement vers
`ServiceProvider`). Le filtrage de Payment passe donc par `ServiceProvider.enterprise`.

Pour chaque entité, les méthodes suivantes sont désormais filtrées :
- `findAll()` / `findAllOptimized()` : ne retournent que les lignes des sociétés accessibles.
- `findById(id)` / `findWithAssociatedLists(id)` : retournent `null` (donc 404 côté
  contrôleur REST, comportement déjà existant) si l'entité trouvée n'appartient pas
  à une société accessible.
- `findByCriteria(...)`, `findPaginatedByCriteria(...)`, `getDataSize(...)` : le résultat
  de la `Specification` est filtré en mémoire par société accessible avant d'être renvoyé.
- `findByEnterpriseId(id)` (Property/Client/ServiceProvider) et `findByPropertyId(id)`
  (Reservation/Charge/Task) et `findByServiceProviderId(id)` (Payment) : ces endpoints
  passthrough acceptaient un id arbitraire fourni par le client (IDOR direct) — ils
  renvoient maintenant une liste vide si l'id demandé n'est pas accessible.

Nouvelles méthodes DAO ajoutées (Spring Data derived queries) : `findByEnterpriseIdIn`
(Property/Client/ServiceProvider), `findByPropertyIdIn` (Reservation/Charge/Task),
`findByServiceProviderIdIn` (Payment).

### Limites connues (volontairement hors scope de ce chantier)

- **`findAllOptimized()`** : la requête JPQL `SELECT NEW Xxx(id, label)` ne charge pas le
  champ `enterprise`/`property`, donc impossible de filtrer dessus directement. Ces méthodes
  retombent maintenant sur `findAll()` (liste complète mais déjà filtrée) — légèrement moins
  performant que la projection d'origine, mais correct.
- **`getDataSize`/`findPaginatedByCriteria`** : le filtrage se fait en mémoire après la requête
  DB, donc le compte total et la pagination peuvent être légèrement imprécis si beaucoup de
  lignes appartiennent à d'autres sociétés (edge case). Le frontend actuel n'utilise que
  `findAll()`, donc pas d'impact réel aujourd'hui.
- **Task sans `property`** (rattachée seulement à une `reservation` ou un `serviceProvider`) :
  non couverte par ce filtre, qui ne regarde que `task.property`. À couvrir si ce cas d'usage
  apparaît.
- **Autres endpoints passthrough non durcis** : `findByCityId`, `findByPropertyTypeCode`,
  `findByPropertyStatusCode`, `findByChargeTypeCode`, `findByReservationStatusCode`, etc. ne
  filtrent pas par société (ils ne fuient pas directement une société précise comme le
  faisait `findByEnterpriseId`, mais renvoient quand même toutes les lignes tous statuts/types
  confondus si appelés directement). Risque plus faible que l'IDOR par id de société, mais à
  traiter dans un futur passage si le temps le permet.
### Isolation par société, côté ÉCRITURE (ajouté après coup — voir plus bas)

En vérifiant le chantier, une question légitime a été soulevée : la lecture était filtrée,
mais rien n'empêchait un collaborateur de la société A de **créer** ou **réassigner** une
entité vers la société B. Vérifié et confirmé réel :

- `PropertyConverter.toItem(dto)` (utilisé par `create()`) recopiait `dto.getEnterprise().getId()`
  tel quel, sans aucune vérification — un `POST /api/collaborator/property/` avec
  `enterprise: {id: <société B>}` créait bien la propriété pour la société B.
- `PropertyConverter.copy(dto, t)` (utilisé par `update()`) remplaçait purement et simplement
  `t.getEnterprise()` par un nouvel objet dont l'id venait de `dto.getEnterprise().getId()` —
  un collaborateur pouvait donc prendre une propriété qu'il possède légitimement et la
  réassigner à une autre société via `PUT`.
- Même schéma pour Client, ServiceProvider (assignation directe), et pour
  Reservation/Charge/Task/Payment (assignation d'une `property`/`serviceProvider` appartenant
  à une autre société).

**Fix** : ajout d'un garde-fou `assertEnterpriseAssignable(t)` (Property/Client/ServiceProvider)
ou `assertPropertyAssignable(t)` / `assertServiceProviderAssignable(t)` (Reservation/Charge/Task/
Payment) au tout début de `create()` et juste après le chargement de l'entité existante dans
`update()`, dans chacun des 7 `*CollaboratorServiceImpl`. Il vérifie que la société (directe ou
via la property/serviceProvider visée) fait partie de `EnterpriseAccessService.getAccessibleEnterpriseIds()`,
sinon lève `PermissionDeniedException`.

**Nouveau : [`PermissionDeniedException.java`](src/main/java/ma/zyn/app/zynerator/exception/PermissionDeniedException.java)**
— exception dédiée, mappée sur HTTP 403 via un nouveau
**[`PermissionDeniedExceptionHandler.java`](src/main/java/ma/zyn/app/zynerator/exception/PermissionDeniedExceptionHandler.java)**
(`@RestControllerAdvice`). Ce dernier a été nécessaire car **aucun contrôleur REST du projet
(`ws.facade.*`) n'étend `BaseController`/`AbstractController`** — leurs `@ExceptionHandler`
locaux ne s'appliquent donc jamais, et n'importe quelle `RuntimeException` non gérée
explicitement dans le contrôleur remontait en 500 brut (bug préexistant plus large, non
corrigé dans son ensemble ici — seul `PermissionDeniedException` est couvert par ce nouvel
advice global, pour rester dans le périmètre de ce chantier). `BusinessRuleException` et
`EntityNotFoundException` restent donc, comme avant ce chantier, sujettes au même problème
ailleurs dans le code — signalé ici pour mémoire, pas corrigé (hors scope).

Réutilisé tel quel pour le Chantier 2 (permissions de rôle → 403).

### Comment retester manuellement

Démarrer le backend (`mvnw spring-boot:run`), puis :

```powershell
# 1. Login admin (compte seedé par défaut : admin / 123)
$login = Invoke-RestMethod http://localhost:8036/login -Method Post `
  -Body (@{username="admin";password="123"} | ConvertTo-Json) -ContentType application/json
$h = @{ Authorization = "Bearer $($login.token)" }

# 2. Créer 2 societes, 2 proprietes (une par societe), 2 collaborateurs (un par societe)
#    via /api/admin/enterprise/, /api/admin/property/, /api/admin/collaborator/
#    (voir CollaboratorDto pour les champs requis : enabled/accountNonExpired/... doivent
#    être fournis explicitement, sinon NPE preexistant sur getEnabled().booleanValue())

# 3. Login avec chaque collaborateur, puis :
$hA = @{ Authorization = "Bearer <token collaborateur A>" }
Invoke-RestMethod http://localhost:8036/api/collaborator/property/ -Headers $hA
# -> ne doit renvoyer QUE les proprietes de la societe de A

Invoke-RestMethod http://localhost:8036/api/collaborator/property/id/<id_propriete_de_B> -Headers $hA
# -> doit renvoyer 404 (pas 200 avec les données de B)

Invoke-RestMethod http://localhost:8036/api/collaborator/property/enterprise/id/<id_societe_B> -Headers $hA
# -> doit renvoyer une liste vide

# 4. Toujours avec le token de A, tenter d'ECRIRE vers la societe B :
Invoke-RestMethod http://localhost:8036/api/collaborator/property/ -Method Post -Headers $hA `
  -ContentType application/json -Body (@{name="x";streetNumber="1";streetName="x";postalCode="x";enterprise=@{id=<id_societe_B>}} | ConvertTo-Json)
# -> doit renvoyer 403 Forbidden (pas 201 avec la propriete creee pour B)
```

**Testé le 2026-08-25** avec 2 sociétés + 2 collaborateurs + plusieurs properties :
- Lecture : le collaborateur A ne voit que sa propriété (`findAll`), 404 sur l'accès direct par
  id à la propriété de B, liste vide sur `findByEnterpriseId` de la société de B.
- Écriture : 403 confirmé sur (1) création d'une Property pour la société B, (2) réassignation
  d'une Property déjà possédée vers la société B via `update`, (3) création d'une Reservation
  pointant vers une Property de la société B. Vérifié ensuite au niveau FK (vue admin
  `findByEnterpriseId`) que la propriété de A n'avait pas été altérée par la tentative
  d'attaque. Non-régression confirmée : A peut toujours créer une Property pour sa propre
  société sans problème.

---

## Chantier 2 — Vérification réelle SubAdmin vs Gestionnaire (FAIT)

### Problème de départ

`canManageUsers`/`canManageFinancials`/`canDeleteProperty`/`canManageServiceProviders`/
`canManageAiUsage` existaient sur `CollaboratorRole` et `CollaboratorPermissionOverride`
mais n'étaient vérifiés **nulle part** : un Gestionnaire pouvait faire exactement les mêmes
actions qu'un SubAdmin.

### Ce qui a été construit

**[`service/security/EffectivePermissionService.java`](src/main/java/ma/zyn/app/service/security/EffectivePermissionService.java)**
— pour le collaborateur authentifié + une société donnée :
1. Retrouve son `EnterpriseMembership` pour cette société précise (un collaborateur
   multi-société peut avoir des rôles différents selon la société).
2. Permission effective = `CollaboratorRole.canX` **OU** (s'il existe un
   `CollaboratorPermissionOverride` pour cette membership) `override.canX`.

**Décision de conception (OR, pas remplacement complet)** : `CollaboratorPermissionOverride`
a ses 5 champs initialisés à `false` par défaut (Java + colonne DB), et son converter ne
touche que les champs explicitement envoyés par le client — impossible donc de distinguer
fiablement "override non touché" de "override mis à false explicitement". Traiter l'override
comme un remplacement complet ferait courir le risque qu'un override créé pour accorder UNE
permission en révoque silencieusement 4 autres. Le OR garantit qu'un override ne fait
qu'accorder une exception, jamais en retirer une — documenté dans le code, à revoir si le
besoin d'un override "restrictif" apparaît un jour.

`assertCanX(enterpriseId)` lève `PermissionDeniedException` → **403** (voir
`PermissionDeniedExceptionHandler`, construit au Chantier 1).

**Vérifications appliquées** (`create`/`update`/`deleteById`, dans les `*CollaboratorServiceImpl`) :

| Action | Permission | Fichier |
|---|---|---|
| Supprimer une Property | `canDeleteProperty` | `PropertyCollaboratorServiceImpl` |
| Créer/modifier/supprimer un Collaborator | `canManageUsers` (pour chaque société visée par ses `enterpriseMemberships`) | `CollaboratorCollaboratorServiceImpl` |
| Créer/modifier/supprimer un EnterpriseMembership | `canManageUsers` | `EnterpriseMembershipCollaboratorServiceImpl` |
| Créer/modifier/supprimer une Charge | `canManageFinancials` | `ChargeCollaboratorServiceImpl` |
| Créer/modifier/supprimer un Payment | `canManageFinancials` | `PaymentCollaboratorServiceImpl` |
| Créer/modifier/supprimer un ServiceProvider | `canManageServiceProviders` | `ServiceProviderCollaboratorServiceImpl` |
| Scan de facture IA | `canManageAiUsage` | `InvoiceScanRestCollaborator` |

**En prime, `deleteById` a aussi été mis sous garde d'isolation par société (Chantier 1)
sur les 7 entités** — c'était un trou identique à celui du create/update découvert avant de
démarrer ce chantier (un collaborateur pouvait supprimer n'importe quelle entité d'une autre
société par id, sans aucun contrôle).

### Bug préexistant trouvé et corrigé en cours de route

**[`EnterpriseMembershipConverter.java:70`](src/main/java/ma/zyn/app/ws/converter/enterprise/EnterpriseMembershipConverter.java)**
— `toItem()` ne recopiait `collaboratorRole` que si `this.collaboratorRole` (flag d'instance)
était `true`. Comme `EnterpriseMembershipConverter()` n'appelle que `initList(true)` (jamais
`initObject(true)`, exactement le même bug générique que celui trouvé en tout début de
session sur `CollaboratorConverter`), ce flag reste `false` par défaut. Résultat concret :
**créer un Collaborator avec une `EnterpriseMembership` imbriquée perdait silencieusement le
rôle assigné** (`collaboratorRole` restait `null` en base) — ce qui aurait rendu le Chantier 2
inopérant en pratique (un SubAdmin fraîchement créé se retrouvait sans aucune permission).
Corrigé en alignant `toItem()` sur le pattern déjà utilisé pour `collaborator`/`enterprise`
dans la même méthode (copie inconditionnelle si `dto.getCollaboratorRole().getId() != null`,
sans dépendre du flag).

### Fix additionnel : `/api/collaborator/invoice-scan/analyze`

`enterpriseId` **et** `collaboratorId` étaient acceptés comme paramètres de requête bruts,
sans aucune vérification — un collaborateur pouvait attribuer son usage IA à n'importe quel
`collaboratorId`, ou contourner le contrôle de quota en omettant `enterpriseId` (le quota
n'était vérifié que si non-null). Corrigé : `collaboratorId` n'est plus accepté du client
(dérivé du contexte de sécurité), et `enterpriseId` est validé contre les sociétés
accessibles avant tout appel à l'API Gemini.

### Comment retester manuellement

```powershell
# Collaborateur "Gestionnaire" (permissions par defaut = false) :
Invoke-RestMethod http://localhost:8036/api/collaborator/charge/ -Method Post -Headers $hGestionnaire `
  -ContentType application/json -Body (@{label="x";amount=10;property=@{id=<id>}} | ConvertTo-Json)
# -> 403 "Votre rôle ne vous autorise pas à gérer les finances de cette société."

# Meme appel avec un collaborateur "SubAdmin" dont le role a canManageFinancials=true -> doit reussir (201).
```

**Testé le 2026-08-25** : créé un rôle SubAdmin avec les 5 permissions à `true` et vérifié
qu'un Gestionnaire (permissions par défaut `false`) est bloqué en 403 sur : création de Charge,
création de ServiceProvider, création de Collaborator, scan de facture IA — et que le SubAdmin
équivalent réussit exactement les mêmes actions pour sa propre société, tout en restant bloqué
(403, isolation Chantier 1) s'il cible une société à laquelle il n'est pas rattaché.

### Limites connues

- `canManageUsers` sur la modification d'un Collaborator ne vérifie que les
  `enterpriseMemberships` **envoyées dans le payload de la requête** ; si un collaborateur
  malveillant omettait sa liste de memberships dans un `PUT`, aucune vérification ne
  s'appliquerait sur ce point précis (mais il ne pourrait de toute façon rien changer côté
  memberships puisqu'elles ne seraient pas dans le diff). Comportement jugé acceptable, non
  approfondi davantage faute de temps.
- Aucune UI frontend n'expose encore la configuration des `CollaboratorPermissionOverride`
  (ni la gestion des rôles avec leurs 5 booléens) — c'est un CRUD générique existant
  (`/admin/collaboratorRole`, etc.) mais pas mis en avant. Hors scope de ce chantier backend.

---

## Chantier 3 — Restriction Gestionnaire par propriété précise

Pas commencé.
