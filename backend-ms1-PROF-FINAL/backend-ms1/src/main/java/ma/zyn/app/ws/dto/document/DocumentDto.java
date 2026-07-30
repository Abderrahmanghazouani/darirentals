package  ma.zyn.app.ws.dto.document;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;


import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.charge.ChargeDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class DocumentDto  extends AuditBaseDto {

    private String fileName  ;
    private String file  ;
    private String extractedVendor  ;
    private BigDecimal extractedAmount  ;

    private DocumentTypeDto documentType ;
    private ReservationDto reservation ;
    private ChargeDto charge ;



    public DocumentDto(){
        super();
    }



    public String getFileName(){
        return this.fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }

    public String getFile(){
        return this.file;
    }
    public void setFile(String file){
        this.file = file;
    }

    public String getExtractedVendor(){
        return this.extractedVendor;
    }
    public void setExtractedVendor(String extractedVendor){
        this.extractedVendor = extractedVendor;
    }

    public BigDecimal getExtractedAmount(){
        return this.extractedAmount;
    }
    public void setExtractedAmount(BigDecimal extractedAmount){
        this.extractedAmount = extractedAmount;
    }


    public DocumentTypeDto getDocumentType(){
        return this.documentType;
    }

    public void setDocumentType(DocumentTypeDto documentType){
        this.documentType = documentType;
    }
    public ReservationDto getReservation(){
        return this.reservation;
    }

    public void setReservation(ReservationDto reservation){
        this.reservation = reservation;
    }
    public ChargeDto getCharge(){
        return this.charge;
    }

    public void setCharge(ChargeDto charge){
        this.charge = charge;
    }






}
