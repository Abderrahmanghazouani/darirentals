package ma.zyn.app.service.facade.client.reservation;

import java.util.List;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.dao.criteria.core.reservation.ReservationPlatformCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface ReservationPlatformClientService {







	ReservationPlatform create(ReservationPlatform t);

    ReservationPlatform update(ReservationPlatform t);

    List<ReservationPlatform> update(List<ReservationPlatform> ts,boolean createIfNotExist);

    ReservationPlatform findById(Long id);

    ReservationPlatform findOrSave(ReservationPlatform t);

    ReservationPlatform findByReferenceEntity(ReservationPlatform t);

    ReservationPlatform findWithAssociatedLists(Long id);

    List<ReservationPlatform> findAllOptimized();

    List<ReservationPlatform> findAll();

    List<ReservationPlatform> findByCriteria(ReservationPlatformCriteria criteria);

    List<ReservationPlatform> findPaginatedByCriteria(ReservationPlatformCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(ReservationPlatformCriteria criteria);

    List<ReservationPlatform> delete(List<ReservationPlatform> ts);

    boolean deleteById(Long id);

    List<List<ReservationPlatform>> getToBeSavedAndToBeDeleted(List<ReservationPlatform> oldList, List<ReservationPlatform> newList);

}
