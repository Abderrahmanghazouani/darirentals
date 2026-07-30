package  ma.zyn.app.dao.criteria.core.payment;


import ma.zyn.app.dao.criteria.core.provider.ServiceProviderCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class PaymentCriteria extends  BaseCriteria  {

    private String amount;
    private String amountMin;
    private String amountMax;
    private String notes;
    private String notesLike;

    private ServiceProviderCriteria serviceProvider ;
    private List<ServiceProviderCriteria> serviceProviders ;
    private PaymentTypeCriteria paymentType ;
    private List<PaymentTypeCriteria> paymentTypes ;
    private PaymentStatusCriteria paymentStatus ;
    private List<PaymentStatusCriteria> paymentStatuss ;


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
      
    public String getNotes(){
        return this.notes;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }
    public String getNotesLike(){
        return this.notesLike;
    }
    public void setNotesLike(String notesLike){
        this.notesLike = notesLike;
    }


    public ServiceProviderCriteria getServiceProvider(){
        return this.serviceProvider;
    }

    public void setServiceProvider(ServiceProviderCriteria serviceProvider){
        this.serviceProvider = serviceProvider;
    }
    public List<ServiceProviderCriteria> getServiceProviders(){
        return this.serviceProviders;
    }

    public void setServiceProviders(List<ServiceProviderCriteria> serviceProviders){
        this.serviceProviders = serviceProviders;
    }
    public PaymentTypeCriteria getPaymentType(){
        return this.paymentType;
    }

    public void setPaymentType(PaymentTypeCriteria paymentType){
        this.paymentType = paymentType;
    }
    public List<PaymentTypeCriteria> getPaymentTypes(){
        return this.paymentTypes;
    }

    public void setPaymentTypes(List<PaymentTypeCriteria> paymentTypes){
        this.paymentTypes = paymentTypes;
    }
    public PaymentStatusCriteria getPaymentStatus(){
        return this.paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusCriteria paymentStatus){
        this.paymentStatus = paymentStatus;
    }
    public List<PaymentStatusCriteria> getPaymentStatuss(){
        return this.paymentStatuss;
    }

    public void setPaymentStatuss(List<PaymentStatusCriteria> paymentStatuss){
        this.paymentStatuss = paymentStatuss;
    }
}
