package ma.zyn.app.bean.core.ai;






import ma.zyn.app.bean.core.enterprise.Enterprise;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ai_quota")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="ai_quota_seq",sequenceName="ai_quota_seq",allocationSize=1, initialValue = 1)
public class AiQuota  extends BaseEntity     {




    private Long tokensAllocated ;

    private Enterprise enterprise ;


    public AiQuota(){
        super();
    }

    public AiQuota(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="ai_quota_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public Long getTokensAllocated(){
        return this.tokensAllocated;
    }
    public void setTokensAllocated(Long tokensAllocated){
        this.tokensAllocated = tokensAllocated;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise")
    public Enterprise getEnterprise(){
        return this.enterprise;
    }
    public void setEnterprise(Enterprise enterprise){
        this.enterprise = enterprise;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AiQuota aiQuota = (AiQuota) o;
        return id != null && id.equals(aiQuota.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

