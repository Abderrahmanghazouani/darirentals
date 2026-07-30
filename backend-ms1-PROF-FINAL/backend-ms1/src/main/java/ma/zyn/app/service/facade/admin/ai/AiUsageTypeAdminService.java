package ma.zyn.app.service.facade.admin.ai;

import java.util.List;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.dao.criteria.core.ai.AiUsageTypeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface AiUsageTypeAdminService {







	AiUsageType create(AiUsageType t);

    AiUsageType update(AiUsageType t);

    List<AiUsageType> update(List<AiUsageType> ts,boolean createIfNotExist);

    AiUsageType findById(Long id);

    AiUsageType findOrSave(AiUsageType t);

    AiUsageType findByReferenceEntity(AiUsageType t);

    AiUsageType findWithAssociatedLists(Long id);

    List<AiUsageType> findAllOptimized();

    List<AiUsageType> findAll();

    List<AiUsageType> findByCriteria(AiUsageTypeCriteria criteria);

    List<AiUsageType> findPaginatedByCriteria(AiUsageTypeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(AiUsageTypeCriteria criteria);

    List<AiUsageType> delete(List<AiUsageType> ts);

    boolean deleteById(Long id);

    List<List<AiUsageType>> getToBeSavedAndToBeDeleted(List<AiUsageType> oldList, List<AiUsageType> newList);

}
