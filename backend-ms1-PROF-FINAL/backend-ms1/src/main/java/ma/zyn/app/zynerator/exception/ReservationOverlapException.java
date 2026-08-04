package ma.zyn.app.zynerator.exception;

/**
 * Levée quand une réservation (création ou modification) chevauche une
 * réservation existante sur la même propriété pour la même période.
 */
public class ReservationOverlapException extends RuntimeException {

    public ReservationOverlapException(String message) {
        super(message);
    }
}
