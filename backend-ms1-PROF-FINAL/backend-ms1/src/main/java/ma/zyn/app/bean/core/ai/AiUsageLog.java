package ma.zyn.app.bean.core.ai;


import java.time.LocalDateTime;


import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;


import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.document.Document;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ai_usage_log")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="ai_usage_log_seq",sequenceName="ai_usage_log_seq",allocationSize=1, initialValue = 1)
public class AiUsageLog  extends BaseEntity     {




    private Long tokensUsed ;

    private LocalDateTime date ;

    private Enterprise enterprise ;
    private AiUsageType aiUsageType ;
    private Collaborator collaborator ;
    private Document document ;


    public AiUsageLog(){
        super();
    }

    public AiUsageLog(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="ai_usage_log_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public Long getTokensUsed(){
        return this.tokensUsed;
    }
    public void setTokensUsed(Long tokensUsed){
        this.tokensUsed = tokensUsed;
    }
    public LocalDateTime getDate(){
        return this.date;
    }
    public void setDate(LocalDateTime date){
        this.date = date;
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
    @JoinColumn(name = "ai_usage_type")
    public AiUsageType getAiUsageType(){
        return this.aiUsageType;
    }
    public void setAiUsageType(AiUsageType aiUsageType){
        this.aiUsageType = aiUsageType;
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
    @JoinColumn(name = "document")
    public Document getDocument(){
        return this.document;
    }
    public void setDocument(Document document){
        this.document = document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AiUsageLog aiUsageLog = (AiUsageLog) o;
        return id != null && id.equals(aiUsageLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

