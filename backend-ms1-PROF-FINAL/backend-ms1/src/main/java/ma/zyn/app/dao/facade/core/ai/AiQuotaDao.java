package ma.zyn.app.dao.facade.core.ai;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.ai.AiQuota;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AiQuotaDao extends AbstractRepository<AiQuota,Long>  {

    List<AiQuota> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);


}
