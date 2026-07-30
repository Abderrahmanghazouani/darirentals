package ma.zyn.app.service.impl.admin.task;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.dao.criteria.core.task.TaskPriorityCriteria;
import ma.zyn.app.dao.facade.core.task.TaskPriorityDao;
import ma.zyn.app.dao.specification.core.task.TaskPrioritySpecification;
import ma.zyn.app.service.facade.admin.task.TaskPriorityAdminService;
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
public class TaskPriorityAdminServiceImpl implements TaskPriorityAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public TaskPriority update(TaskPriority t) {
        TaskPriority loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{TaskPriority.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public TaskPriority findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public TaskPriority findOrSave(TaskPriority t) {
        if (t != null) {
            TaskPriority result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<TaskPriority> findAll() {
        return dao.findAll();
    }

    public List<TaskPriority> findByCriteria(TaskPriorityCriteria criteria) {
        List<TaskPriority> content = null;
        if (criteria != null) {
            TaskPrioritySpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private TaskPrioritySpecification constructSpecification(TaskPriorityCriteria criteria) {
        TaskPrioritySpecification mySpecification =  (TaskPrioritySpecification) RefelexivityUtil.constructObjectUsingOneParam(TaskPrioritySpecification.class, criteria);
        return mySpecification;
    }

    public List<TaskPriority> findPaginatedByCriteria(TaskPriorityCriteria criteria, int page, int pageSize, String order, String sortField) {
        TaskPrioritySpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(TaskPriorityCriteria criteria) {
        TaskPrioritySpecification mySpecification = constructSpecification(criteria);
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
    public List<TaskPriority> delete(List<TaskPriority> list) {
		List<TaskPriority> result = new ArrayList();
        if (list != null) {
            for (TaskPriority t : list) {
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
    public TaskPriority create(TaskPriority t) {
        TaskPriority loaded = findByReferenceEntity(t);
        TaskPriority saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public TaskPriority findWithAssociatedLists(Long id){
        TaskPriority result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<TaskPriority> update(List<TaskPriority> ts, boolean createIfNotExist) {
        List<TaskPriority> result = new ArrayList<>();
        if (ts != null) {
            for (TaskPriority t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    TaskPriority loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, TaskPriority t, TaskPriority loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public TaskPriority findByReferenceEntity(TaskPriority t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<TaskPriority> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<TaskPriority>> getToBeSavedAndToBeDeleted(List<TaskPriority> oldList, List<TaskPriority> newList) {
        List<List<TaskPriority>> result = new ArrayList<>();
        List<TaskPriority> resultDelete = new ArrayList<>();
        List<TaskPriority> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<TaskPriority> oldList, List<TaskPriority> newList, List<TaskPriority> resultUpdateOrSave, List<TaskPriority> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                TaskPriority myOld = oldList.get(i);
                TaskPriority t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                TaskPriority myNew = newList.get(i);
                TaskPriority t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public TaskPriorityAdminServiceImpl(TaskPriorityDao dao) {
        this.dao = dao;
    }

    private TaskPriorityDao dao;
}
