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

## Chantier 2 — Chat "Pose une question à ton portefeuille"

En cours.
