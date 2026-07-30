package ma.zyn.app.unit.ws.facade.admin.ai;

import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.service.impl.admin.ai.AiQuotaAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.ai.AiQuotaRestAdmin;
import ma.zyn.app.ws.converter.ai.AiQuotaConverter;
import ma.zyn.app.ws.dto.ai.AiQuotaDto;
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
public class AiQuotaRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private AiQuotaAdminServiceImpl service;
    @Mock
    private AiQuotaConverter converter;

    @InjectMocks
    private AiQuotaRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllAiQuotaTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<AiQuotaDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<AiQuotaDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveAiQuotaTest() throws Exception {
        // Mock data
        AiQuotaDto requestDto = new AiQuotaDto();
        AiQuota entity = new AiQuota();
        AiQuota saved = new AiQuota();
        AiQuotaDto savedDto = new AiQuotaDto();

        // Mock the converter to return the aiQuota object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved aiQuota DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<AiQuotaDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        AiQuotaDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved aiQuota DTO
        assertEquals(savedDto.getTokensAllocated(), responseBody.getTokensAllocated());
    }

}
