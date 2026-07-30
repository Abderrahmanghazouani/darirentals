package ma.zyn.app.unit.service.impl.collaborator.enterprise;

import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.dao.facade.core.enterprise.EnterpriseMembershipDao;
import ma.zyn.app.service.impl.collaborator.enterprise.EnterpriseMembershipCollaboratorServiceImpl;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership ;
import ma.zyn.app.bean.core.auth.CollaboratorRole ;
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
class EnterpriseMembershipCollaboratorServiceImplTest {

    @Mock
    private EnterpriseMembershipDao repository;
    private AutoCloseable autoCloseable;
    private EnterpriseMembershipCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EnterpriseMembershipCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllEnterpriseMembership() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveEnterpriseMembership() {
        // Given
        EnterpriseMembership toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteEnterpriseMembership() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetEnterpriseMembershipById() {
        // Given
        Long idToRetrieve = 1L; // Example EnterpriseMembership ID to retrieve
        EnterpriseMembership expected = new EnterpriseMembership(); // You need to replace EnterpriseMembership with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        EnterpriseMembership result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private EnterpriseMembership constructSample(int i) {
		EnterpriseMembership given = new EnterpriseMembership();
        given.setCollaborator(new Collaborator(1L));
        given.setEnterprise(new Enterprise(1L));
        given.setCollaboratorRole(new CollaboratorRole(1L));
        List<CollaboratorPermissionOverride> collaboratorPermissionOverrides = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                CollaboratorPermissionOverride element = new CollaboratorPermissionOverride();
                                                element.setId((long)id);
                                                element.setCanManageFinancials(true);
                                                element.setCanManageUsers(true);
                                                element.setCanDeleteProperty(true);
                                                element.setCanManageServiceProviders(true);
                                                element.setCanManageAiUsage(true);
                                                element.setEnterpriseMembership(new EnterpriseMembership(Long.valueOf(6)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setCollaboratorPermissionOverrides(collaboratorPermissionOverrides);
        return given;
    }

}
