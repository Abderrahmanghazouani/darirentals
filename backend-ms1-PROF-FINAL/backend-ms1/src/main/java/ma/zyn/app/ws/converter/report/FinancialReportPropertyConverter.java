package  ma.zyn.app.ws.converter.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.beans.BeanUtils;
import ma.zyn.app.zynerator.converter.AbstractConverterHelper;

import java.util.ArrayList;
import java.util.List;

import ma.zyn.app.ws.converter.report.FinancialReportConverter;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.bean.core.property.Property;

import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.bean.core.property.Property;


import ma.zyn.app.zynerator.util.StringUtil;
import ma.zyn.app.zynerator.converter.AbstractConverter;
import ma.zyn.app.zynerator.util.DateUtil;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.ws.dto.report.FinancialReportPropertyDto;

@Component
public class FinancialReportPropertyConverter {

    @Autowired
    private FinancialReportConverter financialReportConverter ;
    @Autowired
    private PropertyConverter propertyConverter ;
    private boolean financialReport;
    private boolean property;

    public  FinancialReportPropertyConverter() {
        initObject(true);
    }

    public FinancialReportProperty toItem(FinancialReportPropertyDto dto) {
        if (dto == null) {
            return null;
        } else {
        FinancialReportProperty item = new FinancialReportProperty();
            if(StringUtil.isNotEmpty(dto.getId()))
                item.setId(dto.getId());
            if(dto.getFinancialReport() != null && dto.getFinancialReport().getId() != null){
                item.setFinancialReport(new FinancialReport());
                item.getFinancialReport().setId(dto.getFinancialReport().getId());
                item.getFinancialReport().setId(dto.getFinancialReport().getId());
            }

            if(dto.getProperty() != null && dto.getProperty().getId() != null){
                item.setProperty(new Property());
                item.getProperty().setId(dto.getProperty().getId());
                item.getProperty().setName(dto.getProperty().getName());
            }




        return item;
        }
    }


    public FinancialReportPropertyDto toDto(FinancialReportProperty item) {
        if (item == null) {
            return null;
        } else {
            FinancialReportPropertyDto dto = new FinancialReportPropertyDto();
            if(StringUtil.isNotEmpty(item.getId()))
                dto.setId(item.getId());
            if(this.financialReport && item.getFinancialReport()!=null) {
                // Meme logique que pour "property" ci-dessous : sans ce garde-fou, le cycle
                // FinancialReportProperty -> FinancialReport -> Enterprise -> properties ->
                // FinancialReportProperty -> ... provoque un StackOverflowError des que
                // l'enterprise du converter partage est en initObject(true) (ex: findAll()).
                boolean savedEnterprise = financialReportConverter.isEnterprise();
                boolean savedFinancialReportProperties = financialReportConverter.isFinancialReportProperties();
                financialReportConverter.setEnterprise(false);
                financialReportConverter.setFinancialReportProperties(false);
                dto.setFinancialReport(financialReportConverter.toDto(item.getFinancialReport())) ;
                financialReportConverter.setEnterprise(savedEnterprise);
                financialReportConverter.setFinancialReportProperties(savedFinancialReportProperties);

            }
            if(this.property && item.getProperty()!=null) {
                // Evite le cycle infini (StackOverflowError) Property -> Enterprise ->
                // properties -> financialReportProperties -> FinancialReport -> ... : on
                // desactive temporairement ces deux expansions sur le converter partage avant
                // de convertir la propriete, puis on restaure son etat precedent.
                boolean savedEnterprise = propertyConverter.isEnterprise();
                boolean savedFinancialReportProperties = propertyConverter.isFinancialReportProperties();
                propertyConverter.setEnterprise(false);
                propertyConverter.setFinancialReportProperties(false);
                dto.setProperty(propertyConverter.toDto(item.getProperty())) ;
                propertyConverter.setEnterprise(savedEnterprise);
                propertyConverter.setFinancialReportProperties(savedFinancialReportProperties);

            }


        return dto;
        }
    }

    public void init(boolean value) {
        initObject(value);
    }

    public void initObject(boolean value) {
        this.financialReport = value;
        this.property = value;
    }
	
    public List<FinancialReportProperty> toItem(List<FinancialReportPropertyDto> dtos) {
        List<FinancialReportProperty> items = new ArrayList<>();
        if (dtos != null && !dtos.isEmpty()) {
            for (FinancialReportPropertyDto dto : dtos) {
                items.add(toItem(dto));
            }
        }
        return items;
    }


    public List<FinancialReportPropertyDto> toDto(List<FinancialReportProperty> items) {
        List<FinancialReportPropertyDto> dtos = new ArrayList<>();
        if (items != null && !items.isEmpty()) {
            for (FinancialReportProperty item : items) {
                dtos.add(toDto(item));
            }
        }
        return dtos;
    }


    public void copy(FinancialReportPropertyDto dto, FinancialReportProperty t) {
		BeanUtils.copyProperties(dto, t, AbstractConverterHelper.getNullPropertyNames(dto));
        if(t.getFinancialReport() == null  && dto.getFinancialReport() != null){
            t.setFinancialReport(new FinancialReport());
        }else if (t.getFinancialReport() != null  && dto.getFinancialReport() != null){
            t.setFinancialReport(null);
            t.setFinancialReport(new FinancialReport());
        }
        if(t.getProperty() == null  && dto.getProperty() != null){
            t.setProperty(new Property());
        }else if (t.getProperty() != null  && dto.getProperty() != null){
            t.setProperty(null);
            t.setProperty(new Property());
        }
        if (dto.getFinancialReport() != null)
        financialReportConverter.copy(dto.getFinancialReport(), t.getFinancialReport());
        if (dto.getProperty() != null)
        propertyConverter.copy(dto.getProperty(), t.getProperty());
    }

    public List<FinancialReportProperty> copy(List<FinancialReportPropertyDto> dtos) {
        List<FinancialReportProperty> result = new ArrayList<>();
        if (dtos != null) {
            for (FinancialReportPropertyDto dto : dtos) {
                FinancialReportProperty instance = new FinancialReportProperty();
                copy(dto, instance);
                result.add(instance);
            }
        }
        return result.isEmpty() ? null : result;
    }


    public FinancialReportConverter getFinancialReportConverter(){
        return this.financialReportConverter;
    }
    public void setFinancialReportConverter(FinancialReportConverter financialReportConverter ){
        this.financialReportConverter = financialReportConverter;
    }
    public PropertyConverter getPropertyConverter(){
        return this.propertyConverter;
    }
    public void setPropertyConverter(PropertyConverter propertyConverter ){
        this.propertyConverter = propertyConverter;
    }
    public boolean  isFinancialReport(){
        return this.financialReport;
    }
    public void  setFinancialReport(boolean financialReport){
        this.financialReport = financialReport;
    }
    public boolean  isProperty(){
        return this.property;
    }
    public void  setProperty(boolean property){
        this.property = property;
    }
}
