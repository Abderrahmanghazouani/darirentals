package  ma.zyn.app.ws.dto.report;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Date;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;


import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
import ma.zyn.app.ws.dto.property.PropertyDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialReportDto  extends AuditBaseDto {

    private BigDecimal totalRevenue  ;
    private BigDecimal totalCharges  ;
    private BigDecimal netProfit  ;
    private String generatedAt ;
    private String file  ;

    private FinancialReportTypeDto financialReportType ;
    private FinancialReportScopeDto financialReportScope ;
    private EnterpriseDto enterprise ;
    private CollaboratorDto generatedBy ;

    private List<FinancialReportPropertyDto> financialReportProperties ;


    public FinancialReportDto(){
        super();
    }



    public BigDecimal getTotalRevenue(){
        return this.totalRevenue;
    }
    public void setTotalRevenue(BigDecimal totalRevenue){
        this.totalRevenue = totalRevenue;
    }

    public BigDecimal getTotalCharges(){
        return this.totalCharges;
    }
    public void setTotalCharges(BigDecimal totalCharges){
        this.totalCharges = totalCharges;
    }

    public BigDecimal getNetProfit(){
        return this.netProfit;
    }
    public void setNetProfit(BigDecimal netProfit){
        this.netProfit = netProfit;
    }

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    public String getGeneratedAt(){
        return this.generatedAt;
    }
    public void setGeneratedAt(String generatedAt){
        this.generatedAt = generatedAt;
    }

    public String getFile(){
        return this.file;
    }
    public void setFile(String file){
        this.file = file;
    }


    public FinancialReportTypeDto getFinancialReportType(){
        return this.financialReportType;
    }

    public void setFinancialReportType(FinancialReportTypeDto financialReportType){
        this.financialReportType = financialReportType;
    }
    public FinancialReportScopeDto getFinancialReportScope(){
        return this.financialReportScope;
    }

    public void setFinancialReportScope(FinancialReportScopeDto financialReportScope){
        this.financialReportScope = financialReportScope;
    }
    public EnterpriseDto getEnterprise(){
        return this.enterprise;
    }

    public void setEnterprise(EnterpriseDto enterprise){
        this.enterprise = enterprise;
    }
    public CollaboratorDto getGeneratedBy(){
        return this.generatedBy;
    }

    public void setGeneratedBy(CollaboratorDto generatedBy){
        this.generatedBy = generatedBy;
    }



    public List<FinancialReportPropertyDto> getFinancialReportProperties(){
        return this.financialReportProperties;
    }

    public void setFinancialReportProperties(List<FinancialReportPropertyDto> financialReportProperties){
        this.financialReportProperties = financialReportProperties;
    }



}
