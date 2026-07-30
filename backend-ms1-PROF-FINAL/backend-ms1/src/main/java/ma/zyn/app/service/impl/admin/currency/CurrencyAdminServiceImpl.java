package ma.zyn.app.service.impl.admin.currency;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.currency.Currency;
import ma.zyn.app.dao.criteria.core.currency.CurrencyCriteria;
import ma.zyn.app.dao.facade.core.currency.CurrencyDao;
import ma.zyn.app.dao.specification.core.currency.CurrencySpecification;
import ma.zyn.app.service.facade.admin.currency.CurrencyAdminService;
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

import ma.zyn.app.service.facade.admin.auth.CollaboratorAdminService ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.service.facade.admin.currency.ExchangeRateAdminService ;
import ma.zyn.app.bean.core.currency.ExchangeRate ;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseAdminService ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;

import java.util.List;
@Service
public class CurrencyAdminServiceImpl implements CurrencyAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Currency update(Currency t) {
        Currency loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Currency.class.getSimpleName(), t.getId().toString()});
        } else {
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    public Currency findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public Currency findOrSave(Currency t) {
        if (t != null) {
            Currency result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Currency> findAll() {
        return dao.findAll();
    }

    public List<Currency> findByCriteria(CurrencyCriteria criteria) {
        List<Currency> content = null;
        if (criteria != null) {
            CurrencySpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private CurrencySpecification constructSpecification(CurrencyCriteria criteria) {
        CurrencySpecification mySpecification =  (CurrencySpecification) RefelexivityUtil.constructObjectUsingOneParam(CurrencySpecification.class, criteria);
        return mySpecification;
    }

    public List<Currency> findPaginatedByCriteria(CurrencyCriteria criteria, int page, int pageSize, String order, String sortField) {
        CurrencySpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(CurrencyCriteria criteria) {
        CurrencySpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            deleteAssociatedLists(id);
            dao.deleteById(id);
        }
        return condition;
    }

    public void deleteAssociatedLists(Long id) {
        exchangeRateService.deleteByBaseCurrencyId(id);
        exchangeRateService.deleteByTargetCurrencyId(id);
        enterpriseService.deleteByCurrencyId(id);
        collaboratorService.deleteByDisplayCurrencyId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Currency> delete(List<Currency> list) {
		List<Currency> result = new ArrayList();
        if (list != null) {
            for (Currency t : list) {
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
    public Currency create(Currency t) {
        Currency loaded = findByReferenceEntity(t);
        Currency saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getExchangeRatesAsBase() != null) {
                t.getExchangeRatesAsBase().forEach(element-> {
                    element.setBaseCurrency(saved);
                    exchangeRateService.create(element);
                });
            }
            if (t.getExchangeRatesAsTarget() != null) {
                t.getExchangeRatesAsTarget().forEach(element-> {
                    element.setTargetCurrency(saved);
                    exchangeRateService.create(element);
                });
            }
            if (t.getEnterprises() != null) {
                t.getEnterprises().forEach(element-> {
                    element.setCurrency(saved);
                    enterpriseService.create(element);
                });
            }
            if (t.getCollaborators() != null) {
                t.getCollaborators().forEach(element-> {
                    element.setDisplayCurrency(saved);
                    collaboratorService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public Currency findWithAssociatedLists(Long id){
        Currency result = dao.findById(id).orElse(null);
        if(result!=null && result.getId() != null) {
            result.setExchangeRatesAsBase(exchangeRateService.findByBaseCurrencyId(id));
            result.setExchangeRatesAsTarget(exchangeRateService.findByTargetCurrencyId(id));
            result.setEnterprises(enterpriseService.findByCurrencyId(id));
            result.setCollaborators(collaboratorService.findByDisplayCurrencyId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Currency> update(List<Currency> ts, boolean createIfNotExist) {
        List<Currency> result = new ArrayList<>();
        if (ts != null) {
            for (Currency t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Currency loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Currency t, Currency loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Currency currency){
    if(currency !=null && currency.getId() != null){
        List<List<ExchangeRate>> resultExchangeRatesAsBase= exchangeRateService.getToBeSavedAndToBeDeleted(exchangeRateService.findByBaseCurrencyId(currency.getId()),currency.getExchangeRatesAsBase());
            exchangeRateService.delete(resultExchangeRatesAsBase.get(1));
        emptyIfNull(resultExchangeRatesAsBase.get(0)).forEach(e -> e.setBaseCurrency(currency));
        exchangeRateService.update(resultExchangeRatesAsBase.get(0),true);
        List<List<ExchangeRate>> resultExchangeRatesAsTarget= exchangeRateService.getToBeSavedAndToBeDeleted(exchangeRateService.findByTargetCurrencyId(currency.getId()),currency.getExchangeRatesAsTarget());
            exchangeRateService.delete(resultExchangeRatesAsTarget.get(1));
        emptyIfNull(resultExchangeRatesAsTarget.get(0)).forEach(e -> e.setTargetCurrency(currency));
        exchangeRateService.update(resultExchangeRatesAsTarget.get(0),true);
        List<List<Enterprise>> resultEnterprises= enterpriseService.getToBeSavedAndToBeDeleted(enterpriseService.findByCurrencyId(currency.getId()),currency.getEnterprises());
            enterpriseService.delete(resultEnterprises.get(1));
        emptyIfNull(resultEnterprises.get(0)).forEach(e -> e.setCurrency(currency));
        enterpriseService.update(resultEnterprises.get(0),true);
        List<List<Collaborator>> resultCollaborators= collaboratorService.getToBeSavedAndToBeDeleted(collaboratorService.findByDisplayCurrencyId(currency.getId()),currency.getCollaborators());
            collaboratorService.delete(resultCollaborators.get(1));
        emptyIfNull(resultCollaborators.get(0)).forEach(e -> e.setDisplayCurrency(currency));
        collaboratorService.update(resultCollaborators.get(0),true);
        }
    }








    public Currency findByReferenceEntity(Currency t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<Currency> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<Currency>> getToBeSavedAndToBeDeleted(List<Currency> oldList, List<Currency> newList) {
        List<List<Currency>> result = new ArrayList<>();
        List<Currency> resultDelete = new ArrayList<>();
        List<Currency> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Currency> oldList, List<Currency> newList, List<Currency> resultUpdateOrSave, List<Currency> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Currency myOld = oldList.get(i);
                Currency t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Currency myNew = newList.get(i);
                Currency t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private CollaboratorAdminService collaboratorService ;
    @Autowired
    private ExchangeRateAdminService exchangeRateService ;
    @Autowired
    private EnterpriseAdminService enterpriseService ;

    public CurrencyAdminServiceImpl(CurrencyDao dao) {
        this.dao = dao;
    }

    private CurrencyDao dao;
}
