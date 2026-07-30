package ma.zyn.app.service.facade.admin.auth;

import java.util.List;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface CollaboratorAdminService {


    Collaborator findByUsername(String username);
    boolean changePassword(String username, String newPassword);

    List<Collaborator> findByDisplayCurrencyCode(String code);
    List<Collaborator> findByDisplayCurrencyId(Long id);
    int deleteByDisplayCurrencyId(Long id);
    int deleteByDisplayCurrencyCode(String code);
    long countByDisplayCurrencyCode(String code);




	Collaborator create(Collaborator t);

    Collaborator update(Collaborator t);

    List<Collaborator> update(List<Collaborator> ts,boolean createIfNotExist);

    Collaborator findById(Long id);

    Collaborator findOrSave(Collaborator t);

    Collaborator findByReferenceEntity(Collaborator t);

    Collaborator findWithAssociatedLists(Long id);

    List<Collaborator> findAllOptimized();

    List<Collaborator> findAll();

    List<Collaborator> findByCriteria(CollaboratorCriteria criteria);

    List<Collaborator> findPaginatedByCriteria(CollaboratorCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(CollaboratorCriteria criteria);

    List<Collaborator> delete(List<Collaborator> ts);

    boolean deleteById(Long id);

    List<List<Collaborator>> getToBeSavedAndToBeDeleted(List<Collaborator> oldList, List<Collaborator> newList);

}
