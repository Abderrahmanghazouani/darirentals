package  ma.zyn.app.ws.converter.provider;

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
import ma.zyn.app.ws.converter.payment.PaymentTypeConverter;
import ma.zyn.app.bean.core.payment.PaymentType;
import ma.zyn.app.ws.converter.task.TaskStatusConverter;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.ws.converter.charge.ChargeConverter;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.payment.PaymentConverter;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.ws.converter.payment.PaymentStatusConverter;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.task.TaskTypeConverter;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.ws.converter.provider.ServiceTypeConverter;
import ma.zyn.app.bean.core.provider.ServiceType;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;

import ma.zyn.app.bean.core.enterprise.Enterprise;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;

@Component
public class ServiceProviderConverter {

    @Autowired
    private TaskConverter taskConverter ;
    @Autowired
    private TaskPriorityConverter taskPriorityConverter ;
    @Autowired
    private PaymentTypeConverter paymentTypeConverter ;
    @Autowired
    private TaskStatusConverter taskStatusConverter ;
    @Autowired
    private ChargeConverter chargeConverter ;
    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private PaymentConverter paymentConverter ;
    @Autowired
    private PaymentStatusConverter paymentStatusConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private TaskTypeConverter taskTypeConverter ;
    @Autowired
    private ServiceTypeConverter serviceTypeConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    private boolean serviceType;
    private boolean enterprise;
    private boolean payments;
    private boolean tasks;

    public  ServiceProviderConverter() {
        init(true);
    }

    public ServiceProvider toItem(ServiceProviderDto dto) {
        if (dto == null) {
            return null;
        } else {
        ServiceProvider item = new ServiceProvider();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getName()))
                item.setName(dto.getName());
            if(StringUtil.isNotEmpty(dto.getPhone()))
                item.setPhone(dto.getPhone());
            if(dto.getIsActive() != null)
                item.setIsActive(dto.getIsActive());
            if(this.serviceType && dto.getServiceType()!=null)
                item.setServiceType(serviceTypeConverter.toItem(dto.getServiceType())) ;

            if(dto.getEnterprise() != null && dto.getEnterprise().getId() != null){
                item.setEnterprise(new Enterprise());
                item.getEnterprise().setId(dto.getEnterprise().getId());
                item.getEnterprise().setName(dto.getEnterprise().getName());
            }


            if(this.payments && ListUtil.isNotEmpty(dto.getPayments()))
                item.setPayments(paymentConverter.toItem(dto.getPayments()));
            if(this.tasks && ListUtil.isNotEmpty(dto.getTasks()))
                item.setTasks(taskConverter.toItem(dto.getTasks()));


        return item;
        }
    }


    public ServiceProviderDto toDto(ServiceProvider item) {
        if (item == null) {
            return null;
        } else {
            ServiceProviderDto dto = new ServiceProviderDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getName()))
                dto.setName(item.getName());
            if(StringUtil.isNotEmpty(item.getPhone()))
                dto.setPhone(item.getPhone());
                dto.setIsActive(item.getIsActive());
            if(this.serviceType && item.getServiceType()!=null) {
                dto.setServiceType(serviceTypeConverter.toDto(item.getServiceType())) ;

            }
            if(this.enterprise && item.getEnterprise()!=null) {
                dto.setEnterprise(enterpriseConverter.toDto(item.getEnterprise())) ;

            }
        if(this.payments && ListUtil.isNotEmpty(item.getPayments())){
            paymentConverter.init(true);
            paymentConverter.setServiceProvider(false);
            dto.setPayments(paymentConverter.toDto(item.getPayments()));
            paymentConverter.setServiceProvider(true);

        }
        if(this.tasks && ListUtil.isNotEmpty(item.getTasks())){
            taskConverter.init(true);
            taskConverter.setServiceProvider(false);
            dto.setTasks(taskConverter.toDto(item.getTasks()));
            taskConverter.setServiceProvider(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.payments = value;
        this.tasks = value;
    }
    public void initObject(boolean value) {
        this.serviceType = value;
        this.enterprise = value;
    }
	
    public List<ServiceProvider> toItem(List<ServiceProviderDto> dtos) {
        List<ServiceProvider> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ServiceProviderDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ServiceProviderDto> toDto(List<ServiceProvider> items) {
        List<ServiceProviderDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (ServiceProvider item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ServiceProviderDto dto, ServiceProvider t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getServiceType() == null  && dto.getServiceType() != null){
            t.setServiceType(new ServiceType());
        }else if (t.getServiceType() != null  && dto.getServiceType() != null){
            t.setServiceType(null);
            t.setServiceType(new ServiceType());
        }
        if(t.getEnterprise() == null  && dto.getEnterprise() != null){
            t.setEnterprise(new Enterprise());
        }else if (t.getEnterprise() != null  && dto.getEnterprise() != null){
            t.setEnterprise(null);
            t.setEnterprise(new Enterprise());
        }
        if (dto.getServiceType() != null)
        serviceTypeConverter.copy(dto.getServiceType(), t.getServiceType());
        if (dto.getEnterprise() != null)
        enterpriseConverter.copy(dto.getEnterprise(), t.getEnterprise());
        if (dto.getPayments() != null)
            t.setPayments(paymentConverter.copy(dto.getPayments()));
        if (dto.getTasks() != null)
            t.setTasks(taskConverter.copy(dto.getTasks()));
    }

    public List<ServiceProvider> copy(List<ServiceProviderDto> dtos) {
        List<ServiceProvider> result = new ArrayList<>();
        if (dtos != null) {
            for (ServiceProviderDto dto : dtos) {
                ServiceProvider instance = new ServiceProvider();
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
    public PaymentTypeConverter getPaymentTypeConverter(){
        return this.paymentTypeConverter;
    }
    public void setPaymentTypeConverter(PaymentTypeConverter paymentTypeConverter ){
        this.paymentTypeConverter = paymentTypeConverter;
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
    public PaymentStatusConverter getPaymentStatusConverter(){
        return this.paymentStatusConverter;
    }
    public void setPaymentStatusConverter(PaymentStatusConverter paymentStatusConverter ){
        this.paymentStatusConverter = paymentStatusConverter;
    }
    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public TaskTypeConverter getTaskTypeConverter(){
        return this.taskTypeConverter;
    }
    public void setTaskTypeConverter(TaskTypeConverter taskTypeConverter ){
        this.taskTypeConverter = taskTypeConverter;
    }
    public ServiceTypeConverter getServiceTypeConverter(){
        return this.serviceTypeConverter;
    }
    public void setServiceTypeConverter(ServiceTypeConverter serviceTypeConverter ){
        this.serviceTypeConverter = serviceTypeConverter;
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
    public boolean  isServiceType(){
        return this.serviceType;
    }
    public void  setServiceType(boolean serviceType){
        this.serviceType = serviceType;
    }
    public boolean  isEnterprise(){
        return this.enterprise;
    }
    public void  setEnterprise(boolean enterprise){
        this.enterprise = enterprise;
    }
    public boolean  isPayments(){
        return this.payments ;
    }
    public void  setPayments(boolean payments ){
        this.payments  = payments ;
    }
    public boolean  isTasks(){
        return this.tasks ;
    }
    public void  setTasks(boolean tasks ){
        this.tasks  = tasks ;
    }
}
