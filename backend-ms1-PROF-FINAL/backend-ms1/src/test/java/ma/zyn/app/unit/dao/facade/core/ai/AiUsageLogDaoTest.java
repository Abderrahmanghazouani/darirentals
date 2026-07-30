package ma.zyn.app.unit.dao.facade.core.ai;

import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.dao.facade.core.ai.AiUsageLogDao;

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
import ma.zyn.app.bean.core.ai.AiUsageType ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.document.Document ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class AiUsageLogDaoTest {

@Autowired
    private AiUsageLogDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        AiUsageLog entity = new AiUsageLog();
        entity.setId(id);
        underTest.save(entity);
        AiUsageLog loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        AiUsageLog entity = new AiUsageLog();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        AiUsageLog loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<AiUsageLog> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<AiUsageLog> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        AiUsageLog given = constructSample(1);
        AiUsageLog saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
