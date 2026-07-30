package ma.zyn.app.service.facade.client.reservation;

import java.util.List;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.dao.criteria.core.reservation.ReservationRequestStatusCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ReservationRequestStatusClientService {







	ReservationRequestStatus create(ReservationRequestStatus t);

    ReservationRequestStatus update(ReservationRequestStatus t);

    List<ReservationRequestStatus> update(List<ReservationRequestStatus> ts,boolean createIfNotExist);

    ReservationRequestStatus findById(Long id);

    ReservationRequestStatus findOrSave(ReservationRequestStatus t);

    ReservationRequestStatus findByReferenceEntity(ReservationRequestStatus t);

    ReservationRequestStatus findWithAssociatedLists(Long id);

    List<ReservationRequestStatus> findAllOptimized();

    List<ReservationRequestStatus> findAll();

    List<ReservationRequestStatus> findByCriteria(ReservationRequestStatusCriteria criteria);

    List<ReservationRequestStatus> findPaginatedByCriteria(ReservationRequestStatusCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ReservationRequestStatusCriteria criteria);

    List<ReservationRequestStatus> delete(List<ReservationRequestStatus> ts);

    boolean deleteById(Long id);

    List<List<ReservationRequestStatus>> getToBeSavedAndToBeDeleted(List<ReservationRequestStatus> oldList, List<ReservationRequestStatus> newList);

}
