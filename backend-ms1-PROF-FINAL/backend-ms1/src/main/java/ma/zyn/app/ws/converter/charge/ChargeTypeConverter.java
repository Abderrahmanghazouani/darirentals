package  ma.zyn.app.ws.converter.charge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;




import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.ws.dto.charge.ChargeTypeDto;

@Component
public class ChargeTypeConverter {



    public ChargeType toItem(ChargeTypeDto dto) {
        if (dto == null) {
            return null;
        } else {
        ChargeType item = new ChargeType();
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


    public ChargeTypeDto toDto(ChargeType item) {
        if (item == null) {
            return null;
        } else {
            ChargeTypeDto dto = new ChargeTypeDto();
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


	
    public List<ChargeType> toItem(List<ChargeTypeDto> dtos) {
        List<ChargeType> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ChargeTypeDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ChargeTypeDto> toDto(List<ChargeType> items) {
        List<ChargeTypeDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (ChargeType item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ChargeTypeDto dto, ChargeType t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<ChargeType> copy(List<ChargeTypeDto> dtos) {
        List<ChargeType> result = new ArrayList<>();
        if (dtos != null) {
            for (ChargeTypeDto dto : dtos) {
                ChargeType instance = new ChargeType();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
