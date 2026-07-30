package ma.zyn.app.unit.service.impl.collaborator.charge;

import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.dao.facade.core.charge.ChargeTypeDao;
import ma.zyn.app.service.impl.collaborator.charge.ChargeTypeCollaboratorServiceImpl;

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
class ChargeTypeCollaboratorServiceImplTest {

    @Mock
    private ChargeTypeDao repository;
    private AutoCloseable autoCloseable;
    private ChargeTypeCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ChargeTypeCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllChargeType() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveChargeType() {
        // Given
        ChargeType toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteChargeType() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetChargeTypeById() {
        // Given
        Long idToRetrieve = 1L; // Example ChargeType ID to retrieve
        ChargeType expected = new ChargeType(); // You need to replace ChargeType with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        ChargeType result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private ChargeType constructSample(int i) {
		ChargeType given = new ChargeType();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        return given;
    }

}
