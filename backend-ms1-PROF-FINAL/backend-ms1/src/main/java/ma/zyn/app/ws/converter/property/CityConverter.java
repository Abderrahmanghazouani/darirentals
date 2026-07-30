package  ma.zyn.app.ws.converter.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.task.TaskConverter;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.report.FinancialReportPropertyConverter;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.ws.converter.report.FinancialReportConverter;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.property.CountryConverter;
import ma.zyn.app.bean.core.property.Country;
import ma.zyn.app.ws.converter.property.PropertyTypeConverter;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.ws.converter.property.PropertyStatusConverter;
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.charge.ChargeConverter;
import ma.zyn.app.bean.core.charge.Charge;

import ma.zyn.app.bean.core.property.Country;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.ws.dto.property.CityDto;

@Component
public class CityConverter {

    @Autowired
    private TaskConverter taskConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private FinancialReportPropertyConverter financialReportPropertyConverter ;
    @Autowired
    private ReservationRequestConverter reservationRequestConverter ;
    @Autowired
    private FinancialReportConverter financialReportConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private CountryConverter countryConverter ;
    @Autowired
    private PropertyTypeConverter propertyTypeConverter ;
    @Autowired
    private PropertyStatusConverter propertyStatusConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private ChargeConverter chargeConverter ;
    private boolean country;
    private boolean properties;

    public  CityConverter() {
        init(true);
    }

    public City toItem(CityDto dto) {
        if (dto == null) {
            return null;
        } else {
        City item = new City();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getName()))
                item.setName(dto.getName());
            if(dto.getCountry() != null && dto.getCountry().getId() != null){
                item.setCountry(new Country());
                item.getCountry().setId(dto.getCountry().getId());
                item.getCountry().setName(dto.getCountry().getName());
            }


            if(this.properties && ListUtil.isNotEmpty(dto.getProperties()))
                item.setProperties(propertyConverter.toItem(dto.getProperties()));


        return item;
        }
    }


    public CityDto toDto(City item) {
        if (item == null) {
            return null;
        } else {
            CityDto dto = new CityDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getName()))
                dto.setName(item.getName());
            if(this.country && item.getCountry()!=null) {
                dto.setCountry(countryConverter.toDto(item.getCountry())) ;

            }
        if(this.properties && ListUtil.isNotEmpty(item.getProperties())){
            propertyConverter.init(true);
            propertyConverter.setCity(false);
            dto.setProperties(propertyConverter.toDto(item.getProperties()));
            propertyConverter.setCity(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.properties = value;
    }
    public void initObject(boolean value) {
        this.country = value;
    }
	
    public List<City> toItem(List<CityDto> dtos) {
        List<City> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (CityDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<CityDto> toDto(List<City> items) {
        List<CityDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (City item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(CityDto dto, City t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getCountry() == null  && dto.getCountry() != null){
            t.setCountry(new Country());
        }else if (t.getCountry() != null  && dto.getCountry() != null){
            t.setCountry(null);
            t.setCountry(new Country());
        }
        if (dto.getCountry() != null)
        countryConverter.copy(dto.getCountry(), t.getCountry());
        if (dto.getProperties() != null)
            t.setProperties(propertyConverter.copy(dto.getProperties()));
    }

    public List<City> copy(List<CityDto> dtos) {
        List<City> result = new ArrayList<>();
        if (dtos != null) {
            for (CityDto dto : dtos) {
                City instance = new City();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public TaskConverter getTaskConverter(){
        return this.taskConverter;
    }
    public void setTaskConverter(TaskConverter taskConverter ){
        this.taskConverter = taskConverter;
    }
    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public FinancialReportPropertyConverter getFinancialReportPropertyConverter(){
        return this.financialReportPropertyConverter;
    }
    public void setFinancialReportPropertyConverter(FinancialReportPropertyConverter financialReportPropertyConverter ){
        this.financialReportPropertyConverter = financialReportPropertyConverter;
    }
    public ReservationRequestConverter getReservationRequestConverter(){
        return this.reservationRequestConverter;
    }
    public void setReservationRequestConverter(ReservationRequestConverter reservationRequestConverter ){
        this.reservationRequestConverter = reservationRequestConverter;
    }
    public FinancialReportConverter getFinancialReportConverter(){
        return this.financialReportConverter;
    }
    public void setFinancialReportConverter(FinancialReportConverter financialReportConverter ){
        this.financialReportConverter = financialReportConverter;
    }
    public EnterpriseConverter getEnterpriseConverter(){
        return this.enterpriseConverter;
    }
    public void setEnterpriseConverter(EnterpriseConverter enterpriseConverter ){
        this.enterpriseConverter = enterpriseConverter;
    }
    public CountryConverter getCountryConverter(){
        return this.countryConverter;
    }
    public void setCountryConverter(CountryConverter countryConverter ){
        this.countryConverter = countryConverter;
    }
    public PropertyTypeConverter getPropertyTypeConverter(){
        return this.propertyTypeConverter;
    }
    public void setPropertyTypeConverter(PropertyTypeConverter propertyTypeConverter ){
        this.propertyTypeConverter = propertyTypeConverter;
    }
    public PropertyStatusConverter getPropertyStatusConverter(){
        return this.propertyStatusConverter;
    }
    public void setPropertyStatusConverter(PropertyStatusConverter propertyStatusConverter ){
        this.propertyStatusConverter = propertyStatusConverter;
    }
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
    }
    public ChargeConverter getChargeConverter(){
        return this.chargeConverter;
    }
    public void setChargeConverter(ChargeConverter chargeConverter ){
        this.chargeConverter = chargeConverter;
    }
    public boolean  isCountry(){
        return this.country;
    }
    public void  setCountry(boolean country){
        this.country = country;
    }
    public boolean  isProperties(){
        return this.properties ;
    }
    public void  setProperties(boolean properties ){
        this.properties  = properties ;
    }
}
