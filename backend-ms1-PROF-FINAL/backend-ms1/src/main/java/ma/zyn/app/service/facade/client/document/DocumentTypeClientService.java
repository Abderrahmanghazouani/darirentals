package ma.zyn.app.service.facade.client.document;

import java.util.List;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.dao.criteria.core.document.DocumentTypeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface DocumentTypeClientService {







	DocumentType create(DocumentType t);

    DocumentType update(DocumentType t);

    List<DocumentType> update(List<DocumentType> ts,boolean createIfNotExist);

    DocumentType findById(Long id);

    DocumentType findOrSave(DocumentType t);

    DocumentType findByReferenceEntity(DocumentType t);

    DocumentType findWithAssociatedLists(Long id);

    List<DocumentType> findAllOptimized();

    List<DocumentType> findAll();

    List<DocumentType> findByCriteria(DocumentTypeCriteria criteria);

    List<DocumentType> findPaginatedByCriteria(DocumentTypeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(DocumentTypeCriteria criteria);

    List<DocumentType> delete(List<DocumentType> ts);

    boolean deleteById(Long id);

    List<List<DocumentType>> getToBeSavedAndToBeDeleted(List<DocumentType> oldList, List<DocumentType> newList);

}
