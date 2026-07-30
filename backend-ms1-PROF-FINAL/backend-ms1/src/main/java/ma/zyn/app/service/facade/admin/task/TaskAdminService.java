package ma.zyn.app.service.facade.admin.task;

import java.util.List;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.dao.criteria.core.task.TaskCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface TaskAdminService {



    List<Task> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);
    List<Task> findByReservationId(Long id);
    int deleteByReservationId(Long id);
    long countByReservationReference(String reference);
    List<Task> findByServiceProviderId(Long id);
    int deleteByServiceProviderId(Long id);
    long countByServiceProviderId(Long id);
    List<Task> findByAssignedToId(Long id);
    int deleteByAssignedToId(Long id);
    long countByAssignedToEmail(String email);
    List<Task> findByTaskTypeCode(String code);
    List<Task> findByTaskTypeId(Long id);
    int deleteByTaskTypeId(Long id);
    int deleteByTaskTypeCode(String code);
    long countByTaskTypeCode(String code);
    List<Task> findByTaskPriorityCode(String code);
    List<Task> findByTaskPriorityId(Long id);
    int deleteByTaskPriorityId(Long id);
    int deleteByTaskPriorityCode(String code);
    long countByTaskPriorityCode(String code);
    List<Task> findByTaskStatusCode(String code);
    List<Task> findByTaskStatusId(Long id);
    int deleteByTaskStatusId(Long id);
    int deleteByTaskStatusCode(String code);
    long countByTaskStatusCode(String code);




	Task create(Task t);

    Task update(Task t);

    List<Task> update(List<Task> ts,boolean createIfNotExist);

    Task findById(Long id);

    Task findOrSave(Task t);

    Task findByReferenceEntity(Task t);

    Task findWithAssociatedLists(Long id);

    List<Task> findAllOptimized();

    List<Task> findAll();

    List<Task> findByCriteria(TaskCriteria criteria);

    List<Task> findPaginatedByCriteria(TaskCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(TaskCriteria criteria);

    List<Task> delete(List<Task> ts);

    boolean deleteById(Long id);

    List<List<Task>> getToBeSavedAndToBeDeleted(List<Task> oldList, List<Task> newList);

}
