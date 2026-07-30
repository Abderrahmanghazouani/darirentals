package ma.zyn.app.unit.ws.facade.collaborator.report;

import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.service.impl.collaborator.report.FinancialReportCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.report.FinancialReportRestCollaborator;
import ma.zyn.app.ws.converter.report.FinancialReportConverter;
import ma.zyn.app.ws.dto.report.FinancialReportDto;
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
public class FinancialReportRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private FinancialReportCollaboratorServiceImpl service;
    @Mock
    private FinancialReportConverter converter;

    @InjectMocks
    private FinancialReportRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllFinancialReportTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<FinancialReportDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<FinancialReportDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveFinancialReportTest() throws Exception {
        // Mock data
        FinancialReportDto requestDto = new FinancialReportDto();
        FinancialReport entity = new FinancialReport();
        FinancialReport saved = new FinancialReport();
        FinancialReportDto savedDto = new FinancialReportDto();

        // Mock the converter to return the financialReport object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved financialReport DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<FinancialReportDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        FinancialReportDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved financialReport DTO
        assertEquals(savedDto.getTotalRevenue(), responseBody.getTotalRevenue());
        assertEquals(savedDto.getTotalCharges(), responseBody.getTotalCharges());
        assertEquals(savedDto.getNetProfit(), responseBody.getNetProfit());
        assertEquals(savedDto.getGeneratedAt(), responseBody.getGeneratedAt());
        assertEquals(savedDto.getFile(), responseBody.getFile());
    }

}
