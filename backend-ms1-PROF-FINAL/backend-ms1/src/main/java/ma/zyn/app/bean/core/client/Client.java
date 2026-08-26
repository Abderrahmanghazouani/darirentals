package ma.zyn.app.bean.core.client;

import java.util.List;




import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.bean.core.document.Document;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import ma.zyn.app.zynerator.security.bean.User;

@Entity
@Table(name = "client")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="client_seq",sequenceName="client_seq",allocationSize=1, initialValue = 1)
public class Client  extends User    {


    public Client(String username) {
        super(username);
    }


    @Column(length = 500)
    private String nationality;










    private Enterprise enterprise ;

    private List<Reservation> reservations ;
    private List<ReservationRequest> reservationRequests ;

    public Client(){
        super();
    }

    public Client(Long id){
        this.id = id;
    }

    public Client(Long id,String fullName){
        this.id = id;
        this.fullName = fullName ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="client_seq")
    @Override
    public Long getId(){
        return this.id;
    }
    @Override
    public void setId(Long id){
        this.id = id;
    }
    public String getNationality(){
        return this.nationality;
    }
    public void setNationality(String nationality){
        this.nationality = nationality;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise")
    public Enterprise getEnterprise(){
        return this.enterprise;
    }
    public void setEnterprise(Enterprise enterprise){
        this.enterprise = enterprise;
    }
    @OneToMany(mappedBy = "client")
    public List<Reservation> getReservations(){
        return this.reservations;
    }

    public void setReservations(List<Reservation> reservations){
        this.reservations = reservations;
    }
    @OneToMany(mappedBy = "client")
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
        Client client = (Client) o;
        return id != null && id.equals(client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}