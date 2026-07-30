package ma.zyn.app.dao.facade.core.payment;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.payment.PaymentType;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.payment.PaymentType;
import java.util.List;


@Repository
public interface PaymentTypeDao extends AbstractRepository<PaymentType,Long>  {
    PaymentType findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW PaymentType(item.id,item.label) FROM PaymentType item")
    List<PaymentType> findAllOptimized();

}
