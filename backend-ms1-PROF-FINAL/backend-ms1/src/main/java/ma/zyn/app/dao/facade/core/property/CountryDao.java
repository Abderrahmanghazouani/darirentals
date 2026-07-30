package ma.zyn.app.dao.facade.core.property;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.property.Country;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.property.Country;
import java.util.List;


@Repository
public interface CountryDao extends AbstractRepository<Country,Long>  {
    Country findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW Country(item.id,item.name) FROM Country item")
    List<Country> findAllOptimized();

}
