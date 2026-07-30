package  ma.zyn.app.ws.dto.enterprise;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


import ma.zyn.app.ws.dto.report.FinancialReportTypeDto;
import ma.zyn.app.ws.dto.report.FinancialReportPropertyDto;
import ma.zyn.app.ws.dto.property.CityDto;
import ma.zyn.app.ws.dto.auth.CollaboratorRoleDto;
import ma.zyn.app.ws.dto.charge.ChargeDto;
import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.payment.PaymentDto;
import ma.zyn.app.ws.dto.auth.CollaboratorPermissionOverrideDto;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
import ma.zyn.app.ws.dto.property.PropertyStatusDto;
import ma.zyn.app.ws.dto.ai.AiQuotaDto;
import ma.zyn.app.ws.dto.ai.AiUsageLogDto;
import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;
import ma.zyn.app.ws.dto.report.FinancialReportDto;
import ma.zyn.app.ws.dto.property.PropertyTypeDto;
import ma.zyn.app.ws.dto.document.DocumentDto;
import ma.zyn.app.ws.dto.reservation.ReservationDto;
import ma.zyn.app.ws.dto.ai.AiUsageTypeDto;
import ma.zyn.app.ws.dto.currency.CurrencyDto;
import ma.zyn.app.ws.dto.provider.ServiceTypeDto;
import ma.zyn.app.ws.dto.report.FinancialReportScopeDto;
import ma.zyn.app.ws.dto.property.PropertyDto;
import ma.zyn.app.ws.dto.client.ClientDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class EnterpriseDto  extends AuditBaseDto {

    private String name  ;
    private String phone  ;
    private String address  ;

    private CurrencyDto currency ;

    private List<PropertyDto> properties ;
    private List<ClientDto> clients ;
    private List<ServiceProviderDto> serviceProviders ;
    private List<EnterpriseMembershipDto> enterpriseMemberships ;
    private List<AiQuotaDto> aiQuotas ;
    private List<AiUsageLogDto> aiUsageLogs ;
    private List<FinancialReportDto> financialReports ;


    public EnterpriseDto(){
        super();
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


    public CurrencyDto getCurrency(){
        return this.currency;
    }

    public void setCurrency(CurrencyDto currency){
        this.currency = currency;
    }



    public List<PropertyDto> getProperties(){
        return this.properties;
    }

    public void setProperties(List<PropertyDto> properties){
        this.properties = properties;
    }
    public List<ClientDto> getClients(){
        return this.clients;
    }

    public void setClients(List<ClientDto> clients){
        this.clients = clients;
    }
    public List<ServiceProviderDto> getServiceProviders(){
        return this.serviceProviders;
    }

    public void setServiceProviders(List<ServiceProviderDto> serviceProviders){
        this.serviceProviders = serviceProviders;
    }
    public List<EnterpriseMembershipDto> getEnterpriseMemberships(){
        return this.enterpriseMemberships;
    }

    public void setEnterpriseMemberships(List<EnterpriseMembershipDto> enterpriseMemberships){
        this.enterpriseMemberships = enterpriseMemberships;
    }
    public List<AiQuotaDto> getAiQuotas(){
        return this.aiQuotas;
    }

    public void setAiQuotas(List<AiQuotaDto> aiQuotas){
        this.aiQuotas = aiQuotas;
    }
    public List<AiUsageLogDto> getAiUsageLogs(){
        return this.aiUsageLogs;
    }

    public void setAiUsageLogs(List<AiUsageLogDto> aiUsageLogs){
        this.aiUsageLogs = aiUsageLogs;
    }
    public List<FinancialReportDto> getFinancialReports(){
        return this.financialReports;
    }

    public void setFinancialReports(List<FinancialReportDto> financialReports){
        this.financialReports = financialReports;
    }



}
