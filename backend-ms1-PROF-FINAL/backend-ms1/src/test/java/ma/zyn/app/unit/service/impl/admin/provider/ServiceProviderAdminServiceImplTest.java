package ma.zyn.app.unit.service.impl.admin.provider;

import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.dao.facade.core.provider.ServiceProviderDao;
import ma.zyn.app.service.impl.admin.provider.ServiceProviderAdminServiceImpl;

import ma.zyn.app.bean.core.task.Task ;
import ma.zyn.app.bean.core.task.TaskPriority ;
import ma.zyn.app.bean.core.payment.PaymentType ;
import ma.zyn.app.bean.core.task.TaskStatus ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.payment.Payment ;
import ma.zyn.app.bean.core.payment.PaymentStatus ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.task.TaskType ;
import ma.zyn.app.bean.core.provider.ServiceType ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.property.Property ;
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
class ServiceProviderAdminServiceImplTest {

    @Mock
    private ServiceProviderDao repository;
    private AutoCloseable autoCloseable;
    private ServiceProviderAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ServiceProviderAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllServiceProvider() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveServiceProvider() {
        // Given
        ServiceProvider toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteServiceProvider() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetServiceProviderById() {
        // Given
        Long idToRetrieve = 1L; // Example ServiceProvider ID to retrieve
        ServiceProvider expected = new ServiceProvider(); // You need to replace ServiceProvider with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        ServiceProvider result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private ServiceProvider constructSample(int i) {
		ServiceProvider given = new ServiceProvider();
        given.setName("name-"+i);
        given.setPhone("phone-"+i);
        given.setIsActive(false);
        given.setServiceType(new ServiceType(1L));
        given.setEnterprise(new Enterprise(1L));
        List<Payment> payments = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Payment element = new Payment();
                                                element.setId((long)id);
                                                element.setAmount(new BigDecimal(1*10));
                                                element.setNotes("notes"+id);
                                                element.setServiceProvider(new ServiceProvider(Long.valueOf(3)));
                                                element.setPaymentType(new PaymentType(Long.valueOf(4)));
                                                element.setPaymentStatus(new PaymentStatus(Long.valueOf(5)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setPayments(payments);
        List<Task> tasks = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Task element = new Task();
                                                element.setId((long)id);
                                                element.setTitle("title"+id);
                                                element.setDescription("description"+id);
                                                element.setProperty(new Property(Long.valueOf(3)));
                                                element.setReservation(new Reservation(Long.valueOf(4)));
                                                element.setServiceProvider(new ServiceProvider(Long.valueOf(5)));
                                                element.setAssignedTo(new Collaborator(Long.valueOf(6)));
                                                element.setTaskType(new TaskType(Long.valueOf(7)));
                                                element.setTaskPriority(new TaskPriority(Long.valueOf(8)));
                                                element.setTaskStatus(new TaskStatus(Long.valueOf(9)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setTasks(tasks);
        return given;
    }

}
