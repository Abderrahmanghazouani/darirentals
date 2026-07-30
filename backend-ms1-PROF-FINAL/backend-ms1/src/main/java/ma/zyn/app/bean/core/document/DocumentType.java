package ma.zyn.app.bean.core.document;








import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "document_type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="document_type_seq",sequenceName="document_type_seq",allocationSize=1, initialValue = 1)
public class DocumentType  extends BaseEntity     {




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



    public DocumentType(){
        super();
    }

    public DocumentType(Long id){
        this.id = id;
    }

    public DocumentType(Long id,String label){
        this.id = id;
        this.label = label ;
    }
    public DocumentType(String label){
        this.label = label ;
    }
    public DocumentType(String label,String code){
        this.label=label;
        this.code=code;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="document_type_seq")
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
        DocumentType documentType = (DocumentType) o;
        return id != null && id.equals(documentType.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

