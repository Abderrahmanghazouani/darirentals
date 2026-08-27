package ma.zyn.app.service.impl.collaborator.property;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.zynerator.exception.PermissionDeniedException;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.dao.criteria.core.property.PropertyCriteria;
import ma.zyn.app.dao.facade.core.property.PropertyDao;
import ma.zyn.app.dao.specification.core.property.PropertySpecification;
import ma.zyn.app.service.facade.collaborator.property.PropertyCollaboratorService;
import ma.zyn.app.service.security.EnterpriseAccessService;
import ma.zyn.app.service.security.EffectivePermissionService;
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
import ma.zyn.app.service.facade.collaborator.reservation.ReservationCollaboratorService ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.service.facade.collaborator.report.FinancialReportPropertyCollaboratorService ;
import ma.zyn.app.bean.core.report.FinancialReportProperty ;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationRequestCollaboratorService ;
import ma.zyn.app.bean.core.reservation.ReservationRequest ;
import ma.zyn.app.service.facade.collaborator.enterprise.EnterpriseCollaboratorService ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.service.facade.collaborator.property.PropertyTypeCollaboratorService ;
import ma.zyn.app.bean.core.property.PropertyType ;
import ma.zyn.app.service.facade.collaborator.property.PropertyStatusCollaboratorService ;
import ma.zyn.app.bean.core.property.PropertyStatus ;
import ma.zyn.app.service.facade.collaborator.property.CityCollaboratorService ;
import ma.zyn.app.bean.core.property.City ;
import ma.zyn.app.service.facade.collaborator.charge.ChargeCollaboratorService ;
import ma.zyn.app.bean.core.charge.Charge ;
import ma.zyn.app.service.facade.collaborator.auth.CollaboratorPropertyAccessCollaboratorService ;

import java.util.List;
@Service
public class PropertyCollaboratorServiceImpl implements PropertyCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Property update(Property t) {
        Property loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Property.class.getSimpleName(), t.getId().toString()});
        } else {
            assertEnterpriseAssignable(t);
            assertPropertyManageable(loadedItem);
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    /** Chantier 1 (isolation par societe, cote ecriture) : empeche de creer/reassigner
     * une Property vers une societe a laquelle le collaborateur n'est pas rattache.
     * Voir NOTES-permissions.md. */
    private void assertEnterpriseAssignable(Property t) {
        Long enterpriseId = t.getEnterprise() != null ? t.getEnterprise().getId() : null;
        if (!enterpriseAccessService.hasAccessToEnterprise(enterpriseId)) {
            throw new PermissionDeniedException(
                "Vous n'etes pas rattache a cette societe : impossible de creer ou modifier cette propriete pour elle.",
                new String[]{"Property"});
        }
    }

    public Property findById(Long id) {
        Property found = dao.findById(id).orElse(null);
        if (found != null && !isAccessible(found)) {
            return null;
        }
        return found;
    }

    /** Chantier 1 (isolation par societe) + Chantier 3 (restriction par propriete pour un
     * Gestionnaire) : combine les deux dans EnterpriseAccessService.isPropertyAccessible().
     * Voir NOTES-permissions.md. */
    private boolean isAccessible(Property property) {
        return enterpriseAccessService.isPropertyAccessible(property);
    }

    /** Chantier 3 : un Gestionnaire ne peut modifier/supprimer que les proprietes qui lui
     * sont explicitement assignees via CollaboratorPropertyAccess - verifie sur l'entite
     * CHARGEE (avant modification), pas sur le DTO envoye par le client. Un SubAdmin n'est
     * jamais concerne. Voir NOTES-permissions.md. */
    private void assertPropertyManageable(Property loadedItem) {
        if (!enterpriseAccessService.isPropertyAccessible(loadedItem)) {
            throw new PermissionDeniedException(
                "Cette propriete ne vous est pas assignee : impossible de la modifier ou de la supprimer.",
                new String[]{"Property"});
        }
    }


    public Property findOrSave(Property t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Property result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Property> findAll() {
        return filterAccessible(dao.findByEnterpriseIdIn(enterpriseAccessService.getAccessibleEnterpriseIds()));
    }

    public List<Property> findByCriteria(PropertyCriteria criteria) {
        List<Property> content = null;
        if (criteria != null) {
            PropertySpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return filterAccessible(content);

    }

    private List<Property> filterAccessible(List<Property> items) {
        return emptyIfNull(items).stream()
                .filter(enterpriseAccessService::isPropertyAccessible)
                .collect(java.util.stream.Collectors.toList());
    }


    private PropertySpecification constructSpecification(PropertyCriteria criteria) {
        PropertySpecification mySpecification =  (PropertySpecification) RefelexivityUtil.constructObjectUsingOneParam(PropertySpecification.class, criteria);
        return mySpecification;
    }

    public List<Property> findPaginatedByCriteria(PropertyCriteria criteria, int page, int pageSize, String order, String sortField) {
        PropertySpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return filterAccessible(dao.findAll(mySpecification, pageable).getContent());
    }

    public int getDataSize(PropertyCriteria criteria) {
        return findByCriteria(criteria).size();
    }

    public List<Property> findByPropertyTypeCode(String code){
        return dao.findByPropertyTypeCode(code);
    }
    public List<Property> findByPropertyTypeId(Long id){
        return dao.findByPropertyTypeId(id);
    }
    public int deleteByPropertyTypeCode(String code){
        return dao.deleteByPropertyTypeCode(code);
    }
    public int deleteByPropertyTypeId(Long id){
        return dao.deleteByPropertyTypeId(id);
    }
    public long countByPropertyTypeCode(String code){
        return dao.countByPropertyTypeCode(code);
    }
    public List<Property> findByPropertyStatusCode(String code){
        return dao.findByPropertyStatusCode(code);
    }
    public List<Property> findByPropertyStatusId(Long id){
        return dao.findByPropertyStatusId(id);
    }
    public int deleteByPropertyStatusCode(String code){
        return dao.deleteByPropertyStatusCode(code);
    }
    public int deleteByPropertyStatusId(Long id){
        return dao.deleteByPropertyStatusId(id);
    }
    public long countByPropertyStatusCode(String code){
        return dao.countByPropertyStatusCode(code);
    }
    public List<Property> findByCityId(Long id){
        return dao.findByCityId(id);
    }
    public int deleteByCityId(Long id){
        return dao.deleteByCityId(id);
    }
    public long countByCityId(Long id){
        return dao.countByCityId(id);
    }
    public List<Property> findByEnterpriseId(Long id){
        if (!enterpriseAccessService.hasAccessToEnterprise(id)) {
            return new ArrayList<>();
        }
        return filterAccessible(dao.findByEnterpriseId(id));
    }
    public int deleteByEnterpriseId(Long id){
        return dao.deleteByEnterpriseId(id);
    }
    public long countByEnterpriseId(Long id){
        return dao.countByEnterpriseId(id);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            Property target = dao.findById(id).orElse(null);
            if (target != null) {
                Long enterpriseId = target.getEnterprise() != null ? target.getEnterprise().getId() : null;
                // Chantier 1 : isolation par societe. Chantier 3 : restriction par propriete
                // (Gestionnaire uniquement).
                if (!enterpriseAccessService.hasAccessToEnterprise(enterpriseId)) {
                    throw new PermissionDeniedException(
                        "Vous n'etes pas rattache a cette societe : impossible de supprimer cette propriete.",
                        new String[]{"Property"});
                }
                assertPropertyManageable(target);
                // Chantier 2 : permission de role.
                effectivePermissionService.assertCanDeleteProperty(enterpriseId);
            }
            deleteAssociatedLists(id);
            dao.deleteById(id);
        }
        return condition;
    }

    public void deleteAssociatedLists(Long id) {
        reservationService.deleteByPropertyId(id);
        chargeService.deleteByPropertyId(id);
        taskService.deleteByPropertyId(id);
        financialReportPropertyService.deleteByPropertyId(id);
        reservationRequestService.deleteByRequestedPropertyId(id);
        reservationRequestService.deleteByAlternativePropertyId(id);
        // Chantier 3 (NOTES-permissions.md) : nettoie les affectations Gestionnaire liees
        // a cette propriete, sinon lignes orphelines dans collaborator_property_access.
        propertyAccessService.deleteByPropertyId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Property> delete(List<Property> list) {
		List<Property> result = new ArrayList();
        if (list != null) {
            for (Property t : list) {
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
    public Property create(Property t) {
        assertEnterpriseAssignable(t);
        Property loaded = findByReferenceEntity(t);
        Property saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getReservations() != null) {
                t.getReservations().forEach(element-> {
                    element.setProperty(saved);
                    reservationService.create(element);
                });
            }
            if (t.getCharges() != null) {
                t.getCharges().forEach(element-> {
                    element.setProperty(saved);
                    chargeService.create(element);
                });
            }
            if (t.getTasks() != null) {
                t.getTasks().forEach(element-> {
                    element.setProperty(saved);
                    taskService.create(element);
                });
            }
            if (t.getFinancialReportProperties() != null) {
                t.getFinancialReportProperties().forEach(element-> {
                    element.setProperty(saved);
                    financialReportPropertyService.create(element);
                });
            }
            if (t.getReservationRequests() != null) {
                t.getReservationRequests().forEach(element-> {
                    element.setRequestedProperty(saved);
                    reservationRequestService.create(element);
                });
            }
            if (t.getAlternativeRequests() != null) {
                t.getAlternativeRequests().forEach(element-> {
                    element.setAlternativeProperty(saved);
                    reservationRequestService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public Property findWithAssociatedLists(Long id){
        Property result = dao.findById(id).orElse(null);
        if (result != null && !isAccessible(result)) {
            return null;
        }
        if(result!=null && result.getId() != null) {
            result.setReservations(reservationService.findByPropertyId(id));
            result.setCharges(chargeService.findByPropertyId(id));
            result.setTasks(taskService.findByPropertyId(id));
            result.setFinancialReportProperties(financialReportPropertyService.findByPropertyId(id));
            result.setReservationRequests(reservationRequestService.findByRequestedPropertyId(id));
            result.setAlternativeRequests(reservationRequestService.findByAlternativePropertyId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Property> update(List<Property> ts, boolean createIfNotExist) {
        List<Property> result = new ArrayList<>();
        if (ts != null) {
            for (Property t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Property loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Property t, Property loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Property property){
    if(property !=null && property.getId() != null){
        List<List<Reservation>> resultReservations= reservationService.getToBeSavedAndToBeDeleted(reservationService.findByPropertyId(property.getId()),property.getReservations());
            reservationService.delete(resultReservations.get(1));
        emptyIfNull(resultReservations.get(0)).forEach(e -> e.setProperty(property));
        reservationService.update(resultReservations.get(0),true);
        List<List<Charge>> resultCharges= chargeService.getToBeSavedAndToBeDeleted(chargeService.findByPropertyId(property.getId()),property.getCharges());
            chargeService.delete(resultCharges.get(1));
        emptyIfNull(resultCharges.get(0)).forEach(e -> e.setProperty(property));
        chargeService.update(resultCharges.get(0),true);
        List<List<Task>> resultTasks= taskService.getToBeSavedAndToBeDeleted(taskService.findByPropertyId(property.getId()),property.getTasks());
            taskService.delete(resultTasks.get(1));
        emptyIfNull(resultTasks.get(0)).forEach(e -> e.setProperty(property));
        taskService.update(resultTasks.get(0),true);
        List<List<FinancialReportProperty>> resultFinancialReportProperties= financialReportPropertyService.getToBeSavedAndToBeDeleted(financialReportPropertyService.findByPropertyId(property.getId()),property.getFinancialReportProperties());
            financialReportPropertyService.delete(resultFinancialReportProperties.get(1));
        emptyIfNull(resultFinancialReportProperties.get(0)).forEach(e -> e.setProperty(property));
        financialReportPropertyService.update(resultFinancialReportProperties.get(0),true);
        List<List<ReservationRequest>> resultReservationRequests= reservationRequestService.getToBeSavedAndToBeDeleted(reservationRequestService.findByRequestedPropertyId(property.getId()),property.getReservationRequests());
            reservationRequestService.delete(resultReservationRequests.get(1));
        emptyIfNull(resultReservationRequests.get(0)).forEach(e -> e.setRequestedProperty(property));
        reservationRequestService.update(resultReservationRequests.get(0),true);
        List<List<ReservationRequest>> resultAlternativeRequests= reservationRequestService.getToBeSavedAndToBeDeleted(reservationRequestService.findByAlternativePropertyId(property.getId()),property.getAlternativeRequests());
            reservationRequestService.delete(resultAlternativeRequests.get(1));
        emptyIfNull(resultAlternativeRequests.get(0)).forEach(e -> e.setAlternativeProperty(property));
        reservationRequestService.update(resultAlternativeRequests.get(0),true);
        }
    }








    public Property findByReferenceEntity(Property t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(Property t){
        if( t != null) {
            t.setPropertyType(propertyTypeService.findOrSave(t.getPropertyType()));
            t.setPropertyStatus(propertyStatusService.findOrSave(t.getPropertyStatus()));
            t.setCity(cityService.findOrSave(t.getCity()));
            t.setEnterprise(enterpriseService.findOrSave(t.getEnterprise()));
        }
    }



    public List<Property> findAllOptimized() {
        // La projection findAllOptimized() ne charge pas "enterprise" : on ne peut pas filtrer
        // dessus, donc on retombe sur la liste complete deja filtree par societe (Chantier 1).
        return findAll();
    }

    @Override
    public List<List<Property>> getToBeSavedAndToBeDeleted(List<Property> oldList, List<Property> newList) {
        List<List<Property>> result = new ArrayList<>();
        List<Property> resultDelete = new ArrayList<>();
        List<Property> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Property> oldList, List<Property> newList, List<Property> resultUpdateOrSave, List<Property> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Property myOld = oldList.get(i);
                Property t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Property myNew = newList.get(i);
                Property t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private TaskCollaboratorService taskService ;
    @Autowired
    private ReservationCollaboratorService reservationService ;
    @Autowired
    private FinancialReportPropertyCollaboratorService financialReportPropertyService ;
    @Autowired
    private ReservationRequestCollaboratorService reservationRequestService ;
    @Autowired
    private EnterpriseCollaboratorService enterpriseService ;
    @Autowired
    private PropertyTypeCollaboratorService propertyTypeService ;
    @Autowired
    private PropertyStatusCollaboratorService propertyStatusService ;
    @Autowired
    private CityCollaboratorService cityService ;
    @Autowired
    private ChargeCollaboratorService chargeService ;
    @Autowired
    private EnterpriseAccessService enterpriseAccessService ;
    @Autowired
    private EffectivePermissionService effectivePermissionService ;
    @Autowired
    private CollaboratorPropertyAccessCollaboratorService propertyAccessService ;

    public PropertyCollaboratorServiceImpl(PropertyDao dao) {
        this.dao = dao;
    }

    private PropertyDao dao;
}
