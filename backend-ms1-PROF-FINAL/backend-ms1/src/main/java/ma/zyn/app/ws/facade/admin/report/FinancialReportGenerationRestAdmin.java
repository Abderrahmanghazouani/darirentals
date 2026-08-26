package ma.zyn.app.ws.facade.admin.report;

import io.swagger.v3.oas.annotations.Operation;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.service.facade.admin.report.FinancialReportAdminService;
import ma.zyn.app.service.report.FinancialReportExportService;
import ma.zyn.app.service.report.FinancialReportGenerationService;
import ma.zyn.app.ws.converter.report.FinancialReportConverter;
import ma.zyn.app.ws.dto.report.FinancialReportDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * Generation et export des rapports financiers. Distinct du CRUD generique
 * /api/admin/financialReport/ (qui reste utilisable pour la consultation en liste - voir
 * findAll deja correct - mais dont l'ecriture est desormais bloquee, voir
 * FinancialReportAdminServiceImpl.update()). Voir NOTES-rapports-financiers.md.
 *
 * Chantier volontairement independant des permissions - endpoints proteges uniquement par
 * /api/admin/** (ROLE_ADMIN) via la config de securite existante, comme tout le reste de
 * l'admin.
 */
@RestController
@RequestMapping("/api/admin/financial-reports/")
public class FinancialReportGenerationRestAdmin {

    private final FinancialReportGenerationService generationService;
    private final FinancialReportExportService exportService;
    private final FinancialReportAdminService financialReportService;
    private final FinancialReportConverter converter;

    public FinancialReportGenerationRestAdmin(FinancialReportGenerationService generationService,
                                               FinancialReportExportService exportService,
                                               FinancialReportAdminService financialReportService,
                                               FinancialReportConverter converter) {
        this.generationService = generationService;
        this.exportService = exportService;
        this.financialReportService = financialReportService;
        this.converter = converter;
    }

    @Operation(summary = "Genere un nouveau rapport financier fige (calcul instantane, sauvegarde definitive)")
    @PostMapping("generate")
    public ResponseEntity<?> generate(@RequestBody FinancialReportGenerationService.GenerateRequest request) {
        try {
            FinancialReport created = generationService.generate(request);
            // Conversion volontairement peu profonde : initObject(true) sur ce converter
            // entraine l'expansion de "enterprise", qui reexpanse ses "properties", qui
            // reexpansent "financialReportProperties", qui repointent vers "financialReport"
            // -> cycle infini (StackOverflowError) via les converters generes. On expose donc
            // uniquement type/portee (lookups simples, sans cycle) ; le frontend connait deja
            // l'enterprise/la propriete puisqu'il vient de les soumettre.
            converter.initList(false);
            converter.initObject(false);
            converter.setFinancialReportType(true);
            converter.setFinancialReportScope(true);
            FinancialReportDto dto = converter.toDto(created);
            return new ResponseEntity<>(dto, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Historique des rapports, avec la propriete ciblee (portee Proprietes)")
    @GetMapping("history")
    public ResponseEntity<List<FinancialReportDto>> history() {
        List<FinancialReport> list = financialReportService.findAll();
        // Contrairement au CRUD generique (/api/admin/financialReport/, initList(false)),
        // on a ici besoin de financialReportProperties pour afficher la propriete ciblee sur
        // un rapport a portee "Proprietes". Sans risque de cycle : voir les garde-fous deja en
        // place dans FinancialReportPropertyConverter/PropertyConverter/ClientConverter.
        converter.initObject(true);
        converter.setFinancialReportProperties(true);
        List<FinancialReportDto> dtos = converter.toDto(list);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Operation(summary = "Exporte un rapport financier en PDF")
    @GetMapping("{id}/pdf")
    @Transactional(readOnly = true)
    public ResponseEntity<?> exportPdf(@PathVariable Long id) {
        FinancialReport report = financialReportService.findById(id);
        if (report == null) {
            return new ResponseEntity<>(Map.of("message", "Rapport introuvable"), HttpStatus.NOT_FOUND);
        }
        byte[] pdf = exportService.generatePdf(report);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "rapport-financier-" + id + ".pdf");
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }

    @Operation(summary = "Exporte un rapport financier en CSV")
    @GetMapping("{id}/csv")
    @Transactional(readOnly = true)
    public ResponseEntity<?> exportCsv(@PathVariable Long id) {
        FinancialReport report = financialReportService.findById(id);
        if (report == null) {
            return new ResponseEntity<>(Map.of("message", "Rapport introuvable"), HttpStatus.NOT_FOUND);
        }
        String csv = exportService.generateCsv(report);
        byte[] body = csv.getBytes(StandardCharsets.UTF_8);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "rapport-financier-" + id + ".csv");
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }
}
