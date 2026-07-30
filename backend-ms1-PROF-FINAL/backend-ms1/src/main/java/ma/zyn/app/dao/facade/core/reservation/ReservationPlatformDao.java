package ma.zyn.app.dao.facade.core.reservation;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import java.util.List;


@Repository
public interface ReservationPlatformDao extends AbstractRepository<ReservationPlatform,Long>  {
    ReservationPlatform findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW ReservationPlatform(item.id,item.label) FROM ReservationPlatform item")
    List<ReservationPlatform> findAllOptimized();

}
