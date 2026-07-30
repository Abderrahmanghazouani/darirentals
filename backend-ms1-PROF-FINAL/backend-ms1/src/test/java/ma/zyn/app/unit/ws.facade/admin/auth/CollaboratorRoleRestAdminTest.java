package ma.zyn.app.unit.ws.facade.admin.auth;

import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.service.impl.admin.auth.CollaboratorRoleAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.auth.CollaboratorRoleRestAdmin;
import ma.zyn.app.ws.converter.auth.CollaboratorRoleConverter;
import ma.zyn.app.ws.dto.auth.CollaboratorRoleDto;
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
public class CollaboratorRoleRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private CollaboratorRoleAdminServiceImpl service;
    @Mock
    private CollaboratorRoleConverter converter;

    @InjectMocks
    private CollaboratorRoleRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllCollaboratorRoleTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<CollaboratorRoleDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<CollaboratorRoleDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveCollaboratorRoleTest() throws Exception {
        // Mock data
        CollaboratorRoleDto requestDto = new CollaboratorRoleDto();
        CollaboratorRole entity = new CollaboratorRole();
        CollaboratorRole saved = new CollaboratorRole();
        CollaboratorRoleDto savedDto = new CollaboratorRoleDto();

        // Mock the converter to return the collaboratorRole object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved collaboratorRole DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<CollaboratorRoleDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        CollaboratorRoleDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved collaboratorRole DTO
        assertEquals(savedDto.getDescription(), responseBody.getDescription());
        assertEquals(savedDto.getCode(), responseBody.getCode());
        assertEquals(savedDto.getLabel(), responseBody.getLabel());
        assertEquals(savedDto.getStyle(), responseBody.getStyle());
        assertEquals(savedDto.getIsDefault(), responseBody.getIsDefault());
        assertEquals(savedDto.getSortOrder(), responseBody.getSortOrder());
        assertEquals(savedDto.getCanManageFinancials(), responseBody.getCanManageFinancials());
        assertEquals(savedDto.getCanManageUsers(), responseBody.getCanManageUsers());
        assertEquals(savedDto.getCanDeleteProperty(), responseBody.getCanDeleteProperty());
        assertEquals(savedDto.getCanManageServiceProviders(), responseBody.getCanManageServiceProviders());
        assertEquals(savedDto.getCanManageAiUsage(), responseBody.getCanManageAiUsage());
    }

}
