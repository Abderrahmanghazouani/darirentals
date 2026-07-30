package  ma.zyn.app.dao.specification.core.currency;

import ma.zyn.app.dao.criteria.core.currency.ExchangeRateCriteria;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class ExchangeRateSpecification extends  AbstractSpecification<ExchangeRateCriteria, ExchangeRate>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicateBigDecimal("rate", criteria.getRate(), criteria.getRateMin(), criteria.getRateMax());
        addPredicate("source", criteria.getSource(),criteria.getSourceLike());
        addPredicateFk("baseCurrency","id", criteria.getBaseCurrency()==null?null:criteria.getBaseCurrency().getId());
        addPredicateFk("baseCurrency","id", criteria.getBaseCurrencys());
        addPredicateFk("baseCurrency","code", criteria.getBaseCurrency()==null?null:criteria.getBaseCurrency().getCode());
        addPredicateFk("targetCurrency","id", criteria.getTargetCurrency()==null?null:criteria.getTargetCurrency().getId());
        addPredicateFk("targetCurrency","id", criteria.getTargetCurrencys());
        addPredicateFk("targetCurrency","code", criteria.getTargetCurrency()==null?null:criteria.getTargetCurrency().getCode());
    }

    public ExchangeRateSpecification(ExchangeRateCriteria criteria) {
        super(criteria);
    }

    public ExchangeRateSpecification(ExchangeRateCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
