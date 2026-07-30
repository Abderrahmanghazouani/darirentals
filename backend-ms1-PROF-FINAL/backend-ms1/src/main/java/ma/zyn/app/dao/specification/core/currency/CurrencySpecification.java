package  ma.zyn.app.dao.specification.core.currency;

import ma.zyn.app.dao.criteria.core.currency.CurrencyCriteria;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class CurrencySpecification extends  AbstractSpecification<CurrencyCriteria, Currency>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
        addPredicate("label", criteria.getLabel(),criteria.getLabelLike());
        addPredicate("style", criteria.getStyle(),criteria.getStyleLike());
        addPredicateBool("isDefault", criteria.getIsDefault());
        addPredicateInt("sortOrder", criteria.getSortOrder(), criteria.getSortOrderMin(), criteria.getSortOrderMax());
        addPredicate("symbol", criteria.getSymbol(),criteria.getSymbolLike());
    }

    public CurrencySpecification(CurrencyCriteria criteria) {
        super(criteria);
    }

    public CurrencySpecification(CurrencyCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
