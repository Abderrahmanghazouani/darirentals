package  ma.zyn.app.dao.specification.core.provider;

import ma.zyn.app.dao.criteria.core.provider.ServiceProviderCriteria;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ServiceProviderSpecification extends  AbstractSpecification<ServiceProviderCriteria, ServiceProvider>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("name", criteria.getName(),criteria.getNameLike());
        addPredicate("phone", criteria.getPhone(),criteria.getPhoneLike());
        addPredicateBool("isActive", criteria.getIsActive());
        addPredicateFk("serviceType","id", criteria.getServiceType()==null?null:criteria.getServiceType().getId());
        addPredicateFk("serviceType","id", criteria.getServiceTypes());
        addPredicateFk("serviceType","code", criteria.getServiceType()==null?null:criteria.getServiceType().getCode());
        addPredicateFk("enterprise","id", criteria.getEnterprise()==null?null:criteria.getEnterprise().getId());
        addPredicateFk("enterprise","id", criteria.getEnterprises());
    }

    public ServiceProviderSpecification(ServiceProviderCriteria criteria) {
        super(criteria);
    }

    public ServiceProviderSpecification(ServiceProviderCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
