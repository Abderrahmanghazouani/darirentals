package  ma.zyn.app.dao.criteria.core.auth;


import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseMembershipCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class CollaboratorPermissionOverrideCriteria extends  BaseCriteria  {

    private Boolean canManageFinancials;
    private Boolean canManageUsers;
    private Boolean canDeleteProperty;
    private Boolean canManageServiceProviders;
    private Boolean canManageAiUsage;

    private EnterpriseMembershipCriteria enterpriseMembership ;
    private List<EnterpriseMembershipCriteria> enterpriseMemberships ;


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

    public EnterpriseMembershipCriteria getEnterpriseMembership(){
        return this.enterpriseMembership;
    }

    public void setEnterpriseMembership(EnterpriseMembershipCriteria enterpriseMembership){
        this.enterpriseMembership = enterpriseMembership;
    }
    public List<EnterpriseMembershipCriteria> getEnterpriseMemberships(){
        return this.enterpriseMemberships;
    }

    public void setEnterpriseMemberships(List<EnterpriseMembershipCriteria> enterpriseMemberships){
        this.enterpriseMemberships = enterpriseMemberships;
    }
}
