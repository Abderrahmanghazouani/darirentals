package ma.zyn.app.unit.ws.facade.admin.provider;

import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.service.impl.admin.provider.ServiceProviderAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.provider.ServiceProviderRestAdmin;
import ma.zyn.app.ws.converter.provider.ServiceProviderConverter;
import ma.zyn.app.ws.dto.provider.ServiceProviderDto;
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
public class ServiceProviderRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private ServiceProviderAdminServiceImpl service;
    @Mock
    private ServiceProviderConverter converter;

    @InjectMocks
    private ServiceProviderRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllServiceProviderTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<ServiceProviderDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<ServiceProviderDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveServiceProviderTest() throws Exception {
        // Mock data
        ServiceProviderDto requestDto = new ServiceProviderDto();
        ServiceProvider entity = new ServiceProvider();
        ServiceProvider saved = new ServiceProvider();
        ServiceProviderDto savedDto = new ServiceProviderDto();

        // Mock the converter to return the serviceProvider object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved serviceProvider DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<ServiceProviderDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        ServiceProviderDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved serviceProvider DTO
        assertEquals(savedDto.getName(), responseBody.getName());
        assertEquals(savedDto.getPhone(), responseBody.getPhone());
        assertEquals(savedDto.getIsActive(), responseBody.getIsActive());
    }

}
