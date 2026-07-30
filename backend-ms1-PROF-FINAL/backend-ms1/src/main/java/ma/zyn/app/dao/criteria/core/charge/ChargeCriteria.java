package  ma.zyn.app.dao.criteria.core.charge;


import ma.zyn.app.dao.criteria.core.payment.PaymentCriteria;
import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class ChargeCriteria extends  BaseCriteria  {

    private String label;
    private String labelLike;
    private String amount;
    private String amountMin;
    private String amountMax;

    private PropertyCriteria property ;
    private List<PropertyCriteria> propertys ;
    private ChargeTypeCriteria chargeType ;
    private List<ChargeTypeCriteria> chargeTypes ;
    private PaymentCriteria payment ;
    private List<PaymentCriteria> payments ;


    public String getLabel(){
        return this.label;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getLabelLike(){
        return this.labelLike;
    }
    public void setLabelLike(String labelLike){
        this.labelLike = labelLike;
    }

    public String getAmount(){
        return this.amount;
    }
    public void setAmount(String amount){
        this.amount = amount;
    }   
    public String getAmountMin(){
        return this.amountMin;
    }
    public void setAmountMin(String amountMin){
        this.amountMin = amountMin;
    }
    public String getAmountMax(){
        return this.amountMax;
    }
    public void setAmountMax(String amountMax){
        this.amountMax = amountMax;
    }
      

    public PropertyCriteria getProperty(){
        return this.property;
    }

    public void setProperty(PropertyCriteria property){
        this.property = property;
    }
    public List<PropertyCriteria> getPropertys(){
        return this.propertys;
    }

    public void setPropertys(List<PropertyCriteria> propertys){
        this.propertys = propertys;
    }
    public ChargeTypeCriteria getChargeType(){
        return this.chargeType;
    }

    public void setChargeType(ChargeTypeCriteria chargeType){
        this.chargeType = chargeType;
    }
    public List<ChargeTypeCriteria> getChargeTypes(){
        return this.chargeTypes;
    }

    public void setChargeTypes(List<ChargeTypeCriteria> chargeTypes){
        this.chargeTypes = chargeTypes;
    }
    public PaymentCriteria getPayment(){
        return this.payment;
    }

    public void setPayment(PaymentCriteria payment){
        this.payment = payment;
    }
    public List<PaymentCriteria> getPayments(){
        return this.payments;
    }

    public void setPayments(List<PaymentCriteria> payments){
        this.payments = payments;
    }
}
