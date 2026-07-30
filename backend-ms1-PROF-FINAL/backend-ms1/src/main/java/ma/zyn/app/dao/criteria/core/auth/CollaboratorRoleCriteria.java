package  ma.zyn.app.dao.criteria.core.auth;



import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class CollaboratorRoleCriteria extends  BaseCriteria  {

    private String description;
    private String descriptionLike;
    private String code;
    private String codeLike;
    private String label;
    private String labelLike;
    private String style;
    private String styleLike;
    private Boolean isDefault;
    private String sortOrder;
    private String sortOrderMin;
    private String sortOrderMax;
    private Boolean canManageFinancials;
    private Boolean canManageUsers;
    private Boolean canDeleteProperty;
    private Boolean canManageServiceProviders;
    private Boolean canManageAiUsage;



    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getDescriptionLike(){
        return this.descriptionLike;
    }
    public void setDescriptionLike(String descriptionLike){
        this.descriptionLike = descriptionLike;
    }

    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCodeLike(){
        return this.codeLike;
    }
    public void setCodeLike(String codeLike){
        this.codeLike = codeLike;
    }

    public String getLabel(){
        return this.label;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getLabelLike(){
        return this.labelLike;
    }
    public void setLabelLike(String labelLike){
        this.labelLike = labelLike;
    }

    public String getStyle(){
        return this.style;
    }
    public void setStyle(String style){
        this.style = style;
    }
    public String getStyleLike(){
        return this.styleLike;
    }
    public void setStyleLike(String styleLike){
        this.styleLike = styleLike;
    }

    public Boolean getIsDefault(){
        return this.isDefault;
    }
    public void setIsDefault(Boolean isDefault){
        this.isDefault = isDefault;
    }
    public String getSortOrder(){
        return this.sortOrder;
    }
    public void setSortOrder(String sortOrder){
        this.sortOrder = sortOrder;
    }   
    public String getSortOrderMin(){
        return this.sortOrderMin;
    }
    public void setSortOrderMin(String sortOrderMin){
        this.sortOrderMin = sortOrderMin;
    }
    public String getSortOrderMax(){
        return this.sortOrderMax;
    }
    public void setSortOrderMax(String sortOrderMax){
        this.sortOrderMax = sortOrderMax;
    }
      
    public Boolean getCanManageFinancials(){
        return this.canManageFinancials;
    }
    public void setCanManageFinancials(Boolean canManageFinancials){
        this.canManageFinancials = canManageFinancials;
    }
    public Boolean getCanManageUsers(){
        return this.canManageUsers;
    }
    public void setCanManageUsers(Boolean canManageUsers){
        this.canManageUsers = canManageUsers;
    }
    public Boolean getCanDeleteProperty(){
        return this.canDeleteProperty;
    }
    public void setCanDeleteProperty(Boolean canDeleteProperty){
        this.canDeleteProperty = canDeleteProperty;
    }
    public Boolean getCanManageServiceProviders(){
        return this.canManageServiceProviders;
    }
    public void setCanManageServiceProviders(Boolean canManageServiceProviders){
        this.canManageServiceProviders = canManageServiceProviders;
    }
    public Boolean getCanManageAiUsage(){
        return this.canManageAiUsage;
    }
    public void setCanManageAiUsage(Boolean canManageAiUsage){
        this.canManageAiUsage = canManageAiUsage;
    }

}
