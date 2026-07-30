package  ma.zyn.app.dao.specification.core.provider;

import ma.zyn.app.dao.criteria.core.provider.ServiceTypeCriteria;
import ma.zyn.app.bean.core.provider.ServiceType;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ServiceTypeSpecification extends  AbstractSpecification<ServiceTypeCriteria, ServiceType>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public ServiceTypeSpecification(ServiceTypeCriteria criteria) {
        super(criteria);
    }

    public ServiceTypeSpecification(ServiceTypeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
