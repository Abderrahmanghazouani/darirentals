package ma.zyn.app.ws.dto.ai;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Résultat renvoyé au frontend après analyse d'une facture par l'IA.
 * Rien n'est encore enregistré en base à ce stade (ni Document, ni Charge) :
 * l'utilisateur doit valider/corriger avant toute sauvegarde définitive.
 */
public class InvoiceScanResultDto {

    /** Chemin relatif du fichier temporairement stocké côté serveur, à renvoyer tel quel lors de la validation. */
    private String documentToken;
    private String fileName;

    private BigDecimal extractedAmount;
    private LocalDate extractedDate;
    private String extractedVendor;

    /** Simple suggestion textuelle (libellé), pas un id de ChargeType : le frontend tente un rapprochement. */
    private String suggestedChargeTypeLabel;

    private Long tokensUsed;

    /** Non-null si l'IA n'a pas pu tout extraire correctement (image floue, format inattendu...). */
    private String warning;

    public String getDocumentToken() {
        return documentToken;
    }

    public void setDocumentToken(String documentToken) {
        this.documentToken = documentToken;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public BigDecimal getExtractedAmount() {
        return extractedAmount;
    }

    public void setExtractedAmount(BigDecimal extractedAmount) {
        this.extractedAmount = extractedAmount;
    }

    public LocalDate getExtractedDate() {
        return extractedDate;
    }

    public void setExtractedDate(LocalDate extractedDate) {
        this.extractedDate = extractedDate;
    }

    public String getExtractedVendor() {
        return extractedVendor;
    }

    public void setExtractedVendor(String extractedVendor) {
        this.extractedVendor = extractedVendor;
    }

    public String getSuggestedChargeTypeLabel() {
        return suggestedChargeTypeLabel;
    }

    public void setSuggestedChargeTypeLabel(String suggestedChargeTypeLabel) {
        this.suggestedChargeTypeLabel = suggestedChargeTypeLabel;
    }

    public Long getTokensUsed() {
        return tokensUsed;
    }

    public void setTokensUsed(Long tokensUsed) {
        this.tokensUsed = tokensUsed;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }
}
