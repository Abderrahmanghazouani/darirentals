package  ma.zyn.app.ws.converter.currency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.ai.AiQuotaConverter;
import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.ws.converter.ai.AiUsageLogConverter;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.ws.converter.currency.ExchangeRateConverter;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.ws.converter.task.TaskConverter;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.ws.converter.reservation.ReservationRequestConverter;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.ws.converter.report.FinancialReportConverter;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.ws.converter.enterprise.EnterpriseMembershipConverter;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.ws.converter.client.ClientConverter;
import ma.zyn.app.bean.core.client.Client;



import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.ws.dto.currency.CurrencyDto;

@Component
public class CurrencyConverter {

    @Autowired
    private AiQuotaConverter aiQuotaConverter ;
    @Autowired
    private AiUsageLogConverter aiUsageLogConverter ;
    @Autowired
    private ExchangeRateConverter exchangeRateConverter ;
    @Autowired
    private TaskConverter taskConverter ;
    @Autowired
    private ReservationRequestConverter reservationRequestConverter ;
    @Autowired
    private FinancialReportConverter financialReportConverter ;
    @Autowired
    private EnterpriseMembershipConverter enterpriseMembershipConverter ;
    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private ServiceProviderConverter serviceProviderConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    @Autowired
    private ClientConverter clientConverter ;
    private boolean exchangeRatesAsBase;
    private boolean exchangeRatesAsTarget;
    private boolean enterprises;
    private boolean collaborators;

    public  CurrencyConverter() {
        initList(true);
    }

    public Currency toItem(CurrencyDto dto) {
        if (dto == null) {
            return null;
        } else {
        Currency item = new Currency();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getDescription()))
                item.setDescription(dto.getDescription());
            if(StringUtil.isNotEmpty(dto.getCode()))
                item.setCode(dto.getCode());
            if(StringUtil.isNotEmpty(dto.getLabel()))
                item.setLabel(dto.getLabel());
            if(StringUtil.isNotEmpty(dto.getStyle()))
                item.setStyle(dto.getStyle());
            if(dto.getIsDefault() != null)
                item.setIsDefault(dto.getIsDefault());
            if(StringUtil.isNotEmpty(dto.getSortOrder()))
                item.setSortOrder(dto.getSortOrder());
            if(StringUtil.isNotEmpty(dto.getSymbol()))
                item.setSymbol(dto.getSymbol());

            if(this.exchangeRatesAsBase && ListUtil.isNotEmpty(dto.getExchangeRatesAsBase()))
                item.setExchangeRatesAsBase(exchangeRateConverter.toItem(dto.getExchangeRatesAsBase()));
            if(this.exchangeRatesAsTarget && ListUtil.isNotEmpty(dto.getExchangeRatesAsTarget()))
                item.setExchangeRatesAsTarget(exchangeRateConverter.toItem(dto.getExchangeRatesAsTarget()));
            if(this.enterprises && ListUtil.isNotEmpty(dto.getEnterprises()))
                item.setEnterprises(enterpriseConverter.toItem(dto.getEnterprises()));
            if(this.collaborators && ListUtil.isNotEmpty(dto.getCollaborators()))
                item.setCollaborators(collaboratorConverter.toItem(dto.getCollaborators()));


        return item;
        }
    }


    public CurrencyDto toDto(Currency item) {
        if (item == null) {
            return null;
        } else {
            CurrencyDto dto = new CurrencyDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getDescription()))
                dto.setDescription(item.getDescription());
            if(StringUtil.isNotEmpty(item.getCode()))
                dto.setCode(item.getCode());
            if(StringUtil.isNotEmpty(item.getLabel()))
                dto.setLabel(item.getLabel());
            if(StringUtil.isNotEmpty(item.getStyle()))
                dto.setStyle(item.getStyle());
                dto.setIsDefault(item.getIsDefault());
            if(StringUtil.isNotEmpty(item.getSortOrder()))
                dto.setSortOrder(item.getSortOrder());
            if(StringUtil.isNotEmpty(item.getSymbol()))
                dto.setSymbol(item.getSymbol());
        if(this.exchangeRatesAsBase && ListUtil.isNotEmpty(item.getExchangeRatesAsBase())){
            exchangeRateConverter.init(true);
            exchangeRateConverter.setBaseCurrency(false);
            exchangeRateConverter.setTargetCurrency(false);
            dto.setExchangeRatesAsBase(exchangeRateConverter.toDto(item.getExchangeRatesAsBase()));
            exchangeRateConverter.setBaseCurrency(true);
            exchangeRateConverter.setTargetCurrency(true);

        }
        if(this.exchangeRatesAsTarget && ListUtil.isNotEmpty(item.getExchangeRatesAsTarget())){
            exchangeRateConverter.init(true);
            exchangeRateConverter.setBaseCurrency(false);
            exchangeRateConverter.setTargetCurrency(false);
            dto.setExchangeRatesAsTarget(exchangeRateConverter.toDto(item.getExchangeRatesAsTarget()));
            exchangeRateConverter.setBaseCurrency(true);
            exchangeRateConverter.setTargetCurrency(true);

        }
        if(this.enterprises && ListUtil.isNotEmpty(item.getEnterprises())){
            enterpriseConverter.init(true);
            enterpriseConverter.setCurrency(false);
            dto.setEnterprises(enterpriseConverter.toDto(item.getEnterprises()));
            enterpriseConverter.setCurrency(true);

        }
        if(this.collaborators && ListUtil.isNotEmpty(item.getCollaborators())){
            collaboratorConverter.init(true);
            collaboratorConverter.setDisplayCurrency(false);
            dto.setCollaborators(collaboratorConverter.toDto(item.getCollaborators()));
            collaboratorConverter.setDisplayCurrency(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.exchangeRatesAsBase = value;
        this.exchangeRatesAsTarget = value;
        this.enterprises = value;
        this.collaborators = value;
    }
	
    public List<Currency> toItem(List<CurrencyDto> dtos) {
        List<Currency> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (CurrencyDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<CurrencyDto> toDto(List<Currency> items) {
        List<CurrencyDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (Currency item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(CurrencyDto dto, Currency t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if (dto.getExchangeRatesAsBase() != null)
            t.setExchangeRatesAsBase(exchangeRateConverter.copy(dto.getExchangeRatesAsBase()));
        if (dto.getExchangeRatesAsTarget() != null)
            t.setExchangeRatesAsTarget(exchangeRateConverter.copy(dto.getExchangeRatesAsTarget()));
        if (dto.getEnterprises() != null)
            t.setEnterprises(enterpriseConverter.copy(dto.getEnterprises()));
        if (dto.getCollaborators() != null)
            t.setCollaborators(collaboratorConverter.copy(dto.getCollaborators()));
    }

    public List<Currency> copy(List<CurrencyDto> dtos) {
        List<Currency> result = new ArrayList<>();
        if (dtos != null) {
            for (CurrencyDto dto : dtos) {
                Currency instance = new Currency();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
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
    public ExchangeRateConverter getExchangeRateConverter(){
        return this.exchangeRateConverter;
    }
    public void setExchangeRateConverter(ExchangeRateConverter exchangeRateConverter ){
        this.exchangeRateConverter = exchangeRateConverter;
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
    public EnterpriseMembershipConverter getEnterpriseMembershipConverter(){
        return this.enterpriseMembershipConverter;
    }
    public void setEnterpriseMembershipConverter(EnterpriseMembershipConverter enterpriseMembershipConverter ){
        this.enterpriseMembershipConverter = enterpriseMembershipConverter;
    }
    public CollaboratorConverter getCollaboratorConverter(){
        return this.collaboratorConverter;
    }
    public void setCollaboratorConverter(CollaboratorConverter collaboratorConverter ){
        this.collaboratorConverter = collaboratorConverter;
    }
    public ServiceProviderConverter getServiceProviderConverter(){
        return this.serviceProviderConverter;
    }
    public void setServiceProviderConverter(ServiceProviderConverter serviceProviderConverter ){
        this.serviceProviderConverter = serviceProviderConverter;
    }
    public EnterpriseConverter getEnterpriseConverter(){
        return this.enterpriseConverter;
    }
    public void setEnterpriseConverter(EnterpriseConverter enterpriseConverter ){
        this.enterpriseConverter = enterpriseConverter;
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
    public boolean  isExchangeRatesAsBase(){
        return this.exchangeRatesAsBase ;
    }
    public void  setExchangeRatesAsBase(boolean exchangeRatesAsBase ){
        this.exchangeRatesAsBase  = exchangeRatesAsBase ;
    }
    public boolean  isExchangeRatesAsTarget(){
        return this.exchangeRatesAsTarget ;
    }
    public void  setExchangeRatesAsTarget(boolean exchangeRatesAsTarget ){
        this.exchangeRatesAsTarget  = exchangeRatesAsTarget ;
    }
    public boolean  isEnterprises(){
        return this.enterprises ;
    }
    public void  setEnterprises(boolean enterprises ){
        this.enterprises  = enterprises ;
    }
    public boolean  isCollaborators(){
        return this.collaborators ;
    }
    public void  setCollaborators(boolean collaborators ){
        this.collaborators  = collaborators ;
    }
}
