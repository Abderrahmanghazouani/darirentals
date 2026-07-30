package  ma.zyn.app.ws.converter.enterprise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.report.FinancialReportTypeConverter;
import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.ws.converter.report.FinancialReportPropertyConverter;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.ws.converter.enterprise.EnterpriseMembershipConverter;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.ws.converter.property.CityConverter;
import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.ws.converter.auth.CollaboratorRoleConverter;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.ws.converter.charge.ChargeConverter;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.payment.PaymentConverter;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.ws.converter.auth.CollaboratorPermissionOverrideConverter;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.ws.converter.property.PropertyStatusConverter;
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.ws.converter.ai.AiQuotaConverter;
import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.ws.converter.ai.AiUsageLogConverter;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.ws.converter.task.TaskConverter;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.ws.converter.report.FinancialReportConverter;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.ws.converter.property.PropertyTypeConverter;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.ws.converter.document.DocumentConverter;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.ws.converter.reservation.ReservationConverter;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.ws.converter.ai.AiUsageTypeConverter;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.ws.converter.currency.CurrencyConverter;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.ws.converter.provider.ServiceTypeConverter;
import ma.zyn.app.bean.core.provider.ServiceType;
import ma.zyn.app.ws.converter.report.FinancialReportScopeConverter;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.client.ClientConverter;
import ma.zyn.app.bean.core.client.Client;

import ma.zyn.app.bean.core.currency.Currency;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;

@Component
public class EnterpriseConverter {

    @Autowired
    private FinancialReportTypeConverter financialReportTypeConverter ;
    @Autowired
    private FinancialReportPropertyConverter financialReportPropertyConverter ;
    @Autowired
    private EnterpriseMembershipConverter enterpriseMembershipConverter ;
    @Autowired
    private CityConverter cityConverter ;
    @Autowired
    private CollaboratorRoleConverter collaboratorRoleConverter ;
    @Autowired
    private ChargeConverter chargeConverter ;
    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private PaymentConverter paymentConverter ;
    @Autowired
    private CollaboratorPermissionOverrideConverter collaboratorPermissionOverrideConverter ;
    @Autowired
    private ServiceProviderConverter serviceProviderConverter ;
    @Autowired
    private PropertyStatusConverter propertyStatusConverter ;
    @Autowired
    private AiQuotaConverter aiQuotaConverter ;
    @Autowired
    private AiUsageLogConverter aiUsageLogConverter ;
    @Autowired
    private TaskConverter taskConverter ;
    @Autowired
    private ReservationRequestConverter reservationRequestConverter ;
    @Autowired
    private FinancialReportConverter financialReportConverter ;
    @Autowired
    private PropertyTypeConverter propertyTypeConverter ;
    @Autowired
    private DocumentConverter documentConverter ;
    @Autowired
    private ReservationConverter reservationConverter ;
    @Autowired
    private AiUsageTypeConverter aiUsageTypeConverter ;
    @Autowired
    private CurrencyConverter currencyConverter ;
    @Autowired
    private ServiceTypeConverter serviceTypeConverter ;
    @Autowired
    private FinancialReportScopeConverter financialReportScopeConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private ClientConverter clientConverter ;
    private boolean currency;
    private boolean properties;
    private boolean clients;
    private boolean serviceProviders;
    private boolean enterpriseMemberships;
    private boolean aiQuotas;
    private boolean aiUsageLogs;
    private boolean financialReports;

    public  EnterpriseConverter() {
        init(true);
    }

    public Enterprise toItem(EnterpriseDto dto) {
        if (dto == null) {
            return null;
        } else {
        Enterprise item = new Enterprise();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getName()))
                item.setName(dto.getName());
            if(StringUtil.isNotEmpty(dto.getPhone()))
                item.setPhone(dto.getPhone());
            if(StringUtil.isNotEmpty(dto.getAddress()))
                item.setAddress(dto.getAddress());
            if(dto.getCurrency() != null && dto.getCurrency().getId() != null){
                item.setCurrency(new Currency());
                item.getCurrency().setId(dto.getCurrency().getId());
                item.getCurrency().setLabel(dto.getCurrency().getLabel());
            }


            if(this.properties && ListUtil.isNotEmpty(dto.getProperties()))
                item.setProperties(propertyConverter.toItem(dto.getProperties()));
            if(this.clients && ListUtil.isNotEmpty(dto.getClients()))
                item.setClients(clientConverter.toItem(dto.getClients()));
            if(this.serviceProviders && ListUtil.isNotEmpty(dto.getServiceProviders()))
                item.setServiceProviders(serviceProviderConverter.toItem(dto.getServiceProviders()));
            if(this.enterpriseMemberships && ListUtil.isNotEmpty(dto.getEnterpriseMemberships()))
                item.setEnterpriseMemberships(enterpriseMembershipConverter.toItem(dto.getEnterpriseMemberships()));
            if(this.aiQuotas && ListUtil.isNotEmpty(dto.getAiQuotas()))
                item.setAiQuotas(aiQuotaConverter.toItem(dto.getAiQuotas()));
            if(this.aiUsageLogs && ListUtil.isNotEmpty(dto.getAiUsageLogs()))
                item.setAiUsageLogs(aiUsageLogConverter.toItem(dto.getAiUsageLogs()));
            if(this.financialReports && ListUtil.isNotEmpty(dto.getFinancialReports()))
                item.setFinancialReports(financialReportConverter.toItem(dto.getFinancialReports()));


        return item;
        }
    }


    public EnterpriseDto toDto(Enterprise item) {
        if (item == null) {
            return null;
        } else {
            EnterpriseDto dto = new EnterpriseDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getName()))
                dto.setName(item.getName());
            if(StringUtil.isNotEmpty(item.getPhone()))
                dto.setPhone(item.getPhone());
            if(StringUtil.isNotEmpty(item.getAddress()))
                dto.setAddress(item.getAddress());
            if(this.currency && item.getCurrency()!=null) {
                dto.setCurrency(currencyConverter.toDto(item.getCurrency())) ;

            }
        if(this.properties && ListUtil.isNotEmpty(item.getProperties())){
            propertyConverter.init(true);
            propertyConverter.setEnterprise(false);
            dto.setProperties(propertyConverter.toDto(item.getProperties()));
            propertyConverter.setEnterprise(true);

        }
        if(this.clients && ListUtil.isNotEmpty(item.getClients())){
            clientConverter.init(true);
            clientConverter.setEnterprise(false);
            dto.setClients(clientConverter.toDto(item.getClients()));
            clientConverter.setEnterprise(true);

        }
        if(this.serviceProviders && ListUtil.isNotEmpty(item.getServiceProviders())){
            serviceProviderConverter.init(true);
            serviceProviderConverter.setEnterprise(false);
            dto.setServiceProviders(serviceProviderConverter.toDto(item.getServiceProviders()));
            serviceProviderConverter.setEnterprise(true);

        }
        if(this.enterpriseMemberships && ListUtil.isNotEmpty(item.getEnterpriseMemberships())){
            enterpriseMembershipConverter.init(true);
            enterpriseMembershipConverter.setEnterprise(false);
            dto.setEnterpriseMemberships(enterpriseMembershipConverter.toDto(item.getEnterpriseMemberships()));
            enterpriseMembershipConverter.setEnterprise(true);

        }
        if(this.aiQuotas && ListUtil.isNotEmpty(item.getAiQuotas())){
            aiQuotaConverter.init(true);
            aiQuotaConverter.setEnterprise(false);
            dto.setAiQuotas(aiQuotaConverter.toDto(item.getAiQuotas()));
            aiQuotaConverter.setEnterprise(true);

        }
        if(this.aiUsageLogs && ListUtil.isNotEmpty(item.getAiUsageLogs())){
            aiUsageLogConverter.init(true);
            aiUsageLogConverter.setEnterprise(false);
            dto.setAiUsageLogs(aiUsageLogConverter.toDto(item.getAiUsageLogs()));
            aiUsageLogConverter.setEnterprise(true);

        }
        if(this.financialReports && ListUtil.isNotEmpty(item.getFinancialReports())){
            financialReportConverter.init(true);
            financialReportConverter.setEnterprise(false);
            dto.setFinancialReports(financialReportConverter.toDto(item.getFinancialReports()));
            financialReportConverter.setEnterprise(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.properties = value;
        this.clients = value;
        this.serviceProviders = value;
        this.enterpriseMemberships = value;
        this.aiQuotas = value;
        this.aiUsageLogs = value;
        this.financialReports = value;
    }
    public void initObject(boolean value) {
        this.currency = value;
    }
	
    public List<Enterprise> toItem(List<EnterpriseDto> dtos) {
        List<Enterprise> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (EnterpriseDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<EnterpriseDto> toDto(List<Enterprise> items) {
        List<EnterpriseDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Enterprise item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(EnterpriseDto dto, Enterprise t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getCurrency() == null  && dto.getCurrency() != null){
            t.setCurrency(new Currency());
        }else if (t.getCurrency() != null  && dto.getCurrency() != null){
            t.setCurrency(null);
            t.setCurrency(new Currency());
        }
        if (dto.getCurrency() != null)
        currencyConverter.copy(dto.getCurrency(), t.getCurrency());
        if (dto.getProperties() != null)
            t.setProperties(propertyConverter.copy(dto.getProperties()));
        if (dto.getClients() != null)
            t.setClients(clientConverter.copy(dto.getClients()));
        if (dto.getServiceProviders() != null)
            t.setServiceProviders(serviceProviderConverter.copy(dto.getServiceProviders()));
        if (dto.getEnterpriseMemberships() != null)
            t.setEnterpriseMemberships(enterpriseMembershipConverter.copy(dto.getEnterpriseMemberships()));
        if (dto.getAiQuotas() != null)
            t.setAiQuotas(aiQuotaConverter.copy(dto.getAiQuotas()));
        if (dto.getAiUsageLogs() != null)
            t.setAiUsageLogs(aiUsageLogConverter.copy(dto.getAiUsageLogs()));
        if (dto.getFinancialReports() != null)
            t.setFinancialReports(financialReportConverter.copy(dto.getFinancialReports()));
    }

    public List<Enterprise> copy(List<EnterpriseDto> dtos) {
        List<Enterprise> result = new ArrayList<>();
        if (dtos != null) {
            for (EnterpriseDto dto : dtos) {
                Enterprise instance = new Enterprise();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public FinancialReportTypeConverter getFinancialReportTypeConverter(){
        return this.financialReportTypeConverter;
    }
    public void setFinancialReportTypeConverter(FinancialReportTypeConverter financialReportTypeConverter ){
        this.financialReportTypeConverter = financialReportTypeConverter;
    }
    public FinancialReportPropertyConverter getFinancialReportPropertyConverter(){
        return this.financialReportPropertyConverter;
    }
    public void setFinancialReportPropertyConverter(FinancialReportPropertyConverter financialReportPropertyConverter ){
        this.financialReportPropertyConverter = financialReportPropertyConverter;
    }
    public EnterpriseMembershipConverter getEnterpriseMembershipConverter(){
        return this.enterpriseMembershipConverter;
    }
    public void setEnterpriseMembershipConverter(EnterpriseMembershipConverter enterpriseMembershipConverter ){
        this.enterpriseMembershipConverter = enterpriseMembershipConverter;
    }
    public CityConverter getCityConverter(){
        return this.cityConverter;
    }
    public void setCityConverter(CityConverter cityConverter ){
        this.cityConverter = cityConverter;
    }
    public CollaboratorRoleConverter getCollaboratorRoleConverter(){
        return this.collaboratorRoleConverter;
    }
    public void setCollaboratorRoleConverter(CollaboratorRoleConverter collaboratorRoleConverter ){
        this.collaboratorRoleConverter = collaboratorRoleConverter;
    }
    public ChargeConverter getChargeConverter(){
        return this.chargeConverter;
    }
    public void setChargeConverter(ChargeConverter chargeConverter ){
        this.chargeConverter = chargeConverter;
    }
    public CollaboratorConverter getCollaboratorConverter(){
        return this.collaboratorConverter;
    }
    public void setCollaboratorConverter(CollaboratorConverter collaboratorConverter ){
        this.collaboratorConverter = collaboratorConverter;
    }
    public PaymentConverter getPaymentConverter(){
        return this.paymentConverter;
    }
    public void setPaymentConverter(PaymentConverter paymentConverter ){
        this.paymentConverter = paymentConverter;
    }
    public CollaboratorPermissionOverrideConverter getCollaboratorPermissionOverrideConverter(){
        return this.collaboratorPermissionOverrideConverter;
    }
    public void setCollaboratorPermissionOverrideConverter(CollaboratorPermissionOverrideConverter collaboratorPermissionOverrideConverter ){
        this.collaboratorPermissionOverrideConverter = collaboratorPermissionOverrideConverter;
    }
    public ServiceProviderConverter getServiceProviderConverter(){
        return this.serviceProviderConverter;
    }
    public void setServiceProviderConverter(ServiceProviderConverter serviceProviderConverter ){
        this.serviceProviderConverter = serviceProviderConverter;
    }
    public PropertyStatusConverter getPropertyStatusConverter(){
        return this.propertyStatusConverter;
    }
    public void setPropertyStatusConverter(PropertyStatusConverter propertyStatusConverter ){
        this.propertyStatusConverter = propertyStatusConverter;
    }
    public AiQuotaConverter getAiQuotaConverter(){
        return this.aiQuotaConverter;
    }
    public void setAiQuotaConverter(AiQuotaConverter aiQuotaConverter ){
        this.aiQuotaConverter = aiQuotaConverter;
    }
    public AiUsageLogConverter getAiUsageLogConverter(){
        return this.aiUsageLogConverter;
    }
    public void setAiUsageLogConverter(AiUsageLogConverter aiUsageLogConverter ){
        this.aiUsageLogConverter = aiUsageLogConverter;
    }
    public TaskConverter getTaskConverter(){
        return this.taskConverter;
    }
    public void setTaskConverter(TaskConverter taskConverter ){
        this.taskConverter = taskConverter;
    }
    public ReservationRequestConverter getReservationRequestConverter(){
        return this.reservationRequestConverter;
    }
    public void setReservationRequestConverter(ReservationRequestConverter reservationRequestConverter ){
        this.reservationRequestConverter = reservationRequestConverter;
    }
    public FinancialReportConverter getFinancialReportConverter(){
        return this.financialReportConverter;
    }
    public void setFinancialReportConverter(FinancialReportConverter financialReportConverter ){
        this.financialReportConverter = financialReportConverter;
    }
    public PropertyTypeConverter getPropertyTypeConverter(){
        return this.propertyTypeConverter;
    }
    public void setPropertyTypeConverter(PropertyTypeConverter propertyTypeConverter ){
        this.propertyTypeConverter = propertyTypeConverter;
    }
    public DocumentConverter getDocumentConverter(){
        return this.documentConverter;
    }
    public void setDocumentConverter(DocumentConverter documentConverter ){
        this.documentConverter = documentConverter;
    }
    public ReservationConverter getReservationConverter(){
        return this.reservationConverter;
    }
    public void setReservationConverter(ReservationConverter reservationConverter ){
        this.reservationConverter = reservationConverter;
    }
    public AiUsageTypeConverter getAiUsageTypeConverter(){
        return this.aiUsageTypeConverter;
    }
    public void setAiUsageTypeConverter(AiUsageTypeConverter aiUsageTypeConverter ){
        this.aiUsageTypeConverter = aiUsageTypeConverter;
    }
    public CurrencyConverter getCurrencyConverter(){
        return this.currencyConverter;
    }
    public void setCurrencyConverter(CurrencyConverter currencyConverter ){
        this.currencyConverter = currencyConverter;
    }
    public ServiceTypeConverter getServiceTypeConverter(){
        return this.serviceTypeConverter;
    }
    public void setServiceTypeConverter(ServiceTypeConverter serviceTypeConverter ){
        this.serviceTypeConverter = serviceTypeConverter;
    }
    public FinancialReportScopeConverter getFinancialReportScopeConverter(){
        return this.financialReportScopeConverter;
    }
    public void setFinancialReportScopeConverter(FinancialReportScopeConverter financialReportScopeConverter ){
        this.financialReportScopeConverter = financialReportScopeConverter;
    }
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
    }
    public ClientConverter getClientConverter(){
        return this.clientConverter;
    }
    public void setClientConverter(ClientConverter clientConverter ){
        this.clientConverter = clientConverter;
    }
    public boolean  isCurrency(){
        return this.currency;
    }
    public void  setCurrency(boolean currency){
        this.currency = currency;
    }
    public boolean  isProperties(){
        return this.properties ;
    }
    public void  setProperties(boolean properties ){
        this.properties  = properties ;
    }
    public boolean  isClients(){
        return this.clients ;
    }
    public void  setClients(boolean clients ){
        this.clients  = clients ;
    }
    public boolean  isServiceProviders(){
        return this.serviceProviders ;
    }
    public void  setServiceProviders(boolean serviceProviders ){
        this.serviceProviders  = serviceProviders ;
    }
    public boolean  isEnterpriseMemberships(){
        return this.enterpriseMemberships ;
    }
    public void  setEnterpriseMemberships(boolean enterpriseMemberships ){
        this.enterpriseMemberships  = enterpriseMemberships ;
    }
    public boolean  isAiQuotas(){
        return this.aiQuotas ;
    }
    public void  setAiQuotas(boolean aiQuotas ){
        this.aiQuotas  = aiQuotas ;
    }
    public boolean  isAiUsageLogs(){
        return this.aiUsageLogs ;
    }
    public void  setAiUsageLogs(boolean aiUsageLogs ){
        this.aiUsageLogs  = aiUsageLogs ;
    }
    public boolean  isFinancialReports(){
        return this.financialReports ;
    }
    public void  setFinancialReports(boolean financialReports ){
        this.financialReports  = financialReports ;
    }
}
