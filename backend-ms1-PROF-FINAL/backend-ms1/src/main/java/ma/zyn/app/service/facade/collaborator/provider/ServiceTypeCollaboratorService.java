package ma.zyn.app.service.facade.collaborator.provider;

import java.util.List;
import ma.zyn.app.bean.core.provider.ServiceType;
import ma.zyn.app.dao.criteria.core.provider.ServiceTypeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ServiceTypeCollaboratorService {







	ServiceType create(ServiceType t);

    ServiceType update(ServiceType t);

    List<ServiceType> update(List<ServiceType> ts,boolean createIfNotExist);

    ServiceType findById(Long id);

    ServiceType findOrSave(ServiceType t);

    ServiceType findByReferenceEntity(ServiceType t);

    ServiceType findWithAssociatedLists(Long id);

    List<ServiceType> findAllOptimized();

    List<ServiceType> findAll();

    List<ServiceType> findByCriteria(ServiceTypeCriteria criteria);

    List<ServiceType> findPaginatedByCriteria(ServiceTypeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ServiceTypeCriteria criteria);

    List<ServiceType> delete(List<ServiceType> ts);

    boolean deleteById(Long id);

    List<List<ServiceType>> getToBeSavedAndToBeDeleted(List<ServiceType> oldList, List<ServiceType> newList);

}
