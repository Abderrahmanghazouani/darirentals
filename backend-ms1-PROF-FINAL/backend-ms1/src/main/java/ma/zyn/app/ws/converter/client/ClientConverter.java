package  ma.zyn.app.ws.converter.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.task.TaskConverter;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.ws.converter.reservation.ReservationPlatformConverter;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.ws.converter.reservation.ReservationStatusConverter;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.reservation.ReservationRequestStatusConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.ws.converter.document.DocumentConverter;
import ma.zyn.app.bean.core.document.Document;

import ma.zyn.app.bean.core.enterprise.Enterprise;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.client.Client;
import ma.zyn.app.ws.dto.client.ClientDto;

@Component
public class ClientConverter {

    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private TaskConverter taskConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private ReservationRequestConverter reservationRequestConverter ;
    @Autowired
    private ReservationPlatformConverter reservationPlatformConverter ;
    @Autowired
    private ReservationStatusConverter reservationStatusConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private ReservationRequestStatusConverter reservationRequestStatusConverter ;
    @Autowired
    private DocumentConverter documentConverter ;
    private boolean enterprise;
    private boolean reservations;
    private boolean reservationRequests;

    public  ClientConverter() {
        init(true);
    }

    public Client toItem(ClientDto dto) {
        if (dto == null) {
            return null;
        } else {
        Client item = new Client();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getFullName()))
                item.setFullName(dto.getFullName());
            if(StringUtil.isNotEmpty(dto.getPhone()))
                item.setPhone(dto.getPhone());
            if(StringUtil.isNotEmpty(dto.getNationality()))
                item.setNationality(dto.getNationality());
            if(StringUtil.isNotEmpty(dto.getEmail()))
                item.setEmail(dto.getEmail());
            item.setEnabled(dto.getEnabled());
            item.setCredentialsNonExpired(dto.getCredentialsNonExpired());
            item.setAccountNonExpired(dto.getAccountNonExpired());
            if(StringUtil.isNotEmpty(dto.getUsername()))
                item.setUsername(dto.getUsername());
            item.setPasswordChanged(dto.getPasswordChanged());
            item.setAccountNonLocked(dto.getAccountNonLocked());
            if(StringUtil.isNotEmpty(dto.getPassword()))
                item.setPassword(dto.getPassword());
            if(dto.getEnterprise() != null && dto.getEnterprise().getId() != null){
                item.setEnterprise(new Enterprise());
                item.getEnterprise().setId(dto.getEnterprise().getId());
                item.getEnterprise().setName(dto.getEnterprise().getName());
            }


            if(this.reservations && ListUtil.isNotEmpty(dto.getReservations()))
                item.setReservations(reservationConverter.toItem(dto.getReservations()));
            if(this.reservationRequests && ListUtil.isNotEmpty(dto.getReservationRequests()))
                item.setReservationRequests(reservationRequestConverter.toItem(dto.getReservationRequests()));

            //item.setRoles(dto.getRoles());

        return item;
        }
    }


    public ClientDto toDto(Client item) {
        if (item == null) {
            return null;
        } else {
            ClientDto dto = new ClientDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getFullName()))
                dto.setFullName(item.getFullName());
            if(StringUtil.isNotEmpty(item.getPhone()))
                dto.setPhone(item.getPhone());
            if(StringUtil.isNotEmpty(item.getNationality()))
                dto.setNationality(item.getNationality());
            if(StringUtil.isNotEmpty(item.getEmail()))
                dto.setEmail(item.getEmail());
            if(StringUtil.isNotEmpty(item.getEnabled()))
                dto.setEnabled(item.getEnabled());
            if(StringUtil.isNotEmpty(item.getCredentialsNonExpired()))
                dto.setCredentialsNonExpired(item.getCredentialsNonExpired());
            if(StringUtil.isNotEmpty(item.getAccountNonExpired()))
                dto.setAccountNonExpired(item.getAccountNonExpired());
            if(StringUtil.isNotEmpty(item.getUsername()))
                dto.setUsername(item.getUsername());
            if(StringUtil.isNotEmpty(item.getPasswordChanged()))
                dto.setPasswordChanged(item.getPasswordChanged());
            if(StringUtil.isNotEmpty(item.getAccountNonLocked()))
                dto.setAccountNonLocked(item.getAccountNonLocked());
            if(this.enterprise && item.getEnterprise()!=null) {
                dto.setEnterprise(enterpriseConverter.toDto(item.getEnterprise())) ;

            }
        if(this.reservations && ListUtil.isNotEmpty(item.getReservations())){
            reservationConverter.init(true);
            reservationConverter.setClient(false);
            dto.setReservations(reservationConverter.toDto(item.getReservations()));
            reservationConverter.setClient(true);

        }
        if(this.reservationRequests && ListUtil.isNotEmpty(item.getReservationRequests())){
            reservationRequestConverter.init(true);
            reservationRequestConverter.setClient(false);
            dto.setReservationRequests(reservationRequestConverter.toDto(item.getReservationRequests()));
            reservationRequestConverter.setClient(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.reservations = value;
        this.reservationRequests = value;
    }
    public void initObject(boolean value) {
        this.enterprise = value;
    }
	
    public List<Client> toItem(List<ClientDto> dtos) {
        List<Client> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ClientDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ClientDto> toDto(List<Client> items) {
        List<ClientDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Client item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ClientDto dto, Client t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getEnterprise() == null  && dto.getEnterprise() != null){
            t.setEnterprise(new Enterprise());
        }else if (t.getEnterprise() != null  && dto.getEnterprise() != null){
            t.setEnterprise(null);
            t.setEnterprise(new Enterprise());
        }
        if (dto.getEnterprise() != null)
        enterpriseConverter.copy(dto.getEnterprise(), t.getEnterprise());
        if (dto.getReservations() != null)
            t.setReservations(reservationConverter.copy(dto.getReservations()));
        if (dto.getReservationRequests() != null)
            t.setReservationRequests(reservationRequestConverter.copy(dto.getReservationRequests()));
    }

    public List<Client> copy(List<ClientDto> dtos) {
        List<Client> result = new ArrayList<>();
        if (dtos != null) {
            for (ClientDto dto : dtos) {
                Client instance = new Client();
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
    public TaskConverter getTaskConverter(){
        return this.taskConverter;
    }
    public void setTaskConverter(TaskConverter taskConverter ){
        this.taskConverter = taskConverter;
    }
    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public ReservationRequestConverter getReservationRequestConverter(){
        return this.reservationRequestConverter;
    }
    public void setReservationRequestConverter(ReservationRequestConverter reservationRequestConverter ){
        this.reservationRequestConverter = reservationRequestConverter;
    }
    public ReservationPlatformConverter getReservationPlatformConverter(){
        return this.reservationPlatformConverter;
    }
    public void setReservationPlatformConverter(ReservationPlatformConverter reservationPlatformConverter ){
        this.reservationPlatformConverter = reservationPlatformConverter;
    }
    public ReservationStatusConverter getReservationStatusConverter(){
        return this.reservationStatusConverter;
    }
    public void setReservationStatusConverter(ReservationStatusConverter reservationStatusConverter ){
        this.reservationStatusConverter = reservationStatusConverter;
    }
    public EnterpriseConverter getEnterpriseConverter(){
        return this.enterpriseConverter;
    }
    public void setEnterpriseConverter(EnterpriseConverter enterpriseConverter ){
        this.enterpriseConverter = enterpriseConverter;
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
    public DocumentConverter getDocumentConverter(){
        return this.documentConverter;
    }
    public void setDocumentConverter(DocumentConverter documentConverter ){
        this.documentConverter = documentConverter;
    }
    public boolean  isEnterprise(){
        return this.enterprise;
    }
    public void  setEnterprise(boolean enterprise){
        this.enterprise = enterprise;
    }
    public boolean  isReservations(){
        return this.reservations ;
    }
    public void  setReservations(boolean reservations ){
        this.reservations  = reservations ;
    }
    public boolean  isReservationRequests(){
        return this.reservationRequests ;
    }
    public void  setReservationRequests(boolean reservationRequests ){
        this.reservationRequests  = reservationRequests ;
    }
}
