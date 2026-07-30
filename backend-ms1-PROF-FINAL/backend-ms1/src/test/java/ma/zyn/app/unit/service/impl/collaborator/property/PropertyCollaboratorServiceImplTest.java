package ma.zyn.app.unit.service.impl.collaborator.property;

import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.dao.facade.core.property.PropertyDao;
import ma.zyn.app.service.impl.collaborator.property.PropertyCollaboratorServiceImpl;

import ma.zyn.app.bean.core.task.Task ;
import ma.zyn.app.bean.core.task.TaskPriority ;
import ma.zyn.app.bean.core.report.FinancialReportProperty ;
import ma.zyn.app.bean.core.reservation.ReservationRequest ;
import ma.zyn.app.bean.core.reservation.ReservationPlatform ;
import ma.zyn.app.bean.core.report.FinancialReport ;
import ma.zyn.app.bean.core.property.PropertyType ;
import ma.zyn.app.bean.core.property.City ;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus ;
import ma.zyn.app.bean.core.task.TaskStatus ;
import ma.zyn.app.bean.core.charge.Charge ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.payment.Payment ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.charge.ChargeType ;
import ma.zyn.app.bean.core.task.TaskType ;
import ma.zyn.app.bean.core.reservation.ReservationStatus ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.property.PropertyStatus ;
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
class PropertyCollaboratorServiceImplTest {

    @Mock
    private PropertyDao repository;
    private AutoCloseable autoCloseable;
    private PropertyCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new PropertyCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllProperty() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveProperty() {
        // Given
        Property toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteProperty() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetPropertyById() {
        // Given
        Long idToRetrieve = 1L; // Example Property ID to retrieve
        Property expected = new Property(); // You need to replace Property with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Property result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Property constructSample(int i) {
		Property given = new Property();
        given.setName("name-"+i);
        given.setStreetNumber("streetNumber-"+i);
        given.setStreetName("streetName-"+i);
        given.setPostalCode("postalCode-"+i);
        given.setCapacity(i);
        given.setPricePerNight(BigDecimal.TEN);
        given.setLatitude(BigDecimal.TEN);
        given.setLongitude(BigDecimal.TEN);
        given.setPropertyType(new PropertyType(1L));
        given.setPropertyStatus(new PropertyStatus(1L));
        given.setCity(new City(1L));
        given.setEnterprise(new Enterprise(1L));
        List<Reservation> reservations = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Reservation element = new Reservation();
                                                element.setId((long)id);
                                                element.setReference("reference"+id);
                                                element.setAmount(new BigDecimal(2*10));
                                                element.setPricePerNight(new BigDecimal(3*10));
                                                element.setClient(new Client(Long.valueOf(4)));
                                                element.setProperty(new Property(Long.valueOf(5)));
                                                element.setReservationPlatform(new ReservationPlatform(Long.valueOf(6)));
                                                element.setReservationStatus(new ReservationStatus(Long.valueOf(7)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setReservations(reservations);
        List<Charge> charges = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Charge element = new Charge();
                                                element.setId((long)id);
                                                element.setLabel("label"+id);
                                                element.setAmount(new BigDecimal(2*10));
                                                element.setProperty(new Property(Long.valueOf(3)));
                                                element.setChargeType(new ChargeType(Long.valueOf(4)));
                                                element.setPayment(new Payment(Long.valueOf(5)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setCharges(charges);
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
        List<FinancialReportProperty> financialReportProperties = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                FinancialReportProperty element = new FinancialReportProperty();
                                                element.setId((long)id);
                                                element.setFinancialReport(new FinancialReport(Long.valueOf(1)));
                                                element.setProperty(new Property(Long.valueOf(2)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setFinancialReportProperties(financialReportProperties);
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
        List<ReservationRequest> alternativeRequests = IntStream.rangeClosed(1, 3)
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
        given.setAlternativeRequests(alternativeRequests);
        return given;
    }

}
