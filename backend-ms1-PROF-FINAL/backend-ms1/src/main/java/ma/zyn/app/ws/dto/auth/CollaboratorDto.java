package  ma.zyn.app.ws.dto.auth;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import ma.zyn.app.zynerator.security.bean.Role;
import java.util.Collection;
import ma.zyn.app.zynerator.security.ws.dto.UserDto;
import java.util.List;


import ma.zyn.app.ws.dto.ai.AiUsageLogDto;
import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.ws.dto.task.TaskPriorityDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseMembershipDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestStatusDto;
import ma.zyn.app.ws.dto.document.DocumentDto;
import ma.zyn.app.ws.dto.task.TaskStatusDto;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
import ma.zyn.app.ws.dto.ai.AiUsageTypeDto;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.currency.CurrencyDto;
import ma.zyn.app.ws.dto.task.TaskTypeDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
import ma.zyn.app.ws.dto.property.PropertyDto;
import ma.zyn.app.ws.dto.client.ClientDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaboratorDto  extends UserDto {

    private String name  ;
    private String phone  ;
    private Boolean isActive  ;

    private CurrencyDto displayCurrency ;

    private List<EnterpriseMembershipDto> enterpriseMemberships ;
    private List<AiUsageLogDto> aiUsageLogs ;
    private List<TaskDto> tasks ;
    private List<ReservationRequestDto> reservationRequests ;


    private Collection<Role> roles;
    public CollaboratorDto(){
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


    public CurrencyDto getDisplayCurrency(){
        return this.displayCurrency;
    }

    public void setDisplayCurrency(CurrencyDto displayCurrency){
        this.displayCurrency = displayCurrency;
    }



    public List<EnterpriseMembershipDto> getEnterpriseMemberships(){
        return this.enterpriseMemberships;
    }

    public void setEnterpriseMemberships(List<EnterpriseMembershipDto> enterpriseMemberships){
        this.enterpriseMemberships = enterpriseMemberships;
    }
    public List<AiUsageLogDto> getAiUsageLogs(){
        return this.aiUsageLogs;
    }

    public void setAiUsageLogs(List<AiUsageLogDto> aiUsageLogs){
        this.aiUsageLogs = aiUsageLogs;
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




    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}
