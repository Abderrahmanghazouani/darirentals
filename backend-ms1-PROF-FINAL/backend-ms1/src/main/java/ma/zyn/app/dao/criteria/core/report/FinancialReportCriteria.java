package  ma.zyn.app.dao.criteria.core.report;


import ma.zyn.app.dao.criteria.core.auth.CollaboratorCriteria;
import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;
import java.time.LocalDateTime;
import java.time.LocalDate;

public class FinancialReportCriteria extends  BaseCriteria  {

    private String totalRevenue;
    private String totalRevenueMin;
    private String totalRevenueMax;
    private String totalCharges;
    private String totalChargesMin;
    private String totalChargesMax;
    private String netProfit;
    private String netProfitMin;
    private String netProfitMax;
    private LocalDateTime generatedAt;
    private LocalDateTime generatedAtFrom;
    private LocalDateTime generatedAtTo;
    private String file;
    private String fileLike;

    private FinancialReportTypeCriteria financialReportType ;
    private List<FinancialReportTypeCriteria> financialReportTypes ;
    private FinancialReportScopeCriteria financialReportScope ;
    private List<FinancialReportScopeCriteria> financialReportScopes ;
    private EnterpriseCriteria enterprise ;
    private List<EnterpriseCriteria> enterprises ;
    private CollaboratorCriteria generatedBy ;
    private List<CollaboratorCriteria> generatedBys ;


    public String getTotalRevenue(){
        return this.totalRevenue;
    }
    public void setTotalRevenue(String totalRevenue){
        this.totalRevenue = totalRevenue;
    }   
    public String getTotalRevenueMin(){
        return this.totalRevenueMin;
    }
    public void setTotalRevenueMin(String totalRevenueMin){
        this.totalRevenueMin = totalRevenueMin;
    }
    public String getTotalRevenueMax(){
        return this.totalRevenueMax;
    }
    public void setTotalRevenueMax(String totalRevenueMax){
        this.totalRevenueMax = totalRevenueMax;
    }
      
    public String getTotalCharges(){
        return this.totalCharges;
    }
    public void setTotalCharges(String totalCharges){
        this.totalCharges = totalCharges;
    }   
    public String getTotalChargesMin(){
        return this.totalChargesMin;
    }
    public void setTotalChargesMin(String totalChargesMin){
        this.totalChargesMin = totalChargesMin;
    }
    public String getTotalChargesMax(){
        return this.totalChargesMax;
    }
    public void setTotalChargesMax(String totalChargesMax){
        this.totalChargesMax = totalChargesMax;
    }
      
    public String getNetProfit(){
        return this.netProfit;
    }
    public void setNetProfit(String netProfit){
        this.netProfit = netProfit;
    }   
    public String getNetProfitMin(){
        return this.netProfitMin;
    }
    public void setNetProfitMin(String netProfitMin){
        this.netProfitMin = netProfitMin;
    }
    public String getNetProfitMax(){
        return this.netProfitMax;
    }
    public void setNetProfitMax(String netProfitMax){
        this.netProfitMax = netProfitMax;
    }
      
    public LocalDateTime getGeneratedAt(){
        return this.generatedAt;
    }
    public void setGeneratedAt(LocalDateTime generatedAt){
        this.generatedAt = generatedAt;
    }
    public LocalDateTime getGeneratedAtFrom(){
        return this.generatedAtFrom;
    }
    public void setGeneratedAtFrom(LocalDateTime generatedAtFrom){
        this.generatedAtFrom = generatedAtFrom;
    }
    public LocalDateTime getGeneratedAtTo(){
        return this.generatedAtTo;
    }
    public void setGeneratedAtTo(LocalDateTime generatedAtTo){
        this.generatedAtTo = generatedAtTo;
    }
    public String getFile(){
        return this.file;
    }
    public void setFile(String file){
        this.file = file;
    }
    public String getFileLike(){
        return this.fileLike;
    }
    public void setFileLike(String fileLike){
        this.fileLike = fileLike;
    }


    public FinancialReportTypeCriteria getFinancialReportType(){
        return this.financialReportType;
    }

    public void setFinancialReportType(FinancialReportTypeCriteria financialReportType){
        this.financialReportType = financialReportType;
    }
    public List<FinancialReportTypeCriteria> getFinancialReportTypes(){
        return this.financialReportTypes;
    }

    public void setFinancialReportTypes(List<FinancialReportTypeCriteria> financialReportTypes){
        this.financialReportTypes = financialReportTypes;
    }
    public FinancialReportScopeCriteria getFinancialReportScope(){
        return this.financialReportScope;
    }

    public void setFinancialReportScope(FinancialReportScopeCriteria financialReportScope){
        this.financialReportScope = financialReportScope;
    }
    public List<FinancialReportScopeCriteria> getFinancialReportScopes(){
        return this.financialReportScopes;
    }

    public void setFinancialReportScopes(List<FinancialReportScopeCriteria> financialReportScopes){
        this.financialReportScopes = financialReportScopes;
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
    public CollaboratorCriteria getGeneratedBy(){
        return this.generatedBy;
    }

    public void setGeneratedBy(CollaboratorCriteria generatedBy){
        this.generatedBy = generatedBy;
    }
    public List<CollaboratorCriteria> getGeneratedBys(){
        return this.generatedBys;
    }

    public void setGeneratedBys(List<CollaboratorCriteria> generatedBys){
        this.generatedBys = generatedBys;
    }
}
