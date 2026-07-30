package  ma.zyn.app.ws.dto.auth;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;



import ma.zyn.app.ws.dto.enterprise.EnterpriseMembershipDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaboratorPermissionOverrideDto  extends AuditBaseDto {

    private Boolean canManageFinancials  ;
    private Boolean canManageUsers  ;
    private Boolean canDeleteProperty  ;
    private Boolean canManageServiceProviders  ;
    private Boolean canManageAiUsage  ;

    private EnterpriseMembershipDto enterpriseMembership ;



    public CollaboratorPermissionOverrideDto(){
        super();
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


    public EnterpriseMembershipDto getEnterpriseMembership(){
        return this.enterpriseMembership;
    }

    public void setEnterpriseMembership(EnterpriseMembershipDto enterpriseMembership){
        this.enterpriseMembership = enterpriseMembership;
    }






}
