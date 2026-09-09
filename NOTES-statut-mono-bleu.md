# Statut mono-bleu — DariRentals

Suivi du chantier "Statut mono-bleu" (branche `feature/statut-mono-bleu`), fait sur `main`
après le merge de [NOTES-harmonisation-couleurs.md](NOTES-harmonisation-couleurs.md). Chantier
100% frontend, un seul fichier touché (`app/globals.css`) - aucun composant modifié : toute la
plateforme reste pilotée par les tokens `--success`/`--warning`/`--destructive`.

## Changement de direction par rapport au Set B (chantier précédent)

Le Set B (teal/corail, validé puis mergé au chantier
[harmonisation-couleurs](NOTES-harmonisation-couleurs.md)) a été jugé, à l'usage, encore "à
part" du bleu dominant de la plateforme - un teal et un corail restent des couleurs distinctes
du bleu, même refroidies. **Nouvelle direction, plus radicale et validée par abdo avec aperçu
visuel avant implémentation** : faire dériver `--success` (positif) et `--warning` (attention)
directement du bleu primaire lui-même, et ne garder que `--destructive` (négatif/erreur) comme
vraie couleur distincte - un dashboard quasi-monochrome bleu, avec seulement le rouge qui
tranche pour les vrais problèmes.

## Méthode : une proposition calibrée, présentée avant application

Contrairement aux chantiers précédents (3 jeux de valeurs alternatifs), la direction stratégique
était déjà actée par abdo - le travail ici consistait à calibrer les valeurs *précises* par
calcul, sans rouvrir le choix de direction, et à le présenter avant application (aperçu visuel +
tableau de contraste, méthode habituelle).

## Valeurs retenues

| Token | Avant (Set B) | Après (mono-bleu) |
|---|---|---|
| `--success` | `#397f7c` (clair) / `#72c0bd` (sombre) | `#1e40af` (clair) / `#8a8df0` (sombre) |
| `--warning` | `#8a7442` (clair) / `#bda775` (sombre) | `#3b82f6` = `--primary` (clair et sombre) |
| `--destructive` | `#c25164` (clair) / `#aa3c4e` (sombre) | `#dc2626` (clair) / `#c52020` (sombre) |
| `--destructive-text` | `#c25164` (clair) / `#c76072` (sombre) | `#dc2626` (clair) / `#e24b4b` (sombre) |

## Le point délicat : `--warning` ne peut pas être aussi clair que suggéré au départ

La piste initiale envisageait un bleu très clair façon `#93C5FD` pour `--warning`. **Vérifié
par calcul avant d'appliquer quoi que ce soit** : `#93C5FD` ne donne que **1.80:1** de contraste
sur blanc - en texte de badge ("En attente"), ce serait quasiment illisible, une vraie
régression d'utilisabilité, pas un simple compromis cosmétique comme celui déjà accepté pour
`--primary`.

**Solution retenue** : `--warning` reprend `--primary` **à l'identique** (`#3B82F6`), sans
créer de nouvelle valeur - 3.68:1 sur blanc, exactement le même compromis déjà documenté et
accepté pour `--primary` ailleurs dans l'app (voir `NOTES-palette-bleu-indigo.md`). Ce choix
n'introduit aucune régression nouvelle, il étend un compromis déjà validé.

## Distinguabilité `--success` / `--warning` (les deux dans la même famille bleue)

Point de vigilance explicite demandé par abdo, vérifié par calcul avant application :

### Clair
`--success` (`#1E40AF`, H226° S71% **L40%**) vs `--warning` (`#3B82F6`, H217° S91% **L60%**) :
écart de **20 points de luminosité HSL**, contraste mutuel **2.37:1**. Net à l'œil (confirmé
sur l'aperçu visuel présenté avant validation) malgré un chiffre WCAG mutuel modeste - cette
formule mesure la luminance, pas la teinte, donc un écart de luminosité de 20 points reste très
perceptible en pratique même si le ratio brut paraît bas.

### Sombre : la vraie difficulté, résolue par la teinte plutôt que la clarté
En sombre, les deux couleurs doivent rester assez claires pour se lire sur une carte presque
noire (`--card` sombre, mesuré `rgb(23,23,23)`) - l'écart de luminosité qui fonctionne en clair
ne peut plus jouer le même rôle, les deux sont mécaniquement rapprochées en luminance.

**Différenciation choisie par la teinte** :
- `--warning` sombre reprend `--primary` sombre à l'identique (`#3B82F6`, sky-blue H217°,
  4.87:1 sur `--card` sombre - déjà établi de longue date).
- `--success` sombre reprend l'indigo déjà utilisé pour le dégradé de la sidebar (voir
  `NOTES-palette-bleu-indigo.md`, `#4C51E8`), éclairci à **`#8A8DF0`** (H238°, 6.10:1 sur
  `--card` sombre) - un violet-bleu clairement différent au premier coup d'œil du sky-blue de
  `--warning`.

**Honnêteté sur les chiffres** : le contraste WCAG *entre les deux couleurs elles-mêmes* reste
mathématiquement modeste (~1.25:1), parce que la formule ne mesure que la luminance et que les
deux sont désormais claires par nécessité. abdo a explicitement validé ce compromis après avoir
vu l'aperçu visuel (l'écart de teinte se voit clairement à l'œil), plutôt que d'être présenté
un chiffre flatteur qui aurait masqué cette limite réelle de la méthode de calcul.

## `--destructive` : reste la seule vraie couleur distincte

`#DC2626` (clair, H0° S72% L51%) / `#C52020` (sombre, fond plein) / `#E24B4B` (sombre, texte
nu) - un rouge franc, sans aucune dérive vers le bleu, conformément à la consigne explicite
d'abdo ("ne le fais pas dériver vers le bleu aussi, sinon plus aucune distinction visuelle
'danger' ne resterait"). La séparation fond-plein/texte-nu en mode sombre est conservée -
même preuve mathématique qu'aux chantiers précédents (aucune valeur unique ne satisfait à la
fois le contraste "texte blanc sur fond plein" et "texte coloré sur carte sombre").

### Contraste vérifié par calcul

| Rôle | Valeur | Fond | Contraste |
|---|---|---|---|
| `--success` texte nu, clair | `#1E40AF` | blanc | **8.72:1** ✅ |
| `--success` texte nu, clair | `#1E40AF` | lavande | 7.68:1 |
| `--success` texte nu, sombre | `#8A8DF0` | `--card` sombre | **6.10:1** ✅ |
| `--warning` (= `--primary`), clair | `#3B82F6` | blanc | 3.68:1 (compromis déjà accepté) |
| `--warning` (= `--primary`), sombre | `#3B82F6` | `--card` sombre | **4.87:1** ✅ |
| `--destructive` fond plein + texte blanc, clair | `#DC2626` | blanc | **4.83:1** ✅ |
| `--destructive` fond plein + texte blanc, sombre | `#C52020` | blanc | **5.83:1** ✅ |
| `--destructive-text` texte nu, sombre | `#E24B4B` | `--card` sombre | **4.56:1** ✅ |

`--warning-foreground` (sombre) passé de `oklch(0.145 0 0)` (quasi-noir, adapté à l'ancien
`--warning` pastel clair) à `oklch(0.985 0 0)` (blanc) : le nouveau `--warning` sombre étant un
bleu soutenu (`#3B82F6`, L60%) et non plus un pastel clair, du texte blanc dessus est
nécessaire - même logique que `--primary-foreground`. Non consommé par un composant aujourd'hui
(vérifié - toujours zéro usage direct de `text-warning-foreground`), gardé cohérent par
précaution.

## Vérification visuelle (DOM + calcul, clair et sombre)

Tokens racine relus directement sur `document.documentElement`, exacts dans les deux thèmes :
`--success` `#1e40af`/`#8a8df0`, `--warning` `#3b82f6` (identique clair/sombre), `--destructive`
`#dc2626`/`#c52020`, `--destructive-text` `#dc2626`/`#e24b4b`.

- **Dashboard** : badge de variation "Revenus" (`-19.7%`, négatif) en rouge franc
  (`rgb(220,38,38)` clair, `rgb(226,75,75)` sombre) - net et lisible sur les deux fonds.
- **Santé du portefeuille (Health Score)**, score "Bon" (80/100, palier `--warning`) : anneau
  `rgb(59,130,246)` dans les deux thèmes - exactement `--primary`, comme prévu.
- **Rentabilité par propriété** (3 `StatCard`) : Revenus et Bénéfice net (positif) en
  `#1e40af`/`#8a8df0` (succès), Charges en `#dc2626`/`#e24b4b` (destructif) - les 3 cartes
  restent nettement distinctes, clair et sombre.
- **Badges de statut CRUD** : "Confirmee" (Réservations) en `#1e40af` ; "Non payée" (Charges)
  en `#3b82f6` (identique clair/sombre) - les 3 statuts (positif/attention/négatif) restent
  visuellement distinguables : bleu foncé / bleu primaire / rouge.
- **Graphique Revenus/Charges** (`MonthlyChart`) : référence directement `var(--color-success)`/
  `var(--color-destructive)`, tokens confirmés corrects via lecture CSS - aucune logique de
  composant à vérifier séparément, la barre "Revenus" suit `--success` et "Charges" suit
  `--destructive` sans changement de code.

`npm run build` : passe avant et après application (26 routes, TypeScript propre).

## Fichier modifié

- [`app/globals.css`](nextjs-app-FINAL/nextjs-app/app/globals.css) — `--success`, `--warning`,
  `--destructive`, `--destructive-text`, `--warning-foreground` (`:root` et `.dark`). Aucun
  composant `.tsx` modifié : l'architecture 100% tokens absorbe le changement sans aucune autre
  édition, pour la troisième fois consécutive sur ce même axe de personnalisation.
