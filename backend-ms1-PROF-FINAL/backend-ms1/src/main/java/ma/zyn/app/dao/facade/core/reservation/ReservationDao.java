package ma.zyn.app.dao.facade.core.reservation;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.reservation.Reservation;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.reservation.Reservation;
import java.util.List;


@Repository
public interface ReservationDao extends AbstractRepository<Reservation,Long>  {
    Reservation findByReference(String reference);
    int deleteByReference(String reference);

    List<Reservation> findByClientId(Long id);
    int deleteByClientId(Long id);
    long countByClientEmail(String email);
    List<Reservation> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);
    List<Reservation> findByPropertyIdIn(List<Long> ids);
    List<Reservation> findByReservationPlatformCode(String code);
    List<Reservation> findByReservationPlatformId(Long id);
    int deleteByReservationPlatformId(Long id);
    int deleteByReservationPlatformCode(String code);
    long countByReservationPlatformCode(String code);
    List<Reservation> findByReservationStatusCode(String code);
    List<Reservation> findByReservationStatusId(Long id);
    int deleteByReservationStatusId(Long id);
    int deleteByReservationStatusCode(String code);
    long countByReservationStatusCode(String code);

    @Query("SELECT NEW Reservation(item.id,item.reference) FROM Reservation item")
    List<Reservation> findAllOptimized();

}
