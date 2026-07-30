package ma.zyn.app.service.facade.collaborator.reservation;

import java.util.List;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.dao.criteria.core.reservation.ReservationStatusCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ReservationStatusCollaboratorService {







	ReservationStatus create(ReservationStatus t);

    ReservationStatus update(ReservationStatus t);

    List<ReservationStatus> update(List<ReservationStatus> ts,boolean createIfNotExist);

    ReservationStatus findById(Long id);

    ReservationStatus findOrSave(ReservationStatus t);

    ReservationStatus findByReferenceEntity(ReservationStatus t);

    ReservationStatus findWithAssociatedLists(Long id);

    List<ReservationStatus> findAllOptimized();

    List<ReservationStatus> findAll();

    List<ReservationStatus> findByCriteria(ReservationStatusCriteria criteria);

    List<ReservationStatus> findPaginatedByCriteria(ReservationStatusCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ReservationStatusCriteria criteria);

    List<ReservationStatus> delete(List<ReservationStatus> ts);

    boolean deleteById(Long id);

    List<List<ReservationStatus>> getToBeSavedAndToBeDeleted(List<ReservationStatus> oldList, List<ReservationStatus> newList);

}
