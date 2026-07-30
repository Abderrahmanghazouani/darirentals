package  ma.zyn.app.dao.specification.core.charge;

import ma.zyn.app.dao.criteria.core.charge.ChargeCriteria;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ChargeSpecification extends  AbstractSpecification<ChargeCriteria, Charge>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicateBigDecimal("amount", criteria.getAmount(), criteria.getAmountMin(), criteria.getAmountMax());
        addPredicateFk("property","id", criteria.getProperty()==null?null:criteria.getProperty().getId());
        addPredicateFk("property","id", criteria.getPropertys());
        addPredicateFk("chargeType","id", criteria.getChargeType()==null?null:criteria.getChargeType().getId());
        addPredicateFk("chargeType","id", criteria.getChargeTypes());
        addPredicateFk("chargeType","code", criteria.getChargeType()==null?null:criteria.getChargeType().getCode());
        addPredicateFk("payment","id", criteria.getPayment()==null?null:criteria.getPayment().getId());
        addPredicateFk("payment","id", criteria.getPayments());
    }

    public ChargeSpecification(ChargeCriteria criteria) {
        super(criteria);
    }

    public ChargeSpecification(ChargeCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
