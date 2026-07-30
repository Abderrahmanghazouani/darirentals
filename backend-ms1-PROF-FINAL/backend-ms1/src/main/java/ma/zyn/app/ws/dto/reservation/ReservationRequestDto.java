package  ma.zyn.app.ws.dto.reservation;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;



import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.property.PropertyDto;
import ma.zyn.app.ws.dto.client.ClientDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReservationRequestDto  extends AuditBaseDto {

    private String clientNote  ;
    private String staffNote  ;

    private ClientDto client ;
    private PropertyDto requestedProperty ;
    private PropertyDto alternativeProperty ;
    private CollaboratorDto reviewedBy ;
    private ReservationRequestStatusDto reservationRequestStatus ;
    private ReservationDto reservation ;



    public ReservationRequestDto(){
        super();
    }



    public String getClientNote(){
        return this.clientNote;
    }
    public void setClientNote(String clientNote){
        this.clientNote = clientNote;
    }

    public String getStaffNote(){
        return this.staffNote;
    }
    public void setStaffNote(String staffNote){
        this.staffNote = staffNote;
    }


    public ClientDto getClient(){
        return this.client;
    }

    public void setClient(ClientDto client){
        this.client = client;
    }
    public PropertyDto getRequestedProperty(){
        return this.requestedProperty;
    }

    public void setRequestedProperty(PropertyDto requestedProperty){
        this.requestedProperty = requestedProperty;
    }
    public PropertyDto getAlternativeProperty(){
        return this.alternativeProperty;
    }

    public void setAlternativeProperty(PropertyDto alternativeProperty){
        this.alternativeProperty = alternativeProperty;
    }
    public CollaboratorDto getReviewedBy(){
        return this.reviewedBy;
    }

    public void setReviewedBy(CollaboratorDto reviewedBy){
        this.reviewedBy = reviewedBy;
    }
    public ReservationRequestStatusDto getReservationRequestStatus(){
        return this.reservationRequestStatus;
    }

    public void setReservationRequestStatus(ReservationRequestStatusDto reservationRequestStatus){
        this.reservationRequestStatus = reservationRequestStatus;
    }
    public ReservationDto getReservation(){
        return this.reservation;
    }

    public void setReservation(ReservationDto reservation){
        this.reservation = reservation;
    }






}
