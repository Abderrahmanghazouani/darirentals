package ma.zyn.app.service.facade.collaborator.payment;

import java.util.List;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.dao.criteria.core.payment.PaymentCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface PaymentCollaboratorService {



    List<Payment> findByServiceProviderId(Long id);
    int deleteByServiceProviderId(Long id);
    long countByServiceProviderId(Long id);
    List<Payment> findByPaymentTypeCode(String code);
    List<Payment> findByPaymentTypeId(Long id);
    int deleteByPaymentTypeId(Long id);
    int deleteByPaymentTypeCode(String code);
    long countByPaymentTypeCode(String code);
    List<Payment> findByPaymentStatusCode(String code);
    List<Payment> findByPaymentStatusId(Long id);
    int deleteByPaymentStatusId(Long id);
    int deleteByPaymentStatusCode(String code);
    long countByPaymentStatusCode(String code);




	Payment create(Payment t);

    Payment update(Payment t);

    List<Payment> update(List<Payment> ts,boolean createIfNotExist);

    Payment findById(Long id);

    Payment findOrSave(Payment t);

    Payment findByReferenceEntity(Payment t);

    Payment findWithAssociatedLists(Long id);

    List<Payment> findAllOptimized();

    List<Payment> findAll();

    List<Payment> findByCriteria(PaymentCriteria criteria);

    List<Payment> findPaginatedByCriteria(PaymentCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(PaymentCriteria criteria);

    List<Payment> delete(List<Payment> ts);

    boolean deleteById(Long id);

    List<List<Payment>> getToBeSavedAndToBeDeleted(List<Payment> oldList, List<Payment> newList);

}
