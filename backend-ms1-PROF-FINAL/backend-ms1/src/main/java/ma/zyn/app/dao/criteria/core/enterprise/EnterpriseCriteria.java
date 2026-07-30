package  ma.zyn.app.dao.criteria.core.enterprise;


import ma.zyn.app.dao.criteria.core.currency.CurrencyCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class EnterpriseCriteria extends  BaseCriteria  {

    private String name;
    private String nameLike;
    private String phone;
    private String phoneLike;
    private String address;
    private String addressLike;

    private CurrencyCriteria currency ;
    private List<CurrencyCriteria> currencys ;


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

    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    public String getAddressLike(){
        return this.addressLike;
    }
    public void setAddressLike(String addressLike){
        this.addressLike = addressLike;
    }


    public CurrencyCriteria getCurrency(){
        return this.currency;
    }

    public void setCurrency(CurrencyCriteria currency){
        this.currency = currency;
    }
    public List<CurrencyCriteria> getCurrencys(){
        return this.currencys;
    }

    public void setCurrencys(List<CurrencyCriteria> currencys){
        this.currencys = currencys;
    }
}
