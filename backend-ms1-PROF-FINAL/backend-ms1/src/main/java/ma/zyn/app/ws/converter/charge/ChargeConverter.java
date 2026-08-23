package  ma.zyn.app.ws.converter.charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.payment.PaymentConverter;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.ws.converter.charge.ChargeTypeConverter;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.document.DocumentTypeConverter;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.document.DocumentConverter;
import ma.zyn.app.bean.core.document.Document;

import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.payment.Payment;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.ws.dto.charge.ChargeDto;

@Component
public class ChargeConverter {

    @Autowired
    private PaymentConverter paymentConverter ;
    @Autowired
    private ChargeTypeConverter chargeTypeConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private DocumentTypeConverter documentTypeConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private DocumentConverter documentConverter ;
    private boolean property;
    private boolean chargeType;
    private boolean payment;
    private boolean documents;

    public  ChargeConverter() {
        init(true);
    }

    public Charge toItem(ChargeDto dto) {
        if (dto == null) {
            return null;
        } else {
        Charge item = new Charge();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getLabel()))
                item.setLabel(dto.getLabel());
            if(StringUtil.isNotEmpty(dto.getAmount()))
                item.setAmount(dto.getAmount());
            if(dto.getChargeDate() != null)
                item.setChargeDate(dto.getChargeDate());
            if(dto.getProperty() != null && dto.getProperty().getId() != null){
                item.setProperty(new Property());
                item.getProperty().setId(dto.getProperty().getId());
                item.getProperty().setName(dto.getProperty().getName());
            }

            if(this.chargeType && dto.getChargeType()!=null)
                item.setChargeType(chargeTypeConverter.toItem(dto.getChargeType())) ;

            if(dto.getPayment() != null && dto.getPayment().getId() != null){
                item.setPayment(new Payment());
                item.getPayment().setId(dto.getPayment().getId());
                item.getPayment().setId(dto.getPayment().getId());
            }


            if(this.documents && ListUtil.isNotEmpty(dto.getDocuments()))
                item.setDocuments(documentConverter.toItem(dto.getDocuments()));


        return item;
        }
    }


    public ChargeDto toDto(Charge item) {
        if (item == null) {
            return null;
        } else {
            ChargeDto dto = new ChargeDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getLabel()))
                dto.setLabel(item.getLabel());
            if(StringUtil.isNotEmpty(item.getAmount()))
                dto.setAmount(item.getAmount());
            if(item.getChargeDate() != null)
                dto.setChargeDate(item.getChargeDate());
            if(this.property && item.getProperty()!=null) {
                dto.setProperty(propertyConverter.toDto(item.getProperty())) ;

            }
            if(this.chargeType && item.getChargeType()!=null) {
                dto.setChargeType(chargeTypeConverter.toDto(item.getChargeType())) ;

            }
            if(this.payment && item.getPayment()!=null) {
                dto.setPayment(paymentConverter.toDto(item.getPayment())) ;

            }
        if(this.documents && ListUtil.isNotEmpty(item.getDocuments())){
            documentConverter.init(true);
            documentConverter.setCharge(false);
            dto.setDocuments(documentConverter.toDto(item.getDocuments()));
            documentConverter.setCharge(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.documents = value;
    }
    public void initObject(boolean value) {
        this.property = value;
        this.chargeType = value;
        this.payment = value;
    }
	
    public List<Charge> toItem(List<ChargeDto> dtos) {
        List<Charge> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ChargeDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ChargeDto> toDto(List<Charge> items) {
        List<ChargeDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Charge item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ChargeDto dto, Charge t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getProperty() == null  && dto.getProperty() != null){
            t.setProperty(new Property());
        }else if (t.getProperty() != null  && dto.getProperty() != null){
            t.setProperty(null);
            t.setProperty(new Property());
        }
        if(t.getChargeType() == null  && dto.getChargeType() != null){
            t.setChargeType(new ChargeType());
        }else if (t.getChargeType() != null  && dto.getChargeType() != null){
            t.setChargeType(null);
            t.setChargeType(new ChargeType());
        }
        if(t.getPayment() == null  && dto.getPayment() != null){
            t.setPayment(new Payment());
        }else if (t.getPayment() != null  && dto.getPayment() != null){
            t.setPayment(null);
            t.setPayment(new Payment());
        }
        if (dto.getProperty() != null)
        propertyConverter.copy(dto.getProperty(), t.getProperty());
        if (dto.getChargeType() != null)
        chargeTypeConverter.copy(dto.getChargeType(), t.getChargeType());
        if (dto.getPayment() != null)
        paymentConverter.copy(dto.getPayment(), t.getPayment());
        if (dto.getDocuments() != null)
            t.setDocuments(documentConverter.copy(dto.getDocuments()));
    }

    public List<Charge> copy(List<ChargeDto> dtos) {
        List<Charge> result = new ArrayList<>();
        if (dtos != null) {
            for (ChargeDto dto : dtos) {
                Charge instance = new Charge();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public PaymentConverter getPaymentConverter(){
        return this.paymentConverter;
    }
    public void setPaymentConverter(PaymentConverter paymentConverter ){
        this.paymentConverter = paymentConverter;
    }
    public ChargeTypeConverter getChargeTypeConverter(){
        return this.chargeTypeConverter;
    }
    public void setChargeTypeConverter(ChargeTypeConverter chargeTypeConverter ){
        this.chargeTypeConverter = chargeTypeConverter;
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
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
    }
    public DocumentConverter getDocumentConverter(){
        return this.documentConverter;
    }
    public void setDocumentConverter(DocumentConverter documentConverter ){
        this.documentConverter = documentConverter;
    }
    public boolean  isProperty(){
        return this.property;
    }
    public void  setProperty(boolean property){
        this.property = property;
    }
    public boolean  isChargeType(){
        return this.chargeType;
    }
    public void  setChargeType(boolean chargeType){
        this.chargeType = chargeType;
    }
    public boolean  isPayment(){
        return this.payment;
    }
    public void  setPayment(boolean payment){
        this.payment = payment;
    }
    public boolean  isDocuments(){
        return this.documents ;
    }
    public void  setDocuments(boolean documents ){
        this.documents  = documents ;
    }
}
