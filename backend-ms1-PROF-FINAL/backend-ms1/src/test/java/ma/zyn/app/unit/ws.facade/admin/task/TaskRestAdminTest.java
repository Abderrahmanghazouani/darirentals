package ma.zyn.app.unit.ws.facade.admin.task;

import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.service.impl.admin.task.TaskAdminServiceImpl;
import ma.zyn.app.ws.facade.admin.task.TaskRestAdmin;
import ma.zyn.app.ws.converter.task.TaskConverter;
import ma.zyn.app.ws.dto.task.TaskDto;
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
public class TaskRestAdminTest {

    private MockMvc mockMvc;

    @Mock
    private TaskAdminServiceImpl service;
    @Mock
    private TaskConverter converter;

    @InjectMocks
    private TaskRestAdmin controller;

    @Before("")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void itShouldFindAllTaskTest() throws Exception {
        // Mock the service to return an empty list
        when(service.findAll()).thenReturn(Collections.emptyList());
        when(converter.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        // Call the controller method
        ResponseEntity<List<TaskDto>> result = controller.findAll();

        // Verify the result
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        // Response body should be empty list
        List<TaskDto> responseBody = result.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    public void itShouldSaveTaskTest() throws Exception {
        // Mock data
        TaskDto requestDto = new TaskDto();
        Task entity = new Task();
        Task saved = new Task();
        TaskDto savedDto = new TaskDto();

        // Mock the converter to return the task object when converting from DTO
        when(converter.toItem(requestDto)).thenReturn(entity);

        // Mock the service to return the saved client
        when(service.create(entity)).thenReturn(saved);

        // Mock the converter to return the saved task DTO
        when(converter.toDto(saved)).thenReturn(savedDto);

        // Call the controller method
        ResponseEntity<TaskDto> result = controller.save(requestDto);

        // Verify the result
        assertEquals(HttpStatus.CREATED, result.getStatusCode());

        // Verify the response body
        TaskDto responseBody = result.getBody();
        assertNotNull(responseBody);

        // Add assertions to compare the response body with the saved task DTO
        assertEquals(savedDto.getTitle(), responseBody.getTitle());
        assertEquals(savedDto.getDescription(), responseBody.getDescription());
    }

}
