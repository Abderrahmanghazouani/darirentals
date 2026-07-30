package ma.zyn.app.unit.ws.facade.collaborator.enterprise;

import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.service.impl.collaborator.enterprise.EnterpriseMembershipCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.enterprise.EnterpriseMembershipRestCollaborator;
import ma.zyn.app.ws.converter.enterprise.EnterpriseMembershipConverter;
import ma.zyn.app.ws.dto.enterprise.EnterpriseMembershipDto;
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
public class EnterpriseMembershipRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private EnterpriseMembershipCollaboratorServiceImpl service;
    @Mock
    private EnterpriseMembershipConverter converter;

    @InjectMocks
    private EnterpriseMembershipRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllEnterpriseMembershipTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<EnterpriseMembershipDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<EnterpriseMembershipDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveEnterpriseMembershipTest() throws Exception {
        // Mock data
        EnterpriseMembershipDto requestDto = new EnterpriseMembershipDto();
        EnterpriseMembership entity = new EnterpriseMembership();
        EnterpriseMembership saved = new EnterpriseMembership();
        EnterpriseMembershipDto savedDto = new EnterpriseMembershipDto();

        // Mock the converter to return the enterpriseMembership object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved enterpriseMembership DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<EnterpriseMembershipDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        EnterpriseMembershipDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved enterpriseMembership DTO
    }

}
