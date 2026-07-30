package  ma.zyn.app.ws.dto.report;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;



import ma.zyn.app.ws.dto.property.PropertyDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class FinancialReportPropertyDto  extends AuditBaseDto {


    private FinancialReportDto financialReport ;
    private PropertyDto property ;



    public FinancialReportPropertyDto(){
        super();
    }




    public FinancialReportDto getFinancialReport(){
        return this.financialReport;
    }

    public void setFinancialReport(FinancialReportDto financialReport){
        this.financialReport = financialReport;
    }
    public PropertyDto getProperty(){
        return this.property;
    }

    public void setProperty(PropertyDto property){
        this.property = property;
    }






}
