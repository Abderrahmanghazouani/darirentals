package ma.zyn.app.service.facade.client.charge;

import java.util.List;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.dao.criteria.core.charge.ChargeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ChargeClientService {



    List<Charge> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);
    List<Charge> findByChargeTypeCode(String code);
    List<Charge> findByChargeTypeId(Long id);
    int deleteByChargeTypeId(Long id);
    int deleteByChargeTypeCode(String code);
    long countByChargeTypeCode(String code);
    List<Charge> findByPaymentId(Long id);
    int deleteByPaymentId(Long id);
    long countByPaymentId(Long id);




	Charge create(Charge t);

    Charge update(Charge t);

    List<Charge> update(List<Charge> ts,boolean createIfNotExist);

    Charge findById(Long id);

    Charge findOrSave(Charge t);

    Charge findByReferenceEntity(Charge t);

    Charge findWithAssociatedLists(Long id);

    List<Charge> findAllOptimized();

    List<Charge> findAll();

    List<Charge> findByCriteria(ChargeCriteria criteria);

    List<Charge> findPaginatedByCriteria(ChargeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ChargeCriteria criteria);

    List<Charge> delete(List<Charge> ts);

    boolean deleteById(Long id);

    List<List<Charge>> getToBeSavedAndToBeDeleted(List<Charge> oldList, List<Charge> newList);

}
