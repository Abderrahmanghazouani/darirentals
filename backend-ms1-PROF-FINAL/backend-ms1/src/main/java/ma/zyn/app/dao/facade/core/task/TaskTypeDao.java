package ma.zyn.app.dao.facade.core.task;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.task.TaskType;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.task.TaskType;
import java.util.List;


@Repository
public interface TaskTypeDao extends AbstractRepository<TaskType,Long>  {
    TaskType findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW TaskType(item.id,item.label) FROM TaskType item")
    List<TaskType> findAllOptimized();

}
