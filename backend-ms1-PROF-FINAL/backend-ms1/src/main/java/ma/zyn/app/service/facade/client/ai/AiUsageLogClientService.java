package ma.zyn.app.service.facade.client.ai;

import java.util.List;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.dao.criteria.core.ai.AiUsageLogCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface AiUsageLogClientService {



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




	AiUsageLog create(AiUsageLog t);

    AiUsageLog update(AiUsageLog t);

    List<AiUsageLog> update(List<AiUsageLog> ts,boolean createIfNotExist);

    AiUsageLog findById(Long id);

    AiUsageLog findOrSave(AiUsageLog t);

    AiUsageLog findByReferenceEntity(AiUsageLog t);

    AiUsageLog findWithAssociatedLists(Long id);

    List<AiUsageLog> findAllOptimized();

    List<AiUsageLog> findAll();

    List<AiUsageLog> findByCriteria(AiUsageLogCriteria criteria);

    List<AiUsageLog> findPaginatedByCriteria(AiUsageLogCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(AiUsageLogCriteria criteria);

    List<AiUsageLog> delete(List<AiUsageLog> ts);

    boolean deleteById(Long id);

    List<List<AiUsageLog>> getToBeSavedAndToBeDeleted(List<AiUsageLog> oldList, List<AiUsageLog> newList);

}
