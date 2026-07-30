package ma.zyn.app.unit.dao.facade.core.property;

import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.dao.facade.core.property.PropertyDao;

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

import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.property.PropertyType ;
import ma.zyn.app.bean.core.property.PropertyStatus ;
import ma.zyn.app.bean.core.property.City ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class PropertyDaoTest {

@Autowired
    private PropertyDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        Property entity = new Property();
        entity.setId(id);
        underTest.save(entity);
        Property loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        Property entity = new Property();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        Property loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<Property> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<Property> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        Property given = constructSample(1);
        Property saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private Property constructSample(int i) {
		Property given = new Property();
        given.setName("name-"+i);
        given.setStreetNumber("streetNumber-"+i);
        given.setStreetName("streetName-"+i);
        given.setPostalCode("postalCode-"+i);
        given.setCapacity(i);
        given.setPricePerNight(BigDecimal.TEN);
        given.setLatitude(BigDecimal.TEN);
        given.setLongitude(BigDecimal.TEN);
        given.setPropertyType(new PropertyType(1L));
        given.setPropertyStatus(new PropertyStatus(1L));
        given.setCity(new City(1L));
        given.setEnterprise(new Enterprise(1L));
        return given;
    }

}
