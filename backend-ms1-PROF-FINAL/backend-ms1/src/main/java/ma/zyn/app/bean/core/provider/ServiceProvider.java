package ma.zyn.app.bean.core.provider;

import java.util.List;





import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.bean.core.payment.PaymentType;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.property.Property;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "service_provider")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="service_provider_seq",sequenceName="service_provider_seq",allocationSize=1, initialValue = 1)
public class ServiceProvider  extends BaseEntity     {




    @Column(length = 500)
    private String name;

    @Column(length = 500)
    private String phone;

    @Column(columnDefinition = "boolean default false")
    private Boolean isActive = false;

    private ServiceType serviceType ;
    private Enterprise enterprise ;

    private List<Payment> payments ;
    private List<Task> tasks ;

    public ServiceProvider(){
        super();
    }

    public ServiceProvider(Long id){
        this.id = id;
    }

    public ServiceProvider(Long id,String name){
        this.id = id;
        this.name = name ;
    }
    public ServiceProvider(String name){
        this.name = name ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="service_provider_seq")
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
    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public Boolean  getIsActive(){
        return this.isActive;
    }
    public void setIsActive(Boolean isActive){
        this.isActive = isActive;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_type")
    public ServiceType getServiceType(){
        return this.serviceType;
    }
    public void setServiceType(ServiceType serviceType){
        this.serviceType = serviceType;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise")
    public Enterprise getEnterprise(){
        return this.enterprise;
    }
    public void setEnterprise(Enterprise enterprise){
        this.enterprise = enterprise;
    }
    @OneToMany(mappedBy = "serviceProvider")
    public List<Payment> getPayments(){
        return this.payments;
    }

    public void setPayments(List<Payment> payments){
        this.payments = payments;
    }
    @OneToMany(mappedBy = "serviceProvider")
    public List<Task> getTasks(){
        return this.tasks;
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceProvider serviceProvider = (ServiceProvider) o;
        return id != null && id.equals(serviceProvider.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

