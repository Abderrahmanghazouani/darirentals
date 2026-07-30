package  ma.zyn.app.dao.specification.core.payment;

import ma.zyn.app.dao.criteria.core.payment.PaymentStatusCriteria;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class PaymentStatusSpecification extends  AbstractSpecification<PaymentStatusCriteria, PaymentStatus>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public PaymentStatusSpecification(PaymentStatusCriteria criteria) {
        super(criteria);
    }

    public PaymentStatusSpecification(PaymentStatusCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
