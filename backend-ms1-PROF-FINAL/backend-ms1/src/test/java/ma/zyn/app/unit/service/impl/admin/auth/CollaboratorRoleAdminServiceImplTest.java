package ma.zyn.app.unit.service.impl.admin.auth;

import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.dao.facade.core.auth.CollaboratorRoleDao;
import ma.zyn.app.service.impl.admin.auth.CollaboratorRoleAdminServiceImpl;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;



import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SpringBootTest
class CollaboratorRoleAdminServiceImplTest {

    @Mock
    private CollaboratorRoleDao repository;
    private AutoCloseable autoCloseable;
    private CollaboratorRoleAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CollaboratorRoleAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllCollaboratorRole() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveCollaboratorRole() {
        // Given
        CollaboratorRole toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteCollaboratorRole() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetCollaboratorRoleById() {
        // Given
        Long idToRetrieve = 1L; // Example CollaboratorRole ID to retrieve
        CollaboratorRole expected = new CollaboratorRole(); // You need to replace CollaboratorRole with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        CollaboratorRole result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
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
