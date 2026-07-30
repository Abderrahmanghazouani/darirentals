package ma.zyn.app.service.facade.client.property;

import java.util.List;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface PropertyClientService {



    List<Property> findByPropertyTypeCode(String code);
    List<Property> findByPropertyTypeId(Long id);
    int deleteByPropertyTypeId(Long id);
    int deleteByPropertyTypeCode(String code);
    long countByPropertyTypeCode(String code);
    List<Property> findByPropertyStatusCode(String code);
    List<Property> findByPropertyStatusId(Long id);
    int deleteByPropertyStatusId(Long id);
    int deleteByPropertyStatusCode(String code);
    long countByPropertyStatusCode(String code);
    List<Property> findByCityId(Long id);
    int deleteByCityId(Long id);
    long countByCityId(Long id);
    List<Property> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);




	Property create(Property t);

    Property update(Property t);

    List<Property> update(List<Property> ts,boolean createIfNotExist);

    Property findById(Long id);

    Property findOrSave(Property t);

    Property findByReferenceEntity(Property t);

    Property findWithAssociatedLists(Long id);

    List<Property> findAllOptimized();

    List<Property> findAll();

    List<Property> findByCriteria(PropertyCriteria criteria);

    List<Property> findPaginatedByCriteria(PropertyCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(PropertyCriteria criteria);

    List<Property> delete(List<Property> ts);

    boolean deleteById(Long id);

    List<List<Property>> getToBeSavedAndToBeDeleted(List<Property> oldList, List<Property> newList);

}
