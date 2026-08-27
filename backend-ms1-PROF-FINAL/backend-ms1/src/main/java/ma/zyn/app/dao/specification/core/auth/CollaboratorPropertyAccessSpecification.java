package  ma.zyn.app.dao.specification.core.auth;

import ma.zyn.app.dao.criteria.core.auth.CollaboratorPropertyAccessCriteria;
import ma.zyn.app.bean.core.auth.CollaboratorPropertyAccess;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class CollaboratorPropertyAccessSpecification extends  AbstractSpecification<CollaboratorPropertyAccessCriteria, CollaboratorPropertyAccess>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateFk("collaborator","id", criteria.getCollaborator()==null?null:criteria.getCollaborator().getId());
        addPredicateFk("collaborator","id", criteria.getCollaborators());
        addPredicateFk("property","id", criteria.getProperty()==null?null:criteria.getProperty().getId());
        addPredicateFk("property","id", criteria.getPropertys());
    }

    public CollaboratorPropertyAccessSpecification(CollaboratorPropertyAccessCriteria criteria) {
        super(criteria);
    }

    public CollaboratorPropertyAccessSpecification(CollaboratorPropertyAccessCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
