package ma.zyn.app.service.facade.admin.property;

import java.util.List;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.dao.criteria.core.property.PropertyTypeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface PropertyTypeAdminService {







	PropertyType create(PropertyType t);

    PropertyType update(PropertyType t);

    List<PropertyType> update(List<PropertyType> ts,boolean createIfNotExist);

    PropertyType findById(Long id);

    PropertyType findOrSave(PropertyType t);

    PropertyType findByReferenceEntity(PropertyType t);

    PropertyType findWithAssociatedLists(Long id);

    List<PropertyType> findAllOptimized();

    List<PropertyType> findAll();

    List<PropertyType> findByCriteria(PropertyTypeCriteria criteria);

    List<PropertyType> findPaginatedByCriteria(PropertyTypeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(PropertyTypeCriteria criteria);

    List<PropertyType> delete(List<PropertyType> ts);

    boolean deleteById(Long id);

    List<List<PropertyType>> getToBeSavedAndToBeDeleted(List<PropertyType> oldList, List<PropertyType> newList);

}
