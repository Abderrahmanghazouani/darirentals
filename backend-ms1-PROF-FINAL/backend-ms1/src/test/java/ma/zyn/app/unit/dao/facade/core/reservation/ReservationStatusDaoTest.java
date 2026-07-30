package ma.zyn.app.unit.dao.facade.core.reservation;

import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.dao.facade.core.reservation.ReservationStatusDao;

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


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ReservationStatusDaoTest {

@Autowired
    private ReservationStatusDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindByCode(){
        String code = "code-1";
        ReservationStatus entity = new ReservationStatus();
        entity.setCode(code);
        underTest.save(entity);
        ReservationStatus loaded = underTest.findByCode(code);
        assertThat(loaded.getCode()).isEqualTo(code);
    }

    @Test
    void shouldDeleteByCode() {
        String code = "code-12345678";
        int result = underTest.deleteByCode(code);

        ReservationStatus loaded = underTest.findByCode(code);
        assertThat(loaded).isNull();
        assertThat(result).isEqualTo(0);
    }

    @Test
    void shouldFindById(){
        Long id = 1L;
        ReservationStatus entity = new ReservationStatus();
        entity.setId(id);
        underTest.save(entity);
        ReservationStatus loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        ReservationStatus entity = new ReservationStatus();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        ReservationStatus loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<ReservationStatus> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<ReservationStatus> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        ReservationStatus given = constructSample(1);
        ReservationStatus saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private ReservationStatus constructSample(int i) {
		ReservationStatus given = new ReservationStatus();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        return given;
    }

}
