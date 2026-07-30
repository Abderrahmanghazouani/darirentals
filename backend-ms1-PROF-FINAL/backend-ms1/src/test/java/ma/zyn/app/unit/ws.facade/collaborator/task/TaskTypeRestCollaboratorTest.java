package ma.zyn.app.unit.ws.facade.collaborator.task;

import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.service.impl.collaborator.task.TaskTypeCollaboratorServiceImpl;
import ma.zyn.app.ws.facade.collaborator.task.TaskTypeRestCollaborator;
import ma.zyn.app.ws.converter.task.TaskTypeConverter;
import ma.zyn.app.ws.dto.task.TaskTypeDto;
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
public class TaskTypeRestCollaboratorTest {

    private MockMvc mockMvc;

    @Mock
    private TaskTypeCollaboratorServiceImpl service;
    @Mock
    private TaskTypeConverter converter;

    @InjectMocks
    private TaskTypeRestCollaborator controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllTaskTypeTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<TaskTypeDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<TaskTypeDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveTaskTypeTest() throws Exception {
        // Mock data
        TaskTypeDto requestDto = new TaskTypeDto();
        TaskType entity = new TaskType();
        TaskType saved = new TaskType();
        TaskTypeDto savedDto = new TaskTypeDto();

        // Mock the converter to return the taskType object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved taskType DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<TaskTypeDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        TaskTypeDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved taskType DTO
        assertEquals(savedDto.getDescription(), responseBody.getDescription());
        assertEquals(savedDto.getCode(), responseBody.getCode());
        assertEquals(savedDto.getLabel(), responseBody.getLabel());
        assertEquals(savedDto.getStyle(), responseBody.getStyle());
        assertEquals(savedDto.getIsDefault(), responseBody.getIsDefault());
        assertEquals(savedDto.getSortOrder(), responseBody.getSortOrder());
    }

}
