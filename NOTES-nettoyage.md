# Nettoyage & finitions avant soutenance — DariRentals

Chantier "nettoyage-finitions" (branche `feature/nettoyage-finitions`). Pas de nouvelle
fonctionnalité — dette technique connue, documentée dans les NOTES précédentes.

---

## Point 1 — CurrencyProvider mal placé (corrigé)

**Avant** : `CurrencyProvider` posé dans `admin/page.tsx` et `collaborator/page.tsx` (niveau
page), donc absent de l'arbre au-dessus de `app-shell.tsx` (niveau layout) — `useCurrency()`
aurait planté si appelé depuis la topbar.

**Après** :
- `CurrencyProvider` déplacé dans [`app/admin/layout.tsx`](nextjs-app-FINAL/nextjs-app/app/admin/layout.tsx)
  et [`app/collaborator/layout.tsx`](nextjs-app-FINAL/nextjs-app/app/collaborator/layout.tsx)
  (un par rôle, pas de provider partagé au niveau racine - chaque rôle a son propre
  `fetchCurrencies`/`fetchRates` scopé via `getEntityClients(role)`, et le layout racine
  (`/login`, `/reserver`, `/`) n'a pas besoin de conversion de devise).
- `admin/page.tsx`/`collaborator/page.tsx` : wrapper `<CurrencyProvider>` retiré (devenu
  redondant), `useCurrency()` toujours utilisé dans les dashboards, fourni par le layout ancêtre.
- `<CurrencySelector />` ajouté dans la topbar de
  [`components/app-shell.tsx`](nextjs-app-FINAL/nextjs-app/components/app-shell.tsx), à côté de
  `LanguageToggle` — **`ThemeToggle` n'y est plus** depuis le chantier `sidebar-premium` (déplacé
  en bas de la sidebar), donc "à côté des autres toggles (thème, langue)" ne s'applique plus
  qu'à `LanguageToggle` dans la topbar elle-même.

**Testé** :
- `npm run build` : passe (26 routes).
- Dashboard admin : sélecteur "MAD ▾" visible et fonctionnel dans la topbar, bascule vers USD
  convertit correctement toutes les valeurs affichées (chiffre d'affaires, revenu net, graphique).
- Sélection persistée en naviguant vers une AUTRE page (Biens & logements) - confirme que le
  provider est bien au niveau layout, pas page.
- `/reserver` (page publique, son propre `CurrencyProvider` local séparé, non touché) : toujours
  fonctionnel indépendamment.

---

## Point 2 — Débordement mobile à 375px (corrigé)

**Cause réelle, pas celle soupçonnée au départ** : mesuré précisément (`document.body.scrollWidth`
vs `clientWidth`) avant de corriger, comme demandé. Le tableau "Performance des propriétés"
**n'était PAS la cause du débordement de page** - il a déjà un `overflow-x-auto` qui le contient
correctement (vérifié : le tableau peut être plus large que l'écran sans jamais pousser le reste
de la page). Deux vrais coupables identifiés à la place :

1. **Le graphique Revenus (recharts) de `RevenueIntelligenceCard`** : son wrapper interne a déjà
   un `min-w-0` (ajouté lors d'un chantier précédent, avec un commentaire l'expliquant), mais ça
   ne suffisait pas - il est placé dans `<div className="lg:col-span-2">`, un item de grille CSS
   (`grid grid-cols-1 lg:grid-cols-3`). Un item de grille a par défaut `min-width: auto` (taille
   minimale basée sur son contenu), **pas 0**, même en `grid-cols-1` sur mobile - le `min-w-0`
   interne ne pouvait rien face à ça. Corrigé en ajoutant `min-w-0` sur cet item de grille
   lui-même, dans `app/admin/page.tsx` ET `app/collaborator/page.tsx` (même pattern dupliqué).
2. **Le `CurrencySelector` que je venais d'ajouter au point 1** : dans la topbar à 375px, le
   cluster hamburger + libellé de page + (recherche cachée) + devise + langue + cloche + avatar
   ne tenait plus. Corrigé en masquant `CurrencySelector` sous `sm` (640px), même seuil que la
   barre de recherche déjà cachée sur mobile dans la même topbar.

**Amélioration complémentaire** (demandée explicitement, au-delà de la cause racine) : la cellule
"Propriété" du tableau Performance n'avait ni troncature ni largeur max - avec des noms de test
courts ça ne se voyait pas, mais avec des noms réalistes plus longs le tableau se serait élargi
inutilement (toujours contenu par `overflow-x-auto`, mais mauvaise UX : obliger un scroll
horizontal sur un tableau de dashboard mobile). Ajouté `max-w-[160px] truncate` (`sm:max-w-[240px]`)
avec `title` complet au survol, dans
[`components/dashboard/property-performance-card.tsx`](nextjs-app-FINAL/nextjs-app/components/dashboard/property-performance-card.tsx).

**Testé, avant/après, mesure exacte (pas à l'œil)** :
| | `document.body.scrollWidth` à 375px |
|---|---|
| Avant | 653px (278px de débordement réel) |
| Après fix #1 (grille) | 428px (53px restants, causés par le point 2 lui-même) |
| Après fix #2 (topbar) | **375px = `clientWidth`, zéro débordement** |

Vérifié aussi après scroll jusqu'au tableau Performance des propriétés lui-même : toujours
375px, table réduite à 289px contenue dans son wrapper à 278px (léger scroll interne résiduel de
11px, normal et voulu, pas un débordement de page).

---

## Point 3 — Audit des données de test (aucune suppression, liste pour décision)

Audit fait par requêtes SQL directes (MySQL, base `darirentals`, via un script Node temporaire
avec `mysql2` - le client `mysql` CLI de XAMPP présent sur la machine ne supportait pas le plugin
d'authentification du serveur réel). Regroupé par chantier d'origine identifiable.

### Sociétés (`enterprise`)

| id | Nom | Origine probable | Recommandation |
|---|---|---|---|
| 1 | `abdo` | Société principale, utilisée partout (dashboard admin par défaut) | **Garder** - donnée réelle/principale |
| 2 | `kacm` | A une devise configurée, 2 collaborateurs (`anas` SubAdmin, `mohammed` Gestionnaire) | À discuter - pas de label "test", pourrait être une démo utile |
| 3 | `Societe A (test perms)` | Chantier `permissions-reelles-v2` | Test - à nettoyer ou garder comme démo multi-société |
| 4 | `Societe B (test perms)` | Chantier `permissions-reelles-v2` | Test - idem |
| 5 | `Societe Test Devise` | Chantier devises multi-currency | Test - probablement nettoyable |
| 7 | `Societe Test Cycle` | Chantier cycle-recursion (bug fix) | Test - probablement nettoyable |

### Utilisateurs (`user_app`, 24 au total)

- **Comptes de base/seed** : `admin`, `client`, `collaborator` (comptes génériques du seed
  initial) - **garder**.
- **Compte réel** : `admin1` / `ghazouaniabderrahman447@gmail.com` (= "anas", ton compte) -
  **garder**, évidemment.
- **`ali`, `mohammed`** (même email emsi-edu.ma que toi) : `mohammed` a un rôle réel (Gestionnaire
  @ kacm), `ali` n'a ni rôle ni société - orphelin, probablement un test abandonné.
- **`guest_*` (6 comptes : 3eb4c477, 82a40fc0, aee3eeb4, 855dc4e1, 083881ec, 26584553)** : créés
  automatiquement par les soumissions du formulaire public `/reserver` pendant les tests -
  **candidats naturels au nettoyage**, aucune valeur de démo.
- **`collab_a_test`, `collab_b_test`, `collab_a_subadmin`, `collab_a_subadmin2`** : chantier
  permissions, société A/B (test perms).
- **`retest_a_subadmin`, `retest_a_gestionnaire`, `retest_b_subadmin`, `retest_b_gestionnaire`,
  `cree_par_subadmin`, `retest_multi_subadmin`, `nonregression_fix2`** : chantier
  permissions-reelles-v2 (re-tests), noms explicitement préfixés "retest_"/"nonregression_".
- **`client.cycle.test`** : chantier cycle-recursion.

### Propriétés (`property`, 5 au total)

| id | Nom | Société | Remarque |
|---|---|---|---|
| 1 | `gh` | *(aucune - orpheline)* | Nom générique de test, mais **c'est la propriété utilisée dans toutes mes démos de cette session** (réservation, charges, graphique Revenus) - la nettoyer viderait le dashboard de démo actuel |
| 2 | `Riad Societe A` | Societe A (test perms) | Test perms |
| 3 | `Villa Societe B` | Societe B (test perms) | Test perms |
| 5 | `Prop legitime A2` | Societe A (test perms) | Test perms |
| 11 | `Non Regression A` | Societe A (test perms) | Test cycle-recursion |

### Clients (8 au total)

`client` (seed), 6 comptes `guest_*` (mêmes que ci-dessus, un compte client est créé pour chaque
demande de réservation publique), `client.cycle.test`.

### Données transactionnelles liées

- **Réservations (4)** : `RES-09310115` (gh), `RES-B-1` (Villa Societe B), `RES-A5-1` (Prop
  legitime A2), `RES-A2-1` (Riad Societe A) - toutes sur des propriétés de test/démo ci-dessus.
- **Demandes de réservation (6)** : toutes soumises par des comptes `guest_*`, sur `gh` (4) et
  `Riad Societe A` (2).
- **Charges (5)** : "electrcite"/"eau" (typos, sur `gh`), "Facture ZAMZAM MALL" (orpheline, sans
  propriété liée), "Charge SubAdmin" (Riad Societe A), "Charge non assignee" (Prop legitime A2).
- **Tâches (3)** : "Tache Societe B", "Tache Prop5 non accessible", "Tache Prop2 accessible" -
  toutes chantier permissions.
- **Paiements (2)** : montants 10 et 75, aucun détail identifiable (pas de note/référence).
- **Restriction de propriété (1)** : `retest_a_gestionnaire` → `Riad Societe A` (chantier
  permissions).

### Ce qui est sûr à 100% (pas d'ambiguïté)

- Les 6 comptes `guest_*` et leurs demandes de réservation associées : purs artefacts de test du
  formulaire public, zéro valeur de démo.
- Le compte `ali` : orphelin, aucun rôle ni société.

### Ce qui mérite ta décision avant tout nettoyage

- **La propriété `gh` et ses données liées** (réservation, 2 charges, 4 demandes) : nom de test
  générique MAIS c'est la donnée qui alimente concrètement le dashboard de démo actuel (chiffre
  d'affaires, graphique, tâches). La nettoyer sans la remplacer viderait la démo.
- **Les sociétés "Societe A/B (test perms)"** et tout ce qui en dépend (comptes `collab_*`/
  `retest_*`, propriétés, tâches, charges) : soit du nettoyage pur, soit tu les gardes comme
  démonstration concrète du système multi-société/permissions pour la soutenance (l'isolation
  des données entre sociétés est justement un argument de vente du projet).
- **`kacm`** : pas de marqueur "test" dans le nom, comportement ambigu - à clarifier avec toi.

### Actions exécutées (validées par abdo)

Transaction unique (tout ou rien), FK vérifiées avant exécution (`information_schema.KEY_COLUMN_USAGE`
+ recherche explicite de toute ligne référençant les ids concernés dans chaque table dépendante) :

1. **Supprimé** : le compte `ali` (id 13, collaborateur orphelin, zéro rôle/société/référence
   ailleurs) et les 6 comptes `guest_*` (clients génériques créés par le formulaire public
   `/reserver`) - avec leurs 6 `reservation_request` associées (obligatoire : ces demandes
   référençaient les clients supprimés) et leurs 7 lignes `role_app_user_app`.
   `client.cycle.test` et le compte `client` (seed) **conservés**, non concernés par la décision.
2. **Renommé** propriété id 1 : `gh` → **`Riad Zahra`**.
3. **Renommé** sociétés : id 3 `Societe A (test perms)` → **`Dar Atlas Hospitality`**, id 4
   `Societe B (test perms)` → **`Bleu Ourika Collection`**.
4. **`kacm`, `Societe Test Devise`, `Societe Test Cycle`** : non touchées, décision en attente.

**Vérifié après coup** : ré-audit SQL complet (relations `enterprise_membership` toujours
correctes après renommage, y compris `retest_multi_subadmin` qui appartient aux deux sociétés
renommées à la fois), et test en direct dans l'app (connecté en admin) : Dashboard, Biens &
logements (`Riad Zahra` apparaît, statut/prix/capacité intacts), Demandes de réservation ("Aucune
demande." - état vide propre, pas de crash), Collaborateurs (`ali` absent, tous les autres
listés sans erreur). Zéro nouvelle erreur console liée au nettoyage.

### Point resté en suspens : incohérence de noms à régler

Les propriétés **"Riad Societe A"** et **"Villa Societe B"** (dans les sociétés désormais
appelées Dar Atlas Hospitality / Bleu Ourika Collection) gardent leur ancien nom faisant
référence à "Societe A"/"Societe B" - ça va détonner en soutenance à côté des nouveaux noms de
sociétés. Pas renommées : non demandé explicitement, et je préfère confirmer plutôt que d'inventer
des noms de propriétés à ta place.

### `kacm` — ce que j'ai trouvé (en attente de ta décision)

- 2 collaborateurs : **anas** (`admin1`, ton propre compte, sans rôle explicite dessus) et
  **mohammed** (rôle "Gestionnaire", même domaine email `emsi-edu.ma` que toi).
- **0 propriété** rattachée.
- Une devise configurée (`currency` non nulle, contrairement à toutes les autres sociétés y
  compris "abdo").
- Aucune colonne de date de création sur `enterprise` - impossible de dater sa création par
  requête. Le nom "kacm" ne correspond à aucune convention "test" visible ailleurs dans la base.

Je ne peux pas déterminer son origine/but au-delà de ces faits bruts - à toi de me dire si tu la
reconnais.

### Décisions finales exécutées

1. **Propriétés renommées** : `Riad Societe A` (id 2) → **`Riad Kasbah`**, `Villa Societe B`
   (id 3) → **`Villa Sahara`**. Cohérent maintenant avec Dar Atlas Hospitality / Bleu Ourika
   Collection.
2. **`kacm` supprimée**. Vérifié avant suppression (comme demandé) :
   - Aucune donnée rattachée directement à `kacm` dans `ai_quota`, `ai_usage_log`, `client`,
     `financial_report`, `service_provider`, `property` (0 partout) - seule
     `enterprise_membership` la référençait (2 lignes : `anas` en SubAdmin, `mohammed` en
     Gestionnaire).
   - **`anas`** a une autre société valide (`abdo`) - aucun impact, reste pleinement
     fonctionnel.
   - **`mohammed`** n'avait `kacm` comme UNIQUE société. Conformément à l'instruction ("supprime
     juste ce rattachement précis, pas les comptes"), son compte collaborateur est conservé
     intact, mais **il se retrouve avec zéro société rattachée** après cette suppression - même
     situation que `ali` avant qu'on le supprime, sauf que là le compte reste. Signalé
     explicitement : son compte peut toujours se connecter (rôle applicatif toujours en base),
     mais `app/select-enterprise/page.tsx` redirige automatiquement un collaborateur à 0 société
     vers `/collaborator` (pas d'écran cassé, pas de boucle) - vérifié par lecture du code, PAS
     par connexion réelle (mot de passe de `mohammed` inconnu, jamais utilisé pendant ce
     chantier). Si ce compte doit un jour redevenir utile, il faudra soit le rattacher à une
     autre société, soit le supprimer comme `ali`.
   - Suppression faite en transaction unique : les 2 lignes `enterprise_membership` puis
     l'entreprise elle-même.

**Testé après coup** : connecté en admin - Biens & logements (`Riad Kasbah`/`Villa Sahara`
visibles, statuts/prix intacts), Réservations en vue Liste (`RES-B-1`→Villa Sahara,
`RES-A2-1`→Riad Kasbah, référencement correct), Collaborateurs (`anas` et `mohammed` toujours
listés sans erreur), zéro erreur console. Sociétés restantes : `abdo`, `Dar Atlas Hospitality`,
`Bleu Ourika Collection`, `Societe Test Devise`, `Societe Test Cycle` (ces deux dernières non
concernées par les décisions de ce chantier, toujours en place).
