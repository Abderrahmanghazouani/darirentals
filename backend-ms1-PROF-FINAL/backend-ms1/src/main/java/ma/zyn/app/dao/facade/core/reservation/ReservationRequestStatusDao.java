package ma.zyn.app.dao.facade.core.reservation;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import java.util.List;


@Repository
public interface ReservationRequestStatusDao extends AbstractRepository<ReservationRequestStatus,Long>  {
    ReservationRequestStatus findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW ReservationRequestStatus(item.id,item.label) FROM ReservationRequestStatus item")
    List<ReservationRequestStatus> findAllOptimized();

}
