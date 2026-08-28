package ma.zyn.app.service.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.service.facade.admin.ai.AiQuotaAdminService;
import ma.zyn.app.service.facade.admin.ai.AiUsageLogAdminService;
import ma.zyn.app.service.facade.admin.ai.AiUsageTypeAdminService;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseAdminService;
import ma.zyn.app.ws.dto.ai.AssistantResponseDto;
import ma.zyn.app.zynerator.exception.AiQuotaExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * AI Property Assistant : insights du matin + chat "pose une question a ton portefeuille".
 *
 * PRINCIPE FONDAMENTAL (voir NOTES-ai-assistant.md) : ce service ne touche JAMAIS la base de
 * donnees et n'a aucune connaissance metier. Le frontend calcule D'ABORD les vraies donnees
 * (reutilise health-score.ts/revenue-intelligence.ts/property-performance.ts/action-center.ts)
 * et les transmet ici sous forme d'un paquet JSON deja verifie ("facts"). Gemini ne fait que
 * reformuler ce paquet en langage naturel (insights) ou repondre a une question a partir de
 * lui (chat) - jamais de chiffre invente, jamais de requete SQL/JPA generee par l'IA.
 *
 * Reutilise exactement la meme configuration/cle que InvoiceScanService (gemini.api.key).
 */
@Service
public class PropertyAssistantService {

    @Value("${gemini.api.key:}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-2.0-flash}")
    private String geminiModel;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(15))
            .build();

    @Autowired
    private AiQuotaAdminService aiQuotaService;
    @Autowired
    private AiUsageLogAdminService aiUsageLogService;
    @Autowired
    private AiUsageTypeAdminService aiUsageTypeService;
    @Autowired
    private EnterpriseAdminService enterpriseService;
    @Autowired
    private ObjectMapper objectMapper;

    public AssistantResponseDto generateMorningInsights(JsonNode facts, Long enterpriseId, Long collaboratorId) throws IOException {
        requireFacts(facts);
        checkQuota(enterpriseId);

        String prompt = "Tu es l'assistant d'un gestionnaire de locations saisonnieres. Voici un paquet de "
                + "donnees REELLES et DEJA VERIFIEES sur son portefeuille, au format JSON :\n\n" + facts.toString()
                + "\n\nRedige un court message d'accueil du matin, chaleureux et naturel, en francais, de 2 a 3 "
                + "phrases maximum, qui resume les points les plus utiles de ces donnees (evolution du revenu, "
                + "arrivees a venir, taches en retard, etc. - uniquement ce qui est pertinent et present dans les "
                + "donnees). N'invente STRICTEMENT AUCUN chiffre, nom ou fait qui ne figure pas dans le JSON "
                + "ci-dessus. Utilise UNIQUEMENT la devise indiquee dans le champ \"currency\" du JSON (jamais "
                + "un symbole monetaire different, ex: pas de €, $, etc. si ce n'est pas la devise indiquee). "
                + "Reponds uniquement avec le texte du message, sans introduction, sans markdown, sans "
                + "guillemets.";

        return callAndLog(prompt, enterpriseId, collaboratorId);
    }

    public AssistantResponseDto answerQuestion(JsonNode facts, String question, Long enterpriseId, Long collaboratorId) throws IOException {
        requireFacts(facts);
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("Aucune question reçue.");
        }
        checkQuota(enterpriseId);

        String prompt = "Tu es l'assistant d'un gestionnaire de locations saisonnieres. Voici un paquet de "
                + "donnees REELLES et DEJA VERIFIEES sur son portefeuille, au format JSON :\n\n" + facts.toString()
                + "\n\nReponds a la question suivante UNIQUEMENT a partir des donnees ci-dessus, en francais, de "
                + "maniere concise (quelques phrases maximum). Si l'information demandee n'y figure pas, dis "
                + "clairement que tu ne disposes pas de cette information plutot que de l'inventer ou de "
                + "l'estimer. N'invente STRICTEMENT AUCUN chiffre, nom ou fait absent du JSON ci-dessus. Utilise "
                + "UNIQUEMENT la devise indiquee dans le champ \"currency\" du JSON (jamais un symbole monetaire "
                + "different). Ne reponds a aucune question sans rapport avec la gestion du portefeuille "
                + "immobilier (meteo, actualites, culture generale...) - explique poliment que tu es limite au "
                + "portefeuille.\n\n"
                + "Question : " + question;

        return callAndLog(prompt, enterpriseId, collaboratorId);
    }

    private void requireFacts(JsonNode facts) {
        if (facts == null || facts.isNull() || facts.isMissingNode()) {
            throw new IllegalArgumentException("Aucune donnée de portefeuille reçue.");
        }
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            throw new IllegalStateException(
                    "L'assistant IA n'est pas encore configuré (clé API manquante côté serveur)."
            );
        }
    }

    private AssistantResponseDto callAndLog(String prompt, Long enterpriseId, Long collaboratorId) throws IOException {
        JsonNode geminiResponse = callGemini(prompt);

        long tokensUsed = 0;
        String message;
        try {
            JsonNode usage = geminiResponse.path("usageMetadata");
            if (usage.has("totalTokenCount")) {
                tokensUsed = usage.get("totalTokenCount").asLong();
            }
            message = geminiResponse.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
            message = message == null ? "" : message.trim();
            if (message.isEmpty()) {
                message = "Désolé, je n'ai pas pu générer de réponse. Réessaie dans un instant.";
            }
        } catch (Exception parseError) {
            message = "Désolé, je n'ai pas pu générer de réponse. Réessaie dans un instant.";
        }

        logUsage(enterpriseId, collaboratorId, tokensUsed);

        AssistantResponseDto dto = new AssistantResponseDto();
        dto.setMessage(message);
        dto.setTokensUsed(tokensUsed);
        return dto;
    }

    private void checkQuota(Long enterpriseId) {
        // Pas de societe rattachee au contexte de l'appel (ex: vue admin transverse) : pas de
        // quota par societe applicable ici - meme comportement que InvoiceScanService.checkQuota.
        if (enterpriseId == null) {
            return;
        }
        List<AiQuota> quotas = aiQuotaService.findByEnterpriseId(enterpriseId);
        if (quotas == null || quotas.isEmpty()) {
            return;
        }
        long allocated = quotas.stream()
                .map(AiQuota::getTokensAllocated)
                .filter(v -> v != null)
                .mapToLong(Long::longValue)
                .sum();

        List<AiUsageLog> logs = aiUsageLogService.findByEnterpriseId(enterpriseId);
        long used = logs == null ? 0 : logs.stream()
                .map(AiUsageLog::getTokensUsed)
                .filter(v -> v != null)
                .mapToLong(Long::longValue)
                .sum();

        if (used >= allocated) {
            throw new AiQuotaExceededException(
                    "Quota IA épuisé pour cette société (" + used + "/" + allocated + " tokens utilisés)."
            );
        }
    }

    private JsonNode callGemini(String prompt) throws IOException {
        com.fasterxml.jackson.databind.node.ObjectNode textPart = objectMapper.createObjectNode();
        textPart.put("text", prompt);

        com.fasterxml.jackson.databind.node.ArrayNode partsArray = objectMapper.createArrayNode();
        partsArray.add(textPart);

        com.fasterxml.jackson.databind.node.ObjectNode contentNode = objectMapper.createObjectNode();
        contentNode.set("parts", partsArray);

        com.fasterxml.jackson.databind.node.ArrayNode contentsArray = objectMapper.createArrayNode();
        contentsArray.add(contentNode);

        com.fasterxml.jackson.databind.node.ObjectNode rootNode = objectMapper.createObjectNode();
        rootNode.set("contents", contentsArray);

        String body = rootNode.toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/" + geminiModel + ":generateContent"))
                .header("x-goog-api-key", geminiApiKey)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(45))
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new IOException("Erreur Gemini (" + response.statusCode() + ") : " + response.body());
            }
            return objectMapper.readTree(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("Appel IA interrompu.", e);
        }
    }

    private void logUsage(Long enterpriseId, Long collaboratorId, long tokensUsed) {
        try {
            AiUsageLog log = new AiUsageLog();
            log.setTokensUsed(tokensUsed);
            log.setDate(LocalDateTime.now());
            if (enterpriseId != null) {
                Enterprise enterprise = enterpriseService.findById(enterpriseId);
                log.setEnterprise(enterprise);
            }
            if (collaboratorId != null) {
                Collaborator collaborator = new Collaborator();
                collaborator.setId(collaboratorId);
                log.setCollaborator(collaborator);
            }
            log.setAiUsageType(findAssistantUsageType());
            aiUsageLogService.create(log);
        } catch (Exception e) {
            // La journalisation ne doit jamais faire echouer la reponse pour l'utilisateur.
            System.err.println("Impossible de journaliser l'usage IA (assistant) : " + e.getMessage());
        }
    }

    // Meme heuristique que InvoiceScanService.findInvoiceScanUsageType() : les codes AiUsageType
    // ne sont pas figes (CRUD libre), donc on cherche par mot-cle plutot que par code fixe. Voir
    // NOTES-ai-assistant.md pour le libelle a creer si aucun type existant ne correspond encore.
    private AiUsageType findAssistantUsageType() {
        List<AiUsageType> types = aiUsageTypeService.findAll();
        if (types == null) {
            return null;
        }
        return types.stream()
                .filter(t -> {
                    String probe = ((t.getCode() != null ? t.getCode() : "")
                            + " " + (t.getLabel() != null ? t.getLabel() : "")).toLowerCase(Locale.ROOT);
                    return probe.contains("assistant") || probe.contains("insight") || probe.contains("portefeuille")
                            || probe.contains("chat");
                })
                .findFirst()
                .orElse(null);
    }
}
