package ma.zyn.app.unit.dao.facade.core.enterprise;

import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.dao.facade.core.enterprise.EnterpriseDao;

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

import ma.zyn.app.bean.core.currency.Currency ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class EnterpriseDaoTest {

@Autowired
    private EnterpriseDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        Enterprise entity = new Enterprise();
        entity.setId(id);
        underTest.save(entity);
        Enterprise loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        Enterprise entity = new Enterprise();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        Enterprise loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<Enterprise> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<Enterprise> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        Enterprise given = constructSample(1);
        Enterprise saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private Enterprise constructSample(int i) {
		Enterprise given = new Enterprise();
        given.setName("name-"+i);
        given.setPhone("phone-"+i);
        given.setAddress("address-"+i);
        given.setCurrency(new Currency(1L));
        return given;
    }

}
