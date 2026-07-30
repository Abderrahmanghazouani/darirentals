package ma.zyn.app.unit.dao.facade.core.report;

import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.dao.facade.core.report.FinancialReportPropertyDao;

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

import ma.zyn.app.bean.core.report.FinancialReport ;
import ma.zyn.app.bean.core.property.Property ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FinancialReportPropertyDaoTest {

@Autowired
    private FinancialReportPropertyDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        FinancialReportProperty entity = new FinancialReportProperty();
        entity.setId(id);
        underTest.save(entity);
        FinancialReportProperty loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        FinancialReportProperty entity = new FinancialReportProperty();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        FinancialReportProperty loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<FinancialReportProperty> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<FinancialReportProperty> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        FinancialReportProperty given = constructSample(1);
        FinancialReportProperty saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private FinancialReportProperty constructSample(int i) {
		FinancialReportProperty given = new FinancialReportProperty();
        given.setFinancialReport(new FinancialReport(1L));
        given.setProperty(new Property(1L));
        return given;
    }

}
