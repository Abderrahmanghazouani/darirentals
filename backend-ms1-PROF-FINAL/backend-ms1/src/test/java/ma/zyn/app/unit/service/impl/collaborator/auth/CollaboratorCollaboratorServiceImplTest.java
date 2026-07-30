package ma.zyn.app.unit.service.impl.collaborator.auth;

import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.dao.facade.core.auth.CollaboratorDao;
import ma.zyn.app.service.impl.collaborator.auth.CollaboratorCollaboratorServiceImpl;

import ma.zyn.app.bean.core.ai.AiUsageLog ;
import ma.zyn.app.bean.core.task.Task ;
import ma.zyn.app.bean.core.task.TaskPriority ;
import ma.zyn.app.bean.core.reservation.ReservationRequest ;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership ;
import ma.zyn.app.bean.core.auth.CollaboratorRole ;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus ;
import ma.zyn.app.bean.core.document.Document ;
import ma.zyn.app.bean.core.task.TaskStatus ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.bean.core.ai.AiUsageType ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.currency.Currency ;
import ma.zyn.app.bean.core.task.TaskType ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.client.Client ;
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
class CollaboratorCollaboratorServiceImplTest {

    @Mock
    private CollaboratorDao repository;
    private AutoCloseable autoCloseable;
    private CollaboratorCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CollaboratorCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllCollaborator() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveCollaborator() {
        // Given
        Collaborator toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteCollaborator() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetCollaboratorById() {
        // Given
        Long idToRetrieve = 1L; // Example Collaborator ID to retrieve
        Collaborator expected = new Collaborator(); // You need to replace Collaborator with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Collaborator result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Collaborator constructSample(int i) {
		Collaborator given = new Collaborator();
        given.setName("name-"+i);
        given.setPhone("phone-"+i);
        given.setIsActive(false);
        given.setDisplayCurrency(new Currency(1L));
        List<EnterpriseMembership> enterpriseMemberships = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                EnterpriseMembership element = new EnterpriseMembership();
                                                element.setId((long)id);
                                                element.setCollaborator(new Collaborator(Long.valueOf(1)));
                                                element.setEnterprise(new Enterprise(Long.valueOf(2)));
                                                element.setCollaboratorRole(new CollaboratorRole(Long.valueOf(3)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setEnterpriseMemberships(enterpriseMemberships);
        List<AiUsageLog> aiUsageLogs = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                AiUsageLog element = new AiUsageLog();
                                                element.setId((long)id);
                                                element.setTokensUsed(1L);
                                                element.setDate(LocalDateTime.now());
                                                element.setEnterprise(new Enterprise(Long.valueOf(3)));
                                                element.setAiUsageType(new AiUsageType(Long.valueOf(4)));
                                                element.setCollaborator(new Collaborator(Long.valueOf(5)));
                                                element.setDocument(new Document(Long.valueOf(6)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setAiUsageLogs(aiUsageLogs);
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
        List<ReservationRequest> reservationRequests = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                ReservationRequest element = new ReservationRequest();
                                                element.setId((long)id);
                                                element.setClientNote("clientNote"+id);
                                                element.setStaffNote("staffNote"+id);
                                                element.setClient(new Client(Long.valueOf(3)));
                                                element.setRequestedProperty(new Property(Long.valueOf(4)));
                                                element.setAlternativeProperty(new Property(Long.valueOf(5)));
                                                element.setReviewedBy(new Collaborator(Long.valueOf(6)));
                                                element.setReservationRequestStatus(new ReservationRequestStatus(Long.valueOf(7)));
                                                element.setReservation(new Reservation(Long.valueOf(8)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setReservationRequests(reservationRequests);
        given.setEmail("email-"+i);
        given.setEnabled(false);
        given.setCredentialsNonExpired(false);
        given.setAccountNonExpired(false);
        given.setUsername("username-"+i);
        given.setPasswordChanged(false);
        given.setAccountNonLocked(false);
        given.setPassword("password-"+i);
        return given;
    }

}
