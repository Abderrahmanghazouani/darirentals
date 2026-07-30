package  ma.zyn.app.dao.specification.core.document;

import ma.zyn.app.dao.criteria.core.document.DocumentTypeCriteria;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class DocumentTypeSpecification extends  AbstractSpecification<DocumentTypeCriteria, DocumentType>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public DocumentTypeSpecification(DocumentTypeCriteria criteria) {
        super(criteria);
    }

    public DocumentTypeSpecification(DocumentTypeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
