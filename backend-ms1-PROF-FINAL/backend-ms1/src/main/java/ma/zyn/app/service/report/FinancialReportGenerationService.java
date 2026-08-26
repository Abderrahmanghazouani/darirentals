package ma.zyn.app.service.report;

import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.service.facade.admin.auth.CollaboratorAdminService;
import ma.zyn.app.service.facade.admin.charge.ChargeAdminService;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseAdminService;
import ma.zyn.app.service.facade.admin.property.PropertyAdminService;
import ma.zyn.app.service.facade.admin.report.FinancialReportAdminService;
import ma.zyn.app.service.facade.admin.report.FinancialReportScopeAdminService;
import ma.zyn.app.service.facade.admin.report.FinancialReportTypeAdminService;
import ma.zyn.app.service.facade.admin.reservation.ReservationAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Genere un rapport financier FIGE : calcule revenus/charges/benefice a l'instant present a
 * partir des Reservation/Charge existantes, puis sauvegarde ces valeurs dans FinancialReport.
 * Une fois cree, un FinancialReport n'est plus jamais recalcule - voir
 * FinancialReportAdminServiceImpl.update() (bloque) et NOTES-rapports-financiers.md.
 *
 * Chantier volontairement independant des permissions (EnterpriseAccessService,
 * PermissionDeniedException, etc. - chantier separe en cours ailleurs, non touche ici).
 * Ecran admin uniquement pour l'instant : voir la limite documentee sur generatedBy.
 */
@Service
public class FinancialReportGenerationService {

    private static final String TYPE_MONTHLY_CODE = "Mensuel";
    private static final String SCOPE_PROPERTY_CODE = "Proprietes";

    @Autowired
    private FinancialReportAdminService financialReportService;
    @Autowired
    private FinancialReportTypeAdminService financialReportTypeService;
    @Autowired
    private FinancialReportScopeAdminService financialReportScopeService;
    @Autowired
    private EnterpriseAdminService enterpriseService;
    @Autowired
    private PropertyAdminService propertyService;
    @Autowired
    private ReservationAdminService reservationService;
    @Autowired
    private ChargeAdminService chargeService;
    @Autowired
    private CollaboratorAdminService collaboratorService;

    /** Requete de generation - simple POJO, pas une entite generee. */
    public static class GenerateRequest {
        public Long enterpriseId;
        public String financialReportTypeCode; // "Mensuel" | "Annuel"
        public String financialReportScopeCode; // "Entreprise" | "Proprietes"
        public Long propertyId; // requis si scope = Proprietes
        public Integer year;
        public Integer month; // 1-12, requis si type = Mensuel
        /** Optionnel : id du Collaborator auteur. Une session admin (ROLE_ADMIN, pas un
         * Collaborator) n'en a pas - voir NOTES-rapports-financiers.md. */
        public Long generatedByCollaboratorId;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public FinancialReport generate(GenerateRequest request) {
        if (request == null || request.enterpriseId == null) {
            throw new IllegalArgumentException("Societe requise.");
        }
        Enterprise enterprise = enterpriseService.findById(request.enterpriseId);
        if (enterprise == null) {
            throw new IllegalArgumentException("Societe introuvable.");
        }

        FinancialReportType type = findTypeByCode(request.financialReportTypeCode);
        if (type == null) {
            throw new IllegalArgumentException("Type de rapport invalide (attendu : Mensuel ou Annuel).");
        }
        FinancialReportScope scope = findScopeByCode(request.financialReportScopeCode);
        if (scope == null) {
            throw new IllegalArgumentException("Portee de rapport invalide (attendu : Entreprise ou Proprietes).");
        }

        if (request.year == null) {
            throw new IllegalArgumentException("Annee requise.");
        }
        boolean monthly = TYPE_MONTHLY_CODE.equals(type.getCode());
        LocalDate periodStart;
        LocalDate periodEnd;
        if (monthly) {
            if (request.month == null || request.month < 1 || request.month > 12) {
                throw new IllegalArgumentException("Mois invalide (1-12 requis pour un rapport Mensuel).");
            }
            periodStart = LocalDate.of(request.year, request.month, 1);
            periodEnd = periodStart.withDayOfMonth(periodStart.lengthOfMonth());
        } else {
            periodStart = LocalDate.of(request.year, 1, 1);
            periodEnd = LocalDate.of(request.year, 12, 31);
        }

        boolean scopedToProperty = SCOPE_PROPERTY_CODE.equals(scope.getCode());
        Property targetProperty = null;
        List<Property> targetProperties;
        if (scopedToProperty) {
            if (request.propertyId == null) {
                throw new IllegalArgumentException("Propriete requise pour la portee Proprietes.");
            }
            targetProperty = propertyService.findById(request.propertyId);
            if (targetProperty == null || targetProperty.getEnterprise() == null
                    || !enterprise.getId().equals(targetProperty.getEnterprise().getId())) {
                throw new IllegalArgumentException("Propriete introuvable pour cette societe.");
            }
            targetProperties = List.of(targetProperty);
        } else {
            targetProperties = propertyService.findByEnterpriseId(enterprise.getId());
        }

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalCharges = BigDecimal.ZERO;
        for (Property property : targetProperties) {
            for (Reservation reservation : reservationService.findByPropertyId(property.getId())) {
                LocalDate checkIn = reservation.getCheckInDate();
                if (checkIn != null && !checkIn.isBefore(periodStart) && !checkIn.isAfter(periodEnd)) {
                    totalRevenue = totalRevenue.add(orZero(reservation.getAmount()));
                }
            }
            for (Charge charge : chargeService.findByPropertyId(property.getId())) {
                LocalDate chargeDate = charge.getChargeDate();
                if (chargeDate != null && !chargeDate.isBefore(periodStart) && !chargeDate.isAfter(periodEnd)) {
                    totalCharges = totalCharges.add(orZero(charge.getAmount()));
                }
            }
        }
        BigDecimal netProfit = totalRevenue.subtract(totalCharges);

        FinancialReport report = new FinancialReport();
        report.setEnterprise(enterprise);
        report.setFinancialReportType(type);
        report.setFinancialReportScope(scope);
        report.setPeriodStart(periodStart);
        report.setPeriodEnd(periodEnd);
        report.setTotalRevenue(totalRevenue);
        report.setTotalCharges(totalCharges);
        report.setNetProfit(netProfit);
        report.setGeneratedAt(LocalDateTime.now());
        if (request.generatedByCollaboratorId != null) {
            Collaborator collaborator = collaboratorService.findById(request.generatedByCollaboratorId);
            report.setGeneratedBy(collaborator);
        }
        if (scopedToProperty) {
            FinancialReportProperty link = new FinancialReportProperty();
            link.setProperty(targetProperty);
            report.setFinancialReportProperties(List.of(link));
        }

        return financialReportService.create(report);
    }

    private static BigDecimal orZero(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private FinancialReportType findTypeByCode(String code) {
        if (code == null) return null;
        return financialReportTypeService.findAll().stream()
                .filter(t -> code.equals(t.getCode()))
                .findFirst()
                .orElse(null);
    }

    private FinancialReportScope findScopeByCode(String code) {
        if (code == null) return null;
        return financialReportScopeService.findAll().stream()
                .filter(s -> code.equals(s.getCode()))
                .findFirst()
                .orElse(null);
    }
}
