package  ma.zyn.app.dao.specification.core.document;

import ma.zyn.app.dao.criteria.core.document.DocumentCriteria;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class DocumentSpecification extends  AbstractSpecification<DocumentCriteria, Document>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("fileName", criteria.getFileName(),criteria.getFileNameLike());
        addPredicate("file", criteria.getFile(),criteria.getFileLike());
        addPredicate("extractedVendor", criteria.getExtractedVendor(),criteria.getExtractedVendorLike());
        addPredicateBigDecimal("extractedAmount", criteria.getExtractedAmount(), criteria.getExtractedAmountMin(), criteria.getExtractedAmountMax());
        addPredicateFk("documentType","id", criteria.getDocumentType()==null?null:criteria.getDocumentType().getId());
        addPredicateFk("documentType","id", criteria.getDocumentTypes());
        addPredicateFk("documentType","code", criteria.getDocumentType()==null?null:criteria.getDocumentType().getCode());
        addPredicateFk("reservation","id", criteria.getReservation()==null?null:criteria.getReservation().getId());
        addPredicateFk("reservation","id", criteria.getReservations());
        addPredicateFk("reservation","reference", criteria.getReservation()==null?null:criteria.getReservation().getReference());
        addPredicateFk("charge","id", criteria.getCharge()==null?null:criteria.getCharge().getId());
        addPredicateFk("charge","id", criteria.getCharges());
    }

    public DocumentSpecification(DocumentCriteria criteria) {
        super(criteria);
    }

    public DocumentSpecification(DocumentCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
