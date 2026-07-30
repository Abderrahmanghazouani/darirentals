package ma.zyn.app.bean.core.property;

import java.util.List;





import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.charge.Charge;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "city")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="city_seq",sequenceName="city_seq",allocationSize=1, initialValue = 1)
public class City  extends BaseEntity     {




    @Column(length = 500)
    private String name;

    private Country country ;

    private List<Property> properties ;

    public City(){
        super();
    }

    public City(Long id){
        this.id = id;
    }

    public City(Long id,String name){
        this.id = id;
        this.name = name ;
    }
    public City(String name){
        this.name = name ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="city_seq")
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country")
    public Country getCountry(){
        return this.country;
    }
    public void setCountry(Country country){
        this.country = country;
    }
    @OneToMany(mappedBy = "city")
    public List<Property> getProperties(){
        return this.properties;
    }

    public void setProperties(List<Property> properties){
        this.properties = properties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City city = (City) o;
        return id != null && id.equals(city.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

