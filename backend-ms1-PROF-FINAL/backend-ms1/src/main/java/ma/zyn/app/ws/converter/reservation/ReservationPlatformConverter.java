package  ma.zyn.app.ws.converter.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;




import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.ws.dto.reservation.ReservationPlatformDto;

@Component
public class ReservationPlatformConverter {



    public ReservationPlatform toItem(ReservationPlatformDto dto) {
        if (dto == null) {
            return null;
        } else {
        ReservationPlatform item = new ReservationPlatform();
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


    public ReservationPlatformDto toDto(ReservationPlatform item) {
        if (item == null) {
            return null;
        } else {
            ReservationPlatformDto dto = new ReservationPlatformDto();
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


	
    public List<ReservationPlatform> toItem(List<ReservationPlatformDto> dtos) {
        List<ReservationPlatform> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ReservationPlatformDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ReservationPlatformDto> toDto(List<ReservationPlatform> items) {
        List<ReservationPlatformDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (ReservationPlatform item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ReservationPlatformDto dto, ReservationPlatform t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<ReservationPlatform> copy(List<ReservationPlatformDto> dtos) {
        List<ReservationPlatform> result = new ArrayList<>();
        if (dtos != null) {
            for (ReservationPlatformDto dto : dtos) {
                ReservationPlatform instance = new ReservationPlatform();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
