package  ma.zyn.app.dao.specification.core.report;

import ma.zyn.app.dao.criteria.core.report.FinancialReportPropertyCriteria;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class FinancialReportPropertySpecification extends  AbstractSpecification<FinancialReportPropertyCriteria, FinancialReportProperty>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateFk("financialReport","id", criteria.getFinancialReport()==null?null:criteria.getFinancialReport().getId());
        addPredicateFk("financialReport","id", criteria.getFinancialReports());
        addPredicateFk("property","id", criteria.getProperty()==null?null:criteria.getProperty().getId());
        addPredicateFk("property","id", criteria.getPropertys());
    }

    public FinancialReportPropertySpecification(FinancialReportPropertyCriteria criteria) {
        super(criteria);
    }

    public FinancialReportPropertySpecification(FinancialReportPropertyCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
