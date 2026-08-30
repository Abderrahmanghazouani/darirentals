# Mode sombre/clair — DariRentals

Suivi du chantier "Mode sombre" (branche `feature/mode-sombre`). Chantier 100% frontend.

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

## Étape 3 — Tokens de couleur de marque en mode sombre (FAIT)

Les 4 tokens ne sont plus de simples copies - chaque valeur a été choisie puis **vérifiée par
calcul réel de ratio de contraste WCAG** (formule de luminance relative), pas seulement à l'œil,
en tenant compte de l'usage réel de chaque token dans le code (recherché avant de choisir) :

| Token | Clair | Sombre | Pourquoi |
|---|---|---|---|
| `--primary` | `#185fa5` | `#2563eb` (bleu-600) | Utilisé en fond plein avec texte blanc (`bg-primary text-primary-foreground`, boutons). Éclaircir *et* améliorer le contraste vont dans le même sens ici : 5.17:1 avec du blanc (contre un bleu marine qui, lui, était très sombre). |
| `--destructive` | `#e24b4a` | `#d33f3e` | **Exception au réflexe "plus clair"**, justifiée : la marque utilise déjà un rouge assez clair (`#e24b4a`, seulement ~3.9:1 avec du blanc - déjà limite en mode clair). L'éclaircir encore réduit mécaniquement le contraste texte-blanc (mesuré 3.1-3.4:1 sur plusieurs variantes testées, pire qu'en clair). Assombrir/resaturer légèrement fonctionne mieux (4.62:1) - c'est aussi le choix de shadcn/ui lui-même pour son `--destructive` par défaut en mode sombre. |
| `--success` | `#0f9b8e` | `#2dd4bf` (teal-400) | Jamais utilisé en fond plein dans ce projet (recherché dans le code : uniquement `bg-success/10 text-success` ou icônes) - éclairci franchement, sans contrainte de texte blanc superposé. Contraste texte-sur-carte-sombre mesuré : 11.17:1. |
| `--warning` | `#d97706` | `#fbbf24` (amber-400) | Même raisonnement que `--success` (jamais en fond plein). Contraste texte-sur-carte-sombre mesuré : 12.45:1. |

`--ring` mis à jour pour suivre `--primary` (`#2563eb`).

### Vérification visuelle (Dashboard, `/admin` et `/admin/property`)

- Health Score : anneau de progression (teal), badge de niveau ("Bon", ambre), barres de
  composantes (teal pour 100/100, le nouveau rouge pour la sous-composante à 20/100) - tous
  lisibles, pas de couleur "délavée" ou "criarde".
- Bouton "Nouvelle propriété" (fond plein bleu-600 + texte blanc) et badge "Active" : nets,
  confortables à lire.
- Message d'erreur de l'assistant IA (texte destructif sur carte sombre) : bien lisible.
- **Non-régression mode clair vérifiée** : après avoir modifié uniquement le bloc `.dark`,
  `--primary`/`--destructive` en mode clair valent toujours exactement `#185fa5`/`#e24b4a`
  (aucune valeur partagée entre les deux blocs n'a été touchée par erreur).

`npm run build` passe à chaque étape.

---

## Étape 4 — Vérification exhaustive de tous les écrans

À venir.
