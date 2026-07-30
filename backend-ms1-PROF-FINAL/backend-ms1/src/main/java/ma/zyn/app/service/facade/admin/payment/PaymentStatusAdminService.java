package ma.zyn.app.service.facade.admin.payment;

import java.util.List;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.dao.criteria.core.payment.PaymentStatusCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface PaymentStatusAdminService {







	PaymentStatus create(PaymentStatus t);

    PaymentStatus update(PaymentStatus t);

    List<PaymentStatus> update(List<PaymentStatus> ts,boolean createIfNotExist);

    PaymentStatus findById(Long id);

    PaymentStatus findOrSave(PaymentStatus t);

    PaymentStatus findByReferenceEntity(PaymentStatus t);

    PaymentStatus findWithAssociatedLists(Long id);

    List<PaymentStatus> findAllOptimized();

    List<PaymentStatus> findAll();

    List<PaymentStatus> findByCriteria(PaymentStatusCriteria criteria);

    List<PaymentStatus> findPaginatedByCriteria(PaymentStatusCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(PaymentStatusCriteria criteria);

    List<PaymentStatus> delete(List<PaymentStatus> ts);

    boolean deleteById(Long id);

    List<List<PaymentStatus>> getToBeSavedAndToBeDeleted(List<PaymentStatus> oldList, List<PaymentStatus> newList);

}
