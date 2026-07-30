package ma.zyn.app.service.facade.admin.charge;

import java.util.List;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.dao.criteria.core.charge.ChargeTypeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ChargeTypeAdminService {







	ChargeType create(ChargeType t);

    ChargeType update(ChargeType t);

    List<ChargeType> update(List<ChargeType> ts,boolean createIfNotExist);

    ChargeType findById(Long id);

    ChargeType findOrSave(ChargeType t);

    ChargeType findByReferenceEntity(ChargeType t);

    ChargeType findWithAssociatedLists(Long id);

    List<ChargeType> findAllOptimized();

    List<ChargeType> findAll();

    List<ChargeType> findByCriteria(ChargeTypeCriteria criteria);

    List<ChargeType> findPaginatedByCriteria(ChargeTypeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ChargeTypeCriteria criteria);

    List<ChargeType> delete(List<ChargeType> ts);

    boolean deleteById(Long id);

    List<List<ChargeType>> getToBeSavedAndToBeDeleted(List<ChargeType> oldList, List<ChargeType> newList);

}
