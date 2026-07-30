package ma.zyn.app.bean.core.payment;

import java.util.List;





import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.bean.core.charge.Charge;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "payment")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="payment_seq",sequenceName="payment_seq",allocationSize=1, initialValue = 1)
public class Payment  extends BaseEntity     {




    private BigDecimal amount = BigDecimal.ZERO;

    private String notes;

    private ServiceProvider serviceProvider ;
    private PaymentType paymentType ;
    private PaymentStatus paymentStatus ;

    private List<Charge> charges ;

    public Payment(){
        super();
    }

    public Payment(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="payment_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public BigDecimal getAmount(){
        return this.amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }
      @Column(columnDefinition="TEXT")
    public String getNotes(){
        return this.notes;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_provider")
    public ServiceProvider getServiceProvider(){
        return this.serviceProvider;
    }
    public void setServiceProvider(ServiceProvider serviceProvider){
        this.serviceProvider = serviceProvider;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_type")
    public PaymentType getPaymentType(){
        return this.paymentType;
    }
    public void setPaymentType(PaymentType paymentType){
        this.paymentType = paymentType;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_status")
    public PaymentStatus getPaymentStatus(){
        return this.paymentStatus;
    }
    public void setPaymentStatus(PaymentStatus paymentStatus){
        this.paymentStatus = paymentStatus;
    }
    @OneToMany(mappedBy = "payment")
    public List<Charge> getCharges(){
        return this.charges;
    }

    public void setCharges(List<Charge> charges){
        this.charges = charges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return id != null && id.equals(payment.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

