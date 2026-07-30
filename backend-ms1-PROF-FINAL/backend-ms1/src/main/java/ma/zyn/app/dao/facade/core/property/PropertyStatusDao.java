package ma.zyn.app.dao.facade.core.property;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.property.PropertyStatus;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.property.PropertyStatus;
import java.util.List;


@Repository
public interface PropertyStatusDao extends AbstractRepository<PropertyStatus,Long>  {
    PropertyStatus findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW PropertyStatus(item.id,item.label) FROM PropertyStatus item")
    List<PropertyStatus> findAllOptimized();

}
