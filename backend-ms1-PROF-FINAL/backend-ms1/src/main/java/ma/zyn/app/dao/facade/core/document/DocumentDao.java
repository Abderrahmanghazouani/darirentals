package ma.zyn.app.dao.facade.core.document;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.document.Document;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface DocumentDao extends AbstractRepository<Document,Long>  {

    List<Document> findByDocumentTypeCode(String code);
    List<Document> findByDocumentTypeId(Long id);
    int deleteByDocumentTypeId(Long id);
    int deleteByDocumentTypeCode(String code);
    long countByDocumentTypeCode(String code);
    List<Document> findByReservationId(Long id);
    int deleteByReservationId(Long id);
    long countByReservationReference(String reference);
    List<Document> findByChargeId(Long id);
    int deleteByChargeId(Long id);
    long countByChargeId(Long id);

    @Query("SELECT NEW Document(item.id,item.fileName) FROM Document item")
    List<Document> findAllOptimized();

}
