# Patch — Rentabilité par propriété

## Comment appliquer

```powershell
git checkout main
git pull origin main
git checkout -b feature/rentabilite-propriete
```

Extrais ce zip **à la racine du repo** (écrase les fichiers qui se
recoupent : `Charge.java`, `ChargeDto.java`, `ChargeConverter.java`,
`charge-form.tsx`, `invoice-scan-dialog.tsx`, `Charge.ts`,
`entity-registry.ts`, `app/admin/property/page.tsx` ; ajoute le nouveau
dossier `app/admin/property/[id]/rentabilite/`).

## Fichiers modifiés

- `Charge.java` / `ChargeDto.java` / `ChargeConverter.java` — ajout du champ
  `chargeDate` (n'existait pas encore, nécessaire pour le filtre par période)
- `components/charges/charge-form.tsx` — champ "Date de la charge" ajouté
  (pré-rempli à aujourd'hui pour les nouvelles charges)
- `components/charges/invoice-scan-dialog.tsx` — la date extraite par l'IA
  pré-remplit maintenant aussi `chargeDate`
- `lib/types/Charge.ts` / `lib/entity-registry.ts` — `chargeDate` propagé
  (bonus : le CRUD générique `/admin/charge` en profite aussi)
- `app/admin/property/page.tsx` — colonne "Rentabilité" ajoutée avec lien
  vers la nouvelle fiche

## Fichier créé

- `app/admin/property/[id]/rentabilite/page.tsx` — écran principal

## Comment ça marche

Calcul **entièrement côté client** (fetch `findAll()` + filtre + `reduce()`),
même logique que `stats.totalRevenue` sur le dashboard — pas de nouvel
endpoint backend pour l'agrégation :

1. `GET .../property/id/{id}` pour le nom de la propriété
2. `GET .../reservation` + `GET .../charge` (listes complètes), filtrées
   côté client sur `property.id === id`
3. Sélecteur de période (mois / année / tout) applique une borne de date
   supplémentaire sur `checkInDate` (réservations) et `chargeDate` (charges)
4. Bénéfice net = revenus − charges, affiché en vert/rouge selon le signe

## ⚠️ Point d'attention : charges existantes sans date

`chargeDate` est un champ tout neuf — les charges créées **avant** ce patch
n'en ont pas. Elles restent visibles dans "Tout l'historique" mais sont
exclues des filtres "Ce mois-ci"/"Cette année" (impossible de savoir à
quelle période elles appartiennent). Une note s'affiche à l'utilisateur dans
ce cas plutôt que de les faire disparaître silencieusement.

## Comment tester

1. `/admin/property` → clique "Voir" dans la colonne Rentabilité d'une
   propriété qui a déjà des réservations/charges
2. Vérifie que les 3 cartes (Revenus/Charges/Bénéfice net) affichent des
   totaux cohérents avec le détail listé en dessous
3. Bascule les 3 filtres de période, vérifie que les totaux et les tableaux
   se mettent à jour
4. Crée une nouvelle charge avec une date → vérifie qu'elle apparaît bien
   dans le filtre "Ce mois-ci"
5. Si tu as des charges anciennes (sans date) : vérifie qu'elles
   n'apparaissent que dans "Tout l'historique", avec la note explicative
