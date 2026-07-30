package  ma.zyn.app.ws.converter.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.currency.CurrencyConverter;
import ma.zyn.app.bean.core.currency.Currency;

import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.bean.core.currency.Currency;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.ws.dto.currency.ExchangeRateDto;

@Component
public class ExchangeRateConverter {

    @Autowired
    private CurrencyConverter currencyConverter ;
    private boolean baseCurrency;
    private boolean targetCurrency;

    public  ExchangeRateConverter() {
        initObject(true);
    }

    public ExchangeRate toItem(ExchangeRateDto dto) {
        if (dto == null) {
            return null;
        } else {
        ExchangeRate item = new ExchangeRate();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getRate()))
                item.setRate(dto.getRate());
            if(StringUtil.isNotEmpty(dto.getSource()))
                item.setSource(dto.getSource());
            if(dto.getBaseCurrency() != null && dto.getBaseCurrency().getId() != null){
                item.setBaseCurrency(new Currency());
                item.getBaseCurrency().setId(dto.getBaseCurrency().getId());
                item.getBaseCurrency().setLabel(dto.getBaseCurrency().getLabel());
            }

            if(dto.getTargetCurrency() != null && dto.getTargetCurrency().getId() != null){
                item.setTargetCurrency(new Currency());
                item.getTargetCurrency().setId(dto.getTargetCurrency().getId());
                item.getTargetCurrency().setLabel(dto.getTargetCurrency().getLabel());
            }




        return item;
        }
    }


    public ExchangeRateDto toDto(ExchangeRate item) {
        if (item == null) {
            return null;
        } else {
            ExchangeRateDto dto = new ExchangeRateDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getRate()))
                dto.setRate(item.getRate());
            if(StringUtil.isNotEmpty(item.getSource()))
                dto.setSource(item.getSource());
            if(this.baseCurrency && item.getBaseCurrency()!=null) {
                dto.setBaseCurrency(currencyConverter.toDto(item.getBaseCurrency())) ;

            }
            if(this.targetCurrency && item.getTargetCurrency()!=null) {
                dto.setTargetCurrency(currencyConverter.toDto(item.getTargetCurrency())) ;

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.baseCurrency = value;
        this.targetCurrency = value;
    }
	
    public List<ExchangeRate> toItem(List<ExchangeRateDto> dtos) {
        List<ExchangeRate> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ExchangeRateDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ExchangeRateDto> toDto(List<ExchangeRate> items) {
        List<ExchangeRateDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (ExchangeRate item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ExchangeRateDto dto, ExchangeRate t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getBaseCurrency() == null  && dto.getBaseCurrency() != null){
            t.setBaseCurrency(new Currency());
        }else if (t.getBaseCurrency() != null  && dto.getBaseCurrency() != null){
            t.setBaseCurrency(null);
            t.setBaseCurrency(new Currency());
        }
        if(t.getTargetCurrency() == null  && dto.getTargetCurrency() != null){
            t.setTargetCurrency(new Currency());
        }else if (t.getTargetCurrency() != null  && dto.getTargetCurrency() != null){
            t.setTargetCurrency(null);
            t.setTargetCurrency(new Currency());
        }
        if (dto.getBaseCurrency() != null)
        currencyConverter.copy(dto.getBaseCurrency(), t.getBaseCurrency());
        if (dto.getTargetCurrency() != null)
        currencyConverter.copy(dto.getTargetCurrency(), t.getTargetCurrency());
    }

    public List<ExchangeRate> copy(List<ExchangeRateDto> dtos) {
        List<ExchangeRate> result = new ArrayList<>();
        if (dtos != null) {
            for (ExchangeRateDto dto : dtos) {
                ExchangeRate instance = new ExchangeRate();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public CurrencyConverter getCurrencyConverter(){
        return this.currencyConverter;
    }
    public void setCurrencyConverter(CurrencyConverter currencyConverter ){
        this.currencyConverter = currencyConverter;
    }
    public boolean  isBaseCurrency(){
        return this.baseCurrency;
    }
    public void  setBaseCurrency(boolean baseCurrency){
        this.baseCurrency = baseCurrency;
    }
    public boolean  isTargetCurrency(){
        return this.targetCurrency;
    }
    public void  setTargetCurrency(boolean targetCurrency){
        this.targetCurrency = targetCurrency;
    }
}
