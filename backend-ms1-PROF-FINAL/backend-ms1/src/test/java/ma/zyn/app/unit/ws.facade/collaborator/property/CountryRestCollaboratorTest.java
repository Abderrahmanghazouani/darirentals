package ma.zyn.app.unit.ws.facade.collaborator.property;

import ma.zyn.app.bean.core.property.Country;
import ma.zyn.app.service.impl.collaborator.property.CountryCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.property.CountryRestCollaborator;
import ma.zyn.app.ws.converter.property.CountryConverter;
import ma.zyn.app.ws.dto.property.CountryDto;
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
public class CountryRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private CountryCollaboratorServiceImpl service;
    @Mock
    private CountryConverter converter;

    @InjectMocks
    private CountryRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllCountryTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<CountryDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<CountryDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveCountryTest() throws Exception {
        // Mock data
        CountryDto requestDto = new CountryDto();
        Country entity = new Country();
        Country saved = new Country();
        CountryDto savedDto = new CountryDto();

        // Mock the converter to return the country object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved country DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<CountryDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        CountryDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved country DTO
        assertEquals(savedDto.getName(), responseBody.getName());
        assertEquals(savedDto.getCode(), responseBody.getCode());
    }

}
