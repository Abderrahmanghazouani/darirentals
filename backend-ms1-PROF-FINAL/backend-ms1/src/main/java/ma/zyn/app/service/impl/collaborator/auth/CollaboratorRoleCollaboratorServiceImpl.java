package ma.zyn.app.service.impl.collaborator.auth;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorRoleCriteria;
import ma.zyn.app.dao.facade.core.auth.CollaboratorRoleDao;
import ma.zyn.app.dao.specification.core.auth.CollaboratorRoleSpecification;
import ma.zyn.app.service.facade.collaborator.auth.CollaboratorRoleCollaboratorService;
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
public class CollaboratorRoleCollaboratorServiceImpl implements CollaboratorRoleCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public CollaboratorRole update(CollaboratorRole t) {
        CollaboratorRole loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{CollaboratorRole.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public CollaboratorRole findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public CollaboratorRole findOrSave(CollaboratorRole t) {
        if (t != null) {
            CollaboratorRole result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<CollaboratorRole> findAll() {
        return dao.findAll();
    }

    public List<CollaboratorRole> findByCriteria(CollaboratorRoleCriteria criteria) {
        List<CollaboratorRole> content = null;
        if (criteria != null) {
            CollaboratorRoleSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private CollaboratorRoleSpecification constructSpecification(CollaboratorRoleCriteria criteria) {
        CollaboratorRoleSpecification mySpecification =  (CollaboratorRoleSpecification) RefelexivityUtil.constructObjectUsingOneParam(CollaboratorRoleSpecification.class, criteria);
        return mySpecification;
    }

    public List<CollaboratorRole> findPaginatedByCriteria(CollaboratorRoleCriteria criteria, int page, int pageSize, String order, String sortField) {
        CollaboratorRoleSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(CollaboratorRoleCriteria criteria) {
        CollaboratorRoleSpecification mySpecification = constructSpecification(criteria);
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
    public List<CollaboratorRole> delete(List<CollaboratorRole> list) {
		List<CollaboratorRole> result = new ArrayList();
        if (list != null) {
            for (CollaboratorRole t : list) {
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
    public CollaboratorRole create(CollaboratorRole t) {
        CollaboratorRole loaded = findByReferenceEntity(t);
        CollaboratorRole saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public CollaboratorRole findWithAssociatedLists(Long id){
        CollaboratorRole result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<CollaboratorRole> update(List<CollaboratorRole> ts, boolean createIfNotExist) {
        List<CollaboratorRole> result = new ArrayList<>();
        if (ts != null) {
            for (CollaboratorRole t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    CollaboratorRole loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, CollaboratorRole t, CollaboratorRole loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public CollaboratorRole findByReferenceEntity(CollaboratorRole t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<CollaboratorRole> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<CollaboratorRole>> getToBeSavedAndToBeDeleted(List<CollaboratorRole> oldList, List<CollaboratorRole> newList) {
        List<List<CollaboratorRole>> result = new ArrayList<>();
        List<CollaboratorRole> resultDelete = new ArrayList<>();
        List<CollaboratorRole> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<CollaboratorRole> oldList, List<CollaboratorRole> newList, List<CollaboratorRole> resultUpdateOrSave, List<CollaboratorRole> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                CollaboratorRole myOld = oldList.get(i);
                CollaboratorRole t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                CollaboratorRole myNew = newList.get(i);
                CollaboratorRole t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public CollaboratorRoleCollaboratorServiceImpl(CollaboratorRoleDao dao) {
        this.dao = dao;
    }

    private CollaboratorRoleDao dao;
}
