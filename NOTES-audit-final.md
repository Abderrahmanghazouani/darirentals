# Audit final avant soutenance — DariRentals

Branche `fix/audit-final-avant-soutenance`. **Audit uniquement — aucune correction appliquée**,
conformément à la méthode demandée. Toutes les affirmations ci-dessous sont vérifiées (test
réel via navigateur et/ou API avec JWT, ou lecture directe du code source) — aucune supposition
non vérifiée n'est présentée comme un fait.

**Environnement de test** : backend `localhost:8036` (build incluant tous les chantiers
mergés jusqu'à `fix/client-collaborator-500`), frontend `localhost:3000`, MySQL partagée.

**Fixtures de test créées pendant l'audit, laissées en place** (à nettoyer ou réutiliser) :
- Collaborateurs `audit_gest` / `audit_sub` / `audit_multi` (mot de passe `test1234`, ids
  28/29/30) — respectivement Gestionnaire restreint à 1 propriété, SubAdmin, et collaborateur
  multi-société (abdo + Dar Atlas Hospitality). Utiles pour retester les correctifs du
  Finding P1-1 sans tout re-créer.
- Client `Audit Public Portal` (créé via `/reserver`) + sa demande de réservation confirmée
  (id 9, statut "Confirmee").
- Éléments de test ponctuels déjà nettoyés pendant l'audit (collaborateur créé par le
  contournement de permission, tâche et charge de test) - voir Finding P1-1 pour la preuve
  qui a nécessité leur création.

---

## PARTIE 1 — Parcours fonctionnel

### Résumé
La quasi-totalité des parcours testés fonctionne correctement, y compris des points sensibles
(chevauchement de réservation, isolation par société, restriction Gestionnaire par propriété,
export PDF/CSV, assistant IA). **Un problème de sécurité réel et exploitable a été trouvé**
(Finding P1-1) - c'est le plus important de tout l'audit, toutes parties confondues.

### Finding P1-1 — 🔴 CRITIQUE : un Gestionnaire (ou n'importe quel collaborateur) peut créer des comptes Collaborator sans la permission `canManageUsers`

**Reproduction exacte** (`audit_gest`, rôle Gestionnaire, `canManageUsers=false`) :
```
POST /api/collaborator/collaborator/
Authorization: Bearer <token Gestionnaire>
{"name":"x","email":"xtest@x.com","username":"xx1","password":"test1234",
 "isActive":true,"enabled":true,"accountNonExpired":true,"accountNonLocked":true,
 "credentialsNonExpired":true,"passwordChanged":true}
```
→ **HTTP 201**, compte créé (id 31, nettoyé après coup). Aucun champ `enterpriseMemberships`
dans le payload.

**Cause (code)** — [`CollaboratorCollaboratorServiceImpl.java`](backend-ms1-PROF-FINAL/backend-ms1/src/main/java/ma/zyn/app/service/impl/collaborator/auth/CollaboratorCollaboratorServiceImpl.java) :
```java
public Collaborator create(Collaborator t) {
    assertCanManageUsersForMemberships(t);   // <-- voir ci-dessous
    ...
}
private void assertCanManageUsersForMemberships(Collaborator t) {
    if (t.getEnterpriseMemberships() != null) {   // <-- le trou
        t.getEnterpriseMemberships().forEach(membership -> {
            effectivePermissionService.assertCanManageUsers(...);
        });
    }
}
```
La vérification ne s'exécute QUE si le payload contient une liste `enterpriseMemberships` non
nulle. L'omettre du tout (comme n'importe quel client HTTP peut le faire) fait de
`assertCanManageUsersForMemberships` un no-op complet — **aucune permission n'est vérifiée**.

**Portée réelle testée** :
- Bypass confirmé sur **`create()`** (payload sans `enterpriseMemberships` → 201, aucune
  permission requise).
- **`update()` correctement protégé** : testé avec le même type de payload (sans
  `enterpriseMemberships`) pour modifier un AUTRE collaborateur (id 29) → **403** correct. Le
  contrôleur charge apparemment les memberships existantes avant l'appel service, donc le
  bypass ne s'applique pas en pratique à `update()` (vérifié empiriquement, pas juste supposé).
- Le compte créé via le bypass n'a **aucune** `EnterpriseMembership` (puisque le payload qui
  a permis le bypass n'en contenait pas) - il ne peut donc rien voir/faire une fois connecté
  (isolation Chantier 1 : aucune société accessible). **Impact réel = création de comptes non
  autorisée (pollution/abus de ressource), pas une élévation de privilège directe** - vérifié
  que la voie d'escalade évidente (POST direct sur `/api/collaborator/enterpriseMembership/`
  pour se donner un rôle) reste correctement bloquée (403 confirmé, ce endpoint vérifie la
  permission de façon inconditionnelle).
- Le même bug (`assertCanManageUsersForMemberships`) est appelé à l'identique par `update()`
  et `create()` — la ligne 68 (`update`) et 328 (`create`) partagent la même méthode privée.
  Le contournement empirique n'a marché QUE sur `create()` dans mes tests ; à revérifier après
  correctif que `update()` ne devient pas vulnérable dans un scénario différent (payload
  construit différemment).

**Recommandation** (à discuter, pas appliquée) : rendre la vérification obligatoire et non
conditionnelle - par exemple exiger qu'un `create()` de Collaborator échoue si l'appelant n'a
`canManageUsers` sur AUCUNE société (indépendamment du contenu de `enterpriseMemberships`), ou
vérifier la permission sur la société de l'appelant lui-même plutôt que sur celles du payload.

---

### 1. Authentification — ✅ tout confirmé
- Connexion admin (admin/123) → `/admin` : OK.
- Connexion collaborateur 1 société (`audit_sub`) → `/collaborator` direct (pas de détour par
  select-enterprise) : OK.
- Connexion collaborateur 2+ sociétés (`audit_multi`) → `/select-enterprise` (liste les 2
  sociétés + rôle) → choix → `/collaborator` : OK.
- Déconnexion admin et collaborateur : OK (bouton "Déconnexion" en icône seule dans le pied de
  sidebar - repéré via son `aria-label`, pas de texte visible à côté de l'icône).
- Mauvais mot de passe → "Échec de la connexion", reste sur `/login` : OK.

### 2. Propriétés — ✅ déjà testé de façon exhaustive (chantier `formulaires-premium`)
Wizard 3 étapes, carte interactive, création/édition/suppression réelles, recentrage de la
carte en édition : déjà vérifiés bout en bout avec captures d'écran lors de ce chantier (voir
`NOTES-formulaires-premium.md`). Non re-testé en détail ici pour ne pas dupliquer un travail
déjà fait et documenté à la même rigueur. Filtres ville/type/statut non re-testés explicitement
dans cet audit (fonctionnalité simple, risque faible).

### 3. Réservations
- **Chevauchement bloqué** : réservation créée sur Riad Zahra 2026-09-03 → 2026-09-05, alors
  qu'une réservation "Client Cycle Test" occupe déjà 2026-09-02 → 2026-09-06 sur la même
  propriété → message **"Cette propriété est déjà réservée sur cette période."**, création
  refusée. ✅
- Création/modification/vue calendrier : déjà testées au chantier `formulaires-premium`
  (création réelle RES-…, modification du montant, calendrier visible). ✅ (non répété).
- "Annulation" au sens propre non testée isolément (changer le statut vers un statut
  "Annulée" existant) - probablement un simple changement de statut via le formulaire déjà
  testé, risque jugé faible, non vérifié explicitement.

### 4. Charges et paiements
- Création manuelle : déjà testée au chantier `formulaires-premium` (création/édition réelles).
  ✅ (non répété).
- **Scan de facture IA : non testé dans cet audit** (nécessite un upload de fichier réel, plus
  complexe à automatiser). Le comportement "message d'erreur de quota Gemini propre" a déjà été
  observé fonctionner correctement ailleurs dans l'app pendant cette session (l'assistant IA du
  dashboard affiche proprement les erreurs 429/502 Gemini, voir NOTES-mode-sombre.md et le test
  Finding ci-dessous) - le scan de facture utilise le même pattern de gestion d'erreur côté
  frontend, mais **recommandé de le tester manuellement une fois avant la soutenance** (upload
  d'une vraie image de facture), ce point précis n'ayant pas de preuve directe dans cet audit.

### 5. Tâches
- Création + assignation : déjà testées au chantier `formulaires-premium`. ✅
- **Tâche en retard** : filtre "En retard (N)" fonctionne correctement (isole bien les tâches
  dont l'échéance est dépassée). **Alerte visuelle présente sur le Dashboard** (icône
  `AlertTriangle` rouge à côté de chaque tâche en retard dans le widget "À faire aujourd'hui")
  mais **absente du tableau de la page `/admin/tasks` elle-même** - la colonne "Échéance" ne
  change pas de couleur/style pour une ligne en retard, seul le filtre existe. Écart mineur
  entre les deux écrans, pas un bug bloquant.

### 6. Clients et collaborateurs
- **Bug 500 création client : reconfirmé corrigé.** `POST /api/admin/client/` avec les flags de
  compte à `null` (payload exact du front) → 201 (déjà vérifié au chantier
  `fix/client-collaborator-500`, reconfirmé ici en marge des tests de permissions : création de
  `audit_gest`/`audit_sub`/`audit_multi` toutes réussies sans encombre).
- **Bug 500 suppression collaborateur : reconfirmé corrigé.** Suppression testée sur le
  collaborateur créé par le Finding P1-1 (id 31) → 200, lignes bien supprimées.
- Wizard 2 étapes, société/rôle, sélection de propriétés si Gestionnaire : déjà testés au
  chantier `formulaires-premium`. ✅ (non répété ici).

### 7. Permissions réelles
- **Gestionnaire restreint à 1 propriété** (`audit_gest`, accès à Riad Kasbah id 2 uniquement
  sur les 3 propriétés de sa société) :
  - `GET /api/collaborator/property/` → **ne renvoie que la propriété assignée**. ✅
  - `GET .../property/id/5` (même société, non assignée) → **404**. ✅
  - `DELETE .../property/id/2` (assignée, mais `canDeleteProperty=false`) → **403** "Votre rôle
    ne vous autorise pas à supprimer une propriété de cette société." ✅
  - `POST .../charge/` (`canManageFinancials=false`) → **403** "...gérer les finances...". ✅
  - `POST .../serviceProvider/` (`canManageServiceProviders=false`) → **403**
    "...gérer les prestataires...". ✅
  - `POST .../collaborator/` (`canManageUsers=false`) → **voir Finding P1-1** (bypass confirmé
    avec un payload minimal ; 403 correct avec un payload complet incluant des
    `enterpriseMemberships`).
- **SubAdmin** (`audit_sub`, même société) :
  - Voit les 3 propriétés de sa société (aucune restriction). ✅
  - `POST .../charge/` → **201** (a `canManageFinancials=true`). ✅
  - Isolation inter-société : `GET .../property/id/1` (propriété d'une AUTRE société) → **404**.
    ✅
- Ces résultats correspondent exactement à ce que documente déjà `NOTES-permissions.md`
  (Chantiers 1/2/3, testés le 25-27/08) - reconfirmés fonctionnels sur le code actuel, à
  l'exception du Finding P1-1 qui est une régression ou un trou non couvert par les tests
  précédents (leurs scripts de test incluaient systématiquement un payload avec
  `enterpriseMemberships`, ce qui explique qu'il n'ait jamais été détecté avant).

### 8. Portail public
- `/reserver` en déconnecté : galerie des 5 propriétés avec prix, position, bouton "Demander" :
  OK.
- Demande de réservation complète (dates, nom, téléphone, message) → soumission → "Demande
  envoyée / Merci ! Votre demande a bien été envoyée." : OK, un `Client` invité est créé
  automatiquement (`guest_f500b3dc` dans ce test).
- Traitement côté admin (`/admin/reservation-requests`) : la demande apparaît avec
  Confirmer/Refuser. Clic "Confirmer" → statut passe à "Confirmee", disparaît de "En attente".
  **Point à clarifier** : Confirmer une demande **ne crée aucune `Reservation` réelle**
  correspondante (vérifié : aucune réservation avec les dates demandées n'existe après
  confirmation) - l'admin doit apparemment créer manuellement la réservation dans
  `/admin/reservations` à partir des infos de la demande confirmée. Comportement voulu ou
  fonctionnalité incomplète ? à trancher.
- Statut affiché brut ("EnAttente"/"Confirmee", sans espace ni accent) - c'est la valeur
  stockée en base pour `ReservationRequestStatus.label`, pas un bug d'affichage frontend
  (vérifié via l'API : `"label":"Confirmee","code":"Confirmee"`, identiques) - qualité du
  contenu de seed, pas du code.

### 9. Rapports financiers et taux de change
- Génération d'un rapport (société "abdo", Mensuel, portée Entreprise) → "Rapport généré et
  figé avec succès.", nouvelle ligne dans l'historique. ✅
- **Export PDF** : `GET /api/admin/financial-reports/{id}/pdf` → 200, `application/pdf`, PDF
  valide (1 page). ✅
- **Export CSV** : idem → 200, `text/csv`, contenu valide (voir aussi Finding P2 pour le détail
  devise). ✅
- **Taux de change** : "Actualiser maintenant" → dates passées de 2026-09-10 à 2026-09-19 pour
  EUR/USD/GBP (clé ExchangeRate-API configurée et fonctionnelle). ✅. Voir Finding P1-2 pour un
  défaut d'affichage du message de confirmation.
- "Nouveau taux" (saisie manuelle) : le panneau s'ouvre correctement avec les champs attendus
  (non soumis, pour ne pas polluer les taux réels).

### Finding P1-2 — 🟡 mineur : message de confirmation "Actualiser maintenant" mal formé
Après actualisation des taux, le message affiché est littéralement :
> `Taux mis a jour : [EUR, USD, GBP]`

- "a" sans accent ("à jour").
- `[EUR, USD, GBP]` a la forme d'un tableau JS sérialisé brut plutôt qu'une liste de devises
  formatée en texte ("EUR, USD, GBP" sans crochets, ou "EUR, USD et GBP").
Confirmé dans le DOM : `<p class="text-sm text-success">Taux mis a jour : [EUR, USD, GBP]</p>`.

### 10. AI Property Assistant — ✅ tout confirmé
- **Morning Insights** : texte généré avec de vraies données ("Bonjour, votre score de santé
  global est de 65 (Bon) et vos revenus du mois s'élèvent à 2650 MAD... 4 tâches en retard...")
  - cohérent avec le reste du dashboard au moment du test.
- **Chat** : question réelle ("Combien j'ai gagné ce mois-ci ?") → réponse exacte ("Vous avez
  gagné 2 650 MAD ce mois-ci"), cohérente avec le KPI affiché.
- **Question hors périmètre** ("Quelle est la capitale de la France ?") → refus propre :
  "En tant qu'assistant dédié à la gestion de votre portefeuille... je ne peux donc pas répondre
  aux questions de culture générale...".
- Conforme au principe déjà établi : contenu généré par l'IA volontairement en français, quelle
  que soit la langue de l'interface (non re-testé en anglais dans cet audit, cohérent avec le
  principe déjà validé par abdo).

---

## PARTIE 2 — Changement de devise

**Architecture** (vérifiée par lecture du code) : `CurrencyProvider` enveloppe tout
`/admin/**` et `/collaborator/**` au niveau des `layout.tsx` (donc le sélecteur de devise de la
topbar est présent sur TOUTES les pages admin/collaborateur) et `/reserver`. Mais `useCurrency()`
n'est **consommé** (montants réellement convertis) que dans une poignée d'endroits précis :
`app/admin/page.tsx`, `app/collaborator/page.tsx`, `app/reserver/page.tsx`, et les composants
qu'ils alimentent via une prop `formatValue`. **Partout ailleurs, les montants sont affichés en
MAD brut avec un suffixe littéral `"MAD"`, quelle que soit la devise sélectionnée dans la
topbar** - vérifié précisément, liste exhaustive ci-dessous.

### ✅ Endroits où la conversion s'applique correctement
- **Dashboard admin** : KPI "Revenus" du mois (`format(...)`), tableau "Réservations
  récentes" (`format(r.amount)`), graphique Revenue Intelligence (tooltip au survol des barres
  via `formatValue`), Property Performance Card (colonnes Revenu/Charges/Bénéfice net via
  `formatValue`).
- **Dashboard collaborateur** : idem (KPI Revenus, tableau réservations récentes, Revenue
  Intelligence). Note : ce dashboard n'inclut pas Property Performance Card (composant absent,
  pas un bug de devise).
- **`/reserver`** : prix des propriétés dans la galerie (`format(p.pricePerNight)`). Le
  formulaire de demande lui-même n'affiche aucun montant (pas de prix/nuit ni de total dans le
  dialog "Demander").

### ❌ Endroits où la conversion NE s'applique PAS (montant visible, resté en MAD brut)
1. **Rentabilité par propriété** (`/admin/property/[id]/rentabilite`) - **les 3 cartes
   Revenus/Charges/Bénéfice net demandées explicitement dans l'audit**, plus le détail des
   réservations et charges prises en compte. Le fichier a sa propre fonction locale
   `formatMoney()` (juste un `toLocaleString`), aucun import de `useCurrency`.
2. **Rapports financiers** (`/admin/financial-reports`) - montants de l'historique des rapports
   (`formatAmount()` locale, `${value.toFixed(2)} MAD`) **et le contenu du CSV/PDF exporté**
   (vérifié : le CSV généré contient littéralement "Revenus (MAD)", "Charges (MAD)", "Bénéfice
   net (MAD)" - toujours en MAD, quelle que soit la devise choisie à l'écran au moment de
   l'export). C'est structurellement obligé : la génération du PDF/CSV est faite **côté
   backend**, qui n'a aucune connaissance de la préférence de devise (un `localStorage` côté
   navigateur, jamais transmis à l'API).
3. **Property (liste)**, admin et collaborateur - colonne "Prix/nuit" (`p.pricePerNight + "
   MAD"`), brut.
4. **Charges (liste)**, admin et collaborateur - colonne "Montant" et les cartes "Charges par
   propriété" (total par propriété), brut.
5. **Paiements (liste)**, admin et collaborateur - colonne "Montant", brut.
6. **Health Score** (widget du dashboard, admin ET collaborateur) - le détail de la
   "Performance financière" affiche littéralement `revenus X MAD, charges Y MAD` en dur
   (`lib/dashboard/health-score.ts`), jamais passé par `useCurrency`.
7. **Graphique Revenue Intelligence** - nuance trouvée en creusant : le **Tooltip** (survol
   d'une barre) est bien converti, mais **les graduations de l'axe Y** du graphique
   (`MonthlyChart`) restent des nombres bruts non convertis (ex. "0, 850, 1700...") - une
   incohérence visuelle mineure si l'axe et le tooltip sont regardés ensemble en devise
   étrangère (l'axe reste à l'échelle MAD, le tooltip affiche une autre devise).
8. **Formulaires Charge / Payment / Reservation** - tous les montants saisis ou déjà affichés
   (montant d'une charge, montant versé d'un paiement, liste des charges à rattacher avec leur
   montant, prix/nuit et montant total d'une réservation) sont en MAD brut, jamais convertis.
   **Point à clarifier explicitement (ambigu)** : est-ce voulu (MAD = devise de référence de
   la société, on ne saisit/lit jamais un formulaire dans une autre devise, seuls les écrans de
   consultation "vue d'ensemble" convertissent pour l'affichage) ou est-ce un oubli ? L'analyse
   du code suggère fortement que c'est le comportement **voulu à l'origine** (MAD = devise de
   stockage et de saisie, la conversion est un confort d'affichage en lecture seule) - mais ce
   n'est écrit nulle part explicitement, à confirmer.
9. **AI Property Assistant** (Morning Insights + Chat) - `lib/dashboard/ai-facts.ts` envoie
   toujours les montants en MAD à l'IA, **volontairement** (commentaire explicite dans le code :
   "la conversion à l'affichage est un détail purement frontend qui n'a pas sa place dans les
   faits envoyés à l'IA"). Décision déjà actée, pas un bug — mais **effet de bord visible** :
   si un admin choisit d'afficher le dashboard en EUR, les Insights/le Chat continueront de
   parler en MAD ("Vous avez gagné 2 650 MAD..."), à côté d'un dashboard affiché en euros. Pas
   une erreur de code, mais une incohérence UX potentiellement déroutante à signaler.

---

## PARTIE 3 — Changement de langue

**Architecture** (vérifiée par lecture du code) : le dictionnaire `lib/i18n/translations.ts`
ne couvre que ces sections : `common`, `reserver`, `login`, `dashboardHeader`, `dashboardStats`,
**`dashboardHome`**, `propertyMap`, `dayTimeline`, `healthScore`, `revenueIntelligence`,
`propertyPerformance`, `actionCenter`, `tools`, `upcomingArrivals`, `allModules`, `assistant`.
**Tout le reste de l'application est en français codé en dur**, quelle que soit la langue
choisie - vérifié précisément fichier par fichier (comptage des appels `dict.` dans chaque
fichier, 0 = aucune traduction possible sur cette page).

### Finding P3-1 — 🟠 le plus visible : le dashboard collaborateur n'est PAS traduit, contrairement à son jumeau admin
**`app/collaborator/page.tsx` : 0 appel à `dict.*`, n'importe même pas `useLanguage`.**
`app/admin/page.tsx` (la même page, côté admin) : 25 appels à `dict.*`, dict `dashboardHome`
utilisé pour exactement ce contenu. Les deux pages sont structurellement identiques et
partagent les MÊMES clés de dictionnaire déjà écrites - seule la page collaborateur ne les
utilise jamais. Concrètement, en anglais, un collaborateur voit :
- "Bonjour, ravi de vous revoir" (jamais "Hello, welcome back") - le greeting du haut de page.
- Toute la carte "Réservations récentes" : titre, sous-titre, "Voir toutes", "Chargement...",
  **et l'état vide "Aucune réservation."** (alors que la version admin utilise
  `dict.dashboardHome.noReservations`, déjà traduit).
- La carte "À faire aujourd'hui" (même chose, texte en dur).
- **Alors que**, sur cette même page, les 3 widgets `PropertyMapCard`/`DayTimelineCard`/
  `RevenueIntelligenceCard` (qui s'auto-traduisent, `useLanguage()` interne) basculent
  correctement en anglais. **Résultat en EN : une page mi-anglaise mi-française**, avec des
  widgets traduits juste à côté de texte français en dur - le résultat le plus visuellement
  incohérent de tout l'audit.

### Finding P3-2 — 🟠 sidebar et topbar chrome (`components/app-shell.tsx`) : 0% traduits
Présents sur **toutes** les pages admin ET collaborateur, donc l'impact est global :
- Tous les libellés de navigation : "Biens & logements", "Réservations", "Demandes de
  réservation", "Tâches", "Charges", "Paiements", "Collaborateurs", "Taux de change",
  "Rapports financiers", "Rentabilité", "Tous les modules".
- Titres de section : "OPÉRATIONS", "FINANCES", "ÉQUIPE" (visibles dans le texte de page
  capturé, en dur dans le JSX).
- Le fil d'ariane ("Vue d'ensemble" en breadcrumb), le placeholder de recherche
  ("Rechercher"), le nom de société ("Société" par défaut), l'infobulle "Déplier"/"Replier" du
  bouton de collapse.
- Le bouton de langue lui-même (FR/EN) fonctionne (change bien `dict`/`locale`), mais ne
  traduit donc quasiment rien de ce qui l'entoure dans la sidebar.

### Finding P3-3 — 🟠 tous les écrans CRUD (listes ET formulaires) : 0% traduits
Vérifié précisément (0 appel `dict.` dans chacun) :
`admin/property` (liste + formulaire), `admin/client` (liste + formulaire),
`admin/collaborator` (liste + formulaire), `admin/reservations` (liste) +
`reservation-form.tsx`, `admin/charges` (liste) + `charge-form.tsx`, `admin/payments` (liste) +
`payment-form.tsx`, `admin/tasks` (liste) + `task-form.tsx`, `admin/financial-reports`,
`admin/exchange-rates`, `select-enterprise`. Idem côté `/collaborator/*` (mêmes composants
partagés ou équivalents non traduits).

**Formulaires Sheet/wizard du chantier `formulaires-premium` : confirmé, jamais inclus dans le
système i18n** (chantier fait sans lien avec le système de langue, comme suspecté dans la
demande) - tous les libellés d'étape ("Informations générales", "Localisation", "Détails",
"Informations personnelles", "Rattachement", "Récapitulatif") et tous les boutons
("Suivant", "Précédent", "Enregistrer", "Annuler") sont des chaînes françaises en dur dans
`property-form.tsx`, `collaborator-form.tsx`, `form-stepper.tsx`, `form-section.tsx`,
`entity-form-dialog.tsx`.

Messages d'erreur génériques du hook CRUD partagé (`lib/use-entity-crud.ts`) : "Erreur de
chargement" / "Erreur d'enregistrement" / "Erreur de suppression" - en dur, cohérent avec le
reste (ces pages ne sont de toute façon pas traduites par ailleurs).

### Finding P3-4 — 🟡 landing page (`/`) et écran de chargement premium : jamais traduits
- `app/page.tsx` (landing complète) : 0 appel `dict`/`useLanguage`, et **aucun sélecteur de
  langue visible sur cette page** - un visiteur ne peut pas la basculer en anglais depuis là.
  628 Nuance : si l'utilisateur a déjà choisi EN ailleurs (ex. depuis `/login`) puis revient sur
  `/`, la landing repasse en français silencieusement (elle ignore la préférence stockée), sans
  moyen de la rebasculer sans retourner sur `/login`.
- L'écran de chargement (`if (loggedIn)` dans `app/page.tsx`, chantier
  `loading-login-premium`) - texte "Ouverture de votre espace…" en dur. Contrairement à la
  landing, l'utilisateur qui voit cet écran est **forcément déjà connecté** (donc a
  nécessairement déjà eu accès au toggle de langue ailleurs) - c'est le cas le plus net d'un
  texte qui aurait dû suivre la préférence déjà choisie.

### Ce qui reste correctement français dans les deux langues (vérifié intentionnel, pas un bug)
- Contenu généré par l'assistant IA (Morning Insights, réponses du Chat) : volontairement en
  français quelle que soit la langue de l'interface, principe déjà validé par abdo dans un
  chantier précédent. Non re-signalé comme un défaut.

---

## Récapitulatif des findings (pour décision)

| # | Sévérité | Résumé |
|---|---|---|
| P1-1 | 🔴 Critique | Un collaborateur sans `canManageUsers` peut créer des comptes Collaborator (payload sans `enterpriseMemberships`) |
| P1-2 | 🟡 Mineur | Message "Taux mis a jour : [EUR, USD, GBP]" mal formé (accent + array brut) |
| P2 | 🟠 Majeur (produit) | Conversion de devise absente sur ~9 zones listées (Rentabilité, Rapports financiers + export, listes Property/Charges/Payments, Health Score, axe Y du graphique, formulaires, IA) |
| P3-1 | 🟠 Majeur | Dashboard collaborateur non traduit du tout (0 vs 25 appels dict côté admin) - page mi-FR mi-EN une fois basculée |
| P3-2 | 🟠 Majeur | Sidebar/topbar (app-shell.tsx) 0% traduits - impact sur toutes les pages |
| P3-3 | 🟠 Majeur | Tous les écrans CRUD (listes + formulaires, y compris les Sheet/wizard de `formulaires-premium`) 0% traduits |
| P3-4 | 🟡 Mineur | Landing page + écran de chargement premium non traduits (pas de toggle sur la landing ; l'écran de chargement concerne pourtant un utilisateur déjà connecté) |

**Points à clarifier avec abdo avant de prioriser les correctifs** :
- Le comportement voulu pour les montants saisis dans les formulaires (Charge/Payment/
  Reservation) : rester en MAD (devise de référence) ou suivre la devise sélectionnée ?
- "Confirmer" une demande de réservation publique doit-il créer automatiquement une
  `Reservation`, ou est-ce voulu que ce soit une étape manuelle séparée ?
- Ampleur du chantier i18n à prévoir : vu le volume (sidebar + tous les CRUD + dashboard
  collaborateur), un vrai chantier de traduction complet est probablement nécessaire plutôt que
  quelques correctifs ponctuels - à discuter en termes de priorité avant la soutenance.

---

# CORRECTIONS (après décision d'ordre de traitement)

## ✅ P1-1 — corrigé et testé (permission `canManageUsers` sur création/modification de Collaborator)

**Correctif** :
- [`EffectivePermissionService.assertCanManageUsersOnAnyEnterprise()`](backend-ms1-PROF-FINAL/backend-ms1/src/main/java/ma/zyn/app/service/security/EffectivePermissionService.java)
  (nouveau) : l'appelant doit avoir `canManageUsers` sur au moins une des sociétés auxquelles il
  est rattaché, quel que soit le contenu du payload.
- [`CollaboratorCollaboratorServiceImpl.assertCanManageUsersForMemberships()`](backend-ms1-PROF-FINAL/backend-ms1/src/main/java/ma/zyn/app/service/impl/collaborator/auth/CollaboratorCollaboratorServiceImpl.java)
  appelle désormais cette garde **avant** la boucle sur les memberships du payload (qui reste en
  place pour vérifier chaque société visée). `create()` et `update()` en bénéficient.
- `update()` vérifie en plus `canManageUsers` contre les memberships **réelles en base** du
  collaborateur modifié (comme `deleteById()` le faisait déjà), plus seulement celles du payload.

**Tests (backend recompilé, port dédié, vraies données)** :

| Cas | Avant | Après |
|---|---|---|
| Gestionnaire `POST` collaborateur, payload sans `enterpriseMemberships` | **201** (faille) | **403** |
| Gestionnaire `POST` avec membership société 3 | 403 | 403 |
| Gestionnaire `PUT` collaborateur 29 | 403 | 403 |
| SubAdmin `POST` sans memberships (légitime) | 201 | **201** |
| SubAdmin `POST` avec membership société 3 (légitime) | 201 | **201** |
| SubAdmin `PUT` collaborateur 28 (société 3 seule) | 200 | **200** |
| SubAdmin `PUT` collaborateur 30 (sociétés 1 + 3, la 1 hors de sa portée) | (non testé) | **403** |

Restriction Gestionnaire par propriété inchangée (`audit_gest` : 1 propriété visible, 404 sur les autres).

**Effet de bord assumé** : un SubAdmin ne peut plus modifier un collaborateur rattaché à une société
dont il ne fait pas partie (même si l'une de ses sociétés lui est commune) - cohérent avec
`deleteById()`, plus strict qu'avant.
