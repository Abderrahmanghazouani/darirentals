package  ma.zyn.app.dao.criteria.core.currency;



import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class ExchangeRateCriteria extends  BaseCriteria  {

    private String rate;
    private String rateMin;
    private String rateMax;
    private String source;
    private String sourceLike;

    private CurrencyCriteria baseCurrency ;
    private List<CurrencyCriteria> baseCurrencys ;
    private CurrencyCriteria targetCurrency ;
    private List<CurrencyCriteria> targetCurrencys ;


    public String getRate(){
        return this.rate;
    }
    public void setRate(String rate){
        this.rate = rate;
    }   
    public String getRateMin(){
        return this.rateMin;
    }
    public void setRateMin(String rateMin){
        this.rateMin = rateMin;
    }
    public String getRateMax(){
        return this.rateMax;
    }
    public void setRateMax(String rateMax){
        this.rateMax = rateMax;
    }
      
    public String getSource(){
        return this.source;
    }
    public void setSource(String source){
        this.source = source;
    }
    public String getSourceLike(){
        return this.sourceLike;
    }
    public void setSourceLike(String sourceLike){
        this.sourceLike = sourceLike;
    }


    public CurrencyCriteria getBaseCurrency(){
        return this.baseCurrency;
    }

    public void setBaseCurrency(CurrencyCriteria baseCurrency){
        this.baseCurrency = baseCurrency;
    }
    public List<CurrencyCriteria> getBaseCurrencys(){
        return this.baseCurrencys;
    }

    public void setBaseCurrencys(List<CurrencyCriteria> baseCurrencys){
        this.baseCurrencys = baseCurrencys;
    }
    public CurrencyCriteria getTargetCurrency(){
        return this.targetCurrency;
    }

    public void setTargetCurrency(CurrencyCriteria targetCurrency){
        this.targetCurrency = targetCurrency;
    }
    public List<CurrencyCriteria> getTargetCurrencys(){
        return this.targetCurrencys;
    }

    public void setTargetCurrencys(List<CurrencyCriteria> targetCurrencys){
        this.targetCurrencys = targetCurrencys;
    }
}
