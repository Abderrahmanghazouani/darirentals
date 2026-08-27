package ma.zyn.app.dao.facade.core.provider;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ServiceProviderDao extends AbstractRepository<ServiceProvider,Long>  {

    List<ServiceProvider> findByServiceTypeCode(String code);
    List<ServiceProvider> findByServiceTypeId(Long id);
    int deleteByServiceTypeId(Long id);
    int deleteByServiceTypeCode(String code);
    long countByServiceTypeCode(String code);
    List<ServiceProvider> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);
    List<ServiceProvider> findByEnterpriseIdIn(List<Long> ids);

    @Query("SELECT NEW ServiceProvider(item.id,item.name) FROM ServiceProvider item")
    List<ServiceProvider> findAllOptimized();

}
