package ma.zyn.app.service.facade.collaborator.currency;

import java.util.List;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.dao.criteria.core.currency.ExchangeRateCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ExchangeRateCollaboratorService {



    List<ExchangeRate> findByBaseCurrencyCode(String code);
    List<ExchangeRate> findByBaseCurrencyId(Long id);
    int deleteByBaseCurrencyId(Long id);
    int deleteByBaseCurrencyCode(String code);
    long countByBaseCurrencyCode(String code);
    List<ExchangeRate> findByTargetCurrencyCode(String code);
    List<ExchangeRate> findByTargetCurrencyId(Long id);
    int deleteByTargetCurrencyId(Long id);
    int deleteByTargetCurrencyCode(String code);
    long countByTargetCurrencyCode(String code);




	ExchangeRate create(ExchangeRate t);

    ExchangeRate update(ExchangeRate t);

    List<ExchangeRate> update(List<ExchangeRate> ts,boolean createIfNotExist);

    ExchangeRate findById(Long id);

    ExchangeRate findOrSave(ExchangeRate t);

    ExchangeRate findByReferenceEntity(ExchangeRate t);

    ExchangeRate findWithAssociatedLists(Long id);

    List<ExchangeRate> findAllOptimized();

    List<ExchangeRate> findAll();

    List<ExchangeRate> findByCriteria(ExchangeRateCriteria criteria);

    List<ExchangeRate> findPaginatedByCriteria(ExchangeRateCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ExchangeRateCriteria criteria);

    List<ExchangeRate> delete(List<ExchangeRate> ts);

    boolean deleteById(Long id);

    List<List<ExchangeRate>> getToBeSavedAndToBeDeleted(List<ExchangeRate> oldList, List<ExchangeRate> newList);

}
