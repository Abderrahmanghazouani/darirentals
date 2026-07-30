package  ma.zyn.app.dao.criteria.core.property;


import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class PropertyCriteria extends  BaseCriteria  {

    private String name;
    private String nameLike;
    private String streetNumber;
    private String streetNumberLike;
    private String streetName;
    private String streetNameLike;
    private String postalCode;
    private String postalCodeLike;
    private String capacity;
    private String capacityMin;
    private String capacityMax;
    private String pricePerNight;
    private String pricePerNightMin;
    private String pricePerNightMax;
    private String latitude;
    private String latitudeMin;
    private String latitudeMax;
    private String longitude;
    private String longitudeMin;
    private String longitudeMax;

    private PropertyTypeCriteria propertyType ;
    private List<PropertyTypeCriteria> propertyTypes ;
    private PropertyStatusCriteria propertyStatus ;
    private List<PropertyStatusCriteria> propertyStatuss ;
    private CityCriteria city ;
    private List<CityCriteria> citys ;
    private EnterpriseCriteria enterprise ;
    private List<EnterpriseCriteria> enterprises ;


    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getNameLike(){
        return this.nameLike;
    }
    public void setNameLike(String nameLike){
        this.nameLike = nameLike;
    }

    public String getStreetNumber(){
        return this.streetNumber;
    }
    public void setStreetNumber(String streetNumber){
        this.streetNumber = streetNumber;
    }
    public String getStreetNumberLike(){
        return this.streetNumberLike;
    }
    public void setStreetNumberLike(String streetNumberLike){
        this.streetNumberLike = streetNumberLike;
    }

    public String getStreetName(){
        return this.streetName;
    }
    public void setStreetName(String streetName){
        this.streetName = streetName;
    }
    public String getStreetNameLike(){
        return this.streetNameLike;
    }
    public void setStreetNameLike(String streetNameLike){
        this.streetNameLike = streetNameLike;
    }

    public String getPostalCode(){
        return this.postalCode;
    }
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    public String getPostalCodeLike(){
        return this.postalCodeLike;
    }
    public void setPostalCodeLike(String postalCodeLike){
        this.postalCodeLike = postalCodeLike;
    }

    public String getCapacity(){
        return this.capacity;
    }
    public void setCapacity(String capacity){
        this.capacity = capacity;
    }   
    public String getCapacityMin(){
        return this.capacityMin;
    }
    public void setCapacityMin(String capacityMin){
        this.capacityMin = capacityMin;
    }
    public String getCapacityMax(){
        return this.capacityMax;
    }
    public void setCapacityMax(String capacityMax){
        this.capacityMax = capacityMax;
    }
      
    public String getPricePerNight(){
        return this.pricePerNight;
    }
    public void setPricePerNight(String pricePerNight){
        this.pricePerNight = pricePerNight;
    }   
    public String getPricePerNightMin(){
        return this.pricePerNightMin;
    }
    public void setPricePerNightMin(String pricePerNightMin){
        this.pricePerNightMin = pricePerNightMin;
    }
    public String getPricePerNightMax(){
        return this.pricePerNightMax;
    }
    public void setPricePerNightMax(String pricePerNightMax){
        this.pricePerNightMax = pricePerNightMax;
    }
      
    public String getLatitude(){
        return this.latitude;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }   
    public String getLatitudeMin(){
        return this.latitudeMin;
    }
    public void setLatitudeMin(String latitudeMin){
        this.latitudeMin = latitudeMin;
    }
    public String getLatitudeMax(){
        return this.latitudeMax;
    }
    public void setLatitudeMax(String latitudeMax){
        this.latitudeMax = latitudeMax;
    }
      
    public String getLongitude(){
        return this.longitude;
    }
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }   
    public String getLongitudeMin(){
        return this.longitudeMin;
    }
    public void setLongitudeMin(String longitudeMin){
        this.longitudeMin = longitudeMin;
    }
    public String getLongitudeMax(){
        return this.longitudeMax;
    }
    public void setLongitudeMax(String longitudeMax){
        this.longitudeMax = longitudeMax;
    }
      

    public PropertyTypeCriteria getPropertyType(){
        return this.propertyType;
    }

    public void setPropertyType(PropertyTypeCriteria propertyType){
        this.propertyType = propertyType;
    }
    public List<PropertyTypeCriteria> getPropertyTypes(){
        return this.propertyTypes;
    }

    public void setPropertyTypes(List<PropertyTypeCriteria> propertyTypes){
        this.propertyTypes = propertyTypes;
    }
    public PropertyStatusCriteria getPropertyStatus(){
        return this.propertyStatus;
    }

    public void setPropertyStatus(PropertyStatusCriteria propertyStatus){
        this.propertyStatus = propertyStatus;
    }
    public List<PropertyStatusCriteria> getPropertyStatuss(){
        return this.propertyStatuss;
    }

    public void setPropertyStatuss(List<PropertyStatusCriteria> propertyStatuss){
        this.propertyStatuss = propertyStatuss;
    }
    public CityCriteria getCity(){
        return this.city;
    }

    public void setCity(CityCriteria city){
        this.city = city;
    }
    public List<CityCriteria> getCitys(){
        return this.citys;
    }

    public void setCitys(List<CityCriteria> citys){
        this.citys = citys;
    }
    public EnterpriseCriteria getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseCriteria enterprise){
        this.enterprise = enterprise;
    }
    public List<EnterpriseCriteria> getEnterprises(){
        return this.enterprises;
    }

    public void setEnterprises(List<EnterpriseCriteria> enterprises){
        this.enterprises = enterprises;
    }
}
