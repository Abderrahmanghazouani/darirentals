package ma.zyn.app.dao.facade.core.reservation;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ReservationRequestDao extends AbstractRepository<ReservationRequest,Long>  {

    List<ReservationRequest> findByClientId(Long id);
    int deleteByClientId(Long id);
    long countByClientEmail(String email);
    List<ReservationRequest> findByRequestedPropertyId(Long id);
    int deleteByRequestedPropertyId(Long id);
    long countByRequestedPropertyId(Long id);
    List<ReservationRequest> findByAlternativePropertyId(Long id);
    int deleteByAlternativePropertyId(Long id);
    long countByAlternativePropertyId(Long id);
    List<ReservationRequest> findByReviewedById(Long id);
    int deleteByReviewedById(Long id);
    long countByReviewedByEmail(String email);
    List<ReservationRequest> findByReservationRequestStatusCode(String code);
    List<ReservationRequest> findByReservationRequestStatusId(Long id);
    int deleteByReservationRequestStatusId(Long id);
    int deleteByReservationRequestStatusCode(String code);
    long countByReservationRequestStatusCode(String code);
    List<ReservationRequest> findByReservationId(Long id);
    int deleteByReservationId(Long id);
    long countByReservationReference(String reference);


}
