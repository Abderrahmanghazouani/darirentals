package ma.zyn.app.unit.service.impl.admin.property;

import ma.zyn.app.bean.core.property.Country;
import ma.zyn.app.dao.facade.core.property.CountryDao;
import ma.zyn.app.service.impl.admin.property.CountryAdminServiceImpl;

import ma.zyn.app.bean.core.property.City ;
import ma.zyn.app.bean.core.property.Country ;
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
class CountryAdminServiceImplTest {

    @Mock
    private CountryDao repository;
    private AutoCloseable autoCloseable;
    private CountryAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CountryAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllCountry() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveCountry() {
        // Given
        Country toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteCountry() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetCountryById() {
        // Given
        Long idToRetrieve = 1L; // Example Country ID to retrieve
        Country expected = new Country(); // You need to replace Country with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Country result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Country constructSample(int i) {
		Country given = new Country();
        given.setName("name-"+i);
        given.setCode("code-"+i);
        List<City> cities = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                City element = new City();
                                                element.setId((long)id);
                                                element.setName("name"+id);
                                                element.setCountry(new Country(Long.valueOf(2)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setCities(cities);
        return given;
    }

}
