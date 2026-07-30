package ma.zyn.app.unit.service.impl.collaborator.report;

import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.dao.facade.core.report.FinancialReportTypeDao;
import ma.zyn.app.service.impl.collaborator.report.FinancialReportTypeCollaboratorServiceImpl;

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
class FinancialReportTypeCollaboratorServiceImplTest {

    @Mock
    private FinancialReportTypeDao repository;
    private AutoCloseable autoCloseable;
    private FinancialReportTypeCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new FinancialReportTypeCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllFinancialReportType() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveFinancialReportType() {
        // Given
        FinancialReportType toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteFinancialReportType() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetFinancialReportTypeById() {
        // Given
        Long idToRetrieve = 1L; // Example FinancialReportType ID to retrieve
        FinancialReportType expected = new FinancialReportType(); // You need to replace FinancialReportType with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        FinancialReportType result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private FinancialReportType constructSample(int i) {
		FinancialReportType given = new FinancialReportType();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        return given;
    }

}
