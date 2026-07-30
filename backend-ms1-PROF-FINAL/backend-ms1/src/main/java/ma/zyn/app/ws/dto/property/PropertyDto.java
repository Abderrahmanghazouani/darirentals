package  ma.zyn.app.ws.dto.property;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.math.BigDecimal;


import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.ws.dto.task.TaskPriorityDto;
import ma.zyn.app.ws.dto.report.FinancialReportPropertyDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;
import ma.zyn.app.ws.dto.reservation.ReservationPlatformDto;
import ma.zyn.app.ws.dto.report.FinancialReportDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestStatusDto;
import ma.zyn.app.ws.dto.document.DocumentDto;
import ma.zyn.app.ws.dto.task.TaskStatusDto;
import ma.zyn.app.ws.dto.charge.ChargeDto;
import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.payment.PaymentDto;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.charge.ChargeTypeDto;
import ma.zyn.app.ws.dto.task.TaskTypeDto;
import ma.zyn.app.ws.dto.reservation.ReservationStatusDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
import ma.zyn.app.ws.dto.client.ClientDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PropertyDto  extends AuditBaseDto {

    private String name  ;
    private String streetNumber  ;
    private String streetName  ;
    private String postalCode  ;
    private Integer capacity  = 0 ;
    private BigDecimal pricePerNight  ;
    private BigDecimal latitude  ;
    private BigDecimal longitude  ;

    private PropertyTypeDto propertyType ;
    private PropertyStatusDto propertyStatus ;
    private CityDto city ;
    private EnterpriseDto enterprise ;

    private List<ReservationDto> reservations ;
    private List<ChargeDto> charges ;
    private List<TaskDto> tasks ;
    private List<FinancialReportPropertyDto> financialReportProperties ;
    private List<ReservationRequestDto> reservationRequests ;
    private List<ReservationRequestDto> alternativeRequests ;


    public PropertyDto(){
        super();
    }



    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getStreetNumber(){
        return this.streetNumber;
    }
    public void setStreetNumber(String streetNumber){
        this.streetNumber = streetNumber;
    }

    public String getStreetName(){
        return this.streetName;
    }
    public void setStreetName(String streetName){
        this.streetName = streetName;
    }

    public String getPostalCode(){
        return this.postalCode;
    }
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }

    public Integer getCapacity(){
        return this.capacity;
    }
    public void setCapacity(Integer capacity){
        this.capacity = capacity;
    }

    public BigDecimal getPricePerNight(){
        return this.pricePerNight;
    }
    public void setPricePerNight(BigDecimal pricePerNight){
        this.pricePerNight = pricePerNight;
    }

    public BigDecimal getLatitude(){
        return this.latitude;
    }
    public void setLatitude(BigDecimal latitude){
        this.latitude = latitude;
    }

    public BigDecimal getLongitude(){
        return this.longitude;
    }
    public void setLongitude(BigDecimal longitude){
        this.longitude = longitude;
    }


    public PropertyTypeDto getPropertyType(){
        return this.propertyType;
    }

    public void setPropertyType(PropertyTypeDto propertyType){
        this.propertyType = propertyType;
    }
    public PropertyStatusDto getPropertyStatus(){
        return this.propertyStatus;
    }

    public void setPropertyStatus(PropertyStatusDto propertyStatus){
        this.propertyStatus = propertyStatus;
    }
    public CityDto getCity(){
        return this.city;
    }

    public void setCity(CityDto city){
        this.city = city;
    }
    public EnterpriseDto getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseDto enterprise){
        this.enterprise = enterprise;
    }



    public List<ReservationDto> getReservations(){
        return this.reservations;
    }

    public void setReservations(List<ReservationDto> reservations){
        this.reservations = reservations;
    }
    public List<ChargeDto> getCharges(){
        return this.charges;
    }

    public void setCharges(List<ChargeDto> charges){
        this.charges = charges;
    }
    public List<TaskDto> getTasks(){
        return this.tasks;
    }

    public void setTasks(List<TaskDto> tasks){
        this.tasks = tasks;
    }
    public List<FinancialReportPropertyDto> getFinancialReportProperties(){
        return this.financialReportProperties;
    }

    public void setFinancialReportProperties(List<FinancialReportPropertyDto> financialReportProperties){
        this.financialReportProperties = financialReportProperties;
    }
    public List<ReservationRequestDto> getReservationRequests(){
        return this.reservationRequests;
    }

    public void setReservationRequests(List<ReservationRequestDto> reservationRequests){
        this.reservationRequests = reservationRequests;
    }
    public List<ReservationRequestDto> getAlternativeRequests(){
        return this.alternativeRequests;
    }

    public void setAlternativeRequests(List<ReservationRequestDto> alternativeRequests){
        this.alternativeRequests = alternativeRequests;
    }



}
