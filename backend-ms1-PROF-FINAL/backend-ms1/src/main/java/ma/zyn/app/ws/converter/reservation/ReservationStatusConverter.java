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
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.ws.dto.reservation.ReservationStatusDto;

@Component
public class ReservationStatusConverter {



    public ReservationStatus toItem(ReservationStatusDto dto) {
        if (dto == null) {
            return null;
        } else {
        ReservationStatus item = new ReservationStatus();
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


    public ReservationStatusDto toDto(ReservationStatus item) {
        if (item == null) {
            return null;
        } else {
            ReservationStatusDto dto = new ReservationStatusDto();
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


	
    public List<ReservationStatus> toItem(List<ReservationStatusDto> dtos) {
        List<ReservationStatus> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ReservationStatusDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ReservationStatusDto> toDto(List<ReservationStatus> items) {
        List<ReservationStatusDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (ReservationStatus item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ReservationStatusDto dto, ReservationStatus t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<ReservationStatus> copy(List<ReservationStatusDto> dtos) {
        List<ReservationStatus> result = new ArrayList<>();
        if (dtos != null) {
            for (ReservationStatusDto dto : dtos) {
                ReservationStatus instance = new ReservationStatus();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
