package ma.zyn.app.unit.service.impl.admin.reservation;

import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.dao.facade.core.reservation.ReservationDao;
import ma.zyn.app.service.impl.admin.reservation.ReservationAdminServiceImpl;

import ma.zyn.app.bean.core.task.Task ;
import ma.zyn.app.bean.core.task.TaskPriority ;
import ma.zyn.app.bean.core.reservation.ReservationRequest ;
import ma.zyn.app.bean.core.reservation.ReservationPlatform ;
import ma.zyn.app.bean.core.document.DocumentType ;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus ;
import ma.zyn.app.bean.core.document.Document ;
import ma.zyn.app.bean.core.task.TaskStatus ;
import ma.zyn.app.bean.core.charge.Charge ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.task.TaskType ;
import ma.zyn.app.bean.core.reservation.ReservationStatus ;
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
class ReservationAdminServiceImplTest {

    @Mock
    private ReservationDao repository;
    private AutoCloseable autoCloseable;
    private ReservationAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ReservationAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllReservation() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveReservation() {
        // Given
        Reservation toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteReservation() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetReservationById() {
        // Given
        Long idToRetrieve = 1L; // Example Reservation ID to retrieve
        Reservation expected = new Reservation(); // You need to replace Reservation with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Reservation result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Reservation constructSample(int i) {
		Reservation given = new Reservation();
        given.setReference("reference-"+i);
        given.setAmount(BigDecimal.TEN);
        given.setPricePerNight(BigDecimal.TEN);
        given.setClient(new Client(1L));
        given.setProperty(new Property(1L));
        given.setReservationPlatform(new ReservationPlatform(1L));
        given.setReservationStatus(new ReservationStatus(1L));
        List<Document> documents = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Document element = new Document();
                                                element.setId((long)id);
                                                element.setFileName("fileName"+id);
                                                element.setFile("file"+id);
                                                element.setExtractedVendor("extractedVendor"+id);
                                                element.setExtractedAmount(new BigDecimal(4*10));
                                                element.setDocumentType(new DocumentType(Long.valueOf(5)));
                                                element.setReservation(new Reservation(Long.valueOf(6)));
                                                element.setCharge(new Charge(Long.valueOf(7)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setDocuments(documents);
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
        return given;
    }

}
