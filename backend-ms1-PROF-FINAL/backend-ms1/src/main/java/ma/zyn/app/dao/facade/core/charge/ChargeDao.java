package ma.zyn.app.dao.facade.core.charge;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.charge.Charge;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ChargeDao extends AbstractRepository<Charge,Long>  {

    List<Charge> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);
    List<Charge> findByPropertyIdIn(List<Long> ids);
    List<Charge> findByChargeTypeCode(String code);
    List<Charge> findByChargeTypeId(Long id);
    int deleteByChargeTypeId(Long id);
    int deleteByChargeTypeCode(String code);
    long countByChargeTypeCode(String code);
    List<Charge> findByPaymentId(Long id);
    int deleteByPaymentId(Long id);
    long countByPaymentId(Long id);

    @Query("SELECT NEW Charge(item.id,item.label) FROM Charge item")
    List<Charge> findAllOptimized();

}
