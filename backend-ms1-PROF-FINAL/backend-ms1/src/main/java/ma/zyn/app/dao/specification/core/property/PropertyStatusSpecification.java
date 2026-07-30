package  ma.zyn.app.dao.specification.core.property;

import ma.zyn.app.dao.criteria.core.property.PropertyStatusCriteria;
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class PropertyStatusSpecification extends  AbstractSpecification<PropertyStatusCriteria, PropertyStatus>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public PropertyStatusSpecification(PropertyStatusCriteria criteria) {
        super(criteria);
    }

    public PropertyStatusSpecification(PropertyStatusCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
