package ma.zyn.app.service.impl.collaborator.currency;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import ma.zyn.app.dao.criteria.core.currency.ExchangeRateCriteria;
import ma.zyn.app.dao.facade.core.currency.ExchangeRateDao;
import ma.zyn.app.dao.specification.core.currency.ExchangeRateSpecification;
import ma.zyn.app.service.facade.collaborator.currency.ExchangeRateCollaboratorService;
import ma.zyn.app.zynerator.service.AbstractServiceImpl;
import static ma.zyn.app.zynerator.util.ListUtil.*;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ma.zyn.app.zynerator.util.RefelexivityUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ma.zyn.app.service.facade.collaborator.currency.CurrencyCollaboratorService ;
import ma.zyn.app.bean.core.currency.Currency ;

import java.util.List;
@Service
public class ExchangeRateCollaboratorServiceImpl implements ExchangeRateCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ExchangeRate update(ExchangeRate t) {
        ExchangeRate loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{ExchangeRate.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public ExchangeRate findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public ExchangeRate findOrSave(ExchangeRate t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            ExchangeRate result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<ExchangeRate> findAll() {
        return dao.findAll();
    }

    public List<ExchangeRate> findByCriteria(ExchangeRateCriteria criteria) {
        List<ExchangeRate> content = null;
        if (criteria != null) {
            ExchangeRateSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ExchangeRateSpecification constructSpecification(ExchangeRateCriteria criteria) {
        ExchangeRateSpecification mySpecification =  (ExchangeRateSpecification) RefelexivityUtil.constructObjectUsingOneParam(ExchangeRateSpecification.class, criteria);
        return mySpecification;
    }

    public List<ExchangeRate> findPaginatedByCriteria(ExchangeRateCriteria criteria, int page, int pageSize, String order, String sortField) {
        ExchangeRateSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ExchangeRateCriteria criteria) {
        ExchangeRateSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<ExchangeRate> findByBaseCurrencyCode(String code){
        return dao.findByBaseCurrencyCode(code);
    }
    public List<ExchangeRate> findByBaseCurrencyId(Long id){
        return dao.findByBaseCurrencyId(id);
    }
    public int deleteByBaseCurrencyCode(String code){
        return dao.deleteByBaseCurrencyCode(code);
    }
    public int deleteByBaseCurrencyId(Long id){
        return dao.deleteByBaseCurrencyId(id);
    }
    public long countByBaseCurrencyCode(String code){
        return dao.countByBaseCurrencyCode(code);
    }
    public List<ExchangeRate> findByTargetCurrencyCode(String code){
        return dao.findByTargetCurrencyCode(code);
    }
    public List<ExchangeRate> findByTargetCurrencyId(Long id){
        return dao.findByTargetCurrencyId(id);
    }
    public int deleteByTargetCurrencyCode(String code){
        return dao.deleteByTargetCurrencyCode(code);
    }
    public int deleteByTargetCurrencyId(Long id){
        return dao.deleteByTargetCurrencyId(id);
    }
    public long countByTargetCurrencyCode(String code){
        return dao.countByTargetCurrencyCode(code);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            dao.deleteById(id);
        }
        return condition;
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ExchangeRate> delete(List<ExchangeRate> list) {
		List<ExchangeRate> result = new ArrayList();
        if (list != null) {
            for (ExchangeRate t : list) {
                if(dao.findById(t.getId()).isEmpty()){
					result.add(t);
				}else{
                    dao.deleteById(t.getId());
                }
            }
        }
		return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ExchangeRate create(ExchangeRate t) {
        ExchangeRate loaded = findByReferenceEntity(t);
        ExchangeRate saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public ExchangeRate findWithAssociatedLists(Long id){
        ExchangeRate result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ExchangeRate> update(List<ExchangeRate> ts, boolean createIfNotExist) {
        List<ExchangeRate> result = new ArrayList<>();
        if (ts != null) {
            for (ExchangeRate t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    ExchangeRate loadedItem = dao.findById(t.getId()).orElse(null);
                    if (isEligibleForCreateOrUpdate(createIfNotExist, t, loadedItem)) {
                        dao.save(t);
                    } else {
                        result.add(t);
                    }
                }
            }
        }
        return result;
    }


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, ExchangeRate t, ExchangeRate loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public ExchangeRate findByReferenceEntity(ExchangeRate t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(ExchangeRate t){
        if( t != null) {
            t.setBaseCurrency(currencyService.findOrSave(t.getBaseCurrency()));
            t.setTargetCurrency(currencyService.findOrSave(t.getTargetCurrency()));
        }
    }



    public List<ExchangeRate> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<ExchangeRate>> getToBeSavedAndToBeDeleted(List<ExchangeRate> oldList, List<ExchangeRate> newList) {
        List<List<ExchangeRate>> result = new ArrayList<>();
        List<ExchangeRate> resultDelete = new ArrayList<>();
        List<ExchangeRate> resultUpdateOrSave = new ArrayList<>();
        if (isEmpty(oldList) && isNotEmpty(newList)) {
            resultUpdateOrSave.addAll(newList);
        } else if (isEmpty(newList) && isNotEmpty(oldList)) {
            resultDelete.addAll(oldList);
        } else if (isNotEmpty(newList) && isNotEmpty(oldList)) {
			extractToBeSaveOrDelete(oldList, newList, resultUpdateOrSave, resultDelete);
        }
        result.add(resultUpdateOrSave);
        result.add(resultDelete);
        return result;
    }

    private void extractToBeSaveOrDelete(List<ExchangeRate> oldList, List<ExchangeRate> newList, List<ExchangeRate> resultUpdateOrSave, List<ExchangeRate> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                ExchangeRate myOld = oldList.get(i);
                ExchangeRate t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                ExchangeRate myNew = newList.get(i);
                ExchangeRate t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private CurrencyCollaboratorService currencyService ;

    public ExchangeRateCollaboratorServiceImpl(ExchangeRateDao dao) {
        this.dao = dao;
    }

    private ExchangeRateDao dao;
}
