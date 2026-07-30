package  ma.zyn.app.ws.dto.property;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;




@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryDto  extends AuditBaseDto {

    private String name  ;
    private String code  ;


    private List<CityDto> cities ;


    public CountryDto(){
        super();
    }



    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }





    public List<CityDto> getCities(){
        return this.cities;
    }

    public void setCities(List<CityDto> cities){
        this.cities = cities;
    }



}
