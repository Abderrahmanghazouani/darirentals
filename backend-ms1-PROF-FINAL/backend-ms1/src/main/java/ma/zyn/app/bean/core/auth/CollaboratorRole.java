package ma.zyn.app.bean.core.auth;








import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "collaborator_role")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="collaborator_role_seq",sequenceName="collaborator_role_seq",allocationSize=1, initialValue = 1)
public class CollaboratorRole  extends BaseEntity     {




    private String description;

    @Column(length = 500)
    private String code;

    @Column(length = 500)
    private String label;

    @Column(length = 500)
    private String style;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDefault = false;

    private Integer sortOrder = 0;

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



    public CollaboratorRole(){
        super();
    }

    public CollaboratorRole(Long id){
        this.id = id;
    }

    public CollaboratorRole(Long id,String label){
        this.id = id;
        this.label = label ;
    }
    public CollaboratorRole(String label){
        this.label = label ;
    }
    public CollaboratorRole(String label,String code){
        this.label=label;
        this.code=code;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="collaborator_role_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
      @Column(columnDefinition="TEXT")
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getLabel(){
        return this.label;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getStyle(){
        return this.style;
    }
    public void setStyle(String style){
        this.style = style;
    }
    public Boolean  getIsDefault(){
        return this.isDefault;
    }
    public void setIsDefault(Boolean isDefault){
        this.isDefault = isDefault;
    }
    public Integer getSortOrder(){
        return this.sortOrder;
    }
    public void setSortOrder(Integer sortOrder){
        this.sortOrder = sortOrder;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CollaboratorRole collaboratorRole = (CollaboratorRole) o;
        return id != null && id.equals(collaboratorRole.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

