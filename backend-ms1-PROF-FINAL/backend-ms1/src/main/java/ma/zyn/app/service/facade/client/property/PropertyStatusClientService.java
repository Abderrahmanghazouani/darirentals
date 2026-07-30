package ma.zyn.app.service.facade.client.property;

import java.util.List;
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.dao.criteria.core.property.PropertyStatusCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface PropertyStatusClientService {







	PropertyStatus create(PropertyStatus t);

    PropertyStatus update(PropertyStatus t);

    List<PropertyStatus> update(List<PropertyStatus> ts,boolean createIfNotExist);

    PropertyStatus findById(Long id);

    PropertyStatus findOrSave(PropertyStatus t);

    PropertyStatus findByReferenceEntity(PropertyStatus t);

    PropertyStatus findWithAssociatedLists(Long id);

    List<PropertyStatus> findAllOptimized();

    List<PropertyStatus> findAll();

    List<PropertyStatus> findByCriteria(PropertyStatusCriteria criteria);

    List<PropertyStatus> findPaginatedByCriteria(PropertyStatusCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(PropertyStatusCriteria criteria);

    List<PropertyStatus> delete(List<PropertyStatus> ts);

    boolean deleteById(Long id);

    List<List<PropertyStatus>> getToBeSavedAndToBeDeleted(List<PropertyStatus> oldList, List<PropertyStatus> newList);

}
