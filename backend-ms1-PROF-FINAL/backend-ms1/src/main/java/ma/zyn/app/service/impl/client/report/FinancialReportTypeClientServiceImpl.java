package ma.zyn.app.service.impl.client.report;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.dao.criteria.core.report.FinancialReportTypeCriteria;
import ma.zyn.app.dao.facade.core.report.FinancialReportTypeDao;
import ma.zyn.app.dao.specification.core.report.FinancialReportTypeSpecification;
import ma.zyn.app.service.facade.client.report.FinancialReportTypeClientService;
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


import java.util.List;
@Service
public class FinancialReportTypeClientServiceImpl implements FinancialReportTypeClientService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public FinancialReportType update(FinancialReportType t) {
        FinancialReportType loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{FinancialReportType.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public FinancialReportType findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public FinancialReportType findOrSave(FinancialReportType t) {
        if (t != null) {
            FinancialReportType result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<FinancialReportType> findAll() {
        return dao.findAll();
    }

    public List<FinancialReportType> findByCriteria(FinancialReportTypeCriteria criteria) {
        List<FinancialReportType> content = null;
        if (criteria != null) {
            FinancialReportTypeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private FinancialReportTypeSpecification constructSpecification(FinancialReportTypeCriteria criteria) {
        FinancialReportTypeSpecification mySpecification =  (FinancialReportTypeSpecification) RefelexivityUtil.constructObjectUsingOneParam(FinancialReportTypeSpecification.class, criteria);
        return mySpecification;
    }

    public List<FinancialReportType> findPaginatedByCriteria(FinancialReportTypeCriteria criteria, int page, int pageSize, String order, String sortField) {
        FinancialReportTypeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(FinancialReportTypeCriteria criteria) {
        FinancialReportTypeSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
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
    public List<FinancialReportType> delete(List<FinancialReportType> list) {
		List<FinancialReportType> result = new ArrayList();
        if (list != null) {
            for (FinancialReportType t : list) {
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
    public FinancialReportType create(FinancialReportType t) {
        FinancialReportType loaded = findByReferenceEntity(t);
        FinancialReportType saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public FinancialReportType findWithAssociatedLists(Long id){
        FinancialReportType result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<FinancialReportType> update(List<FinancialReportType> ts, boolean createIfNotExist) {
        List<FinancialReportType> result = new ArrayList<>();
        if (ts != null) {
            for (FinancialReportType t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    FinancialReportType loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, FinancialReportType t, FinancialReportType loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public FinancialReportType findByReferenceEntity(FinancialReportType t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<FinancialReportType> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<FinancialReportType>> getToBeSavedAndToBeDeleted(List<FinancialReportType> oldList, List<FinancialReportType> newList) {
        List<List<FinancialReportType>> result = new ArrayList<>();
        List<FinancialReportType> resultDelete = new ArrayList<>();
        List<FinancialReportType> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<FinancialReportType> oldList, List<FinancialReportType> newList, List<FinancialReportType> resultUpdateOrSave, List<FinancialReportType> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                FinancialReportType myOld = oldList.get(i);
                FinancialReportType t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                FinancialReportType myNew = newList.get(i);
                FinancialReportType t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public FinancialReportTypeClientServiceImpl(FinancialReportTypeDao dao) {
        this.dao = dao;
    }

    private FinancialReportTypeDao dao;
}
