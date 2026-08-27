package ma.zyn.app.service.facade.admin.auth;

import java.util.List;
import ma.zyn.app.bean.core.auth.CollaboratorPropertyAccess;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorPropertyAccessCriteria;



public interface CollaboratorPropertyAccessAdminService {



    List<CollaboratorPropertyAccess> findByCollaboratorId(Long id);
    int deleteByCollaboratorId(Long id);
    long countByCollaboratorId(Long id);
    List<CollaboratorPropertyAccess> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);




	CollaboratorPropertyAccess create(CollaboratorPropertyAccess t);

    CollaboratorPropertyAccess update(CollaboratorPropertyAccess t);

    List<CollaboratorPropertyAccess> update(List<CollaboratorPropertyAccess> ts,boolean createIfNotExist);

    CollaboratorPropertyAccess findById(Long id);

    CollaboratorPropertyAccess findOrSave(CollaboratorPropertyAccess t);

    CollaboratorPropertyAccess findByReferenceEntity(CollaboratorPropertyAccess t);

    CollaboratorPropertyAccess findWithAssociatedLists(Long id);

    List<CollaboratorPropertyAccess> findAllOptimized();

    List<CollaboratorPropertyAccess> findAll();

    List<CollaboratorPropertyAccess> findByCriteria(CollaboratorPropertyAccessCriteria criteria);

    List<CollaboratorPropertyAccess> findPaginatedByCriteria(CollaboratorPropertyAccessCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(CollaboratorPropertyAccessCriteria criteria);

    List<CollaboratorPropertyAccess> delete(List<CollaboratorPropertyAccess> ts);

    boolean deleteById(Long id);

    List<List<CollaboratorPropertyAccess>> getToBeSavedAndToBeDeleted(List<CollaboratorPropertyAccess> oldList, List<CollaboratorPropertyAccess> newList);

}
