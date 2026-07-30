package ma.zyn.app.unit.dao.facade.core.charge;

import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.dao.facade.core.charge.ChargeDao;

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

import ma.zyn.app.bean.core.payment.Payment ;
import ma.zyn.app.bean.core.charge.ChargeType ;
import ma.zyn.app.bean.core.property.Property ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ChargeDaoTest {

@Autowired
    private ChargeDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        Charge entity = new Charge();
        entity.setId(id);
        underTest.save(entity);
        Charge loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        Charge entity = new Charge();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        Charge loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<Charge> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<Charge> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        Charge given = constructSample(1);
        Charge saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private Charge constructSample(int i) {
		Charge given = new Charge();
        given.setLabel("label-"+i);
        given.setAmount(BigDecimal.TEN);
        given.setProperty(new Property(1L));
        given.setChargeType(new ChargeType(1L));
        given.setPayment(new Payment(1L));
        return given;
    }

}
