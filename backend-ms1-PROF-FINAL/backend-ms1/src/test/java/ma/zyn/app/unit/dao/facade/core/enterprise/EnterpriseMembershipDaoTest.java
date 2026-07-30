package ma.zyn.app.unit.dao.facade.core.enterprise;

import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.dao.facade.core.enterprise.EnterpriseMembershipDao;

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
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.auth.CollaboratorRole ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class EnterpriseMembershipDaoTest {

@Autowired
    private EnterpriseMembershipDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        EnterpriseMembership entity = new EnterpriseMembership();
        entity.setId(id);
        underTest.save(entity);
        EnterpriseMembership loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        EnterpriseMembership entity = new EnterpriseMembership();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        EnterpriseMembership loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<EnterpriseMembership> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<EnterpriseMembership> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        EnterpriseMembership given = constructSample(1);
        EnterpriseMembership saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private EnterpriseMembership constructSample(int i) {
		EnterpriseMembership given = new EnterpriseMembership();
        given.setCollaborator(new Collaborator(1L));
        given.setEnterprise(new Enterprise(1L));
        given.setCollaboratorRole(new CollaboratorRole(1L));
        return given;
    }

}
