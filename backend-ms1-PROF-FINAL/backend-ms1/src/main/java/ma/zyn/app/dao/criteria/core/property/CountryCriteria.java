package  ma.zyn.app.dao.criteria.core.property;



import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class CountryCriteria extends  BaseCriteria  {

    private String name;
    private String nameLike;
    private String code;
    private String codeLike;



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

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCodeLike(){
        return this.codeLike;
    }
    public void setCodeLike(String codeLike){
        this.codeLike = codeLike;
    }


}
