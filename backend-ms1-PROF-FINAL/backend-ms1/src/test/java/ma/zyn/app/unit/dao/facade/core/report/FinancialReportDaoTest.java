package ma.zyn.app.unit.dao.facade.core.report;

import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.dao.facade.core.report.FinancialReportDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.BeforeEach;

import java.math.BigDecimal;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.time.LocalDateTime;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.report.FinancialReportType ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.report.FinancialReportScope ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FinancialReportDaoTest {

@Autowired
    private FinancialReportDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        FinancialReport entity = new FinancialReport();
        entity.setId(id);
        underTest.save(entity);
        FinancialReport loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        FinancialReport entity = new FinancialReport();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        FinancialReport loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<FinancialReport> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<FinancialReport> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        FinancialReport given = constructSample(1);
        FinancialReport saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
        return given;
    }

}
