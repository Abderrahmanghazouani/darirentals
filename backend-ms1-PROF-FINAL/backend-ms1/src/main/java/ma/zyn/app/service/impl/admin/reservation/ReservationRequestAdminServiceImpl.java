package ma.zyn.app.service.impl.admin.reservation;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.reservation.ReservationRequest;
import ma.zyn.app.dao.criteria.core.reservation.ReservationRequestCriteria;
import ma.zyn.app.dao.facade.core.reservation.ReservationRequestDao;
import ma.zyn.app.dao.specification.core.reservation.ReservationRequestSpecification;
import ma.zyn.app.service.facade.admin.reservation.ReservationRequestAdminService;
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

import ma.zyn.app.service.facade.admin.auth.CollaboratorAdminService ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.service.facade.admin.reservation.ReservationAdminService ;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.zynerator.exception.BusinessRuleException;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.service.facade.admin.property.PropertyAdminService ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.service.facade.admin.reservation.ReservationRequestStatusAdminService ;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus ;
import ma.zyn.app.service.facade.admin.client.ClientAdminService ;
import ma.zyn.app.bean.core.client.Client ;

import java.util.List;
@Service
public class ReservationRequestAdminServiceImpl implements ReservationRequestAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ReservationRequest update(ReservationRequest t) {
        ReservationRequest loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{ReservationRequest.class.getSimpleName(), t.getId().toString()});
        } else {
            // Audit final (NOTES-audit-final.md, point 2) : "Confirmer" une demande ne faisait que
            // changer son statut - aucune Reservation n'etait jamais creee. Invariant applique ici :
            // une demande "Confirmee" est toujours rattachee a une Reservation. On la cree donc des
            // qu'on enregistre une demande confirmee qui n'en a pas encore (une seule fois : le
            // lien `reservation` est pose immediatement, donc pas de doublon a la sauvegarde
            // suivante). NB : on ne peut pas comparer "ancien statut / nouveau statut" - avec
            // open-in-view, le controleur et ce service partagent la meme entite geree, dont le
            // statut a deja ete remplace par converter.copy() avant cet appel.
            if (isConfirmed(t.getReservationRequestStatus()) && t.getReservation() == null) {
                t.setReservation(createReservationFromRequest(t));
            }
            dao.save(t);
            return loadedItem;
        }
    }

    private static final java.util.regex.Pattern NOTE_DATES =
            java.util.regex.Pattern.compile("du (\\d{4}-\\d{2}-\\d{2}) au (\\d{4}-\\d{2}-\\d{2})");

    /** Le statut peut n'arriver qu'avec son id (le front envoie {id:1}) : on resout le code en base. */
    private boolean isConfirmed(ReservationRequestStatus status) {
        if (status == null) {
            return false;
        }
        String code = status.getCode();
        if (code == null && status.getId() != null) {
            ReservationRequestStatus full = reservationRequestStatusService.findById(status.getId());
            code = full != null ? full.getCode() : null;
        }
        return "Confirmee".equals(code);
    }

    private Reservation createReservationFromRequest(ReservationRequest request) {
        java.time.LocalDate checkIn = request.getRequestedCheckIn();
        java.time.LocalDate checkOut = request.getRequestedCheckOut();
        if ((checkIn == null || checkOut == null) && request.getClientNote() != null) {
            // Demandes creees avant l'ajout des dates structurees : on les relit dans la note.
            java.util.regex.Matcher m = NOTE_DATES.matcher(request.getClientNote());
            if (m.find()) {
                checkIn = java.time.LocalDate.parse(m.group(1));
                checkOut = java.time.LocalDate.parse(m.group(2));
            }
        }
        if (checkIn == null || checkOut == null) {
            throw new BusinessRuleException("Impossible de confirmer : cette demande n'a pas de dates de séjour exploitables. Créez la réservation manuellement.");
        }
        Property property = request.getRequestedProperty();
        if (property == null || property.getId() == null) {
            throw new BusinessRuleException("Impossible de confirmer : aucune propriété n'est associée à cette demande.");
        }

        Reservation reservation = new Reservation();
        reservation.setReference("RES-" + String.valueOf(System.currentTimeMillis()).substring(5));
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);
        reservation.setClient(request.getClient());
        reservation.setProperty(property);
        java.math.BigDecimal price = property.getPricePerNight() != null ? property.getPricePerNight() : java.math.BigDecimal.ZERO;
        long nights = java.time.temporal.ChronoUnit.DAYS.between(checkIn, checkOut);
        reservation.setPricePerNight(price);
        reservation.setAmount(nights > 0 ? price.multiply(java.math.BigDecimal.valueOf(nights)) : java.math.BigDecimal.ZERO);
        reservation.setReservationStatus(findReservationStatus("Confirmee"));
        // Une demande du portail public est une reservation directe (ni Booking ni Airbnb).
        reservation.setReservationPlatform(findReservationPlatform("Direct"));

        // create() verifie le chevauchement : ReservationOverlapException (-> 409) si la
        // propriete est deja prise sur ces dates, et la confirmation est alors annulee.
        Reservation created = reservationService.create(reservation);
        if (created == null) {
            throw new BusinessRuleException("Impossible de confirmer : la réservation n'a pas pu être créée (référence déjà utilisée).");
        }
        return created;
    }

    private ReservationStatus findReservationStatus(String code) {
        return reservationStatusService.findAll().stream()
                .filter(s -> code.equals(s.getCode())).findFirst().orElse(null);
    }

    private ReservationPlatform findReservationPlatform(String code) {
        return reservationPlatformService.findAll().stream()
                .filter(p -> code.equals(p.getCode())).findFirst().orElse(null);
    }

    public ReservationRequest findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public ReservationRequest findOrSave(ReservationRequest t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            ReservationRequest result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<ReservationRequest> findAll() {
        return dao.findAll();
    }

    public List<ReservationRequest> findByCriteria(ReservationRequestCriteria criteria) {
        List<ReservationRequest> content = null;
        if (criteria != null) {
            ReservationRequestSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ReservationRequestSpecification constructSpecification(ReservationRequestCriteria criteria) {
        ReservationRequestSpecification mySpecification =  (ReservationRequestSpecification) RefelexivityUtil.constructObjectUsingOneParam(ReservationRequestSpecification.class, criteria);
        return mySpecification;
    }

    public List<ReservationRequest> findPaginatedByCriteria(ReservationRequestCriteria criteria, int page, int pageSize, String order, String sortField) {
        ReservationRequestSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ReservationRequestCriteria criteria) {
        ReservationRequestSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<ReservationRequest> findByClientId(Long id){
        return dao.findByClientId(id);
    }
    public int deleteByClientId(Long id){
        return dao.deleteByClientId(id);
    }
    public long countByClientEmail(String email){
        return dao.countByClientEmail(email);
    }
    public List<ReservationRequest> findByRequestedPropertyId(Long id){
        return dao.findByRequestedPropertyId(id);
    }
    public int deleteByRequestedPropertyId(Long id){
        return dao.deleteByRequestedPropertyId(id);
    }
    public long countByRequestedPropertyId(Long id){
        return dao.countByRequestedPropertyId(id);
    }
    public List<ReservationRequest> findByAlternativePropertyId(Long id){
        return dao.findByAlternativePropertyId(id);
    }
    public int deleteByAlternativePropertyId(Long id){
        return dao.deleteByAlternativePropertyId(id);
    }
    public long countByAlternativePropertyId(Long id){
        return dao.countByAlternativePropertyId(id);
    }
    public List<ReservationRequest> findByReviewedById(Long id){
        return dao.findByReviewedById(id);
    }
    public int deleteByReviewedById(Long id){
        return dao.deleteByReviewedById(id);
    }
    public long countByReviewedByEmail(String email){
        return dao.countByReviewedByEmail(email);
    }
    public List<ReservationRequest> findByReservationRequestStatusCode(String code){
        return dao.findByReservationRequestStatusCode(code);
    }
    public List<ReservationRequest> findByReservationRequestStatusId(Long id){
        return dao.findByReservationRequestStatusId(id);
    }
    public int deleteByReservationRequestStatusCode(String code){
        return dao.deleteByReservationRequestStatusCode(code);
    }
    public int deleteByReservationRequestStatusId(Long id){
        return dao.deleteByReservationRequestStatusId(id);
    }
    public long countByReservationRequestStatusCode(String code){
        return dao.countByReservationRequestStatusCode(code);
    }
    public List<ReservationRequest> findByReservationId(Long id){
        return dao.findByReservationId(id);
    }
    public int deleteByReservationId(Long id){
        return dao.deleteByReservationId(id);
    }
    public long countByReservationReference(String reference){
        return dao.countByReservationReference(reference);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            dao.deleteById(id);
        }
        return condition;
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ReservationRequest> delete(List<ReservationRequest> list) {
		List<ReservationRequest> result = new ArrayList();
        if (list != null) {
            for (ReservationRequest t : list) {
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
    public ReservationRequest create(ReservationRequest t) {
        ReservationRequest loaded = findByReferenceEntity(t);
        ReservationRequest saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public ReservationRequest findWithAssociatedLists(Long id){
        ReservationRequest result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ReservationRequest> update(List<ReservationRequest> ts, boolean createIfNotExist) {
        List<ReservationRequest> result = new ArrayList<>();
        if (ts != null) {
            for (ReservationRequest t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    ReservationRequest loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, ReservationRequest t, ReservationRequest loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public ReservationRequest findByReferenceEntity(ReservationRequest t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(ReservationRequest t){
        if( t != null) {
            t.setClient(clientService.findOrSave(t.getClient()));
            t.setRequestedProperty(propertyService.findOrSave(t.getRequestedProperty()));
            t.setAlternativeProperty(propertyService.findOrSave(t.getAlternativeProperty()));
            t.setReviewedBy(collaboratorService.findOrSave(t.getReviewedBy()));
            t.setReservationRequestStatus(reservationRequestStatusService.findOrSave(t.getReservationRequestStatus()));
            t.setReservation(reservationService.findOrSave(t.getReservation()));
        }
    }



    public List<ReservationRequest> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<ReservationRequest>> getToBeSavedAndToBeDeleted(List<ReservationRequest> oldList, List<ReservationRequest> newList) {
        List<List<ReservationRequest>> result = new ArrayList<>();
        List<ReservationRequest> resultDelete = new ArrayList<>();
        List<ReservationRequest> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<ReservationRequest> oldList, List<ReservationRequest> newList, List<ReservationRequest> resultUpdateOrSave, List<ReservationRequest> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                ReservationRequest myOld = oldList.get(i);
                ReservationRequest t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                ReservationRequest myNew = newList.get(i);
                ReservationRequest t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private CollaboratorAdminService collaboratorService ;
    @Autowired
    private ReservationAdminService reservationService ;
    @Autowired
    private ma.zyn.app.service.facade.admin.reservation.ReservationStatusAdminService reservationStatusService ;
    @Autowired
    private ma.zyn.app.service.facade.admin.reservation.ReservationPlatformAdminService reservationPlatformService ;
    @Autowired
    private PropertyAdminService propertyService ;
    @Autowired
    private ReservationRequestStatusAdminService reservationRequestStatusService ;
    @Autowired
    private ClientAdminService clientService ;

    public ReservationRequestAdminServiceImpl(ReservationRequestDao dao) {
        this.dao = dao;
    }

    private ReservationRequestDao dao;
}
