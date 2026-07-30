package ma.zyn.app.service.impl.admin.task;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.dao.criteria.core.task.TaskStatusCriteria;
import ma.zyn.app.dao.facade.core.task.TaskStatusDao;
import ma.zyn.app.dao.specification.core.task.TaskStatusSpecification;
import ma.zyn.app.service.facade.admin.task.TaskStatusAdminService;
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
public class TaskStatusAdminServiceImpl implements TaskStatusAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public TaskStatus update(TaskStatus t) {
        TaskStatus loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{TaskStatus.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public TaskStatus findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public TaskStatus findOrSave(TaskStatus t) {
        if (t != null) {
            TaskStatus result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<TaskStatus> findAll() {
        return dao.findAll();
    }

    public List<TaskStatus> findByCriteria(TaskStatusCriteria criteria) {
        List<TaskStatus> content = null;
        if (criteria != null) {
            TaskStatusSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private TaskStatusSpecification constructSpecification(TaskStatusCriteria criteria) {
        TaskStatusSpecification mySpecification =  (TaskStatusSpecification) RefelexivityUtil.constructObjectUsingOneParam(TaskStatusSpecification.class, criteria);
        return mySpecification;
    }

    public List<TaskStatus> findPaginatedByCriteria(TaskStatusCriteria criteria, int page, int pageSize, String order, String sortField) {
        TaskStatusSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(TaskStatusCriteria criteria) {
        TaskStatusSpecification mySpecification = constructSpecification(criteria);
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
    public List<TaskStatus> delete(List<TaskStatus> list) {
		List<TaskStatus> result = new ArrayList();
        if (list != null) {
            for (TaskStatus t : list) {
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
    public TaskStatus create(TaskStatus t) {
        TaskStatus loaded = findByReferenceEntity(t);
        TaskStatus saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public TaskStatus findWithAssociatedLists(Long id){
        TaskStatus result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<TaskStatus> update(List<TaskStatus> ts, boolean createIfNotExist) {
        List<TaskStatus> result = new ArrayList<>();
        if (ts != null) {
            for (TaskStatus t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    TaskStatus loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, TaskStatus t, TaskStatus loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public TaskStatus findByReferenceEntity(TaskStatus t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<TaskStatus> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<TaskStatus>> getToBeSavedAndToBeDeleted(List<TaskStatus> oldList, List<TaskStatus> newList) {
        List<List<TaskStatus>> result = new ArrayList<>();
        List<TaskStatus> resultDelete = new ArrayList<>();
        List<TaskStatus> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<TaskStatus> oldList, List<TaskStatus> newList, List<TaskStatus> resultUpdateOrSave, List<TaskStatus> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                TaskStatus myOld = oldList.get(i);
                TaskStatus t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                TaskStatus myNew = newList.get(i);
                TaskStatus t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public TaskStatusAdminServiceImpl(TaskStatusDao dao) {
        this.dao = dao;
    }

    private TaskStatusDao dao;
}
