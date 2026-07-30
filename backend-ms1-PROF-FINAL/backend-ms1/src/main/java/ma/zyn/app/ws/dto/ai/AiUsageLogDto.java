package  ma.zyn.app.ws.dto.ai;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;


import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
import ma.zyn.app.ws.dto.document.DocumentDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiUsageLogDto  extends AuditBaseDto {

    private Long tokensUsed  ;
    private String date ;

    private EnterpriseDto enterprise ;
    private AiUsageTypeDto aiUsageType ;
    private CollaboratorDto collaborator ;
    private DocumentDto document ;



    public AiUsageLogDto(){
        super();
    }



    public Long getTokensUsed(){
        return this.tokensUsed;
    }
    public void setTokensUsed(Long tokensUsed){
        this.tokensUsed = tokensUsed;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }


    public EnterpriseDto getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseDto enterprise){
        this.enterprise = enterprise;
    }
    public AiUsageTypeDto getAiUsageType(){
        return this.aiUsageType;
    }

    public void setAiUsageType(AiUsageTypeDto aiUsageType){
        this.aiUsageType = aiUsageType;
    }
    public CollaboratorDto getCollaborator(){
        return this.collaborator;
    }

    public void setCollaborator(CollaboratorDto collaborator){
        this.collaborator = collaborator;
    }
    public DocumentDto getDocument(){
        return this.document;
    }

    public void setDocument(DocumentDto document){
        this.document = document;
    }






}
