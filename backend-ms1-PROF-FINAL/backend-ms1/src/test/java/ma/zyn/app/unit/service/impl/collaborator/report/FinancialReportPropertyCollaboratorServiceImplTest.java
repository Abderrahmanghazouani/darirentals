package ma.zyn.app.unit.service.impl.collaborator.report;

import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.dao.facade.core.report.FinancialReportPropertyDao;
import ma.zyn.app.service.impl.collaborator.report.FinancialReportPropertyCollaboratorServiceImpl;

import ma.zyn.app.bean.core.report.FinancialReport ;
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
class FinancialReportPropertyCollaboratorServiceImplTest {

    @Mock
    private FinancialReportPropertyDao repository;
    private AutoCloseable autoCloseable;
    private FinancialReportPropertyCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new FinancialReportPropertyCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllFinancialReportProperty() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveFinancialReportProperty() {
        // Given
        FinancialReportProperty toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteFinancialReportProperty() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetFinancialReportPropertyById() {
        // Given
        Long idToRetrieve = 1L; // Example FinancialReportProperty ID to retrieve
        FinancialReportProperty expected = new FinancialReportProperty(); // You need to replace FinancialReportProperty with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        FinancialReportProperty result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private FinancialReportProperty constructSample(int i) {
		FinancialReportProperty given = new FinancialReportProperty();
        given.setFinancialReport(new FinancialReport(1L));
        given.setProperty(new Property(1L));
        return given;
    }

}
