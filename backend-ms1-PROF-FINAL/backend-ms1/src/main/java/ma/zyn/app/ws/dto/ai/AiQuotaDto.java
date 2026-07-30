package  ma.zyn.app.ws.dto.ai;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;



import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiQuotaDto  extends AuditBaseDto {

    private Long tokensAllocated  ;

    private EnterpriseDto enterprise ;



    public AiQuotaDto(){
        super();
    }



    public Long getTokensAllocated(){
        return this.tokensAllocated;
    }
    public void setTokensAllocated(Long tokensAllocated){
        this.tokensAllocated = tokensAllocated;
    }


    public EnterpriseDto getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseDto enterprise){
        this.enterprise = enterprise;
    }






}
