package ma.zyn.app.service.facade.collaborator.enterprise;

import java.util.List;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseMembershipCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface EnterpriseMembershipCollaboratorService {



    List<EnterpriseMembership> findByCollaboratorId(Long id);
    int deleteByCollaboratorId(Long id);
    long countByCollaboratorEmail(String email);
    List<EnterpriseMembership> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);
    List<EnterpriseMembership> findByCollaboratorRoleCode(String code);
    List<EnterpriseMembership> findByCollaboratorRoleId(Long id);
    int deleteByCollaboratorRoleId(Long id);
    int deleteByCollaboratorRoleCode(String code);
    long countByCollaboratorRoleCode(String code);




	EnterpriseMembership create(EnterpriseMembership t);

    EnterpriseMembership update(EnterpriseMembership t);

    List<EnterpriseMembership> update(List<EnterpriseMembership> ts,boolean createIfNotExist);

    EnterpriseMembership findById(Long id);

    EnterpriseMembership findOrSave(EnterpriseMembership t);

    EnterpriseMembership findByReferenceEntity(EnterpriseMembership t);

    EnterpriseMembership findWithAssociatedLists(Long id);

    List<EnterpriseMembership> findAllOptimized();

    List<EnterpriseMembership> findAll();

    List<EnterpriseMembership> findByCriteria(EnterpriseMembershipCriteria criteria);

    List<EnterpriseMembership> findPaginatedByCriteria(EnterpriseMembershipCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(EnterpriseMembershipCriteria criteria);

    List<EnterpriseMembership> delete(List<EnterpriseMembership> ts);

    boolean deleteById(Long id);

    List<List<EnterpriseMembership>> getToBeSavedAndToBeDeleted(List<EnterpriseMembership> oldList, List<EnterpriseMembership> newList);

}
