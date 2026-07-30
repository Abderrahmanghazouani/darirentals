package  ma.zyn.app.dao.specification.core.property;

import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.zynerator.specification.AbstractSpecification;


public class PropertySpecification extends  AbstractSpecification<PropertyCriteria, Property>  {

    @Override
    public void constructPredicates() {
        addPredicateId("id", criteria);
        addPredicate("name", criteria.getName(),criteria.getNameLike());
        addPredicate("streetNumber", criteria.getStreetNumber(),criteria.getStreetNumberLike());
        addPredicate("streetName", criteria.getStreetName(),criteria.getStreetNameLike());
        addPredicate("postalCode", criteria.getPostalCode(),criteria.getPostalCodeLike());
        addPredicateInt("capacity", criteria.getCapacity(), criteria.getCapacityMin(), criteria.getCapacityMax());
        addPredicateBigDecimal("pricePerNight", criteria.getPricePerNight(), criteria.getPricePerNightMin(), criteria.getPricePerNightMax());
        addPredicateBigDecimal("latitude", criteria.getLatitude(), criteria.getLatitudeMin(), criteria.getLatitudeMax());
        addPredicateBigDecimal("longitude", criteria.getLongitude(), criteria.getLongitudeMin(), criteria.getLongitudeMax());
        addPredicateFk("propertyType","id", criteria.getPropertyType()==null?null:criteria.getPropertyType().getId());
        addPredicateFk("propertyType","id", criteria.getPropertyTypes());
        addPredicateFk("propertyType","code", criteria.getPropertyType()==null?null:criteria.getPropertyType().getCode());
        addPredicateFk("propertyStatus","id", criteria.getPropertyStatus()==null?null:criteria.getPropertyStatus().getId());
        addPredicateFk("propertyStatus","id", criteria.getPropertyStatuss());
        addPredicateFk("propertyStatus","code", criteria.getPropertyStatus()==null?null:criteria.getPropertyStatus().getCode());
        addPredicateFk("city","id", criteria.getCity()==null?null:criteria.getCity().getId());
        addPredicateFk("city","id", criteria.getCitys());
        addPredicateFk("enterprise","id", criteria.getEnterprise()==null?null:criteria.getEnterprise().getId());
        addPredicateFk("enterprise","id", criteria.getEnterprises());
    }

    public PropertySpecification(PropertyCriteria criteria) {
        super(criteria);
    }

    public PropertySpecification(PropertyCriteria criteria, boolean distinct) {
        super(criteria, distinct);
    }

}
