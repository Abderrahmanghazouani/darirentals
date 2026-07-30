package ma.zyn.app.unit.service.impl.collaborator.report;

import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.dao.facade.core.report.FinancialReportScopeDao;
import ma.zyn.app.service.impl.collaborator.report.FinancialReportScopeCollaboratorServiceImpl;

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
class FinancialReportScopeCollaboratorServiceImplTest {

    @Mock
    private FinancialReportScopeDao repository;
    private AutoCloseable autoCloseable;
    private FinancialReportScopeCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new FinancialReportScopeCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllFinancialReportScope() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveFinancialReportScope() {
        // Given
        FinancialReportScope toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteFinancialReportScope() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetFinancialReportScopeById() {
        // Given
        Long idToRetrieve = 1L; // Example FinancialReportScope ID to retrieve
        FinancialReportScope expected = new FinancialReportScope(); // You need to replace FinancialReportScope with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        FinancialReportScope result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private FinancialReportScope constructSample(int i) {
		FinancialReportScope given = new FinancialReportScope();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        return given;
    }

}
