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
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.ws.dto.property.PropertyStatusDto;

@Component
public class PropertyStatusConverter {



    public PropertyStatus toItem(PropertyStatusDto dto) {
        if (dto == null) {
            return null;
        } else {
        PropertyStatus item = new PropertyStatus();
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


    public PropertyStatusDto toDto(PropertyStatus item) {
        if (item == null) {
            return null;
        } else {
            PropertyStatusDto dto = new PropertyStatusDto();
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


	
    public List<PropertyStatus> toItem(List<PropertyStatusDto> dtos) {
        List<PropertyStatus> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (PropertyStatusDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<PropertyStatusDto> toDto(List<PropertyStatus> items) {
        List<PropertyStatusDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (PropertyStatus item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(PropertyStatusDto dto, PropertyStatus t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<PropertyStatus> copy(List<PropertyStatusDto> dtos) {
        List<PropertyStatus> result = new ArrayList<>();
        if (dtos != null) {
            for (PropertyStatusDto dto : dtos) {
                PropertyStatus instance = new PropertyStatus();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
