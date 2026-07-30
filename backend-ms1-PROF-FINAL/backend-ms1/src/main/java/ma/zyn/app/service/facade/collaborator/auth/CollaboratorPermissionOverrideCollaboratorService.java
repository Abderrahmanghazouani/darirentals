package ma.zyn.app.service.facade.collaborator.auth;

import java.util.List;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorPermissionOverrideCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface CollaboratorPermissionOverrideCollaboratorService {



    List<CollaboratorPermissionOverride> findByEnterpriseMembershipId(Long id);
    int deleteByEnterpriseMembershipId(Long id);
    long countByEnterpriseMembershipId(Long id);




	CollaboratorPermissionOverride create(CollaboratorPermissionOverride t);

    CollaboratorPermissionOverride update(CollaboratorPermissionOverride t);

    List<CollaboratorPermissionOverride> update(List<CollaboratorPermissionOverride> ts,boolean createIfNotExist);

    CollaboratorPermissionOverride findById(Long id);

    CollaboratorPermissionOverride findOrSave(CollaboratorPermissionOverride t);

    CollaboratorPermissionOverride findByReferenceEntity(CollaboratorPermissionOverride t);

    CollaboratorPermissionOverride findWithAssociatedLists(Long id);

    List<CollaboratorPermissionOverride> findAllOptimized();

    List<CollaboratorPermissionOverride> findAll();

    List<CollaboratorPermissionOverride> findByCriteria(CollaboratorPermissionOverrideCriteria criteria);

    List<CollaboratorPermissionOverride> findPaginatedByCriteria(CollaboratorPermissionOverrideCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(CollaboratorPermissionOverrideCriteria criteria);

    List<CollaboratorPermissionOverride> delete(List<CollaboratorPermissionOverride> ts);

    boolean deleteById(Long id);

    List<List<CollaboratorPermissionOverride>> getToBeSavedAndToBeDeleted(List<CollaboratorPermissionOverride> oldList, List<CollaboratorPermissionOverride> newList);

}
