package  ma.zyn.app.dao.specification.core.auth;

import ma.zyn.app.dao.criteria.core.auth.CollaboratorRoleCriteria;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class CollaboratorRoleSpecification extends  AbstractSpecification<CollaboratorRoleCriteria, CollaboratorRole>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
        addPredicateBool("canManageFinancials", criteria.getCanManageFinancials());
        addPredicateBool("canManageUsers", criteria.getCanManageUsers());
        addPredicateBool("canDeleteProperty", criteria.getCanDeleteProperty());
        addPredicateBool("canManageServiceProviders", criteria.getCanManageServiceProviders());
        addPredicateBool("canManageAiUsage", criteria.getCanManageAiUsage());
    }

    public CollaboratorRoleSpecification(CollaboratorRoleCriteria criteria) {
        super(criteria);
    }

    public CollaboratorRoleSpecification(CollaboratorRoleCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
