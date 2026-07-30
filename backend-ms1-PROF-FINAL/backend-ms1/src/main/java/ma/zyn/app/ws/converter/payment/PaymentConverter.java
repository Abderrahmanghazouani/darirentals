package  ma.zyn.app.ws.converter.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.payment.PaymentStatusConverter;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.ws.converter.charge.ChargeTypeConverter;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.ws.converter.payment.PaymentTypeConverter;
import ma.zyn.app.bean.core.payment.PaymentType;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.document.DocumentConverter;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.ws.converter.charge.ChargeConverter;
import ma.zyn.app.bean.core.charge.Charge;

import ma.zyn.app.bean.core.provider.ServiceProvider;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.ws.dto.payment.PaymentDto;

@Component
public class PaymentConverter {

    @Autowired
    private PaymentStatusConverter paymentStatusConverter ;
    @Autowired
    private ServiceProviderConverter serviceProviderConverter ;
    @Autowired
    private ChargeTypeConverter chargeTypeConverter ;
    @Autowired
    private PaymentTypeConverter paymentTypeConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private DocumentConverter documentConverter ;
    @Autowired
    private ChargeConverter chargeConverter ;
    private boolean serviceProvider;
    private boolean paymentType;
    private boolean paymentStatus;
    private boolean charges;

    public  PaymentConverter() {
        init(true);
    }

    public Payment toItem(PaymentDto dto) {
        if (dto == null) {
            return null;
        } else {
        Payment item = new Payment();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getAmount()))
                item.setAmount(dto.getAmount());
            if(StringUtil.isNotEmpty(dto.getNotes()))
                item.setNotes(dto.getNotes());
            if(dto.getServiceProvider() != null && dto.getServiceProvider().getId() != null){
                item.setServiceProvider(new ServiceProvider());
                item.getServiceProvider().setId(dto.getServiceProvider().getId());
                item.getServiceProvider().setName(dto.getServiceProvider().getName());
            }

            if(this.paymentType && dto.getPaymentType()!=null)
                item.setPaymentType(paymentTypeConverter.toItem(dto.getPaymentType())) ;

            if(this.paymentStatus && dto.getPaymentStatus()!=null)
                item.setPaymentStatus(paymentStatusConverter.toItem(dto.getPaymentStatus())) ;


            if(this.charges && ListUtil.isNotEmpty(dto.getCharges()))
                item.setCharges(chargeConverter.toItem(dto.getCharges()));


        return item;
        }
    }


    public PaymentDto toDto(Payment item) {
        if (item == null) {
            return null;
        } else {
            PaymentDto dto = new PaymentDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getAmount()))
                dto.setAmount(item.getAmount());
            if(StringUtil.isNotEmpty(item.getNotes()))
                dto.setNotes(item.getNotes());
            if(this.serviceProvider && item.getServiceProvider()!=null) {
                dto.setServiceProvider(serviceProviderConverter.toDto(item.getServiceProvider())) ;

            }
            if(this.paymentType && item.getPaymentType()!=null) {
                dto.setPaymentType(paymentTypeConverter.toDto(item.getPaymentType())) ;

            }
            if(this.paymentStatus && item.getPaymentStatus()!=null) {
                dto.setPaymentStatus(paymentStatusConverter.toDto(item.getPaymentStatus())) ;

            }
        if(this.charges && ListUtil.isNotEmpty(item.getCharges())){
            chargeConverter.init(true);
            chargeConverter.setPayment(false);
            dto.setCharges(chargeConverter.toDto(item.getCharges()));
            chargeConverter.setPayment(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.charges = value;
    }
    public void initObject(boolean value) {
        this.serviceProvider = value;
        this.paymentType = value;
        this.paymentStatus = value;
    }
	
    public List<Payment> toItem(List<PaymentDto> dtos) {
        List<Payment> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (PaymentDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<PaymentDto> toDto(List<Payment> items) {
        List<PaymentDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Payment item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(PaymentDto dto, Payment t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getServiceProvider() == null  && dto.getServiceProvider() != null){
            t.setServiceProvider(new ServiceProvider());
        }else if (t.getServiceProvider() != null  && dto.getServiceProvider() != null){
            t.setServiceProvider(null);
            t.setServiceProvider(new ServiceProvider());
        }
        if(t.getPaymentType() == null  && dto.getPaymentType() != null){
            t.setPaymentType(new PaymentType());
        }else if (t.getPaymentType() != null  && dto.getPaymentType() != null){
            t.setPaymentType(null);
            t.setPaymentType(new PaymentType());
        }
        if(t.getPaymentStatus() == null  && dto.getPaymentStatus() != null){
            t.setPaymentStatus(new PaymentStatus());
        }else if (t.getPaymentStatus() != null  && dto.getPaymentStatus() != null){
            t.setPaymentStatus(null);
            t.setPaymentStatus(new PaymentStatus());
        }
        if (dto.getServiceProvider() != null)
        serviceProviderConverter.copy(dto.getServiceProvider(), t.getServiceProvider());
        if (dto.getPaymentType() != null)
        paymentTypeConverter.copy(dto.getPaymentType(), t.getPaymentType());
        if (dto.getPaymentStatus() != null)
        paymentStatusConverter.copy(dto.getPaymentStatus(), t.getPaymentStatus());
        if (dto.getCharges() != null)
            t.setCharges(chargeConverter.copy(dto.getCharges()));
    }

    public List<Payment> copy(List<PaymentDto> dtos) {
        List<Payment> result = new ArrayList<>();
        if (dtos != null) {
            for (PaymentDto dto : dtos) {
                Payment instance = new Payment();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public PaymentStatusConverter getPaymentStatusConverter(){
        return this.paymentStatusConverter;
    }
    public void setPaymentStatusConverter(PaymentStatusConverter paymentStatusConverter ){
        this.paymentStatusConverter = paymentStatusConverter;
    }
    public ServiceProviderConverter getServiceProviderConverter(){
        return this.serviceProviderConverter;
    }
    public void setServiceProviderConverter(ServiceProviderConverter serviceProviderConverter ){
        this.serviceProviderConverter = serviceProviderConverter;
    }
    public ChargeTypeConverter getChargeTypeConverter(){
        return this.chargeTypeConverter;
    }
    public void setChargeTypeConverter(ChargeTypeConverter chargeTypeConverter ){
        this.chargeTypeConverter = chargeTypeConverter;
    }
    public PaymentTypeConverter getPaymentTypeConverter(){
        return this.paymentTypeConverter;
    }
    public void setPaymentTypeConverter(PaymentTypeConverter paymentTypeConverter ){
        this.paymentTypeConverter = paymentTypeConverter;
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
    public ChargeConverter getChargeConverter(){
        return this.chargeConverter;
    }
    public void setChargeConverter(ChargeConverter chargeConverter ){
        this.chargeConverter = chargeConverter;
    }
    public boolean  isServiceProvider(){
        return this.serviceProvider;
    }
    public void  setServiceProvider(boolean serviceProvider){
        this.serviceProvider = serviceProvider;
    }
    public boolean  isPaymentType(){
        return this.paymentType;
    }
    public void  setPaymentType(boolean paymentType){
        this.paymentType = paymentType;
    }
    public boolean  isPaymentStatus(){
        return this.paymentStatus;
    }
    public void  setPaymentStatus(boolean paymentStatus){
        this.paymentStatus = paymentStatus;
    }
    public boolean  isCharges(){
        return this.charges ;
    }
    public void  setCharges(boolean charges ){
        this.charges  = charges ;
    }
}
