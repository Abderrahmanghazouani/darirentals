package ma.zyn.app.unit.dao.facade.core.task;

import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.dao.facade.core.task.TaskDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.time.LocalDateTime;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.task.TaskPriority ;
import ma.zyn.app.bean.core.task.TaskType ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.task.TaskStatus ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TaskDaoTest {

@Autowired
    private TaskDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        Task entity = new Task();
        entity.setId(id);
        underTest.save(entity);
        Task loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        Task entity = new Task();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        Task loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<Task> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<Task> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        Task given = constructSample(1);
        Task saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
