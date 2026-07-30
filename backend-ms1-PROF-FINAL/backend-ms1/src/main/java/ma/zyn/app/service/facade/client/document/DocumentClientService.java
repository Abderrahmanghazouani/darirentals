package ma.zyn.app.service.facade.client.document;

import java.util.List;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.dao.criteria.core.document.DocumentCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface DocumentClientService {



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




	Document create(Document t);

    Document update(Document t);

    List<Document> update(List<Document> ts,boolean createIfNotExist);

    Document findById(Long id);

    Document findOrSave(Document t);

    Document findByReferenceEntity(Document t);

    Document findWithAssociatedLists(Long id);

    List<Document> findAllOptimized();

    List<Document> findAll();

    List<Document> findByCriteria(DocumentCriteria criteria);

    List<Document> findPaginatedByCriteria(DocumentCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(DocumentCriteria criteria);

    List<Document> delete(List<Document> ts);

    boolean deleteById(Long id);

    List<List<Document>> getToBeSavedAndToBeDeleted(List<Document> oldList, List<Document> newList);

}
