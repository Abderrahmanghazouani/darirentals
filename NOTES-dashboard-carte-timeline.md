# Dashboard — carte des propriétés + timeline du jour

Chantier "dashboard-carte-timeline" (branche `feature/dashboard-carte-timeline`). Remplace les
4 cartes de stats du haut du Dashboard admin par 2 widgets, même largeur totale de section.

---

## Étape 0 — Inspection (FAIT)

- Les "4 cartes" à remplacer, dans `app/admin/page.tsx` (`grid grid-cols-1 sm:grid-cols-2
  lg:grid-cols-4`) : **pas** "Propriétés/Réservations/Clients/Revenu" comme décrit dans la
  demande initiale - en réalité "Chiffre d'affaires" (`statRevenue`), "Taux d'occupation"
  (`statOccupancy`, jamais implémenté - `value="—"` codé en dur, TODO déjà présent), "Réservations"
  (`statReservations`), "Revenu net" (`statNetRevenue`). Signalé, puis remplacées quand même :
  l'intention (remplacer les 4 cartes du haut par 2 widgets) était sans ambiguïté.
- `components/location-map.tsx` + `location-map-dynamic.tsx` : composant Leaflet existant,
  conçu pour un **seul** marqueur avec sélection au clic (`latitude`/`longitude`/`onPick`),
  utilisé par `property-form.tsx` (admin et collaborateur) à la création/édition d'une
  propriété. Pas fait pour afficher plusieurs marqueurs colorés en lecture seule tel quel -
  **étendu** (pas recréé) avec un mode `markers` optionnel, rétrocompatible : sans cette prop,
  le composant se comporte exactement comme avant (vérifié, `property-form.tsx` non touché et
  non impacté).
- Chantier scopé à `app/admin/page.tsx` uniquement, comme demandé explicitement -
  `app/collaborator/page.tsx` non touché (a une structure de cartes de stats similaire mais pas
  identique ; à faire dans un chantier séparé si souhaité).

## Widget 1 — Carte des propriétés

- [`lib/dashboard/property-map.ts`](nextjs-app-FINAL/nextjs-app/lib/dashboard/property-map.ts) :
  fonction pure `computePropertyMapPoints(properties, reservations)`, calcule le statut
  (`active`/`ending-soon`/`idle`) à partir des réservations réelles déjà chargées (code de statut
  `"Confirmee"`, fenêtre "bientôt terminé" = 3 jours, mêmes conventions que
  `action-center.ts`/`health-score.ts`).
- **Bug de données réel trouvé en testant** (pas un bug de code) : 4 propriétés sur 5 ont
  `latitude`/`longitude` = **`0/0`** en base, pas `null` - "Null Island", au large du golfe de
  Guinée. Le filtre initial (`!= null`) les traitait comme des positions valides, forçant la
  carte à zoomer sur tout l'Atlantique pour rien. Corrigé : `(0, 0)` traité comme "pas de
  position", au même titre que `null`.
- [`components/dashboard/property-map-card.tsx`](nextjs-app-FINAL/nextjs-app/components/dashboard/property-map-card.tsx) :
  Card + carte Leaflet (mode `markers`) + légende. Couleurs des pastilles en `var(--success)` /
  `var(--warning)` / `var(--muted-foreground)` (pas de hex figé) - reste correct si l'utilisateur
  bascule clair/sombre pendant que le dashboard est déjà ouvert, sans recalcul JS.
- Popup au clic : construit via DOM (`document.createElement`, `textContent`) plutôt qu'une
  chaîne HTML injectée - nom de la propriété + lien `<a>` natif vers
  `/admin/property/{id}/rentabilite` (navigation complète du navigateur, pas de `router.push` -
  cohérent avec le reste de l'app, ex. "Retour à l'accueil" sur `/login`).
- **Bug de style trouvé en testant** : les popups Leaflet gardent un fond blanc et du texte noir
  codés en dur par `leaflet.css`, ignorant totalement le thème sombre. Corrigé dans
  `app/globals.css` avec des règles globales `.leaflet-popup-content-wrapper`/`.leaflet-popup-tip`
  sur `var(--popover)`/`var(--popover-foreground)`.
- Testé : marqueur unique visible (seule `Riad Zahra` a une vraie position), couleur grise
  correcte (aucune réservation active au moment du test), popup + lien vers la rentabilité
  fonctionnel (navigation confirmée), clair et sombre, desktop et mobile (375px, zéro
  débordement horizontal mesuré).

## Widget 2 — Timeline du jour

- [`lib/dashboard/day-timeline.ts`](nextjs-app-FINAL/nextjs-app/lib/dashboard/day-timeline.ts) :
  fonction pure `computeDayTimeline(reservations, tasks)` - arrivées (`checkInDate` = aujourd'hui,
  non annulée), départs (`checkOutDate` = aujourd'hui, non annulée), tâches à échéance (`dueDate`
  = aujourd'hui, statut != `Terminee`).
- **Pas de tri par heure** : le cahier des charges l'envisageait "si disponible" - vérifié dans
  les DTOs (`ReservationDto`, `TaskDto`) qu'aucune des trois dates n'a de composante horaire
  (dates seules côté backend). Ordre fixe : arrivées, départs, tâches.
- [`components/dashboard/day-timeline-card.tsx`](nextjs-app-FINAL/nextjs-app/components/dashboard/day-timeline-card.tsx) :
  même gabarit visuel que `ActionCenterCard` (ligne cliquable `router.push`, icône + pastille de
  couleur par type, état vide avec icône) pour rester cohérent avec le reste du dashboard. Icônes
  lucide-react : `LogIn` (arrivée, vert), `LogOut` (départ, bleu/info), `ClipboardList` (tâche,
  ambre). État vide : icône `Coffee` + message encourageant, pas juste "Aucune donnée".
- Testé avec de vraies données (voir section suivante pour l'incident et sa résolution) : les 3
  types de lignes s'affichent avec la bonne icône/couleur/libellé, navigation vers `/admin/
  reservations` et `/admin/tasks` confirmée. État vide (cas réel actuel : aucun événement le jour
  du test) vérifié en clair et sombre, desktop et mobile.

## Intégration finale

`app/admin/page.tsx` : les 4 `StatCard` remplacées par
`grid grid-cols-1 gap-4 sm:grid-cols-2` contenant `PropertyMapCard` et `DayTimelineCard` (2 blocs
au lieu de 4, même largeur totale de section, conforme à la demande). Nettoyage en cascade
(conséquence directe du retrait des 4 cartes, pas un changement de périmètre) : le `useMemo`
`stats` calculait 6 champs (`totalRevenue`, `netRevenue`, `activeProperties`,
`totalReservations`, `upcomingReservations`, `totalClients`) devenus tous morts sauf
`recentReservations` (utilisé par la liste "Réservations récentes" plus bas) - simplifié en un
seul `useMemo` dédié. Import `CANCELLED_STATUS_CODE` et fonction `todayIso()` du fichier
devenus inutilisés, retirés. Rien d'autre du dashboard touché (Health Score, Revenue
Intelligence, Property Performance, Action Center, Réservations récentes, Calendrier, assistant
IA) - tous revérifiés fonctionnels après coup.

Nouvelles clés i18n ajoutées (FR + EN) : `propertyMap.*` et `dayTimeline.*` dans
`lib/i18n/translations.ts`, même convention que les sections existantes (`propertyPerformance`,
`actionCenter`).

## Incident de session : décalage de fuseau horaire sur les lectures de date via mysql2/Node

**Contexte** : pour vérifier le rendu du Widget 2 avec de vraies données (aucun événement réel
le jour du test), une modification temporaire de 3 dates a été faite directement en base
(reservation `checkInDate`/`checkOutDate`, `task.dueDate`), avec l'intention de tout restaurer
immédiatement après capture d'écran.

**Ce qui a mal tourné** : la restauration via un script Node (`mysql2`) a utilisé des dates lues
par ce même script comme référence - qui se sont révélées **décalées d'un jour** par rapport à
ce que l'application affiche réellement. Deux tentatives de correction successives ont chacune
introduit un nouveau décalage d'un jour, sans le corriger.

**Cause probable** : `mysql2` restitue les colonnes `DATE` sous forme d'objets `Date` JS
construits en interprétant la valeur stockée comme un **minuit local** (fuseau du process
Node), puis toute sérialisation ultérieure (`JSON.stringify`, `.toISOString()`) reconvertit cet
objet en UTC - ce qui décale l'affichage d'un jour si le fuseau local n'est pas UTC. **La vraie
valeur stockée en base n'a jamais été fausse** - c'est uniquement la restitution par ce script de
lecture qui trompe l'œil. Aucun signe que l'application Spring Boot/le frontend aient le même
problème (ils affichent les dates correctement, confirmé abondamment tout au long de cette
session).

**Résolu** en abandonnant complètement le script Node comme source de vérité pour cette
vérification, et en comparant à la place avec des enregistrements **jamais touchés** partageant
la même donnée d'origine (`Tache Prop5 non accessible`/`Tache Prop2 accessible`, seedées avec la
même échéance que la tâche modifiée) lues **via l'écran réel de l'application** - la seule source
fiable. Les 3 valeurs ont été restaurées et reconfirmées par ce biais avant de continuer.

**Règle à suivre la prochaine fois** (pour moi-même, Claude, ou quiconque reprend ce chantier) :

> ⚠️ **Ne jamais utiliser un script Node/`mysql2` (ou tout script one-off similaire) comme
> référence pour LIRE une date `DATE`/`DATETIME` déjà en base dans le but de la comparer ou de
> la restaurer.** Toujours passer par l'écran réel de l'application (ou l'API backend
> directement, `curl` sur un endpoint authentifié) pour obtenir la valeur "vraie" telle que
> l'utilisateur la voit. Un script Node reste parfaitement fiable pour des `INSERT`/`UPDATE`
> avec une chaîne de date explicite (`'YYYY-MM-DD'`) ou pour lire des colonnes non temporelles -
> le problème est spécifique à la restitution de valeurs temporelles par le driver, pas à
> l'écriture ni à la base elle-même.
