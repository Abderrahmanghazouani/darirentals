# Harmonisation des couleurs de statut — DariRentals

Suivi du chantier "Harmonisation couleurs statut" (branche
`feature/harmonisation-couleurs-statut`), fait sur `main` après le merge de
[NOTES-palette-bleu-indigo.md](NOTES-palette-bleu-indigo.md). Chantier 100% frontend, un seul
fichier touché (`app/globals.css`) - aucune logique métier ni composant modifié : toute la
plateforme est déjà pilotée par les tokens `--success`/`--warning`/`--destructive`.

## Constat de départ

À côté du bleu vif `#3B82F6`, les couleurs de statut héritées de la palette d'Anas (vert
franc, ambre doré, rouge-terracotta) paraissaient "à part" - plus saturées et plus chaudes que
le reste de l'interface, dominée par le bleu depuis le chantier palette-bleu-indigo.

**Découverte au passage lors du calcul de contraste des valeurs actuelles** : deux régressions
de contraste préexistantes, jamais corrigées jusqu'ici (probablement héritées telles quelles
de la palette d'Anas sans être vérifiées à l'époque) :

| Token | Valeur (clair) | Contraste sur blanc | Statut AA (4.5:1 texte normal) |
|---|---|---|---|
| `--success` | `#4f8b72` | 3.99:1 | ❌ sous le seuil |
| `--warning` | `#c4924d` | 2.78:1 | ❌ nettement sous le seuil |
| `--destructive` | `#c75c59` | 4.11:1 | ❌ juste sous le seuil |

Ce chantier corrige ces trois manques au passage, en plus de l'objectif de teinte.

## Méthode : 3 jeux de valeurs proposés, un validé

Trois pistes ont été calculées et présentées à abdo avant toute application (aperçu visuel +
tableau de contraste) :

- **Set A — Ajustement subtil** : teintes proches des originales, juste refroidies.
- **Set B — Teal / corail** : décalage net vers le froid tout en restant identifiable. **Validé.**
- **Set C — Froid prononcé** : le plus désaturé/rosé, `--destructive` s'approchant du magenta -
  écarté car risquant de s'éloigner du réflexe "rouge = danger".

## Valeurs retenues (Set B)

Chaque valeur choisie par recherche de la luminosité la plus haute (donc la moins sombre/dure)
qui passe encore 4.5:1 sur blanc, à une teinte/saturation cible "teal/corail" décidée d'abord
par abdo puis affinée par calcul - même méthode que pour tous les tokens de ce projet.

| Token | Avant | Après (clair) | Après (sombre) |
|---|---|---|---|
| `--success` | `#4f8b72` (H155° vert) | `#397f7c` (H177° teal) | `#72c0bd` (H178° teal clair) |
| `--warning` | `#c4924d` (H35° ambre doré) | `#8a7442` (H42° ambre mat) | `#bda775` (H42° ambre mat clair) |
| `--destructive` | `#c75c59` (H2° rouge-terracotta) | `#c25164` (H350° rosé-corail) | `#aa3c4e` (H350°, fond plein) |
| `--destructive-text` | `#c75c59` clair / `#bf6966` sombre | `#c25164` (= `--destructive`) | `#c76072` (H350°, texte nu) |

`--info` (`#6584a2`/`#87a3bf`) : **inchangé**, hors périmètre - déjà une teinte bleu-gris
cohérente avec le nouveau bleu, pas de raison de la retoucher.

### Contraste vérifié par calcul (formule WCAG, canvas pour le fond sombre)

| Rôle | Valeur | Fond | Contraste |
|---|---|---|---|
| `--success` texte nu, clair | `#397f7c` | blanc (`--card`) | **4.67:1** ✅ |
| `--success` texte nu, clair | `#397f7c` | lavande (`--background`) | 4.11:1 |
| `--success` texte nu, sombre | `#72c0bd` | `--card` sombre (mesuré `rgb(23,23,23)`) | **8.53:1** ✅ |
| `--warning` texte nu, clair | `#8a7442` | blanc (`--card`) | **4.51:1** ✅ |
| `--warning` texte nu, clair | `#8a7442` | lavande (`--background`) | 3.97:1 |
| `--warning` texte nu, sombre | `#bda775` | `--card` sombre | **7.64:1** ✅ |
| `--destructive` fond plein + texte blanc, clair | `#c25164` | blanc | **4.51:1** ✅ |
| `--destructive` fond plein + texte blanc, sombre | `#aa3c4e` | blanc | **6.06:1** ✅ |
| `--destructive-text` texte nu, sombre | `#c76072` | `--card` sombre | **4.56:1** ✅ |

Les 3 tokens passent maintenant l'AA texte normal sur `--card` (le fond réel où ils sont
utilisés - tous les badges/cartes vivent dans un `<Card>` blanc, pas directement sur
`--background`). Le chiffre "lavande" est indiqué à titre informatif seulement : aucun badge
`text-success`/`text-warning` n'est posé directement sur `--background`, toujours à l'intérieur
d'un `<Card>` blanc - vérifié en relisant `stat-card.tsx`, `health-score-card.tsx`,
`revenue-intelligence-card.tsx`, `badge.tsx`.

### `--destructive`/`--destructive-text` : la séparation en mode sombre reste nécessaire

Même preuve qu'au chantier mode-sombre, réappliquée à la nouvelle teinte rosé-corail :
descendre la luminosité pour le fond plein (texte blanc dessus, boutons "Refuser"/badge
`variant="destructive"`) et la monter pour le texte nu sur carte sombre sont deux exigences
opposées - aucune valeur unique ne satisfait les deux à 4.5:1 simultanément. La séparation en
deux tokens, déjà en place, est conservée telle quelle - seule la teinte change.

### `--warning-foreground` : ajusté en cohérence, jamais consommé aujourd'hui

`--success-foreground`/`--warning-foreground`/`--destructive-foreground` ne sont utilisés par
aucun composant aujourd'hui (recherché dans tout `.tsx`/`.ts` - zéro `text-success-foreground`,
`text-warning-foreground`, `text-destructive-foreground`; les fonds pleins existants
utilisent `text-white` codé en dur dans `button.tsx`/`badge.tsx`). Gardés cohérents par
précaution pour un usage futur :

- `--success-foreground` : reste blanc (déjà correct, `--success` clair reste assez foncé).
- `--warning-foreground` (clair) : passé de `#242127` (texte foncé) à blanc - la nouvelle
  valeur `--warning` étant plus foncée (L40% contre L54% avant), le texte foncé ne donnerait
  plus que 3.52:1 contre 4.51:1 avec du blanc.
- Valeurs sombres (`oklch(0.145 0 0)`, quasi-noir) : inchangées - les variantes sombres de
  `--success`/`--warning` restent des pastels clairs (L60%), le texte foncé y reste adapté.

## Vérification visuelle (DOM + calcul, clair et sombre)

Vérifié par lecture directe de `getComputedStyle`/attributs SVG (plus fiable que la capture
d'écran pour confirmer une valeur exacte - même méthode qu'au chantier palette-bleu-indigo) :

- **Dashboard admin** : badge de variation "Revenus" (`-19.7%`) en rosé-corail ; graphique
  Revenus/Charges (`MonthlyChart`, barres `recharts`) - barres exactement `rgb(57,127,124)`
  (`--success`) et `rgb(194,81,100)` (`--destructive`) en clair, tokens confirmés corrects en
  sombre (même variables CSS, valeurs sombres relues directement sur `:root.dark`).
- **Santé du portefeuille (Health Score)** : anneau de progression pour un score "Bon" (80/100)
  - `rgb(138,116,66)` en clair (`#8a7442`), `rgb(189,167,117)` en sombre (`#bda775`) - exacts.
- **Rentabilité par propriété** (3 `StatCard`) : Revenus et Bénéfice net (positif) en
  `#397f7c`/`#72c0bd` (succès), Charges en `#c25164`/`#c76072` (destructif) - les 3 cartes
  restent clairement distinctes visuellement, clair et sombre.
- **Badges de statut CRUD** : "Confirmee" (Réservations) en `#397f7c` ; "Non payée" (Charges) en
  `#8a7442`/`#bda775` (clair/sombre) - exacts dans les deux thèmes.
- **Tokens racine** relus directement sur `document.documentElement` en clair et en sombre :
  correspondent exactement aux valeurs du tableau ci-dessus, aucun écart.

`npm run build` : passe avant et après application (26 routes, TypeScript propre).

## Fichier modifié

- [`app/globals.css`](nextjs-app-FINAL/nextjs-app/app/globals.css) — `--success`,
  `--warning`, `--destructive`, `--destructive-text`, `--warning-foreground` (`:root` et
  `.dark`). Aucun composant `.tsx` modifié : l'architecture 100% tokens absorbe le changement
  sans aucune autre édition.
