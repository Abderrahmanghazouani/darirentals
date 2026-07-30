package ma.zyn.app.dao.facade.core.reservation;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import java.util.List;


@Repository
public interface ReservationStatusDao extends AbstractRepository<ReservationStatus,Long>  {
    ReservationStatus findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW ReservationStatus(item.id,item.label) FROM ReservationStatus item")
    List<ReservationStatus> findAllOptimized();

}
