package ma.zyn.app.unit.service.impl.collaborator.auth;

import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.dao.facade.core.auth.CollaboratorPermissionOverrideDao;
import ma.zyn.app.service.impl.collaborator.auth.CollaboratorPermissionOverrideCollaboratorServiceImpl;

import ma.zyn.app.bean.core.enterprise.EnterpriseMembership ;
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
class CollaboratorPermissionOverrideCollaboratorServiceImplTest {

    @Mock
    private CollaboratorPermissionOverrideDao repository;
    private AutoCloseable autoCloseable;
    private CollaboratorPermissionOverrideCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CollaboratorPermissionOverrideCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllCollaboratorPermissionOverride() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveCollaboratorPermissionOverride() {
        // Given
        CollaboratorPermissionOverride toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteCollaboratorPermissionOverride() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetCollaboratorPermissionOverrideById() {
        // Given
        Long idToRetrieve = 1L; // Example CollaboratorPermissionOverride ID to retrieve
        CollaboratorPermissionOverride expected = new CollaboratorPermissionOverride(); // You need to replace CollaboratorPermissionOverride with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        CollaboratorPermissionOverride result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
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
