package ma.zyn.app.unit.dao.facade.core.reservation;

import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.dao.facade.core.reservation.ReservationRequestDao;

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
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus ;
import ma.zyn.app.bean.core.client.Client ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ReservationRequestDaoTest {

@Autowired
    private ReservationRequestDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        ReservationRequest entity = new ReservationRequest();
        entity.setId(id);
        underTest.save(entity);
        ReservationRequest loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        ReservationRequest entity = new ReservationRequest();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        ReservationRequest loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<ReservationRequest> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<ReservationRequest> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        ReservationRequest given = constructSample(1);
        ReservationRequest saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
