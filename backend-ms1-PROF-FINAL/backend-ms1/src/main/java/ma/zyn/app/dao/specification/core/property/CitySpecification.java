package  ma.zyn.app.dao.specification.core.property;

import ma.zyn.app.dao.criteria.core.property.CityCriteria;
import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class CitySpecification extends  AbstractSpecification<CityCriteria, City>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("name", criteria.getName(),criteria.getNameLike());
        addPredicateFk("country","id", criteria.getCountry()==null?null:criteria.getCountry().getId());
        addPredicateFk("country","id", criteria.getCountrys());
        addPredicateFk("country","code", criteria.getCountry()==null?null:criteria.getCountry().getCode());
    }

    public CitySpecification(CityCriteria criteria) {
        super(criteria);
    }

    public CitySpecification(CityCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
