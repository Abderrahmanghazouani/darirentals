package ma.zyn.app.service.impl.client.property;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.property.PropertyStatus;
import ma.zyn.app.dao.criteria.core.property.PropertyStatusCriteria;
import ma.zyn.app.dao.facade.core.property.PropertyStatusDao;
import ma.zyn.app.dao.specification.core.property.PropertyStatusSpecification;
import ma.zyn.app.service.facade.client.property.PropertyStatusClientService;
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
public class PropertyStatusClientServiceImpl implements PropertyStatusClientService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public PropertyStatus update(PropertyStatus t) {
        PropertyStatus loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{PropertyStatus.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public PropertyStatus findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public PropertyStatus findOrSave(PropertyStatus t) {
        if (t != null) {
            PropertyStatus result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<PropertyStatus> findAll() {
        return dao.findAll();
    }

    public List<PropertyStatus> findByCriteria(PropertyStatusCriteria criteria) {
        List<PropertyStatus> content = null;
        if (criteria != null) {
            PropertyStatusSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private PropertyStatusSpecification constructSpecification(PropertyStatusCriteria criteria) {
        PropertyStatusSpecification mySpecification =  (PropertyStatusSpecification) RefelexivityUtil.constructObjectUsingOneParam(PropertyStatusSpecification.class, criteria);
        return mySpecification;
    }

    public List<PropertyStatus> findPaginatedByCriteria(PropertyStatusCriteria criteria, int page, int pageSize, String order, String sortField) {
        PropertyStatusSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(PropertyStatusCriteria criteria) {
        PropertyStatusSpecification mySpecification = constructSpecification(criteria);
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
    public List<PropertyStatus> delete(List<PropertyStatus> list) {
		List<PropertyStatus> result = new ArrayList();
        if (list != null) {
            for (PropertyStatus t : list) {
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
    public PropertyStatus create(PropertyStatus t) {
        PropertyStatus loaded = findByReferenceEntity(t);
        PropertyStatus saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public PropertyStatus findWithAssociatedLists(Long id){
        PropertyStatus result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<PropertyStatus> update(List<PropertyStatus> ts, boolean createIfNotExist) {
        List<PropertyStatus> result = new ArrayList<>();
        if (ts != null) {
            for (PropertyStatus t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    PropertyStatus loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, PropertyStatus t, PropertyStatus loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public PropertyStatus findByReferenceEntity(PropertyStatus t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<PropertyStatus> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<PropertyStatus>> getToBeSavedAndToBeDeleted(List<PropertyStatus> oldList, List<PropertyStatus> newList) {
        List<List<PropertyStatus>> result = new ArrayList<>();
        List<PropertyStatus> resultDelete = new ArrayList<>();
        List<PropertyStatus> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<PropertyStatus> oldList, List<PropertyStatus> newList, List<PropertyStatus> resultUpdateOrSave, List<PropertyStatus> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                PropertyStatus myOld = oldList.get(i);
                PropertyStatus t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                PropertyStatus myNew = newList.get(i);
                PropertyStatus t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public PropertyStatusClientServiceImpl(PropertyStatusDao dao) {
        this.dao = dao;
    }

    private PropertyStatusDao dao;
}
