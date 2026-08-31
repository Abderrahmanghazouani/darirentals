# Sidebar premium — DariRentals

Suivi du chantier "sidebar premium" (branche `feature/sidebar-premium`). Chantier 100% frontend,
sur `components/app-shell.tsx` (partagé admin/collaborateur) et ses deux layouts.

Aucune image de référence n'est arrivée avec la demande - implémentation faite à partir de la
description texte, très détaillée (barre colorée + fond teinté sur l'item actif, tooltips en
mode replié, carte profil, toggle en bas).

---

## Étape 0 — Inspection (FAIT)

- `components/app-shell.tsx` (construit par Anas, PR #28) : une seule structure `NavSection[]`
  (titre + items), sidebar fixe 248px, jamais repliable. `ThemeToggle`/`LanguageToggle` vivaient
  dans la topbar (ajoutés lors du chantier `mode-sombre`), pas dans la sidebar.
- **Aucun menu mobile n'existe actuellement** : l'`<aside>` est `hidden lg:flex` - en dessous de
  1024px (pas 768px), la sidebar disparaît purement et simplement, sans aucun remplacement
  (pas de hamburger, pas de drawer). Vérifié en direct au navigateur avant de commencer -
  contredit l'hypothèse de départ ("comme actuellement"). Signalé à abdo, qui a choisi de
  construire le drawer mobile dans ce chantier plutôt que de documenter la dette.
- Composants shadcn manquants, à installer : `tooltip.tsx` (+ `@radix-ui/react-tooltip`, absent
  de `package.json`), `sheet.tsx` (repose sur `@radix-ui/react-dialog`, déjà présent - pas de
  nouvelle dépendance pour celui-là).
- Nav existante décalée par rapport aux vraies routes : `/admin/financial-reports`,
  `/admin/exchange-rates`, `/admin/reservation-requests`, `/admin/collaborator`,
  `/admin/payments`, `/admin/tasks` existent et fonctionnent (testés lors du chantier
  `mode-sombre`) mais n'étaient reliés nulle part dans `app/admin/layout.tsx` - remplacés par un
  faux groupe "Rapports" avec des liens désactivés ("Rapports", "Paramètres", jamais implémentés).

## Structure des modules (routes vérifiées une à une avant utilisation)

Groupes définis avec abdo, routes confirmées via `find app/admin -name page.tsx` /
`find app/collaborator -name page.tsx` avant d'écrire le moindre lien.

| Groupe | Admin | Collaborateur |
|---|---|---|
| Vue d'ensemble | Tableau de bord → `/admin` | Tableau de bord → `/collaborator` |
| Opérations | Propriétés, Réservations, **Demandes de réservation**, Tâches | Propriétés, Réservations, Tâches |
| Finances | Charges, Paiements, Rapports financiers, Taux de change | Charges, Paiements |
| Équipe | Collaborateurs, Clients | *(absent - pas de route équivalente)* |

- **"Demandes de réservation" ajouté côté admin**, en plus de la liste d'origine du cahier des
  charges : module réel et fonctionnel (`/admin/reservation-requests`, confirmations/refus de
  demandes), absent par oubli de la nav précédente plutôt que volontairement exclu - signalé, à
  retirer si non souhaité.
- Côté collaborateur, "Finances" et "Équipe" réduits aux seules routes qui existent réellement
  sous `/collaborator/**` (vérifié : pas de `/collaborator/client`, `/collaborator/collaborator`,
  `/collaborator/exchange-rates` ni `/collaborator/financial-reports`).
- Le tableau de bord reste un item unique : Health Score, Revenue Intelligence et l'assistant IA
  sont déjà intégrés dans la page `/admin` et `/collaborator` elles-mêmes (pas des liens séparés)
  - rien à changer là, juste une confirmation du cahier des charges.
- Ancien faux groupe "Rapports" (liens désactivés grisés "Rapports"/"Paramètres", jamais
  implémentés) supprimé - conforme à "uniquement les VRAIS modules construits et fonctionnels".

### "Tous les modules"

Un seul lien discret tout en bas de la nav (pas un groupe, icône `LayoutGrid`, texte atténué),
vers une nouvelle page dédiée listant les 37 entités de référence :
[`app/admin/modules/page.tsx`](nextjs-app-FINAL/nextjs-app/app/admin/modules/page.tsx) et
[`app/collaborator/modules/page.tsx`](nextjs-app-FINAL/nextjs-app/app/collaborator/modules/page.tsx)
(grille de liens vers `/admin/${entité}` / `/collaborator/${entité}`, réutilise `entityRegistry`).
Remplace fonctionnellement le bloc `<details>` équivalent déjà présent en bas du tableau de bord
collaborateur (laissé en place, redondant mais inoffensif, hors scope de ce chantier).

## Fonctionnalités de la sidebar

1. **Repliable/dépliable** - bouton chevron à côté du logo, état persisté
   (`localStorage["sidebar-collapsed"]`), lu après montage pour éviter tout mismatch
   d'hydratation (même logique que `ThemeToggle`/le choix de thème). Repli à 76px (icônes
   seules, centrées), déplié à 248px (inchangé). Transition CSS sur la largeur.
2. **Tooltips en mode replié** - `components/ui/tooltip.tsx` créé (`@radix-ui/react-tooltip`
   installé), chaque item de nav (actif ou non, y compris "Tous les modules", l'encart société
   et la carte profil) enrobé dans un tooltip `side="right"` montrant son libellé complet,
   actif uniquement quand la sidebar est repliée (`NavLabel`, wrapper conditionnel).
3. **Item actif** - barre verticale colorée (`bg-primary`, 4px, côté gauche) + fond teinté
   (`bg-primary/10`) + texte `text-primary` en gras, en repli comme en déplié. Remplace l'ancien
   style (fond neutre `bg-accent`, pas de barre).
4. **Séparateur** - `border-t border-border` avant le bloc du bas (déjà présent dans l'ancienne
   version au-dessus de la carte profil, conservé).
5. **Carte profil** - avatar (initiales) + nom + email, inchangée en mode déplié ; réduite à
   l'avatar seul (centré, tooltip nom+email) en mode replié. Déconnexion : bouton dans la même
   ligne en mode déplié (comme avant), ligne dédiée juste en dessous en mode replié (pas assez de
   place pour l'accoler à l'avatar sur 76px).
6. **Toggle de thème tout en bas, sous la carte profil** - déplacé depuis la topbar (où il vivait
   depuis le chantier `mode-sombre`) vers le pied de la sidebar, sous la carte profil, comme
   demandé. `LanguageToggle` reste dans la topbar (non demandé, pas déplacé). En mode replié, le
   groupe de 3 boutons (Clair/Sombre/Système) ne tient pas sur 76px - `ThemeToggle` reçoit une
   nouvelle prop `compact` : un seul bouton icône (reflète le thème résolu) ouvrant un menu
   déroulant (`DropdownMenu`, déjà installé) avec les 3 choix.
7. **Logo** - monogramme existant conservé, texte "DariRentals" masqué en mode replié.

## Menu mobile (drawer, construit dans ce chantier - décision d'abdo)

- `components/ui/sheet.tsx` créé (repose sur `@radix-ui/react-dialog`, déjà installé - pas de
  nouvelle dépendance).
- Bouton hamburger dans la topbar, visible uniquement `lg:hidden` (même seuil que la disparition
  de la sidebar desktop - pas de zone morte entre les deux comme cela aurait été le cas avec le
  seuil 768px mentionné dans la demande d'origine ; écart assumé et documenté ici plutôt que de
  laisser un trou 768-1024px sans aucune navigation).
- Le drawer réutilise le même contenu de nav que la sidebar desktop (`SidebarNav`/`SidebarFooter`
  factorisés en sous-composants partagés), toujours en mode déplié (pas de repli sur mobile - pas
  de sens sur un panneau qui se ferme entièrement).
- Se ferme automatiquement à la navigation (`useEffect` sur `pathname`).

## Aléa méthodologique : rendu des popovers/portails Radix dans le navigateur automatisé

**Même limite déjà rencontrée et confirmée sans gravité lors du chantier `mode-sombre`** (Dialog
de création, Select de filtre - abdo avait vérifié manuellement que c'était correct dans un vrai
navigateur). Reproduite deux fois de plus ici, sur des composants différents :

- Le drawer mobile (`SheetContent`) : capture d'écran ne montrant que l'overlay assombri, panneau
  invisible. Vérifié par `getComputedStyle`/`getBoundingClientRect` : position `fixed`, bonne
  largeur (280px), fond opaque correct (`bg-background`), `opacity: 1`, `z-index: 50` - tout est
  juste au niveau du DOM/CSSOM, le contenu est bien présent (confirmé via `textContent`) et
  fonctionnel (navigation + fermeture auto testées avec succès via clic direct sur l'élément).
- Un changement de thème déclenché par clic simulé a temporairement affiché une page "délavée"
  à l'écran alors que `document.body.innerText` confirmait tout le contenu correctement rendu -
  résolu après une navigation fraîche, sans autre intervention.

**Pas traité comme un bug** (cohérent avec la conclusion déjà validée par abdo sur ce même sujet
la dernière fois) - probable limite de composition du navigateur d'automatisation sur les
portails Radix en position fixe/animée, pas un défaut de l'app. **À vérifier par abdo dans un
vrai navigateur, en particulier le drawer mobile** (nouveau composant, jamais testé en dehors de
cet environnement).

## Autre observation, non corrigée

L'indicateur de développement Next.js (badge rond "N", bas-gauche, visible uniquement en
`next dev` - absent de tout build de production) chevauche visuellement les boutons du pied de
sidebar (déconnexion, toggle de thème) à cet endroit précis de l'écran. Gêne purement locale pour
tester en dev (clics automatisés interceptés par ce badge à plusieurs reprises, contournés via
déclenchement direct des gestionnaires d'événements pour vérifier que la logique elle-même
fonctionne) - aucun impact en production, non corrigé volontairement (façonner la sidebar autour
d'un badge de dev temporaire serait de la sur-ingénierie).

## Vérifié

- Rôle admin ET collaborateur : groupes corrects, aucune route inexistante, badge "Réservations"
  fonctionnel, "Tous les modules" navigue et affiche l'état actif.
- Repli/dépli desktop : tooltips au survol, persistance après rechargement complet (pas de flash
  d'état incorrect observé).
- Clair et sombre : item actif, carte profil, toggle de thème (compact et normal) cohérents dans
  les deux thèmes.
- Mobile (drawer) : ouverture, contenu identique à la sidebar desktop, fermeture automatique à la
  navigation - fonctionnel (vérifié par déclenchement direct, rendu visuel à reconfirmer par abdo,
  voir section précédente).
- Aucun lien existant cassé : toutes les routes de l'ancienne nav (`/admin`, `/admin/reservations`,
  `/admin/property`, `/admin/client`, `/admin/charges`, et les équivalents collaborateur) toujours
  présentes, `LanguageToggle` toujours fonctionnel depuis la topbar.
- `npm run build` : passe (26 routes générées dont les 2 nouvelles `/admin/modules` et
  `/collaborator/modules`), TypeScript propre.
