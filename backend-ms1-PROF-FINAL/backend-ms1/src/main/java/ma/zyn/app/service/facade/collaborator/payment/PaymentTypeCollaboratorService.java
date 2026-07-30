package ma.zyn.app.service.facade.collaborator.payment;

import java.util.List;
import ma.zyn.app.bean.core.payment.PaymentType;
import ma.zyn.app.dao.criteria.core.payment.PaymentTypeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface PaymentTypeCollaboratorService {







	PaymentType create(PaymentType t);

    PaymentType update(PaymentType t);

    List<PaymentType> update(List<PaymentType> ts,boolean createIfNotExist);

    PaymentType findById(Long id);

    PaymentType findOrSave(PaymentType t);

    PaymentType findByReferenceEntity(PaymentType t);

    PaymentType findWithAssociatedLists(Long id);

    List<PaymentType> findAllOptimized();

    List<PaymentType> findAll();

    List<PaymentType> findByCriteria(PaymentTypeCriteria criteria);

    List<PaymentType> findPaginatedByCriteria(PaymentTypeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(PaymentTypeCriteria criteria);

    List<PaymentType> delete(List<PaymentType> ts);

    boolean deleteById(Long id);

    List<List<PaymentType>> getToBeSavedAndToBeDeleted(List<PaymentType> oldList, List<PaymentType> newList);

}
