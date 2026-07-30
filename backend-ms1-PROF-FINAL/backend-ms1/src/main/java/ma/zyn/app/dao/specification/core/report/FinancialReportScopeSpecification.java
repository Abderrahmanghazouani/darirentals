package  ma.zyn.app.dao.specification.core.report;

import ma.zyn.app.dao.criteria.core.report.FinancialReportScopeCriteria;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class FinancialReportScopeSpecification extends  AbstractSpecification<FinancialReportScopeCriteria, FinancialReportScope>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public FinancialReportScopeSpecification(FinancialReportScopeCriteria criteria) {
        super(criteria);
    }

    public FinancialReportScopeSpecification(FinancialReportScopeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
