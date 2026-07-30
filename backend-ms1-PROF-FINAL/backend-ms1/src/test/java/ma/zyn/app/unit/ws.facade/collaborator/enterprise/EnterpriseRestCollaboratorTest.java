package ma.zyn.app.unit.ws.facade.collaborator.enterprise;

import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.service.impl.collaborator.enterprise.EnterpriseCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.enterprise.EnterpriseRestCollaborator;
import ma.zyn.app.ws.converter.enterprise.EnterpriseConverter;
import ma.zyn.app.ws.dto.enterprise.EnterpriseDto;
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
public class EnterpriseRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private EnterpriseCollaboratorServiceImpl service;
    @Mock
    private EnterpriseConverter converter;

    @InjectMocks
    private EnterpriseRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllEnterpriseTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<EnterpriseDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<EnterpriseDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveEnterpriseTest() throws Exception {
        // Mock data
        EnterpriseDto requestDto = new EnterpriseDto();
        Enterprise entity = new Enterprise();
        Enterprise saved = new Enterprise();
        EnterpriseDto savedDto = new EnterpriseDto();

        // Mock the converter to return the enterprise object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved enterprise DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<EnterpriseDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        EnterpriseDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved enterprise DTO
        assertEquals(savedDto.getName(), responseBody.getName());
        assertEquals(savedDto.getPhone(), responseBody.getPhone());
        assertEquals(savedDto.getAddress(), responseBody.getAddress());
    }

}
