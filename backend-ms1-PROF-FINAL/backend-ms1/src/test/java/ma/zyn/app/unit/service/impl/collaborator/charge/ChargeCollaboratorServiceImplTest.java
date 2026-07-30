package ma.zyn.app.unit.service.impl.collaborator.charge;

import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.dao.facade.core.charge.ChargeDao;
import ma.zyn.app.service.impl.collaborator.charge.ChargeCollaboratorServiceImpl;

import ma.zyn.app.bean.core.payment.Payment ;
import ma.zyn.app.bean.core.charge.ChargeType ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.document.DocumentType ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.bean.core.document.Document ;
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
class ChargeCollaboratorServiceImplTest {

    @Mock
    private ChargeDao repository;
    private AutoCloseable autoCloseable;
    private ChargeCollaboratorServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new ChargeCollaboratorServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllCharge() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveCharge() {
        // Given
        Charge toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteCharge() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetChargeById() {
        // Given
        Long idToRetrieve = 1L; // Example Charge ID to retrieve
        Charge expected = new Charge(); // You need to replace Charge with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Charge result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Charge constructSample(int i) {
		Charge given = new Charge();
        given.setLabel("label-"+i);
        given.setAmount(BigDecimal.TEN);
        given.setProperty(new Property(1L));
        given.setChargeType(new ChargeType(1L));
        given.setPayment(new Payment(1L));
        List<Document> documents = IntStream.rangeClosed(1, 3)
                                             .mapToObj(id -> {
                                                Document element = new Document();
                                                element.setId((long)id);
                                                element.setFileName("fileName"+id);
                                                element.setFile("file"+id);
                                                element.setExtractedVendor("extractedVendor"+id);
                                                element.setExtractedAmount(new BigDecimal(4*10));
                                                element.setDocumentType(new DocumentType(Long.valueOf(5)));
                                                element.setReservation(new Reservation(Long.valueOf(6)));
                                                element.setCharge(new Charge(Long.valueOf(7)));
                                                return element;
                                             })
                                             .collect(Collectors.toList());
        given.setDocuments(documents);
        return given;
    }

}
