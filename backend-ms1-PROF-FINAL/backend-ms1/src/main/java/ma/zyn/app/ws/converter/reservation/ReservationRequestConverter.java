package  ma.zyn.app.ws.converter.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.reservation.ReservationRequestStatusConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.ws.converter.client.ClientConverter;
import ma.zyn.app.bean.core.client.Client;

import ma.zyn.app.bean.core.client.Client;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.reservation.Reservation;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;

@Component
public class ReservationRequestConverter {

    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private ReservationRequestStatusConverter reservationRequestStatusConverter ;
    @Autowired
    private ClientConverter clientConverter ;
    private boolean client;
    private boolean requestedProperty;
    private boolean alternativeProperty;
    private boolean reviewedBy;
    private boolean reservationRequestStatus;
    private boolean reservation;

    public  ReservationRequestConverter() {
        initObject(true);
    }

    public ReservationRequest toItem(ReservationRequestDto dto) {
        if (dto == null) {
            return null;
        } else {
        ReservationRequest item = new ReservationRequest();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getClientNote()))
                item.setClientNote(dto.getClientNote());
            if(StringUtil.isNotEmpty(dto.getStaffNote()))
                item.setStaffNote(dto.getStaffNote());
            if(dto.getClient() != null && dto.getClient().getId() != null){
                item.setClient(new Client());
                item.getClient().setId(dto.getClient().getId());
                item.getClient().setFullName(dto.getClient().getFullName());
            }

            if(dto.getRequestedProperty() != null && dto.getRequestedProperty().getId() != null){
                item.setRequestedProperty(new Property());
                item.getRequestedProperty().setId(dto.getRequestedProperty().getId());
                item.getRequestedProperty().setName(dto.getRequestedProperty().getName());
            }

            if(dto.getAlternativeProperty() != null && dto.getAlternativeProperty().getId() != null){
                item.setAlternativeProperty(new Property());
                item.getAlternativeProperty().setId(dto.getAlternativeProperty().getId());
                item.getAlternativeProperty().setName(dto.getAlternativeProperty().getName());
            }

            if(dto.getReviewedBy() != null && dto.getReviewedBy().getId() != null){
                item.setReviewedBy(new Collaborator());
                item.getReviewedBy().setId(dto.getReviewedBy().getId());
                item.getReviewedBy().setName(dto.getReviewedBy().getName());
            }

            if(this.reservationRequestStatus && dto.getReservationRequestStatus()!=null)
                item.setReservationRequestStatus(reservationRequestStatusConverter.toItem(dto.getReservationRequestStatus())) ;

            if(dto.getReservation() != null && dto.getReservation().getId() != null){
                item.setReservation(new Reservation());
                item.getReservation().setId(dto.getReservation().getId());
                item.getReservation().setReference(dto.getReservation().getReference());
            }




        return item;
        }
    }


    public ReservationRequestDto toDto(ReservationRequest item) {
        if (item == null) {
            return null;
        } else {
            ReservationRequestDto dto = new ReservationRequestDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getClientNote()))
                dto.setClientNote(item.getClientNote());
            if(StringUtil.isNotEmpty(item.getStaffNote()))
                dto.setStaffNote(item.getStaffNote());
            if(this.client && item.getClient()!=null) {
                // Le client affiche sur une demande de reservation ne doit pas re-lister ses
                // propres reservations/demandes : source du cycle ReservationRequest <-> Client
                // <-> Property <-> Reservation (voir les memes garde-fous dans PropertyConverter,
                // ClientConverter et ReservationConverter).
                boolean savedClientReservations = clientConverter.isReservations();
                boolean savedClientReservationRequests = clientConverter.isReservationRequests();
                clientConverter.setReservations(false);
                clientConverter.setReservationRequests(false);
                dto.setClient(clientConverter.toDto(item.getClient())) ;
                clientConverter.setReservations(savedClientReservations);
                clientConverter.setReservationRequests(savedClientReservationRequests);

            }
            if(this.requestedProperty && item.getRequestedProperty()!=null) {
                // Meme protection que pour "client" ci-dessus.
                boolean savedPropertyReservations = propertyConverter.isReservations();
                boolean savedPropertyReservationRequests = propertyConverter.isReservationRequests();
                boolean savedPropertyAlternativeRequests = propertyConverter.isAlternativeRequests();
                propertyConverter.setReservations(false);
                propertyConverter.setReservationRequests(false);
                propertyConverter.setAlternativeRequests(false);
                dto.setRequestedProperty(propertyConverter.toDto(item.getRequestedProperty())) ;
                propertyConverter.setReservations(savedPropertyReservations);
                propertyConverter.setReservationRequests(savedPropertyReservationRequests);
                propertyConverter.setAlternativeRequests(savedPropertyAlternativeRequests);

            }
            if(this.alternativeProperty && item.getAlternativeProperty()!=null) {
                // Meme protection que pour "requestedProperty" ci-dessus.
                boolean savedPropertyReservations = propertyConverter.isReservations();
                boolean savedPropertyReservationRequests = propertyConverter.isReservationRequests();
                boolean savedPropertyAlternativeRequests = propertyConverter.isAlternativeRequests();
                propertyConverter.setReservations(false);
                propertyConverter.setReservationRequests(false);
                propertyConverter.setAlternativeRequests(false);
                dto.setAlternativeProperty(propertyConverter.toDto(item.getAlternativeProperty())) ;
                propertyConverter.setReservations(savedPropertyReservations);
                propertyConverter.setReservationRequests(savedPropertyReservationRequests);
                propertyConverter.setAlternativeRequests(savedPropertyAlternativeRequests);

            }
            if(this.reviewedBy && item.getReviewedBy()!=null) {
                dto.setReviewedBy(collaboratorConverter.toDto(item.getReviewedBy())) ;

            }
            if(this.reservationRequestStatus && item.getReservationRequestStatus()!=null) {
                dto.setReservationRequestStatus(reservationRequestStatusConverter.toDto(item.getReservationRequestStatus())) ;

            }
            if(this.reservation && item.getReservation()!=null) {
                // Meme protection : la reservation affichee ne doit pas re-lister ses propres
                // client/property/reservationRequests (source du meme cycle).
                boolean savedReservationClient = reservationConverter.isClient();
                boolean savedReservationProperty = reservationConverter.isProperty();
                boolean savedReservationReservationRequests = reservationConverter.isReservationRequests();
                reservationConverter.setClient(false);
                reservationConverter.setProperty(false);
                reservationConverter.setReservationRequests(false);
                dto.setReservation(reservationConverter.toDto(item.getReservation())) ;
                reservationConverter.setClient(savedReservationClient);
                reservationConverter.setProperty(savedReservationProperty);
                reservationConverter.setReservationRequests(savedReservationReservationRequests);

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.client = value;
        this.requestedProperty = value;
        this.alternativeProperty = value;
        this.reviewedBy = value;
        this.reservationRequestStatus = value;
        this.reservation = value;
    }
	
    public List<ReservationRequest> toItem(List<ReservationRequestDto> dtos) {
        List<ReservationRequest> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ReservationRequestDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ReservationRequestDto> toDto(List<ReservationRequest> items) {
        List<ReservationRequestDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (ReservationRequest item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ReservationRequestDto dto, ReservationRequest t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getClient() == null  && dto.getClient() != null){
            t.setClient(new Client());
        }else if (t.getClient() != null  && dto.getClient() != null){
            t.setClient(null);
            t.setClient(new Client());
        }
        if(t.getRequestedProperty() == null  && dto.getRequestedProperty() != null){
            t.setRequestedProperty(new Property());
        }else if (t.getRequestedProperty() != null  && dto.getRequestedProperty() != null){
            t.setRequestedProperty(null);
            t.setRequestedProperty(new Property());
        }
        if(t.getAlternativeProperty() == null  && dto.getAlternativeProperty() != null){
            t.setAlternativeProperty(new Property());
        }else if (t.getAlternativeProperty() != null  && dto.getAlternativeProperty() != null){
            t.setAlternativeProperty(null);
            t.setAlternativeProperty(new Property());
        }
        if(t.getReviewedBy() == null  && dto.getReviewedBy() != null){
            t.setReviewedBy(new Collaborator());
        }else if (t.getReviewedBy() != null  && dto.getReviewedBy() != null){
            t.setReviewedBy(null);
            t.setReviewedBy(new Collaborator());
        }
        if(t.getReservationRequestStatus() == null  && dto.getReservationRequestStatus() != null){
            t.setReservationRequestStatus(new ReservationRequestStatus());
        }else if (t.getReservationRequestStatus() != null  && dto.getReservationRequestStatus() != null){
            t.setReservationRequestStatus(null);
            t.setReservationRequestStatus(new ReservationRequestStatus());
        }
        if(t.getReservation() == null  && dto.getReservation() != null){
            t.setReservation(new Reservation());
        }else if (t.getReservation() != null  && dto.getReservation() != null){
            t.setReservation(null);
            t.setReservation(new Reservation());
        }
        if (dto.getClient() != null)
        clientConverter.copy(dto.getClient(), t.getClient());
        if (dto.getRequestedProperty() != null)
        propertyConverter.copy(dto.getRequestedProperty(), t.getRequestedProperty());
        if (dto.getAlternativeProperty() != null)
        propertyConverter.copy(dto.getAlternativeProperty(), t.getAlternativeProperty());
        if (dto.getReviewedBy() != null)
        collaboratorConverter.copy(dto.getReviewedBy(), t.getReviewedBy());
        if (dto.getReservationRequestStatus() != null)
        reservationRequestStatusConverter.copy(dto.getReservationRequestStatus(), t.getReservationRequestStatus());
        if (dto.getReservation() != null)
        reservationConverter.copy(dto.getReservation(), t.getReservation());
    }

    public List<ReservationRequest> copy(List<ReservationRequestDto> dtos) {
        List<ReservationRequest> result = new ArrayList<>();
        if (dtos != null) {
            for (ReservationRequestDto dto : dtos) {
                ReservationRequest instance = new ReservationRequest();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public CollaboratorConverter getCollaboratorConverter(){
        return this.collaboratorConverter;
    }
    public void setCollaboratorConverter(CollaboratorConverter collaboratorConverter ){
        this.collaboratorConverter = collaboratorConverter;
    }
    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
    }
    public ReservationRequestStatusConverter getReservationRequestStatusConverter(){
        return this.reservationRequestStatusConverter;
    }
    public void setReservationRequestStatusConverter(ReservationRequestStatusConverter reservationRequestStatusConverter ){
        this.reservationRequestStatusConverter = reservationRequestStatusConverter;
    }
    public ClientConverter getClientConverter(){
        return this.clientConverter;
    }
    public void setClientConverter(ClientConverter clientConverter ){
        this.clientConverter = clientConverter;
    }
    public boolean  isClient(){
        return this.client;
    }
    public void  setClient(boolean client){
        this.client = client;
    }
    public boolean  isRequestedProperty(){
        return this.requestedProperty;
    }
    public void  setRequestedProperty(boolean requestedProperty){
        this.requestedProperty = requestedProperty;
    }
    public boolean  isAlternativeProperty(){
        return this.alternativeProperty;
    }
    public void  setAlternativeProperty(boolean alternativeProperty){
        this.alternativeProperty = alternativeProperty;
    }
    public boolean  isReviewedBy(){
        return this.reviewedBy;
    }
    public void  setReviewedBy(boolean reviewedBy){
        this.reviewedBy = reviewedBy;
    }
    public boolean  isReservationRequestStatus(){
        return this.reservationRequestStatus;
    }
    public void  setReservationRequestStatus(boolean reservationRequestStatus){
        this.reservationRequestStatus = reservationRequestStatus;
    }
    public boolean  isReservation(){
        return this.reservation;
    }
    public void  setReservation(boolean reservation){
        this.reservation = reservation;
    }
}
