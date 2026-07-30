package ma.zyn.app.dao.facade.core.property;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.property.PropertyType;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.property.PropertyType;
import java.util.List;


@Repository
public interface PropertyTypeDao extends AbstractRepository<PropertyType,Long>  {
    PropertyType findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW PropertyType(item.id,item.label) FROM PropertyType item")
    List<PropertyType> findAllOptimized();

}
