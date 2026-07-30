package ma.zyn.app.bean.core.ai;








import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ai_usage_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="ai_usage_type_seq",sequenceName="ai_usage_type_seq",allocationSize=1, initialValue = 1)
public class AiUsageType  extends BaseEntity     {




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



    public AiUsageType(){
        super();
    }

    public AiUsageType(Long id){
        this.id = id;
    }

    public AiUsageType(Long id,String label){
        this.id = id;
        this.label = label ;
    }
    public AiUsageType(String label){
        this.label = label ;
    }
    public AiUsageType(String label,String code){
        this.label=label;
        this.code=code;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="ai_usage_type_seq")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AiUsageType aiUsageType = (AiUsageType) o;
        return id != null && id.equals(aiUsageType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

