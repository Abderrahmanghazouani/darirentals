package ma.zyn.app.bean.core.currency;

import java.util.List;





import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.client.Client;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "currency")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="currency_seq",sequenceName="currency_seq",allocationSize=1, initialValue = 1)
public class Currency  extends BaseEntity     {




    private String description;

    @Column(length = 500)
    private String code;

    @Column(length = 500)
    private String label;

    @Column(length = 500)
    private String style;

    @Column(columnDefinition = "boolean default false")
    private Boolean isDefault = false;

    private Integer sortOrder = 0;

    @Column(length = 500)
    private String symbol;


    private List<ExchangeRate> exchangeRatesAsBase ;
    private List<ExchangeRate> exchangeRatesAsTarget ;
    private List<Enterprise> enterprises ;
    private List<Collaborator> collaborators ;

    public Currency(){
        super();
    }

    public Currency(Long id){
        this.id = id;
    }

    public Currency(Long id,String label){
        this.id = id;
        this.label = label ;
    }
    public Currency(String label){
        this.label = label ;
    }
    public Currency(String label,String code){
        this.label=label;
        this.code=code;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="currency_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
      @Column(columnDefinition="TEXT")
    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public String getCode(){
        return this.code;
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getLabel(){
        return this.label;
    }
    public void setLabel(String label){
        this.label = label;
    }
    public String getStyle(){
        return this.style;
    }
    public void setStyle(String style){
        this.style = style;
    }
    public Boolean  getIsDefault(){
        return this.isDefault;
    }
    public void setIsDefault(Boolean isDefault){
        this.isDefault = isDefault;
    }
    public Integer getSortOrder(){
        return this.sortOrder;
    }
    public void setSortOrder(Integer sortOrder){
        this.sortOrder = sortOrder;
    }
    public String getSymbol(){
        return this.symbol;
    }
    public void setSymbol(String symbol){
        this.symbol = symbol;
    }
    @OneToMany(mappedBy = "baseCurrency")
    public List<ExchangeRate> getExchangeRatesAsBase(){
        return this.exchangeRatesAsBase;
    }

    public void setExchangeRatesAsBase(List<ExchangeRate> exchangeRatesAsBase){
        this.exchangeRatesAsBase = exchangeRatesAsBase;
    }
    @OneToMany(mappedBy = "targetCurrency")
    public List<ExchangeRate> getExchangeRatesAsTarget(){
        return this.exchangeRatesAsTarget;
    }

    public void setExchangeRatesAsTarget(List<ExchangeRate> exchangeRatesAsTarget){
        this.exchangeRatesAsTarget = exchangeRatesAsTarget;
    }
    @OneToMany(mappedBy = "currency")
    public List<Enterprise> getEnterprises(){
        return this.enterprises;
    }

    public void setEnterprises(List<Enterprise> enterprises){
        this.enterprises = enterprises;
    }
    @OneToMany(mappedBy = "displayCurrency")
    public List<Collaborator> getCollaborators(){
        return this.collaborators;
    }

    public void setCollaborators(List<Collaborator> collaborators){
        this.collaborators = collaborators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return id != null && id.equals(currency.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

