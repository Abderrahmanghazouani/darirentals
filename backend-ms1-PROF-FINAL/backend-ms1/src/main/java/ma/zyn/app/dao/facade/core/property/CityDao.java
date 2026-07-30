package ma.zyn.app.dao.facade.core.property;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.property.City;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CityDao extends AbstractRepository<City,Long>  {

    List<City> findByCountryId(Long id);
    int deleteByCountryId(Long id);
    long countByCountryCode(String code);

    @Query("SELECT NEW City(item.id,item.name) FROM City item")
    List<City> findAllOptimized();

}
