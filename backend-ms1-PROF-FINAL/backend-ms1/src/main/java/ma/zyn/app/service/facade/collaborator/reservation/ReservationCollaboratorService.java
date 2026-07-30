package ma.zyn.app.service.facade.collaborator.reservation;

import java.util.List;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.dao.criteria.core.reservation.ReservationCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ReservationCollaboratorService {



    List<Reservation> findByClientId(Long id);
    int deleteByClientId(Long id);
    long countByClientEmail(String email);
    List<Reservation> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);
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




	Reservation create(Reservation t);

    Reservation update(Reservation t);

    List<Reservation> update(List<Reservation> ts,boolean createIfNotExist);

    Reservation findById(Long id);

    Reservation findOrSave(Reservation t);

    Reservation findByReferenceEntity(Reservation t);

    Reservation findWithAssociatedLists(Long id);

    List<Reservation> findAllOptimized();

    List<Reservation> findAll();

    List<Reservation> findByCriteria(ReservationCriteria criteria);

    List<Reservation> findPaginatedByCriteria(ReservationCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ReservationCriteria criteria);

    List<Reservation> delete(List<Reservation> ts);

    boolean deleteById(Long id);

    List<List<Reservation>> getToBeSavedAndToBeDeleted(List<Reservation> oldList, List<Reservation> newList);

}
