package ma.zyn.app.ws.facade.collaborator.ai;

import io.swagger.v3.oas.annotations.Operation;
import ma.zyn.app.service.ai.InvoiceScanService;
import ma.zyn.app.ws.dto.ai.InvoiceScanResultDto;
import ma.zyn.app.zynerator.exception.AiQuotaExceededException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/collaborator/invoice-scan/")
public class InvoiceScanRestCollaborator {

    @Autowired
    private InvoiceScanService service;

    @Operation(summary = "Analyse une facture (image) via l'IA pour pré-remplir une charge")
    @PostMapping(value = "analyze", consumes = "multipart/form-data")
    public ResponseEntity<?> analyze(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "enterpriseId", required = false) Long enterpriseId,
            @RequestParam(value = "collaboratorId", required = false) Long collaboratorId
    ) {
        try {
            InvoiceScanResultDto result = service.analyze(file, enterpriseId, collaboratorId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AiQuotaExceededException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("message", "Le scan a échoué (" + e.getMessage() + "). Remplis le formulaire manuellement."),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }
}
