package ma.zyn.app.ws.facade.collaborator.ai;

import io.swagger.v3.oas.annotations.Operation;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.service.ai.InvoiceScanService;
import ma.zyn.app.service.security.EffectivePermissionService;
import ma.zyn.app.service.security.EnterpriseAccessService;
import ma.zyn.app.ws.dto.ai.InvoiceScanResultDto;
import ma.zyn.app.zynerator.exception.AiQuotaExceededException;
import ma.zyn.app.zynerator.exception.PermissionDeniedException;
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
    @Autowired
    private EnterpriseAccessService enterpriseAccessService;
    @Autowired
    private EffectivePermissionService effectivePermissionService;

    @Operation(summary = "Analyse une facture (image) via l'IA pour pré-remplir une charge")
    @PostMapping(value = "analyze", consumes = "multipart/form-data")
    public ResponseEntity<?> analyze(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "enterpriseId", required = false) Long enterpriseId
    ) {
        try {
            // Chantier 1 : la societe doit faire partie de celles du collaborateur authentifie -
            // jamais fait confiance a la valeur envoyee par le client seule.
            if (!enterpriseAccessService.hasAccessToEnterprise(enterpriseId)) {
                throw new PermissionDeniedException(
                    "Vous n'etes pas rattache a cette societe : impossible d'utiliser le scan IA pour elle.",
                    new String[]{"InvoiceScan"});
            }
            // Chantier 2 : permission de role.
            effectivePermissionService.assertCanManageAiUsage(enterpriseId);

            // collaboratorId n'est plus accepte du client (etait falsifiable) : derive du contexte de securite.
            Collaborator current = enterpriseAccessService.getCurrentCollaborator();
            Long collaboratorId = current != null ? current.getId() : null;

            InvoiceScanResultDto result = service.analyze(file, enterpriseId, collaboratorId);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (AiQuotaExceededException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.TOO_MANY_REQUESTS);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
        } catch (PermissionDeniedException e) {
            throw e;
        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("message", "Le scan a échoué (" + e.getMessage() + "). Remplis le formulaire manuellement."),
                    HttpStatus.BAD_GATEWAY
            );
        }
    }
}
