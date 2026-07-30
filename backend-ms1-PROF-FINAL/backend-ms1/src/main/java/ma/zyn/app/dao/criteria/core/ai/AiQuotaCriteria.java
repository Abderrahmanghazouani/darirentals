package  ma.zyn.app.dao.criteria.core.ai;


import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class AiQuotaCriteria extends  BaseCriteria  {

    private String tokensAllocated;
    private String tokensAllocatedMin;
    private String tokensAllocatedMax;

    private EnterpriseCriteria enterprise ;
    private List<EnterpriseCriteria> enterprises ;


    public String getTokensAllocated(){
        return this.tokensAllocated;
    }
    public void setTokensAllocated(String tokensAllocated){
        this.tokensAllocated = tokensAllocated;
    }   
    public String getTokensAllocatedMin(){
        return this.tokensAllocatedMin;
    }
    public void setTokensAllocatedMin(String tokensAllocatedMin){
        this.tokensAllocatedMin = tokensAllocatedMin;
    }
    public String getTokensAllocatedMax(){
        return this.tokensAllocatedMax;
    }
    public void setTokensAllocatedMax(String tokensAllocatedMax){
        this.tokensAllocatedMax = tokensAllocatedMax;
    }
      

    public EnterpriseCriteria getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseCriteria enterprise){
        this.enterprise = enterprise;
    }
    public List<EnterpriseCriteria> getEnterprises(){
        return this.enterprises;
    }

    public void setEnterprises(List<EnterpriseCriteria> enterprises){
        this.enterprises = enterprises;
    }
}
