package  ma.zyn.app.dao.specification.core.charge;

import ma.zyn.app.dao.criteria.core.charge.ChargeTypeCriteria;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ChargeTypeSpecification extends  AbstractSpecification<ChargeTypeCriteria, ChargeType>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public ChargeTypeSpecification(ChargeTypeCriteria criteria) {
        super(criteria);
    }

    public ChargeTypeSpecification(ChargeTypeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
