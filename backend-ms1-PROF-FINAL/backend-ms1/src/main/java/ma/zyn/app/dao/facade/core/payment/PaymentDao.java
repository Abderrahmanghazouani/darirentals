package ma.zyn.app.dao.facade.core.payment;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.payment.Payment;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PaymentDao extends AbstractRepository<Payment,Long>  {

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


}
