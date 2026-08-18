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
import ma.zyn.app.ws.dto.ai.InvoiceScanResultDto;
import ma.zyn.app.zynerator.exception.AiQuotaExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Analyse une facture (image) via l'API OpenAI (vision) pour pré-remplir un formulaire de charge.
 * Ne crée JAMAIS de Charge ni de Document : ça reste à la charge du frontend, une fois que
 * l'utilisateur a vérifié/corrigé les valeurs proposées et validé lui-même.
 *
 * La clé API (openai.api.key) reste strictement côté serveur — jamais renvoyée au frontend.
 */
@Service
public class InvoiceScanService {

    private static final List<String> SUPPORTED_IMAGE_TYPES = List.of(
            "image/png", "image/jpeg", "image/jpg", "image/webp"
    );

    @Value("${openai.api.key:}")
    private String openAiApiKey;

    @Value("${openai.model:gpt-4o-mini}")
    private String openAiModel;

    @Value("${invoice.upload.dir:uploads/invoices}")
    private String uploadDir;

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

    public InvoiceScanResultDto analyze(MultipartFile file, Long enterpriseId, Long collaboratorId) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Aucun fichier reçu.");
        }
        String contentType = file.getContentType() != null ? file.getContentType().toLowerCase(Locale.ROOT) : "";
        if (!SUPPORTED_IMAGE_TYPES.contains(contentType)) {
            throw new IllegalArgumentException(
                    "Le scan automatique ne supporte que les images (JPG, PNG, WEBP) pour le moment. "
                            + "Pour un PDF ou si le scan échoue, utilise le formulaire manuel."
            );
        }
        if (openAiApiKey == null || openAiApiKey.isBlank()) {
            throw new IllegalStateException(
                    "Le scan par IA n'est pas encore configuré (clé API manquante côté serveur)."
            );
        }

        checkQuota(enterpriseId);

        // 1. Sauvegarde locale du fichier original (conservé comme preuve, indépendamment du résultat IA).
        String storedRelativePath = storeFile(file);

        // 2. Appel OpenAI Vision.
        byte[] bytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        JsonNode openAiResponse = callOpenAi(base64, contentType);

        InvoiceScanResultDto result = new InvoiceScanResultDto();
        result.setDocumentToken(storedRelativePath);
        result.setFileName(file.getOriginalFilename());

        long tokensUsed = 0;
        try {
            JsonNode usage = openAiResponse.path("usage");
            if (usage.has("total_tokens")) {
                tokensUsed = usage.get("total_tokens").asLong();
            }

            String content = openAiResponse.path("choices").get(0).path("message").path("content").asText();
            JsonNode extracted = objectMapper.readTree(content);

            if (extracted.hasNonNull("amount")) {
                result.setExtractedAmount(new BigDecimal(extracted.get("amount").asText()));
            }
            if (extracted.hasNonNull("date")) {
                try {
                    result.setExtractedDate(LocalDate.parse(extracted.get("date").asText()));
                } catch (Exception ignore) {
                    // date mal formée : on laisse l'utilisateur la saisir manuellement
                }
            }
            if (extracted.hasNonNull("vendor")) {
                result.setExtractedVendor(extracted.get("vendor").asText());
            }
            if (extracted.hasNonNull("chargeType")) {
                result.setSuggestedChargeTypeLabel(extracted.get("chargeType").asText());
            }

            if (result.getExtractedAmount() == null && result.getExtractedVendor() == null) {
                result.setWarning("L'IA n'a pas réussi à lire clairement cette facture. Vérifie et complète les champs manuellement.");
            }
        } catch (Exception parseError) {
            result.setWarning("L'IA a répondu dans un format inattendu. Vérifie et complète les champs manuellement.");
        }

        result.setTokensUsed(tokensUsed);

        // 3. Journalisation de l'usage réel (le coût est engagé dès l'appel OpenAI, que l'utilisateur
        // valide ensuite la charge ou annule). document reste vide ici : il sera lié plus tard si besoin.
        logUsage(enterpriseId, collaboratorId, tokensUsed);

        return result;
    }

    private void checkQuota(Long enterpriseId) {
        if (enterpriseId == null) {
            return;
        }
        List<AiQuota> quotas = aiQuotaService.findByEnterpriseId(enterpriseId);
        if (quotas == null || quotas.isEmpty()) {
            // Pas de quota configuré pour cette société = pas de limite pour le moment.
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
                    "Quota IA épuisé pour cette société (" + used + "/" + allocated + " tokens utilisés). "
                            + "Utilise le formulaire manuel, ou demande une augmentation de quota."
            );
        }
    }

    private String storeFile(MultipartFile file) throws IOException {
        Path baseDir = Path.of(uploadDir);
        Files.createDirectories(baseDir);
        String safeOriginalName = file.getOriginalFilename() == null ? "facture" : file.getOriginalFilename();
        String uniqueName = UUID.randomUUID() + "_" + safeOriginalName.replaceAll("[^a-zA-Z0-9._-]", "_");
        Path target = baseDir.resolve(uniqueName);
        Files.write(target, file.getBytes());
        // Chemin relatif stocké dans Document.file — cohérent avec le reste de l'appli (pas de chemin absolu en base).
        return uploadDir + "/" + uniqueName;
    }

    private JsonNode callOpenAi(String base64Image, String contentType) throws IOException {
        String prompt = "Tu es un assistant qui extrait les informations de factures de charges immobilières "
                + "(eau, électricité, ménage, maintenance...). Réponds UNIQUEMENT avec un objet JSON strict, "
                + "sans aucun texte autour, avec exactement ces clés : "
                + "\"amount\" (nombre, montant total TTC, ou null si illisible), "
                + "\"date\" (format YYYY-MM-DD, ou null si illisible), "
                + "\"vendor\" (nom du fournisseur/prestataire, ou null si illisible), "
                + "\"chargeType\" (une courte catégorie en français parmi: Eau, Électricité, Ménage, Maintenance, "
                + "Internet, Autre — ou null si incertain).";

        String body = objectMapper.createObjectNode()
                .put("model", openAiModel)
                .<com.fasterxml.jackson.databind.node.ObjectNode>set("response_format",
                        objectMapper.createObjectNode().put("type", "json_object"))
                .set("messages", objectMapper.createArrayNode()
                        .add(objectMapper.createObjectNode()
                                .put("role", "system")
                                .put("content", prompt))
                        .add(objectMapper.createObjectNode()
                                .put("role", "user")
                                .set("content", objectMapper.createArrayNode()
                                        .add(objectMapper.createObjectNode()
                                                .put("type", "text")
                                                .put("text", "Voici une facture à analyser."))
                                        .add(objectMapper.createObjectNode()
                                                .put("type", "image_url")
                                                .set("image_url", objectMapper.createObjectNode()
                                                        .put("url", "data:" + contentType + ";base64," + base64Image))))))
                .toString();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Authorization", "Bearer " + openAiApiKey)
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(45))
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 400) {
                throw new IOException("Erreur OpenAI (" + response.statusCode() + ") : " + response.body());
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
            log.setAiUsageType(findInvoiceScanUsageType());
            aiUsageLogService.create(log);
        } catch (Exception e) {
            // La journalisation ne doit jamais faire échouer le scan lui-même pour l'utilisateur.
            System.err.println("Impossible de journaliser l'usage IA : " + e.getMessage());
        }
    }

    // Les codes AiUsageType ne sont pas figés (créés librement via le CRUD), donc on cherche par
    // heuristique un type existant plutôt que de dépendre d'un code fixe qui pourrait ne pas exister.
    private AiUsageType findInvoiceScanUsageType() {
        List<AiUsageType> types = aiUsageTypeService.findAll();
        if (types == null) {
            return null;
        }
        return types.stream()
                .filter(t -> {
                    String probe = ((t.getCode() != null ? t.getCode() : "")
                            + " " + (t.getLabel() != null ? t.getLabel() : "")).toLowerCase(Locale.ROOT);
                    return probe.contains("scan") || probe.contains("facture") || probe.contains("invoice");
                })
                .findFirst()
                .orElse(null);
    }
}
