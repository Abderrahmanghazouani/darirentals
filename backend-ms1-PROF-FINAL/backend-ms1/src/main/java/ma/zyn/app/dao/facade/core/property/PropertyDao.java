package ma.zyn.app.dao.facade.core.property;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.property.Property;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface PropertyDao extends AbstractRepository<Property,Long>  {

    List<Property> findByPropertyTypeCode(String code);
    List<Property> findByPropertyTypeId(Long id);
    int deleteByPropertyTypeId(Long id);
    int deleteByPropertyTypeCode(String code);
    long countByPropertyTypeCode(String code);
    List<Property> findByPropertyStatusCode(String code);
    List<Property> findByPropertyStatusId(Long id);
    int deleteByPropertyStatusId(Long id);
    int deleteByPropertyStatusCode(String code);
    long countByPropertyStatusCode(String code);
    List<Property> findByCityId(Long id);
    int deleteByCityId(Long id);
    long countByCityId(Long id);
    List<Property> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);

    @Query("SELECT NEW Property(item.id,item.name) FROM Property item")
    List<Property> findAllOptimized();

}
