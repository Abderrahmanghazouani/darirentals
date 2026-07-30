package ma.zyn.app.unit.dao.facade.core.document;

import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.dao.facade.core.document.DocumentDao;

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

import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.bean.core.document.DocumentType ;
import ma.zyn.app.bean.core.charge.Charge ;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class DocumentDaoTest {

@Autowired
    private DocumentDao underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }


    @Test
    void shouldFindById(){
        Long id = 1L;
        Document entity = new Document();
        entity.setId(id);
        underTest.save(entity);
        Document loaded = underTest.findById(id).orElse(null);
        assertThat(loaded.getId()).isEqualTo(id);
    }

    @Test
    void shoulDeleteById() {
        Long id = 1L;
        Document entity = new Document();
        entity.setId(id);
        underTest.save(entity);

        underTest.deleteById(id);

        Document loaded = underTest.findById(id).orElse(null);
        assertThat(loaded).isNull();
    }


    @Test
    void shouldFindAll() {
        // Given
        List<Document> items = IntStream.rangeClosed(1, 10).mapToObj(i->constructSample(i)).collect(Collectors.toList());

        // Init
        items.forEach(underTest::save);

        // When
        List<Document> loadedItems = underTest.findAll();

        // Then
        assertThat(loadedItems).isNotNull();
        assertThat(loadedItems.size()).isEqualTo(10);
    }

    @Test
    void shouldSave(){
        Document given = constructSample(1);
        Document saved = underTest.save(given);
        assertThat(saved.getId()).isNotNull();
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
