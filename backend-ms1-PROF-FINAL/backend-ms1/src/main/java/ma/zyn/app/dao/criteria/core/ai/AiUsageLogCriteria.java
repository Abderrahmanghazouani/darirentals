package  ma.zyn.app.dao.criteria.core.ai;


import ma.zyn.app.dao.criteria.core.auth.CollaboratorCriteria;
import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;
import ma.zyn.app.dao.criteria.core.document.DocumentCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class AiUsageLogCriteria extends  BaseCriteria  {

    private String tokensUsed;
    private String tokensUsedMin;
    private String tokensUsedMax;
    private LocalDateTime date;
    private LocalDateTime dateFrom;
    private LocalDateTime dateTo;

    private EnterpriseCriteria enterprise ;
    private List<EnterpriseCriteria> enterprises ;
    private AiUsageTypeCriteria aiUsageType ;
    private List<AiUsageTypeCriteria> aiUsageTypes ;
    private CollaboratorCriteria collaborator ;
    private List<CollaboratorCriteria> collaborators ;
    private DocumentCriteria document ;
    private List<DocumentCriteria> documents ;


    public String getTokensUsed(){
        return this.tokensUsed;
    }
    public void setTokensUsed(String tokensUsed){
        this.tokensUsed = tokensUsed;
    }   
    public String getTokensUsedMin(){
        return this.tokensUsedMin;
    }
    public void setTokensUsedMin(String tokensUsedMin){
        this.tokensUsedMin = tokensUsedMin;
    }
    public String getTokensUsedMax(){
        return this.tokensUsedMax;
    }
    public void setTokensUsedMax(String tokensUsedMax){
        this.tokensUsedMax = tokensUsedMax;
    }
      
    public LocalDateTime getDate(){
        return this.date;
    }
    public void setDate(LocalDateTime date){
        this.date = date;
    }
    public LocalDateTime getDateFrom(){
        return this.dateFrom;
    }
    public void setDateFrom(LocalDateTime dateFrom){
        this.dateFrom = dateFrom;
    }
    public LocalDateTime getDateTo(){
        return this.dateTo;
    }
    public void setDateTo(LocalDateTime dateTo){
        this.dateTo = dateTo;
    }

    public EnterpriseCriteria getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseCriteria enterprise){
        this.enterprise = enterprise;
    }
    public List<EnterpriseCriteria> getEnterprises(){
        return this.enterprises;
    }

    public void setEnterprises(List<EnterpriseCriteria> enterprises){
        this.enterprises = enterprises;
    }
    public AiUsageTypeCriteria getAiUsageType(){
        return this.aiUsageType;
    }

    public void setAiUsageType(AiUsageTypeCriteria aiUsageType){
        this.aiUsageType = aiUsageType;
    }
    public List<AiUsageTypeCriteria> getAiUsageTypes(){
        return this.aiUsageTypes;
    }

    public void setAiUsageTypes(List<AiUsageTypeCriteria> aiUsageTypes){
        this.aiUsageTypes = aiUsageTypes;
    }
    public CollaboratorCriteria getCollaborator(){
        return this.collaborator;
    }

    public void setCollaborator(CollaboratorCriteria collaborator){
        this.collaborator = collaborator;
    }
    public List<CollaboratorCriteria> getCollaborators(){
        return this.collaborators;
    }

    public void setCollaborators(List<CollaboratorCriteria> collaborators){
        this.collaborators = collaborators;
    }
    public DocumentCriteria getDocument(){
        return this.document;
    }

    public void setDocument(DocumentCriteria document){
        this.document = document;
    }
    public List<DocumentCriteria> getDocuments(){
        return this.documents;
    }

    public void setDocuments(List<DocumentCriteria> documents){
        this.documents = documents;
    }
}
