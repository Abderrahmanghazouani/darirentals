package ma.zyn.app.unit.ws.facade.admin.property;

import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.service.impl.admin.property.PropertyAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.property.PropertyRestAdmin;
import ma.zyn.app.ws.converter.property.PropertyConverter;
import ma.zyn.app.ws.dto.property.PropertyDto;
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
public class PropertyRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private PropertyAdminServiceImpl service;
    @Mock
    private PropertyConverter converter;

    @InjectMocks
    private PropertyRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllPropertyTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<PropertyDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<PropertyDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSavePropertyTest() throws Exception {
        // Mock data
        PropertyDto requestDto = new PropertyDto();
        Property entity = new Property();
        Property saved = new Property();
        PropertyDto savedDto = new PropertyDto();

        // Mock the converter to return the property object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved property DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<PropertyDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        PropertyDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved property DTO
        assertEquals(savedDto.getName(), responseBody.getName());
        assertEquals(savedDto.getStreetNumber(), responseBody.getStreetNumber());
        assertEquals(savedDto.getStreetName(), responseBody.getStreetName());
        assertEquals(savedDto.getPostalCode(), responseBody.getPostalCode());
        assertEquals(savedDto.getCapacity(), responseBody.getCapacity());
        assertEquals(savedDto.getPricePerNight(), responseBody.getPricePerNight());
        assertEquals(savedDto.getLatitude(), responseBody.getLatitude());
        assertEquals(savedDto.getLongitude(), responseBody.getLongitude());
    }

}
