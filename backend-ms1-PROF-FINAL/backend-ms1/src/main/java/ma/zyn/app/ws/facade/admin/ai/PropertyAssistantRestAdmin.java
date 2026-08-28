package ma.zyn.app.ws.facade.admin.ai;

import io.swagger.v3.oas.annotations.Operation;
import ma.zyn.app.service.ai.PropertyAssistantService;
import ma.zyn.app.ws.dto.ai.AssistantChatRequestDto;
import ma.zyn.app.ws.dto.ai.AssistantInsightsRequestDto;
import ma.zyn.app.ws.dto.ai.AssistantResponseDto;
import ma.zyn.app.zynerator.exception.AiQuotaExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * AI Property Assistant (insights du matin + chat portefeuille) - voir NOTES-ai-assistant.md.
 * Aucune verification de permission specifique ici : coherent avec le reste des controleurs
 * admin (l'admin n'est jamais soumis a EnterpriseAccessService/EffectivePermissionService,
 * reserves aux collaborateurs - Chantiers 1/2/3).
 */
@RestController
@RequestMapping("/api/admin/property-assistant/")
public class PropertyAssistantRestAdmin {

    @Autowired
    private PropertyAssistantService service;

    @Operation(summary = "Genere les insights du matin a partir d'un paquet de donnees deja calculees")
    @PostMapping("insights")
    public ResponseEntity<?> insights(@RequestBody AssistantInsightsRequestDto dto) {
        try {
            AssistantResponseDto result = service.generateMorningInsights(dto.getFacts(), dto.getEnterpriseId(), null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AiQuotaExceededException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("message", "L'assistant n'a pas pu répondre (" + e.getMessage() + ")."),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }

    @Operation(summary = "Repond a une question sur le portefeuille a partir d'un paquet de donnees deja calculees")
    @PostMapping("chat")
    public ResponseEntity<?> chat(@RequestBody AssistantChatRequestDto dto) {
        try {
            AssistantResponseDto result = service.answerQuestion(dto.getFacts(), dto.getQuestion(), dto.getEnterpriseId(), null);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AiQuotaExceededException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("message", "L'assistant n'a pas pu répondre (" + e.getMessage() + ")."),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }
}
