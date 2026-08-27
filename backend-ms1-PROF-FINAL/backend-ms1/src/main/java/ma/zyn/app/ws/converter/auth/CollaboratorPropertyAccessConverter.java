package  ma.zyn.app.ws.converter.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.bean.core.auth.CollaboratorPropertyAccess;
import ma.zyn.app.ws.dto.auth.CollaboratorPropertyAccessDto;

@Component
public class CollaboratorPropertyAccessConverter {

    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    private boolean collaborator;
    private boolean property;

    public  CollaboratorPropertyAccessConverter() {
        initObject(true);
    }

    public CollaboratorPropertyAccess toItem(CollaboratorPropertyAccessDto dto) {
        if (dto == null) {
            return null;
        } else {
        CollaboratorPropertyAccess item = new CollaboratorPropertyAccess();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(dto.getCollaborator() != null && dto.getCollaborator().getId() != null){
                item.setCollaborator(new Collaborator());
                item.getCollaborator().setId(dto.getCollaborator().getId());
                item.getCollaborator().setName(dto.getCollaborator().getName());
            }

            if(dto.getProperty() != null && dto.getProperty().getId() != null){
                item.setProperty(new Property());
                item.getProperty().setId(dto.getProperty().getId());
                item.getProperty().setName(dto.getProperty().getName());
            }




        return item;
        }
    }


    public CollaboratorPropertyAccessDto toDto(CollaboratorPropertyAccess item) {
        if (item == null) {
            return null;
        } else {
            CollaboratorPropertyAccessDto dto = new CollaboratorPropertyAccessDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(this.collaborator && item.getCollaborator()!=null) {
                dto.setCollaborator(collaboratorConverter.toDto(item.getCollaborator())) ;

            }
            if(this.property && item.getProperty()!=null) {
                dto.setProperty(propertyConverter.toDto(item.getProperty())) ;

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.collaborator = value;
        this.property = value;
    }

    public List<CollaboratorPropertyAccess> toItem(List<CollaboratorPropertyAccessDto> dtos) {
        List<CollaboratorPropertyAccess> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (CollaboratorPropertyAccessDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<CollaboratorPropertyAccessDto> toDto(List<CollaboratorPropertyAccess> items) {
        List<CollaboratorPropertyAccessDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (CollaboratorPropertyAccess item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(CollaboratorPropertyAccessDto dto, CollaboratorPropertyAccess t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getCollaborator() == null  && dto.getCollaborator() != null){
            t.setCollaborator(new Collaborator());
        }else if (t.getCollaborator() != null  && dto.getCollaborator() != null){
            t.setCollaborator(null);
            t.setCollaborator(new Collaborator());
        }
        if(t.getProperty() == null  && dto.getProperty() != null){
            t.setProperty(new Property());
        }else if (t.getProperty() != null  && dto.getProperty() != null){
            t.setProperty(null);
            t.setProperty(new Property());
        }
        if (dto.getCollaborator() != null)
        collaboratorConverter.copy(dto.getCollaborator(), t.getCollaborator());
        if (dto.getProperty() != null)
        propertyConverter.copy(dto.getProperty(), t.getProperty());
    }

    public List<CollaboratorPropertyAccess> copy(List<CollaboratorPropertyAccessDto> dtos) {
        List<CollaboratorPropertyAccess> result = new ArrayList<>();
        if (dtos != null) {
            for (CollaboratorPropertyAccessDto dto : dtos) {
                CollaboratorPropertyAccess instance = new CollaboratorPropertyAccess();
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
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
    }
    public boolean  isCollaborator(){
        return this.collaborator;
    }
    public void  setCollaborator(boolean collaborator){
        this.collaborator = collaborator;
    }
    public boolean  isProperty(){
        return this.property;
    }
    public void  setProperty(boolean property){
        this.property = property;
    }
}
