package ma.zyn.app.service.impl.admin.auth;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.auth.CollaboratorPropertyAccess;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorPropertyAccessCriteria;
import ma.zyn.app.dao.facade.core.auth.CollaboratorPropertyAccessDao;
import ma.zyn.app.dao.specification.core.auth.CollaboratorPropertyAccessSpecification;
import ma.zyn.app.service.facade.admin.auth.CollaboratorPropertyAccessAdminService;
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
import ma.zyn.app.service.facade.admin.property.PropertyAdminService ;
import ma.zyn.app.bean.core.property.Property ;

@Service
public class CollaboratorPropertyAccessAdminServiceImpl implements CollaboratorPropertyAccessAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public CollaboratorPropertyAccess update(CollaboratorPropertyAccess t) {
        CollaboratorPropertyAccess loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{CollaboratorPropertyAccess.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public CollaboratorPropertyAccess findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public CollaboratorPropertyAccess findOrSave(CollaboratorPropertyAccess t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            CollaboratorPropertyAccess result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<CollaboratorPropertyAccess> findAll() {
        return dao.findAll();
    }

    public List<CollaboratorPropertyAccess> findByCriteria(CollaboratorPropertyAccessCriteria criteria) {
        List<CollaboratorPropertyAccess> content = null;
        if (criteria != null) {
            CollaboratorPropertyAccessSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private CollaboratorPropertyAccessSpecification constructSpecification(CollaboratorPropertyAccessCriteria criteria) {
        CollaboratorPropertyAccessSpecification mySpecification =  (CollaboratorPropertyAccessSpecification) RefelexivityUtil.constructObjectUsingOneParam(CollaboratorPropertyAccessSpecification.class, criteria);
        return mySpecification;
    }

    public List<CollaboratorPropertyAccess> findPaginatedByCriteria(CollaboratorPropertyAccessCriteria criteria, int page, int pageSize, String order, String sortField) {
        CollaboratorPropertyAccessSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(CollaboratorPropertyAccessCriteria criteria) {
        CollaboratorPropertyAccessSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<CollaboratorPropertyAccess> findByCollaboratorId(Long id){
        return dao.findByCollaboratorId(id);
    }
    public int deleteByCollaboratorId(Long id){
        return dao.deleteByCollaboratorId(id);
    }
    public long countByCollaboratorId(Long id){
        return dao.countByCollaboratorId(id);
    }
    public List<CollaboratorPropertyAccess> findByPropertyId(Long id){
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
    public List<CollaboratorPropertyAccess> delete(List<CollaboratorPropertyAccess> list) {
		List<CollaboratorPropertyAccess> result = new ArrayList();
        if (list != null) {
            for (CollaboratorPropertyAccess t : list) {
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
    public CollaboratorPropertyAccess create(CollaboratorPropertyAccess t) {
        CollaboratorPropertyAccess loaded = findByReferenceEntity(t);
        CollaboratorPropertyAccess saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public CollaboratorPropertyAccess findWithAssociatedLists(Long id){
        return dao.findById(id).orElse(null);
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<CollaboratorPropertyAccess> update(List<CollaboratorPropertyAccess> ts, boolean createIfNotExist) {
        List<CollaboratorPropertyAccess> result = new ArrayList<>();
        if (ts != null) {
            for (CollaboratorPropertyAccess t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    CollaboratorPropertyAccess loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, CollaboratorPropertyAccess t, CollaboratorPropertyAccess loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }


    public CollaboratorPropertyAccess findByReferenceEntity(CollaboratorPropertyAccess t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(CollaboratorPropertyAccess t){
        if( t != null) {
            t.setCollaborator(collaboratorService.findOrSave(t.getCollaborator()));
            t.setProperty(propertyService.findOrSave(t.getProperty()));
        }
    }



    public List<CollaboratorPropertyAccess> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<CollaboratorPropertyAccess>> getToBeSavedAndToBeDeleted(List<CollaboratorPropertyAccess> oldList, List<CollaboratorPropertyAccess> newList) {
        List<List<CollaboratorPropertyAccess>> result = new ArrayList<>();
        List<CollaboratorPropertyAccess> resultDelete = new ArrayList<>();
        List<CollaboratorPropertyAccess> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<CollaboratorPropertyAccess> oldList, List<CollaboratorPropertyAccess> newList, List<CollaboratorPropertyAccess> resultUpdateOrSave, List<CollaboratorPropertyAccess> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                CollaboratorPropertyAccess myOld = oldList.get(i);
                CollaboratorPropertyAccess t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                CollaboratorPropertyAccess myNew = newList.get(i);
                CollaboratorPropertyAccess t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}


    @Autowired
    private CollaboratorAdminService collaboratorService ;
    @Autowired
    private PropertyAdminService propertyService ;

    public CollaboratorPropertyAccessAdminServiceImpl(CollaboratorPropertyAccessDao dao) {
        this.dao = dao;
    }

    private CollaboratorPropertyAccessDao dao;
}
