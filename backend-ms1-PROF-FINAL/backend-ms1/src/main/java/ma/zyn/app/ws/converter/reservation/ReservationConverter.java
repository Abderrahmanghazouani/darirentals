package  ma.zyn.app.ws.converter.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.task.TaskConverter;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.ws.converter.task.TaskPriorityConverter;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.ws.converter.reservation.ReservationPlatformConverter;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.ws.converter.document.DocumentTypeConverter;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.ws.converter.reservation.ReservationRequestStatusConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.ws.converter.document.DocumentConverter;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.ws.converter.task.TaskStatusConverter;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.ws.converter.charge.ChargeConverter;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.ws.converter.task.TaskTypeConverter;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.ws.converter.reservation.ReservationStatusConverter;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.client.ClientConverter;
import ma.zyn.app.bean.core.client.Client;

import ma.zyn.app.bean.core.client.Client;
import ma.zyn.app.bean.core.property.Property;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.dto.reservation.ReservationDto;

@Component
public class ReservationConverter {

    @Autowired
    private TaskConverter taskConverter ;
    @Autowired
    private TaskPriorityConverter taskPriorityConverter ;
    @Autowired
    private ReservationRequestConverter reservationRequestConverter ;
    @Autowired
    private ReservationPlatformConverter reservationPlatformConverter ;
    @Autowired
    private DocumentTypeConverter documentTypeConverter ;
    @Autowired
    private ReservationRequestStatusConverter reservationRequestStatusConverter ;
    @Autowired
    private DocumentConverter documentConverter ;
    @Autowired
    private TaskStatusConverter taskStatusConverter ;
    @Autowired
    private ChargeConverter chargeConverter ;
    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private ServiceProviderConverter serviceProviderConverter ;
    @Autowired
    private TaskTypeConverter taskTypeConverter ;
    @Autowired
    private ReservationStatusConverter reservationStatusConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private ClientConverter clientConverter ;
    private boolean client;
    private boolean property;
    private boolean reservationPlatform;
    private boolean reservationStatus;
    private boolean documents;
    private boolean tasks;
    private boolean reservationRequests;

    public  ReservationConverter() {
        init(true);
    }

    public Reservation toItem(ReservationDto dto) {
        if (dto == null) {
            return null;
        } else {
            Reservation item = new Reservation();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getReference()))
                item.setReference(dto.getReference());
            if(StringUtil.isNotEmpty(dto.getAmount()))
                item.setAmount(dto.getAmount());
            if(StringUtil.isNotEmpty(dto.getPricePerNight()))
                item.setPricePerNight(dto.getPricePerNight());
            if(dto.getCheckInDate() != null)
                item.setCheckInDate(dto.getCheckInDate());
            if(dto.getCheckOutDate() != null)
                item.setCheckOutDate(dto.getCheckOutDate());
            if(dto.getClient() != null && dto.getClient().getId() != null){
                item.setClient(new Client());
                item.getClient().setId(dto.getClient().getId());
                item.getClient().setFullName(dto.getClient().getFullName());
            }

            if(dto.getProperty() != null && dto.getProperty().getId() != null){
                item.setProperty(new Property());
                item.getProperty().setId(dto.getProperty().getId());
                item.getProperty().setName(dto.getProperty().getName());
            }

            if(this.reservationPlatform && dto.getReservationPlatform()!=null)
                item.setReservationPlatform(reservationPlatformConverter.toItem(dto.getReservationPlatform())) ;

            if(this.reservationStatus && dto.getReservationStatus()!=null)
                item.setReservationStatus(reservationStatusConverter.toItem(dto.getReservationStatus())) ;


            if(this.documents && ListUtil.isNotEmpty(dto.getDocuments()))
                item.setDocuments(documentConverter.toItem(dto.getDocuments()));
            if(this.tasks && ListUtil.isNotEmpty(dto.getTasks()))
                item.setTasks(taskConverter.toItem(dto.getTasks()));
            if(this.reservationRequests && ListUtil.isNotEmpty(dto.getReservationRequests()))
                item.setReservationRequests(reservationRequestConverter.toItem(dto.getReservationRequests()));


            return item;
        }
    }


    public ReservationDto toDto(Reservation item) {
        if (item == null) {
            return null;
        } else {
            ReservationDto dto = new ReservationDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getReference()))
                dto.setReference(item.getReference());
            if(StringUtil.isNotEmpty(item.getAmount()))
                dto.setAmount(item.getAmount());
            if(StringUtil.isNotEmpty(item.getPricePerNight()))
                dto.setPricePerNight(item.getPricePerNight());
            if(item.getCheckInDate() != null)
                dto.setCheckInDate(item.getCheckInDate());
            if(item.getCheckOutDate() != null)
                dto.setCheckOutDate(item.getCheckOutDate());
            if(this.client && item.getClient()!=null) {
                dto.setClient(clientConverter.toDto(item.getClient())) ;

            }
            if(this.property && item.getProperty()!=null) {
                dto.setProperty(propertyConverter.toDto(item.getProperty())) ;

            }
            if(this.reservationPlatform && item.getReservationPlatform()!=null) {
                dto.setReservationPlatform(reservationPlatformConverter.toDto(item.getReservationPlatform())) ;

            }
            if(this.reservationStatus && item.getReservationStatus()!=null) {
                dto.setReservationStatus(reservationStatusConverter.toDto(item.getReservationStatus())) ;

            }
            if(this.documents && ListUtil.isNotEmpty(item.getDocuments())){
                documentConverter.init(true);
                documentConverter.setReservation(false);
                dto.setDocuments(documentConverter.toDto(item.getDocuments()));
                documentConverter.setReservation(true);

            }
            if(this.tasks && ListUtil.isNotEmpty(item.getTasks())){
                taskConverter.init(true);
                taskConverter.setReservation(false);
                dto.setTasks(taskConverter.toDto(item.getTasks()));
                taskConverter.setReservation(true);

            }
            if(this.reservationRequests && ListUtil.isNotEmpty(item.getReservationRequests())){
                reservationRequestConverter.init(true);
                reservationRequestConverter.setReservation(false);
                dto.setReservationRequests(reservationRequestConverter.toDto(item.getReservationRequests()));
                reservationRequestConverter.setReservation(true);

            }


            return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.documents = value;
        this.tasks = value;
        this.reservationRequests = value;
    }
    public void initObject(boolean value) {
        this.client = value;
        this.property = value;
        this.reservationPlatform = value;
        this.reservationStatus = value;
    }

    public List<Reservation> toItem(List<ReservationDto> dtos) {
        List<Reservation> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ReservationDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ReservationDto> toDto(List<Reservation> items) {
        List<ReservationDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Reservation item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ReservationDto dto, Reservation t) {
        BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getClient() == null  && dto.getClient() != null){
            t.setClient(new Client());
        }else if (t.getClient() != null  && dto.getClient() != null){
            t.setClient(null);
            t.setClient(new Client());
        }
        if(t.getProperty() == null  && dto.getProperty() != null){
            t.setProperty(new Property());
        }else if (t.getProperty() != null  && dto.getProperty() != null){
            t.setProperty(null);
            t.setProperty(new Property());
        }
        if(t.getReservationPlatform() == null  && dto.getReservationPlatform() != null){
            t.setReservationPlatform(new ReservationPlatform());
        }else if (t.getReservationPlatform() != null  && dto.getReservationPlatform() != null){
            t.setReservationPlatform(null);
            t.setReservationPlatform(new ReservationPlatform());
        }
        if(t.getReservationStatus() == null  && dto.getReservationStatus() != null){
            t.setReservationStatus(new ReservationStatus());
        }else if (t.getReservationStatus() != null  && dto.getReservationStatus() != null){
            t.setReservationStatus(null);
            t.setReservationStatus(new ReservationStatus());
        }
        if (dto.getClient() != null)
            clientConverter.copy(dto.getClient(), t.getClient());
        if (dto.getProperty() != null)
            propertyConverter.copy(dto.getProperty(), t.getProperty());
        if (dto.getReservationPlatform() != null)
            reservationPlatformConverter.copy(dto.getReservationPlatform(), t.getReservationPlatform());
        if (dto.getReservationStatus() != null)
            reservationStatusConverter.copy(dto.getReservationStatus(), t.getReservationStatus());
        if (dto.getDocuments() != null)
            t.setDocuments(documentConverter.copy(dto.getDocuments()));
        if (dto.getTasks() != null)
            t.setTasks(taskConverter.copy(dto.getTasks()));
        if (dto.getReservationRequests() != null)
            t.setReservationRequests(reservationRequestConverter.copy(dto.getReservationRequests()));
    }

    public List<Reservation> copy(List<ReservationDto> dtos) {
        List<Reservation> result = new ArrayList<>();
        if (dtos != null) {
            for (ReservationDto dto : dtos) {
                Reservation instance = new Reservation();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public TaskConverter getTaskConverter(){
        return this.taskConverter;
    }
    public void setTaskConverter(TaskConverter taskConverter ){
        this.taskConverter = taskConverter;
    }
    public TaskPriorityConverter getTaskPriorityConverter(){
        return this.taskPriorityConverter;
    }
    public void setTaskPriorityConverter(TaskPriorityConverter taskPriorityConverter ){
        this.taskPriorityConverter = taskPriorityConverter;
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
    public DocumentTypeConverter getDocumentTypeConverter(){
        return this.documentTypeConverter;
    }
    public void setDocumentTypeConverter(DocumentTypeConverter documentTypeConverter ){
        this.documentTypeConverter = documentTypeConverter;
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
    public TaskStatusConverter getTaskStatusConverter(){
        return this.taskStatusConverter;
    }
    public void setTaskStatusConverter(TaskStatusConverter taskStatusConverter ){
        this.taskStatusConverter = taskStatusConverter;
    }
    public ChargeConverter getChargeConverter(){
        return this.chargeConverter;
    }
    public void setChargeConverter(ChargeConverter chargeConverter ){
        this.chargeConverter = chargeConverter;
    }
    public CollaboratorConverter getCollaboratorConverter(){
        return this.collaboratorConverter;
    }
    public void setCollaboratorConverter(CollaboratorConverter collaboratorConverter ){
        this.collaboratorConverter = collaboratorConverter;
    }
    public ServiceProviderConverter getServiceProviderConverter(){
        return this.serviceProviderConverter;
    }
    public void setServiceProviderConverter(ServiceProviderConverter serviceProviderConverter ){
        this.serviceProviderConverter = serviceProviderConverter;
    }
    public TaskTypeConverter getTaskTypeConverter(){
        return this.taskTypeConverter;
    }
    public void setTaskTypeConverter(TaskTypeConverter taskTypeConverter ){
        this.taskTypeConverter = taskTypeConverter;
    }
    public ReservationStatusConverter getReservationStatusConverter(){
        return this.reservationStatusConverter;
    }
    public void setReservationStatusConverter(ReservationStatusConverter reservationStatusConverter ){
        this.reservationStatusConverter = reservationStatusConverter;
    }
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
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
    public boolean  isProperty(){
        return this.property;
    }
    public void  setProperty(boolean property){
        this.property = property;
    }
    public boolean  isReservationPlatform(){
        return this.reservationPlatform;
    }
    public void  setReservationPlatform(boolean reservationPlatform){
        this.reservationPlatform = reservationPlatform;
    }
    public boolean  isReservationStatus(){
        return this.reservationStatus;
    }
    public void  setReservationStatus(boolean reservationStatus){
        this.reservationStatus = reservationStatus;
    }
    public boolean  isDocuments(){
        return this.documents ;
    }
    public void  setDocuments(boolean documents ){
        this.documents  = documents ;
    }
    public boolean  isTasks(){
        return this.tasks ;
    }
    public void  setTasks(boolean tasks ){
        this.tasks  = tasks ;
    }
    public boolean  isReservationRequests(){
        return this.reservationRequests ;
    }
    public void  setReservationRequests(boolean reservationRequests ){
        this.reservationRequests  = reservationRequests ;
    }
}
