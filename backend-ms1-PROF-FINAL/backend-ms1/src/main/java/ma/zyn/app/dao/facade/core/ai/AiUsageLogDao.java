package ma.zyn.app.dao.facade.core.ai;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface AiUsageLogDao extends AbstractRepository<AiUsageLog,Long>  {

    List<AiUsageLog> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);
    List<AiUsageLog> findByAiUsageTypeCode(String code);
    List<AiUsageLog> findByAiUsageTypeId(Long id);
    int deleteByAiUsageTypeId(Long id);
    int deleteByAiUsageTypeCode(String code);
    long countByAiUsageTypeCode(String code);
    List<AiUsageLog> findByCollaboratorId(Long id);
    int deleteByCollaboratorId(Long id);
    long countByCollaboratorEmail(String email);
    List<AiUsageLog> findByDocumentId(Long id);
    int deleteByDocumentId(Long id);
    long countByDocumentId(Long id);


}
