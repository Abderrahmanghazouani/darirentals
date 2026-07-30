package ma.zyn.app.service.facade.client.provider;

import java.util.List;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.dao.criteria.core.provider.ServiceProviderCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ServiceProviderClientService {



    List<ServiceProvider> findByServiceTypeCode(String code);
    List<ServiceProvider> findByServiceTypeId(Long id);
    int deleteByServiceTypeId(Long id);
    int deleteByServiceTypeCode(String code);
    long countByServiceTypeCode(String code);
    List<ServiceProvider> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);




	ServiceProvider create(ServiceProvider t);

    ServiceProvider update(ServiceProvider t);

    List<ServiceProvider> update(List<ServiceProvider> ts,boolean createIfNotExist);

    ServiceProvider findById(Long id);

    ServiceProvider findOrSave(ServiceProvider t);

    ServiceProvider findByReferenceEntity(ServiceProvider t);

    ServiceProvider findWithAssociatedLists(Long id);

    List<ServiceProvider> findAllOptimized();

    List<ServiceProvider> findAll();

    List<ServiceProvider> findByCriteria(ServiceProviderCriteria criteria);

    List<ServiceProvider> findPaginatedByCriteria(ServiceProviderCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ServiceProviderCriteria criteria);

    List<ServiceProvider> delete(List<ServiceProvider> ts);

    boolean deleteById(Long id);

    List<List<ServiceProvider>> getToBeSavedAndToBeDeleted(List<ServiceProvider> oldList, List<ServiceProvider> newList);

}
