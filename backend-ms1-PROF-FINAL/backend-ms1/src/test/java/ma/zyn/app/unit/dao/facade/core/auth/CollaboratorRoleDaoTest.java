package ma.zyn.app.unit.dao.facade.core.auth;

import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.dao.facade.core.auth.CollaboratorRoleDao;

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
public class CollaboratorRoleDaoTest {

@Autowired
    private CollaboratorRoleDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindByCode(){
        String code = "code-1";
        CollaboratorRole entity = new CollaboratorRole();
        entity.setCode(code);
        underTest.save(entity);
        CollaboratorRole loaded = underTest.findByCode(code);
        assertThat(loaded.getCode()).isEqualTo(code);
    }

    @Test
    void shouldDeleteByCode() {
        String code = "code-12345678";
        int result = underTest.deleteByCode(code);

        CollaboratorRole loaded = underTest.findByCode(code);
        assertThat(loaded).isNull();
        assertThat(result).isEqualTo(0);
    }

    @Test
    void shouldFindById(){
        Long id = 1L;
        CollaboratorRole entity = new CollaboratorRole();
        entity.setId(id);
        underTest.save(entity);
        CollaboratorRole loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        CollaboratorRole entity = new CollaboratorRole();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        CollaboratorRole loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<CollaboratorRole> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<CollaboratorRole> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        CollaboratorRole given = constructSample(1);
        CollaboratorRole saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private CollaboratorRole constructSample(int i) {
		CollaboratorRole given = new CollaboratorRole();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        given.setCanManageFinancials(false);
        given.setCanManageUsers(false);
        given.setCanDeleteProperty(false);
        given.setCanManageServiceProviders(false);
        given.setCanManageAiUsage(false);
        return given;
    }

}
