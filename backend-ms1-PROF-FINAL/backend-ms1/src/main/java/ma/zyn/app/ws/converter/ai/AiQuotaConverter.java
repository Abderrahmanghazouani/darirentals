package  ma.zyn.app.ws.converter.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;

import ma.zyn.app.bean.core.enterprise.Enterprise;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.ws.dto.ai.AiQuotaDto;

@Component
public class AiQuotaConverter {

    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    private boolean enterprise;

    public  AiQuotaConverter() {
        initObject(true);
    }

    public AiQuota toItem(AiQuotaDto dto) {
        if (dto == null) {
            return null;
        } else {
        AiQuota item = new AiQuota();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getTokensAllocated()))
                item.setTokensAllocated(dto.getTokensAllocated());
            if(dto.getEnterprise() != null && dto.getEnterprise().getId() != null){
                item.setEnterprise(new Enterprise());
                item.getEnterprise().setId(dto.getEnterprise().getId());
                item.getEnterprise().setName(dto.getEnterprise().getName());
            }




        return item;
        }
    }


    public AiQuotaDto toDto(AiQuota item) {
        if (item == null) {
            return null;
        } else {
            AiQuotaDto dto = new AiQuotaDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getTokensAllocated()))
                dto.setTokensAllocated(item.getTokensAllocated());
            if(this.enterprise && item.getEnterprise()!=null) {
                dto.setEnterprise(enterpriseConverter.toDto(item.getEnterprise())) ;

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.enterprise = value;
    }
	
    public List<AiQuota> toItem(List<AiQuotaDto> dtos) {
        List<AiQuota> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (AiQuotaDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<AiQuotaDto> toDto(List<AiQuota> items) {
        List<AiQuotaDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (AiQuota item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(AiQuotaDto dto, AiQuota t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getEnterprise() == null  && dto.getEnterprise() != null){
            t.setEnterprise(new Enterprise());
        }else if (t.getEnterprise() != null  && dto.getEnterprise() != null){
            t.setEnterprise(null);
            t.setEnterprise(new Enterprise());
        }
        if (dto.getEnterprise() != null)
        enterpriseConverter.copy(dto.getEnterprise(), t.getEnterprise());
    }

    public List<AiQuota> copy(List<AiQuotaDto> dtos) {
        List<AiQuota> result = new ArrayList<>();
        if (dtos != null) {
            for (AiQuotaDto dto : dtos) {
                AiQuota instance = new AiQuota();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public EnterpriseConverter getEnterpriseConverter(){
        return this.enterpriseConverter;
    }
    public void setEnterpriseConverter(EnterpriseConverter enterpriseConverter ){
        this.enterpriseConverter = enterpriseConverter;
    }
    public boolean  isEnterprise(){
        return this.enterprise;
    }
    public void  setEnterprise(boolean enterprise){
        this.enterprise = enterprise;
    }
}
