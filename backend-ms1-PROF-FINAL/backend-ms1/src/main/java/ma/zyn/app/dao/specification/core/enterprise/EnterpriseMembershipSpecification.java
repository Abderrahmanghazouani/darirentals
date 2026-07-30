package  ma.zyn.app.dao.specification.core.enterprise;

import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseMembershipCriteria;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class EnterpriseMembershipSpecification extends  AbstractSpecification<EnterpriseMembershipCriteria, EnterpriseMembership>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateFk("collaborator","id", criteria.getCollaborator()==null?null:criteria.getCollaborator().getId());
        addPredicateFk("collaborator","id", criteria.getCollaborators());
        addPredicateFk("collaborator","email", criteria.getCollaborator()==null?null:criteria.getCollaborator().getEmail());
        addPredicateFk("enterprise","id", criteria.getEnterprise()==null?null:criteria.getEnterprise().getId());
        addPredicateFk("enterprise","id", criteria.getEnterprises());
        addPredicateFk("collaboratorRole","id", criteria.getCollaboratorRole()==null?null:criteria.getCollaboratorRole().getId());
        addPredicateFk("collaboratorRole","id", criteria.getCollaboratorRoles());
        addPredicateFk("collaboratorRole","code", criteria.getCollaboratorRole()==null?null:criteria.getCollaboratorRole().getCode());
    }

    public EnterpriseMembershipSpecification(EnterpriseMembershipCriteria criteria) {
        super(criteria);
    }

    public EnterpriseMembershipSpecification(EnterpriseMembershipCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
