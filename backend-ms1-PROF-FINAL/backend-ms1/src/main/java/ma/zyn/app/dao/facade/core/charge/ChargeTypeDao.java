package ma.zyn.app.dao.facade.core.charge;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.charge.ChargeType;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.charge.ChargeType;
import java.util.List;


@Repository
public interface ChargeTypeDao extends AbstractRepository<ChargeType,Long>  {
    ChargeType findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW ChargeType(item.id,item.label) FROM ChargeType item")
    List<ChargeType> findAllOptimized();

}
