# Palette bleu indigo — DariRentals

Suivi du chantier "Palette bleu indigo" (branche `feature/palette-bleu-indigo`). Remplace
définitivement le violet/mauve d'Anas (`#5a3b5d`) par la nouvelle palette bleu indigo,
précise et validée par abdo. Chantier 100% frontend (CSS + composants React), aucune donnée
ni logique métier touchée.

---

## Étape 0 — Audit (FAIT)

Recherche exhaustive de toute trace de violet/mauve avant de toucher au code :

- **Zéro classe Tailwind `purple-*`/`violet-*`/`indigo-*` codée en dur** nulle part dans l'app -
  toute la plateforme est déjà pilotée par les tokens CSS (`--primary`, `--secondary`, etc.),
  confirmé par recherche sur `.tsx`/`.css`.
- **Tokens à changer** dans [`app/globals.css`](nextjs-app-FINAL/nextjs-app/app/globals.css) :
  `--primary`, `--background` (demandés explicitement), plus `--secondary`/`--accent`/`--ring`
  qui contenaient encore une teinte mauve-lavande héritée de l'ancienne palette (pas
  explicitement listés dans la demande initiale, signalés puis confirmés par abdo à corriger
  aussi - "ne laisse rien en mauve").
- **Exactement 2 couleurs codées en dur hors tokens**, toutes deux des ombres `rgba()` calquées
  sur l'ancien `--primary` mauve : [`components/ui/card.tsx`](nextjs-app-FINAL/nextjs-app/components/ui/card.tsx)
  et [`app/page.tsx`](nextjs-app-FINAL/nextjs-app/app/page.tsx) (constante `CARD_SHADOW`).
- `--muted`/`--muted-foreground`/`--border`/`--input`/`--card`/`--popover` : neutres gris déjà,
  non concernés. `--success`/`--warning`/`--destructive`/`--destructive-text`/`--info` : hors
  périmètre, conservés tels quels (déjà bien calibrés d'après le chantier mode-sombre).
- Sidebar (`components/app-shell.tsx`) : fond uni `bg-background`, à remplacer par le dégradé
  demandé.

Audit validé par abdo avant de passer à l'implémentation.

---

## Étape 1-2 — Tokens globaux + sidebar (FAIT)

### Tokens (`app/globals.css`, `:root`)

| Token | Avant (mauve) | Après (bleu indigo) |
|---|---|---|
| `--primary` | `#5a3b5d` | `#3b82f6` (valeur fournie, respectée exactement) |
| `--background` | `#f6f4f1` | `#eef0fa` (bleu-lavande très clair, pas blanc pur) |
| `--secondary` | `#f2eff4` | `#eff5fe` |
| `--secondary-foreground` | `#5a3b5d` | `#1e3a8a` |
| `--accent` | `#ede6ef` | `#e6effe` |
| `--accent-foreground` | `#5a3b5d` | `#1e3a8a` |
| `--ring` | `#96759a` | `#3b82f6` |

`--secondary`/`--accent` recalculés en teinte bleue à la demande explicite d'abdo (mélange de
`--primary` avec du blanc pour le fond, texte bleu foncé proche de Tailwind `blue-900`) plutôt
que de laisser le mauve. Vérifié par calcul : **9.57:1** sur `--secondary`, **10.6:1** sur
`--accent` - marge large, aucun compromis nécessaire ici contrairement à `--primary` (voir
section dédiée ci-dessous).

`--card`/`--popover` restent blanc pur (`#ffffff`), pour contraster avec le nouveau fond
lavande, conformément à la demande.

### Ombres codées en dur

Les 2 occurrences de `rgba(90,59,93,0.06)` (mauve) remplacées par `rgba(59,130,246,0.06)`
(bleu, même opacité) dans `card.tsx` et `page.tsx` - teinte cohérente avec le nouveau
`--primary` plutôt qu'une valeur neutre, à la demande d'abdo.

### Sidebar (`components/app-shell.tsx`)

Dégradé du haut vers le bas `#4C51E8` (indigo) → `#1E2A5E` (marine foncé), appliqué via un
objet de style inline (`SIDEBAR_GRADIENT_STYLE`, `linear-gradient(to bottom, ...)`) plutôt
qu'une classe Tailwind arbitraire, pour une application fiable indépendamment de la version
de Tailwind. Posé sur le `<aside>` desktop ET le `<SheetContent>` du drawer mobile (même
dégradé, vérifié par lecture du style calculé dans le DOM - voir "Vérification" ci-dessous).

**Dégradé identique en clair ET en sombre** : confirmé explicitement par abdo comme une
identité de marque fixe, pas un élément qui doit suivre le thème de l'app - architecturalement
différent du reste de l'UI (qui suit `--*` + `.dark`).

Tout le texte/les accents de la sidebar sont passés de `text-primary`/`bg-primary` (qui
supposent un fond clair/neutre ambiant) à des valeurs blanches explicites, calculées par
contraste contre le dégradé :

- Item de nav actif : `bg-white/15 text-white font-semibold` + barre `bg-white`.
- Item de nav inactif : `text-white/75`, survol `hover:bg-white/10 hover:text-white`.
- Titres de section : `text-white/60`. Items désactivés : `text-white/40`.
- Footer (avatar, nom, email, bouton déconnexion), badge "société active", chevrons : mêmes
  paliers de blanc transparent.
- Logo : badge `bg-white text-primary` (inversé), wordmark `text-white`.

**Contraste vérifié par calcul (formule de luminance relative WCAG, pas à l'œil)** :

- Texte blanc plein (`text-white`) sur le point le plus clair du dégradé (`#4C51E8`) :
  **5.75:1** - conforme AA texte normal.
- Texte blanc plein sur le point le plus sombre (`#1E2A5E`) : **13.57:1** - large marge.
- `text-primary` (`#3B82F6`) directement sur `#4C51E8` : seulement **1.56:1** - c'est ce calcul
  qui a motivé le remplacement systématique de `text-primary`/`bg-primary/10` par du blanc
  explicite dans toute la sidebar, plutôt que de réutiliser les tokens habituels.

La barre du haut (`<header>`, topbar) reste volontairement inchangée (`bg-card/80`,
`text-muted-foreground`...) - elle continue de suivre le thème clair/sombre normalement,
seule la sidebar porte le dégradé de marque fixe.

### `ThemeToggle` : nouvelle prop `onDark`

Le bouton `ghost` de `Button` (`components/ui/button.tsx`) n'a pas de couleur de texte propre
- il hérite de `--foreground`/`--accent-foreground`, qui suivent le thème de l'app. Sur la
sidebar (toujours sombre, même en thème clair), cet héritage produirait du texte presque noir
illisible. Ajout d'une prop `onDark` à
[`components/theme/theme-toggle.tsx`](nextjs-app-FINAL/nextjs-app/components/theme/theme-toggle.tsx)
(même schéma que la prop `compact` existante) qui force des classes blanches explicites,
y compris au survol, plutôt que de compter sur l'héritage. Appliqué uniquement aux boutons
inactifs (variante `ghost`) - le bouton actif (variante `default`, déjà `bg-primary
text-primary-foreground`) reste inchangé et lisible sur fond blanc.

### Bug Leaflet trouvé et corrigé au passage

`leaflet.css` impose `.leaflet-container a { color: #0078A8; }` (élément + classe,
spécificité CSS 0-1-1), qui l'emportait sur l'utilitaire `.text-primary` (0-1-0) - le lien
"Voir la rentabilité" du popup Leaflet ne s'affichait donc **jamais** dans `--primary`, quelle
que soit la palette (bug préexistant avec l'ancien mauve, jamais repéré jusqu'ici). Corrigé
par une règle plus spécifique dans `globals.css` :

```css
.leaflet-popup-content a {
  color: var(--primary);
}
```

Vérifié avant/après via `getComputedStyle` : `rgb(0, 120, 168)` (bug) → `rgb(59, 130, 246)`
(corrigé), dans les deux thèmes.

---

## Mode sombre (`.dark`)

- `--primary` : **même valeur `#3B82F6`** qu'en clair, réutilisée telle quelle plutôt qu'une
  nuance dédiée au sombre. Balayage exhaustif (teinte 217°, saturation 92%, luminosité 25-75%)
  prouvant qu'aucune luminosité unique ne satisfait à la fois "texte blanc sur fond plein"
  (bouton) et "texte nu sur carte sombre" à 4.5:1 - le compromis existe déjà, le dupliquer dans
  une variante sombre n'aurait rien amélioré pour un token supplémentaire. Décision validée par
  abdo (voir section "Limite connue et acceptée" ci-dessous).
- `--primary-foreground` passé de `oklch(0.145 0 0)` (quasi noir, correct pour l'ancien mauve
  clair) à `oklch(0.985 0 0)` (blanc) - le bleu, contrairement au mauve clair d'Anas, a besoin
  de texte blanc dessus dans les deux thèmes.
- `--ring` suit `--primary` (`#3b82f6`).
- `--secondary`/`--accent`/`--muted` restent les gris neutres `oklch` déjà calibrés au chantier
  mode-sombre - non concernés par cette palette (jamais mauve en sombre, déjà neutres).
- `--destructive`/`--destructive-text`/`--success`/`--warning`/`--info` : **inchangés**, hors
  périmètre de ce chantier.

Contraste vérifié : `#3B82F6` sur `--card` sombre (mesuré `rgb(23,23,23)` par rendu canvas,
pas par la sérialisation `oklch()` approximative de `getComputedStyle`) = **4.87:1** - conforme
AA, net progrès par rapport au mode clair pour ce rôle-là (texte nu). En fond plein avec texte
blanc (boutons), le contraste reste 3.68:1 comme en clair (voir ci-dessous).

## Limite connue et acceptée : `--primary` en fond plein avec texte blanc

`#3B82F6` avec du texte blanc dessus (boutons `variant="default"`, badges pleins) donne
**3.68:1** - sous le seuil AA strict pour texte normal (4.5:1), mais au-dessus du seuil "grand
texte"/UI (3:1), et une pratique courante de l'industrie pour cette teinte de bleu précise
(c'est très proche de Tailwind `blue-500`, largement utilisé tel quel dans des boutons blancs
sur le web). abdo a explicitement demandé de garder la valeur telle quelle plutôt que de créer
un second token (comme cela avait été fait pour `--destructive`/`--destructive-text` au
chantier mode-sombre) : "garde la valeur telle quelle... pas la peine de sur-ingénierer chaque
couleur". **Différence avec le cas `--destructive`** : là, aucune valeur n'atteignait même un
contraste correct dans un seul rôle ; ici le compromis est mineur (3.68:1, pas 2-3:1) et déjà
acceptable en pratique - décision de ne pas dupliquer le token, actée comme limite connue et
volontairement non corrigée plus loin.

---

## Étape 3 — Balayage de tous les écrans (FAIT)

Vérifié en clair et en sombre, desktop et mobile, par une combinaison de captures d'écran et
de vérification DOM (`getComputedStyle` + scan programmatique de toute couleur "mauve-like"
sur la page - tolérance sur R≈B avec R,B nettement > G, méthode plus fiable que l'inspection
visuelle seule pour couvrir exhaustivement une page) :

- **Dashboard admin** : sidebar, topbar, stat cards, carte + timeline, graphique Revenus -
  tous bleus, aucun mauve résiduel (fait à l'étape 2, revérifié).
- **Dashboard collaborateur** (`/collaborator`, connecté avec un compte de test dédié) :
  sidebar/dégradé/bouton "Scanner une facture"/statuts identiques au dashboard admin - même
  composant `AppShell` partagé, comportement automatique confirmé par capture d'écran.
- **`/login`** (split-screen) : panneau de marque (`bg-primary`) et bouton "Se connecter" bleus,
  clair et sombre.
- **`/reserver`** : cartes propriétés, boutons "Demander", liens "Voir la position" bleus,
  clair et sombre.
- **Landing page `/`** : les 5 éléments `.bg-primary` (logo header, "Se connecter", "Accéder à
  mon espace", bloc CTA final, second badge logo) vérifiés à `rgb(59, 130, 246)` exact via
  requête DOM (capture d'écran indisponible ponctuellement sur cette page - limite connue de
  l'outil après un `scrollTo`, contournée par vérification DOM).
- **CRUD** : Biens & logements (liste + dialog "Nouvelle propriété", focus ring bleu vérifié),
  Réservations, Demandes de réservation, Tâches, Charges (badges "Non payée" ambre, montants
  intacts), Paiements, Collaborateurs (+ vue mobile via drawer), Clients, Tous les modules -
  **zéro trace de mauve détectée sur chacun** (scan DOM automatisé, 0 correspondance à chaque
  fois).
- **Rapports financiers** : page + graphiques (36 éléments `<svg>` scannés pour tout `fill`/
  `stroke` mauve codé en dur - zéro trouvé) - bleu partout, clair et sombre.
- **Taux de change** : zéro trace de mauve.
- **Drawer mobile** (viewport 375px, testé sur `/admin/collaborator`) : dégradé du drawer lu
  directement dans le DOM - `linear-gradient(rgb(76, 81, 232), rgb(30, 42, 94))`, soit
  exactement `#4C51E8` → `#1E2A5E`, identique au rail desktop.

**Confirmation finale par recherche globale** : recherche de toute occurrence des anciennes
valeurs hex mauve (`#5a3b5d`, `#96759a`, `#f2eff4`, `#ede6ef`) et de toute classe Tailwind
`purple-*`/`violet-*` dans l'ensemble du code source (`.ts`/`.tsx`/`.css`) - **zéro résultat**.

Aucun bug supplémentaire trouvé pendant ce balayage (au-delà du bug Leaflet déjà corrigé à
l'étape 2) - l'architecture 100% pilotée par tokens CSS s'est confirmée sur l'ensemble des
écrans, sans exception codée en dur oubliée.

`npm run build` : passe (26 routes générées, TypeScript propre, Turbopack).

---

## Résumé des fichiers modifiés

- [`app/globals.css`](nextjs-app-FINAL/nextjs-app/app/globals.css) - tokens `:root` et `.dark`,
  règle Leaflet.
- [`components/app-shell.tsx`](nextjs-app-FINAL/nextjs-app/components/app-shell.tsx) - dégradé
  sidebar (desktop + mobile), tout le texte/accents en blanc explicite.
- [`components/theme/theme-toggle.tsx`](nextjs-app-FINAL/nextjs-app/components/theme/theme-toggle.tsx) -
  prop `onDark`.
- [`components/ui/card.tsx`](nextjs-app-FINAL/nextjs-app/components/ui/card.tsx) - ombre bleue.
- [`app/page.tsx`](nextjs-app-FINAL/nextjs-app/app/page.tsx) - `CARD_SHADOW` bleue.
