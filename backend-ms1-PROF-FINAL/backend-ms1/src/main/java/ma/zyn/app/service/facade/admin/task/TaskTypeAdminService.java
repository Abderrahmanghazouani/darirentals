package ma.zyn.app.service.facade.admin.task;

import java.util.List;
import ma.zyn.app.bean.core.task.TaskType;
import ma.zyn.app.dao.criteria.core.task.TaskTypeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface TaskTypeAdminService {







	TaskType create(TaskType t);

    TaskType update(TaskType t);

    List<TaskType> update(List<TaskType> ts,boolean createIfNotExist);

    TaskType findById(Long id);

    TaskType findOrSave(TaskType t);

    TaskType findByReferenceEntity(TaskType t);

    TaskType findWithAssociatedLists(Long id);

    List<TaskType> findAllOptimized();

    List<TaskType> findAll();

    List<TaskType> findByCriteria(TaskTypeCriteria criteria);

    List<TaskType> findPaginatedByCriteria(TaskTypeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(TaskTypeCriteria criteria);

    List<TaskType> delete(List<TaskType> ts);

    boolean deleteById(Long id);

    List<List<TaskType>> getToBeSavedAndToBeDeleted(List<TaskType> oldList, List<TaskType> newList);

}
