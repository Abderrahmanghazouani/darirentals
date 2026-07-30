package  ma.zyn.app.dao.specification.core.property;

import ma.zyn.app.dao.criteria.core.property.PropertyTypeCriteria;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class PropertyTypeSpecification extends  AbstractSpecification<PropertyTypeCriteria, PropertyType>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public PropertyTypeSpecification(PropertyTypeCriteria criteria) {
        super(criteria);
    }

    public PropertyTypeSpecification(PropertyTypeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
