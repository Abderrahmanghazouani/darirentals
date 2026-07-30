package  ma.zyn.app.ws.converter.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.enterprise.EnterpriseMembershipConverter;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;

import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.ws.dto.auth.CollaboratorPermissionOverrideDto;

@Component
public class CollaboratorPermissionOverrideConverter {

    @Autowired
    private EnterpriseMembershipConverter enterpriseMembershipConverter ;
    private boolean enterpriseMembership;

    public  CollaboratorPermissionOverrideConverter() {
        initObject(true);
    }

    public CollaboratorPermissionOverride toItem(CollaboratorPermissionOverrideDto dto) {
        if (dto == null) {
            return null;
        } else {
        CollaboratorPermissionOverride item = new CollaboratorPermissionOverride();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
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
            if(dto.getEnterpriseMembership() != null && dto.getEnterpriseMembership().getId() != null){
                item.setEnterpriseMembership(new EnterpriseMembership());
                item.getEnterpriseMembership().setId(dto.getEnterpriseMembership().getId());
                item.getEnterpriseMembership().setId(dto.getEnterpriseMembership().getId());
            }




        return item;
        }
    }


    public CollaboratorPermissionOverrideDto toDto(CollaboratorPermissionOverride item) {
        if (item == null) {
            return null;
        } else {
            CollaboratorPermissionOverrideDto dto = new CollaboratorPermissionOverrideDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
                dto.setCanManageFinancials(item.getCanManageFinancials());
                dto.setCanManageUsers(item.getCanManageUsers());
                dto.setCanDeleteProperty(item.getCanDeleteProperty());
                dto.setCanManageServiceProviders(item.getCanManageServiceProviders());
                dto.setCanManageAiUsage(item.getCanManageAiUsage());
            if(this.enterpriseMembership && item.getEnterpriseMembership()!=null) {
                dto.setEnterpriseMembership(enterpriseMembershipConverter.toDto(item.getEnterpriseMembership())) ;

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.enterpriseMembership = value;
    }
	
    public List<CollaboratorPermissionOverride> toItem(List<CollaboratorPermissionOverrideDto> dtos) {
        List<CollaboratorPermissionOverride> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (CollaboratorPermissionOverrideDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<CollaboratorPermissionOverrideDto> toDto(List<CollaboratorPermissionOverride> items) {
        List<CollaboratorPermissionOverrideDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (CollaboratorPermissionOverride item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(CollaboratorPermissionOverrideDto dto, CollaboratorPermissionOverride t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getEnterpriseMembership() == null  && dto.getEnterpriseMembership() != null){
            t.setEnterpriseMembership(new EnterpriseMembership());
        }else if (t.getEnterpriseMembership() != null  && dto.getEnterpriseMembership() != null){
            t.setEnterpriseMembership(null);
            t.setEnterpriseMembership(new EnterpriseMembership());
        }
        if (dto.getEnterpriseMembership() != null)
        enterpriseMembershipConverter.copy(dto.getEnterpriseMembership(), t.getEnterpriseMembership());
    }

    public List<CollaboratorPermissionOverride> copy(List<CollaboratorPermissionOverrideDto> dtos) {
        List<CollaboratorPermissionOverride> result = new ArrayList<>();
        if (dtos != null) {
            for (CollaboratorPermissionOverrideDto dto : dtos) {
                CollaboratorPermissionOverride instance = new CollaboratorPermissionOverride();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public EnterpriseMembershipConverter getEnterpriseMembershipConverter(){
        return this.enterpriseMembershipConverter;
    }
    public void setEnterpriseMembershipConverter(EnterpriseMembershipConverter enterpriseMembershipConverter ){
        this.enterpriseMembershipConverter = enterpriseMembershipConverter;
    }
    public boolean  isEnterpriseMembership(){
        return this.enterpriseMembership;
    }
    public void  setEnterpriseMembership(boolean enterpriseMembership){
        this.enterpriseMembership = enterpriseMembership;
    }
}
