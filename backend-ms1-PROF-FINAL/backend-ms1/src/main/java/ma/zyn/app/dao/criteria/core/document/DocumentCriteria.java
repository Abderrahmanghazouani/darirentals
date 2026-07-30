package  ma.zyn.app.dao.criteria.core.document;


import ma.zyn.app.dao.criteria.core.reservation.ReservationCriteria;
import ma.zyn.app.dao.criteria.core.charge.ChargeCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class DocumentCriteria extends  BaseCriteria  {

    private String fileName;
    private String fileNameLike;
    private String file;
    private String fileLike;
    private String extractedVendor;
    private String extractedVendorLike;
    private String extractedAmount;
    private String extractedAmountMin;
    private String extractedAmountMax;

    private DocumentTypeCriteria documentType ;
    private List<DocumentTypeCriteria> documentTypes ;
    private ReservationCriteria reservation ;
    private List<ReservationCriteria> reservations ;
    private ChargeCriteria charge ;
    private List<ChargeCriteria> charges ;


    public String getFileName(){
        return this.fileName;
    }
    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public String getFileNameLike(){
        return this.fileNameLike;
    }
    public void setFileNameLike(String fileNameLike){
        this.fileNameLike = fileNameLike;
    }

    public String getFile(){
        return this.file;
    }
    public void setFile(String file){
        this.file = file;
    }
    public String getFileLike(){
        return this.fileLike;
    }
    public void setFileLike(String fileLike){
        this.fileLike = fileLike;
    }

    public String getExtractedVendor(){
        return this.extractedVendor;
    }
    public void setExtractedVendor(String extractedVendor){
        this.extractedVendor = extractedVendor;
    }
    public String getExtractedVendorLike(){
        return this.extractedVendorLike;
    }
    public void setExtractedVendorLike(String extractedVendorLike){
        this.extractedVendorLike = extractedVendorLike;
    }

    public String getExtractedAmount(){
        return this.extractedAmount;
    }
    public void setExtractedAmount(String extractedAmount){
        this.extractedAmount = extractedAmount;
    }   
    public String getExtractedAmountMin(){
        return this.extractedAmountMin;
    }
    public void setExtractedAmountMin(String extractedAmountMin){
        this.extractedAmountMin = extractedAmountMin;
    }
    public String getExtractedAmountMax(){
        return this.extractedAmountMax;
    }
    public void setExtractedAmountMax(String extractedAmountMax){
        this.extractedAmountMax = extractedAmountMax;
    }
      

    public DocumentTypeCriteria getDocumentType(){
        return this.documentType;
    }

    public void setDocumentType(DocumentTypeCriteria documentType){
        this.documentType = documentType;
    }
    public List<DocumentTypeCriteria> getDocumentTypes(){
        return this.documentTypes;
    }

    public void setDocumentTypes(List<DocumentTypeCriteria> documentTypes){
        this.documentTypes = documentTypes;
    }
    public ReservationCriteria getReservation(){
        return this.reservation;
    }

    public void setReservation(ReservationCriteria reservation){
        this.reservation = reservation;
    }
    public List<ReservationCriteria> getReservations(){
        return this.reservations;
    }

    public void setReservations(List<ReservationCriteria> reservations){
        this.reservations = reservations;
    }
    public ChargeCriteria getCharge(){
        return this.charge;
    }

    public void setCharge(ChargeCriteria charge){
        this.charge = charge;
    }
    public List<ChargeCriteria> getCharges(){
        return this.charges;
    }

    public void setCharges(List<ChargeCriteria> charges){
        this.charges = charges;
    }
}
