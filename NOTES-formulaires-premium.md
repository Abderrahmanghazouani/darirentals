# Formulaires premium — DariRentals

Suivi du chantier "Formulaires premium" (branche `feature/formulaires-premium`). Refonte de
tous les formulaires de création/édition : panneau latéral (Sheet) comme conteneur commun,
wizard en étapes à l'intérieur pour les deux formulaires les plus longs. Chantier frontend.

---

## Étape 0 — Inspection

- **Architecture centralisée** : un seul wrapper,
  [`components/crud/entity-form-dialog.tsx`](nextjs-app-FINAL/nextjs-app/components/crud/entity-form-dialog.tsx)
  (`EntityFormDialog`, props `open`/`onOpenChange`/`title`/`children`), utilisé par les 7
  formulaires demandés **et** les écrans génériques `/admin/[entity]`. Changer son intérieur
  (Dialog → Sheet) bénéficie à tout, sans toucher aux ~14 fichiers `page.tsx` appelants.
- Hook générique [`lib/use-entity-crud.ts`](nextjs-app-FINAL/nextjs-app/lib/use-entity-crud.ts)
  (état ouverture/édition/sauvegarde) : logique inchangée, une seule addition (voir "bug
  wizard" plus bas).
- [`components/ui/sheet.tsx`](nextjs-app-FINAL/nextjs-app/components/ui/sheet.tsx) : déjà
  présent (drawer mobile de la sidebar, `side="left"`), mais sans `SheetHeader`/`SheetFooter`
  (seulement `Title`/`Description`) - à compléter, sans toucher au `side="left"`.
- **Validation par formulaire** :
  - RHF + zod : Property, Client, Collaborator, Charge, Payment, Task.
  - **Reservation : PAS de RHF/zod** — `useState` + validation manuelle (`errors`) + appel
    async `checkAvailability` avant soumission. Sa logique de validation n'a pas été touchée,
    uniquement son conteneur et sa mise en page.

---

## Conteneur commun — Sheet

- `EntityFormDialog` rend désormais un `Sheet` `side="right"`, **520px** en desktop
  (`w-full sm:max-w-[520px]`, dans la fourchette 480-560 demandée - une seule taille pour tous
  plutôt que deux selon le formulaire, pour la cohérence visuelle), **plein écran sous le
  breakpoint `sm`** (`w-full` prend le dessus). Nom du composant et signature des props
  inchangés : zéro modification des appelants.
- `SheetContent` reçoit `flex flex-col p-0 gap-0` ; chaque formulaire gère sa mise en page
  interne en 3 zones :
  1. `SheetHeader` (titre) — ou, pour les wizards, header titre + une 2ᵉ barre avec le stepper.
  2. Zone de champs `flex-1 overflow-y-auto px-6 py-5` (seule cette zone défile).
  3. `SheetFooter` **sticky** (`shrink-0 border-t bg-background`) avec Annuler/Enregistrer
     toujours visibles.
- `SheetHeader` et `SheetFooter` ajoutés à `sheet.tsx` (miroir de `dialog.tsx`). `SheetFooter`
  a un style dédié (bordure haute + fond + `shrink-0`) que `DialogFooter` (simple flex) n'avait
  pas - c'est ce qui le rend "collé en bas" quand les champs défilent derrière.
- L'overlay `bg-black/50` existant fournit l'assombrissement de l'arrière-plan demandé.

---

## Wizard en étapes — Property et Collaborator

Composant [`components/crud/form-stepper.tsx`](nextjs-app-FINAL/nextjs-app/components/crud/form-stepper.tsx)
(`FormStepper`) : pastilles numérotées reliées par un trait, étape courante en `--primary`,
étapes validées avec une coche. Purement visuel - la navigation (Précédent/Suivant, validation
par étape) est locale à chaque formulaire (pas de moteur de wizard générique pour seulement
2 formulaires).

### Property — 3 étapes
1. **Informations générales** : nom, type, statut, société.
2. **Localisation** : adresse (n°/rue/CP), ville, latitude/longitude, carte interactive
   (`LocationMap`, composant inchangé) - le clic sur la carte remplit lat/lng, le marqueur
   recentre sur les coordonnées en édition.
3. **Détails** : capacité, prix/nuit **+ récapitulatif** de tous les champs (replié en bas de
   cette dernière étape, pas une 4ᵉ étape séparée - le cahier des charges nomme exactement
   3 étapes).

### Collaborator — 2 étapes
1. **Informations personnelles** : nom, email, téléphone, identifiant, mot de passe, compte
   actif.
2. **Rattachement** : société, rôle, et si rôle = Gestionnaire, la liste des propriétés
   autorisées (apparaît/disparaît dynamiquement selon le rôle) **+ récapitulatif** (la ligne
   "Propriétés" du récap n'apparaît que pour un Gestionnaire).

### Validation par étape
`form.trigger([...champs RHF de l'étape])` avant d'autoriser "Suivant" - seuls les champs
réellement contraints par le schéma zod bloquent :
- Property étape 1 : `name` (seul champ requis du schéma). Type/statut/société sont des `<Select>`
  en état local, sans contrainte zod aujourd'hui - **on n'a pas ajouté de nouvelle règle
  obligatoire** sur des champs jusque-là optionnels.
- Collaborator étape 1 : `name`, `email`, `username`. Société/rôle restent facultatifs comme
  avant (membership à `null` si l'un manque).

Le bouton "Suivant" est `type="button"` avec `onClick={goNext}` ; la soumission n'est possible
que via le bouton "Enregistrer" `type="submit"` de la dernière étape, doublé d'un garde-fou
`if (!isLastStep) return;` dans le handler `onSubmit`.

### Bug trouvé et corrigé pendant les tests : l'étape ne repartait pas de zéro

Constaté en testant Collaborator : après une création (terminée à la dernière étape), rouvrir
le formulaire pour éditer une autre ligne l'affichait **encore sur la dernière étape**. Cause :
le `Sheet` ne démonte pas toujours son contenu assez vite entre deux ouvertures qui
s'enchaînent (animation de fermeture encore en cours), donc l'instance React du formulaire —
et son `useState(step)` — survivait d'une ouverture à l'autre.

**Correctif** : `useEntityCrud` expose un compteur `formSession` incrémenté à **chaque**
`openCreate()`/`openEdit()`. Les pages passent `key={crud.formSession}` au composant de
formulaire → remontage garanti à chaque ouverture → `step` (et l'état react-hook-form) repartent
toujours de zéro, y compris pour l'enchaînement "créer → rouvrir en création". Appliqué aux
3 sites wizard (admin/property, collaborator/property, admin/collaborator).

---

## Formulaires courts — Sheet à une seule étape

Composant [`components/crud/form-section.tsx`](nextjs-app-FINAL/nextjs-app/components/crud/form-section.tsx)
(`FormSection`) : petit titre en capitales discrètes + espacement cohérent.

- **Client** : "Identité" / "Rattachement & accès".
- **Charge** : "Informations" / "Montant" / "Paiement associé".
- **Payment** : "Informations" / "Charges couvertes" (liste de cases à cocher inline).
- **Task** : "Informations" / "Planification" / "Classification". La grille 3 colonnes
  (Type/Priorité/Statut) passe en 1 colonne sous `sm` (`grid-cols-1 sm:grid-cols-3`) - seule
  amélioration de mise en page mobile ajoutée, le reste des grilles 2 colonnes reste lisible
  à 375px.
- **Reservation** : "Client & logement" / "Séjour" / "Canal". Jugée **à une seule étape** -
  nombre de champs comparable aux autres formulaires courts, loin de la complexité de Property.
  Sa validation manuelle et son `checkAvailability` async sont intacts.

Le générique [`components/crud/auto-form.tsx`](nextjs-app-FINAL/nextjs-app/components/crud/auto-form.tsx)
(écrans `/admin/[entity]` : ChargeType, PaymentType…) a reçu la même structure de conteneur
(zone défilante + `SheetFooter`) pour ne pas être cassé visuellement dans le nouveau Sheet,
mais sans regroupement en sections (ses champs sont dynamiques) - hors périmètre explicite du
chantier. Le formulaire public `/reserver` garde son `Dialog` propre (hors périmètre).

---

## Tests (bout en bout, clair/sombre, mobile)

Pilotés par le DOM (le navigateur automatisé a un décalage de référentiel entre ses clics
coordonnées et l'inspection DOM, sans rapport avec le code livré).

| Formulaire | Création réelle | Édition réelle | Suppression | Clair | Sombre | Mobile |
|---|---|---|---|---|---|---|
| **Property** (wizard 3) | ✅ (+ carte, validation étape, récap) | ✅ (pré-remplissage des 3 étapes, carte recentrée) | ✅ | ✅ | ✅ | ✅ plein écran |
| **Task** | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ plein écran |
| **Charge** | ✅ | ✅ (250→999) | ✅ | ✅ | — (conteneur commun) | — |
| **Payment** | ✅ (300, notes) | ✅ (300→777) | ✅ | ✅ | — | — |
| **Reservation** | ✅ (RES-…, 4 nuits calculées, `checkAvailability` OK) | ✅ (montant 1200) | ✅ | ✅ | ✅ | — |
| **Collaborator** (wizard 2) | ✅ (validation étape 1 = 3 "Requis", rôle Gestionnaire → section propriétés dynamique, récap) | ✅ (pré-remplissage 2 étapes + membership rechargée, navigation Précédent/Suivant) | ⚠️ bloquée par un **500 backend** (`DELETE /api/admin/collaborator/id/{id}`) | ✅ | — | — |
| **Client** | ⚠️ **500 backend** (`POST /api/admin/client/`) | non testée (création bloquée) | — | ✅ (UI/sections/validation OK) | ✅ | — |

`npm run build` : passe à chaque étape (26 routes, TypeScript propre).

### Points backend à signaler (hors périmètre de ce chantier frontend)

- `POST /api/admin/client/` renvoie **500** même avec tous les champs remplis + société
  sélectionnée. Le formulaire Client construit un DTO **identique octet pour octet** à la
  version précédente (logique `handleSubmit` copiée telle quelle) - le 500 est donc préexistant
  et propre au backend, pas introduit par la refonte.
- `DELETE /api/admin/collaborator/id/{id}` renvoie **500** - un collaborateur de test
  ("Collab Test E2E MODIFIE") n'a pas pu être nettoyé via l'UI pour cette raison. Donnée de
  test inoffensive.

---

## Fichiers

**Nouveaux** : `components/crud/form-stepper.tsx`, `components/crud/form-section.tsx`.

**Modifiés** :
- `components/ui/sheet.tsx` — `SheetHeader` + `SheetFooter`.
- `components/crud/entity-form-dialog.tsx` — Dialog → Sheet.
- `lib/use-entity-crud.ts` — compteur `formSession`.
- `app/admin/property/property-form.tsx` (+ copie `app/collaborator/property/`) — wizard 3 étapes.
- `app/admin/collaborator/collaborator-form.tsx` — wizard 2 étapes.
- `app/admin/client/client-form.tsx`, `components/charges/charge-form.tsx`,
  `components/payments/payment-form.tsx`, `components/tasks/task-form.tsx`,
  `components/reservations/reservation-form.tsx`, `components/crud/auto-form.tsx` — conteneur
  Sheet + regroupement en sections.
- `app/admin/property/page.tsx`, `app/collaborator/property/page.tsx`,
  `app/admin/collaborator/page.tsx` — `key={crud.formSession}` sur le formulaire.
