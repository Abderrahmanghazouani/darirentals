package ma.zyn.app.service.impl.admin.report;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.dao.criteria.core.report.FinancialReportPropertyCriteria;
import ma.zyn.app.dao.facade.core.report.FinancialReportPropertyDao;
import ma.zyn.app.dao.specification.core.report.FinancialReportPropertySpecification;
import ma.zyn.app.service.facade.admin.report.FinancialReportPropertyAdminService;
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

import ma.zyn.app.service.facade.admin.report.FinancialReportAdminService ;
import ma.zyn.app.bean.core.report.FinancialReport ;
import ma.zyn.app.service.facade.admin.property.PropertyAdminService ;
import ma.zyn.app.bean.core.property.Property ;

import java.util.List;
@Service
public class FinancialReportPropertyAdminServiceImpl implements FinancialReportPropertyAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public FinancialReportProperty update(FinancialReportProperty t) {
        FinancialReportProperty loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{FinancialReportProperty.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public FinancialReportProperty findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public FinancialReportProperty findOrSave(FinancialReportProperty t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            FinancialReportProperty result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<FinancialReportProperty> findAll() {
        return dao.findAll();
    }

    public List<FinancialReportProperty> findByCriteria(FinancialReportPropertyCriteria criteria) {
        List<FinancialReportProperty> content = null;
        if (criteria != null) {
            FinancialReportPropertySpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private FinancialReportPropertySpecification constructSpecification(FinancialReportPropertyCriteria criteria) {
        FinancialReportPropertySpecification mySpecification =  (FinancialReportPropertySpecification) RefelexivityUtil.constructObjectUsingOneParam(FinancialReportPropertySpecification.class, criteria);
        return mySpecification;
    }

    public List<FinancialReportProperty> findPaginatedByCriteria(FinancialReportPropertyCriteria criteria, int page, int pageSize, String order, String sortField) {
        FinancialReportPropertySpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(FinancialReportPropertyCriteria criteria) {
        FinancialReportPropertySpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<FinancialReportProperty> findByFinancialReportId(Long id){
        return dao.findByFinancialReportId(id);
    }
    public int deleteByFinancialReportId(Long id){
        return dao.deleteByFinancialReportId(id);
    }
    public long countByFinancialReportId(Long id){
        return dao.countByFinancialReportId(id);
    }
    public List<FinancialReportProperty> findByPropertyId(Long id){
        return dao.findByPropertyId(id);
    }
    public int deleteByPropertyId(Long id){
        return dao.deleteByPropertyId(id);
    }
    public long countByPropertyId(Long id){
        return dao.countByPropertyId(id);
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
    public List<FinancialReportProperty> delete(List<FinancialReportProperty> list) {
		List<FinancialReportProperty> result = new ArrayList();
        if (list != null) {
            for (FinancialReportProperty t : list) {
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
    public FinancialReportProperty create(FinancialReportProperty t) {
        FinancialReportProperty loaded = findByReferenceEntity(t);
        FinancialReportProperty saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public FinancialReportProperty findWithAssociatedLists(Long id){
        FinancialReportProperty result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<FinancialReportProperty> update(List<FinancialReportProperty> ts, boolean createIfNotExist) {
        List<FinancialReportProperty> result = new ArrayList<>();
        if (ts != null) {
            for (FinancialReportProperty t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    FinancialReportProperty loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, FinancialReportProperty t, FinancialReportProperty loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public FinancialReportProperty findByReferenceEntity(FinancialReportProperty t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(FinancialReportProperty t){
        if( t != null) {
            t.setFinancialReport(financialReportService.findOrSave(t.getFinancialReport()));
            t.setProperty(propertyService.findOrSave(t.getProperty()));
        }
    }



    public List<FinancialReportProperty> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<FinancialReportProperty>> getToBeSavedAndToBeDeleted(List<FinancialReportProperty> oldList, List<FinancialReportProperty> newList) {
        List<List<FinancialReportProperty>> result = new ArrayList<>();
        List<FinancialReportProperty> resultDelete = new ArrayList<>();
        List<FinancialReportProperty> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<FinancialReportProperty> oldList, List<FinancialReportProperty> newList, List<FinancialReportProperty> resultUpdateOrSave, List<FinancialReportProperty> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                FinancialReportProperty myOld = oldList.get(i);
                FinancialReportProperty t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                FinancialReportProperty myNew = newList.get(i);
                FinancialReportProperty t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private FinancialReportAdminService financialReportService ;
    @Autowired
    private PropertyAdminService propertyService ;

    public FinancialReportPropertyAdminServiceImpl(FinancialReportPropertyDao dao) {
        this.dao = dao;
    }

    private FinancialReportPropertyDao dao;
}
