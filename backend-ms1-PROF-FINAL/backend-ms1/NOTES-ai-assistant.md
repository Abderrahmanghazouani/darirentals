# AI Property Assistant — DariRentals

Suivi du chantier "AI Property Assistant" (branche `feature/ai-property-assistant`).

---

## Principe fondamental (rappel)

Gemini ne touche **jamais** la base de données et n'a aucune connaissance métier. Le frontend
calcule D'ABORD les vraies données (réutilise `health-score.ts`, `revenue-intelligence.ts`,
`property-performance.ts`, `action-center.ts` — les mêmes modules déjà utilisés par les cartes
du Dashboard), les assemble dans un paquet JSON ("facts"), et transmet CE PAQUET au backend, qui
le transmet tel quel à Gemini avec un prompt strict. Gemini ne fait que reformuler ou répondre à
partir de ce paquet — jamais de chiffre inventé, jamais de requête générée vers la base.

---

## Chantier 1 — Insights du matin (FAIT)

### Approche choisie

Appel Gemini qui reformule les faits en 2-3 phrases naturelles (plutôt que des phrases
pré-écrites) — plus chaleureux, et le risque d'hallucination est contenu par le prompt strict +
le fait que Gemini ne reçoit RIEN d'autre que le JSON de faits.

### Ce qui a été construit

**[`lib/dashboard/ai-facts.ts`](../../nextjs-app-FINAL/nextjs-app/lib/dashboard/ai-facts.ts)**
(frontend, nouveau) — `buildAssistantFacts()` construit le paquet `AssistantFacts` à partir des
données déjà chargées par le Dashboard, en réutilisant tel quel :
- `computeRevenueMonthComparison` (revenue-intelligence.ts) → revenu du mois / mois précédent.
- `computeMonthlyFinancials(..., 1)` → charges du mois en cours.
- `computeHealthScore` (health-score.ts) → score de santé + niveau.
- `computeOverdueTasks` / `computePendingRequests` (action-center.ts) → tâches en retard /
  demandes en attente (titre + détail déjà formatés en phrase, réutilisés tels quels).
- `computePropertyPerformance` + `sortPropertyPerformance` (property-performance.ts) → top 3
  propriétés par bénéfice net.
- Réservations à venir : même logique que `stats.upcomingReservations` dans `app/admin/page.tsx`.

Champ `currency: "MAD"` explicitement inclus dans le paquet — **découverte en testant** : sans
lui, Gemini invente un symbole monétaire (`€` observé) puisque les montants bruts n'ont pas
d'unité. Le prompt insiste maintenant explicitement pour n'utiliser que cette devise.

**[`service/ai/PropertyAssistantService.java`](src/main/java/ma/zyn/app/service/ai/PropertyAssistantService.java)**
(backend, nouveau) — mirror exact de `InvoiceScanService` (même config `gemini.api.key`/
`gemini.model`, même `HttpClient`, même pattern quota/logging) mais pour un appel texte pur
(pas d'image) : `generateMorningInsights(facts, enterpriseId, collaboratorId)` envoie un prompt
contenant le JSON de faits + une instruction stricte de reformulation sans invention, et
retourne le texte brut de Gemini.

**[`ws/facade/admin/ai/PropertyAssistantRestAdmin.java`](src/main/java/ma/zyn/app/ws/facade/admin/ai/PropertyAssistantRestAdmin.java)**
— `POST /api/admin/property-assistant/insights`, reçoit `{facts, enterpriseId}`. Pas de
vérification de permission spécifique (cohérent avec le reste des contrôleurs admin — l'admin
n'est jamais soumis à `EnterpriseAccessService`/`EffectivePermissionService`, réservés aux
collaborateurs).

**Décision de conception — quota et vue admin transverse** : le Dashboard admin n'est rattaché à
AUCUNE société unique (il agrège toutes les sociétés). `enterpriseId` est donc `null` pour ces
appels, et `checkQuota()` (mirroré depuis `InvoiceScanService`) traite déjà "pas de société" comme
"pas de limite pour l'instant" — comportement existant réutilisé tel quel, pas une nouvelle
exception. Si l'assistant doit un jour être exposé côté `/collaborator` (portefeuille d'une
société précise), le quota s'appliquera alors normalement via `enterpriseId`.

**Frontend** :
[`components/dashboard/morning-insights-card.tsx`](../../nextjs-app-FINAL/nextjs-app/components/dashboard/morning-insights-card.tsx)
(nouveau) — petite carte affichée juste sous `PremiumHeader`, au-dessus de `HealthScoreCard`.
Appelle `getMorningInsights()` une seule fois au montage (le composant n'est monté qu'une fois le
chargement du Dashboard terminé, donc `facts` est déjà définitif). États : chargement, erreur
(avec bouton "Réessayer"), résultat.
[`lib/ai-assistant-api.ts`](../../nextjs-app-FINAL/nextjs-app/lib/ai-assistant-api.ts) (nouveau)
— client HTTP, mirror de `invoice-scan-api.ts`.

### Testé le 28/08 avec de vraies données

Portefeuille réel (5 propriétés, 4 réservations, 1 charge, 1 demande en attente, 3 arrivées le
même jour) :

> « Bonjour, vos revenus du mois s'élèvent à 3 300 MAD avec un bon score de santé de 80 sur 100
> et aucune tâche en retard. Trois arrivées sont prévues pour le 1er septembre et une demande
> reste en attente de traitement. Passez une excellente journée ! »

Chaque chiffre vérifié un par un contre les autres cartes du même Dashboard (Health Score : 80
"Bon" ✓, Revenu du mois : 3 300 MAD ✓, Charges : 500 MAD — non mentionné mais présent dans le
paquet ✓, tâches en retard : 0 ✓, demandes en attente : 1 ✓, arrivées à venir : 3 le 2026-09-01
✓). Aucune hallucination détectée. Testé une seconde fois sur un onglet neuf (pas de résidu de
console) — comportement identique, zéro erreur console.

### Limites connues

- `AiUsageType` : recherché par heuristique (mot-clé "assistant"/"insight"/"portefeuille"/"chat"
  dans le code ou le libellé, même pattern que `InvoiceScanService.findInvoiceScanUsageType()`).
  Aucun type de ce genre n'existe encore en base — à créer via le CRUD générique
  (`/admin/aiUsageType`) avec un libellé contenant l'un de ces mots, sinon les logs d'usage sont
  créés avec `aiUsageType: null` (pas bloquant, juste moins catégorisé dans les rapports d'usage
  IA).
- Assistant disponible uniquement sur le Dashboard **admin** pour l'instant — le Dashboard
  `/collaborator` équivalent n'existe pas encore (jamais construit pendant le chantier design).

---

## Chantier 2 — Chat "Pose une question à ton portefeuille" (FAIT)

### Ce qui a été construit

Réutilise entièrement l'infrastructure du Chantier 1 : même paquet `AssistantFacts`
(`ai-facts.ts`), même `PropertyAssistantService` (méthode `answerQuestion`, déjà écrite en
même temps que `generateMorningInsights` puisqu'elles partagent tout le reste — quota, appel
Gemini, journalisation), même contrôleur (`POST /api/admin/property-assistant/chat`, reçoit
`{facts, question, enterpriseId}`).

**Prompt strict** (voir `answerQuestion()`) : le paquet JSON complet + la question, avec
instruction de répondre UNIQUEMENT à partir des données fournies, de dire explicitement
"je n'ai pas cette information" plutôt que d'inventer, d'utiliser uniquement la devise du
champ `currency`, et de refuser poliment toute question hors gestion de portefeuille (météo,
actualités...).

**Frontend** :
[`components/dashboard/portfolio-chat-card.tsx`](../../nextjs-app-FINAL/nextjs-app/components/dashboard/portfolio-chat-card.tsx)
(nouveau) — champ de saisie + historique des échanges (question/réponse) de la session,
affichée sous le Health Score sur le Dashboard. Utilise `askPortfolioQuestion()`
(`lib/ai-assistant-api.ts`, déjà écrit au Chantier 1).

### Testé le 28/08 avec de vraies questions, sur le vrai portefeuille (via l'UI réelle)

| Question | Réponse | Vérification |
|---|---|---|
| « Combien j'ai gagné ce mois-ci ? » | « Ce mois-ci, vos revenus s'élèvent à 3300 MAD. Après déduction des charges du mois en cours qui sont de 500 MAD, votre profit net s'élève à 2800 MAD. » | 3300 et 500 vérifiés contre le Dashboard ; 2800 = simple soustraction correcte, pas un chiffre inventé |
| « Quelles tâches sont en retard ? » | « Il n'y a actuellement aucune tâche en retard dans le portefeuille. » | Correct : 0/3 tâches en retard (Health Score) |
| « Quel est le classement des propriétés par bénéfice ? » | Classement des 3 propriétés de `topProperties` avec leurs vrais montants | Correspond exactement au paquet envoyé |
| **« Quel temps fait-il aujourd'hui ? »** (hors périmètre) | « Je suis désolé, mais je ne peux pas répondre à cette question. Mon rôle est strictement limité aux informations concernant la gestion de votre portefeuille immobilier. » | **Refus propre confirmé** — aucune tentative de répondre |
| **« Combien gagne mon voisin avec ses locations ? »** (semble du domaine, mais absent des données) | « Je ne dispose pas de cette information. Les données fournies concernent uniquement votre portefeuille immobilier et ne contiennent aucune donnée sur les revenus de votre voisin. » | **Aucune invention** — distinction correcte entre "hors sujet" et "dans le sujet mais absent des données" |

### Quota et journalisation — vérifiés avec un vrai `AiQuota` configuré

La vue Dashboard admin n'a pas de société unique (`enterpriseId: null` toujours envoyé), donc
`checkQuota()` ne s'applique jamais dans l'usage réel actuel (voir Chantier 1 - comportement
volontaire, pas un oubli). Pour vérifier que le MÉCANISME lui-même fonctionne bien (identique à
`InvoiceScanService`), testé directement en forçant un `enterpriseId` réel dans la requête :

- **Société A**, quota délibérément fixé à 1 token alors que 2864 tokens étaient déjà
  consommés (tests scan-facture antérieurs) → **403... `429 Too Many Requests`** avec le message
  exact `"Quota IA épuisé pour cette société (2864/1 tokens utilisés)."` — appel Gemini jamais
  déclenché.
- **Société B**, quota généreux (100 000 tokens), 0 usage préalable → `200 OK`, réponse
  correcte, `tokensUsed: 996`.
- Vérifié ensuite via `GET /api/admin/aiUsageLog/enterprise/id/4` : une nouvelle ligne
  `AiUsageLog` (id 12, 996 tokens, société B) a bien été créée immédiatement après l'appel —
  journalisation confirmée, même mécanisme que le scan de facture.
- Les 2 `AiQuota` de test ont été supprimés après vérification (sinon la Société A restait
  bloquée en permanence sur le scan de facture à cause du quota artificiel de 1 token).

### Limites connues

- Comme documenté au Chantier 1, l'admin Dashboard n'a pas de société unique : le quota par
  société ne s'applique donc jamais en usage réel actuel (seulement vérifié manuellement
  ci-dessus). S'applique normalement si l'assistant est un jour exposé côté `/collaborator`.
- **Effet observé de React StrictMode (dev uniquement)** : les insights du matin déclenchent
  parfois 2 appels Gemini au montage (React invoque l'effet deux fois en dev pour détecter les
  bugs de cleanup manquant) — visible dans `AiUsageLog` sous forme de 2 lignes créées à la même
  seconde. N'affecte ni la justesse de la réponse affichée, ni le comportement en production
  (`next build` n'a pas ce double-appel) — juste un coût de tokens ~doublé en dev. Pas corrigé
  (mineur, cohérent avec la fragilité StrictMode déjà documentée dans NOTES-permissions.md pour
  un autre composant) ; à garder à l'œil si le volume de test augmente.
- Le chat ne conserve pas d'historique de conversation entre les questions (chaque question est
  envoyée indépendamment avec le même paquet `facts` figé au chargement de la page) — cohérent
  avec le principe "aucune connaissance au-delà des faits fournis", mais signifie qu'une question
  de suivi ("et le mois d'avant ?") ne bénéficie pas du contexte de la question précédente.
