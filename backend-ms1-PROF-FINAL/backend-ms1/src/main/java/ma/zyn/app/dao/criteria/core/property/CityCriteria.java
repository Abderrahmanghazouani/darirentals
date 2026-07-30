package  ma.zyn.app.dao.criteria.core.property;



import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class CityCriteria extends  BaseCriteria  {

    private String name;
    private String nameLike;

    private CountryCriteria country ;
    private List<CountryCriteria> countrys ;


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


    public CountryCriteria getCountry(){
        return this.country;
    }

    public void setCountry(CountryCriteria country){
        this.country = country;
    }
    public List<CountryCriteria> getCountrys(){
        return this.countrys;
    }

    public void setCountrys(List<CountryCriteria> countrys){
        this.countrys = countrys;
    }
}
