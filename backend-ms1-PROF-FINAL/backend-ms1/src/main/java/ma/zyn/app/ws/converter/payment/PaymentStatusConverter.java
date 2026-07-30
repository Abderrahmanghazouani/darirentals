package  ma.zyn.app.ws.converter.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;




import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.ws.dto.payment.PaymentStatusDto;

@Component
public class PaymentStatusConverter {



    public PaymentStatus toItem(PaymentStatusDto dto) {
        if (dto == null) {
            return null;
        } else {
        PaymentStatus item = new PaymentStatus();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getDescription()))
                item.setDescription(dto.getDescription());
            if(StringUtil.isNotEmpty(dto.getCode()))
                item.setCode(dto.getCode());
            if(StringUtil.isNotEmpty(dto.getLabel()))
                item.setLabel(dto.getLabel());
            if(StringUtil.isNotEmpty(dto.getStyle()))
                item.setStyle(dto.getStyle());
            if(dto.getIsDefault() != null)
                item.setIsDefault(dto.getIsDefault());
            if(StringUtil.isNotEmpty(dto.getSortOrder()))
                item.setSortOrder(dto.getSortOrder());



        return item;
        }
    }


    public PaymentStatusDto toDto(PaymentStatus item) {
        if (item == null) {
            return null;
        } else {
            PaymentStatusDto dto = new PaymentStatusDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getDescription()))
                dto.setDescription(item.getDescription());
            if(StringUtil.isNotEmpty(item.getCode()))
                dto.setCode(item.getCode());
            if(StringUtil.isNotEmpty(item.getLabel()))
                dto.setLabel(item.getLabel());
            if(StringUtil.isNotEmpty(item.getStyle()))
                dto.setStyle(item.getStyle());
                dto.setIsDefault(item.getIsDefault());
            if(StringUtil.isNotEmpty(item.getSortOrder()))
                dto.setSortOrder(item.getSortOrder());


        return dto;
        }
    }


	
    public List<PaymentStatus> toItem(List<PaymentStatusDto> dtos) {
        List<PaymentStatus> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (PaymentStatusDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<PaymentStatusDto> toDto(List<PaymentStatus> items) {
        List<PaymentStatusDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (PaymentStatus item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(PaymentStatusDto dto, PaymentStatus t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<PaymentStatus> copy(List<PaymentStatusDto> dtos) {
        List<PaymentStatus> result = new ArrayList<>();
        if (dtos != null) {
            for (PaymentStatusDto dto : dtos) {
                PaymentStatus instance = new PaymentStatus();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
