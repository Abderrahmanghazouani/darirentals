package  ma.zyn.app.ws.converter.document;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.document.DocumentTypeConverter;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.ws.converter.charge.ChargeConverter;
import ma.zyn.app.bean.core.charge.Charge;

import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.charge.Charge;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.ws.dto.document.DocumentDto;

@Component
public class DocumentConverter {

    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private DocumentTypeConverter documentTypeConverter ;
    @Autowired
    private ChargeConverter chargeConverter ;
    private boolean documentType;
    private boolean reservation;
    private boolean charge;

    public  DocumentConverter() {
        initObject(true);
    }

    public Document toItem(DocumentDto dto) {
        if (dto == null) {
            return null;
        } else {
        Document item = new Document();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getFileName()))
                item.setFileName(dto.getFileName());
            if(StringUtil.isNotEmpty(dto.getFile()))
                item.setFile(dto.getFile());
            if(StringUtil.isNotEmpty(dto.getExtractedVendor()))
                item.setExtractedVendor(dto.getExtractedVendor());
            if(StringUtil.isNotEmpty(dto.getExtractedAmount()))
                item.setExtractedAmount(dto.getExtractedAmount());
            if(this.documentType && dto.getDocumentType()!=null)
                item.setDocumentType(documentTypeConverter.toItem(dto.getDocumentType())) ;

            if(dto.getReservation() != null && dto.getReservation().getId() != null){
                item.setReservation(new Reservation());
                item.getReservation().setId(dto.getReservation().getId());
                item.getReservation().setReference(dto.getReservation().getReference());
            }

            if(dto.getCharge() != null && dto.getCharge().getId() != null){
                item.setCharge(new Charge());
                item.getCharge().setId(dto.getCharge().getId());
                item.getCharge().setLabel(dto.getCharge().getLabel());
            }




        return item;
        }
    }


    public DocumentDto toDto(Document item) {
        if (item == null) {
            return null;
        } else {
            DocumentDto dto = new DocumentDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getFileName()))
                dto.setFileName(item.getFileName());
            if(StringUtil.isNotEmpty(item.getFile()))
                dto.setFile(item.getFile());
            if(StringUtil.isNotEmpty(item.getExtractedVendor()))
                dto.setExtractedVendor(item.getExtractedVendor());
            if(StringUtil.isNotEmpty(item.getExtractedAmount()))
                dto.setExtractedAmount(item.getExtractedAmount());
            if(this.documentType && item.getDocumentType()!=null) {
                dto.setDocumentType(documentTypeConverter.toDto(item.getDocumentType())) ;

            }
            if(this.reservation && item.getReservation()!=null) {
                dto.setReservation(reservationConverter.toDto(item.getReservation())) ;

            }
            if(this.charge && item.getCharge()!=null) {
                dto.setCharge(chargeConverter.toDto(item.getCharge())) ;

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.documentType = value;
        this.reservation = value;
        this.charge = value;
    }
	
    public List<Document> toItem(List<DocumentDto> dtos) {
        List<Document> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (DocumentDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<DocumentDto> toDto(List<Document> items) {
        List<DocumentDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Document item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(DocumentDto dto, Document t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getDocumentType() == null  && dto.getDocumentType() != null){
            t.setDocumentType(new DocumentType());
        }else if (t.getDocumentType() != null  && dto.getDocumentType() != null){
            t.setDocumentType(null);
            t.setDocumentType(new DocumentType());
        }
        if(t.getReservation() == null  && dto.getReservation() != null){
            t.setReservation(new Reservation());
        }else if (t.getReservation() != null  && dto.getReservation() != null){
            t.setReservation(null);
            t.setReservation(new Reservation());
        }
        if(t.getCharge() == null  && dto.getCharge() != null){
            t.setCharge(new Charge());
        }else if (t.getCharge() != null  && dto.getCharge() != null){
            t.setCharge(null);
            t.setCharge(new Charge());
        }
        if (dto.getDocumentType() != null)
        documentTypeConverter.copy(dto.getDocumentType(), t.getDocumentType());
        if (dto.getReservation() != null)
        reservationConverter.copy(dto.getReservation(), t.getReservation());
        if (dto.getCharge() != null)
        chargeConverter.copy(dto.getCharge(), t.getCharge());
    }

    public List<Document> copy(List<DocumentDto> dtos) {
        List<Document> result = new ArrayList<>();
        if (dtos != null) {
            for (DocumentDto dto : dtos) {
                Document instance = new Document();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public DocumentTypeConverter getDocumentTypeConverter(){
        return this.documentTypeConverter;
    }
    public void setDocumentTypeConverter(DocumentTypeConverter documentTypeConverter ){
        this.documentTypeConverter = documentTypeConverter;
    }
    public ChargeConverter getChargeConverter(){
        return this.chargeConverter;
    }
    public void setChargeConverter(ChargeConverter chargeConverter ){
        this.chargeConverter = chargeConverter;
    }
    public boolean  isDocumentType(){
        return this.documentType;
    }
    public void  setDocumentType(boolean documentType){
        this.documentType = documentType;
    }
    public boolean  isReservation(){
        return this.reservation;
    }
    public void  setReservation(boolean reservation){
        this.reservation = reservation;
    }
    public boolean  isCharge(){
        return this.charge;
    }
    public void  setCharge(boolean charge){
        this.charge = charge;
    }
}
