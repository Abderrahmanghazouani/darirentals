package  ma.zyn.app.ws.dto.auth;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;



import ma.zyn.app.ws.dto.property.PropertyDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CollaboratorPropertyAccessDto  extends AuditBaseDto {


    private CollaboratorDto collaborator ;
    private PropertyDto property ;



    public CollaboratorPropertyAccessDto(){
        super();
    }




    public CollaboratorDto getCollaborator(){
        return this.collaborator;
    }

    public void setCollaborator(CollaboratorDto collaborator){
        this.collaborator = collaborator;
    }
    public PropertyDto getProperty(){
        return this.property;
    }

    public void setProperty(PropertyDto property){
        this.property = property;
    }

}
