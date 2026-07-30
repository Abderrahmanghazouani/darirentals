package  ma.zyn.app.dao.specification.core.enterprise;

import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class EnterpriseSpecification extends  AbstractSpecification<EnterpriseCriteria, Enterprise>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("name", criteria.getName(),criteria.getNameLike());
        addPredicate("phone", criteria.getPhone(),criteria.getPhoneLike());
        addPredicate("address", criteria.getAddress(),criteria.getAddressLike());
        addPredicateFk("currency","id", criteria.getCurrency()==null?null:criteria.getCurrency().getId());
        addPredicateFk("currency","id", criteria.getCurrencys());
        addPredicateFk("currency","code", criteria.getCurrency()==null?null:criteria.getCurrency().getCode());
    }

    public EnterpriseSpecification(EnterpriseCriteria criteria) {
        super(criteria);
    }

    public EnterpriseSpecification(EnterpriseCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
