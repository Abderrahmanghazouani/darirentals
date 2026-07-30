package  ma.zyn.app.ws.dto.enterprise;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.auth.CollaboratorPermissionOverrideDto;
import ma.zyn.app.ws.dto.auth.CollaboratorRoleDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterpriseMembershipDto  extends AuditBaseDto {


    private CollaboratorDto collaborator ;
    private EnterpriseDto enterprise ;
    private CollaboratorRoleDto collaboratorRole ;

    private List<CollaboratorPermissionOverrideDto> collaboratorPermissionOverrides ;


    public EnterpriseMembershipDto(){
        super();
    }




    public CollaboratorDto getCollaborator(){
        return this.collaborator;
    }

    public void setCollaborator(CollaboratorDto collaborator){
        this.collaborator = collaborator;
    }
    public EnterpriseDto getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseDto enterprise){
        this.enterprise = enterprise;
    }
    public CollaboratorRoleDto getCollaboratorRole(){
        return this.collaboratorRole;
    }

    public void setCollaboratorRole(CollaboratorRoleDto collaboratorRole){
        this.collaboratorRole = collaboratorRole;
    }



    public List<CollaboratorPermissionOverrideDto> getCollaboratorPermissionOverrides(){
        return this.collaboratorPermissionOverrides;
    }

    public void setCollaboratorPermissionOverrides(List<CollaboratorPermissionOverrideDto> collaboratorPermissionOverrides){
        this.collaboratorPermissionOverrides = collaboratorPermissionOverrides;
    }



}
