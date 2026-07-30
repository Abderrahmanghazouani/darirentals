package ma.zyn.app.service.impl.collaborator.reservation;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.reservation.Reservation;
import ma.zyn.app.dao.criteria.core.reservation.ReservationCriteria;
import ma.zyn.app.dao.facade.core.reservation.ReservationDao;
import ma.zyn.app.dao.specification.core.reservation.ReservationSpecification;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationCollaboratorService;
import ma.zyn.app.zynerator.service.AbstractServiceImpl;
import static ma.zyn.app.zynerator.util.ListUtil.*;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import ma.zyn.app.zynerator.util.RefelexivityUtil;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ma.zyn.app.service.facade.collaborator.task.TaskCollaboratorService ;
import ma.zyn.app.bean.core.task.Task ;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationRequestCollaboratorService ;
import ma.zyn.app.bean.core.reservation.ReservationRequest ;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationPlatformCollaboratorService ;
import ma.zyn.app.bean.core.reservation.ReservationPlatform ;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationStatusCollaboratorService ;
import ma.zyn.app.bean.core.reservation.ReservationStatus ;
import ma.zyn.app.service.facade.collaborator.property.PropertyCollaboratorService ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.service.facade.collaborator.client.ClientCollaboratorService ;
import ma.zyn.app.bean.core.client.Client ;
import ma.zyn.app.service.facade.collaborator.document.DocumentCollaboratorService ;
import ma.zyn.app.bean.core.document.Document ;

import java.util.List;
@Service
public class ReservationCollaboratorServiceImpl implements ReservationCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Reservation update(Reservation t) {
        Reservation loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Reservation.class.getSimpleName(), t.getId().toString()});
        } else {
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    public Reservation findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public Reservation findOrSave(Reservation t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Reservation result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Reservation> findAll() {
        return dao.findAll();
    }

    public List<Reservation> findByCriteria(ReservationCriteria criteria) {
        List<Reservation> content = null;
        if (criteria != null) {
            ReservationSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ReservationSpecification constructSpecification(ReservationCriteria criteria) {
        ReservationSpecification mySpecification =  (ReservationSpecification) RefelexivityUtil.constructObjectUsingOneParam(ReservationSpecification.class, criteria);
        return mySpecification;
    }

    public List<Reservation> findPaginatedByCriteria(ReservationCriteria criteria, int page, int pageSize, String order, String sortField) {
        ReservationSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ReservationCriteria criteria) {
        ReservationSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<Reservation> findByClientId(Long id){
        return dao.findByClientId(id);
    }
    public int deleteByClientId(Long id){
        return dao.deleteByClientId(id);
    }
    public long countByClientEmail(String email){
        return dao.countByClientEmail(email);
    }
    public List<Reservation> findByPropertyId(Long id){
        return dao.findByPropertyId(id);
    }
    public int deleteByPropertyId(Long id){
        return dao.deleteByPropertyId(id);
    }
    public long countByPropertyId(Long id){
        return dao.countByPropertyId(id);
    }
    public List<Reservation> findByReservationPlatformCode(String code){
        return dao.findByReservationPlatformCode(code);
    }
    public List<Reservation> findByReservationPlatformId(Long id){
        return dao.findByReservationPlatformId(id);
    }
    public int deleteByReservationPlatformCode(String code){
        return dao.deleteByReservationPlatformCode(code);
    }
    public int deleteByReservationPlatformId(Long id){
        return dao.deleteByReservationPlatformId(id);
    }
    public long countByReservationPlatformCode(String code){
        return dao.countByReservationPlatformCode(code);
    }
    public List<Reservation> findByReservationStatusCode(String code){
        return dao.findByReservationStatusCode(code);
    }
    public List<Reservation> findByReservationStatusId(Long id){
        return dao.findByReservationStatusId(id);
    }
    public int deleteByReservationStatusCode(String code){
        return dao.deleteByReservationStatusCode(code);
    }
    public int deleteByReservationStatusId(Long id){
        return dao.deleteByReservationStatusId(id);
    }
    public long countByReservationStatusCode(String code){
        return dao.countByReservationStatusCode(code);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            deleteAssociatedLists(id);
            dao.deleteById(id);
        }
        return condition;
    }

    public void deleteAssociatedLists(Long id) {
        documentService.deleteByReservationId(id);
        taskService.deleteByReservationId(id);
        reservationRequestService.deleteByReservationId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Reservation> delete(List<Reservation> list) {
		List<Reservation> result = new ArrayList();
        if (list != null) {
            for (Reservation t : list) {
                if(dao.findById(t.getId()).isEmpty()){
					result.add(t);
				}else{
                    dao.deleteById(t.getId());
                }
            }
        }
		return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Reservation create(Reservation t) {
        Reservation loaded = findByReferenceEntity(t);
        Reservation saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getDocuments() != null) {
                t.getDocuments().forEach(element-> {
                    element.setReservation(saved);
                    documentService.create(element);
                });
            }
            if (t.getTasks() != null) {
                t.getTasks().forEach(element-> {
                    element.setReservation(saved);
                    taskService.create(element);
                });
            }
            if (t.getReservationRequests() != null) {
                t.getReservationRequests().forEach(element-> {
                    element.setReservation(saved);
                    reservationRequestService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public Reservation findWithAssociatedLists(Long id){
        Reservation result = dao.findById(id).orElse(null);
        if(result!=null && result.getId() != null) {
            result.setDocuments(documentService.findByReservationId(id));
            result.setTasks(taskService.findByReservationId(id));
            result.setReservationRequests(reservationRequestService.findByReservationId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Reservation> update(List<Reservation> ts, boolean createIfNotExist) {
        List<Reservation> result = new ArrayList<>();
        if (ts != null) {
            for (Reservation t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Reservation loadedItem = dao.findById(t.getId()).orElse(null);
                    if (isEligibleForCreateOrUpdate(createIfNotExist, t, loadedItem)) {
                        dao.save(t);
                    } else {
                        result.add(t);
                    }
                }
            }
        }
        return result;
    }


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Reservation t, Reservation loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Reservation reservation){
    if(reservation !=null && reservation.getId() != null){
        List<List<Document>> resultDocuments= documentService.getToBeSavedAndToBeDeleted(documentService.findByReservationId(reservation.getId()),reservation.getDocuments());
            documentService.delete(resultDocuments.get(1));
        emptyIfNull(resultDocuments.get(0)).forEach(e -> e.setReservation(reservation));
        documentService.update(resultDocuments.get(0),true);
        List<List<Task>> resultTasks= taskService.getToBeSavedAndToBeDeleted(taskService.findByReservationId(reservation.getId()),reservation.getTasks());
            taskService.delete(resultTasks.get(1));
        emptyIfNull(resultTasks.get(0)).forEach(e -> e.setReservation(reservation));
        taskService.update(resultTasks.get(0),true);
        List<List<ReservationRequest>> resultReservationRequests= reservationRequestService.getToBeSavedAndToBeDeleted(reservationRequestService.findByReservationId(reservation.getId()),reservation.getReservationRequests());
            reservationRequestService.delete(resultReservationRequests.get(1));
        emptyIfNull(resultReservationRequests.get(0)).forEach(e -> e.setReservation(reservation));
        reservationRequestService.update(resultReservationRequests.get(0),true);
        }
    }








    public Reservation findByReferenceEntity(Reservation t){
        return t==null? null : dao.findByReference(t.getReference());
    }
    public void findOrSaveAssociatedObject(Reservation t){
        if( t != null) {
            t.setClient(clientService.findOrSave(t.getClient()));
            t.setProperty(propertyService.findOrSave(t.getProperty()));
            t.setReservationPlatform(reservationPlatformService.findOrSave(t.getReservationPlatform()));
            t.setReservationStatus(reservationStatusService.findOrSave(t.getReservationStatus()));
        }
    }



    public List<Reservation> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<Reservation>> getToBeSavedAndToBeDeleted(List<Reservation> oldList, List<Reservation> newList) {
        List<List<Reservation>> result = new ArrayList<>();
        List<Reservation> resultDelete = new ArrayList<>();
        List<Reservation> resultUpdateOrSave = new ArrayList<>();
        if (isEmpty(oldList) && isNotEmpty(newList)) {
            resultUpdateOrSave.addAll(newList);
        } else if (isEmpty(newList) && isNotEmpty(oldList)) {
            resultDelete.addAll(oldList);
        } else if (isNotEmpty(newList) && isNotEmpty(oldList)) {
			extractToBeSaveOrDelete(oldList, newList, resultUpdateOrSave, resultDelete);
        }
        result.add(resultUpdateOrSave);
        result.add(resultDelete);
        return result;
    }

    private void extractToBeSaveOrDelete(List<Reservation> oldList, List<Reservation> newList, List<Reservation> resultUpdateOrSave, List<Reservation> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Reservation myOld = oldList.get(i);
                Reservation t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Reservation myNew = newList.get(i);
                Reservation t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private TaskCollaboratorService taskService ;
    @Autowired
    private ReservationRequestCollaboratorService reservationRequestService ;
    @Autowired
    private ReservationPlatformCollaboratorService reservationPlatformService ;
    @Autowired
    private ReservationStatusCollaboratorService reservationStatusService ;
    @Autowired
    private PropertyCollaboratorService propertyService ;
    @Autowired
    private ClientCollaboratorService clientService ;
    @Autowired
    private DocumentCollaboratorService documentService ;

    public ReservationCollaboratorServiceImpl(ReservationDao dao) {
        this.dao = dao;
    }

    private ReservationDao dao;
}
