package ma.zyn.app.ws.facade.admin.currency;

import io.swagger.v3.oas.annotations.Operation;
import ma.zyn.app.service.currency.ExchangeRateSyncService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Declenchement manuel de la synchronisation des taux de change (bouton "Actualiser
 * maintenant" sur /admin/exchange-rates), en plus du job quotidien planifie. Voir
 * NOTES-devises.md. Protege par /api/admin/** (ROLE_ADMIN) via WebSecurityConfig.
 */
@RestController
@RequestMapping("/api/admin/currency/exchange-rates/")
public class ExchangeRateSyncRestAdmin {

    private final ExchangeRateSyncService syncService;

    public ExchangeRateSyncRestAdmin(ExchangeRateSyncService syncService) {
        this.syncService = syncService;
    }

    @Operation(summary = "Declenche une synchronisation immediate des taux de change depuis ExchangeRate-API")
    @PostMapping("sync")
    public ResponseEntity<ExchangeRateSyncService.SyncResult> sync() {
        ExchangeRateSyncService.SyncResult result = syncService.sync();
        return new ResponseEntity<>(result, result.success ? HttpStatus.OK : HttpStatus.BAD_GATEWAY);
    }
}
