package  ma.zyn.app.dao.specification.core.payment;

import ma.zyn.app.dao.criteria.core.payment.PaymentTypeCriteria;
import ma.zyn.app.bean.core.payment.PaymentType;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class PaymentTypeSpecification extends  AbstractSpecification<PaymentTypeCriteria, PaymentType>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
    }

    public PaymentTypeSpecification(PaymentTypeCriteria criteria) {
        super(criteria);
    }

    public PaymentTypeSpecification(PaymentTypeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
