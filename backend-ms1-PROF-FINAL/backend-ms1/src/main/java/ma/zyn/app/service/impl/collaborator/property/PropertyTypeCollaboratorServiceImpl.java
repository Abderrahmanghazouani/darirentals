package ma.zyn.app.service.impl.collaborator.property;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.property.PropertyType;
import ma.zyn.app.dao.criteria.core.property.PropertyTypeCriteria;
import ma.zyn.app.dao.facade.core.property.PropertyTypeDao;
import ma.zyn.app.dao.specification.core.property.PropertyTypeSpecification;
import ma.zyn.app.service.facade.collaborator.property.PropertyTypeCollaboratorService;
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
public class PropertyTypeCollaboratorServiceImpl implements PropertyTypeCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public PropertyType update(PropertyType t) {
        PropertyType loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{PropertyType.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public PropertyType findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public PropertyType findOrSave(PropertyType t) {
        if (t != null) {
            PropertyType result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<PropertyType> findAll() {
        return dao.findAll();
    }

    public List<PropertyType> findByCriteria(PropertyTypeCriteria criteria) {
        List<PropertyType> content = null;
        if (criteria != null) {
            PropertyTypeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private PropertyTypeSpecification constructSpecification(PropertyTypeCriteria criteria) {
        PropertyTypeSpecification mySpecification =  (PropertyTypeSpecification) RefelexivityUtil.constructObjectUsingOneParam(PropertyTypeSpecification.class, criteria);
        return mySpecification;
    }

    public List<PropertyType> findPaginatedByCriteria(PropertyTypeCriteria criteria, int page, int pageSize, String order, String sortField) {
        PropertyTypeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(PropertyTypeCriteria criteria) {
        PropertyTypeSpecification mySpecification = constructSpecification(criteria);
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
    public List<PropertyType> delete(List<PropertyType> list) {
		List<PropertyType> result = new ArrayList();
        if (list != null) {
            for (PropertyType t : list) {
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
    public PropertyType create(PropertyType t) {
        PropertyType loaded = findByReferenceEntity(t);
        PropertyType saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public PropertyType findWithAssociatedLists(Long id){
        PropertyType result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<PropertyType> update(List<PropertyType> ts, boolean createIfNotExist) {
        List<PropertyType> result = new ArrayList<>();
        if (ts != null) {
            for (PropertyType t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    PropertyType loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, PropertyType t, PropertyType loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public PropertyType findByReferenceEntity(PropertyType t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<PropertyType> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<PropertyType>> getToBeSavedAndToBeDeleted(List<PropertyType> oldList, List<PropertyType> newList) {
        List<List<PropertyType>> result = new ArrayList<>();
        List<PropertyType> resultDelete = new ArrayList<>();
        List<PropertyType> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<PropertyType> oldList, List<PropertyType> newList, List<PropertyType> resultUpdateOrSave, List<PropertyType> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                PropertyType myOld = oldList.get(i);
                PropertyType t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                PropertyType myNew = newList.get(i);
                PropertyType t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public PropertyTypeCollaboratorServiceImpl(PropertyTypeDao dao) {
        this.dao = dao;
    }

    private PropertyTypeDao dao;
}
