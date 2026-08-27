package ma.zyn.app.dao.facade.core.task;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.task.Task;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface TaskDao extends AbstractRepository<Task,Long>  {

    List<Task> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);
    List<Task> findByPropertyIdIn(List<Long> ids);
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

    @Query("SELECT NEW Task(item.id,item.title) FROM Task item")
    List<Task> findAllOptimized();

}
