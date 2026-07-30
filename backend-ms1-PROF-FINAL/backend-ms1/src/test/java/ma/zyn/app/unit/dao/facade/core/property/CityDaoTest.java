package ma.zyn.app.unit.dao.facade.core.property;

import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.dao.facade.core.property.CityDao;

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

import ma.zyn.app.bean.core.property.Country ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CityDaoTest {

@Autowired
    private CityDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        City entity = new City();
        entity.setId(id);
        underTest.save(entity);
        City loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        City entity = new City();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        City loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<City> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<City> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        City given = constructSample(1);
        City saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private City constructSample(int i) {
		City given = new City();
        given.setName("name-"+i);
        given.setCountry(new Country(1L));
        return given;
    }

}
