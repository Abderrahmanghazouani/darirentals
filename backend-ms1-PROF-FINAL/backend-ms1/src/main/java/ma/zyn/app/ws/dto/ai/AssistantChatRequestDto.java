package ma.zyn.app.ws.dto.ai;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * Requete pour le chat "Pose une question a ton portefeuille" (AI Property Assistant) - meme
 * principe que AssistantInsightsRequestDto : "facts" est un paquet de donnees deja calculees
 * et verifiees par le frontend, jamais une requete libre vers la base. Gemini repond a
 * "question" strictement a partir de "facts", et doit dire qu'il ne sait pas si l'info n'y
 * figure pas plutot que d'inventer.
 */
public class AssistantChatRequestDto {

    private JsonNode facts;
    private String question;
    private Long enterpriseId;

    public JsonNode getFacts() {
        return facts;
    }

    public void setFacts(JsonNode facts) {
        this.facts = facts;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Long getEnterpriseId() {
        return enterpriseId;
    }

    public void setEnterpriseId(Long enterpriseId) {
        this.enterpriseId = enterpriseId;
    }
}
