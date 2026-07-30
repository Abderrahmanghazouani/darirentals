package  ma.zyn.app.dao.specification.core.report;

import ma.zyn.app.dao.criteria.core.report.FinancialReportCriteria;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class FinancialReportSpecification extends  AbstractSpecification<FinancialReportCriteria, FinancialReport>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateBigDecimal("totalRevenue", criteria.getTotalRevenue(), criteria.getTotalRevenueMin(), criteria.getTotalRevenueMax());
        addPredicateBigDecimal("totalCharges", criteria.getTotalCharges(), criteria.getTotalChargesMin(), criteria.getTotalChargesMax());
        addPredicateBigDecimal("netProfit", criteria.getNetProfit(), criteria.getNetProfitMin(), criteria.getNetProfitMax());
        addPredicate("generatedAt", criteria.getGeneratedAt(), criteria.getGeneratedAtFrom(), criteria.getGeneratedAtTo());
        addPredicate("file", criteria.getFile(),criteria.getFileLike());
        addPredicateFk("financialReportType","id", criteria.getFinancialReportType()==null?null:criteria.getFinancialReportType().getId());
        addPredicateFk("financialReportType","id", criteria.getFinancialReportTypes());
        addPredicateFk("financialReportType","code", criteria.getFinancialReportType()==null?null:criteria.getFinancialReportType().getCode());
        addPredicateFk("financialReportScope","id", criteria.getFinancialReportScope()==null?null:criteria.getFinancialReportScope().getId());
        addPredicateFk("financialReportScope","id", criteria.getFinancialReportScopes());
        addPredicateFk("financialReportScope","code", criteria.getFinancialReportScope()==null?null:criteria.getFinancialReportScope().getCode());
        addPredicateFk("enterprise","id", criteria.getEnterprise()==null?null:criteria.getEnterprise().getId());
        addPredicateFk("enterprise","id", criteria.getEnterprises());
        addPredicateFk("generatedBy","id", criteria.getGeneratedBy()==null?null:criteria.getGeneratedBy().getId());
        addPredicateFk("generatedBy","id", criteria.getGeneratedBys());
        addPredicateFk("generatedBy","email", criteria.getGeneratedBy()==null?null:criteria.getGeneratedBy().getEmail());
    }

    public FinancialReportSpecification(FinancialReportCriteria criteria) {
        super(criteria);
    }

    public FinancialReportSpecification(FinancialReportCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
