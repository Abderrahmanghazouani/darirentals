package ma.zyn.app.service.facade.client.reservation;

import java.util.List;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.dao.criteria.core.reservation.ReservationRequestCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ReservationRequestClientService {



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




	ReservationRequest create(ReservationRequest t);

    ReservationRequest update(ReservationRequest t);

    List<ReservationRequest> update(List<ReservationRequest> ts,boolean createIfNotExist);

    ReservationRequest findById(Long id);

    ReservationRequest findOrSave(ReservationRequest t);

    ReservationRequest findByReferenceEntity(ReservationRequest t);

    ReservationRequest findWithAssociatedLists(Long id);

    List<ReservationRequest> findAllOptimized();

    List<ReservationRequest> findAll();

    List<ReservationRequest> findByCriteria(ReservationRequestCriteria criteria);

    List<ReservationRequest> findPaginatedByCriteria(ReservationRequestCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ReservationRequestCriteria criteria);

    List<ReservationRequest> delete(List<ReservationRequest> ts);

    boolean deleteById(Long id);

    List<List<ReservationRequest>> getToBeSavedAndToBeDeleted(List<ReservationRequest> oldList, List<ReservationRequest> newList);

}
