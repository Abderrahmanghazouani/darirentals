package  ma.zyn.app.dao.criteria.core.enterprise;


import ma.zyn.app.dao.criteria.core.auth.CollaboratorCriteria;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorRoleCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class EnterpriseMembershipCriteria extends  BaseCriteria  {


    private CollaboratorCriteria collaborator ;
    private List<CollaboratorCriteria> collaborators ;
    private EnterpriseCriteria enterprise ;
    private List<EnterpriseCriteria> enterprises ;
    private CollaboratorRoleCriteria collaboratorRole ;
    private List<CollaboratorRoleCriteria> collaboratorRoles ;



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
    public CollaboratorRoleCriteria getCollaboratorRole(){
        return this.collaboratorRole;
    }

    public void setCollaboratorRole(CollaboratorRoleCriteria collaboratorRole){
        this.collaboratorRole = collaboratorRole;
    }
    public List<CollaboratorRoleCriteria> getCollaboratorRoles(){
        return this.collaboratorRoles;
    }

    public void setCollaboratorRoles(List<CollaboratorRoleCriteria> collaboratorRoles){
        this.collaboratorRoles = collaboratorRoles;
    }
}
