package ma.zyn.app.unit.ws.facade.admin.property;

import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.service.impl.admin.property.PropertyStatusAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.property.PropertyStatusRestAdmin;
import ma.zyn.app.ws.converter.property.PropertyStatusConverter;
import ma.zyn.app.ws.dto.property.PropertyStatusDto;
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
public class PropertyStatusRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private PropertyStatusAdminServiceImpl service;
    @Mock
    private PropertyStatusConverter converter;

    @InjectMocks
    private PropertyStatusRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllPropertyStatusTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<PropertyStatusDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<PropertyStatusDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSavePropertyStatusTest() throws Exception {
        // Mock data
        PropertyStatusDto requestDto = new PropertyStatusDto();
        PropertyStatus entity = new PropertyStatus();
        PropertyStatus saved = new PropertyStatus();
        PropertyStatusDto savedDto = new PropertyStatusDto();

        // Mock the converter to return the propertyStatus object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved propertyStatus DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<PropertyStatusDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        PropertyStatusDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved propertyStatus DTO
        assertEquals(savedDto.getDescription(), responseBody.getDescription());
        assertEquals(savedDto.getCode(), responseBody.getCode());
        assertEquals(savedDto.getLabel(), responseBody.getLabel());
        assertEquals(savedDto.getStyle(), responseBody.getStyle());
        assertEquals(savedDto.getIsDefault(), responseBody.getIsDefault());
        assertEquals(savedDto.getSortOrder(), responseBody.getSortOrder());
    }

}
