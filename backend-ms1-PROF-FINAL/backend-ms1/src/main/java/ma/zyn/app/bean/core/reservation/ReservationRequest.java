package ma.zyn.app.bean.core.reservation;






import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.client.Client;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "reservation_request")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="reservation_request_seq",sequenceName="reservation_request_seq",allocationSize=1, initialValue = 1)
public class ReservationRequest  extends BaseEntity     {




    private String clientNote;

    private String staffNote;

    private Client client ;
    private Property requestedProperty ;
    private Property alternativeProperty ;
    private Collaborator reviewedBy ;
    private ReservationRequestStatus reservationRequestStatus ;
    private Reservation reservation ;


    public ReservationRequest(){
        super();
    }

    public ReservationRequest(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="reservation_request_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
      @Column(columnDefinition="TEXT")
    public String getClientNote(){
        return this.clientNote;
    }
    public void setClientNote(String clientNote){
        this.clientNote = clientNote;
    }
      @Column(columnDefinition="TEXT")
    public String getStaffNote(){
        return this.staffNote;
    }
    public void setStaffNote(String staffNote){
        this.staffNote = staffNote;
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
    @JoinColumn(name = "requested_property")
    public Property getRequestedProperty(){
        return this.requestedProperty;
    }
    public void setRequestedProperty(Property requestedProperty){
        this.requestedProperty = requestedProperty;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "alternative_property")
    public Property getAlternativeProperty(){
        return this.alternativeProperty;
    }
    public void setAlternativeProperty(Property alternativeProperty){
        this.alternativeProperty = alternativeProperty;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    public Collaborator getReviewedBy(){
        return this.reviewedBy;
    }
    public void setReviewedBy(Collaborator reviewedBy){
        this.reviewedBy = reviewedBy;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_request_status")
    public ReservationRequestStatus getReservationRequestStatus(){
        return this.reservationRequestStatus;
    }
    public void setReservationRequestStatus(ReservationRequestStatus reservationRequestStatus){
        this.reservationRequestStatus = reservationRequestStatus;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation")
    public Reservation getReservation(){
        return this.reservation;
    }
    public void setReservation(Reservation reservation){
        this.reservation = reservation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationRequest reservationRequest = (ReservationRequest) o;
        return id != null && id.equals(reservationRequest.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

