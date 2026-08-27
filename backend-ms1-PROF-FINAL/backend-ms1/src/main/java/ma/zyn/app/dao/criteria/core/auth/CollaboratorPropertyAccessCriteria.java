package  ma.zyn.app.dao.criteria.core.auth;


import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class CollaboratorPropertyAccessCriteria extends  BaseCriteria  {


    private CollaboratorCriteria collaborator ;
    private List<CollaboratorCriteria> collaborators ;
    private PropertyCriteria property ;
    private List<PropertyCriteria> propertys ;



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
    public PropertyCriteria getProperty(){
        return this.property;
    }

    public void setProperty(PropertyCriteria property){
        this.property = property;
    }
    public List<PropertyCriteria> getPropertys(){
        return this.propertys;
    }

    public void setPropertys(List<PropertyCriteria> propertys){
        this.propertys = propertys;
    }
}
