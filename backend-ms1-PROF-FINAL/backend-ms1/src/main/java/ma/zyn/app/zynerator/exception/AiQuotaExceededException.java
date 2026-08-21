package ma.zyn.app.zynerator.exception;

/**
 * Levée quand une société a épuisé son quota de tokens IA (AiQuota) alloué.
 */
public class AiQuotaExceededException extends RuntimeException {

    public AiQuotaExceededException(String message) {
        super(message);
    }
}
