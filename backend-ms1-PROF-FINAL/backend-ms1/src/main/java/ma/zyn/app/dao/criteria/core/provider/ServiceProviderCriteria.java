package  ma.zyn.app.dao.criteria.core.provider;


import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class ServiceProviderCriteria extends  BaseCriteria  {

    private String name;
    private String nameLike;
    private String phone;
    private String phoneLike;
    private Boolean isActive;

    private ServiceTypeCriteria serviceType ;
    private List<ServiceTypeCriteria> serviceTypes ;
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

    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getPhoneLike(){
        return this.phoneLike;
    }
    public void setPhoneLike(String phoneLike){
        this.phoneLike = phoneLike;
    }

    public Boolean getIsActive(){
        return this.isActive;
    }
    public void setIsActive(Boolean isActive){
        this.isActive = isActive;
    }

    public ServiceTypeCriteria getServiceType(){
        return this.serviceType;
    }

    public void setServiceType(ServiceTypeCriteria serviceType){
        this.serviceType = serviceType;
    }
    public List<ServiceTypeCriteria> getServiceTypes(){
        return this.serviceTypes;
    }

    public void setServiceTypes(List<ServiceTypeCriteria> serviceTypes){
        this.serviceTypes = serviceTypes;
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
