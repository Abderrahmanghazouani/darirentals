package ma.zyn.app.bean.core.enterprise;

import java.util.List;





import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.bean.core.provider.ServiceType;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.bean.core.client.Client;


import com.fasterxml.jackson.annotation.JsonInclude;
import ma.zyn.app.zynerator.bean.BaseEntity;
import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "enterprise")
@JsonInclude(JsonInclude.Include.NON_NULL)
@SequenceGenerator(name="enterprise_seq",sequenceName="enterprise_seq",allocationSize=1, initialValue = 1)
public class Enterprise  extends BaseEntity     {




    @Column(length = 500)
    private String name;

    @Column(length = 500)
    private String phone;

    @Column(length = 500)
    private String address;

    private Currency currency ;

    private List<Property> properties ;
    private List<Client> clients ;
    private List<ServiceProvider> serviceProviders ;
    private List<EnterpriseMembership> enterpriseMemberships ;
    private List<AiQuota> aiQuotas ;
    private List<AiUsageLog> aiUsageLogs ;
    private List<FinancialReport> financialReports ;

    public Enterprise(){
        super();
    }

    public Enterprise(Long id){
        this.id = id;
    }

    public Enterprise(Long id,String name){
        this.id = id;
        this.name = name ;
    }
    public Enterprise(String name){
        this.name = name ;
    }




    @Id
    @Column(name = "id")
    @GeneratedValue(strategy =  GenerationType.SEQUENCE,generator="enterprise_seq")
      @Override
    public Long getId(){
        return this.id;
    }
        @Override
    public void setId(Long id){
        this.id = id;
    }
    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getPhone(){
        return this.phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    public String getAddress(){
        return this.address;
    }
    public void setAddress(String address){
        this.address = address;
    }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency")
    public Currency getCurrency(){
        return this.currency;
    }
    public void setCurrency(Currency currency){
        this.currency = currency;
    }
    @OneToMany(mappedBy = "enterprise")
    public List<Property> getProperties(){
        return this.properties;
    }

    public void setProperties(List<Property> properties){
        this.properties = properties;
    }
    @OneToMany(mappedBy = "enterprise")
    public List<Client> getClients(){
        return this.clients;
    }

    public void setClients(List<Client> clients){
        this.clients = clients;
    }
    @OneToMany(mappedBy = "enterprise")
    public List<ServiceProvider> getServiceProviders(){
        return this.serviceProviders;
    }

    public void setServiceProviders(List<ServiceProvider> serviceProviders){
        this.serviceProviders = serviceProviders;
    }
    @OneToMany(mappedBy = "enterprise")
    public List<EnterpriseMembership> getEnterpriseMemberships(){
        return this.enterpriseMemberships;
    }

    public void setEnterpriseMemberships(List<EnterpriseMembership> enterpriseMemberships){
        this.enterpriseMemberships = enterpriseMemberships;
    }
    @OneToMany(mappedBy = "enterprise")
    public List<AiQuota> getAiQuotas(){
        return this.aiQuotas;
    }

    public void setAiQuotas(List<AiQuota> aiQuotas){
        this.aiQuotas = aiQuotas;
    }
    @OneToMany(mappedBy = "enterprise")
    public List<AiUsageLog> getAiUsageLogs(){
        return this.aiUsageLogs;
    }

    public void setAiUsageLogs(List<AiUsageLog> aiUsageLogs){
        this.aiUsageLogs = aiUsageLogs;
    }
    @OneToMany(mappedBy = "enterprise")
    public List<FinancialReport> getFinancialReports(){
        return this.financialReports;
    }

    public void setFinancialReports(List<FinancialReport> financialReports){
        this.financialReports = financialReports;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enterprise enterprise = (Enterprise) o;
        return id != null && id.equals(enterprise.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

