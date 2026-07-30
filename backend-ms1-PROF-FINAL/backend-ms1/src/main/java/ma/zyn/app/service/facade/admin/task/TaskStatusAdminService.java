package ma.zyn.app.service.facade.admin.task;

import java.util.List;
import ma.zyn.app.bean.core.task.TaskStatus;
import ma.zyn.app.dao.criteria.core.task.TaskStatusCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface TaskStatusAdminService {







	TaskStatus create(TaskStatus t);

    TaskStatus update(TaskStatus t);

    List<TaskStatus> update(List<TaskStatus> ts,boolean createIfNotExist);

    TaskStatus findById(Long id);

    TaskStatus findOrSave(TaskStatus t);

    TaskStatus findByReferenceEntity(TaskStatus t);

    TaskStatus findWithAssociatedLists(Long id);

    List<TaskStatus> findAllOptimized();

    List<TaskStatus> findAll();

    List<TaskStatus> findByCriteria(TaskStatusCriteria criteria);

    List<TaskStatus> findPaginatedByCriteria(TaskStatusCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(TaskStatusCriteria criteria);

    List<TaskStatus> delete(List<TaskStatus> ts);

    boolean deleteById(Long id);

    List<List<TaskStatus>> getToBeSavedAndToBeDeleted(List<TaskStatus> oldList, List<TaskStatus> newList);

}
