package ma.zyn.app.service.impl.collaborator.task;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.dao.criteria.core.task.TaskTypeCriteria;
import ma.zyn.app.dao.facade.core.task.TaskTypeDao;
import ma.zyn.app.dao.specification.core.task.TaskTypeSpecification;
import ma.zyn.app.service.facade.collaborator.task.TaskTypeCollaboratorService;
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
public class TaskTypeCollaboratorServiceImpl implements TaskTypeCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public TaskType update(TaskType t) {
        TaskType loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{TaskType.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public TaskType findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public TaskType findOrSave(TaskType t) {
        if (t != null) {
            TaskType result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<TaskType> findAll() {
        return dao.findAll();
    }

    public List<TaskType> findByCriteria(TaskTypeCriteria criteria) {
        List<TaskType> content = null;
        if (criteria != null) {
            TaskTypeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private TaskTypeSpecification constructSpecification(TaskTypeCriteria criteria) {
        TaskTypeSpecification mySpecification =  (TaskTypeSpecification) RefelexivityUtil.constructObjectUsingOneParam(TaskTypeSpecification.class, criteria);
        return mySpecification;
    }

    public List<TaskType> findPaginatedByCriteria(TaskTypeCriteria criteria, int page, int pageSize, String order, String sortField) {
        TaskTypeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(TaskTypeCriteria criteria) {
        TaskTypeSpecification mySpecification = constructSpecification(criteria);
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
    public List<TaskType> delete(List<TaskType> list) {
		List<TaskType> result = new ArrayList();
        if (list != null) {
            for (TaskType t : list) {
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
    public TaskType create(TaskType t) {
        TaskType loaded = findByReferenceEntity(t);
        TaskType saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public TaskType findWithAssociatedLists(Long id){
        TaskType result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<TaskType> update(List<TaskType> ts, boolean createIfNotExist) {
        List<TaskType> result = new ArrayList<>();
        if (ts != null) {
            for (TaskType t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    TaskType loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, TaskType t, TaskType loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public TaskType findByReferenceEntity(TaskType t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<TaskType> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<TaskType>> getToBeSavedAndToBeDeleted(List<TaskType> oldList, List<TaskType> newList) {
        List<List<TaskType>> result = new ArrayList<>();
        List<TaskType> resultDelete = new ArrayList<>();
        List<TaskType> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<TaskType> oldList, List<TaskType> newList, List<TaskType> resultUpdateOrSave, List<TaskType> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                TaskType myOld = oldList.get(i);
                TaskType t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                TaskType myNew = newList.get(i);
                TaskType t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public TaskTypeCollaboratorServiceImpl(TaskTypeDao dao) {
        this.dao = dao;
    }

    private TaskTypeDao dao;
}
