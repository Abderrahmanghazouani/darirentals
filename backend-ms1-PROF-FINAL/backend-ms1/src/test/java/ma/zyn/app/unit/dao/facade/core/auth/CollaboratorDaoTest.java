package ma.zyn.app.unit.dao.facade.core.auth;

import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.dao.facade.core.auth.CollaboratorDao;

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

import ma.zyn.app.bean.core.currency.Currency ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CollaboratorDaoTest {

@Autowired
    private CollaboratorDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void shouldFindByEmail(){
        String email = "email-1";
        Collaborator entity = new Collaborator();
        entity.setEmail(email);
        underTest.save(entity);
        Collaborator loaded = underTest.findByEmail(email);
        assertThat(loaded.getEmail()).isEqualTo(email);
    }

    @Test
    void shouldDeleteByEmail() {
        String email = "email-12345678";
        int result = underTest.deleteByEmail(email);

        Collaborator loaded = underTest.findByEmail(email);
        assertThat(loaded).isNull();
        assertThat(result).isEqualTo(0);
    }

    @Test
    void shouldFindById(){
        Long id = 1L;
        Collaborator entity = new Collaborator();
        entity.setId(id);
        underTest.save(entity);
        Collaborator loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        Collaborator entity = new Collaborator();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        Collaborator loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<Collaborator> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<Collaborator> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        Collaborator given = constructSample(1);
        Collaborator saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
    }

    private Collaborator constructSample(int i) {
		Collaborator given = new Collaborator();
        given.setName("name-"+i);
        given.setPhone("phone-"+i);
        given.setIsActive(false);
        given.setDisplayCurrency(new Currency(1L));
        given.setEmail("email-"+i);
        given.setEnabled(false);
        given.setCredentialsNonExpired(false);
        given.setAccountNonExpired(false);
        given.setUsername("username-"+i);
        given.setPasswordChanged(false);
        given.setAccountNonLocked(false);
        given.setPassword("password-"+i);
        return given;
    }

}
