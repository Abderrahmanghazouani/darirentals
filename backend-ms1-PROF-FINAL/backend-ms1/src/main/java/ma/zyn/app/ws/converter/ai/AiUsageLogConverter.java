package  ma.zyn.app.ws.converter.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.ai.AiUsageTypeConverter;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.document.DocumentConverter;
import ma.zyn.app.bean.core.document.Document;

import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.auth.Collaborator;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.ws.dto.ai.AiUsageLogDto;

@Component
public class AiUsageLogConverter {

    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private AiUsageTypeConverter aiUsageTypeConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private DocumentConverter documentConverter ;
    private boolean enterprise;
    private boolean aiUsageType;
    private boolean collaborator;
    private boolean document;

    public  AiUsageLogConverter() {
        initObject(true);
    }

    public AiUsageLog toItem(AiUsageLogDto dto) {
        if (dto == null) {
            return null;
        } else {
        AiUsageLog item = new AiUsageLog();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getTokensUsed()))
                item.setTokensUsed(dto.getTokensUsed());
            if(StringUtil.isNotEmpty(dto.getDate()))
                item.setDate(DateUtil.stringEnToDate(dto.getDate()));
            if(dto.getEnterprise() != null && dto.getEnterprise().getId() != null){
                item.setEnterprise(new Enterprise());
                item.getEnterprise().setId(dto.getEnterprise().getId());
                item.getEnterprise().setName(dto.getEnterprise().getName());
            }

            if(this.aiUsageType && dto.getAiUsageType()!=null)
                item.setAiUsageType(aiUsageTypeConverter.toItem(dto.getAiUsageType())) ;

            if(dto.getCollaborator() != null && dto.getCollaborator().getId() != null){
                item.setCollaborator(new Collaborator());
                item.getCollaborator().setId(dto.getCollaborator().getId());
                item.getCollaborator().setName(dto.getCollaborator().getName());
            }

            if(this.document && dto.getDocument()!=null)
                item.setDocument(documentConverter.toItem(dto.getDocument())) ;




        return item;
        }
    }


    public AiUsageLogDto toDto(AiUsageLog item) {
        if (item == null) {
            return null;
        } else {
            AiUsageLogDto dto = new AiUsageLogDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getTokensUsed()))
                dto.setTokensUsed(item.getTokensUsed());
            if(item.getDate()!=null)
                dto.setDate(DateUtil.dateTimeToString(item.getDate()));
            if(this.enterprise && item.getEnterprise()!=null) {
                dto.setEnterprise(enterpriseConverter.toDto(item.getEnterprise())) ;

            }
            if(this.aiUsageType && item.getAiUsageType()!=null) {
                dto.setAiUsageType(aiUsageTypeConverter.toDto(item.getAiUsageType())) ;

            }
            if(this.collaborator && item.getCollaborator()!=null) {
                dto.setCollaborator(collaboratorConverter.toDto(item.getCollaborator())) ;

            }
            if(this.document && item.getDocument()!=null) {
                dto.setDocument(documentConverter.toDto(item.getDocument())) ;

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.enterprise = value;
        this.aiUsageType = value;
        this.collaborator = value;
        this.document = value;
    }
	
    public List<AiUsageLog> toItem(List<AiUsageLogDto> dtos) {
        List<AiUsageLog> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (AiUsageLogDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<AiUsageLogDto> toDto(List<AiUsageLog> items) {
        List<AiUsageLogDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (AiUsageLog item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(AiUsageLogDto dto, AiUsageLog t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getEnterprise() == null  && dto.getEnterprise() != null){
            t.setEnterprise(new Enterprise());
        }else if (t.getEnterprise() != null  && dto.getEnterprise() != null){
            t.setEnterprise(null);
            t.setEnterprise(new Enterprise());
        }
        if(t.getAiUsageType() == null  && dto.getAiUsageType() != null){
            t.setAiUsageType(new AiUsageType());
        }else if (t.getAiUsageType() != null  && dto.getAiUsageType() != null){
            t.setAiUsageType(null);
            t.setAiUsageType(new AiUsageType());
        }
        if(t.getCollaborator() == null  && dto.getCollaborator() != null){
            t.setCollaborator(new Collaborator());
        }else if (t.getCollaborator() != null  && dto.getCollaborator() != null){
            t.setCollaborator(null);
            t.setCollaborator(new Collaborator());
        }
        if(t.getDocument() == null  && dto.getDocument() != null){
            t.setDocument(new Document());
        }else if (t.getDocument() != null  && dto.getDocument() != null){
            t.setDocument(null);
            t.setDocument(new Document());
        }
        if (dto.getEnterprise() != null)
        enterpriseConverter.copy(dto.getEnterprise(), t.getEnterprise());
        if (dto.getAiUsageType() != null)
        aiUsageTypeConverter.copy(dto.getAiUsageType(), t.getAiUsageType());
        if (dto.getCollaborator() != null)
        collaboratorConverter.copy(dto.getCollaborator(), t.getCollaborator());
        if (dto.getDocument() != null)
        documentConverter.copy(dto.getDocument(), t.getDocument());
    }

    public List<AiUsageLog> copy(List<AiUsageLogDto> dtos) {
        List<AiUsageLog> result = new ArrayList<>();
        if (dtos != null) {
            for (AiUsageLogDto dto : dtos) {
                AiUsageLog instance = new AiUsageLog();
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
    public AiUsageTypeConverter getAiUsageTypeConverter(){
        return this.aiUsageTypeConverter;
    }
    public void setAiUsageTypeConverter(AiUsageTypeConverter aiUsageTypeConverter ){
        this.aiUsageTypeConverter = aiUsageTypeConverter;
    }
    public EnterpriseConverter getEnterpriseConverter(){
        return this.enterpriseConverter;
    }
    public void setEnterpriseConverter(EnterpriseConverter enterpriseConverter ){
        this.enterpriseConverter = enterpriseConverter;
    }
    public DocumentConverter getDocumentConverter(){
        return this.documentConverter;
    }
    public void setDocumentConverter(DocumentConverter documentConverter ){
        this.documentConverter = documentConverter;
    }
    public boolean  isEnterprise(){
        return this.enterprise;
    }
    public void  setEnterprise(boolean enterprise){
        this.enterprise = enterprise;
    }
    public boolean  isAiUsageType(){
        return this.aiUsageType;
    }
    public void  setAiUsageType(boolean aiUsageType){
        this.aiUsageType = aiUsageType;
    }
    public boolean  isCollaborator(){
        return this.collaborator;
    }
    public void  setCollaborator(boolean collaborator){
        this.collaborator = collaborator;
    }
    public boolean  isDocument(){
        return this.document;
    }
    public void  setDocument(boolean document){
        this.document = document;
    }
}
