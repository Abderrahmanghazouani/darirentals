package ma.zyn.app.unit.service.impl.admin.payment;

import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.dao.facade.core.payment.PaymentDao;
import ma.zyn.app.service.impl.admin.payment.PaymentAdminServiceImpl;

import ma.zyn.app.bean.core.payment.PaymentStatus ;
import ma.zyn.app.bean.core.payment.Payment ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.bean.core.charge.ChargeType ;
import ma.zyn.app.bean.core.payment.PaymentType ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.charge.Charge ;
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
class PaymentAdminServiceImplTest {

    @Mock
    private PaymentDao repository;
    private AutoCloseable autoCloseable;
    private PaymentAdminServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new PaymentAdminServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllPayment() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSavePayment() {
        // Given
        Payment toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeletePayment() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetPaymentById() {
        // Given
        Long idToRetrieve = 1L; // Example Payment ID to retrieve
        Payment expected = new Payment(); // You need to replace Payment with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Payment result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Payment constructSample(int i) {
		Payment given = new Payment();
        given.setAmount(BigDecimal.TEN);
        given.setNotes("notes-"+i);
        given.setServiceProvider(new ServiceProvider(1L));
        given.setPaymentType(new PaymentType(1L));
        given.setPaymentStatus(new PaymentStatus(1L));
        List<Charge> charges = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Charge element = new Charge();
                                                element.setId((long)id);
                                                element.setLabel("label"+id);
                                                element.setAmount(new BigDecimal(2*10));
                                                element.setProperty(new Property(Long.valueOf(3)));
                                                element.setChargeType(new ChargeType(Long.valueOf(4)));
                                                element.setPayment(new Payment(Long.valueOf(5)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setCharges(charges);
        return given;
    }

}
