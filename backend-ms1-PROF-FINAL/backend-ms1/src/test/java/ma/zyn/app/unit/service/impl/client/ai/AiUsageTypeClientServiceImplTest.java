package ma.zyn.app.unit.service.impl.client.ai;

import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.dao.facade.core.ai.AiUsageTypeDao;
import ma.zyn.app.service.impl.client.ai.AiUsageTypeClientServiceImpl;

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
class AiUsageTypeClientServiceImplTest {

    @Mock
    private AiUsageTypeDao repository;
    private AutoCloseable autoCloseable;
    private AiUsageTypeClientServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AiUsageTypeClientServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllAiUsageType() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveAiUsageType() {
        // Given
        AiUsageType toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteAiUsageType() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetAiUsageTypeById() {
        // Given
        Long idToRetrieve = 1L; // Example AiUsageType ID to retrieve
        AiUsageType expected = new AiUsageType(); // You need to replace AiUsageType with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        AiUsageType result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private AiUsageType constructSample(int i) {
		AiUsageType given = new AiUsageType();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        return given;
    }

}
