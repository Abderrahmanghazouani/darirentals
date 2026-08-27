package ma.zyn.app.bean.core.auth;




import ma.zyn.app.bean.core.property.Property;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

/**
 * Chantier 3 (NOTES-permissions.md) : liaison Collaborator <-> Property.
 * Restreint les proprietes visibles/gerables par un collaborateur au role
 * "Gestionnaire" (voir EnterpriseAccessService.getAccessiblePropertyIds()).
 * Sans ligne pour une property donnee, un Gestionnaire n'y a pas acces -
 * un SubAdmin n'est jamais concerne par cette table.
 */
@Entity
@Table(name = "collaborator_property_access")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="collaborator_property_access_seq",sequenceName="collaborator_property_access_seq",allocationSize=1, initialValue = 1)
public class CollaboratorPropertyAccess  extends BaseEntity     {




    private Collaborator collaborator ;
    private Property property ;


    public CollaboratorPropertyAccess(){
        super();
    }

    public CollaboratorPropertyAccess(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="collaborator_property_access_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaborator")
    public Collaborator getCollaborator(){
        return this.collaborator;
    }
    public void setCollaborator(Collaborator collaborator){
        this.collaborator = collaborator;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property")
    public Property getProperty(){
        return this.property;
    }
    public void setProperty(Property property){
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollaboratorPropertyAccess collaboratorPropertyAccess = (CollaboratorPropertyAccess) o;
        return id != null && id.equals(collaboratorPropertyAccess.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
