package ma.zyn.app.service.facade.admin.task;

import java.util.List;
import ma.zyn.app.bean.core.task.TaskPriority;
import ma.zyn.app.dao.criteria.core.task.TaskPriorityCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface TaskPriorityAdminService {







	TaskPriority create(TaskPriority t);

    TaskPriority update(TaskPriority t);

    List<TaskPriority> update(List<TaskPriority> ts,boolean createIfNotExist);

    TaskPriority findById(Long id);

    TaskPriority findOrSave(TaskPriority t);

    TaskPriority findByReferenceEntity(TaskPriority t);

    TaskPriority findWithAssociatedLists(Long id);

    List<TaskPriority> findAllOptimized();

    List<TaskPriority> findAll();

    List<TaskPriority> findByCriteria(TaskPriorityCriteria criteria);

    List<TaskPriority> findPaginatedByCriteria(TaskPriorityCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(TaskPriorityCriteria criteria);

    List<TaskPriority> delete(List<TaskPriority> ts);

    boolean deleteById(Long id);

    List<List<TaskPriority>> getToBeSavedAndToBeDeleted(List<TaskPriority> oldList, List<TaskPriority> newList);

}
