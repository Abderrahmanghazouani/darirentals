package ma.zyn.app.unit.ws.facade.collaborator.report;

import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.service.impl.collaborator.report.FinancialReportScopeCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.report.FinancialReportScopeRestCollaborator;
import ma.zyn.app.ws.converter.report.FinancialReportScopeConverter;
import ma.zyn.app.ws.dto.report.FinancialReportScopeDto;
import org.aspectj.lang.annotation.Before;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FinancialReportScopeRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private FinancialReportScopeCollaboratorServiceImpl service;
    @Mock
    private FinancialReportScopeConverter converter;

    @InjectMocks
    private FinancialReportScopeRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllFinancialReportScopeTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<FinancialReportScopeDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<FinancialReportScopeDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveFinancialReportScopeTest() throws Exception {
        // Mock data
        FinancialReportScopeDto requestDto = new FinancialReportScopeDto();
        FinancialReportScope entity = new FinancialReportScope();
        FinancialReportScope saved = new FinancialReportScope();
        FinancialReportScopeDto savedDto = new FinancialReportScopeDto();

        // Mock the converter to return the financialReportScope object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved financialReportScope DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<FinancialReportScopeDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        FinancialReportScopeDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved financialReportScope DTO
        assertEquals(savedDto.getDescription(), responseBody.getDescription());
        assertEquals(savedDto.getCode(), responseBody.getCode());
        assertEquals(savedDto.getLabel(), responseBody.getLabel());
        assertEquals(savedDto.getStyle(), responseBody.getStyle());
        assertEquals(savedDto.getIsDefault(), responseBody.getIsDefault());
        assertEquals(savedDto.getSortOrder(), responseBody.getSortOrder());
    }

}
