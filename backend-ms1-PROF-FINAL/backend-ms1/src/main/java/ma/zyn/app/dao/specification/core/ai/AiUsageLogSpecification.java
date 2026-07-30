package  ma.zyn.app.dao.specification.core.ai;

import ma.zyn.app.dao.criteria.core.ai.AiUsageLogCriteria;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class AiUsageLogSpecification extends  AbstractSpecification<AiUsageLogCriteria, AiUsageLog>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateLong("tokensUsed", criteria.getTokensUsed(), criteria.getTokensUsedMin(), criteria.getTokensUsedMax());
        addPredicate("date", criteria.getDate(), criteria.getDateFrom(), criteria.getDateTo());
        addPredicateFk("enterprise","id", criteria.getEnterprise()==null?null:criteria.getEnterprise().getId());
        addPredicateFk("enterprise","id", criteria.getEnterprises());
        addPredicateFk("aiUsageType","id", criteria.getAiUsageType()==null?null:criteria.getAiUsageType().getId());
        addPredicateFk("aiUsageType","id", criteria.getAiUsageTypes());
        addPredicateFk("aiUsageType","code", criteria.getAiUsageType()==null?null:criteria.getAiUsageType().getCode());
        addPredicateFk("collaborator","id", criteria.getCollaborator()==null?null:criteria.getCollaborator().getId());
        addPredicateFk("collaborator","id", criteria.getCollaborators());
        addPredicateFk("collaborator","email", criteria.getCollaborator()==null?null:criteria.getCollaborator().getEmail());
        addPredicateFk("document","id", criteria.getDocument()==null?null:criteria.getDocument().getId());
        addPredicateFk("document","id", criteria.getDocuments());
    }

    public AiUsageLogSpecification(AiUsageLogCriteria criteria) {
        super(criteria);
    }

    public AiUsageLogSpecification(AiUsageLogCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
