package  ma.zyn.app.ws.converter.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.ai.AiUsageLogConverter;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.ws.converter.task.TaskConverter;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.ws.converter.task.TaskPriorityConverter;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.ws.converter.enterprise.EnterpriseMembershipConverter;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.ws.converter.auth.CollaboratorRoleConverter;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.ws.converter.reservation.ReservationRequestStatusConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.ws.converter.document.DocumentConverter;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.ws.converter.task.TaskStatusConverter;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.ws.converter.auth.CollaboratorPermissionOverrideConverter;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.ws.converter.ai.AiUsageTypeConverter;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.currency.CurrencyConverter;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.ws.converter.task.TaskTypeConverter;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.client.ClientConverter;
import ma.zyn.app.bean.core.client.Client;

import ma.zyn.app.bean.core.currency.Currency;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.dto.auth.CollaboratorDto;

@Component
public class CollaboratorConverter {

    @Autowired
    private AiUsageLogConverter aiUsageLogConverter ;
    @Autowired
    private TaskConverter taskConverter ;
    @Autowired
    private TaskPriorityConverter taskPriorityConverter ;
    @Autowired
    private ReservationRequestConverter reservationRequestConverter ;
    @Autowired
    private EnterpriseMembershipConverter enterpriseMembershipConverter ;
    @Autowired
    private CollaboratorRoleConverter collaboratorRoleConverter ;
    @Autowired
    private ReservationRequestStatusConverter reservationRequestStatusConverter ;
    @Autowired
    private DocumentConverter documentConverter ;
    @Autowired
    private TaskStatusConverter taskStatusConverter ;
    @Autowired
    private CollaboratorPermissionOverrideConverter collaboratorPermissionOverrideConverter ;
    @Autowired
    private ServiceProviderConverter serviceProviderConverter ;
    @Autowired
    private AiUsageTypeConverter aiUsageTypeConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private CurrencyConverter currencyConverter ;
    @Autowired
    private TaskTypeConverter taskTypeConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private ClientConverter clientConverter ;
    private boolean displayCurrency;
    private boolean enterpriseMemberships;
    private boolean aiUsageLogs;
    private boolean tasks;
    private boolean reservationRequests;

    public  CollaboratorConverter() {
        init(true);
    }

    public Collaborator toItem(CollaboratorDto dto) {
        if (dto == null) {
            return null;
        } else {
        Collaborator item = new Collaborator();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getName()))
                item.setName(dto.getName());
            if(StringUtil.isNotEmpty(dto.getPhone()))
                item.setPhone(dto.getPhone());
            if(dto.getIsActive() != null)
                item.setIsActive(dto.getIsActive());
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
            if(dto.getDisplayCurrency() != null && dto.getDisplayCurrency().getId() != null){
                item.setDisplayCurrency(new Currency());
                item.getDisplayCurrency().setId(dto.getDisplayCurrency().getId());
                item.getDisplayCurrency().setLabel(dto.getDisplayCurrency().getLabel());
            }


            if(this.enterpriseMemberships && ListUtil.isNotEmpty(dto.getEnterpriseMemberships()))
                item.setEnterpriseMemberships(enterpriseMembershipConverter.toItem(dto.getEnterpriseMemberships()));
            if(this.aiUsageLogs && ListUtil.isNotEmpty(dto.getAiUsageLogs()))
                item.setAiUsageLogs(aiUsageLogConverter.toItem(dto.getAiUsageLogs()));
            if(this.tasks && ListUtil.isNotEmpty(dto.getTasks()))
                item.setTasks(taskConverter.toItem(dto.getTasks()));
            if(this.reservationRequests && ListUtil.isNotEmpty(dto.getReservationRequests()))
                item.setReservationRequests(reservationRequestConverter.toItem(dto.getReservationRequests()));

            //item.setRoles(dto.getRoles());

        return item;
        }
    }


    public CollaboratorDto toDto(Collaborator item) {
        if (item == null) {
            return null;
        } else {
            CollaboratorDto dto = new CollaboratorDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getName()))
                dto.setName(item.getName());
            if(StringUtil.isNotEmpty(item.getPhone()))
                dto.setPhone(item.getPhone());
                dto.setIsActive(item.getIsActive());
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
            if(this.displayCurrency && item.getDisplayCurrency()!=null) {
                dto.setDisplayCurrency(currencyConverter.toDto(item.getDisplayCurrency())) ;

            }
        if(this.enterpriseMemberships && ListUtil.isNotEmpty(item.getEnterpriseMemberships())){
            enterpriseMembershipConverter.init(true);
            enterpriseMembershipConverter.initObject(true);
            enterpriseMembershipConverter.setCollaborator(false);
            dto.setEnterpriseMemberships(enterpriseMembershipConverter.toDto(item.getEnterpriseMemberships()));
            enterpriseMembershipConverter.setCollaborator(true);

        }
        if(this.aiUsageLogs && ListUtil.isNotEmpty(item.getAiUsageLogs())){
            aiUsageLogConverter.init(true);
            aiUsageLogConverter.setCollaborator(false);
            dto.setAiUsageLogs(aiUsageLogConverter.toDto(item.getAiUsageLogs()));
            aiUsageLogConverter.setCollaborator(true);

        }
        if(this.tasks && ListUtil.isNotEmpty(item.getTasks())){
            taskConverter.init(true);
            taskConverter.setAssignedTo(false);
            dto.setTasks(taskConverter.toDto(item.getTasks()));
            taskConverter.setAssignedTo(true);

        }
        if(this.reservationRequests && ListUtil.isNotEmpty(item.getReservationRequests())){
            reservationRequestConverter.init(true);
            reservationRequestConverter.setReviewedBy(false);
            dto.setReservationRequests(reservationRequestConverter.toDto(item.getReservationRequests()));
            reservationRequestConverter.setReviewedBy(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.enterpriseMemberships = value;
        this.aiUsageLogs = value;
        this.tasks = value;
        this.reservationRequests = value;
    }
    public void initObject(boolean value) {
        this.displayCurrency = value;
    }
	
    public List<Collaborator> toItem(List<CollaboratorDto> dtos) {
        List<Collaborator> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (CollaboratorDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<CollaboratorDto> toDto(List<Collaborator> items) {
        List<CollaboratorDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Collaborator item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(CollaboratorDto dto, Collaborator t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getDisplayCurrency() == null  && dto.getDisplayCurrency() != null){
            t.setDisplayCurrency(new Currency());
        }else if (t.getDisplayCurrency() != null  && dto.getDisplayCurrency() != null){
            t.setDisplayCurrency(null);
            t.setDisplayCurrency(new Currency());
        }
        if (dto.getDisplayCurrency() != null)
        currencyConverter.copy(dto.getDisplayCurrency(), t.getDisplayCurrency());
        if (dto.getEnterpriseMemberships() != null)
            t.setEnterpriseMemberships(enterpriseMembershipConverter.copy(dto.getEnterpriseMemberships()));
        if (dto.getAiUsageLogs() != null)
            t.setAiUsageLogs(aiUsageLogConverter.copy(dto.getAiUsageLogs()));
        if (dto.getTasks() != null)
            t.setTasks(taskConverter.copy(dto.getTasks()));
        if (dto.getReservationRequests() != null)
            t.setReservationRequests(reservationRequestConverter.copy(dto.getReservationRequests()));
    }

    public List<Collaborator> copy(List<CollaboratorDto> dtos) {
        List<Collaborator> result = new ArrayList<>();
        if (dtos != null) {
            for (CollaboratorDto dto : dtos) {
                Collaborator instance = new Collaborator();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public AiUsageLogConverter getAiUsageLogConverter(){
        return this.aiUsageLogConverter;
    }
    public void setAiUsageLogConverter(AiUsageLogConverter aiUsageLogConverter ){
        this.aiUsageLogConverter = aiUsageLogConverter;
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
    public EnterpriseMembershipConverter getEnterpriseMembershipConverter(){
        return this.enterpriseMembershipConverter;
    }
    public void setEnterpriseMembershipConverter(EnterpriseMembershipConverter enterpriseMembershipConverter ){
        this.enterpriseMembershipConverter = enterpriseMembershipConverter;
    }
    public CollaboratorRoleConverter getCollaboratorRoleConverter(){
        return this.collaboratorRoleConverter;
    }
    public void setCollaboratorRoleConverter(CollaboratorRoleConverter collaboratorRoleConverter ){
        this.collaboratorRoleConverter = collaboratorRoleConverter;
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
    public CollaboratorPermissionOverrideConverter getCollaboratorPermissionOverrideConverter(){
        return this.collaboratorPermissionOverrideConverter;
    }
    public void setCollaboratorPermissionOverrideConverter(CollaboratorPermissionOverrideConverter collaboratorPermissionOverrideConverter ){
        this.collaboratorPermissionOverrideConverter = collaboratorPermissionOverrideConverter;
    }
    public ServiceProviderConverter getServiceProviderConverter(){
        return this.serviceProviderConverter;
    }
    public void setServiceProviderConverter(ServiceProviderConverter serviceProviderConverter ){
        this.serviceProviderConverter = serviceProviderConverter;
    }
    public AiUsageTypeConverter getAiUsageTypeConverter(){
        return this.aiUsageTypeConverter;
    }
    public void setAiUsageTypeConverter(AiUsageTypeConverter aiUsageTypeConverter ){
        this.aiUsageTypeConverter = aiUsageTypeConverter;
    }
    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public CurrencyConverter getCurrencyConverter(){
        return this.currencyConverter;
    }
    public void setCurrencyConverter(CurrencyConverter currencyConverter ){
        this.currencyConverter = currencyConverter;
    }
    public TaskTypeConverter getTaskTypeConverter(){
        return this.taskTypeConverter;
    }
    public void setTaskTypeConverter(TaskTypeConverter taskTypeConverter ){
        this.taskTypeConverter = taskTypeConverter;
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
    public ClientConverter getClientConverter(){
        return this.clientConverter;
    }
    public void setClientConverter(ClientConverter clientConverter ){
        this.clientConverter = clientConverter;
    }
    public boolean  isDisplayCurrency(){
        return this.displayCurrency;
    }
    public void  setDisplayCurrency(boolean displayCurrency){
        this.displayCurrency = displayCurrency;
    }
    public boolean  isEnterpriseMemberships(){
        return this.enterpriseMemberships ;
    }
    public void  setEnterpriseMemberships(boolean enterpriseMemberships ){
        this.enterpriseMemberships  = enterpriseMemberships ;
    }
    public boolean  isAiUsageLogs(){
        return this.aiUsageLogs ;
    }
    public void  setAiUsageLogs(boolean aiUsageLogs ){
        this.aiUsageLogs  = aiUsageLogs ;
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
