package ma.zyn.app.dao.facade.core.enterprise;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface EnterpriseDao extends AbstractRepository<Enterprise,Long>  {

    List<Enterprise> findByCurrencyCode(String code);
    List<Enterprise> findByCurrencyId(Long id);
    int deleteByCurrencyId(Long id);
    int deleteByCurrencyCode(String code);
    long countByCurrencyCode(String code);

    @Query("SELECT NEW Enterprise(item.id,item.name) FROM Enterprise item")
    List<Enterprise> findAllOptimized();

}
