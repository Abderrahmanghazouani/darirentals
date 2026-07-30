package  ma.zyn.app.dao.criteria.core.report;


import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;

import ma.zyn.app.zynerator.criteria.BaseCriteria;

import java.util.List;

public class FinancialReportPropertyCriteria extends  BaseCriteria  {


    private FinancialReportCriteria financialReport ;
    private List<FinancialReportCriteria> financialReports ;
    private PropertyCriteria property ;
    private List<PropertyCriteria> propertys ;



    public FinancialReportCriteria getFinancialReport(){
        return this.financialReport;
    }

    public void setFinancialReport(FinancialReportCriteria financialReport){
        this.financialReport = financialReport;
    }
    public List<FinancialReportCriteria> getFinancialReports(){
        return this.financialReports;
    }

    public void setFinancialReports(List<FinancialReportCriteria> financialReports){
        this.financialReports = financialReports;
    }
    public PropertyCriteria getProperty(){
        return this.property;
    }

    public void setProperty(PropertyCriteria property){
        this.property = property;
    }
    public List<PropertyCriteria> getPropertys(){
        return this.propertys;
    }

    public void setPropertys(List<PropertyCriteria> propertys){
        this.propertys = propertys;
    }
}
