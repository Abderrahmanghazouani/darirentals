package ma.zyn.app.service.currency;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.service.facade.admin.currency.CurrencyAdminService;
import ma.zyn.app.service.facade.admin.currency.ExchangeRateAdminService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Synchronise automatiquement les taux de change MAD -> {EUR, USD, GBP} depuis ExchangeRate-API
 * (https://www.exchangerate-api.com/), qui couvre le MAD contrairement a Frankfurter (verifie -
 * Frankfurter est base sur les taux de reference BCE, qui ne publient pas le MAD). Voir
 * NOTES-devises.md pour le detail du choix et le format de reponse de l'API.
 *
 * Ne remplace pas la saisie manuelle : c'est une automatisation de la MEME table
 * (ExchangeRate), qui reste modifiable a la main a tout moment sur /admin/exchange-rates.
 */
@Service
public class ExchangeRateSyncService {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateSyncService.class);
    private static final List<String> TARGET_CODES = List.of("EUR", "USD", "GBP");
    private static final String BASE_CODE = "MAD";

    @Value("${exchangerate.api.key:}")
    private String apiKey;

    @Autowired
    private CurrencyAdminService currencyService;
    @Autowired
    private ExchangeRateAdminService exchangeRateService;
    @Autowired
    private ObjectMapper objectMapper;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static class SyncResult {
        public boolean success;
        public String message;
        public Map<String, BigDecimal> updatedRates = new LinkedHashMap<>();
    }

    /** Une fois par jour a 3h du matin. */
    @Scheduled(cron = "0 0 3 * * *")
    public void scheduledSync() {
        SyncResult result = sync();
        if (result.success) {
            log.info("Synchronisation quotidienne des taux de change reussie : {}", result.updatedRates);
        } else {
            log.warn("Echec de la synchronisation quotidienne des taux de change : {}", result.message);
        }
    }

    /** Declenchable aussi manuellement (bouton "Actualiser maintenant" sur /admin/exchange-rates). */
    public SyncResult sync() {
        SyncResult result = new SyncResult();

        if (apiKey == null || apiKey.isBlank()) {
            result.success = false;
            result.message = "Cle API ExchangeRate-API non configuree "
                    + "(variable d'environnement EXCHANGERATE_API_KEY absente). "
                    + "Les taux existants sont conserves tels quels.";
            log.warn(result.message);
            return result;
        }

        Currency base = findCurrency(BASE_CODE);
        if (base == null) {
            result.success = false;
            result.message = "Devise de base " + BASE_CODE + " introuvable en base de donnees.";
            log.warn(result.message);
            return result;
        }

        JsonNode responseBody;
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + BASE_CODE))
                    .timeout(Duration.ofSeconds(15))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                result.success = false;
                result.message = "ExchangeRate-API a repondu avec le statut HTTP " + response.statusCode()
                        + " - taux existants conserves.";
                log.warn("{} Corps de reponse : {}", result.message, response.body());
                return result;
            }
            responseBody = objectMapper.readTree(response.body());
            if (!"success".equals(responseBody.path("result").asText())) {
                result.success = false;
                result.message = "ExchangeRate-API a repondu en erreur ("
                        + responseBody.path("error-type").asText("type inconnu") + ") - taux existants conserves.";
                log.warn(result.message);
                return result;
            }
        } catch (Exception e) {
            result.success = false;
            result.message = "Impossible de contacter ExchangeRate-API (" + e.getMessage()
                    + ") - taux existants conserves.";
            log.warn(result.message, e);
            return result;
        }

        JsonNode conversionRates = responseBody.path("conversion_rates");
        List<ExchangeRate> existingForBase = exchangeRateService.findByBaseCurrencyId(base.getId());

        for (String targetCode : TARGET_CODES) {
            if (!conversionRates.has(targetCode)) {
                log.warn("ExchangeRate-API n'a pas renvoye de taux pour {} -> {}, ignore.", BASE_CODE, targetCode);
                continue;
            }
            Currency target = findCurrency(targetCode);
            if (target == null) {
                log.warn("Devise cible {} introuvable en base, taux ignore.", targetCode);
                continue;
            }

            BigDecimal rateValue;
            try {
                rateValue = new BigDecimal(conversionRates.get(targetCode).asText());
            } catch (NumberFormatException e) {
                log.warn("Taux {} -> {} illisible dans la reponse API, ignore.", BASE_CODE, targetCode);
                continue;
            }

            ExchangeRate existing = existingForBase.stream()
                    .filter(er -> er.getTargetCurrency() != null && target.getId().equals(er.getTargetCurrency().getId()))
                    .findFirst()
                    .orElse(null);

            String source = "ExchangeRate-API (auto, " + LocalDate.now() + ")";
            if (existing != null) {
                existing.setRate(rateValue);
                existing.setSource(source);
                exchangeRateService.update(existing);
            } else {
                ExchangeRate created = new ExchangeRate();
                created.setBaseCurrency(base);
                created.setTargetCurrency(target);
                created.setRate(rateValue);
                created.setSource(source);
                exchangeRateService.create(created);
            }
            result.updatedRates.put(targetCode, rateValue);
        }

        result.success = !result.updatedRates.isEmpty();
        result.message = result.success
                ? "Taux mis a jour : " + result.updatedRates.keySet()
                : "Aucun des taux cibles (" + TARGET_CODES + ") n'a ete trouve dans la reponse de l'API.";
        if (!result.success) {
            log.warn(result.message);
        }
        return result;
    }

    private Currency findCurrency(String code) {
        return currencyService.findAll().stream()
                .filter(c -> code.equals(c.getCode()))
                .findFirst()
                .orElse(null);
    }
}
