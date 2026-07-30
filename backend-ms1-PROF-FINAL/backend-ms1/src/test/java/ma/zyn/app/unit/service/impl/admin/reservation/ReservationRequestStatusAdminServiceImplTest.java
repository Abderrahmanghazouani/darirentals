package ma.zyn.app.unit.service.impl.admin.reservation;

import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.dao.facade.core.reservation.ReservationRequestStatusDao;
import ma.zyn.app.service.impl.admin.reservation.ReservationRequestStatusAdminServiceImpl;

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
class ReservationRequestStatusAdminServiceImplTest {

    @Mock
    private ReservationRequestStatusDao repository;
    private AutoCloseable autoCloseable;
    private ReservationRequestStatusAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ReservationRequestStatusAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllReservationRequestStatus() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveReservationRequestStatus() {
        // Given
        ReservationRequestStatus toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteReservationRequestStatus() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetReservationRequestStatusById() {
        // Given
        Long idToRetrieve = 1L; // Example ReservationRequestStatus ID to retrieve
        ReservationRequestStatus expected = new ReservationRequestStatus(); // You need to replace ReservationRequestStatus with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        ReservationRequestStatus result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private ReservationRequestStatus constructSample(int i) {
		ReservationRequestStatus given = new ReservationRequestStatus();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        return given;
    }

}
