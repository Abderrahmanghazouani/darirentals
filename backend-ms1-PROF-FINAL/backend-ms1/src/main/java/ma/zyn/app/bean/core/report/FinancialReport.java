package ma.zyn.app.bean.core.report;

import java.util.List;

import java.time.LocalDateTime;


import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;


import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.property.Property;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;
import java.math.BigDecimal;

@Entity
@Table(name = "financial_report")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="financial_report_seq",sequenceName="financial_report_seq",allocationSize=1, initialValue = 1)
public class FinancialReport  extends BaseEntity     {




    private BigDecimal totalRevenue = BigDecimal.ZERO;

    private BigDecimal totalCharges = BigDecimal.ZERO;

    private BigDecimal netProfit = BigDecimal.ZERO;

    private LocalDateTime generatedAt ;

    @Column(length = 500)
    private String file;

    private FinancialReportType financialReportType ;
    private FinancialReportScope financialReportScope ;
    private Enterprise enterprise ;
    private Collaborator generatedBy ;

    private List<FinancialReportProperty> financialReportProperties ;

    public FinancialReport(){
        super();
    }

    public FinancialReport(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="financial_report_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
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
    public LocalDateTime getGeneratedAt(){
        return this.generatedAt;
    }
    public void setGeneratedAt(LocalDateTime generatedAt){
        this.generatedAt = generatedAt;
    }
    public String getFile(){
        return this.file;
    }
    public void setFile(String file){
        this.file = file;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_report_type")
    public FinancialReportType getFinancialReportType(){
        return this.financialReportType;
    }
    public void setFinancialReportType(FinancialReportType financialReportType){
        this.financialReportType = financialReportType;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_report_scope")
    public FinancialReportScope getFinancialReportScope(){
        return this.financialReportScope;
    }
    public void setFinancialReportScope(FinancialReportScope financialReportScope){
        this.financialReportScope = financialReportScope;
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
    @JoinColumn(name = "generated_by")
    public Collaborator getGeneratedBy(){
        return this.generatedBy;
    }
    public void setGeneratedBy(Collaborator generatedBy){
        this.generatedBy = generatedBy;
    }
    @OneToMany(mappedBy = "financialReport")
    public List<FinancialReportProperty> getFinancialReportProperties(){
        return this.financialReportProperties;
    }

    public void setFinancialReportProperties(List<FinancialReportProperty> financialReportProperties){
        this.financialReportProperties = financialReportProperties;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialReport financialReport = (FinancialReport) o;
        return id != null && id.equals(financialReport.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

