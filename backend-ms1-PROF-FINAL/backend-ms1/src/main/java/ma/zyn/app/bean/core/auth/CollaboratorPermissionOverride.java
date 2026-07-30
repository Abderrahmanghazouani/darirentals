package ma.zyn.app.bean.core.auth;






import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "collaborator_permission_override")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="collaborator_permission_override_seq",sequenceName="collaborator_permission_override_seq",allocationSize=1, initialValue = 1)
public class CollaboratorPermissionOverride  extends BaseEntity     {




    @Column(columnDefinition = "boolean default false")
    private Boolean canManageFinancials = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean canManageUsers = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean canDeleteProperty = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean canManageServiceProviders = false;

    @Column(columnDefinition = "boolean default false")
    private Boolean canManageAiUsage = false;

    private EnterpriseMembership enterpriseMembership ;


    public CollaboratorPermissionOverride(){
        super();
    }

    public CollaboratorPermissionOverride(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="collaborator_permission_override_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public Boolean  getCanManageFinancials(){
        return this.canManageFinancials;
    }
    public void setCanManageFinancials(Boolean canManageFinancials){
        this.canManageFinancials = canManageFinancials;
    }
    public Boolean  getCanManageUsers(){
        return this.canManageUsers;
    }
    public void setCanManageUsers(Boolean canManageUsers){
        this.canManageUsers = canManageUsers;
    }
    public Boolean  getCanDeleteProperty(){
        return this.canDeleteProperty;
    }
    public void setCanDeleteProperty(Boolean canDeleteProperty){
        this.canDeleteProperty = canDeleteProperty;
    }
    public Boolean  getCanManageServiceProviders(){
        return this.canManageServiceProviders;
    }
    public void setCanManageServiceProviders(Boolean canManageServiceProviders){
        this.canManageServiceProviders = canManageServiceProviders;
    }
    public Boolean  getCanManageAiUsage(){
        return this.canManageAiUsage;
    }
    public void setCanManageAiUsage(Boolean canManageAiUsage){
        this.canManageAiUsage = canManageAiUsage;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_membership")
    public EnterpriseMembership getEnterpriseMembership(){
        return this.enterpriseMembership;
    }
    public void setEnterpriseMembership(EnterpriseMembership enterpriseMembership){
        this.enterpriseMembership = enterpriseMembership;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollaboratorPermissionOverride collaboratorPermissionOverride = (CollaboratorPermissionOverride) o;
        return id != null && id.equals(collaboratorPermissionOverride.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

