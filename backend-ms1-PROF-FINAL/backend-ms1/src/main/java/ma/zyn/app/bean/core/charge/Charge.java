package ma.zyn.app.bean.core.charge;

import java.util.List;





import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.document.Document;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "charge")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="charge_seq",sequenceName="charge_seq",allocationSize=1, initialValue = 1)
public class Charge  extends BaseEntity     {




    @Column(length = 500)
    private String label;

    private BigDecimal amount = BigDecimal.ZERO;

    private Property property ;
    private ChargeType chargeType ;
    private Payment payment ;

    private List<Document> documents ;

    public Charge(){
        super();
    }

    public Charge(Long id){
        this.id = id;
    }

    public Charge(Long id,String label){
        this.id = id;
        this.label = label ;
    }
    public Charge(String label){
        this.label = label ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="charge_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public String getLabel(){
        return this.label;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
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
    @JoinColumn(name = "charge_type")
    public ChargeType getChargeType(){
        return this.chargeType;
    }
    public void setChargeType(ChargeType chargeType){
        this.chargeType = chargeType;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment")
    public Payment getPayment(){
        return this.payment;
    }
    public void setPayment(Payment payment){
        this.payment = payment;
    }
    @OneToMany(mappedBy = "charge")
    public List<Document> getDocuments(){
        return this.documents;
    }

    public void setDocuments(List<Document> documents){
        this.documents = documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Charge charge = (Charge) o;
        return id != null && id.equals(charge.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

