package ma.zyn.app.unit.dao.facade.core.reservation;

import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.dao.facade.core.reservation.ReservationDao;

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

import ma.zyn.app.bean.core.reservation.ReservationPlatform ;
import ma.zyn.app.bean.core.reservation.ReservationStatus ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.client.Client ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ReservationDaoTest {

@Autowired
    private ReservationDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindByReference(){
        String reference = "reference-1";
        Reservation entity = new Reservation();
        entity.setReference(reference);
        underTest.save(entity);
        Reservation loaded = underTest.findByReference(reference);
        assertThat(loaded.getReference()).isEqualTo(reference);
    }

    @Test
    void shouldDeleteByReference() {
        String reference = "reference-12345678";
        int result = underTest.deleteByReference(reference);

        Reservation loaded = underTest.findByReference(reference);
        assertThat(loaded).isNull();
        assertThat(result).isEqualTo(0);
    }

    @Test
    void shouldFindById(){
        Long id = 1L;
        Reservation entity = new Reservation();
        entity.setId(id);
        underTest.save(entity);
        Reservation loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        Reservation entity = new Reservation();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        Reservation loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<Reservation> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<Reservation> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        Reservation given = constructSample(1);
        Reservation saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
        return given;
    }

}
