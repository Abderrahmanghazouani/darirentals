package ma.zyn.app.unit.service.impl.client.ai;

import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.dao.facade.core.ai.AiQuotaDao;
import ma.zyn.app.service.impl.client.ai.AiQuotaClientServiceImpl;

import ma.zyn.app.bean.core.enterprise.Enterprise ;
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
class AiQuotaClientServiceImplTest {

    @Mock
    private AiQuotaDao repository;
    private AutoCloseable autoCloseable;
    private AiQuotaClientServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AiQuotaClientServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllAiQuota() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveAiQuota() {
        // Given
        AiQuota toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteAiQuota() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetAiQuotaById() {
        // Given
        Long idToRetrieve = 1L; // Example AiQuota ID to retrieve
        AiQuota expected = new AiQuota(); // You need to replace AiQuota with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        AiQuota result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private AiQuota constructSample(int i) {
		AiQuota given = new AiQuota();
        given.setTokensAllocated(i*1L);
        given.setEnterprise(new Enterprise(1L));
        return given;
    }

}
