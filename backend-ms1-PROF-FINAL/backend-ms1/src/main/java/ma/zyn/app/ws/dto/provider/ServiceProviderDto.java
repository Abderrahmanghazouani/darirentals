package  ma.zyn.app.ws.dto.provider;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.ws.dto.task.TaskPriorityDto;
import ma.zyn.app.ws.dto.payment.PaymentTypeDto;
import ma.zyn.app.ws.dto.task.TaskStatusDto;
import ma.zyn.app.ws.dto.charge.ChargeDto;
import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.payment.PaymentDto;
import ma.zyn.app.ws.dto.payment.PaymentStatusDto;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.task.TaskTypeDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
import ma.zyn.app.ws.dto.property.PropertyDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ServiceProviderDto  extends AuditBaseDto {

    private String name  ;
    private String phone  ;
    private Boolean isActive  ;

    private ServiceTypeDto serviceType ;
    private EnterpriseDto enterprise ;

    private List<PaymentDto> payments ;
    private List<TaskDto> tasks ;


    public ServiceProviderDto(){
        super();
    }



    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public Boolean getIsActive(){
        return this.isActive;
    }
    public void setIsActive(Boolean isActive){
        this.isActive = isActive;
    }


    public ServiceTypeDto getServiceType(){
        return this.serviceType;
    }

    public void setServiceType(ServiceTypeDto serviceType){
        this.serviceType = serviceType;
    }
    public EnterpriseDto getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseDto enterprise){
        this.enterprise = enterprise;
    }



    public List<PaymentDto> getPayments(){
        return this.payments;
    }

    public void setPayments(List<PaymentDto> payments){
        this.payments = payments;
    }
    public List<TaskDto> getTasks(){
        return this.tasks;
    }

    public void setTasks(List<TaskDto> tasks){
        this.tasks = tasks;
    }



}
