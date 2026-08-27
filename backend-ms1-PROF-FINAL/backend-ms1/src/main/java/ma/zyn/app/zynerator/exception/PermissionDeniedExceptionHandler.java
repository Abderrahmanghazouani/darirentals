package ma.zyn.app.zynerator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;

/**
 * Convertit PermissionDeniedException en reponse HTTP 403 claire.
 *
 * Necessaire car aucun controleur REST du projet (ws.facade.*) n'etend
 * BaseController/AbstractController : leurs @ExceptionHandler locaux ne
 * s'appliquent donc jamais, et toute RuntimeException non geree explicitement
 * dans le controleur remonte en 500. Voir NOTES-permissions.md (Chantier 1 et 2).
 */
@RestControllerAdvice
public class PermissionDeniedExceptionHandler {

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<ErrorResponsePayload> handle(PermissionDeniedException ex) {
        ErrorResponsePayload body = new ErrorResponsePayload(
            new Timestamp(System.currentTimeMillis()),
            HttpStatus.FORBIDDEN.value(),
            HttpStatus.FORBIDDEN.getReasonPhrase(),
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    public static class ErrorResponsePayload {
        public Timestamp timestamp;
        public int status;
        public String error;
        public String message;

        public ErrorResponsePayload(Timestamp timestamp, int status, String error, String message) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
        }
    }
}
