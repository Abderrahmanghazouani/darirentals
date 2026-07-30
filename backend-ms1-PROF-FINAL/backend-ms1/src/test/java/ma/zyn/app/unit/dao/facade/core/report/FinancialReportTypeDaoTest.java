package ma.zyn.app.unit.dao.facade.core.report;

import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.dao.facade.core.report.FinancialReportTypeDao;

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


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class FinancialReportTypeDaoTest {

@Autowired
    private FinancialReportTypeDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindByCode(){
        String code = "code-1";
        FinancialReportType entity = new FinancialReportType();
        entity.setCode(code);
        underTest.save(entity);
        FinancialReportType loaded = underTest.findByCode(code);
        assertThat(loaded.getCode()).isEqualTo(code);
    }

    @Test
    void shouldDeleteByCode() {
        String code = "code-12345678";
        int result = underTest.deleteByCode(code);

        FinancialReportType loaded = underTest.findByCode(code);
        assertThat(loaded).isNull();
        assertThat(result).isEqualTo(0);
    }

    @Test
    void shouldFindById(){
        Long id = 1L;
        FinancialReportType entity = new FinancialReportType();
        entity.setId(id);
        underTest.save(entity);
        FinancialReportType loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        FinancialReportType entity = new FinancialReportType();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        FinancialReportType loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<FinancialReportType> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<FinancialReportType> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        FinancialReportType given = constructSample(1);
        FinancialReportType saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
