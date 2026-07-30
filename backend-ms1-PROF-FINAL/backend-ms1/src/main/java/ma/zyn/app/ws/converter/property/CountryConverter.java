package  ma.zyn.app.ws.converter.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.property.CityConverter;
import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;



import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.property.Country;
import ma.zyn.app.ws.dto.property.CountryDto;

@Component
public class CountryConverter {

    @Autowired
    private CityConverter cityConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    private boolean cities;

    public  CountryConverter() {
        initList(true);
    }

    public Country toItem(CountryDto dto) {
        if (dto == null) {
            return null;
        } else {
        Country item = new Country();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getName()))
                item.setName(dto.getName());
            if(StringUtil.isNotEmpty(dto.getCode()))
                item.setCode(dto.getCode());

            if(this.cities && ListUtil.isNotEmpty(dto.getCities()))
                item.setCities(cityConverter.toItem(dto.getCities()));


        return item;
        }
    }


    public CountryDto toDto(Country item) {
        if (item == null) {
            return null;
        } else {
            CountryDto dto = new CountryDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getName()))
                dto.setName(item.getName());
            if(StringUtil.isNotEmpty(item.getCode()))
                dto.setCode(item.getCode());
        if(this.cities && ListUtil.isNotEmpty(item.getCities())){
            cityConverter.init(true);
            cityConverter.setCountry(false);
            dto.setCities(cityConverter.toDto(item.getCities()));
            cityConverter.setCountry(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.cities = value;
    }
	
    public List<Country> toItem(List<CountryDto> dtos) {
        List<Country> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (CountryDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<CountryDto> toDto(List<Country> items) {
        List<CountryDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Country item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(CountryDto dto, Country t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if (dto.getCities() != null)
            t.setCities(cityConverter.copy(dto.getCities()));
    }

    public List<Country> copy(List<CountryDto> dtos) {
        List<Country> result = new ArrayList<>();
        if (dtos != null) {
            for (CountryDto dto : dtos) {
                Country instance = new Country();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public CityConverter getCityConverter(){
        return this.cityConverter;
    }
    public void setCityConverter(CityConverter cityConverter ){
        this.cityConverter = cityConverter;
    }
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
    }
    public boolean  isCities(){
        return this.cities ;
    }
    public void  setCities(boolean cities ){
        this.cities  = cities ;
    }
}
