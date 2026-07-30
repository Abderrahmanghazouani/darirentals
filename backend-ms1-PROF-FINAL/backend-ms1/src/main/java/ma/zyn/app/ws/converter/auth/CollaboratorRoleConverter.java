package  ma.zyn.app.ws.converter.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;




import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.ws.dto.auth.CollaboratorRoleDto;

@Component
public class CollaboratorRoleConverter {



    public CollaboratorRole toItem(CollaboratorRoleDto dto) {
        if (dto == null) {
            return null;
        } else {
        CollaboratorRole item = new CollaboratorRole();
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
            if(dto.getCanManageFinancials() != null)
                item.setCanManageFinancials(dto.getCanManageFinancials());
            if(dto.getCanManageUsers() != null)
                item.setCanManageUsers(dto.getCanManageUsers());
            if(dto.getCanDeleteProperty() != null)
                item.setCanDeleteProperty(dto.getCanDeleteProperty());
            if(dto.getCanManageServiceProviders() != null)
                item.setCanManageServiceProviders(dto.getCanManageServiceProviders());
            if(dto.getCanManageAiUsage() != null)
                item.setCanManageAiUsage(dto.getCanManageAiUsage());



        return item;
        }
    }


    public CollaboratorRoleDto toDto(CollaboratorRole item) {
        if (item == null) {
            return null;
        } else {
            CollaboratorRoleDto dto = new CollaboratorRoleDto();
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
                dto.setCanManageFinancials(item.getCanManageFinancials());
                dto.setCanManageUsers(item.getCanManageUsers());
                dto.setCanDeleteProperty(item.getCanDeleteProperty());
                dto.setCanManageServiceProviders(item.getCanManageServiceProviders());
                dto.setCanManageAiUsage(item.getCanManageAiUsage());


        return dto;
        }
    }


	
    public List<CollaboratorRole> toItem(List<CollaboratorRoleDto> dtos) {
        List<CollaboratorRole> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (CollaboratorRoleDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<CollaboratorRoleDto> toDto(List<CollaboratorRole> items) {
        List<CollaboratorRoleDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (CollaboratorRole item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(CollaboratorRoleDto dto, CollaboratorRole t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
    }

    public List<CollaboratorRole> copy(List<CollaboratorRoleDto> dtos) {
        List<CollaboratorRole> result = new ArrayList<>();
        if (dtos != null) {
            for (CollaboratorRoleDto dto : dtos) {
                CollaboratorRole instance = new CollaboratorRole();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


}
