package ma.zyn.app.unit.dao.facade.core.ai;

import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.dao.facade.core.ai.AiQuotaDao;

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

import ma.zyn.app.bean.core.enterprise.Enterprise ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AiQuotaDaoTest {

@Autowired
    private AiQuotaDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        AiQuota entity = new AiQuota();
        entity.setId(id);
        underTest.save(entity);
        AiQuota loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        AiQuota entity = new AiQuota();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        AiQuota loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<AiQuota> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<AiQuota> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        AiQuota given = constructSample(1);
        AiQuota saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private AiQuota constructSample(int i) {
		AiQuota given = new AiQuota();
        given.setTokensAllocated(i*1L);
        given.setEnterprise(new Enterprise(1L));
        return given;
    }

}
