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
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.ws.dto.reservation.ReservationRequestStatusDto;

@Component
public class ReservationRequestStatusConverter {



    public ReservationRequestStatus toItem(ReservationRequestStatusDto dto) {
        if (dto == null) {
            return null;
        } else {
        ReservationRequestStatus item = new ReservationRequestStatus();
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


    public ReservationRequestStatusDto toDto(ReservationRequestStatus item) {
        if (item == null) {
            return null;
        } else {
            ReservationRequestStatusDto dto = new ReservationRequestStatusDto();
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


	
    public List<ReservationRequestStatus> toItem(List<ReservationRequestStatusDto> dtos) {
        List<ReservationRequestStatus> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (ReservationRequestStatusDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<ReservationRequestStatusDto> toDto(List<ReservationRequestStatus> items) {
        List<ReservationRequestStatusDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (ReservationRequestStatus item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(ReservationRequestStatusDto dto, ReservationRequestStatus t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<ReservationRequestStatus> copy(List<ReservationRequestStatusDto> dtos) {
        List<ReservationRequestStatus> result = new ArrayList<>();
        if (dtos != null) {
            for (ReservationRequestStatusDto dto : dtos) {
                ReservationRequestStatus instance = new ReservationRequestStatus();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
