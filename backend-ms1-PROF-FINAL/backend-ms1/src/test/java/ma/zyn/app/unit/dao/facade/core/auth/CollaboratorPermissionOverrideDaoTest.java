package ma.zyn.app.unit.dao.facade.core.auth;

import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.dao.facade.core.auth.CollaboratorPermissionOverrideDao;

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

import ma.zyn.app.bean.core.enterprise.EnterpriseMembership ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CollaboratorPermissionOverrideDaoTest {

@Autowired
    private CollaboratorPermissionOverrideDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        CollaboratorPermissionOverride entity = new CollaboratorPermissionOverride();
        entity.setId(id);
        underTest.save(entity);
        CollaboratorPermissionOverride loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        CollaboratorPermissionOverride entity = new CollaboratorPermissionOverride();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        CollaboratorPermissionOverride loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<CollaboratorPermissionOverride> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<CollaboratorPermissionOverride> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        CollaboratorPermissionOverride given = constructSample(1);
        CollaboratorPermissionOverride saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private CollaboratorPermissionOverride constructSample(int i) {
		CollaboratorPermissionOverride given = new CollaboratorPermissionOverride();
        given.setCanManageFinancials(false);
        given.setCanManageUsers(false);
        given.setCanDeleteProperty(false);
        given.setCanManageServiceProviders(false);
        given.setCanManageAiUsage(false);
        given.setEnterpriseMembership(new EnterpriseMembership(1L));
        return given;
    }

}
