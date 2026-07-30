package  ma.zyn.app.dao.criteria.core.reservation;


import ma.zyn.app.dao.criteria.core.auth.CollaboratorCriteria;
import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;
import ma.zyn.app.dao.criteria.core.client.ClientCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class ReservationRequestCriteria extends  BaseCriteria  {

    private String clientNote;
    private String clientNoteLike;
    private String staffNote;
    private String staffNoteLike;

    private ClientCriteria client ;
    private List<ClientCriteria> clients ;
    private PropertyCriteria requestedProperty ;
    private List<PropertyCriteria> requestedPropertys ;
    private PropertyCriteria alternativeProperty ;
    private List<PropertyCriteria> alternativePropertys ;
    private CollaboratorCriteria reviewedBy ;
    private List<CollaboratorCriteria> reviewedBys ;
    private ReservationRequestStatusCriteria reservationRequestStatus ;
    private List<ReservationRequestStatusCriteria> reservationRequestStatuss ;
    private ReservationCriteria reservation ;
    private List<ReservationCriteria> reservations ;


    public String getClientNote(){
        return this.clientNote;
    }
    public void setClientNote(String clientNote){
        this.clientNote = clientNote;
    }
    public String getClientNoteLike(){
        return this.clientNoteLike;
    }
    public void setClientNoteLike(String clientNoteLike){
        this.clientNoteLike = clientNoteLike;
    }

    public String getStaffNote(){
        return this.staffNote;
    }
    public void setStaffNote(String staffNote){
        this.staffNote = staffNote;
    }
    public String getStaffNoteLike(){
        return this.staffNoteLike;
    }
    public void setStaffNoteLike(String staffNoteLike){
        this.staffNoteLike = staffNoteLike;
    }


    public ClientCriteria getClient(){
        return this.client;
    }

    public void setClient(ClientCriteria client){
        this.client = client;
    }
    public List<ClientCriteria> getClients(){
        return this.clients;
    }

    public void setClients(List<ClientCriteria> clients){
        this.clients = clients;
    }
    public PropertyCriteria getRequestedProperty(){
        return this.requestedProperty;
    }

    public void setRequestedProperty(PropertyCriteria requestedProperty){
        this.requestedProperty = requestedProperty;
    }
    public List<PropertyCriteria> getRequestedPropertys(){
        return this.requestedPropertys;
    }

    public void setRequestedPropertys(List<PropertyCriteria> requestedPropertys){
        this.requestedPropertys = requestedPropertys;
    }
    public PropertyCriteria getAlternativeProperty(){
        return this.alternativeProperty;
    }

    public void setAlternativeProperty(PropertyCriteria alternativeProperty){
        this.alternativeProperty = alternativeProperty;
    }
    public List<PropertyCriteria> getAlternativePropertys(){
        return this.alternativePropertys;
    }

    public void setAlternativePropertys(List<PropertyCriteria> alternativePropertys){
        this.alternativePropertys = alternativePropertys;
    }
    public CollaboratorCriteria getReviewedBy(){
        return this.reviewedBy;
    }

    public void setReviewedBy(CollaboratorCriteria reviewedBy){
        this.reviewedBy = reviewedBy;
    }
    public List<CollaboratorCriteria> getReviewedBys(){
        return this.reviewedBys;
    }

    public void setReviewedBys(List<CollaboratorCriteria> reviewedBys){
        this.reviewedBys = reviewedBys;
    }
    public ReservationRequestStatusCriteria getReservationRequestStatus(){
        return this.reservationRequestStatus;
    }

    public void setReservationRequestStatus(ReservationRequestStatusCriteria reservationRequestStatus){
        this.reservationRequestStatus = reservationRequestStatus;
    }
    public List<ReservationRequestStatusCriteria> getReservationRequestStatuss(){
        return this.reservationRequestStatuss;
    }

    public void setReservationRequestStatuss(List<ReservationRequestStatusCriteria> reservationRequestStatuss){
        this.reservationRequestStatuss = reservationRequestStatuss;
    }
    public ReservationCriteria getReservation(){
        return this.reservation;
    }

    public void setReservation(ReservationCriteria reservation){
        this.reservation = reservation;
    }
    public List<ReservationCriteria> getReservations(){
        return this.reservations;
    }

    public void setReservations(List<ReservationCriteria> reservations){
        this.reservations = reservations;
    }
}
