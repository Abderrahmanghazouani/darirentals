package  ma.zyn.app.ws.converter.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;




import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.ws.dto.property.PropertyTypeDto;

@Component
public class PropertyTypeConverter {



    public PropertyType toItem(PropertyTypeDto dto) {
        if (dto == null) {
            return null;
        } else {
        PropertyType item = new PropertyType();
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


    public PropertyTypeDto toDto(PropertyType item) {
        if (item == null) {
            return null;
        } else {
            PropertyTypeDto dto = new PropertyTypeDto();
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


	
    public List<PropertyType> toItem(List<PropertyTypeDto> dtos) {
        List<PropertyType> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (PropertyTypeDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<PropertyTypeDto> toDto(List<PropertyType> items) {
        List<PropertyTypeDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (PropertyType item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(PropertyTypeDto dto, PropertyType t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<PropertyType> copy(List<PropertyTypeDto> dtos) {
        List<PropertyType> result = new ArrayList<>();
        if (dtos != null) {
            for (PropertyTypeDto dto : dtos) {
                PropertyType instance = new PropertyType();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
