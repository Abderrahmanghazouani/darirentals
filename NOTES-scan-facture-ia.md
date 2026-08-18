# Patch — Scan de facture par IA (OpenAI)

## Comment appliquer

```powershell
git checkout main
git pull origin main
git checkout -b feature/scan-facture-ia
```

Extrais ce zip **à la racine du repo** (écrase les fichiers existants qui se
recoupent — `Document.java`, `application-dev.properties`, `Document.ts`,
`app/admin/charges/page.tsx`) :

```powershell
Expand-Archive -Path chemin\vers\scan-facture-ia-patch.zip -DestinationPath . -Force
```

## Fichiers modifiés

- `Document.java` / `DocumentDto.java` / `DocumentConverter.java` — ajout du
  champ `extractedDate` (manquant, comme `checkInDate`/`dueDate` avant)
- `application-dev.properties` — config OpenAI (clé vide, voir plus bas)
- `lib/types/Document.ts` — idem côté TS
- `app/admin/charges/page.tsx` — bouton "Scanner une facture"

## Fichiers créés

**Backend**
- `zynerator/exception/AiQuotaExceededException.java`
- `ws/dto/ai/InvoiceScanResultDto.java`
- `service/ai/InvoiceScanService.java` — le cœur : quota, stockage local,
  appel OpenAI Vision, journalisation `AiUsageLog`
- `ws/facade/admin/ai/InvoiceScanRestAdmin.java`
- `ws/facade/collaborator/ai/InvoiceScanRestCollaborator.java`

**Frontend**
- `lib/invoice-scan-api.ts`
- `lib/use-current-collaborator.ts`
- `components/charges/invoice-scan-dialog.tsx`

## ⚠️ Avant de tester : la clé OpenAI

La clé reste **strictement backend**, jamais dans le code frontend. Elle se
configure via une variable d'environnement (`OPENAI_API_KEY`), lue dans
`application-dev.properties` :
```properties
openai.api.key=${OPENAI_API_KEY:}
openai.model=${OPENAI_MODEL:gpt-4o-mini}
```

Pour la définir avant de lancer le backend :
```powershell
$env:OPENAI_API_KEY = "sk-..."
.\mvnw spring-boot:run
```
Sans clé définie, le scan répond proprement une erreur 503 ("pas encore
configuré") — le reste du formulaire (saisie manuelle) continue de marcher
normalement.

## Limitation connue : images uniquement, pas de PDF

Le scan accepte JPG/PNG/WEBP mais **pas les PDF** pour le moment — convertir
un PDF en image nécessiterait du code de rendu que je n'ai pas pu tester
dans mon environnement (pas d'accès Maven). Si quelqu'un upload un PDF, un
message clair l'invite à utiliser une photo ou le formulaire manuel. Peut
être ajouté plus tard (`itextpdf` est déjà une dépendance du projet).

## Page collaborateur

`app/collaborator/charges/page.tsx` n'existe pas encore dans le snapshot
utilisé pour ce patch (branche `feature/multi-societe-filtrage` pas encore
mergée). Une fois mergée, il faudra reporter le même bouton + `InvoiceScanDialog`
dessus (identique à ce qui a été fait sur `app/admin/charges/page.tsx`,
juste `ROLE = "collaborator"`).

## Comment ça marche, en bref

1. Upload d'une image de facture → `POST /api/{role}/invoice-scan/analyze`
2. Le backend vérifie le quota (`AiQuota` vs somme des `AiUsageLog` de la
   société), stocke le fichier dans `uploads/invoices/`, appelle OpenAI,
   journalise l'usage réel (que l'utilisateur valide ensuite ou non — le
   coût est déjà engagé dès l'appel IA)
3. Le frontend affiche le formulaire de charge (`ChargeForm`, réutilisé tel
   quel) pré-rempli avec ce que l'IA a trouvé — l'utilisateur corrige/valide
4. À la validation : crée la `Charge`, puis archive le `Document` (fichier +
   valeurs extraites) rattaché à cette charge
5. Le bouton "Nouvelle charge" (100% manuel) continue de fonctionner
   normalement, en parallèle

## Test rapide

1. `/admin/charges` → "Scanner une facture"
2. Upload une photo de facture (eau/électricité...)
3. Vérifie que le formulaire se pré-remplit, corrige si besoin, valide
4. Vérifie que la charge apparaît dans la liste
5. Vérifie côté `/admin/document` (CRUD générique) que le document est bien
   archivé et rattaché à la charge
6. Vérifie côté `/admin/aiUsageLog` qu'une ligne d'usage a été journalisée
7. Teste le cas d'erreur : upload un PDF → message clair, pas de crash
8. Teste sans clé configurée → message clair "pas encore configuré"
