package  ma.zyn.app.ws.dto.reservation;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.math.BigDecimal;


import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.ws.dto.task.TaskPriorityDto;
import ma.zyn.app.ws.dto.document.DocumentTypeDto;
import ma.zyn.app.ws.dto.document.DocumentDto;
import ma.zyn.app.ws.dto.task.TaskStatusDto;
import ma.zyn.app.ws.dto.charge.ChargeDto;
import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
import ma.zyn.app.ws.dto.task.TaskTypeDto;
import ma.zyn.app.ws.dto.property.PropertyDto;
import ma.zyn.app.ws.dto.client.ClientDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationDto  extends AuditBaseDto {

    private String reference  ;
    private BigDecimal amount  ;
    private BigDecimal pricePerNight  ;

    private ClientDto client ;
    private PropertyDto property ;
    private ReservationPlatformDto reservationPlatform ;
    private ReservationStatusDto reservationStatus ;

    private List<DocumentDto> documents ;
    private List<TaskDto> tasks ;
    private List<ReservationRequestDto> reservationRequests ;


    public ReservationDto(){
        super();
    }



    public String getReference(){
        return this.reference;
    }
    public void setReference(String reference){
        this.reference = reference;
    }

    public BigDecimal getAmount(){
        return this.amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public BigDecimal getPricePerNight(){
        return this.pricePerNight;
    }
    public void setPricePerNight(BigDecimal pricePerNight){
        this.pricePerNight = pricePerNight;
    }


    public ClientDto getClient(){
        return this.client;
    }

    public void setClient(ClientDto client){
        this.client = client;
    }
    public PropertyDto getProperty(){
        return this.property;
    }

    public void setProperty(PropertyDto property){
        this.property = property;
    }
    public ReservationPlatformDto getReservationPlatform(){
        return this.reservationPlatform;
    }

    public void setReservationPlatform(ReservationPlatformDto reservationPlatform){
        this.reservationPlatform = reservationPlatform;
    }
    public ReservationStatusDto getReservationStatus(){
        return this.reservationStatus;
    }

    public void setReservationStatus(ReservationStatusDto reservationStatus){
        this.reservationStatus = reservationStatus;
    }



    public List<DocumentDto> getDocuments(){
        return this.documents;
    }

    public void setDocuments(List<DocumentDto> documents){
        this.documents = documents;
    }
    public List<TaskDto> getTasks(){
        return this.tasks;
    }

    public void setTasks(List<TaskDto> tasks){
        this.tasks = tasks;
    }
    public List<ReservationRequestDto> getReservationRequests(){
        return this.reservationRequests;
    }

    public void setReservationRequests(List<ReservationRequestDto> reservationRequests){
        this.reservationRequests = reservationRequests;
    }



}
