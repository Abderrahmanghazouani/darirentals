package ma.zyn.app.unit.service.impl.client.task;

import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.dao.facade.core.task.TaskDao;
import ma.zyn.app.service.impl.client.task.TaskClientServiceImpl;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.task.TaskPriority ;
import ma.zyn.app.bean.core.task.TaskType ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.task.TaskStatus ;
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
class TaskClientServiceImplTest {

    @Mock
    private TaskDao repository;
    private AutoCloseable autoCloseable;
    private TaskClientServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new TaskClientServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllTask() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveTask() {
        // Given
        Task toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteTask() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetTaskById() {
        // Given
        Long idToRetrieve = 1L; // Example Task ID to retrieve
        Task expected = new Task(); // You need to replace Task with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Task result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Task constructSample(int i) {
		Task given = new Task();
        given.setTitle("title-"+i);
        given.setDescription("description-"+i);
        given.setProperty(new Property(1L));
        given.setReservation(new Reservation(1L));
        given.setServiceProvider(new ServiceProvider(1L));
        given.setAssignedTo(new Collaborator(1L));
        given.setTaskType(new TaskType(1L));
        given.setTaskPriority(new TaskPriority(1L));
        given.setTaskStatus(new TaskStatus(1L));
        return given;
    }

}
