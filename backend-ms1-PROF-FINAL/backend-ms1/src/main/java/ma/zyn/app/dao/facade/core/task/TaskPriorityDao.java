package ma.zyn.app.dao.facade.core.task;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.task.TaskPriority;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.task.TaskPriority;
import java.util.List;


@Repository
public interface TaskPriorityDao extends AbstractRepository<TaskPriority,Long>  {
    TaskPriority findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW TaskPriority(item.id,item.label) FROM TaskPriority item")
    List<TaskPriority> findAllOptimized();

}
