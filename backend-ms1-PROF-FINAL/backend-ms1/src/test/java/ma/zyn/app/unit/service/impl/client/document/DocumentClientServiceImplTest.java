package ma.zyn.app.unit.service.impl.client.document;

import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.dao.facade.core.document.DocumentDao;
import ma.zyn.app.service.impl.client.document.DocumentClientServiceImpl;

import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.document.DocumentType ;
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
class DocumentClientServiceImplTest {

    @Mock
    private DocumentDao repository;
    private AutoCloseable autoCloseable;
    private DocumentClientServiceImpl underTest;



    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new DocumentClientServiceImpl(repository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void canGetAllDocument() {
         //when
        underTest.findAll();
        verify(repository).findAll();
    }

    @Test
    void itShouldSaveDocument() {
        // Given
        Document toSave = constructSample(1);
        when(repository.save(toSave)).thenReturn(toSave);

        // When
        underTest.create(toSave);

        // Then
        verify(repository).save(toSave);
    }

    @Test
    void itShouldDeleteDocument() {
        // Given
        Long idToDelete = 1L;
        when(repository.existsById(idToDelete)).thenReturn(true);

        // When
        underTest.deleteById(idToDelete);

        // Then
        verify(repository).deleteById(idToDelete);
    }
    @Test
    void itShouldGetDocumentById() {
        // Given
        Long idToRetrieve = 1L; // Example Document ID to retrieve
        Document expected = new Document(); // You need to replace Document with your actual class
        expected.setId(idToRetrieve);
        when(repository.findById(idToRetrieve)).thenReturn(java.util.Optional.of(expected));

        // When
        Document result = underTest.findById(idToRetrieve);

        // Then
        assertEquals(expected, result);
    }
	
	private Document constructSample(int i) {
		Document given = new Document();
        given.setFileName("fileName-"+i);
        given.setFile("file-"+i);
        given.setExtractedVendor("extractedVendor-"+i);
        given.setExtractedAmount(BigDecimal.TEN);
        given.setDocumentType(new DocumentType(1L));
        given.setReservation(new Reservation(1L));
        given.setCharge(new Charge(1L));
        return given;
    }

}
