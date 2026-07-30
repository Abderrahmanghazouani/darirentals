package ma.zyn.app.dao.facade.core.ai;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.ai.AiUsageType;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.ai.AiUsageType;
import java.util.List;


@Repository
public interface AiUsageTypeDao extends AbstractRepository<AiUsageType,Long>  {
    AiUsageType findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW AiUsageType(item.id,item.label) FROM AiUsageType item")
    List<AiUsageType> findAllOptimized();

}
