package ma.zyn.app.unit.dao.facade.core.ai;

import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.dao.facade.core.ai.AiUsageTypeDao;

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
public class AiUsageTypeDaoTest {

@Autowired
    private AiUsageTypeDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindByCode(){
        String code = "code-1";
        AiUsageType entity = new AiUsageType();
        entity.setCode(code);
        underTest.save(entity);
        AiUsageType loaded = underTest.findByCode(code);
        assertThat(loaded.getCode()).isEqualTo(code);
    }

    @Test
    void shouldDeleteByCode() {
        String code = "code-12345678";
        int result = underTest.deleteByCode(code);

        AiUsageType loaded = underTest.findByCode(code);
        assertThat(loaded).isNull();
        assertThat(result).isEqualTo(0);
    }

    @Test
    void shouldFindById(){
        Long id = 1L;
        AiUsageType entity = new AiUsageType();
        entity.setId(id);
        underTest.save(entity);
        AiUsageType loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        AiUsageType entity = new AiUsageType();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        AiUsageType loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<AiUsageType> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<AiUsageType> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        AiUsageType given = constructSample(1);
        AiUsageType saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
