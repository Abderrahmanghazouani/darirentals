package ma.zyn.app.bean.core.enterprise;

import java.util.List;





import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.bean.core.auth.CollaboratorRole;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "enterprise_membership")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="enterprise_membership_seq",sequenceName="enterprise_membership_seq",allocationSize=1, initialValue = 1)
public class EnterpriseMembership  extends BaseEntity     {




    private Collaborator collaborator ;
    private Enterprise enterprise ;
    private CollaboratorRole collaboratorRole ;

    private List<CollaboratorPermissionOverride> collaboratorPermissionOverrides ;

    public EnterpriseMembership(){
        super();
    }

    public EnterpriseMembership(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="enterprise_membership_seq")
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
    @JoinColumn(name = "enterprise")
    public Enterprise getEnterprise(){
        return this.enterprise;
    }
    public void setEnterprise(Enterprise enterprise){
        this.enterprise = enterprise;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collaborator_role")
    public CollaboratorRole getCollaboratorRole(){
        return this.collaboratorRole;
    }
    public void setCollaboratorRole(CollaboratorRole collaboratorRole){
        this.collaboratorRole = collaboratorRole;
    }
    @OneToMany(mappedBy = "enterpriseMembership")
    public List<CollaboratorPermissionOverride> getCollaboratorPermissionOverrides(){
        return this.collaboratorPermissionOverrides;
    }

    public void setCollaboratorPermissionOverrides(List<CollaboratorPermissionOverride> collaboratorPermissionOverrides){
        this.collaboratorPermissionOverrides = collaboratorPermissionOverrides;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnterpriseMembership enterpriseMembership = (EnterpriseMembership) o;
        return id != null && id.equals(enterpriseMembership.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

