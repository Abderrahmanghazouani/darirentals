package ma.zyn.app.service.facade.client.currency;

import java.util.List;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.dao.criteria.core.currency.CurrencyCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface CurrencyClientService {







	Currency create(Currency t);

    Currency update(Currency t);

    List<Currency> update(List<Currency> ts,boolean createIfNotExist);

    Currency findById(Long id);

    Currency findOrSave(Currency t);

    Currency findByReferenceEntity(Currency t);

    Currency findWithAssociatedLists(Long id);

    List<Currency> findAllOptimized();

    List<Currency> findAll();

    List<Currency> findByCriteria(CurrencyCriteria criteria);

    List<Currency> findPaginatedByCriteria(CurrencyCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(CurrencyCriteria criteria);

    List<Currency> delete(List<Currency> ts);

    boolean deleteById(Long id);

    List<List<Currency>> getToBeSavedAndToBeDeleted(List<Currency> oldList, List<Currency> newList);

}
