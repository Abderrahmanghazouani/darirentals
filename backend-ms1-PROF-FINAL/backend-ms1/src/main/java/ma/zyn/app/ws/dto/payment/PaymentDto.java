package  ma.zyn.app.ws.dto.payment;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.math.BigDecimal;


import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
import ma.zyn.app.ws.dto.charge.ChargeTypeDto;
import ma.zyn.app.ws.dto.property.PropertyDto;
import ma.zyn.app.ws.dto.document.DocumentDto;
import ma.zyn.app.ws.dto.charge.ChargeDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentDto  extends AuditBaseDto {

    private BigDecimal amount  ;
    private String notes  ;

    private ServiceProviderDto serviceProvider ;
    private PaymentTypeDto paymentType ;
    private PaymentStatusDto paymentStatus ;

    private List<ChargeDto> charges ;


    public PaymentDto(){
        super();
    }



    public BigDecimal getAmount(){
        return this.amount;
    }
    public void setAmount(BigDecimal amount){
        this.amount = amount;
    }

    public String getNotes(){
        return this.notes;
    }
    public void setNotes(String notes){
        this.notes = notes;
    }


    public ServiceProviderDto getServiceProvider(){
        return this.serviceProvider;
    }

    public void setServiceProvider(ServiceProviderDto serviceProvider){
        this.serviceProvider = serviceProvider;
    }
    public PaymentTypeDto getPaymentType(){
        return this.paymentType;
    }

    public void setPaymentType(PaymentTypeDto paymentType){
        this.paymentType = paymentType;
    }
    public PaymentStatusDto getPaymentStatus(){
        return this.paymentStatus;
    }

    public void setPaymentStatus(PaymentStatusDto paymentStatus){
        this.paymentStatus = paymentStatus;
    }



    public List<ChargeDto> getCharges(){
        return this.charges;
    }

    public void setCharges(List<ChargeDto> charges){
        this.charges = charges;
    }



}
