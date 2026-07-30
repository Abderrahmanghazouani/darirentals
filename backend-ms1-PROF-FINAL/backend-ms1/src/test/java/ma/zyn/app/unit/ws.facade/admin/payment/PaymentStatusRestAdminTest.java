package ma.zyn.app.unit.ws.facade.admin.payment;

import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.service.impl.admin.payment.PaymentStatusAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.payment.PaymentStatusRestAdmin;
import ma.zyn.app.ws.converter.payment.PaymentStatusConverter;
import ma.zyn.app.ws.dto.payment.PaymentStatusDto;
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
public class PaymentStatusRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private PaymentStatusAdminServiceImpl service;
    @Mock
    private PaymentStatusConverter converter;

    @InjectMocks
    private PaymentStatusRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllPaymentStatusTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<PaymentStatusDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<PaymentStatusDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSavePaymentStatusTest() throws Exception {
        // Mock data
        PaymentStatusDto requestDto = new PaymentStatusDto();
        PaymentStatus entity = new PaymentStatus();
        PaymentStatus saved = new PaymentStatus();
        PaymentStatusDto savedDto = new PaymentStatusDto();

        // Mock the converter to return the paymentStatus object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved paymentStatus DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<PaymentStatusDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        PaymentStatusDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved paymentStatus DTO
        assertEquals(savedDto.getDescription(), responseBody.getDescription());
        assertEquals(savedDto.getCode(), responseBody.getCode());
        assertEquals(savedDto.getLabel(), responseBody.getLabel());
        assertEquals(savedDto.getStyle(), responseBody.getStyle());
        assertEquals(savedDto.getIsDefault(), responseBody.getIsDefault());
        assertEquals(savedDto.getSortOrder(), responseBody.getSortOrder());
    }

}
