package ma.zyn.app.bean.core.reservation;

import java.util.List;





import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.client.Client;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "reservation")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="reservation_seq",sequenceName="reservation_seq",allocationSize=1, initialValue = 1)
public class Reservation  extends BaseEntity     {




    @Column(length = 500)
    private String reference;

    private BigDecimal amount = BigDecimal.ZERO;

    private BigDecimal pricePerNight = BigDecimal.ZERO;

    private Client client ;
    private Property property ;
    private ReservationPlatform reservationPlatform ;
    private ReservationStatus reservationStatus ;

    private List<Document> documents ;
    private List<Task> tasks ;
    private List<ReservationRequest> reservationRequests ;

    public Reservation(){
        super();
    }

    public Reservation(Long id){
        this.id = id;
    }

    public Reservation(Long id,String reference){
        this.id = id;
        this.reference = reference ;
    }
    public Reservation(String reference){
        this.reference = reference ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="reservation_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public String getReference(){
        return this.reference;
    }
    public void setReference(String reference){
        this.reference = reference;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
    public BigDecimal getPricePerNight(){
        return this.pricePerNight;
    }
    public void setPricePerNight(BigDecimal pricePerNight){
        this.pricePerNight = pricePerNight;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client")
    public Client getClient(){
        return this.client;
    }
    public void setClient(Client client){
        this.client = client;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property")
    public Property getProperty(){
        return this.property;
    }
    public void setProperty(Property property){
        this.property = property;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_platform")
    public ReservationPlatform getReservationPlatform(){
        return this.reservationPlatform;
    }
    public void setReservationPlatform(ReservationPlatform reservationPlatform){
        this.reservationPlatform = reservationPlatform;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_status")
    public ReservationStatus getReservationStatus(){
        return this.reservationStatus;
    }
    public void setReservationStatus(ReservationStatus reservationStatus){
        this.reservationStatus = reservationStatus;
    }
    @OneToMany(mappedBy = "reservation")
    public List<Document> getDocuments(){
        return this.documents;
    }

    public void setDocuments(List<Document> documents){
        this.documents = documents;
    }
    @OneToMany(mappedBy = "reservation")
    public List<Task> getTasks(){
        return this.tasks;
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
    }
    @OneToMany(mappedBy = "reservation")
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
        Reservation reservation = (Reservation) o;
        return id != null && id.equals(reservation.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

