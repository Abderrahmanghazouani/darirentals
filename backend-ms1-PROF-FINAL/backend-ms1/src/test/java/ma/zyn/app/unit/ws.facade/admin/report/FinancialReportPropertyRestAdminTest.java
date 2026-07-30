package ma.zyn.app.unit.ws.facade.admin.report;

import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.service.impl.admin.report.FinancialReportPropertyAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.report.FinancialReportPropertyRestAdmin;
import ma.zyn.app.ws.converter.report.FinancialReportPropertyConverter;
import ma.zyn.app.ws.dto.report.FinancialReportPropertyDto;
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
public class FinancialReportPropertyRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private FinancialReportPropertyAdminServiceImpl service;
    @Mock
    private FinancialReportPropertyConverter converter;

    @InjectMocks
    private FinancialReportPropertyRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllFinancialReportPropertyTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<FinancialReportPropertyDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<FinancialReportPropertyDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveFinancialReportPropertyTest() throws Exception {
        // Mock data
        FinancialReportPropertyDto requestDto = new FinancialReportPropertyDto();
        FinancialReportProperty entity = new FinancialReportProperty();
        FinancialReportProperty saved = new FinancialReportProperty();
        FinancialReportPropertyDto savedDto = new FinancialReportPropertyDto();

        // Mock the converter to return the financialReportProperty object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved financialReportProperty DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<FinancialReportPropertyDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        FinancialReportPropertyDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved financialReportProperty DTO
    }

}
