package ma.zyn.app.service.facade.admin.ai;

import java.util.List;
import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.dao.criteria.core.ai.AiQuotaCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface AiQuotaAdminService {



    List<AiQuota> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);




	AiQuota create(AiQuota t);

    AiQuota update(AiQuota t);

    List<AiQuota> update(List<AiQuota> ts,boolean createIfNotExist);

    AiQuota findById(Long id);

    AiQuota findOrSave(AiQuota t);

    AiQuota findByReferenceEntity(AiQuota t);

    AiQuota findWithAssociatedLists(Long id);

    List<AiQuota> findAllOptimized();

    List<AiQuota> findAll();

    List<AiQuota> findByCriteria(AiQuotaCriteria criteria);

    List<AiQuota> findPaginatedByCriteria(AiQuotaCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(AiQuotaCriteria criteria);

    List<AiQuota> delete(List<AiQuota> ts);

    boolean deleteById(Long id);

    List<List<AiQuota>> getToBeSavedAndToBeDeleted(List<AiQuota> oldList, List<AiQuota> newList);

}
