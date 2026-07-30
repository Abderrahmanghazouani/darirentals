package  ma.zyn.app.dao.specification.core.property;

import ma.zyn.app.dao.criteria.core.property.CountryCriteria;
import ma.zyn.app.bean.core.property.Country;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class CountrySpecification extends  AbstractSpecification<CountryCriteria, Country>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("name", criteria.getName(),criteria.getNameLike());
        addPredicate("code", criteria.getCode(),criteria.getCodeLike());
    }

    public CountrySpecification(CountryCriteria criteria) {
        super(criteria);
    }

    public CountrySpecification(CountryCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
