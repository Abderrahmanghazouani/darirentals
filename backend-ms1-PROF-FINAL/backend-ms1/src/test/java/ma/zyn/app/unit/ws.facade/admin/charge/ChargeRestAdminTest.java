package ma.zyn.app.unit.ws.facade.admin.charge;

import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.service.impl.admin.charge.ChargeAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.charge.ChargeRestAdmin;
import ma.zyn.app.ws.converter.charge.ChargeConverter;
import ma.zyn.app.ws.dto.charge.ChargeDto;
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
public class ChargeRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private ChargeAdminServiceImpl service;
    @Mock
    private ChargeConverter converter;

    @InjectMocks
    private ChargeRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllChargeTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<ChargeDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<ChargeDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveChargeTest() throws Exception {
        // Mock data
        ChargeDto requestDto = new ChargeDto();
        Charge entity = new Charge();
        Charge saved = new Charge();
        ChargeDto savedDto = new ChargeDto();

        // Mock the converter to return the charge object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved charge DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<ChargeDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        ChargeDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved charge DTO
        assertEquals(savedDto.getLabel(), responseBody.getLabel());
        assertEquals(savedDto.getAmount(), responseBody.getAmount());
    }

}
