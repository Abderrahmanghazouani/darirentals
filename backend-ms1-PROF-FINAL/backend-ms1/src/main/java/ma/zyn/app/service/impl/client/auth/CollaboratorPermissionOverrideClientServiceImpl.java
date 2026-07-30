package ma.zyn.app.service.impl.client.auth;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorPermissionOverrideCriteria;
import ma.zyn.app.dao.facade.core.auth.CollaboratorPermissionOverrideDao;
import ma.zyn.app.dao.specification.core.auth.CollaboratorPermissionOverrideSpecification;
import ma.zyn.app.service.facade.client.auth.CollaboratorPermissionOverrideClientService;
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

import ma.zyn.app.service.facade.client.enterprise.EnterpriseMembershipClientService ;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership ;

import java.util.List;
@Service
public class CollaboratorPermissionOverrideClientServiceImpl implements CollaboratorPermissionOverrideClientService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public CollaboratorPermissionOverride update(CollaboratorPermissionOverride t) {
        CollaboratorPermissionOverride loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{CollaboratorPermissionOverride.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public CollaboratorPermissionOverride findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public CollaboratorPermissionOverride findOrSave(CollaboratorPermissionOverride t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            CollaboratorPermissionOverride result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<CollaboratorPermissionOverride> findAll() {
        return dao.findAll();
    }

    public List<CollaboratorPermissionOverride> findByCriteria(CollaboratorPermissionOverrideCriteria criteria) {
        List<CollaboratorPermissionOverride> content = null;
        if (criteria != null) {
            CollaboratorPermissionOverrideSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private CollaboratorPermissionOverrideSpecification constructSpecification(CollaboratorPermissionOverrideCriteria criteria) {
        CollaboratorPermissionOverrideSpecification mySpecification =  (CollaboratorPermissionOverrideSpecification) RefelexivityUtil.constructObjectUsingOneParam(CollaboratorPermissionOverrideSpecification.class, criteria);
        return mySpecification;
    }

    public List<CollaboratorPermissionOverride> findPaginatedByCriteria(CollaboratorPermissionOverrideCriteria criteria, int page, int pageSize, String order, String sortField) {
        CollaboratorPermissionOverrideSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(CollaboratorPermissionOverrideCriteria criteria) {
        CollaboratorPermissionOverrideSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<CollaboratorPermissionOverride> findByEnterpriseMembershipId(Long id){
        return dao.findByEnterpriseMembershipId(id);
    }
    public int deleteByEnterpriseMembershipId(Long id){
        return dao.deleteByEnterpriseMembershipId(id);
    }
    public long countByEnterpriseMembershipId(Long id){
        return dao.countByEnterpriseMembershipId(id);
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
    public List<CollaboratorPermissionOverride> delete(List<CollaboratorPermissionOverride> list) {
		List<CollaboratorPermissionOverride> result = new ArrayList();
        if (list != null) {
            for (CollaboratorPermissionOverride t : list) {
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
    public CollaboratorPermissionOverride create(CollaboratorPermissionOverride t) {
        CollaboratorPermissionOverride loaded = findByReferenceEntity(t);
        CollaboratorPermissionOverride saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public CollaboratorPermissionOverride findWithAssociatedLists(Long id){
        CollaboratorPermissionOverride result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<CollaboratorPermissionOverride> update(List<CollaboratorPermissionOverride> ts, boolean createIfNotExist) {
        List<CollaboratorPermissionOverride> result = new ArrayList<>();
        if (ts != null) {
            for (CollaboratorPermissionOverride t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    CollaboratorPermissionOverride loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, CollaboratorPermissionOverride t, CollaboratorPermissionOverride loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public CollaboratorPermissionOverride findByReferenceEntity(CollaboratorPermissionOverride t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(CollaboratorPermissionOverride t){
        if( t != null) {
        }
    }



    public List<CollaboratorPermissionOverride> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<CollaboratorPermissionOverride>> getToBeSavedAndToBeDeleted(List<CollaboratorPermissionOverride> oldList, List<CollaboratorPermissionOverride> newList) {
        List<List<CollaboratorPermissionOverride>> result = new ArrayList<>();
        List<CollaboratorPermissionOverride> resultDelete = new ArrayList<>();
        List<CollaboratorPermissionOverride> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<CollaboratorPermissionOverride> oldList, List<CollaboratorPermissionOverride> newList, List<CollaboratorPermissionOverride> resultUpdateOrSave, List<CollaboratorPermissionOverride> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                CollaboratorPermissionOverride myOld = oldList.get(i);
                CollaboratorPermissionOverride t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                CollaboratorPermissionOverride myNew = newList.get(i);
                CollaboratorPermissionOverride t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private EnterpriseMembershipClientService enterpriseMembershipService ;

    public CollaboratorPermissionOverrideClientServiceImpl(CollaboratorPermissionOverrideDao dao) {
        this.dao = dao;
    }

    private CollaboratorPermissionOverrideDao dao;
}
