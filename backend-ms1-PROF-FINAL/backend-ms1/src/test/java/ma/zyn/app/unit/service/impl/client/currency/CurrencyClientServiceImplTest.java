package ma.zyn.app.unit.service.impl.client.currency;

import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.dao.facade.core.currency.CurrencyDao;
import ma.zyn.app.service.impl.client.currency.CurrencyClientServiceImpl;

import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.bean.core.currency.ExchangeRate ;
import ma.zyn.app.bean.core.currency.Currency ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
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
class CurrencyClientServiceImplTest {

    @Mock
    private CurrencyDao repository;
    private AutoCloseable autoCloseable;
    private CurrencyClientServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CurrencyClientServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllCurrency() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveCurrency() {
        // Given
        Currency toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteCurrency() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetCurrencyById() {
        // Given
        Long idToRetrieve = 1L; // Example Currency ID to retrieve
        Currency expected = new Currency(); // You need to replace Currency with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Currency result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Currency constructSample(int i) {
		Currency given = new Currency();
        given.setDescription("description-"+i);
        given.setCode("code-"+i);
        given.setLabel("label-"+i);
        given.setStyle("style-"+i);
        given.setIsDefault(false);
        given.setSortOrder(i);
        given.setSymbol("symbol-"+i);
        List<ExchangeRate> exchangeRatesAsBase = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                ExchangeRate element = new ExchangeRate();
                                                element.setId((long)id);
                                                element.setRate(new BigDecimal(1*10));
                                                element.setSource("source"+id);
                                                element.setBaseCurrency(new Currency(Long.valueOf(3)));
                                                element.setTargetCurrency(new Currency(Long.valueOf(4)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setExchangeRatesAsBase(exchangeRatesAsBase);
        List<ExchangeRate> exchangeRatesAsTarget = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                ExchangeRate element = new ExchangeRate();
                                                element.setId((long)id);
                                                element.setRate(new BigDecimal(1*10));
                                                element.setSource("source"+id);
                                                element.setBaseCurrency(new Currency(Long.valueOf(3)));
                                                element.setTargetCurrency(new Currency(Long.valueOf(4)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setExchangeRatesAsTarget(exchangeRatesAsTarget);
        List<Enterprise> enterprises = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Enterprise element = new Enterprise();
                                                element.setId((long)id);
                                                element.setName("name"+id);
                                                element.setPhone("phone"+id);
                                                element.setAddress("address"+id);
                                                element.setCurrency(new Currency(Long.valueOf(4)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setEnterprises(enterprises);
        List<Collaborator> collaborators = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Collaborator element = new Collaborator();
                                                element.setId((long)id);
                                                element.setName("name"+id);
                                                element.setPhone("phone"+id);
                                                element.setIsActive(true);
                                                element.setDisplayCurrency(new Currency(Long.valueOf(4)));
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
        given.setCollaborators(collaborators);
        return given;
    }

}
