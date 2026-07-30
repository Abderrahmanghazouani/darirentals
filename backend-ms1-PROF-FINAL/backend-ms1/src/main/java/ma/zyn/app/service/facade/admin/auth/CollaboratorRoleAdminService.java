package ma.zyn.app.service.facade.admin.auth;

import java.util.List;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorRoleCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface CollaboratorRoleAdminService {







	CollaboratorRole create(CollaboratorRole t);

    CollaboratorRole update(CollaboratorRole t);

    List<CollaboratorRole> update(List<CollaboratorRole> ts,boolean createIfNotExist);

    CollaboratorRole findById(Long id);

    CollaboratorRole findOrSave(CollaboratorRole t);

    CollaboratorRole findByReferenceEntity(CollaboratorRole t);

    CollaboratorRole findWithAssociatedLists(Long id);

    List<CollaboratorRole> findAllOptimized();

    List<CollaboratorRole> findAll();

    List<CollaboratorRole> findByCriteria(CollaboratorRoleCriteria criteria);

    List<CollaboratorRole> findPaginatedByCriteria(CollaboratorRoleCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(CollaboratorRoleCriteria criteria);

    List<CollaboratorRole> delete(List<CollaboratorRole> ts);

    boolean deleteById(Long id);

    List<List<CollaboratorRole>> getToBeSavedAndToBeDeleted(List<CollaboratorRole> oldList, List<CollaboratorRole> newList);

}
