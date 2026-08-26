# Multi-devises — DariRentals

Suivi de la fonctionnalité "conversion de devises" (branche `feature/multi-devises`).

**Principe central, à ne jamais perdre de vue** : la conversion est **purement visuelle**.
Tous les montants restent stockés en base en MAD (`Property.pricePerNight`,
`Reservation.amount`, `Charge.amount`, `Payment.amount`) — rien n'est jamais converti ni
réécrit en base. Seul l'affichage change côté frontend, à la volée, selon la devise
sélectionnée.

---

## 1. Devise de référence

`Enterprise.currency` existait déjà (entité, DTO, converter — rien à créer). Ce qui manquait :

- **Aucune devise n'était marquée par défaut** : corrigé dans `AppApplication.createCurrency()`
  — MAD est maintenant seedée avec `isDefault = true`, et les 4 devises ont un `symbol`
  (DH, €, £, $) pour un affichage propre.
- **Une nouvelle société ne recevait aucune devise** si l'admin ne la précisait pas
  explicitement à la création. Corrigé dans
  [`EnterpriseAdminServiceImpl.create()`](src/main/java/ma/zyn/app/service/impl/admin/enterprise/EnterpriseAdminServiceImpl.java) :
  si `currency` est absent du payload, la devise marquée `isDefault=true` (MAD) est assignée
  automatiquement.

Testé : création d'une Enterprise sans champ `currency` → `currency.code = "MAD"` confirmé
(vérifié via `GET /api/admin/enterprise/enterprise/id/{id}` — voir note ci-dessous sur un bug
d'affichage préexistant sans rapport).

**Note (bug préexistant, pas dans le scope de ce chantier)** : `EnterpriseRestAdmin.findById()`
utilise `converter.init(true)`, qui — comme plusieurs autres converters déjà rencontrés dans
ce projet — n'active que les listes (`initList`), pas les objets liés (`initObject`). Résultat :
le endpoint `GET /api/admin/enterprise/id/{id}` ne renvoie jamais `currency` (toujours absent),
même quand il est bien enregistré en base. `GET /api/admin/enterprise/` (findAll) fonctionne
correctement car il appelle explicitement `initObject(true)`. Non corrigé ici pour rester dans
le périmètre demandé — même famille de bug que celui déjà documenté dans NOTES-permissions.md.

---

## 2. Gestion des taux de change

### Backend
- CRUD générique déjà existant et fonctionnel : `/api/admin/exchangeRate/`,
  `/api/admin/currency/` (rien à construire côté API authentifiée).
- **Nouveau** : [`ws/facade/open/CurrencyRestOpen.java`](src/main/java/ma/zyn/app/ws/facade/open/CurrencyRestOpen.java)
  expose en lecture publique (`/api/open/currency/currencies` et `/api/open/currency/exchange-rates`,
  déjà `permitAll` dans `WebSecurityConfig` via `/api/open/**`) — nécessaire pour la page
  `/reserver`, qui n'a pas de session authentifiée. Attention prise en compte dans le code :
  `CurrencyConverter` est un bean singleton dont les listes par défaut incluent
  `enterprises`/`collaborators` — le controller force `initList(false)` pour ne jamais exposer
  ces listes sur un endpoint public (fuite de données potentielle sinon).
- **Seed initial** (idempotent, ne duplique rien au redémarrage) : `AppApplication.createExchangeRate()`
  crée MAD→EUR (0.092), MAD→USD (0.100), MAD→GBP (0.079) avec `source = "Saisie manuelle (seed initial)"`.
  Ce sont des valeurs de départ approximatives, à corriger par l'admin.
### Automatisation (mise à jour après coup)

**Source choisie : [ExchangeRate-API](https://www.exchangerate-api.com/)**, pas Frankfurter.
Vérifié en direct avant d'implémenter : Frankfurter (taux de référence BCE) liste 31 devises et
**ne couvre pas le MAD** (`GET /v1/latest?base=MAD` → 404). ExchangeRate-API couvre bien le MAD
(confirmé dans leur table des devises supportées) — c'est elle qui est utilisée.

- **Clé API** : gratuite, à créer soi-même sur https://www.exchangerate-api.com/ (création de
  compte — je ne peux pas le faire à la place de l'utilisateur). Jamais en dur dans le code :
  lue depuis la variable d'environnement `EXCHANGERATE_API_KEY`
  (`exchangerate.api.key=${EXCHANGERATE_API_KEY:}` dans `application-dev.properties`, même
  convention que `gemini.api.key`/`GEMINI_API_KEY` pour le scan de factures).
- **[`service/currency/ExchangeRateSyncService.java`](src/main/java/ma/zyn/app/service/currency/ExchangeRateSyncService.java)** :
  appelle `GET https://v6.exchangerate-api.com/v6/{cle}/latest/MAD`, lit `conversion_rates.EUR`
  /`.USD`/`.GBP`, et pour chacune met à jour l'`ExchangeRate` existant (base=MAD, target=devise)
  ou en crée un si absent. `source` est renseignée `"ExchangeRate-API (auto, <date>)"` pour
  distinguer visuellement un taux automatique d'un taux saisi à la main.
- **Planification quotidienne** : `@Scheduled(cron = "0 0 3 * * *")` sur `scheduledSync()` (3h du
  matin). Nécessite `@EnableScheduling` — **ajouté sur `AppApplication`, avec un effet de bord
  corrigé au passage** : `DatabaseDumpScheduler` (dump BDD + push vers un dépôt Git distant
  toutes les 5 min) existait déjà avec un `@Scheduled` mais était inerte faute
  d'`@EnableScheduling`. L'activer globalement l'aurait réveillé par effet de bord, alors qu'il
  utilise des identifiants placeholder (`remoteRepo.accessToken=yourAccessTokenPlz`) — corrigé
  en le passant derrière `@ConditionalOnProperty(name = "database.dump.scheduler.enabled",
  havingValue = "true")` (même pattern déjà utilisé dans ce projet pour désactiver Kafka), donc
  désactivé par défaut comme il l'était de fait jusqu'ici.
- **Déclenchement manuel** : bouton "Actualiser maintenant" sur `/admin/exchange-rates`, qui
  appelle `POST /api/admin/currency/exchange-rates/sync` (nouveau
  [`ExchangeRateSyncRestAdmin.java`](src/main/java/ma/zyn/app/ws/facade/admin/currency/ExchangeRateSyncRestAdmin.java),
  protégé par `/api/admin/**` = `ROLE_ADMIN` comme le reste de l'admin). Réutilise exactement le
  même service que le job planifié.
- **Gestion des pannes** — testé en réel avec deux scénarios : (1) `EXCHANGERATE_API_KEY` absente
  → message clair loggé en `WARN`, réponse HTTP 502 avec message exploitable, taux existants
  strictement inchangés, application non affectée. (2) clé invalide (vrai appel réseau à
  l'API réelle avec une fausse clé) → l'API répond 403, capté et traduit en 502 propre, taux
  inchangés. Dans les deux cas, aucune exception ne remonte, aucun crash, le job planifié comme
  le déclenchement manuel se comportent pareil (même méthode `sync()` sous-jacente).
- **Non testé en conditions réelles (nécessite une vraie clé que je ne peux pas générer)** : le
  scénario "succès", c'est-à-dire une vraie mise à jour de taux depuis l'API. Logique identique
  au chemin d'échec, juste la branche succès de `sync()` — à confirmer avec une clé réelle
  (voir section "Comment retester" plus bas).

### Frontend
Nouvel écran dédié [`app/admin/exchange-rates/page.tsx`](../nextjs-app-FINAL/nextjs-app/app/admin/exchange-rates/page.tsx)
(lien ajouté dans les outils du Dashboard) : liste les taux actuels ("1 MAD = X EUR"), permet
d'en créer/modifier/supprimer via un formulaire simple (devise cible + taux + source), et
propose un bouton "Actualiser maintenant" (voir ci-dessus) qui affiche le résultat (succès ou
message d'erreur) puis rafraîchit la liste. Le CRUD générique `/admin/exchangeRate` (généré
automatiquement pour toutes les entités) reste disponible en secours mais cet écran dédié est
plus lisible pour ce cas d'usage précis.

---

## 3. Conversion à l'affichage + sélecteur de devise

Nouveau module réutilisable :
- [`lib/currency/conversion.ts`](../nextjs-app-FINAL/nextjs-app/lib/currency/conversion.ts) —
  fonctions pures : `convertFromBase(amountInMad, targetCode, rates)`,
  `formatMoney(amount, code, symbol)`. Convention : `ExchangeRate.rate` = combien d'unités de
  `targetCurrency` pour 1 unité de `baseCurrency` (ex: rate=0.092 pour MAD→EUR signifie
  1 MAD = 0.092 EUR). Si aucun taux n'est connu pour une devise, la valeur brute est renvoyée
  telle quelle plutôt que de planter.
- [`lib/currency/currency-context.tsx`](../nextjs-app-FINAL/nextjs-app/lib/currency/currency-context.tsx) —
  `CurrencyProvider` (charge devises + taux via des fonctions de fetch injectées, garde la
  devise choisie en `localStorage`) et `useCurrency()` (`convert`, `format`, `selectedCode`,
  `setSelectedCode`).
- [`components/currency/currency-selector.tsx`](../nextjs-app-FINAL/nextjs-app/components/currency/currency-selector.tsx) —
  sélecteur `<Select>` réutilisable, masqué automatiquement s'il n'y a pas au moins 2 devises
  disponibles (ex: aucun taux configuré).

**Intégré sur** :
- **Dashboard admin** ([`app/admin/page.tsx`](../nextjs-app-FINAL/nextjs-app/app/admin/page.tsx)) :
  sélecteur dans l'en-tête, carte "Revenu" et graphique mensuel (`MonthlyChart`, qui accepte
  maintenant une prop `formatValue`) convertis à la volée. `CurrencyProvider` alimenté par
  `getEntityClients("admin").currency/.exchangeRate` (authentifié).
- **`/reserver`** (page publique) : sélecteur au-dessus de la liste, prix par nuit converti.
  `CurrencyProvider` alimenté par les nouveaux endpoints publics `fetchPublicCurrencies` /
  `fetchPublicExchangeRates` (`lib/public-api.ts`).

Comportement si aucune devise alternative n'est sélectionnée ou si le chargement échoue : tout
s'affiche normalement en MAD (comportement identique à avant cette fonctionnalité) — testé.

---

## 4. Devise d'affichage par collaborateur

`Collaborator.displayCurrency` existait déjà (jamais utilisé). Nouveau hook
[`lib/use-collaborator-display-currency.ts`](../nextjs-app-FINAL/nextjs-app/lib/use-collaborator-display-currency.ts)
(`useCollaboratorDisplayCurrencyCode()`) : résout la préférence du collaborateur connecté
(même approche que `useCurrentCollaboratorId` déjà présent dans le projet — pas d'endpoint
`/me` côté backend, on filtre la liste des collaborateurs par le username décodé du token) et
se branche sur `<CurrencyProvider defaultCode={...}>` : ce défaut ne prend effet que si
l'utilisateur n'a **jamais** choisi explicitement une devise (une préférence déjà enregistrée
dans `localStorage` prime toujours).

**Limite assumée** : ce hook est écrit et prêt, mais **pas encore branché sur une page réelle**.
Le Dashboard financier n'existe aujourd'hui que côté `/admin` (réservé `ROLE_ADMIN`, donc jamais
visité par un `Collaborator`) ; `/collaborator/page.tsx` n'est qu'une grille de modules sans
montant à convertir. Il n'y a donc actuellement aucune page où la préférence d'un collaborateur
pourrait s'appliquer. Le mécanisme est fonctionnel et testable isolément ; il suffira d'ajouter
`defaultCode={useCollaboratorDisplayCurrencyCode()}` au `<CurrencyProvider>` du jour où un
dashboard financier existera côté collaborateur.

---

## Comment retester manuellement

```powershell
# Devises et taux publics (page /reserver, sans authentification)
Invoke-RestMethod http://localhost:8036/api/open/currency/currencies
Invoke-RestMethod http://localhost:8036/api/open/currency/exchange-rates

# Création d'une société sans devise -> doit recevoir MAD automatiquement
$h = @{ Authorization = "Bearer <token admin>" }
$e = Invoke-RestMethod http://localhost:8036/api/admin/enterprise/ -Method Post -Headers $h `
  -ContentType application/json -Body (@{name="Test";phone="0600000000";address="Test"} | ConvertTo-Json)
(Invoke-RestMethod http://localhost:8036/api/admin/enterprise/ -Headers $h) |
  Where-Object { $_.id -eq $e.id } | Select-Object id, @{n="currency";e={$_.currency.code}}
# -> currency = MAD
```

Côté frontend : ouvrir `/reserver`, changer le sélecteur de devise → les prix se recalculent
sans rechargement de page. Ouvrir `/admin`, changer le sélecteur → "Revenu" et le graphique
mensuel se recalculent. Revenir sur MAD (ou vider `localStorage`) → affichage identique à avant
la fonctionnalité.

```powershell
# Synchronisation manuelle des taux (nécessite EXCHANGERATE_API_KEY définie côté backend)
Invoke-RestMethod http://localhost:8036/api/admin/currency/exchange-rates/sync -Method Post -Headers $h
# -> { success: true, message: "Taux mis a jour : [EUR, USD, GBP]", updatedRates: {...} }
```

Pour activer réellement l'automatisation : créer un compte gratuit sur
https://www.exchangerate-api.com/, récupérer la clé API, puis définir la variable
d'environnement `EXCHANGERATE_API_KEY` avant de lancer le backend (jamais dans un fichier
committé). Le bouton "Actualiser maintenant" sur `/admin/exchange-rates` et le job quotidien
(3h du matin) fonctionneront alors identiquement — même service `ExchangeRateSyncService`.

**Testé le 2026-08-26** : seed des 4 devises + 3 taux (idempotent au redémarrage), endpoints
publics `/api/open/currency/*` vérifiés, création d'Enterprise sans devise → MAD assignée
automatiquement (confirmé au niveau FK), mise à jour d'un taux via l'API admin (flux utilisé par
le nouvel écran `/admin/exchange-rates`) vérifiée de bout en bout. `npm run build` et
`mvnw clean package -DskipTests` passent après chaque étape.

**Testé le 2026-08-26 (automatisation)** : sans `EXCHANGERATE_API_KEY` → 502 propre, message
explicite, taux inchangés, pas de crash. Avec une clé invalide (vrai appel réseau) → l'API
externe répond 403, capté et traduit en 502 propre, taux inchangés. Le scénario de succès réel
(vraie clé, vraie mise à jour) n'a **pas** pu être testé — nécessite une clé ExchangeRate-API
que je ne peux pas générer moi-même (création de compte). À valider dès qu'une clé réelle est
disponible.
