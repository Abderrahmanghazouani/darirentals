package  ma.zyn.app.ws.dto.charge;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.math.BigDecimal;


import ma.zyn.app.ws.dto.payment.PaymentDto;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.document.DocumentTypeDto;
import ma.zyn.app.ws.dto.property.PropertyDto;
import ma.zyn.app.ws.dto.document.DocumentDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChargeDto  extends AuditBaseDto {

    private String label  ;
    private BigDecimal amount  ;

    private PropertyDto property ;
    private ChargeTypeDto chargeType ;
    private PaymentDto payment ;

    private List<DocumentDto> documents ;


    public ChargeDto(){
        super();
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


    public PropertyDto getProperty(){
        return this.property;
    }

    public void setProperty(PropertyDto property){
        this.property = property;
    }
    public ChargeTypeDto getChargeType(){
        return this.chargeType;
    }

    public void setChargeType(ChargeTypeDto chargeType){
        this.chargeType = chargeType;
    }
    public PaymentDto getPayment(){
        return this.payment;
    }

    public void setPayment(PaymentDto payment){
        this.payment = payment;
    }



    public List<DocumentDto> getDocuments(){
        return this.documents;
    }

    public void setDocuments(List<DocumentDto> documents){
        this.documents = documents;
    }



}
