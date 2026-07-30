package ma.zyn.app.unit.ws.facade.collaborator.payment;

import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.service.impl.collaborator.payment.PaymentCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.payment.PaymentRestCollaborator;
import ma.zyn.app.ws.converter.payment.PaymentConverter;
import ma.zyn.app.ws.dto.payment.PaymentDto;
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
public class PaymentRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentCollaboratorServiceImpl service;
    @Mock
    private PaymentConverter converter;

    @InjectMocks
    private PaymentRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllPaymentTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<PaymentDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<PaymentDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSavePaymentTest() throws Exception {
        // Mock data
        PaymentDto requestDto = new PaymentDto();
        Payment entity = new Payment();
        Payment saved = new Payment();
        PaymentDto savedDto = new PaymentDto();

        // Mock the converter to return the payment object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved payment DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<PaymentDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        PaymentDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved payment DTO
        assertEquals(savedDto.getAmount(), responseBody.getAmount());
        assertEquals(savedDto.getNotes(), responseBody.getNotes());
    }

}
