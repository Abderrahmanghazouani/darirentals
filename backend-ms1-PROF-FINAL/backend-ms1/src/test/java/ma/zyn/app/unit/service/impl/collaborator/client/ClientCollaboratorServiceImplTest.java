package ma.zyn.app.unit.service.impl.collaborator.client;

import ma.zyn.app.bean.core.client.Client;
import ma.zyn.app.dao.facade.core.client.ClientDao;
import ma.zyn.app.service.impl.collaborator.client.ClientCollaboratorServiceImpl;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.reservation.ReservationRequest ;
import ma.zyn.app.bean.core.reservation.ReservationPlatform ;
import ma.zyn.app.bean.core.reservation.ReservationStatus ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus ;
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
class ClientCollaboratorServiceImplTest {

    @Mock
    private ClientDao repository;
    private AutoCloseable autoCloseable;
    private ClientCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ClientCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllClient() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveClient() {
        // Given
        Client toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteClient() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetClientById() {
        // Given
        Long idToRetrieve = 1L; // Example Client ID to retrieve
        Client expected = new Client(); // You need to replace Client with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Client result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Client constructSample(int i) {
		Client given = new Client();
        given.setFullName("fullName-"+i);
        given.setPhone("phone-"+i);
        given.setNationality("nationality-"+i);
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
