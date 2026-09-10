# Loading + login premium — DariRentals

Branche `feature/loading-login-premium`. Deux éléments visuels élevés au niveau de finition de
la landing page actuelle (badges à icône circulaire, halos flous `--primary`, cartes/icônes
soignées). Chantier 100% frontend, 2 fichiers touchés. Aucune fonctionnalité modifiée
(redirection, connexion, gestion d'erreur, toggles thème/langue).

---

## 1. Écran de chargement/redirection sur `/` ([app/page.tsx](nextjs-app-FINAL/nextjs-app/app/page.tsx))

### Avant
`if (loggedIn)` renvoyait un simple `<Loader2 className="size-6 animate-spin text-muted-foreground" />`
centré sur `bg-background` - l'icône Lucide brute, ajoutée juste pour éviter l'écran blanc
pendant la redirection.

### Après
Écran de transition (< 1 s) construit avec les mêmes ingrédients que la landing :
- **Monogramme DariRentals** : `Building2` dans un badge `size-11 rounded-2xl bg-primary
  text-primary-foreground shadow-lg shadow-primary/25` - exactement le traitement du logo du
  header de la landing.
- **Anneau qui tourne** autour du badge : `border-2 border-primary/15 border-t-primary
  animate-spin [animation-duration:1.1s]` - un spinner soigné, pas l'icône Lucide.
- **Halo qui respire** derrière : `size-20 rounded-full bg-primary/15 blur-2xl animate-pulse`
  - même famille visuelle que les glows du hero de la landing.
- **Wordmark + légende** : « DariRentals » (`text-primary`) + « Ouverture de votre espace… »
  (`text-muted-foreground`), en `animate-in fade-in duration-500`.
- Fond `bg-background` (bleu-lavande `#EEF0FA` en clair, quasi-noir en sombre).
- `import { Loader2 }` retiré (devenu inutile).

Volontairement sobre : la légende et le wordmark restent discrets, l'ensemble tient dans un
cercle de 64px + un court libellé.

### Redirection inchangée
Le `useEffect` qui fait `router.replace(\`/${role}\`)` n'a pas bougé - seul le JSX du rendu
d'attente change.

---

## 2. Page de connexion `/login` ([app/login/page.tsx](nextjs-app-FINAL/nextjs-app/app/login/page.tsx))

Le split-screen existant (panneau de branding bleu à gauche + formulaire à droite) était déjà
bon : **détail visuel amélioré, rien refait**.

### Cohérence des badges / pills avec la landing
- Le badge « Gestion locative nouvelle génération » du panneau de gauche passe du `Sparkles`
  nu à une **icône dans un cercle** (`size-5 rounded-full bg-primary-foreground/15`) +
  `pl-1.5 pr-3.5` - c'est le motif exact des badges de la landing (`size-5 rounded-full
  bg-accent` autour de l'icône).
- L'icône `ShieldCheck` du bas du panneau et celle de l'encart sécurité à droite passent d'une
  icône nue à un **carré/rond d'icône** (`bg-primary-foreground/10` à gauche, `bg-accent
  text-accent-foreground` à droite) - même traitement que les icônes de features de la landing.

### Hiérarchie visuelle
- L'icône `LockKeyhole` du titre du formulaire gagne `shadow-sm ring-1 ring-primary/10` -
  le même relief léger que les cartes de la landing (`shadow-sm`).
- Les halos flous du hero de la landing sont repris en fond discret du panneau de droite
  (`bg-primary/5` + `bg-secondary/40`, `blur-3xl`), avec `overflow-hidden` sur la section et
  `relative z-10` sur les contenus (header mobile, contrôles top-right, conteneur du
  formulaire) pour qu'ils passent au-dessus.

### Animation d'apparition douce au chargement
Constante locale `ENTER = "animate-in fade-in slide-in-from-bottom-3 duration-500
fill-mode-both"` (même idiome que la constante `ENTRANCE` des dashboards), avec un décalage
`delay-*` par élément :
- Panneau gauche : logo → badge (`delay-75`) → titre (`delay-100`) → sous-titre (`delay-150`)
  → bénéfices (`delay-200`) → footer (`delay-300`).
- Panneau droit : titre du formulaire (0) → formulaire (`delay-100`) → encart sécurité
  (`delay-200`) → copyright (`delay-300`).
- `fill-mode-both` garantit l'état « avant animation » comme état initial (pas de flash).

### Fonctionnalités préservées (testées)
- Connexion réussie → redirection `/admin` : OK.
- Mauvais mot de passe → encart d'erreur « Échec de la connexion », reste sur `/login` : OK.
- Toggle langue FR/EN (« Bienvenue » ↔ « Welcome ») : OK.
- Toggle thème clair/sombre : OK (rendu vérifié dans les deux thèmes).

---

## Tests

| Écran | Clair | Sombre | Mobile |
|---|---|---|---|
| Chargement `/` | ✅ (monogramme + anneau + halo, capturé en course avant la redirection) | ✅ (styles calculés : fond quasi-noir, badge `#3B82F6`, texte blanc, wordmark bleu lisible) | n/a (transition < 1 s, plein écran) |
| `/login` | ✅ | ✅ (panneau de branding reste bleu = identité de marque, comme la sidebar) | ✅ (panneau de branding masqué `lg:`, header mobile + form, halos discrets) |

`npm run build` : passe à chaque étape (26 routes, TypeScript propre).

## Fichiers modifiés

- `app/page.tsx` — écran d'attente premium (retrait de l'import `Loader2`).
- `app/login/page.tsx` — badges/icônes en cercle, halos de fond, relief des icônes, constante
  `ENTER` + `delay-*` pour l'apparition échelonnée.
