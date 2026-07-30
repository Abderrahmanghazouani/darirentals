package ma.zyn.app.unit.service.impl.collaborator.property;

import ma.zyn.app.bean.core.property.City;
import ma.zyn.app.dao.facade.core.property.CityDao;
import ma.zyn.app.service.impl.collaborator.property.CityCollaboratorServiceImpl;

import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.property.Country ;
import ma.zyn.app.bean.core.property.PropertyType ;
import ma.zyn.app.bean.core.property.PropertyStatus ;
import ma.zyn.app.bean.core.property.City ;
import ma.zyn.app.bean.core.property.Property ;
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
class CityCollaboratorServiceImplTest {

    @Mock
    private CityDao repository;
    private AutoCloseable autoCloseable;
    private CityCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CityCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllCity() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveCity() {
        // Given
        City toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteCity() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetCityById() {
        // Given
        Long idToRetrieve = 1L; // Example City ID to retrieve
        City expected = new City(); // You need to replace City with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        City result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private City constructSample(int i) {
		City given = new City();
        given.setName("name-"+i);
        given.setCountry(new Country(1L));
        List<Property> properties = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Property element = new Property();
                                                element.setId((long)id);
                                                element.setName("name"+id);
                                                element.setStreetNumber("streetNumber"+id);
                                                element.setStreetName("streetName"+id);
                                                element.setPostalCode("postalCode"+id);
                                                element.setCapacity(5);
                                                element.setPricePerNight(new BigDecimal(6*10));
                                                element.setLatitude(new BigDecimal(7*10));
                                                element.setLongitude(new BigDecimal(8*10));
                                                element.setPropertyType(new PropertyType(Long.valueOf(9)));
                                                element.setPropertyStatus(new PropertyStatus(Long.valueOf(10)));
                                                element.setCity(new City(Long.valueOf(11)));
                                                element.setEnterprise(new Enterprise(Long.valueOf(12)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setProperties(properties);
        return given;
    }

}
