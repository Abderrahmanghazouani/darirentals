# Dashboard : carte des propriétés + timeline du jour

Chantier "dashboard-carte-timeline" (branche `feature/dashboard-carte-timeline`). Remplace les 4
cartes de stats du haut du Dashboard (admin ET collaborateur - voir section dédiée plus bas) par
2 widgets : une carte interactive des propriétés et une timeline chronologique du jour.

---

## Étape 0 — Inspection (FAIT)

- Les 4 cartes de stats réelles (admin) : **"Chiffre d'affaires"**, **"Taux d'occupation"**,
  **"Réservations"**, **"Revenu net"** (`dict.dashboardHome.stat*`) - pas
  "Propriétés/Réservations/Clients/Revenu" comme supposé au départ. Corrigé et confirmé avant de
  commencer ; l'intention (remplacer les 4 cartes du haut) reste inchangée.
- `components/location-map.tsx` : composant Leaflet existant, **un seul marqueur** (sélecteur de
  position à la création/édition d'une propriété - `latitude`/`longitude`/`onPick`/`height`),
  monté via `components/location-map-dynamic.tsx` (`next/dynamic`, `ssr:false` - Leaflet a besoin
  du DOM navigateur). Réutilisé et **étendu** (pas recréé) avec un mode lecture seule
  multi-marqueurs (prop `markers`), le mode single-marker existant reste identique et intact pour
  `property-form.tsx` (admin + collaborateur, vérifié aucune régression).

## Widget 1 — Carte des propriétés

- [`lib/dashboard/property-map.ts`](nextjs-app-FINAL/nextjs-app/lib/dashboard/property-map.ts) :
  calcule le statut (vert/ambre/gris) de chaque propriété à partir des réservations déjà chargées
  - aucun nouveau champ, aucun appel API. Statut "Confirmee" (code seedé) + fenêtre de 3 jours
    pour "ending-soon".
- **Trouvaille pendant les tests avec de vraies données** : 4 propriétés sur 5 ont
  `latitude=0, longitude=0` en base, PAS `null` ("Null Island", au large du Golfe de Guinée).
  `p.latitude != null` ne suffisait donc pas comme filtre - `(0, 0)` explicitement exclu en plus
  (aucune propriété réelle n'est légitimement à cette position). Sans ce correctif, la carte
  zoomait pour englober l'Atlantique entier au lieu de centrer sur la seule vraie position.
- Composant [`components/dashboard/property-map-card.tsx`](nextjs-app-FINAL/nextjs-app/components/dashboard/property-map-card.tsx) :
  Card (pas de titre dans le composant, le wrapper n'a pas de CardHeader - conforme au cahier des
  charges), légende sous la carte (3 puces), état vide si aucune propriété localisée.
- Couleurs des marqueurs en `var(--success)` / `var(--warning)` / `var(--muted-foreground)` -
  pas de valeur figée : reste correct si le thème change pendant que le dashboard est déjà
  affiché, sans recalcul JS (le navigateur résout la variable CSS à chaque peinture).
- Popup au clic : construit via `document.createElement`/`textContent` (jamais de HTML brut
  interpolé - aucune injection possible même avec un nom de propriété qui contiendrait des
  caractères spéciaux), nom de la propriété + lien.
- **Bug de thème trouvé et corrigé** : le popup Leaflet garde par défaut un fond blanc et du
  texte noir codés en dur dans `leaflet.css`, ignorant le thème sombre de l'app. Ajouté dans
  `app/globals.css` : `.leaflet-popup-content-wrapper` / `.leaflet-popup-tip` sur
  `var(--popover)`/`var(--popover-foreground)`. Vérifié clair et sombre.

## Widget 2 — Timeline du jour

- [`lib/dashboard/day-timeline.ts`](nextjs-app-FINAL/nextjs-app/lib/dashboard/day-timeline.ts) :
  arrivées (`checkInDate == aujourd'hui`, non annulée), départs (`checkOutDate == aujourd'hui`,
  non annulée), tâches à échéance (`dueDate == aujourd'hui`, statut != Terminee).
- **Pas de tri par heure** : contrairement à ce que le cahier des charges envisageait ("si
  disponible"), aucune des trois entités n'a de composante horaire en base
  (`Reservation.checkInDate`/`checkOutDate` et `Task.dueDate` sont des `LocalDate` côté backend,
  pas des `LocalDateTime`) - vérifié dans les DTOs avant de coder. Ordre fixe : arrivées, départs,
  tâches, conforme au repli prévu par le cahier des charges pour ce cas.
- Composant [`components/dashboard/day-timeline-card.tsx`](nextjs-app-FINAL/nextjs-app/components/dashboard/day-timeline-card.tsx) :
  même gabarit visuel que `ActionCenterCard` (icône + pastille de couleur + libellé, ligne
  cliquable via `router.push`, état vide avec icône `Coffee` + message encourageant plutôt que
  "Aucune donnée").
- 3 icônes lucide-react distinctes (`LogIn`/`LogOut`/`ClipboardList`) + pastille colorée
  (`bg-success`/`bg-info`/`bg-warning`) par type d'événement.

## Intégration (Étape 3)

- `app/admin/page.tsx` : grid `grid-cols-1 sm:grid-cols-2` (2 blocs, même largeur totale que les
  4 cartes précédentes) remplace la grid `sm:grid-cols-2 lg:grid-cols-4`. `stats` (useMemo) réduit
  à `recentReservations` (seul champ encore utilisé ailleurs dans la page) - `totalRevenue`,
  `netRevenue`, `totalReservations`, `upcomingReservations`, `activeProperties`, `totalClients`
  et la fonction `todayIso()` locale, tous devenus morts avec la suppression des 4 cartes,
  supprimés proprement plutôt que laissés en code mort. `StatCard`/`Wallet`/`Home`/`ClipboardList`
  (imports) retirés, plus utilisés dans ce fichier.
- Health Score, Revenue Intelligence, Property Performance, Action Center, assistant IA :
  **non touchés**, toujours alimentés par les mêmes données.

## Dashboard collaborateur (ajouté après coup, sur demande explicite)

Le cahier des charges initial ne visait explicitement que `app/admin/page.tsx` - une fois le
Dashboard admin validé, demande de reproduire à l'identique côté `app/collaborator/page.tsx`
(qui avait bien gardé les 4 anciennes cartes, comme repéré par abdo).

- Mêmes deux composants réutilisés tels quels, avec une prop `role?: "admin" | "collaborator"`
  ajoutée à `PropertyMapCard`/`DayTimelineCard` (et propagée dans `computeDayTimeline`) pour que
  les liens (popup carte, lignes de la timeline) pointent vers les bonnes routes
  (`/collaborator/reservations`, `/collaborator/tasks`) plutôt que `/admin/...` en dur.
- **Aucune fiche de rentabilité par propriété côté collaborateur** (vérifié : pas de route
  `/collaborator/property/[id]/rentabilite`) - le popup de la carte renvoie vers la liste des
  propriétés (`/collaborator/property`) avec un libellé différent ("Voir la propriété" plutôt que
  "Voir la rentabilité", nouvelle clé i18n `propertyMap.viewProperty`).
- **Cloisonnement par société respecté** : les deux widgets reçoivent `scopedProperties`/
  `scopedReservations`/`scopedTasks` (déjà passés par `filterByEnterprise` plus haut dans le
  fichier, exactement comme le reste du dashboard collaborateur) - jamais les données brutes.
  Vérifié avec le compte réel `retest_a_subadmin` (société "Dar Atlas Hospitality") : la carte
  et la timeline ne montrent que les propriétés/réservations de cette société, pas celles
  d'"abdo" ou de "Bleu Ourika Collection".
- Même nettoyage de code mort que côté admin (`stats` réduit à `recentReservations`,
  `scopedClients` devenu inutilisé et retiré, `StatCard`/`Wallet`/`Home`/`ClipboardList`/
  `CANCELLED_STATUS_CODE`/`todayIso()` retirés).

## Bug trouvé et corrigé pendant les tests mobile (375px) - les deux dashboards

Après l'intégration initiale, débordement horizontal détecté à 375px sur les DEUX dashboards
(mesuré : `document.body.scrollWidth` 653px vs `clientWidth` 375px). **Même cause racine que le
bug recharts déjà documenté dans NOTES-nettoyage.md, point 2** : `PropertyMapCard`/
`DayTimelineCard` sont des items directs de la nouvelle grille `grid-cols-1 sm:grid-cols-2` - un
item de grille CSS a `min-width:auto` par défaut (taille minimale basée sur son contenu), pas 0,
même en une seule colonne sur mobile. La carte Leaflet (comme le graphique recharts avant elle)
refusait de rétrécir sous sa largeur interne, forçant toute la page à déborder. Corrigé en
ajoutant `min-w-0` directement sur le `<Card>` racine de chacun des deux composants (donc valable
partout où ils sont utilisés, pas seulement dans ces deux pages). Revérifié : `scrollWidth` =
`clientWidth` = 375 sur les deux dashboards après correction.

## Observation, non traitée comme un bug : `scrollWidth` gonflé par Leaflet

Après le correctif ci-dessus, une mesure isolée a de nouveau montré `document.documentElement.
scrollWidth` supérieur à `clientWidth` (587 vs 375) **uniquement** sur le Dashboard admin **et
uniquement quand une carte Leaflet avec un vrai marqueur est montée** (jamais sur le Dashboard
collaborateur de test, qui affiche l'état vide "Aucune propriété localisée" - donc aucune
instance Leaflet créée). Vérifié avant de conclure à un bug :
- Capture d'écran à 375px réellement propre, aucun contenu coupé, aucune barre de défilement
  visible.
- `window.scrollTo(300, 0)` puis relecture de `window.scrollX` : reste à 0 - **la page n'est pas
  réellement défilable horizontalement**, quoi que rapporte `scrollWidth`.

Conclusion : artefact de mesure connu de Leaflet (ses éléments internes d'animation de zoom sont
positionnés à des coordonnées très éloignées, comptés par certains moteurs de rendu dans le calcul
de `scrollWidth` du document sans jamais produire de défilement réel ni rien d'affiché à
l'utilisateur). Pas de correctif appliqué - il n'y a rien à corriger, et retoucher le CSS pour
faire taire cette seule métrique risquerait d'introduire un vrai problème pour en résoudre un
faux. Si `scrollWidth` est revérifié un jour sur une page avec carte Leaflet, ne pas s'arrêter à
ce chiffre seul - confirmer par `scrollTo` + capture d'écran avant de conclure à un débordement.

## ⚠️ À éviter à l'avenir : lecture de dates en base via un script Node/mysql2

**Incident survenu pendant ce chantier, corrigé avant de continuer (voir la discussion de
session pour le détail complet)** : en modifiant des données de test via un script Node
(`mysql2`) pour une vérification, une lecture ultérieure des mêmes dates VIA UN AUTRE SCRIPT NODE
a semblé montrer un décalage d'un jour par rapport à ce que l'écran de l'application affichait
réellement à abdo. Cause : `mysql2` désérialise les colonnes `DATE` MySQL en objets JS `Date`
**dans le fuseau horaire local du process Node**, puis `JSON.stringify`/`toISOString()` les
reconvertit en UTC pour l'affichage - un `DATE` MySQL stocké sans heure (`'2026-09-14'`) peut
ainsi ressortir comme `"2026-09-13T23:00:00.000Z"` ou `"2026-09-14T23:00:00.000Z"` selon le
fuseau de la machine, alors que la vraie valeur stockée et celle affichée par l'app (qui lit la
même donnée via l'API Java/Spring, pas via ce script) n'ont **aucun décalage réel** entre elles.

**Résolu par comparaison croisée** plutôt que par confiance dans le script : les tâches/
réservations jamais modifiées ("témoins") ont servi de référence - en comparant la valeur "témoin"
affichée par l'app à la valeur affichée par l'app pour l'élément corrigé, sans jamais faire
confiance à ce que le script Node rapportait pour l'un ou l'autre.

**Règle à suivre la prochaine fois qu'une vérification de date en base est nécessaire** :
1. Ne jamais conclure sur une date à partir de la seule lecture brute d'un script Node/mysql2 -
   toujours vérifier via l'écran réel de l'application (qui passe par le backend Java, pas par ce
   script) avant d'agir dessus.
2. Si une comparaison entre plusieurs enregistrements est nécessaire et qu'aucun des deux n'a pu
   être vérifié via l'app, chercher un enregistrement "témoin" jamais modifié partageant la même
   valeur d'origine, et comparer sa valeur affichée par l'app à celle de l'élément en question -
   ne jamais deviner un décalage de jour, le mesurer par comparaison directe.
3. Pour ÉCRIRE une date sans ambiguïté depuis un script Node, une chaîne `'YYYY-MM-DD'` passée
   telle quelle à la requête SQL n'a pas ce problème (vérifié : les 3 corrections de ce chantier
   utilisant ce format sont arrivées correctement, confirmées via l'écran réel) - le problème est
   spécifique à la LECTURE (désérialisation JS Date), pas à l'écriture.

## Vérifié

- Widget 1 et Widget 2 testés séparément avec de vraies données avant intégration, comme demandé
  par la méthode.
- Clair et sombre : marqueurs, popup, légende, timeline - tous cohérents dans les deux thèmes
  (popup Leaflet spécifiquement corrigé, voir plus haut).
- Mobile 375px : les deux dashboards, débordement trouvé et corrigé (voir plus haut), zéro
  débordement réel après correctif (confirmé par `scrollTo`, pas seulement par `scrollWidth`).
- Dashboard admin ET collaborateur (compte réel `retest_a_subadmin`, société "Dar Atlas
  Hospitality") : données correctement cloisonnées par société, aucun mélange.
- Aucune régression : `property-form.tsx` (admin + collaborateur, mode single-marker de
  `LocationMap`) toujours fonctionnel, Health Score/Revenue Intelligence/Property Performance/
  Action Center/assistant IA inchangés.
- `npm run build` : passe à chaque étape majeure (26 routes générées, TypeScript propre).
