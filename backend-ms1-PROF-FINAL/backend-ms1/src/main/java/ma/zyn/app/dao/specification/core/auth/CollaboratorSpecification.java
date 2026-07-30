package  ma.zyn.app.dao.specification.core.auth;

import ma.zyn.app.dao.criteria.core.auth.CollaboratorCriteria;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class CollaboratorSpecification extends  AbstractSpecification<CollaboratorCriteria, Collaborator>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("name", criteria.getName(),criteria.getNameLike());
        addPredicate("phone", criteria.getPhone(),criteria.getPhoneLike());
        addPredicateBool("isActive", criteria.getIsActive());
        addPredicate("email", criteria.getEmail(),criteria.getEmailLike());
        addPredicateBool("enabled", criteria.getEnabled());
        addPredicateBool("credentialsNonExpired", criteria.getCredentialsNonExpired());
        addPredicateBool("accountNonExpired", criteria.getAccountNonExpired());
        addPredicate("username", criteria.getUsername(),criteria.getUsernameLike());
        addPredicateBool("passwordChanged", criteria.getPasswordChanged());
        addPredicateBool("accountNonLocked", criteria.getAccountNonLocked());
        addPredicate("password", criteria.getPassword(),criteria.getPasswordLike());
        addPredicateFk("displayCurrency","id", criteria.getDisplayCurrency()==null?null:criteria.getDisplayCurrency().getId());
        addPredicateFk("displayCurrency","id", criteria.getDisplayCurrencys());
        addPredicateFk("displayCurrency","code", criteria.getDisplayCurrency()==null?null:criteria.getDisplayCurrency().getCode());
    }

    public CollaboratorSpecification(CollaboratorCriteria criteria) {
        super(criteria);
    }

    public CollaboratorSpecification(CollaboratorCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
