# Mode sombre/clair — DariRentals

Suivi du chantier "Mode sombre" (branche `feature/mode-sombre`). Chantier 100% frontend.

---

## Merge de main en cours de route (PR #28 "style-frontend")

Après les étapes 0-3, une PR d'Anas ("theme violet, sidebar, dashboards admin/collaborateur,
landing page") a été mergée dans `main` **pendant** ce chantier. `main` local était périmé
(jamais fetché) - détecté et corrigé avant de continuer, voir la discussion complète dans
l'historique de conversation. Résumé de ce qui a changé et de ce qui a été refait :

- **Refonte complète, pas juste un changement de couleur** : nouvelle sidebar
  (`components/app-shell.tsx` + `app/admin/layout.tsx` + `app/collaborator/layout.tsx`,
  tous nouveaux), dashboards admin/collaborateur quasi entièrement réécrits, `/login` refait
  en split-screen, nouvelle landing page `/`. Palette de marque entièrement remplacée : bleu
  `#185fa5` → mauve `#5a3b5d`, teal → vert mat, ambre → or mat, plus un nouveau token
  `--info` (bleu-gris) qui n'existait pas avant.
- **Bonne nouvelle** : Anas a construit PAR-DESSUS l'existant, pas à côté - `HealthScoreCard`,
  `RevenueIntelligenceCard`, `ActionCenterCard`, `MorningInsightsCard`/`PortfolioChatCard`,
  `computeHealthScore`, et tout le système i18n (`lib/i18n/`) sont conservés et même étendus
  (nouvelles clés `login.brand*`, `dashboardHome.*` dans `translations.ts`, déjà branchées
  sur sa refonte de `/login` et des dashboards).
- **Merge** : seulement 2 conflits (`globals.css`, `app/layout.tsx` - ses fonts DM Sans/DM Mono
  + mon `suppressHydrationWarning`/`ThemeProvider`), résolus. `npm run build` passe.
- **Tokens sombres de l'étape 3 entièrement refaits** contre la VRAIE palette d'Anas (pas
  réappliqué mes anciennes valeurs bleu/teal) - même méthode (vérification par calcul de
  contraste réel), résultats déplacés dans la section "Étape 3" ci-dessous.
- **Deux bugs trouvés en testant le résultat du merge, corrigés avant de continuer** :
  1. Sidebar codée en dur `bg-[#faf8f6]` (`components/app-shell.tsx:149`) - ne réagissait pas
     du tout au mode sombre (restait blanche pendant que le reste de la page passait en
     sombre). Remplacé par `bg-background` : la sidebar partage maintenant le même fond que
     le contenu principal, cohérent avec le fait que la carte "société active" à l'intérieur
     utilise déjà `bg-card` pour se distinguer visuellement de son conteneur.
  2. `PremiumHeader`/`CollaboratorHeader` (où le `ThemeToggle` avait été posé à l'étape 2) ne
     sont plus rendus nulle part - la vraie barre du haut vit maintenant dans `app-shell.tsx`.
     `ThemeToggle` déplacé là. **Découverte au passage** : `LanguageToggle` et
     `CurrencySelector` n'étaient pas non plus dans la nouvelle topbar (l'hypothèse de départ
     était fausse) - `LanguageToggle` ajouté à côté du `ThemeToggle`. `CurrencySelector` reste
     absent : son `CurrencyProvider` est posé au niveau de la PAGE (`admin/page.tsx`), pas du
     LAYOUT, alors que la topbar vit dans le layout (`AppShell`, parent du contenu de page) -
     l'ajouter tel quel ferait planter l'app (`useCurrency()` hors de son Provider). Nécessite
     de déplacer `CurrencyProvider` au niveau du layout pour être fait proprement - pas fait
     ici (hors des deux points demandés), signalé pour un futur passage.

### Testé le 31/08 après ces deux corrections

- Dashboard admin ET collaborateur (composant `AppShell` partagé par les deux rôles) : sidebar
  et topbar cohérentes en clair et en sombre, capture d'écran à l'appui pour les deux.
- `ThemeToggle` fonctionnel dans son nouvel emplacement (cycle clair/sombre confirmé).
- `LanguageToggle` fonctionnel dans son nouvel emplacement (bascule FR/EN confirmée, y compris
  sur les nouvelles clés `dashboardHome.*` d'Anas).
- Zéro nouvelle erreur console (les deux `502` observés sont l'épuisement du quota gratuit
  Gemini, déjà documenté dans NOTES-ai-assistant.md - sans lien avec ce chantier).

---

## Étape 0 — Inspection (FAIT)

- Le bloc `.dark { ... }` existait déjà dans
  [`app/globals.css`](nextjs-app-FINAL/nextjs-app/app/globals.css) (scaffolding shadcn initial,
  jamais activé), avec `@custom-variant dark (&:is(.dark *));` déjà câblé — Tailwind v4 réagit
  donc déjà à une classe `.dark` sur un ancêtre, exactement ce que `next-themes` gère par défaut.
- Les tokens neutres shadcn (`--background`, `--foreground`, `--card`, `--muted`, `--border`...)
  étaient déjà correctement adaptés au sombre (valeurs shadcn par défaut jamais personnalisées).
- Les 4 tokens de marque (`--primary`, `--destructive`, `--success`, `--warning`) dans `.dark`
  étaient de simples copies des valeurs claires (mêmes hex) - à corriger (Étape 3).
- `next-themes` : absent de `package.json`.
- `app/layout.tsx` : pas de `suppressHydrationWarning` sur `<html>` - nécessaire avant d'ajouter
  next-themes (qui modifie `className`/`style` sur `<html>` avant hydratation).

## Étape 1-2 — Installation, ThemeProvider, toggle (FAIT)

- `next-themes@0.4.6` installé.
- [`components/theme/theme-provider.tsx`](nextjs-app-FINAL/nextjs-app/components/theme/theme-provider.tsx)
  — wrapper `attribute="class"` + `defaultTheme="system"` + `enableSystem`.
- [`components/theme/theme-toggle.tsx`](nextjs-app-FINAL/nextjs-app/components/theme/theme-toggle.tsx)
  — groupe de 3 boutons (Soleil/Lune/Écran), même style visuel que `LanguageToggle`.
- `app/layout.tsx` : `<html suppressHydrationWarning>` + `ThemeProvider` posé à la racine
  (au-dessus de `LanguageProvider`).
- Toggle ajouté au `PremiumHeader` (à côté de `LanguageToggle`) comme premier point de test -
  les autres emplacements (CollaboratorHeader, `/login`, `/reserver`) suivront à l'étape 4.

### Testé le 31/08

- Détection système par défaut (`prefers-color-scheme`) sans choix explicite en `localStorage`.
- Script anti-flash de next-themes confirmé présent dans le `<head>` (s'exécute avant le paint).
- Zéro warning d'hydratation (`/admin`, `/login`, onglet neuf).
- Cycle complet Clair → Sombre → Système testé : classe `.dark`/`light` sur `<html>` correcte à
  chaque clic, couleurs réellement changées (vérifié via `getComputedStyle`), bouton actif bien
  mis en évidence.
- Persistance après **rechargement réel** (navigation fraîche, pas juste changement d'état SPA)
  confirmée : le choix explicite survit.

## Étape 3 — Tokens de couleur de marque en mode sombre

**Fait une première fois contre l'ancienne palette bleu/teal, puis entièrement refait contre
la vraie palette d'Anas après le merge de la PR #28** (voir section "Merge de main en cours de
route" ci-dessus) - la palette bleu/teal originale n'existe plus du tout dans le projet.

Les 5 tokens (4 + le nouveau `--info` d'Anas) ne sont plus de simples copies - chaque valeur a
été choisie puis **vérifiée par calcul réel de ratio de contraste WCAG** (formule de luminance
relative), pas seulement à l'œil, en tenant compte de l'usage réel de chaque token dans le code
(`badge.tsx`/`stat-card.tsx` recherchés avant de choisir) :

| Token | Clair (Anas) | Sombre | Pourquoi |
|---|---|---|---|
| `--primary` | `#5a3b5d` (mauve) | `#96759a` + texte quasi-noir | Valeur déjà posée par Anas, conservée telle quelle - mauve clair cohérent avec du texte foncé plutôt que blanc. |
| `--destructive` | `#c75c59` (terracotta) | `#b34e4b` | **Exception au réflexe "plus clair"**, même logique que sur l'ancienne palette : ce rouge a déjà ~4.1:1 avec du blanc en clair. L'éclaircir réduit ce contraste (mesuré 3.2-2.7:1 sur les variantes testées). Assombrir/resaturer légèrement fonctionne mieux (5.11:1) - même choix que shadcn/ui par défaut. |
| `--success` | `#4f8b72` (vert mat) | `#6fb595` | Jamais en fond plein (uniquement badges pastel `bg-success/15 text-success` ou icônes, vérifié dans `badge.tsx`/`stat-card.tsx`) - éclairci en gardant le caractère mat de la palette d'Anas plutôt que de sauter vers un vert saturé. |
| `--warning` | `#c4924d` (or mat) | `#dcae6b` | Même raisonnement que `--success`. |
| `--info` (nouveau, ajouté par Anas) | `#6584a2` (bleu-gris) | `#87a3bf` | Même raisonnement - jamais en fond plein, éclairci en gardant la teinte mate. |

`--ring` suit `--primary` (`#96759a`, valeur d'Anas).

### Vérification visuelle (Dashboard admin ET collaborateur, `/admin/property`)

- Sidebar, topbar, stat cards, Health Score, graphique Revenus, panneau "À faire aujourd'hui" :
  tous lisibles en sombre, aucune couleur "délavée" ou "criarde" (voir captures dans la
  discussion de session).
- Bouton "Scanner une facture" (fond plein mauve clair + texte foncé, choix d'Anas) et badges
  de statut : nets.
- **Non-régression mode clair vérifiée** après le merge : les valeurs `:root` (palette Anas)
  n'ont pas été touchées, seul le bloc `.dark` a été modifié.

`npm run build` passe à chaque étape.

---

## Étape 4 — Vérification exhaustive de tous les écrans

Écrans passés en revue en sombre : Dashboard admin + collaborateur (bar chart Revenus/Charges
inclus), `/login`, `/reserver` (+ son dialog de demande), `/select-enterprise`, CRUD (Biens &
logements + formulaire de création, Tâches, Demandes de réservation + son Select), Rapports
financiers, Taux de change, Rentabilité.

**Détour méthodologique** : à un moment, le dashboard collaborateur s'est affiché vide, erreur
console `Unknown word HEAD` / `Merge conflict marker encountered` dans `globals.css`/
`layout.tsx`. Fichiers sur disque vérifiés propres (zéro marqueur) - c'était le cache Turbopack
du serveur dev, qui tournait depuis avant le merge et n'avait jamais invalidé son build après
la résolution des conflits. Résolu en coupant le process, supprimant `.next`, et relançant.

**Bugs trouvés et corrigés** :

1. **`ThemeToggle` absent sur `/login` et `/reserver`** (seul `LanguageToggle` y était) - point 3
   du cahier des charges initial, resté en suspens à cause de l'interruption du merge. Ajouté au
   même emplacement que `LanguageToggle` dans les deux fichiers (mobile header + desktop
   top-right pour `/login` ; ligne unique top-right pour `/reserver`). Testé en desktop et mobile.

2. **`text-destructive` en texte nu : contraste insuffisant en sombre, résolu par un second
   token** - voir section dédiée ci-dessous, c'est le morceau le plus substantiel de cette étape.

**Point observé, non traité en bug** : sur les popovers Radix (Dialog de création, Select de
filtre), mes captures d'écran automatisées montraient un fond clair/transparent alors que
`getComputedStyle()` confirmait systématiquement un fond `--background`/`--popover` sombre
correct (ancêtre `.dark` présent, aucun style ni overlay parasite, composant source identique
partout). Signalé à abdo pour vérification manuelle dans un vrai navigateur - probable
limite de rendu de l'outil de capture automatisée sur les portails Radix, pas un bug réel.
**Résultat de la vérification manuelle : à compléter par abdo.**

### `--destructive` vs `--destructive-text` : pourquoi deux tokens

En creusant le contraste de `text-destructive` utilisé en texte nu (messages d'erreur, icônes,
valeurs "Charges" en rouge sur Rentabilité), calcul précis via rendu canvas (plus fiable que lire
la chaîne `lab(...)` de `getComputedStyle`, qui avait faussé une première mesure à 4.07:1 au lieu
de la vraie valeur 3.51:1) : **aucune valeur unique de `--destructive` ne peut satisfaire à la
fois** le contraste "texte blanc sur fond plein" (bouton "Refuser", badge `variant="destructive"`)
**et** "texte coloré sur carte sombre" (erreurs, valeurs négatives) à 4.5:1 (seuil AA texte
normal). Preuve : contre blanc il faut une luminance relative ≤0.183 ; contre `--card` sombre
(mesuré `rgb(23,23,23)` par canvas, pas approximé) il en faut une ≥0.214 - les deux plages ne se
recoupent jamais. Vérifié par balayage exhaustif teinte 0-30°/saturation 30-80%/luminosité
25-60% : zéro combinaison satisfaisant les deux à la fois.

**Décision (validée avec abdo)** : deuxième token plutôt qu'un compromis sous le seuil.

- `--destructive` : **inchangé** (`#b34e4b` en sombre), reste réservé aux fonds pleins
  (`bg-destructive`, badge `variant="destructive"`) - 5.11:1 sur blanc, toujours valide.
- `--destructive-text` (nouveau) : même teinte/saturation que `--destructive`, juste éclairci -
  `#c75c59` en clair (identique à `--destructive`, le clair n'avait pas besoin de changer),
  **`#bf6966` en sombre** - 4.63:1 sur `--card`, 5.12:1 sur `--background`, marge confortable
  au-dessus du seuil AA dans les deux cas.
- Tous les usages `text-destructive` nus basculés vers `text-destructive-text` : **65
  occurrences dans 42 fichiers**, remplacement automatisé (script Node, regex avec exclusion de
  `-text` pour éviter tout double-suffixe) puis vérifié par grep qu'aucun `text-destructive` nu
  ne subsiste. `bg-destructive`/`border-destructive`/le badge `variant="destructive"` ne
  contiennent pas la sous-chaîne concernée, non affectés.

### Vérification finale (calcul + visuel)

- `text-destructive-text` mesuré en direct sur `/login` (message "Échec de la connexion") et sur
  Rentabilité (valeur "700,00" de la carte Charges) : `rgb(191, 105, 102)` dans les deux cas,
  4.63:1 sur `--card` / 5.12:1 sur `--background` - conforme AA avec marge.
- Badge `variant="destructive"` (fond plein) revérifié inchangé : `rgb(179, 78, 75)` sur blanc,
  5.11:1 - non affecté par le changement.
- Mode clair revérifié inchangé : `text-destructive-text` = `rgb(199, 92, 89)` = `#c75c59`,
  valeur `--destructive` d'origine, aucune régression.
- `npm run build` : passe (24 routes générées, TypeScript propre).
