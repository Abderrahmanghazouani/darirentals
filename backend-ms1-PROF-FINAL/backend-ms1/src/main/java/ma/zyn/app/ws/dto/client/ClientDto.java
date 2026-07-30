package  ma.zyn.app.ws.dto.client;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import ma.zyn.app.zynerator.security.bean.Role;
import java.util.Collection;
import ma.zyn.app.zynerator.security.ws.dto.UserDto;
import java.util.List;


import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;
import ma.zyn.app.ws.dto.reservation.ReservationPlatformDto;
import ma.zyn.app.ws.dto.reservation.ReservationStatusDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
import ma.zyn.app.ws.dto.property.PropertyDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestStatusDto;
import ma.zyn.app.ws.dto.document.DocumentDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto  extends UserDto {

    private String fullName  ;
    private String phone  ;
    private String nationality  ;

    private EnterpriseDto enterprise ;

    private List<ReservationDto> reservations ;
    private List<ReservationRequestDto> reservationRequests ;


    private Collection<Role> roles;
    public ClientDto(){
        super();
    }



    public String getFullName(){
        return this.fullName;
    }
    public void setFullName(String fullName){
        this.fullName = fullName;
    }

    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getNationality(){
        return this.nationality;
    }
    public void setNationality(String nationality){
        this.nationality = nationality;
    }


    public EnterpriseDto getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseDto enterprise){
        this.enterprise = enterprise;
    }



    public List<ReservationDto> getReservations(){
        return this.reservations;
    }

    public void setReservations(List<ReservationDto> reservations){
        this.reservations = reservations;
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
