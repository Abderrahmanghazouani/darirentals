package ma.zyn.app.unit.service.impl.collaborator.property;

import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.dao.facade.core.property.PropertyStatusDao;
import ma.zyn.app.service.impl.collaborator.property.PropertyStatusCollaboratorServiceImpl;

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
class PropertyStatusCollaboratorServiceImplTest {

    @Mock
    private PropertyStatusDao repository;
    private AutoCloseable autoCloseable;
    private PropertyStatusCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new PropertyStatusCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllPropertyStatus() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSavePropertyStatus() {
        // Given
        PropertyStatus toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeletePropertyStatus() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetPropertyStatusById() {
        // Given
        Long idToRetrieve = 1L; // Example PropertyStatus ID to retrieve
        PropertyStatus expected = new PropertyStatus(); // You need to replace PropertyStatus with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        PropertyStatus result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private PropertyStatus constructSample(int i) {
		PropertyStatus given = new PropertyStatus();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        return given;
    }

}
