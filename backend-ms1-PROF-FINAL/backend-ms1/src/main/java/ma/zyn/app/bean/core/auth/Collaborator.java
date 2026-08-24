package ma.zyn.app.bean.core.auth;

import java.util.List;





import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.client.Client;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import ma.zyn.app.zynerator.security.bean.User;

@Entity
@Table(name = "collaborator")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="collaborator_seq",sequenceName="collaborator_seq",allocationSize=1, initialValue = 1)
public class Collaborator  extends User    {


    public Collaborator(String username) {
        super(username);
    }


    @Column(length = 500)
    private String name;

    @Column(columnDefinition = "boolean default false")
    private Boolean isActive = false;









    private Currency displayCurrency ;

    private List<EnterpriseMembership> enterpriseMemberships ;
    private List<AiUsageLog> aiUsageLogs ;
    private List<Task> tasks ;
    private List<ReservationRequest> reservationRequests ;

    public Collaborator(){
        super();
    }

    public Collaborator(Long id){
        this.id = id;
    }

    public Collaborator(Long id,String name){
        this.id = id;
        this.name = name ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="collaborator_seq")
    @Override
    public Long getId(){
        return this.id;
    }
    @Override
    public void setId(Long id){
        this.id = id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public Boolean  getIsActive(){
        return this.isActive;
    }
    public void setIsActive(Boolean isActive){
        this.isActive = isActive;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "display_currency")
    public Currency getDisplayCurrency(){
        return this.displayCurrency;
    }
    public void setDisplayCurrency(Currency displayCurrency){
        this.displayCurrency = displayCurrency;
    }
    @OneToMany(mappedBy = "collaborator")
    public List<EnterpriseMembership> getEnterpriseMemberships(){
        return this.enterpriseMemberships;
    }

    public void setEnterpriseMemberships(List<EnterpriseMembership> enterpriseMemberships){
        this.enterpriseMemberships = enterpriseMemberships;
    }
    @OneToMany(mappedBy = "collaborator")
    public List<AiUsageLog> getAiUsageLogs(){
        return this.aiUsageLogs;
    }

    public void setAiUsageLogs(List<AiUsageLog> aiUsageLogs){
        this.aiUsageLogs = aiUsageLogs;
    }
    @OneToMany(mappedBy = "assignedTo")
    public List<Task> getTasks(){
        return this.tasks;
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
    }
    @OneToMany(mappedBy = "reviewedBy")
    public List<ReservationRequest> getReservationRequests(){
        return this.reservationRequests;
    }

    public void setReservationRequests(List<ReservationRequest> reservationRequests){
        this.reservationRequests = reservationRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collaborator collaborator = (Collaborator) o;
        return id != null && id.equals(collaborator.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}