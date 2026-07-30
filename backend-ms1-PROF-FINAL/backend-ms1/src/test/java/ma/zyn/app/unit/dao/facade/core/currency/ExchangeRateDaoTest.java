package ma.zyn.app.unit.dao.facade.core.currency;

import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.dao.facade.core.currency.ExchangeRateDao;

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
public class ExchangeRateDaoTest {

@Autowired
    private ExchangeRateDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        ExchangeRate entity = new ExchangeRate();
        entity.setId(id);
        underTest.save(entity);
        ExchangeRate loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        ExchangeRate entity = new ExchangeRate();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        ExchangeRate loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<ExchangeRate> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<ExchangeRate> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        ExchangeRate given = constructSample(1);
        ExchangeRate saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private ExchangeRate constructSample(int i) {
		ExchangeRate given = new ExchangeRate();
        given.setRate(BigDecimal.TEN);
        given.setSource("source-"+i);
        given.setBaseCurrency(new Currency(1L));
        given.setTargetCurrency(new Currency(1L));
        return given;
    }

}
