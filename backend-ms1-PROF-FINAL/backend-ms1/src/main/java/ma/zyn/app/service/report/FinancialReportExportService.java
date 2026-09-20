package ma.zyn.app.service.report;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.dao.facade.core.currency.ExchangeRateDao;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Mise en page du PDF/CSV d'un FinancialReport deja fige (voir FinancialReportGenerationService).
 * Aucune donnee n'est recalculee ici : uniquement une mise en forme des valeurs deja
 * sauvegardees. Utilise iText 5 (com.itextpdf:itextpdf), deja une dependance du projet
 * (voir zynerator/export/PdfConfig.java pour un autre usage existant) - pas de nouvelle
 * dependance ajoutee.
 *
 * Pas de logo image : Enterprise n'a pas de champ logo/branding dans ce projet (pas de mecanisme
 * d'upload de logo existant). Le "logo" demande est rendu en masthead texte ("DariRentals").
 */
@Service
public class FinancialReportExportService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    /** Devise de stockage et de reference comptable : tous les montants en base sont en MAD. */
    public static final String BASE_CURRENCY = "MAD";

    private final ExchangeRateDao exchangeRateDao;

    public FinancialReportExportService(ExchangeRateDao exchangeRateDao) {
        this.exchangeRateDao = exchangeRateDao;
    }

    /**
     * Devise d'affichage d'un export : code + taux MAD -> code (1 MAD = rate code). Meme regle que
     * le frontend (lib/currency/conversion.ts) : sans taux connu pour la devise demandee, on reste
     * en MAD plutot que d'afficher un montant faux.
     */
    public static final class ExportCurrency {
        final String code;
        final BigDecimal rate; // null en MAD (pas de conversion)
        ExportCurrency(String code, BigDecimal rate) { this.code = code; this.rate = rate; }
        public String getCode() { return code; }
        public boolean isConverted() { return rate != null; }
    }

    public ExportCurrency resolveCurrency(String requestedCode) {
        if (requestedCode == null || requestedCode.isBlank() || BASE_CURRENCY.equalsIgnoreCase(requestedCode.trim())) {
            return new ExportCurrency(BASE_CURRENCY, null);
        }
        String code = requestedCode.trim().toUpperCase();
        for (ExchangeRate er : exchangeRateDao.findByTargetCurrencyCode(code)) {
            if (er.getBaseCurrency() != null && BASE_CURRENCY.equals(er.getBaseCurrency().getCode())
                    && er.getRate() != null && er.getRate().signum() > 0) {
                return new ExportCurrency(code, er.getRate());
            }
        }
        return new ExportCurrency(BASE_CURRENCY, null);
    }

    public byte[] generatePdf(FinancialReport report) {
        return generatePdf(report, resolveCurrency(null));
    }

    public byte[] generatePdf(FinancialReport report, ExportCurrency currency) {
        Document document = new Document(PageSize.A4, 40, 40, 60, 60);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL, BaseColor.DARK_GRAY);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
            Font rowLabelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);
            Font rowValueFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
            Font profitFont = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD);
            Font footerFont = new Font(Font.FontFamily.HELVETICA, 8, Font.ITALIC, BaseColor.GRAY);

            Paragraph masthead = new Paragraph("DariRentals", titleFont);
            masthead.setAlignment(Element.ALIGN_CENTER);
            document.add(masthead);

            Paragraph subtitle = new Paragraph("Rapport financier", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingAfter(24);
            document.add(subtitle);

            document.add(infoLine("Societe", enterpriseName(report), labelFont, valueFont));
            document.add(infoLine("Type de rapport", codeLabel(report.getFinancialReportType() != null ? report.getFinancialReportType().getLabel() : null), labelFont, valueFont));
            String scopeDetail = scopeLabel(report);
            String propertyName = propertyName(report);
            if (propertyName != null) {
                scopeDetail = scopeDetail + " - " + propertyName;
            }
            document.add(infoLine("Portee", scopeDetail, labelFont, valueFont));
            document.add(infoLine("Periode couverte", periodLabel(report), labelFont, valueFont));
            document.add(infoLine("Devise", currencyLabel(currency), labelFont, valueFont));

            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);
            table.setSpacingBefore(24);
            table.setWidths(new float[]{2f, 1f});
            addAmountRow(table, "Revenus", report.getTotalRevenue(), currency, rowLabelFont, rowValueFont);
            addAmountRow(table, "Charges", report.getTotalCharges(), currency, rowLabelFont, rowValueFont);
            addAmountRow(table, "Benefice net", report.getNetProfit(), currency, rowLabelFont, profitFont);
            document.add(table);

            Paragraph footer = new Paragraph(
                    "Rapport genere le " + generatedAtLabel(report) + " par " + authorLabel(report)
                            + " - Les montants sont figes a la date de generation et ne refletent pas"
                            + " les operations enregistrees depuis."
                            + (currency.isConverted()
                                ? " Conversion d'affichage depuis le MAD (devise de reference) au taux actuel du "
                                    + java.time.LocalDate.now().format(DATE_FORMAT) + " ; les valeurs figees restent en MAD."
                                : ""),
                    footerFont
            );
            footer.setSpacingBefore(36);
            document.add(footer);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Erreur lors de la generation du PDF du rapport financier : " + e.getMessage(), e);
        }
        return out.toByteArray();
    }

    public String generateCsv(FinancialReport report) {
        return generateCsv(report, resolveCurrency(null));
    }

    public String generateCsv(FinancialReport report, ExportCurrency currency) {
        StringBuilder sb = new StringBuilder();
        sb.append('\uFEFF'); // BOM UTF-8, pour un affichage correct des accents dans Excel.
        sb.append("Champ;Valeur\n");
        appendCsvRow(sb, "Societe", enterpriseName(report));
        appendCsvRow(sb, "Type de rapport", report.getFinancialReportType() != null ? report.getFinancialReportType().getLabel() : "");
        appendCsvRow(sb, "Portee", scopeLabel(report));
        String propertyName = propertyName(report);
        if (propertyName != null) {
            appendCsvRow(sb, "Propriete", propertyName);
        }
        appendCsvRow(sb, "Periode couverte", periodLabel(report));
        appendCsvRow(sb, "Devise", currencyLabel(currency));
        appendCsvRow(sb, "Revenus (" + currency.code + ")", amountValue(report.getTotalRevenue(), currency));
        appendCsvRow(sb, "Charges (" + currency.code + ")", amountValue(report.getTotalCharges(), currency));
        appendCsvRow(sb, "Benefice net (" + currency.code + ")", amountValue(report.getNetProfit(), currency));
        appendCsvRow(sb, "Genere le", generatedAtLabel(report));
        appendCsvRow(sb, "Genere par", authorLabel(report));
        return sb.toString();
    }

    private Paragraph infoLine(String label, String value, Font labelFont, Font valueFont) {
        Paragraph p = new Paragraph();
        p.add(new com.itextpdf.text.Chunk(label + " : ", labelFont));
        p.add(new com.itextpdf.text.Chunk(value != null ? value : "-", valueFont));
        p.setSpacingAfter(4);
        return p;
    }

    private void addAmountRow(PdfPTable table, String label, BigDecimal amount, ExportCurrency currency, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBorder(Rectangle.BOTTOM);
        labelCell.setBorderColor(BaseColor.LIGHT_GRAY);
        labelCell.setPaddingTop(8);
        labelCell.setPaddingBottom(8);

        PdfPCell valueCell = new PdfPCell(new Phrase(amountValue(amount, currency) + " " + currency.code, valueFont));
        valueCell.setBorder(Rectangle.BOTTOM);
        valueCell.setBorderColor(BaseColor.LIGHT_GRAY);
        valueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        valueCell.setPaddingTop(8);
        valueCell.setPaddingBottom(8);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private void appendCsvRow(StringBuilder sb, String label, String value) {
        sb.append(csvEscape(label)).append(';').append(csvEscape(value)).append('\n');
    }

    private String csvEscape(String value) {
        if (value == null) return "";
        if (value.contains(";") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    private String amountValue(BigDecimal amount, ExportCurrency currency) {
        BigDecimal value = amount != null ? amount : BigDecimal.ZERO;
        if (currency.rate != null) {
            value = value.multiply(currency.rate);
        }
        return value.setScale(2, java.math.RoundingMode.HALF_UP).toString();
    }

    private String currencyLabel(ExportCurrency currency) {
        if (!currency.isConverted()) {
            return BASE_CURRENCY + " (devise de reference)";
        }
        return currency.code + " (1 " + BASE_CURRENCY + " = " + currency.rate.stripTrailingZeros().toPlainString()
                + " " + currency.code + ")";
    }

    private String enterpriseName(FinancialReport report) {
        return report.getEnterprise() != null && report.getEnterprise().getName() != null
                ? report.getEnterprise().getName() : "-";
    }

    private String scopeLabel(FinancialReport report) {
        return report.getFinancialReportScope() != null && report.getFinancialReportScope().getLabel() != null
                ? report.getFinancialReportScope().getLabel() : "-";
    }

    private String codeLabel(String label) {
        return label != null ? label : "-";
    }

    private String propertyName(FinancialReport report) {
        List<FinancialReportProperty> links = report.getFinancialReportProperties();
        if (links == null || links.isEmpty()) {
            return null;
        }
        FinancialReportProperty first = links.get(0);
        if (first.getProperty() == null) {
            return null;
        }
        return first.getProperty().getName();
    }

    private String periodLabel(FinancialReport report) {
        if (report.getPeriodStart() == null || report.getPeriodEnd() == null) {
            return "-";
        }
        return report.getPeriodStart().format(DATE_FORMAT) + " au " + report.getPeriodEnd().format(DATE_FORMAT);
    }

    private String generatedAtLabel(FinancialReport report) {
        return report.getGeneratedAt() != null ? report.getGeneratedAt().format(DATETIME_FORMAT) : "-";
    }

    private String authorLabel(FinancialReport report) {
        return report.getGeneratedBy() != null && report.getGeneratedBy().getName() != null
                ? report.getGeneratedBy().getName() : "Administrateur";
    }
}
