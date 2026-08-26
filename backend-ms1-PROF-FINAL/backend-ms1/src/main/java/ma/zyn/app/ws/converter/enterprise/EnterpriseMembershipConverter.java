package  ma.zyn.app.ws.converter.enterprise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.auth.CollaboratorPermissionOverrideConverter;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.auth.CollaboratorRoleConverter;
import ma.zyn.app.bean.core.auth.CollaboratorRole;

import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.enterprise.Enterprise;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.ws.dto.enterprise.EnterpriseMembershipDto;

@Component
public class EnterpriseMembershipConverter {

    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private CollaboratorPermissionOverrideConverter collaboratorPermissionOverrideConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private CollaboratorRoleConverter collaboratorRoleConverter ;
    private boolean collaborator;
    private boolean enterprise;
    private boolean collaboratorRole;
    private boolean collaboratorPermissionOverrides;

    public  EnterpriseMembershipConverter() {
        init(true);
    }

    public EnterpriseMembership toItem(EnterpriseMembershipDto dto) {
        if (dto == null) {
            return null;
        } else {
        EnterpriseMembership item = new EnterpriseMembership();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(dto.getCollaborator() != null && dto.getCollaborator().getId() != null){
                item.setCollaborator(new Collaborator());
                item.getCollaborator().setId(dto.getCollaborator().getId());
                item.getCollaborator().setName(dto.getCollaborator().getName());
            }

            if(dto.getEnterprise() != null && dto.getEnterprise().getId() != null){
                item.setEnterprise(new Enterprise());
                item.getEnterprise().setId(dto.getEnterprise().getId());
                item.getEnterprise().setName(dto.getEnterprise().getName());
            }

            if(dto.getCollaboratorRole() != null && dto.getCollaboratorRole().getId() != null){
                item.setCollaboratorRole(new CollaboratorRole());
                item.getCollaboratorRole().setId(dto.getCollaboratorRole().getId());
                item.getCollaboratorRole().setLabel(dto.getCollaboratorRole().getLabel());
            }


            if(this.collaboratorPermissionOverrides && ListUtil.isNotEmpty(dto.getCollaboratorPermissionOverrides()))
                item.setCollaboratorPermissionOverrides(collaboratorPermissionOverrideConverter.toItem(dto.getCollaboratorPermissionOverrides()));


        return item;
        }
    }


    public EnterpriseMembershipDto toDto(EnterpriseMembership item) {
        if (item == null) {
            return null;
        } else {
            EnterpriseMembershipDto dto = new EnterpriseMembershipDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(this.collaborator && item.getCollaborator()!=null) {
                dto.setCollaborator(collaboratorConverter.toDto(item.getCollaborator())) ;

            }
            if(this.enterprise && item.getEnterprise()!=null) {
                dto.setEnterprise(enterpriseConverter.toDto(item.getEnterprise())) ;

            }
            if(this.collaboratorRole && item.getCollaboratorRole()!=null) {
                dto.setCollaboratorRole(collaboratorRoleConverter.toDto(item.getCollaboratorRole())) ;

            }
        if(this.collaboratorPermissionOverrides && ListUtil.isNotEmpty(item.getCollaboratorPermissionOverrides())){
            collaboratorPermissionOverrideConverter.init(true);
            collaboratorPermissionOverrideConverter.setEnterpriseMembership(false);
            dto.setCollaboratorPermissionOverrides(collaboratorPermissionOverrideConverter.toDto(item.getCollaboratorPermissionOverrides()));
            collaboratorPermissionOverrideConverter.setEnterpriseMembership(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.collaboratorPermissionOverrides = value;
    }
    public void initObject(boolean value) {
        this.collaborator = value;
        this.enterprise = value;
        this.collaboratorRole = value;
    }
	
    public List<EnterpriseMembership> toItem(List<EnterpriseMembershipDto> dtos) {
        List<EnterpriseMembership> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (EnterpriseMembershipDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<EnterpriseMembershipDto> toDto(List<EnterpriseMembership> items) {
        List<EnterpriseMembershipDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (EnterpriseMembership item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(EnterpriseMembershipDto dto, EnterpriseMembership t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getCollaborator() == null  && dto.getCollaborator() != null){
            t.setCollaborator(new Collaborator());
        }else if (t.getCollaborator() != null  && dto.getCollaborator() != null){
            t.setCollaborator(null);
            t.setCollaborator(new Collaborator());
        }
        if(t.getEnterprise() == null  && dto.getEnterprise() != null){
            t.setEnterprise(new Enterprise());
        }else if (t.getEnterprise() != null  && dto.getEnterprise() != null){
            t.setEnterprise(null);
            t.setEnterprise(new Enterprise());
        }
        if(t.getCollaboratorRole() == null  && dto.getCollaboratorRole() != null){
            t.setCollaboratorRole(new CollaboratorRole());
        }else if (t.getCollaboratorRole() != null  && dto.getCollaboratorRole() != null){
            t.setCollaboratorRole(null);
            t.setCollaboratorRole(new CollaboratorRole());
        }
        if (dto.getCollaborator() != null)
        collaboratorConverter.copy(dto.getCollaborator(), t.getCollaborator());
        if (dto.getEnterprise() != null)
        enterpriseConverter.copy(dto.getEnterprise(), t.getEnterprise());
        if (dto.getCollaboratorRole() != null)
        collaboratorRoleConverter.copy(dto.getCollaboratorRole(), t.getCollaboratorRole());
        if (dto.getCollaboratorPermissionOverrides() != null)
            t.setCollaboratorPermissionOverrides(collaboratorPermissionOverrideConverter.copy(dto.getCollaboratorPermissionOverrides()));
    }

    public List<EnterpriseMembership> copy(List<EnterpriseMembershipDto> dtos) {
        List<EnterpriseMembership> result = new ArrayList<>();
        if (dtos != null) {
            for (EnterpriseMembershipDto dto : dtos) {
                EnterpriseMembership instance = new EnterpriseMembership();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public CollaboratorConverter getCollaboratorConverter(){
        return this.collaboratorConverter;
    }
    public void setCollaboratorConverter(CollaboratorConverter collaboratorConverter ){
        this.collaboratorConverter = collaboratorConverter;
    }
    public CollaboratorPermissionOverrideConverter getCollaboratorPermissionOverrideConverter(){
        return this.collaboratorPermissionOverrideConverter;
    }
    public void setCollaboratorPermissionOverrideConverter(CollaboratorPermissionOverrideConverter collaboratorPermissionOverrideConverter ){
        this.collaboratorPermissionOverrideConverter = collaboratorPermissionOverrideConverter;
    }
    public EnterpriseConverter getEnterpriseConverter(){
        return this.enterpriseConverter;
    }
    public void setEnterpriseConverter(EnterpriseConverter enterpriseConverter ){
        this.enterpriseConverter = enterpriseConverter;
    }
    public CollaboratorRoleConverter getCollaboratorRoleConverter(){
        return this.collaboratorRoleConverter;
    }
    public void setCollaboratorRoleConverter(CollaboratorRoleConverter collaboratorRoleConverter ){
        this.collaboratorRoleConverter = collaboratorRoleConverter;
    }
    public boolean  isCollaborator(){
        return this.collaborator;
    }
    public void  setCollaborator(boolean collaborator){
        this.collaborator = collaborator;
    }
    public boolean  isEnterprise(){
        return this.enterprise;
    }
    public void  setEnterprise(boolean enterprise){
        this.enterprise = enterprise;
    }
    public boolean  isCollaboratorRole(){
        return this.collaboratorRole;
    }
    public void  setCollaboratorRole(boolean collaboratorRole){
        this.collaboratorRole = collaboratorRole;
    }
    public boolean  isCollaboratorPermissionOverrides(){
        return this.collaboratorPermissionOverrides ;
    }
    public void  setCollaboratorPermissionOverrides(boolean collaboratorPermissionOverrides ){
        this.collaboratorPermissionOverrides  = collaboratorPermissionOverrides ;
    }
}
