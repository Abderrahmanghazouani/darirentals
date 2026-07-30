package ma.zyn.app.service.impl.admin.report;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.dao.criteria.core.report.FinancialReportScopeCriteria;
import ma.zyn.app.dao.facade.core.report.FinancialReportScopeDao;
import ma.zyn.app.dao.specification.core.report.FinancialReportScopeSpecification;
import ma.zyn.app.service.facade.admin.report.FinancialReportScopeAdminService;
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
public class FinancialReportScopeAdminServiceImpl implements FinancialReportScopeAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public FinancialReportScope update(FinancialReportScope t) {
        FinancialReportScope loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{FinancialReportScope.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public FinancialReportScope findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public FinancialReportScope findOrSave(FinancialReportScope t) {
        if (t != null) {
            FinancialReportScope result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<FinancialReportScope> findAll() {
        return dao.findAll();
    }

    public List<FinancialReportScope> findByCriteria(FinancialReportScopeCriteria criteria) {
        List<FinancialReportScope> content = null;
        if (criteria != null) {
            FinancialReportScopeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private FinancialReportScopeSpecification constructSpecification(FinancialReportScopeCriteria criteria) {
        FinancialReportScopeSpecification mySpecification =  (FinancialReportScopeSpecification) RefelexivityUtil.constructObjectUsingOneParam(FinancialReportScopeSpecification.class, criteria);
        return mySpecification;
    }

    public List<FinancialReportScope> findPaginatedByCriteria(FinancialReportScopeCriteria criteria, int page, int pageSize, String order, String sortField) {
        FinancialReportScopeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(FinancialReportScopeCriteria criteria) {
        FinancialReportScopeSpecification mySpecification = constructSpecification(criteria);
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
    public List<FinancialReportScope> delete(List<FinancialReportScope> list) {
		List<FinancialReportScope> result = new ArrayList();
        if (list != null) {
            for (FinancialReportScope t : list) {
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
    public FinancialReportScope create(FinancialReportScope t) {
        FinancialReportScope loaded = findByReferenceEntity(t);
        FinancialReportScope saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public FinancialReportScope findWithAssociatedLists(Long id){
        FinancialReportScope result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<FinancialReportScope> update(List<FinancialReportScope> ts, boolean createIfNotExist) {
        List<FinancialReportScope> result = new ArrayList<>();
        if (ts != null) {
            for (FinancialReportScope t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    FinancialReportScope loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, FinancialReportScope t, FinancialReportScope loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public FinancialReportScope findByReferenceEntity(FinancialReportScope t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<FinancialReportScope> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<FinancialReportScope>> getToBeSavedAndToBeDeleted(List<FinancialReportScope> oldList, List<FinancialReportScope> newList) {
        List<List<FinancialReportScope>> result = new ArrayList<>();
        List<FinancialReportScope> resultDelete = new ArrayList<>();
        List<FinancialReportScope> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<FinancialReportScope> oldList, List<FinancialReportScope> newList, List<FinancialReportScope> resultUpdateOrSave, List<FinancialReportScope> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                FinancialReportScope myOld = oldList.get(i);
                FinancialReportScope t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                FinancialReportScope myNew = newList.get(i);
                FinancialReportScope t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public FinancialReportScopeAdminServiceImpl(FinancialReportScopeDao dao) {
        this.dao = dao;
    }

    private FinancialReportScopeDao dao;
}
