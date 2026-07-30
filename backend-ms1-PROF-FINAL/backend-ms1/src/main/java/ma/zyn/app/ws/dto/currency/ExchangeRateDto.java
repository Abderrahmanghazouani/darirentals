package  ma.zyn.app.ws.dto.currency;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;




@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExchangeRateDto  extends AuditBaseDto {

    private BigDecimal rate  ;
    private String source  ;

    private CurrencyDto baseCurrency ;
    private CurrencyDto targetCurrency ;



    public ExchangeRateDto(){
        super();
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


    public CurrencyDto getBaseCurrency(){
        return this.baseCurrency;
    }

    public void setBaseCurrency(CurrencyDto baseCurrency){
        this.baseCurrency = baseCurrency;
    }
    public CurrencyDto getTargetCurrency(){
        return this.targetCurrency;
    }

    public void setTargetCurrency(CurrencyDto targetCurrency){
        this.targetCurrency = targetCurrency;
    }






}
