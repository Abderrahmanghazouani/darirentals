package ma.zyn.app.unit.ws.facade.collaborator.currency;

import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.service.impl.collaborator.currency.ExchangeRateCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.currency.ExchangeRateRestCollaborator;
import ma.zyn.app.ws.converter.currency.ExchangeRateConverter;
import ma.zyn.app.ws.dto.currency.ExchangeRateDto;
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
public class ExchangeRateRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private ExchangeRateCollaboratorServiceImpl service;
    @Mock
    private ExchangeRateConverter converter;

    @InjectMocks
    private ExchangeRateRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllExchangeRateTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<ExchangeRateDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<ExchangeRateDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveExchangeRateTest() throws Exception {
        // Mock data
        ExchangeRateDto requestDto = new ExchangeRateDto();
        ExchangeRate entity = new ExchangeRate();
        ExchangeRate saved = new ExchangeRate();
        ExchangeRateDto savedDto = new ExchangeRateDto();

        // Mock the converter to return the exchangeRate object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved exchangeRate DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<ExchangeRateDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        ExchangeRateDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved exchangeRate DTO
        assertEquals(savedDto.getRate(), responseBody.getRate());
        assertEquals(savedDto.getSource(), responseBody.getSource());
    }

}
