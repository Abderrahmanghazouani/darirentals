package  ma.zyn.app.ws.converter.property;

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
import ma.zyn.app.ws.converter.report.FinancialReportPropertyConverter;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.ws.converter.reservation.ReservationPlatformConverter;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.ws.converter.report.FinancialReportConverter;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.ws.converter.property.PropertyTypeConverter;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.ws.converter.property.CityConverter;
import ma.zyn.app.bean.core.property.City;
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
import ma.zyn.app.ws.converter.payment.PaymentConverter;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.charge.ChargeTypeConverter;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.ws.converter.task.TaskTypeConverter;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.ws.converter.reservation.ReservationStatusConverter;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.property.PropertyStatusConverter;
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.ws.converter.client.ClientConverter;
import ma.zyn.app.bean.core.client.Client;

import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.bean.core.enterprise.Enterprise;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.dto.property.PropertyDto;

@Component
public class PropertyConverter {

    @Autowired
    private TaskConverter taskConverter ;
    @Autowired
    private TaskPriorityConverter taskPriorityConverter ;
    @Autowired
    private FinancialReportPropertyConverter financialReportPropertyConverter ;
    @Autowired
    private ReservationRequestConverter reservationRequestConverter ;
    @Autowired
    private ReservationPlatformConverter reservationPlatformConverter ;
    @Autowired
    private FinancialReportConverter financialReportConverter ;
    @Autowired
    private PropertyTypeConverter propertyTypeConverter ;
    @Autowired
    private CityConverter cityConverter ;
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
    private PaymentConverter paymentConverter ;
    @Autowired
    private ServiceProviderConverter serviceProviderConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private ChargeTypeConverter chargeTypeConverter ;
    @Autowired
    private TaskTypeConverter taskTypeConverter ;
    @Autowired
    private ReservationStatusConverter reservationStatusConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private PropertyStatusConverter propertyStatusConverter ;
    @Autowired
    private ClientConverter clientConverter ;
    private boolean propertyType;
    private boolean propertyStatus;
    private boolean city;
    private boolean enterprise;
    private boolean reservations;
    private boolean charges;
    private boolean tasks;
    private boolean financialReportProperties;
    private boolean reservationRequests;
    private boolean alternativeRequests;

    public  PropertyConverter() {
        init(true);
    }

    public Property toItem(PropertyDto dto) {
        if (dto == null) {
            return null;
        } else {
        Property item = new Property();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getName()))
                item.setName(dto.getName());
            if(StringUtil.isNotEmpty(dto.getStreetNumber()))
                item.setStreetNumber(dto.getStreetNumber());
            if(StringUtil.isNotEmpty(dto.getStreetName()))
                item.setStreetName(dto.getStreetName());
            if(StringUtil.isNotEmpty(dto.getPostalCode()))
                item.setPostalCode(dto.getPostalCode());
            if(StringUtil.isNotEmpty(dto.getCapacity()))
                item.setCapacity(dto.getCapacity());
            if(StringUtil.isNotEmpty(dto.getPricePerNight()))
                item.setPricePerNight(dto.getPricePerNight());
            if(StringUtil.isNotEmpty(dto.getLatitude()))
                item.setLatitude(dto.getLatitude());
            if(StringUtil.isNotEmpty(dto.getLongitude()))
                item.setLongitude(dto.getLongitude());
            if(this.propertyType && dto.getPropertyType()!=null)
                item.setPropertyType(propertyTypeConverter.toItem(dto.getPropertyType())) ;

            if(this.propertyStatus && dto.getPropertyStatus()!=null)
                item.setPropertyStatus(propertyStatusConverter.toItem(dto.getPropertyStatus())) ;

            if(dto.getCity() != null && dto.getCity().getId() != null){
                item.setCity(new City());
                item.getCity().setId(dto.getCity().getId());
                item.getCity().setName(dto.getCity().getName());
            }

            if(dto.getEnterprise() != null && dto.getEnterprise().getId() != null){
                item.setEnterprise(new Enterprise());
                item.getEnterprise().setId(dto.getEnterprise().getId());
                item.getEnterprise().setName(dto.getEnterprise().getName());
            }


            if(this.reservations && ListUtil.isNotEmpty(dto.getReservations()))
                item.setReservations(reservationConverter.toItem(dto.getReservations()));
            if(this.charges && ListUtil.isNotEmpty(dto.getCharges()))
                item.setCharges(chargeConverter.toItem(dto.getCharges()));
            if(this.tasks && ListUtil.isNotEmpty(dto.getTasks()))
                item.setTasks(taskConverter.toItem(dto.getTasks()));
            if(this.financialReportProperties && ListUtil.isNotEmpty(dto.getFinancialReportProperties()))
                item.setFinancialReportProperties(financialReportPropertyConverter.toItem(dto.getFinancialReportProperties()));
            if(this.reservationRequests && ListUtil.isNotEmpty(dto.getReservationRequests()))
                item.setReservationRequests(reservationRequestConverter.toItem(dto.getReservationRequests()));
            if(this.alternativeRequests && ListUtil.isNotEmpty(dto.getAlternativeRequests()))
                item.setAlternativeRequests(reservationRequestConverter.toItem(dto.getAlternativeRequests()));


        return item;
        }
    }


    public PropertyDto toDto(Property item) {
        if (item == null) {
            return null;
        } else {
            PropertyDto dto = new PropertyDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getName()))
                dto.setName(item.getName());
            if(StringUtil.isNotEmpty(item.getStreetNumber()))
                dto.setStreetNumber(item.getStreetNumber());
            if(StringUtil.isNotEmpty(item.getStreetName()))
                dto.setStreetName(item.getStreetName());
            if(StringUtil.isNotEmpty(item.getPostalCode()))
                dto.setPostalCode(item.getPostalCode());
            if(StringUtil.isNotEmpty(item.getCapacity()))
                dto.setCapacity(item.getCapacity());
            if(StringUtil.isNotEmpty(item.getPricePerNight()))
                dto.setPricePerNight(item.getPricePerNight());
            if(StringUtil.isNotEmpty(item.getLatitude()))
                dto.setLatitude(item.getLatitude());
            if(StringUtil.isNotEmpty(item.getLongitude()))
                dto.setLongitude(item.getLongitude());
            if(this.propertyType && item.getPropertyType()!=null) {
                dto.setPropertyType(propertyTypeConverter.toDto(item.getPropertyType())) ;

            }
            if(this.propertyStatus && item.getPropertyStatus()!=null) {
                dto.setPropertyStatus(propertyStatusConverter.toDto(item.getPropertyStatus())) ;

            }
            if(this.city && item.getCity()!=null) {
                dto.setCity(cityConverter.toDto(item.getCity())) ;

            }
            if(this.enterprise && item.getEnterprise()!=null) {
                dto.setEnterprise(enterpriseConverter.toDto(item.getEnterprise())) ;

            }
        if(this.reservations && ListUtil.isNotEmpty(item.getReservations())){
            // Casse un autre chemin du meme cycle (voir les blocs reservationRequests/
            // alternativeRequests plus bas) : Property.reservations -> Reservation.client ->
            // Client.reservations -> Reservation.reservationRequests -> requestedProperty ->
            // Property -> ... Le client de chaque reservation reste affiche (this.client non
            // touche), seule la re-expansion de reservationRequests sur CES reservations est
            // desactivee (redondante a cette profondeur).
            boolean savedReservationRequests = reservationConverter.isReservationRequests();
            reservationConverter.init(true);
            reservationConverter.setProperty(false);
            reservationConverter.setReservationRequests(false);
            dto.setReservations(reservationConverter.toDto(item.getReservations()));
            reservationConverter.setReservationRequests(savedReservationRequests);
            reservationConverter.setProperty(true);

        }
        if(this.charges && ListUtil.isNotEmpty(item.getCharges())){
            chargeConverter.init(true);
            chargeConverter.setProperty(false);
            dto.setCharges(chargeConverter.toDto(item.getCharges()));
            chargeConverter.setProperty(true);

        }
        if(this.tasks && ListUtil.isNotEmpty(item.getTasks())){
            taskConverter.init(true);
            taskConverter.setProperty(false);
            dto.setTasks(taskConverter.toDto(item.getTasks()));
            taskConverter.setProperty(true);

        }
        if(this.financialReportProperties && ListUtil.isNotEmpty(item.getFinancialReportProperties())){
            financialReportPropertyConverter.init(true);
            financialReportPropertyConverter.setProperty(false);
            dto.setFinancialReportProperties(financialReportPropertyConverter.toDto(item.getFinancialReportProperties()));
            financialReportPropertyConverter.setProperty(true);

        }
        if(this.reservationRequests && ListUtil.isNotEmpty(item.getReservationRequests())){
            reservationRequestConverter.init(true);
            reservationRequestConverter.setRequestedProperty(false);
            // Casse le cycle Property -> reservationRequests -> client -> Client.reservationRequests
            // -> requestedProperty -> Property -> ... (StackOverflowError) : reservationRequestConverter.init(true)
            // remet requestedProperty a true, mais rien ne protegeait le saut suivant via le client.
            // Le client reste affiche (this.client n'est pas touche ici), seule SA PROPRE liste de
            // demandes est desactivee - elle serait de toute facon redondante avec celle-ci.
            boolean savedClientReservationRequests = clientConverter.isReservationRequests();
            clientConverter.setReservationRequests(false);
            dto.setReservationRequests(reservationRequestConverter.toDto(item.getReservationRequests()));
            clientConverter.setReservationRequests(savedClientReservationRequests);
            reservationRequestConverter.setRequestedProperty(true);

        }
        if(this.alternativeRequests && ListUtil.isNotEmpty(item.getAlternativeRequests())){
            reservationRequestConverter.init(true);
            reservationRequestConverter.setAlternativeProperty(false);
            // Meme protection que ci-dessus, pour le meme cycle via alternativeProperty.
            boolean savedClientReservationRequests = clientConverter.isReservationRequests();
            clientConverter.setReservationRequests(false);
            dto.setAlternativeRequests(reservationRequestConverter.toDto(item.getAlternativeRequests()));
            clientConverter.setReservationRequests(savedClientReservationRequests);
            reservationRequestConverter.setAlternativeProperty(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.reservations = value;
        this.charges = value;
        this.tasks = value;
        this.financialReportProperties = value;
        this.reservationRequests = value;
        this.alternativeRequests = value;
    }
    public void initObject(boolean value) {
        this.propertyType = value;
        this.propertyStatus = value;
        this.city = value;
        this.enterprise = value;
    }
	
    public List<Property> toItem(List<PropertyDto> dtos) {
        List<Property> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (PropertyDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<PropertyDto> toDto(List<Property> items) {
        List<PropertyDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Property item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(PropertyDto dto, Property t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getPropertyType() == null  && dto.getPropertyType() != null){
            t.setPropertyType(new PropertyType());
        }else if (t.getPropertyType() != null  && dto.getPropertyType() != null){
            t.setPropertyType(null);
            t.setPropertyType(new PropertyType());
        }
        if(t.getPropertyStatus() == null  && dto.getPropertyStatus() != null){
            t.setPropertyStatus(new PropertyStatus());
        }else if (t.getPropertyStatus() != null  && dto.getPropertyStatus() != null){
            t.setPropertyStatus(null);
            t.setPropertyStatus(new PropertyStatus());
        }
        if(t.getCity() == null  && dto.getCity() != null){
            t.setCity(new City());
        }else if (t.getCity() != null  && dto.getCity() != null){
            t.setCity(null);
            t.setCity(new City());
        }
        if(t.getEnterprise() == null  && dto.getEnterprise() != null){
            t.setEnterprise(new Enterprise());
        }else if (t.getEnterprise() != null  && dto.getEnterprise() != null){
            t.setEnterprise(null);
            t.setEnterprise(new Enterprise());
        }
        if (dto.getPropertyType() != null)
        propertyTypeConverter.copy(dto.getPropertyType(), t.getPropertyType());
        if (dto.getPropertyStatus() != null)
        propertyStatusConverter.copy(dto.getPropertyStatus(), t.getPropertyStatus());
        if (dto.getCity() != null)
        cityConverter.copy(dto.getCity(), t.getCity());
        if (dto.getEnterprise() != null)
        enterpriseConverter.copy(dto.getEnterprise(), t.getEnterprise());
        if (dto.getReservations() != null)
            t.setReservations(reservationConverter.copy(dto.getReservations()));
        if (dto.getCharges() != null)
            t.setCharges(chargeConverter.copy(dto.getCharges()));
        if (dto.getTasks() != null)
            t.setTasks(taskConverter.copy(dto.getTasks()));
        if (dto.getFinancialReportProperties() != null)
            t.setFinancialReportProperties(financialReportPropertyConverter.copy(dto.getFinancialReportProperties()));
        if (dto.getReservationRequests() != null)
            t.setReservationRequests(reservationRequestConverter.copy(dto.getReservationRequests()));
        if (dto.getAlternativeRequests() != null)
            t.setAlternativeRequests(reservationRequestConverter.copy(dto.getAlternativeRequests()));
    }

    public List<Property> copy(List<PropertyDto> dtos) {
        List<Property> result = new ArrayList<>();
        if (dtos != null) {
            for (PropertyDto dto : dtos) {
                Property instance = new Property();
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
    public FinancialReportPropertyConverter getFinancialReportPropertyConverter(){
        return this.financialReportPropertyConverter;
    }
    public void setFinancialReportPropertyConverter(FinancialReportPropertyConverter financialReportPropertyConverter ){
        this.financialReportPropertyConverter = financialReportPropertyConverter;
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
    public FinancialReportConverter getFinancialReportConverter(){
        return this.financialReportConverter;
    }
    public void setFinancialReportConverter(FinancialReportConverter financialReportConverter ){
        this.financialReportConverter = financialReportConverter;
    }
    public PropertyTypeConverter getPropertyTypeConverter(){
        return this.propertyTypeConverter;
    }
    public void setPropertyTypeConverter(PropertyTypeConverter propertyTypeConverter ){
        this.propertyTypeConverter = propertyTypeConverter;
    }
    public CityConverter getCityConverter(){
        return this.cityConverter;
    }
    public void setCityConverter(CityConverter cityConverter ){
        this.cityConverter = cityConverter;
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
    public PaymentConverter getPaymentConverter(){
        return this.paymentConverter;
    }
    public void setPaymentConverter(PaymentConverter paymentConverter ){
        this.paymentConverter = paymentConverter;
    }
    public ServiceProviderConverter getServiceProviderConverter(){
        return this.serviceProviderConverter;
    }
    public void setServiceProviderConverter(ServiceProviderConverter serviceProviderConverter ){
        this.serviceProviderConverter = serviceProviderConverter;
    }
    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public ChargeTypeConverter getChargeTypeConverter(){
        return this.chargeTypeConverter;
    }
    public void setChargeTypeConverter(ChargeTypeConverter chargeTypeConverter ){
        this.chargeTypeConverter = chargeTypeConverter;
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
    public EnterpriseConverter getEnterpriseConverter(){
        return this.enterpriseConverter;
    }
    public void setEnterpriseConverter(EnterpriseConverter enterpriseConverter ){
        this.enterpriseConverter = enterpriseConverter;
    }
    public PropertyStatusConverter getPropertyStatusConverter(){
        return this.propertyStatusConverter;
    }
    public void setPropertyStatusConverter(PropertyStatusConverter propertyStatusConverter ){
        this.propertyStatusConverter = propertyStatusConverter;
    }
    public ClientConverter getClientConverter(){
        return this.clientConverter;
    }
    public void setClientConverter(ClientConverter clientConverter ){
        this.clientConverter = clientConverter;
    }
    public boolean  isPropertyType(){
        return this.propertyType;
    }
    public void  setPropertyType(boolean propertyType){
        this.propertyType = propertyType;
    }
    public boolean  isPropertyStatus(){
        return this.propertyStatus;
    }
    public void  setPropertyStatus(boolean propertyStatus){
        this.propertyStatus = propertyStatus;
    }
    public boolean  isCity(){
        return this.city;
    }
    public void  setCity(boolean city){
        this.city = city;
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
    public boolean  isCharges(){
        return this.charges ;
    }
    public void  setCharges(boolean charges ){
        this.charges  = charges ;
    }
    public boolean  isTasks(){
        return this.tasks ;
    }
    public void  setTasks(boolean tasks ){
        this.tasks  = tasks ;
    }
    public boolean  isFinancialReportProperties(){
        return this.financialReportProperties ;
    }
    public void  setFinancialReportProperties(boolean financialReportProperties ){
        this.financialReportProperties  = financialReportProperties ;
    }
    public boolean  isReservationRequests(){
        return this.reservationRequests ;
    }
    public void  setReservationRequests(boolean reservationRequests ){
        this.reservationRequests  = reservationRequests ;
    }
    public boolean  isAlternativeRequests(){
        return this.alternativeRequests ;
    }
    public void  setAlternativeRequests(boolean alternativeRequests ){
        this.alternativeRequests  = alternativeRequests ;
    }
}
