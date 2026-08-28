package ma.zyn.app.ws.dto.ai;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Requete pour les insights du matin (AI Property Assistant) : le frontend a deja calcule
 * TOUTES les donnees reelles (revenue-intelligence.ts, health-score.ts, action-center.ts,
 * property-performance.ts) - "facts" est ce paquet de faits deja verifies, jamais une requete
 * libre vers la base. Gemini ne fait que reformuler "facts" en langage naturel.
 */
public class AssistantInsightsRequestDto {

    private JsonNode facts;
    private Long enterpriseId;

    public JsonNode getFacts() {
        return facts;
    }

    public void setFacts(JsonNode facts) {
        this.facts = facts;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
