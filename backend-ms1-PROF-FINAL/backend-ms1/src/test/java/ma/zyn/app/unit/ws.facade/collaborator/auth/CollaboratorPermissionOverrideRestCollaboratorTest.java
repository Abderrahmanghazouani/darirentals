package ma.zyn.app.unit.ws.facade.collaborator.auth;

import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.service.impl.collaborator.auth.CollaboratorPermissionOverrideCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.auth.CollaboratorPermissionOverrideRestCollaborator;
import ma.zyn.app.ws.converter.auth.CollaboratorPermissionOverrideConverter;
import ma.zyn.app.ws.dto.auth.CollaboratorPermissionOverrideDto;
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
public class CollaboratorPermissionOverrideRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private CollaboratorPermissionOverrideCollaboratorServiceImpl service;
    @Mock
    private CollaboratorPermissionOverrideConverter converter;

    @InjectMocks
    private CollaboratorPermissionOverrideRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllCollaboratorPermissionOverrideTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<CollaboratorPermissionOverrideDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<CollaboratorPermissionOverrideDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveCollaboratorPermissionOverrideTest() throws Exception {
        // Mock data
        CollaboratorPermissionOverrideDto requestDto = new CollaboratorPermissionOverrideDto();
        CollaboratorPermissionOverride entity = new CollaboratorPermissionOverride();
        CollaboratorPermissionOverride saved = new CollaboratorPermissionOverride();
        CollaboratorPermissionOverrideDto savedDto = new CollaboratorPermissionOverrideDto();

        // Mock the converter to return the collaboratorPermissionOverride object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved collaboratorPermissionOverride DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<CollaboratorPermissionOverrideDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        CollaboratorPermissionOverrideDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved collaboratorPermissionOverride DTO
        assertEquals(savedDto.getCanManageFinancials(), responseBody.getCanManageFinancials());
        assertEquals(savedDto.getCanManageUsers(), responseBody.getCanManageUsers());
        assertEquals(savedDto.getCanDeleteProperty(), responseBody.getCanDeleteProperty());
        assertEquals(savedDto.getCanManageServiceProviders(), responseBody.getCanManageServiceProviders());
        assertEquals(savedDto.getCanManageAiUsage(), responseBody.getCanManageAiUsage());
    }

}
