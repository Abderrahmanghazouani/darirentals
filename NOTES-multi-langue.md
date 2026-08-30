# Multi-langue FR/EN — DariRentals

Suivi du chantier "Multi-langue FR/EN" (branche `feature/multi-langue`). Chantier 100%
frontend — aucun changement backend.

---

## TODO (hors périmètre de ce chantier, à traiter plus tard)

- **Débordement horizontal mobile (375px) sur `/admin`, carte "Performance des propriétés".**
  `document.documentElement.scrollWidth` (581px) dépasse `clientWidth` (375px). Vérifié
  explicitement **indépendant de la langue** (identique en FR et en EN) — donc pas une
  régression de ce chantier de traduction. Cause probable : le tableau a grossi avec les
  données de test accumulées au fil de la session (plusieurs noms de propriété de 15+
  caractères créés depuis le dernier polish mobile, ex. "Non Regression A", "Prop legitime A2").
  À reprendre dans un futur passage dédié au responsive (voir aussi le détail en fin de section
  "Étape 3 — Dashboard admin + Étape 4 — Navigation" plus bas).

---

## Étape 0 — Choix technique (FAIT)

**Stack** : Next.js 16.2.12, App Router, React 19.2.4. Aucune lib d'i18n préexistante.

**Décision : pas de next-intl, solution maison (zéro dépendance npm ajoutée).**

Raison principale : **toutes** les pages/composants de ce projet sont `"use client"` — aucun
Server Component. La valeur ajoutée de next-intl (`getTranslations()` côté serveur, routing par
locale via `middleware.ts` + segment `app/[locale]/...`) ne s'applique donc à rien ici. Le mode
routing aurait en plus obligé à déplacer **toutes** les routes existantes sous
`app/[locale]/...`, touchant `router.push()`, les redirections de `useRequireRole`, tous les
`href` du projet — risque élevé pour un gain nul, alors que le besoin réel est juste "FR/EN, pas
de routing par locale" (confirmé par l'énoncé).

À la place : mirror exact du pattern déjà utilisé pour `CurrencyProvider`/`useCurrency()`
(`lib/currency/currency-context.tsx`) — Context React + persistance `localStorage`, 100% client,
déjà éprouvé dans ce même codebase.

### Structure mise en place

- **[`lib/i18n/translations.ts`](nextjs-app-FINAL/nextjs-app/lib/i18n/translations.ts)** —
  dictionnaires `fr`/`en`, un objet `Dict` par langue. Le type `Dict` est dérivé de `fr` mais
  force chaque feuille à `string` (pas la valeur littérale) : `en` doit avoir EXACTEMENT les
  mêmes clés que `fr`, sinon erreur de compilation TypeScript — impossible d'oublier une
  traduction sans que `npm run build` échoue.
- **[`lib/i18n/language-context.tsx`](nextjs-app-FINAL/nextjs-app/lib/i18n/language-context.tsx)**
  — `LanguageProvider` + `useLanguage()` → `{ locale, setLocale, dict }`. Persisté dans
  `localStorage` (clé `"language"`), défaut `"fr"`. Posé **une seule fois, à la racine**
  (`app/layout.tsx`), donc disponible sur toutes les pages sans rien répéter.
- **[`components/i18n/language-toggle.tsx`](nextjs-app-FINAL/nextjs-app/components/i18n/language-toggle.tsx)**
  — toggle FR/EN texte (pas de drapeaux : l'anglais n'a pas de drapeau unique représentatif),
  même style visuel que le toggle Calendrier/Liste déjà utilisé sur `/collaborator/reservations`.

**Usage dans une page** : `const { dict } = useLanguage();` puis `dict.reserver.heroTitle`, etc.
— accès direct typé, pas de lookup par chaîne (`t("reserver.heroTitle")`), donc pas de faute de
frappe possible sur une clé qui ne casserait qu'à l'exécution.

**Ce qui n'est JAMAIS traduit** (rappel du périmètre) : les données elles-mêmes (noms de
propriétés, montants, statuts venant de la base), le contenu généré par l'IA (insights,
réponses du chat).

---

## Étape 1 — `/reserver` (FAIT)

Page publique traduite en entier : titre, sous-titre, sélecteur de devise ("Afficher les prix
en :"), cartes propriété ("personnes", "Voir la position", "/ nuit", "Demander"), et toute la
boîte de dialogue de demande (titres, labels de champs, message de succès, erreurs de
validation, boutons). Toggle FR/EN ajouté **en haut de la page**, bien visible, comme demandé
(public visé via un lien Instagram, potentiellement non-francophone).

### Testé le 28/08, avec de vraies données, dans le navigateur

- Chargement FR : propriétés réelles affichées correctement (noms, prix, capacité) - aucune
  régression.
- Bascule vers EN : tous les libellés fixes traduits (`Our available properties`,
  `Choose a property and send us your request...`, `Show prices in:`, `people`,
  `View location`, `/ night`, `Request`) — noms de propriétés et prix restés inchangés (données,
  pas de l'UI), conforme au principe.
- Dialogue de demande en EN : `Request for: Riad Societe A`, `Check-in`/`Check-out`,
  `Full name`, `Phone`, `Message (optional)`, `Cancel`/`Send request`.
- Erreur de validation (champs vides) en EN : `Please provide at least your name and phone
  number.`
- **Soumission réelle en anglais** : succès affiché (`Request sent` / `Thank you!` / `Your
  request has been sent. We'll contact you shortly at +212600000000.`), et vérifié côté API
  admin (`GET /api/admin/reservationRequest/id/7`) qu'une vraie `ReservationRequest` a bien été
  créée avec `client.fullName: "Test EN User"`, `client.phone: "+212600000000"` — la traduction
  ne casse aucune fonctionnalité du formulaire.
- Retour FR : bascule immédiate, tous les libellés redeviennent français.
- **Persistance testée** : rechargement de page après avoir choisi EN → la page reste en
  anglais (lecture `localStorage` au montage).
- Mobile 375px : aucun débordement horizontal (`scrollWidth === clientWidth === 375`).
- Zéro erreur console (vérifié sur onglet neuf).

`npm run build` passe sans erreur (TypeScript inclus).

---

## Étape 2 — `/login` (FAIT)

Page traduite : titre "Connexion"/"Login", labels des champs, bouton (avec état "Connexion..."/
"Signing in..."), message d'erreur. Toggle FR/EN ajouté en haut à droite.

### Bug trouvé et corrigé en testant : message d'erreur figé en français malgré le toggle EN

En basculant vers EN puis en testant un login raté, le message d'erreur restait **"Échec de la
connexion"** (français) au lieu de "Login failed". Cause tracée : `lib/auth.ts` avait un texte
français codé en dur comme valeur par défaut (`let message = "Échec de la connexion"`), utilisé
quand le serveur ne renvoie pas de JSON exploitable — ce qui est justement le cas ici : Spring
Security répond en texte brut sur un login raté
(`Login failed: UserDetailsService returned null...`, pas un JSON avec un champ `message`), donc
le `catch` de `auth.ts` tombait systématiquement sur son texte français par défaut, quelle que
soit la langue choisie côté frontend.

**Corrigé à la source** (pas dans la page) :
- `lib/auth.ts` ne code plus aucun texte utilisateur en dur. Nouvelle classe `LoginError` avec
  un `code` (`"server"` | `"unrecognized_role"`) — c'est `/login/page.tsx` qui décide du texte
  affiché (traduit) selon ce code, jamais `auth.ts`.
- Même correction appliquée à `lib/public-api.ts` (`submitReservationRequest`), qui avait
  exactement le même défaut (`"Erreur lors de l'envoi de la demande"` codé en dur) — utilisé par
  `/reserver`, déjà traduit à l'Étape 1 mais avec la même faille latente non détectée à l'époque
  (le test précédent n'avait pas déclenché ce chemin d'erreur précis).

**Deuxième bug trouvé en creusant celui-ci, corrigé en même temps** : même après cette
correction, un message d'erreur déjà affiché **restait figé dans l'ancienne langue** si
l'utilisateur changeait de langue APRÈS avoir vu l'erreur (le texte traduit était stocké tel
quel dans le state React au moment de l'erreur, pas recalculé au re-render). Corrigé sur
`/login` et `/reserver` : le state stocke maintenant la **nature** de l'erreur
(`"unrecognizedRole" | "server" | "generic"` / `"missingFields" | "server" | "generic"`), et le
texte affiché est dérivé du dictionnaire courant à chaque rendu — un changement de langue après
coup retraduit immédiatement le message déjà affiché. Un message renvoyé tel quel par le serveur
(cas `"server"`) reste dans sa langue d'origine (aucune traduction possible côté frontend pour
un texte qui vient du backend) — limite assumée, documentée ici.

### Testé le 30/08, dans le navigateur

- Login raté en FR : "Échec de la connexion" ✓. Bascule vers EN **sans resoumettre** : le
  message se retraduit immédiatement en "Login failed" ✓ (confirme le fix de réactivité).
- Même test sur `/reserver` (champs vides, erreur de validation) : bascule EN→FR après coup,
  le message passe de "Please provide at least your name and phone number." à "Renseigne au
  moins ton nom et ton téléphone." ✓.
- Soumission réelle réussie en FR sur `/reserver` (nouvelle demande créée) après tous ces
  changements : "Merci !" / "Votre demande a bien été envoyée..." — aucune régression sur le
  chemin nominal.
- **Vrai login réussi** (admin/123) après toutes les corrections : redirection normale vers le
  Dashboard, aucune régression.
- Zéro erreur console (onglet neuf, `/login` et `/reserver`).

`npm run build` passe (TypeScript inclus - `LoginError` et les types d'union `errorKind` sont
bien vérifiés).

## Étape 3 — Dashboard admin + Étape 4 — Navigation (FAIT)

Traitées ensemble : les deux vivent sur la même page (`/admin`), et "Outils"/"Tous les modules"
(navigation, étape 4) sont des sections de `app/admin/page.tsx`.

### Portée réelle : plus large que le seul `page.tsx`

L'énoncé parlait de "Dashboard principal (/admin) - les libellés fixes (titres de cartes,
labels)" : ça inclut donc aussi les **sous-composants carte** du Dashboard, pas seulement le
JSX de `page.tsx`. Traduits : `PremiumHeader` (titre, "Déconnexion", nom de compte par défaut,
date longue localisée fr-FR/en-US), `HealthScoreCard` (titre), `RevenueIntelligenceCard`
(titre, badge "Nouveau", libellé), `PropertyPerformanceCard` (titre, tri, en-têtes de colonnes,
statut inconnu), `ActionCenterCard` (titre, état vide, "+N autres"), et `page.tsx` lui-même
(cartes de stats, "Outils" + ses 7 libellés, "Prochaines arrivées", "Tous les modules",
"Afficher"/"Masquer").

**Ajout non prévu à l'origine, ajouté pour cohérence** : le CHROME fixe des cartes AI Property
Assistant (titre "Pose une question à ton portefeuille"/"Ask your portfolio a question",
placeholder, bouton, texte d'aide, messages de chargement/erreur) — l'énoncé excluait
explicitement "le contenu généré dynamiquement par l'IA (insights du matin, chat)", ce qui vise
le TEXTE GÉNÉRÉ par Gemini, pas le titre/bouton/placeholder autour. Le message effectivement
généré par Gemini (l'insight du matin, chaque réponse du chat) reste toujours en français, quel
que soit le toggle — vérifié en le déclenchant réellement (voir tests ci-dessous).

### Décision de conception : les modules de calcul (`lib/dashboard/*.ts`) ne sont PAS touchés

`health-score.ts` (labels + phrases `detail` des 4 composants, ex: "Marge du mois en cours :
84.8%..."), `revenue-intelligence.ts` (phrase `comparison.summary`, libellés de période "7
jours"/"30 jours"...), `action-center.ts` (`title`/`subtitle` de chaque tâche/demande, avec
dates et noms de propriété déjà interpolés dedans) restent volontairement en français. Deux
raisons : (1) ces fonctions mélangent calcul et texte français directement dans la valeur
retournée - les traduire demanderait de leur faire porter la langue en paramètre, un
changement de leur signature plus risqué que le reste du chantier ; (2) `lib/dashboard/ai-facts.ts`
(AI Property Assistant) consomme CES MÊMES valeurs pour construire le paquet envoyé à Gemini -
les rendre dépendantes de la langue de l'UI aurait couplé le toggle FR/EN au comportement de
l'assistant IA, que l'énoncé demande explicitement de laisser en français pour l'instant.
Résultat visible : le badge de niveau ("Bon"/"Excellent"...), le détail de chaque composante du
score, la phrase de comparaison de revenu, et les libellés de la période du graphique restent
en français même en mode EN - assumé et documenté ici plutôt que laissé comme un oubli silencieux.

**Non touché non plus, pour la même raison de portée** : `entityRegistry` (labels des 37
entités listées dans "Tous les modules") - ce sont des libellés techniques auto-générés à partir
des noms d'entité (ex: "Ai Quota", "Charge Type"), déjà proches de l'anglais et explicitement
hors périmètre ("ne traduis pas les écrans CRUD génériques").

### Bugs trouvés et corrigés en testant (même famille que l'Étape 2)

`lib/ai-assistant-api.ts` avait le même défaut que `lib/auth.ts`/`lib/public-api.ts` : un
message de secours codé en dur (`` `Erreur ${res.status}` ``) quand le serveur ne renvoie pas de
JSON exploitable. Corrigé à la source (message vide, la carte affiche son propre texte traduit).
`MorningInsightsCard` et `PortfolioChatCard` appliquent le même pattern "nature de l'erreur en
state, texte dérivé du dictionnaire courant" que `/login`/`/reserver` (Étape 2) - y compris pour
`PortfolioChatCard`, où **chaque échange de l'historique** retraduit son propre message d'erreur
si la langue change après coup, pas seulement le dernier.

### Testé le 30/08, avec de vraies données, dans le navigateur

- FR → EN sur un vrai portefeuille (5 propriétés, 8 clients, 3 demandes en attente, 3 arrivées
  le même jour) : en-tête ("Dashboard · 1 active property · Sunday, August 30, 2026" - date
  longue bien en anglais), les 4 cartes de stats, "Portfolio health", "Revenue"/"New"/"current
  month revenue", "Property performance" (tri + 5 en-têtes de colonnes + "Unknown status"),
  "Tools" (7 libellés), "Upcoming arrivals"/"Unknown client"/"arriving on", "Action center",
  "All modules"/"Show" — tous corrects. Vérifié dans l'autre sens (EN → FR) aussi.
- Chrome de l'assistant IA en EN : "Ask your portfolio a question", bouton "Ask", placeholder
  "E.g.: how much did I earn this month?", texte d'aide — tous traduits.
- **Vraie question posée en anglais** ("How much did I earn this month?") : le quota gratuit
  Gemini était épuisé au moment du test (20 requêtes/jour, consommées par les tests répétés de
  cette session) → 429 remonté en 502 par le contrôleur (comportement déjà existant, voir
  NOTES-ai-assistant.md), message serveur affiché tel quel (en anglais ici, car c'est le message
  d'erreur brut de l'API Gemini) - confirme que le message serveur n'est jamais retraduit, par
  design. Un peu plus tôt dans la session, avant épuisement du quota, l'insight du matin
  généré par Gemini s'est affiché normalement **en français alors que l'UI était en anglais** -
  confirme que le contenu généré reste bien figé en français, comme demandé.
- Bascule EN → FR **après** l'erreur de quota : le chrome de la carte repasse en français
  ("Pose une question à ton portefeuille", "Demander"), la question déjà posée (tapée par
  l'utilisateur) et le message d'erreur serveur restent inchangés (ni l'un ni l'autre n'est un
  libellé fixe de l'UI) — comportement correct.
- Zéro erreur console liée au travail de traduction (onglet neuf) — deux `502` observés sur
  l'onglet neuf proviennent de l'appel insights (déclenché deux fois par le double-montage
  React StrictMode, déjà documenté dans NOTES-ai-assistant.md) retombant sur le même quota
  Gemini épuisé, pas d'une erreur introduite ici.

### Point trouvé en testant le mobile, hors périmètre de ce chantier

À 375px, `document.documentElement.scrollWidth` (581px) dépasse `clientWidth` (375px) —
débordement horizontal réel sur `/admin`. **Vérifié explicitement que c'est indépendant de la
langue** (identique en FR et en EN) : ce n'est donc pas une régression introduite par cette
couche de traduction. Cause probable : le tableau "Performance des propriétés" a grossi au fil
des données de test accumulées pendant la session (plusieurs noms de propriété de 15+
caractères créés depuis le dernier polish mobile, ex. "Non Regression A", "Prop legitime A2").
Non corrigé ici (hors périmètre du chantier multi-langue) — signalé pour un futur passage si
besoin.

`npm run build` passe (TypeScript inclus) à chaque étape de ce chantier.
