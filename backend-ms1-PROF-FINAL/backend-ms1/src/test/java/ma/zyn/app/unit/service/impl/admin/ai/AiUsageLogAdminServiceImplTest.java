package ma.zyn.app.unit.service.impl.admin.ai;

import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.dao.facade.core.ai.AiUsageLogDao;
import ma.zyn.app.service.impl.admin.ai.AiUsageLogAdminServiceImpl;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.ai.AiUsageType ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.document.Document ;
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
class AiUsageLogAdminServiceImplTest {

    @Mock
    private AiUsageLogDao repository;
    private AutoCloseable autoCloseable;
    private AiUsageLogAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new AiUsageLogAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllAiUsageLog() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveAiUsageLog() {
        // Given
        AiUsageLog toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteAiUsageLog() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetAiUsageLogById() {
        // Given
        Long idToRetrieve = 1L; // Example AiUsageLog ID to retrieve
        AiUsageLog expected = new AiUsageLog(); // You need to replace AiUsageLog with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        AiUsageLog result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private AiUsageLog constructSample(int i) {
		AiUsageLog given = new AiUsageLog();
        given.setTokensUsed(i*1L);
        given.setDate(LocalDateTime.now());
        given.setEnterprise(new Enterprise(1L));
        given.setAiUsageType(new AiUsageType(1L));
        given.setCollaborator(new Collaborator(1L));
        given.setDocument(new Document(1L));
        return given;
    }

}
