package  ma.zyn.app.ws.dto.currency;

import ma.zyn.app.zynerator.dto.AuditBaseDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


import ma.zyn.app.ws.dto.ai.AiQuotaDto;
import ma.zyn.app.ws.dto.ai.AiUsageLogDto;
import ma.zyn.app.ws.dto.task.TaskDto;
import ma.zyn.app.ws.dto.reservation.ReservationRequestDto;
import ma.zyn.app.ws.dto.report.FinancialReportDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseMembershipDto;
import ma.zyn.app.ws.dto.auth.CollaboratorDto;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
import ma.zyn.app.ws.dto.property.PropertyDto;
import ma.zyn.app.ws.dto.client.ClientDto;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class CurrencyDto  extends AuditBaseDto {

    private String description  ;
    private String code  ;
    private String label  ;
    private String style  ;
    private Boolean isDefault  ;
    private Integer sortOrder  = 0 ;
    private String symbol  ;


    private List<ExchangeRateDto> exchangeRatesAsBase ;
    private List<ExchangeRateDto> exchangeRatesAsTarget ;
    private List<EnterpriseDto> enterprises ;
    private List<CollaboratorDto> collaborators ;


    public CurrencyDto(){
        super();
    }



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

    public Boolean getIsDefault(){
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





    public List<ExchangeRateDto> getExchangeRatesAsBase(){
        return this.exchangeRatesAsBase;
    }

    public void setExchangeRatesAsBase(List<ExchangeRateDto> exchangeRatesAsBase){
        this.exchangeRatesAsBase = exchangeRatesAsBase;
    }
    public List<ExchangeRateDto> getExchangeRatesAsTarget(){
        return this.exchangeRatesAsTarget;
    }

    public void setExchangeRatesAsTarget(List<ExchangeRateDto> exchangeRatesAsTarget){
        this.exchangeRatesAsTarget = exchangeRatesAsTarget;
    }
    public List<EnterpriseDto> getEnterprises(){
        return this.enterprises;
    }

    public void setEnterprises(List<EnterpriseDto> enterprises){
        this.enterprises = enterprises;
    }
    public List<CollaboratorDto> getCollaborators(){
        return this.collaborators;
    }

    public void setCollaborators(List<CollaboratorDto> collaborators){
        this.collaborators = collaborators;
    }



}
