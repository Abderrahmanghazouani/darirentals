package ma.zyn.app.service.facade.client.property;

import java.util.List;
import ma.zyn.app.bean.core.property.Country;
import ma.zyn.app.dao.criteria.core.property.CountryCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface CountryClientService {







	Country create(Country t);

    Country update(Country t);

    List<Country> update(List<Country> ts,boolean createIfNotExist);

    Country findById(Long id);

    Country findOrSave(Country t);

    Country findByReferenceEntity(Country t);

    Country findWithAssociatedLists(Long id);

    List<Country> findAllOptimized();

    List<Country> findAll();

    List<Country> findByCriteria(CountryCriteria criteria);

    List<Country> findPaginatedByCriteria(CountryCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(CountryCriteria criteria);

    List<Country> delete(List<Country> ts);

    boolean deleteById(Long id);

    List<List<Country>> getToBeSavedAndToBeDeleted(List<Country> oldList, List<Country> newList);

}
