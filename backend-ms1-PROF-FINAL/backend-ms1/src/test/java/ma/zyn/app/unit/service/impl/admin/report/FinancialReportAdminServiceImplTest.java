package ma.zyn.app.unit.service.impl.admin.report;

import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.dao.facade.core.report.FinancialReportDao;
import ma.zyn.app.service.impl.admin.report.FinancialReportAdminServiceImpl;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.report.FinancialReportType ;
import ma.zyn.app.bean.core.report.FinancialReportProperty ;
import ma.zyn.app.bean.core.report.FinancialReport ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.report.FinancialReportScope ;
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
class FinancialReportAdminServiceImplTest {

    @Mock
    private FinancialReportDao repository;
    private AutoCloseable autoCloseable;
    private FinancialReportAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new FinancialReportAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllFinancialReport() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveFinancialReport() {
        // Given
        FinancialReport toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteFinancialReport() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetFinancialReportById() {
        // Given
        Long idToRetrieve = 1L; // Example FinancialReport ID to retrieve
        FinancialReport expected = new FinancialReport(); // You need to replace FinancialReport with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        FinancialReport result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private FinancialReport constructSample(int i) {
		FinancialReport given = new FinancialReport();
        given.setTotalRevenue(BigDecimal.TEN);
        given.setTotalCharges(BigDecimal.TEN);
        given.setNetProfit(BigDecimal.TEN);
        given.setGeneratedAt(LocalDateTime.now());
        given.setFile("file-"+i);
        given.setFinancialReportType(new FinancialReportType(1L));
        given.setFinancialReportScope(new FinancialReportScope(1L));
        given.setEnterprise(new Enterprise(1L));
        given.setGeneratedBy(new Collaborator(1L));
        List<FinancialReportProperty> financialReportProperties = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                FinancialReportProperty element = new FinancialReportProperty();
                                                element.setId((long)id);
                                                element.setFinancialReport(new FinancialReport(Long.valueOf(1)));
                                                element.setProperty(new Property(Long.valueOf(2)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setFinancialReportProperties(financialReportProperties);
        return given;
    }

}
