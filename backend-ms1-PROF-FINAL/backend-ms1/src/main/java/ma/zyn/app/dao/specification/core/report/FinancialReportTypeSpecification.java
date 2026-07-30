package  ma.zyn.app.dao.specification.core.report;

import ma.zyn.app.dao.criteria.core.report.FinancialReportTypeCriteria;
import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class FinancialReportTypeSpecification extends  AbstractSpecification<FinancialReportTypeCriteria, FinancialReportType>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public FinancialReportTypeSpecification(FinancialReportTypeCriteria criteria) {
        super(criteria);
    }

    public FinancialReportTypeSpecification(FinancialReportTypeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
