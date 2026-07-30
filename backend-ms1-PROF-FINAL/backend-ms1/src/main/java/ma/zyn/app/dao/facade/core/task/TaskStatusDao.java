package ma.zyn.app.dao.facade.core.task;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.task.TaskStatus;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.task.TaskStatus;
import java.util.List;


@Repository
public interface TaskStatusDao extends AbstractRepository<TaskStatus,Long>  {
    TaskStatus findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW TaskStatus(item.id,item.label) FROM TaskStatus item")
    List<TaskStatus> findAllOptimized();

}
