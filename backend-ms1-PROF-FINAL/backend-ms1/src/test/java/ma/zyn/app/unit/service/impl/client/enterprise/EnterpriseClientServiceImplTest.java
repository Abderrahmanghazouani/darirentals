package ma.zyn.app.unit.service.impl.client.enterprise;

import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.dao.facade.core.enterprise.EnterpriseDao;
import ma.zyn.app.service.impl.client.enterprise.EnterpriseClientServiceImpl;

import ma.zyn.app.bean.core.ai.AiQuota ;
import ma.zyn.app.bean.core.ai.AiUsageLog ;
import ma.zyn.app.bean.core.report.FinancialReportType ;
import ma.zyn.app.bean.core.report.FinancialReport ;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership ;
import ma.zyn.app.bean.core.property.PropertyType ;
import ma.zyn.app.bean.core.property.City ;
import ma.zyn.app.bean.core.auth.CollaboratorRole ;
import ma.zyn.app.bean.core.document.Document ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.bean.core.ai.AiUsageType ;
import ma.zyn.app.bean.core.currency.Currency ;
import ma.zyn.app.bean.core.provider.ServiceType ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.bean.core.property.PropertyStatus ;
import ma.zyn.app.bean.core.report.FinancialReportScope ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.client.Client ;
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
class EnterpriseClientServiceImplTest {

    @Mock
    private EnterpriseDao repository;
    private AutoCloseable autoCloseable;
    private EnterpriseClientServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new EnterpriseClientServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllEnterprise() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveEnterprise() {
        // Given
        Enterprise toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteEnterprise() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetEnterpriseById() {
        // Given
        Long idToRetrieve = 1L; // Example Enterprise ID to retrieve
        Enterprise expected = new Enterprise(); // You need to replace Enterprise with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Enterprise result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Enterprise constructSample(int i) {
		Enterprise given = new Enterprise();
        given.setName("name-"+i);
        given.setPhone("phone-"+i);
        given.setAddress("address-"+i);
        given.setCurrency(new Currency(1L));
        List<Property> properties = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Property element = new Property();
                                                element.setId((long)id);
                                                element.setName("name"+id);
                                                element.setStreetNumber("streetNumber"+id);
                                                element.setStreetName("streetName"+id);
                                                element.setPostalCode("postalCode"+id);
                                                element.setCapacity(5);
                                                element.setPricePerNight(new BigDecimal(6*10));
                                                element.setLatitude(new BigDecimal(7*10));
                                                element.setLongitude(new BigDecimal(8*10));
                                                element.setPropertyType(new PropertyType(Long.valueOf(9)));
                                                element.setPropertyStatus(new PropertyStatus(Long.valueOf(10)));
                                                element.setCity(new City(Long.valueOf(11)));
                                                element.setEnterprise(new Enterprise(Long.valueOf(12)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setProperties(properties);
        List<Client> clients = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Client element = new Client();
                                                element.setId((long)id);
                                                element.setFullName("fullName"+id);
                                                element.setPhone("phone"+id);
                                                element.setNationality("nationality"+id);
                                                element.setEnterprise(new Enterprise(Long.valueOf(4)));
                                                element.setEmail("email"+id);
                                                element.setEnabled(true);
                                                element.setCredentialsNonExpired(true);
                                                element.setAccountNonExpired(true);
                                                element.setUsername("username"+id);
                                                element.setPasswordChanged(true);
                                                element.setAccountNonLocked(true);
                                                element.setPassword("password"+id);
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setClients(clients);
        List<ServiceProvider> serviceProviders = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                ServiceProvider element = new ServiceProvider();
                                                element.setId((long)id);
                                                element.setName("name"+id);
                                                element.setPhone("phone"+id);
                                                element.setIsActive(true);
                                                element.setServiceType(new ServiceType(Long.valueOf(4)));
                                                element.setEnterprise(new Enterprise(Long.valueOf(5)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setServiceProviders(serviceProviders);
        List<EnterpriseMembership> enterpriseMemberships = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                EnterpriseMembership element = new EnterpriseMembership();
                                                element.setId((long)id);
                                                element.setCollaborator(new Collaborator(Long.valueOf(1)));
                                                element.setEnterprise(new Enterprise(Long.valueOf(2)));
                                                element.setCollaboratorRole(new CollaboratorRole(Long.valueOf(3)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setEnterpriseMemberships(enterpriseMemberships);
        List<AiQuota> aiQuotas = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                AiQuota element = new AiQuota();
                                                element.setId((long)id);
                                                element.setTokensAllocated(1L);
                                                element.setEnterprise(new Enterprise(Long.valueOf(2)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setAiQuotas(aiQuotas);
        List<AiUsageLog> aiUsageLogs = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                AiUsageLog element = new AiUsageLog();
                                                element.setId((long)id);
                                                element.setTokensUsed(1L);
                                                element.setDate(LocalDateTime.now());
                                                element.setEnterprise(new Enterprise(Long.valueOf(3)));
                                                element.setAiUsageType(new AiUsageType(Long.valueOf(4)));
                                                element.setCollaborator(new Collaborator(Long.valueOf(5)));
                                                element.setDocument(new Document(Long.valueOf(6)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setAiUsageLogs(aiUsageLogs);
        List<FinancialReport> financialReports = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                FinancialReport element = new FinancialReport();
                                                element.setId((long)id);
                                                element.setTotalRevenue(new BigDecimal(1*10));
                                                element.setTotalCharges(new BigDecimal(2*10));
                                                element.setNetProfit(new BigDecimal(3*10));
                                                element.setGeneratedAt(LocalDateTime.now());
                                                element.setFile("file"+id);
                                                element.setFinancialReportType(new FinancialReportType(Long.valueOf(6)));
                                                element.setFinancialReportScope(new FinancialReportScope(Long.valueOf(7)));
                                                element.setEnterprise(new Enterprise(Long.valueOf(8)));
                                                element.setGeneratedBy(new Collaborator(Long.valueOf(9)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setFinancialReports(financialReports);
        return given;
    }

}
