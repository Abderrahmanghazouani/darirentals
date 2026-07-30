package ma.zyn.app.bean.core.currency;








import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "exchange_rate")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="exchange_rate_seq",sequenceName="exchange_rate_seq",allocationSize=1, initialValue = 1)
public class ExchangeRate  extends BaseEntity     {




    private BigDecimal rate = BigDecimal.ZERO;

    @Column(length = 500)
    private String source;

    private Currency baseCurrency ;
    private Currency targetCurrency ;


    public ExchangeRate(){
        super();
    }

    public ExchangeRate(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="exchange_rate_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public BigDecimal getRate(){
        return this.rate;
    }
    public void setRate(BigDecimal rate){
        this.rate = rate;
    }
    public String getSource(){
        return this.source;
    }
    public void setSource(String source){
        this.source = source;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_currency")
    public Currency getBaseCurrency(){
        return this.baseCurrency;
    }
    public void setBaseCurrency(Currency baseCurrency){
        this.baseCurrency = baseCurrency;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_currency")
    public Currency getTargetCurrency(){
        return this.targetCurrency;
    }
    public void setTargetCurrency(Currency targetCurrency){
        this.targetCurrency = targetCurrency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExchangeRate exchangeRate = (ExchangeRate) o;
        return id != null && id.equals(exchangeRate.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

