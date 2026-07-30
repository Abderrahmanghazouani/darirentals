package ma.zyn.app.dao.facade.core.provider;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.provider.ServiceType;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.provider.ServiceType;
import java.util.List;


@Repository
public interface ServiceTypeDao extends AbstractRepository<ServiceType,Long>  {
    ServiceType findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW ServiceType(item.id,item.label) FROM ServiceType item")
    List<ServiceType> findAllOptimized();

}
