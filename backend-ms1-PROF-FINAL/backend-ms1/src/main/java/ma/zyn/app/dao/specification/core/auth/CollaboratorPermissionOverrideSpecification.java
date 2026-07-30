package  ma.zyn.app.dao.specification.core.auth;

import ma.zyn.app.dao.criteria.core.auth.CollaboratorPermissionOverrideCriteria;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class CollaboratorPermissionOverrideSpecification extends  AbstractSpecification<CollaboratorPermissionOverrideCriteria, CollaboratorPermissionOverride>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateBool("canManageFinancials", criteria.getCanManageFinancials());
        addPredicateBool("canManageUsers", criteria.getCanManageUsers());
        addPredicateBool("canDeleteProperty", criteria.getCanDeleteProperty());
        addPredicateBool("canManageServiceProviders", criteria.getCanManageServiceProviders());
        addPredicateBool("canManageAiUsage", criteria.getCanManageAiUsage());
        addPredicateFk("enterpriseMembership","id", criteria.getEnterpriseMembership()==null?null:criteria.getEnterpriseMembership().getId());
        addPredicateFk("enterpriseMembership","id", criteria.getEnterpriseMemberships());
    }

    public CollaboratorPermissionOverrideSpecification(CollaboratorPermissionOverrideCriteria criteria) {
        super(criteria);
    }

    public CollaboratorPermissionOverrideSpecification(CollaboratorPermissionOverrideCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
