package ma.zyn.app.ws.dto.ai;

public class AssistantResponseDto {

    private String message;
    private long tokensUsed;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(long tokensUsed) {
        this.tokensUsed = tokensUsed;
    }
}
