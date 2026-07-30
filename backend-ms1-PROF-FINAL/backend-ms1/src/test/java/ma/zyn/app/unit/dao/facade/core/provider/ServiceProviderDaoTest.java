package ma.zyn.app.unit.dao.facade.core.provider;

import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.dao.facade.core.provider.ServiceProviderDao;

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

import ma.zyn.app.bean.core.provider.ServiceType ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ServiceProviderDaoTest {

@Autowired
    private ServiceProviderDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        ServiceProvider entity = new ServiceProvider();
        entity.setId(id);
        underTest.save(entity);
        ServiceProvider loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        ServiceProvider entity = new ServiceProvider();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        ServiceProvider loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<ServiceProvider> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<ServiceProvider> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        ServiceProvider given = constructSample(1);
        ServiceProvider saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private ServiceProvider constructSample(int i) {
		ServiceProvider given = new ServiceProvider();
        given.setName("name-"+i);
        given.setPhone("phone-"+i);
        given.setIsActive(false);
        given.setServiceType(new ServiceType(1L));
        given.setEnterprise(new Enterprise(1L));
        return given;
    }

}
