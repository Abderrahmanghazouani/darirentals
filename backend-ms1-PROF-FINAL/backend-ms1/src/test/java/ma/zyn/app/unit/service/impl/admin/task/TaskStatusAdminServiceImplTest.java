package ma.zyn.app.unit.service.impl.admin.task;

import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.dao.facade.core.task.TaskStatusDao;
import ma.zyn.app.service.impl.admin.task.TaskStatusAdminServiceImpl;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;



import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
class TaskStatusAdminServiceImplTest {

    @Mock
    private TaskStatusDao repository;
    private AutoCloseable autoCloseable;
    private TaskStatusAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new TaskStatusAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllTaskStatus() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveTaskStatus() {
        // Given
        TaskStatus toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteTaskStatus() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetTaskStatusById() {
        // Given
        Long idToRetrieve = 1L; // Example TaskStatus ID to retrieve
        TaskStatus expected = new TaskStatus(); // You need to replace TaskStatus with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        TaskStatus result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private TaskStatus constructSample(int i) {
		TaskStatus given = new TaskStatus();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        return given;
    }

}
