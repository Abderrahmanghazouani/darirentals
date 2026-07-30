package ma.zyn.app.bean.core.report;






import ma.zyn.app.bean.core.property.Property;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "financial_report_property")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="financial_report_property_seq",sequenceName="financial_report_property_seq",allocationSize=1, initialValue = 1)
public class FinancialReportProperty  extends BaseEntity     {




    private FinancialReport financialReport ;
    private Property property ;


    public FinancialReportProperty(){
        super();
    }

    public FinancialReportProperty(Long id){
        this.id = id;
    }





    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="financial_report_property_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "financial_report")
    public FinancialReport getFinancialReport(){
        return this.financialReport;
    }
    public void setFinancialReport(FinancialReport financialReport){
        this.financialReport = financialReport;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property")
    public Property getProperty(){
        return this.property;
    }
    public void setProperty(Property property){
        this.property = property;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FinancialReportProperty financialReportProperty = (FinancialReportProperty) o;
        return id != null && id.equals(financialReportProperty.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

