# DariRentals — Frontend Next.js (version finale)

Ce projet remplace le frontend Angular par Next.js + Tailwind + shadcn/ui,
en conservant le **même backend Java** (aucun changement d'API nécessaire).

Vérifié à jour par rapport au frontend Angular original du prof (mêmes 38
entités, mêmes champs — comparaison automatique effectuée, 0 différence).

## Démarrage

```bash
npm install
npm run dev
```

Le fichier `.env.local` contient l'URL de l'API :
```
NEXT_PUBLIC_API_URL=http://localhost:8036/api/
```

Ouvre `http://localhost:3000` — tu seras redirigé vers `/login`.

## Comptes de test
(créés automatiquement au premier démarrage du backend)

| Rôle | Username | Mot de passe |
|---|---|---|
| Admin | `admin` | `123` |
| Collaborateur | `collaborator` | `123` |

## Architecture

Plutôt que d'écrire 37 entités × 2 rôles à la main, le projet génère
automatiquement les écrans CRUD à partir des modèles Angular d'origine :

- **`lib/types/*.ts`** — un type TypeScript par entité (généré depuis les
  `*.model.ts` Angular ; scripts dans `scripts/`)
- **`lib/entity-registry.ts`** — registre décrivant chaque entité : champs
  simples et relations (clés étrangères)
- **`lib/auth.ts`** — login, stockage du JWT, décodage du rôle
- **`lib/api-client.ts`** — client HTTP générique (mêmes routes REST que
  l'ancien frontend Angular), avec le token JWT attaché automatiquement
- **`lib/use-entity-crud.ts`** — hook générique gérant liste/CRUD/erreurs
  (redirige vers `/login` si session expirée)
- **`lib/use-require-role.ts`** — protège une page selon le rôle connecté
- **`components/crud/`** — table, formulaire et colonnes générés
  automatiquement à partir des métadonnées d'entité
- **`app/admin/[entity]/page.tsx`** et **`app/collaborator/[entity]/page.tsx`**
  — une seule page dynamique dessert les 37 entités pour chaque rôle
- **`app/login/page.tsx`** — connexion

## Ce qui est fait

- ✅ Authentification (login, JWT, protection des routes par rôle)
- ✅ Les 37 entités : liste, création, édition, suppression
- ✅ Relations simples (clé étrangère) en menu déroulant
- ✅ Mêmes routes API que l'ancien frontend Angular

## Ce qui reste à faire (pistes pour la suite)

- Permissions fines par action (`hasActionPermission` côté Angular)
- Relations multiples (listes imbriquées) dans les formulaires
- Écrans sur-mesure : dashboard, exports Excel/PDF, calendrier de
  réservations, upload de documents, paiement Stripe
- Inscription (register) et activation de compte côté frontend
- Sélection de société active (multi-entreprise, phase 2 du CDC)
