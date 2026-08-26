package  ma.zyn.app.ws.converter.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;
import ma.zyn.app.zynerator.util.ListUtil;

import ma.zyn.app.ws.converter.auth.CollaboratorConverter;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.ws.converter.report.FinancialReportTypeConverter;
import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.ws.converter.report.FinancialReportPropertyConverter;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.ws.converter.report.FinancialReportScopeConverter;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;

import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.bean.core.auth.Collaborator;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.ws.dto.report.FinancialReportDto;

@Component
public class FinancialReportConverter {

    @Autowired
    private CollaboratorConverter collaboratorConverter ;
    @Autowired
    private FinancialReportTypeConverter financialReportTypeConverter ;
    @Autowired
    private FinancialReportPropertyConverter financialReportPropertyConverter ;
    @Autowired
    private EnterpriseConverter enterpriseConverter ;
    @Autowired
    private FinancialReportScopeConverter financialReportScopeConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    private boolean financialReportType;
    private boolean financialReportScope;
    private boolean enterprise;
    private boolean generatedBy;
    private boolean financialReportProperties;

    public  FinancialReportConverter() {
        init(true);
    }

    public FinancialReport toItem(FinancialReportDto dto) {
        if (dto == null) {
            return null;
        } else {
        FinancialReport item = new FinancialReport();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(StringUtil.isNotEmpty(dto.getTotalRevenue()))
                item.setTotalRevenue(dto.getTotalRevenue());
            if(StringUtil.isNotEmpty(dto.getTotalCharges()))
                item.setTotalCharges(dto.getTotalCharges());
            if(StringUtil.isNotEmpty(dto.getNetProfit()))
                item.setNetProfit(dto.getNetProfit());
            if(dto.getPeriodStart() != null)
                item.setPeriodStart(dto.getPeriodStart());
            if(dto.getPeriodEnd() != null)
                item.setPeriodEnd(dto.getPeriodEnd());
            if(StringUtil.isNotEmpty(dto.getGeneratedAt()))
                item.setGeneratedAt(DateUtil.stringEnToDate(dto.getGeneratedAt()));
            if(StringUtil.isNotEmpty(dto.getFile()))
                item.setFile(dto.getFile());
            if(this.financialReportType && dto.getFinancialReportType()!=null)
                item.setFinancialReportType(financialReportTypeConverter.toItem(dto.getFinancialReportType())) ;

            if(this.financialReportScope && dto.getFinancialReportScope()!=null)
                item.setFinancialReportScope(financialReportScopeConverter.toItem(dto.getFinancialReportScope())) ;

            if(dto.getEnterprise() != null && dto.getEnterprise().getId() != null){
                item.setEnterprise(new Enterprise());
                item.getEnterprise().setId(dto.getEnterprise().getId());
                item.getEnterprise().setName(dto.getEnterprise().getName());
            }

            if(dto.getGeneratedBy() != null && dto.getGeneratedBy().getId() != null){
                item.setGeneratedBy(new Collaborator());
                item.getGeneratedBy().setId(dto.getGeneratedBy().getId());
                item.getGeneratedBy().setName(dto.getGeneratedBy().getName());
            }


            if(this.financialReportProperties && ListUtil.isNotEmpty(dto.getFinancialReportProperties()))
                item.setFinancialReportProperties(financialReportPropertyConverter.toItem(dto.getFinancialReportProperties()));


        return item;
        }
    }


    public FinancialReportDto toDto(FinancialReport item) {
        if (item == null) {
            return null;
        } else {
            FinancialReportDto dto = new FinancialReportDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(StringUtil.isNotEmpty(item.getTotalRevenue()))
                dto.setTotalRevenue(item.getTotalRevenue());
            if(StringUtil.isNotEmpty(item.getTotalCharges()))
                dto.setTotalCharges(item.getTotalCharges());
            if(StringUtil.isNotEmpty(item.getNetProfit()))
                dto.setNetProfit(item.getNetProfit());
            if(item.getPeriodStart() != null)
                dto.setPeriodStart(item.getPeriodStart());
            if(item.getPeriodEnd() != null)
                dto.setPeriodEnd(item.getPeriodEnd());
            if(item.getGeneratedAt()!=null)
                dto.setGeneratedAt(DateUtil.dateTimeToString(item.getGeneratedAt()));
            if(StringUtil.isNotEmpty(item.getFile()))
                dto.setFile(item.getFile());
            if(this.financialReportType && item.getFinancialReportType()!=null) {
                dto.setFinancialReportType(financialReportTypeConverter.toDto(item.getFinancialReportType())) ;

            }
            if(this.financialReportScope && item.getFinancialReportScope()!=null) {
                dto.setFinancialReportScope(financialReportScopeConverter.toDto(item.getFinancialReportScope())) ;

            }
            if(this.enterprise && item.getEnterprise()!=null) {
                // Meme garde-fou que dans AiUsageLogConverter/EnterpriseMembershipConverter :
                // evite de rouvrir le cycle Enterprise.aiUsageLogs/enterpriseMemberships ->
                // AiUsageLog/EnterpriseMembership -> ... -> Enterprise -> ... via ce chemin
                // (Enterprise.financialReports -> FinancialReport.enterprise).
                boolean savedEnterpriseAiUsageLogs = enterpriseConverter.isAiUsageLogs();
                boolean savedEnterpriseEnterpriseMemberships = enterpriseConverter.isEnterpriseMemberships();
                enterpriseConverter.setAiUsageLogs(false);
                enterpriseConverter.setEnterpriseMemberships(false);
                dto.setEnterprise(enterpriseConverter.toDto(item.getEnterprise())) ;
                enterpriseConverter.setAiUsageLogs(savedEnterpriseAiUsageLogs);
                enterpriseConverter.setEnterpriseMemberships(savedEnterpriseEnterpriseMemberships);

            }
            if(this.generatedBy && item.getGeneratedBy()!=null) {
                // Meme garde-fou : evite Enterprise.financialReports -> FinancialReport.
                // generatedBy -> Collaborator.enterpriseMemberships/aiUsageLogs -> ... -> Enterprise.
                boolean savedCollaboratorEnterpriseMemberships = collaboratorConverter.isEnterpriseMemberships();
                boolean savedCollaboratorAiUsageLogs = collaboratorConverter.isAiUsageLogs();
                collaboratorConverter.setEnterpriseMemberships(false);
                collaboratorConverter.setAiUsageLogs(false);
                dto.setGeneratedBy(collaboratorConverter.toDto(item.getGeneratedBy())) ;
                collaboratorConverter.setEnterpriseMemberships(savedCollaboratorEnterpriseMemberships);
                collaboratorConverter.setAiUsageLogs(savedCollaboratorAiUsageLogs);

            }
        if(this.financialReportProperties && ListUtil.isNotEmpty(item.getFinancialReportProperties())){
            financialReportPropertyConverter.init(true);
            financialReportPropertyConverter.setFinancialReport(false);
            dto.setFinancialReportProperties(financialReportPropertyConverter.toDto(item.getFinancialReportProperties()));
            financialReportPropertyConverter.setFinancialReport(true);

        }


        return dto;
        }
    }

    public void init(boolean value) {
        initList(value);
    }

    public void initList(boolean value) {
        this.financialReportProperties = value;
    }
    public void initObject(boolean value) {
        this.financialReportType = value;
        this.financialReportScope = value;
        this.enterprise = value;
        this.generatedBy = value;
    }
	
    public List<FinancialReport> toItem(List<FinancialReportDto> dtos) {
        List<FinancialReport> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (FinancialReportDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<FinancialReportDto> toDto(List<FinancialReport> items) {
        List<FinancialReportDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (FinancialReport item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(FinancialReportDto dto, FinancialReport t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getFinancialReportType() == null  && dto.getFinancialReportType() != null){
            t.setFinancialReportType(new FinancialReportType());
        }else if (t.getFinancialReportType() != null  && dto.getFinancialReportType() != null){
            t.setFinancialReportType(null);
            t.setFinancialReportType(new FinancialReportType());
        }
        if(t.getFinancialReportScope() == null  && dto.getFinancialReportScope() != null){
            t.setFinancialReportScope(new FinancialReportScope());
        }else if (t.getFinancialReportScope() != null  && dto.getFinancialReportScope() != null){
            t.setFinancialReportScope(null);
            t.setFinancialReportScope(new FinancialReportScope());
        }
        if(t.getEnterprise() == null  && dto.getEnterprise() != null){
            t.setEnterprise(new Enterprise());
        }else if (t.getEnterprise() != null  && dto.getEnterprise() != null){
            t.setEnterprise(null);
            t.setEnterprise(new Enterprise());
        }
        if(t.getGeneratedBy() == null  && dto.getGeneratedBy() != null){
            t.setGeneratedBy(new Collaborator());
        }else if (t.getGeneratedBy() != null  && dto.getGeneratedBy() != null){
            t.setGeneratedBy(null);
            t.setGeneratedBy(new Collaborator());
        }
        if (dto.getFinancialReportType() != null)
        financialReportTypeConverter.copy(dto.getFinancialReportType(), t.getFinancialReportType());
        if (dto.getFinancialReportScope() != null)
        financialReportScopeConverter.copy(dto.getFinancialReportScope(), t.getFinancialReportScope());
        if (dto.getEnterprise() != null)
        enterpriseConverter.copy(dto.getEnterprise(), t.getEnterprise());
        if (dto.getGeneratedBy() != null)
        collaboratorConverter.copy(dto.getGeneratedBy(), t.getGeneratedBy());
        if (dto.getFinancialReportProperties() != null)
            t.setFinancialReportProperties(financialReportPropertyConverter.copy(dto.getFinancialReportProperties()));
    }

    public List<FinancialReport> copy(List<FinancialReportDto> dtos) {
        List<FinancialReport> result = new ArrayList<>();
        if (dtos != null) {
            for (FinancialReportDto dto : dtos) {
                FinancialReport instance = new FinancialReport();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public CollaboratorConverter getCollaboratorConverter(){
        return this.collaboratorConverter;
    }
    public void setCollaboratorConverter(CollaboratorConverter collaboratorConverter ){
        this.collaboratorConverter = collaboratorConverter;
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
    public EnterpriseConverter getEnterpriseConverter(){
        return this.enterpriseConverter;
    }
    public void setEnterpriseConverter(EnterpriseConverter enterpriseConverter ){
        this.enterpriseConverter = enterpriseConverter;
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
    public boolean  isFinancialReportType(){
        return this.financialReportType;
    }
    public void  setFinancialReportType(boolean financialReportType){
        this.financialReportType = financialReportType;
    }
    public boolean  isFinancialReportScope(){
        return this.financialReportScope;
    }
    public void  setFinancialReportScope(boolean financialReportScope){
        this.financialReportScope = financialReportScope;
    }
    public boolean  isEnterprise(){
        return this.enterprise;
    }
    public void  setEnterprise(boolean enterprise){
        this.enterprise = enterprise;
    }
    public boolean  isGeneratedBy(){
        return this.generatedBy;
    }
    public void  setGeneratedBy(boolean generatedBy){
        this.generatedBy = generatedBy;
    }
    public boolean  isFinancialReportProperties(){
        return this.financialReportProperties ;
    }
    public void  setFinancialReportProperties(boolean financialReportProperties ){
        this.financialReportProperties  = financialReportProperties ;
    }
}
