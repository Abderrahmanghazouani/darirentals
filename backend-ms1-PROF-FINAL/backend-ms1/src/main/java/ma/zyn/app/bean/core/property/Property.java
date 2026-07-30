package ma.zyn.app.bean.core.property;

import java.util.List;





import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.client.Client;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "property")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="property_seq",sequenceName="property_seq",allocationSize=1, initialValue = 1)
public class Property  extends BaseEntity     {




    @Column(length = 500)
    private String name;

    @Column(length = 500)
    private String streetNumber;

    @Column(length = 500)
    private String streetName;

    @Column(length = 500)
    private String postalCode;

    private Integer capacity = 0;

    private BigDecimal pricePerNight = BigDecimal.ZERO;

    private BigDecimal latitude = BigDecimal.ZERO;

    private BigDecimal longitude = BigDecimal.ZERO;

    private PropertyType propertyType ;
    private PropertyStatus propertyStatus ;
    private City city ;
    private Enterprise enterprise ;

    private List<Reservation> reservations ;
    private List<Charge> charges ;
    private List<Task> tasks ;
    private List<FinancialReportProperty> financialReportProperties ;
    private List<ReservationRequest> reservationRequests ;
    private List<ReservationRequest> alternativeRequests ;

    public Property(){
        super();
    }

    public Property(Long id){
        this.id = id;
    }

    public Property(Long id,String name){
        this.id = id;
        this.name = name ;
    }
    public Property(String name){
        this.name = name ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="property_seq")
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
    public String getStreetNumber(){
        return this.streetNumber;
    }
    public void setStreetNumber(String streetNumber){
        this.streetNumber = streetNumber;
    }
    public String getStreetName(){
        return this.streetName;
    }
    public void setStreetName(String streetName){
        this.streetName = streetName;
    }
    public String getPostalCode(){
        return this.postalCode;
    }
    public void setPostalCode(String postalCode){
        this.postalCode = postalCode;
    }
    public Integer getCapacity(){
        return this.capacity;
    }
    public void setCapacity(Integer capacity){
        this.capacity = capacity;
    }
    public BigDecimal getPricePerNight(){
        return this.pricePerNight;
    }
    public void setPricePerNight(BigDecimal pricePerNight){
        this.pricePerNight = pricePerNight;
    }
    public BigDecimal getLatitude(){
        return this.latitude;
    }
    public void setLatitude(BigDecimal latitude){
        this.latitude = latitude;
    }
    public BigDecimal getLongitude(){
        return this.longitude;
    }
    public void setLongitude(BigDecimal longitude){
        this.longitude = longitude;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_type")
    public PropertyType getPropertyType(){
        return this.propertyType;
    }
    public void setPropertyType(PropertyType propertyType){
        this.propertyType = propertyType;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_status")
    public PropertyStatus getPropertyStatus(){
        return this.propertyStatus;
    }
    public void setPropertyStatus(PropertyStatus propertyStatus){
        this.propertyStatus = propertyStatus;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city")
    public City getCity(){
        return this.city;
    }
    public void setCity(City city){
        this.city = city;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise")
    public Enterprise getEnterprise(){
        return this.enterprise;
    }
    public void setEnterprise(Enterprise enterprise){
        this.enterprise = enterprise;
    }
    @OneToMany(mappedBy = "property")
    public List<Reservation> getReservations(){
        return this.reservations;
    }

    public void setReservations(List<Reservation> reservations){
        this.reservations = reservations;
    }
    @OneToMany(mappedBy = "property")
    public List<Charge> getCharges(){
        return this.charges;
    }

    public void setCharges(List<Charge> charges){
        this.charges = charges;
    }
    @OneToMany(mappedBy = "property")
    public List<Task> getTasks(){
        return this.tasks;
    }

    public void setTasks(List<Task> tasks){
        this.tasks = tasks;
    }
    @OneToMany(mappedBy = "property")
    public List<FinancialReportProperty> getFinancialReportProperties(){
        return this.financialReportProperties;
    }

    public void setFinancialReportProperties(List<FinancialReportProperty> financialReportProperties){
        this.financialReportProperties = financialReportProperties;
    }
    @OneToMany(mappedBy = "requestedProperty")
    public List<ReservationRequest> getReservationRequests(){
        return this.reservationRequests;
    }

    public void setReservationRequests(List<ReservationRequest> reservationRequests){
        this.reservationRequests = reservationRequests;
    }
    @OneToMany(mappedBy = "alternativeProperty")
    public List<ReservationRequest> getAlternativeRequests(){
        return this.alternativeRequests;
    }

    public void setAlternativeRequests(List<ReservationRequest> alternativeRequests){
        this.alternativeRequests = alternativeRequests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Property property = (Property) o;
        return id != null && id.equals(property.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

