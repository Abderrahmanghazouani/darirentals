package  ma.zyn.app.dao.specification.core.payment;

import ma.zyn.app.dao.criteria.core.payment.PaymentCriteria;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class PaymentSpecification extends  AbstractSpecification<PaymentCriteria, Payment>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateBigDecimal("amount", criteria.getAmount(), criteria.getAmountMin(), criteria.getAmountMax());
        addPredicateFk("serviceProvider","id", criteria.getServiceProvider()==null?null:criteria.getServiceProvider().getId());
        addPredicateFk("serviceProvider","id", criteria.getServiceProviders());
        addPredicateFk("paymentType","id", criteria.getPaymentType()==null?null:criteria.getPaymentType().getId());
        addPredicateFk("paymentType","id", criteria.getPaymentTypes());
        addPredicateFk("paymentType","code", criteria.getPaymentType()==null?null:criteria.getPaymentType().getCode());
        addPredicateFk("paymentStatus","id", criteria.getPaymentStatus()==null?null:criteria.getPaymentStatus().getId());
        addPredicateFk("paymentStatus","id", criteria.getPaymentStatuss());
        addPredicateFk("paymentStatus","code", criteria.getPaymentStatus()==null?null:criteria.getPaymentStatus().getCode());
    }

    public PaymentSpecification(PaymentCriteria criteria) {
        super(criteria);
    }

    public PaymentSpecification(PaymentCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
