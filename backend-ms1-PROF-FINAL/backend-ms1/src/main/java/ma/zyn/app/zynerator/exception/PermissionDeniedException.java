package ma.zyn.app.zynerator.exception;

/**
 * Levee quand un collaborateur authentifie tente une action hors de son perimetre
 * (societe non rattachee, permission de role insuffisante). Mappee sur HTTP 403
 * dans GlobalException - voir NOTES-permissions.md.
 */
public class PermissionDeniedException extends RuntimeException {

    private String[] params;

    public PermissionDeniedException(String message) {
        super(message);
    }

    public PermissionDeniedException(String message, String[] params) {
        super(message);
        this.params = params;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

}
