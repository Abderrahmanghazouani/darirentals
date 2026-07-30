package ma.zyn.app.unit.service.impl.admin.reservation;

import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.dao.facade.core.reservation.ReservationRequestDao;
import ma.zyn.app.service.impl.admin.reservation.ReservationRequestAdminServiceImpl;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.reservation.Reservation ;
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
class ReservationRequestAdminServiceImplTest {

    @Mock
    private ReservationRequestDao repository;
    private AutoCloseable autoCloseable;
    private ReservationRequestAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ReservationRequestAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllReservationRequest() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveReservationRequest() {
        // Given
        ReservationRequest toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteReservationRequest() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetReservationRequestById() {
        // Given
        Long idToRetrieve = 1L; // Example ReservationRequest ID to retrieve
        ReservationRequest expected = new ReservationRequest(); // You need to replace ReservationRequest with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        ReservationRequest result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private ReservationRequest constructSample(int i) {
		ReservationRequest given = new ReservationRequest();
        given.setClientNote("clientNote-"+i);
        given.setStaffNote("staffNote-"+i);
        given.setClient(new Client(1L));
        given.setRequestedProperty(new Property(1L));
        given.setAlternativeProperty(new Property(1L));
        given.setReviewedBy(new Collaborator(1L));
        given.setReservationRequestStatus(new ReservationRequestStatus(1L));
        given.setReservation(new Reservation(1L));
        return given;
    }

}
