package  ma.zyn.app.ws.dto.property;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.report.FinancialReportPropertyDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;
import ma.zyn.app.ws.dto.report.FinancialReportDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
import ma.zyn.app.ws.dto.charge.ChargeDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityDto  extends AuditBaseDto {

    private String name  ;

    private CountryDto country ;

    private List<PropertyDto> properties ;


    public CityDto(){
        super();
    }



    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }


    public CountryDto getCountry(){
        return this.country;
    }

    public void setCountry(CountryDto country){
        this.country = country;
    }



    public List<PropertyDto> getProperties(){
        return this.properties;
    }

    public void setProperties(List<PropertyDto> properties){
        this.properties = properties;
    }



}
