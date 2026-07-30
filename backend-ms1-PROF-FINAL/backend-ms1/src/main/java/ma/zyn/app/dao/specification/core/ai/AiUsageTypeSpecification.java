package  ma.zyn.app.dao.specification.core.ai;

import ma.zyn.app.dao.criteria.core.ai.AiUsageTypeCriteria;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class AiUsageTypeSpecification extends  AbstractSpecification<AiUsageTypeCriteria, AiUsageType>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public AiUsageTypeSpecification(AiUsageTypeCriteria criteria) {
        super(criteria);
    }

    public AiUsageTypeSpecification(AiUsageTypeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
