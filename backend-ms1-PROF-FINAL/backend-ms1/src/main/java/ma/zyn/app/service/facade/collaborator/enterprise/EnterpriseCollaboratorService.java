package ma.zyn.app.service.facade.collaborator.enterprise;

import java.util.List;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface EnterpriseCollaboratorService {



    List<Enterprise> findByCurrencyCode(String code);
    List<Enterprise> findByCurrencyId(Long id);
    int deleteByCurrencyId(Long id);
    int deleteByCurrencyCode(String code);
    long countByCurrencyCode(String code);




	Enterprise create(Enterprise t);

    Enterprise update(Enterprise t);

    List<Enterprise> update(List<Enterprise> ts,boolean createIfNotExist);

    Enterprise findById(Long id);

    Enterprise findOrSave(Enterprise t);

    Enterprise findByReferenceEntity(Enterprise t);

    Enterprise findWithAssociatedLists(Long id);

    List<Enterprise> findAllOptimized();

    List<Enterprise> findAll();

    List<Enterprise> findByCriteria(EnterpriseCriteria criteria);

    List<Enterprise> findPaginatedByCriteria(EnterpriseCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(EnterpriseCriteria criteria);

    List<Enterprise> delete(List<Enterprise> ts);

    boolean deleteById(Long id);

    List<List<Enterprise>> getToBeSavedAndToBeDeleted(List<Enterprise> oldList, List<Enterprise> newList);

}
