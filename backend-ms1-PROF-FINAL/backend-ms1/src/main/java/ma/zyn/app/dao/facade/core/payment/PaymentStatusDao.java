package ma.zyn.app.dao.facade.core.payment;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import java.util.List;


@Repository
public interface PaymentStatusDao extends AbstractRepository<PaymentStatus,Long>  {
    PaymentStatus findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW PaymentStatus(item.id,item.label) FROM PaymentStatus item")
    List<PaymentStatus> findAllOptimized();

}
