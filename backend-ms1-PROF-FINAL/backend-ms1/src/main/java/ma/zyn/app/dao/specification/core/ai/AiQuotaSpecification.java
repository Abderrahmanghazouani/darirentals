package  ma.zyn.app.dao.specification.core.ai;

import ma.zyn.app.dao.criteria.core.ai.AiQuotaCriteria;
import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class AiQuotaSpecification extends  AbstractSpecification<AiQuotaCriteria, AiQuota>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateLong("tokensAllocated", criteria.getTokensAllocated(), criteria.getTokensAllocatedMin(), criteria.getTokensAllocatedMax());
        addPredicateFk("enterprise","id", criteria.getEnterprise()==null?null:criteria.getEnterprise().getId());
        addPredicateFk("enterprise","id", criteria.getEnterprises());
    }

    public AiQuotaSpecification(AiQuotaCriteria criteria) {
        super(criteria);
    }

    public AiQuotaSpecification(AiQuotaCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
