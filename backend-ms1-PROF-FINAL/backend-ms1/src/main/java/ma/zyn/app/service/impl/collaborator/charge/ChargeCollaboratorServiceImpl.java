package ma.zyn.app.service.impl.collaborator.charge;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.zynerator.exception.PermissionDeniedException;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.dao.criteria.core.charge.ChargeCriteria;
import ma.zyn.app.dao.facade.core.charge.ChargeDao;
import ma.zyn.app.dao.specification.core.charge.ChargeSpecification;
import ma.zyn.app.service.facade.collaborator.charge.ChargeCollaboratorService;
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

import ma.zyn.app.service.facade.collaborator.payment.PaymentCollaboratorService ;
import ma.zyn.app.bean.core.payment.Payment ;
import ma.zyn.app.service.facade.collaborator.charge.ChargeTypeCollaboratorService ;
import ma.zyn.app.bean.core.charge.ChargeType ;
import ma.zyn.app.service.facade.collaborator.property.PropertyCollaboratorService ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.service.facade.collaborator.document.DocumentCollaboratorService ;
import ma.zyn.app.bean.core.document.Document ;

import java.util.List;
@Service
public class ChargeCollaboratorServiceImpl implements ChargeCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Charge update(Charge t) {
        Charge loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Charge.class.getSimpleName(), t.getId().toString()});
        } else {
            assertPropertyAssignable(t);
            effectivePermissionService.assertCanManageFinancials(enterpriseIdOfProperty(t.getProperty().getId()));
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    /** Chantier 1 (isolation par societe, cote ecriture). Voir NOTES-permissions.md. */
    private void assertPropertyAssignable(Charge t) {
        Long propertyId = t.getProperty() != null ? t.getProperty().getId() : null;
        if (propertyId == null || !accessiblePropertyIds().contains(propertyId)) {
            throw new PermissionDeniedException(
                "Vous n'etes pas rattache a la societe de cette propriete : impossible de creer ou modifier cette charge.",
                new String[]{"Charge"});
        }
    }

    /** Chantier 2 : societe de la property, pour verifier canManageFinancials. */
    private Long enterpriseIdOfProperty(Long propertyId) {
        Property property = propertyService.findById(propertyId);
        return property != null && property.getEnterprise() != null ? property.getEnterprise().getId() : null;
    }

    public Charge findById(Long id) {
        Charge found = dao.findById(id).orElse(null);
        if (found != null && !isAccessible(found)) {
            return null;
        }
        return found;
    }

    /** Chantier 1 (isolation par societe) + Chantier 3 (restriction par propriete pour un
     * Gestionnaire) : Charge n'a pas de lien direct vers Enterprise, on passe par sa
     * Property - accessiblePropertyIds() applique deja les deux (via propertyService.findAll()
     * qui les combine). Voir NOTES-permissions.md. */
    private boolean isAccessible(Charge charge) {
        if (charge.getProperty() == null || charge.getProperty().getId() == null) {
            return false;
        }
        return accessiblePropertyIds().contains(charge.getProperty().getId());
    }

    private List<Charge> filterAccessible(List<Charge> items) {
        List<Long> accessiblePropertyIds = accessiblePropertyIds();
        return emptyIfNull(items).stream()
                .filter(item -> item.getProperty() != null && accessiblePropertyIds.contains(item.getProperty().getId()))
                .collect(java.util.stream.Collectors.toList());
    }

    /** propertyService (PropertyCollaboratorServiceImpl) est deja filtre par societe (Chantier 1). */
    private List<Long> accessiblePropertyIds() {
        return propertyService.findAll().stream()
                .map(Property::getId)
                .collect(java.util.stream.Collectors.toList());
    }


    public Charge findOrSave(Charge t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Charge result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Charge> findAll() {
        return dao.findByPropertyIdIn(accessiblePropertyIds());
    }

    public List<Charge> findByCriteria(ChargeCriteria criteria) {
        List<Charge> content = null;
        if (criteria != null) {
            ChargeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return filterAccessible(content);

    }


    private ChargeSpecification constructSpecification(ChargeCriteria criteria) {
        ChargeSpecification mySpecification =  (ChargeSpecification) RefelexivityUtil.constructObjectUsingOneParam(ChargeSpecification.class, criteria);
        return mySpecification;
    }

    public List<Charge> findPaginatedByCriteria(ChargeCriteria criteria, int page, int pageSize, String order, String sortField) {
        ChargeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return filterAccessible(dao.findAll(mySpecification, pageable).getContent());
    }

    public int getDataSize(ChargeCriteria criteria) {
        return findByCriteria(criteria).size();
    }

    public List<Charge> findByPropertyId(Long id){
        if (!accessiblePropertyIds().contains(id)) {
            return new ArrayList<>();
        }
        return dao.findByPropertyId(id);
    }
    public int deleteByPropertyId(Long id){
        return dao.deleteByPropertyId(id);
    }
    public long countByPropertyId(Long id){
        return dao.countByPropertyId(id);
    }
    public List<Charge> findByChargeTypeCode(String code){
        return dao.findByChargeTypeCode(code);
    }
    public List<Charge> findByChargeTypeId(Long id){
        return dao.findByChargeTypeId(id);
    }
    public int deleteByChargeTypeCode(String code){
        return dao.deleteByChargeTypeCode(code);
    }
    public int deleteByChargeTypeId(Long id){
        return dao.deleteByChargeTypeId(id);
    }
    public long countByChargeTypeCode(String code){
        return dao.countByChargeTypeCode(code);
    }
    public List<Charge> findByPaymentId(Long id){
        return dao.findByPaymentId(id);
    }
    public int deleteByPaymentId(Long id){
        return dao.deleteByPaymentId(id);
    }
    public long countByPaymentId(Long id){
        return dao.countByPaymentId(id);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            Charge target = dao.findById(id).orElse(null);
            if (target != null) {
                if (!isAccessible(target)) {
                    throw new PermissionDeniedException(
                        "Vous n'etes pas rattache a la societe de cette propriete : impossible de supprimer cette charge.",
                        new String[]{"Charge"});
                }
                Long enterpriseId = target.getProperty().getEnterprise().getId();
                effectivePermissionService.assertCanManageFinancials(enterpriseId);
            }
            deleteAssociatedLists(id);
            dao.deleteById(id);
        }
        return condition;
    }

    public void deleteAssociatedLists(Long id) {
        documentService.deleteByChargeId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Charge> delete(List<Charge> list) {
		List<Charge> result = new ArrayList();
        if (list != null) {
            for (Charge t : list) {
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
    public Charge create(Charge t) {
        assertPropertyAssignable(t);
        effectivePermissionService.assertCanManageFinancials(enterpriseIdOfProperty(t.getProperty().getId()));
        Charge loaded = findByReferenceEntity(t);
        Charge saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getDocuments() != null) {
                t.getDocuments().forEach(element-> {
                    element.setCharge(saved);
                    documentService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public Charge findWithAssociatedLists(Long id){
        Charge result = dao.findById(id).orElse(null);
        if (result != null && !isAccessible(result)) {
            return null;
        }
        if(result!=null && result.getId() != null) {
            result.setDocuments(documentService.findByChargeId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Charge> update(List<Charge> ts, boolean createIfNotExist) {
        List<Charge> result = new ArrayList<>();
        if (ts != null) {
            for (Charge t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Charge loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Charge t, Charge loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Charge charge){
    if(charge !=null && charge.getId() != null){
        List<List<Document>> resultDocuments= documentService.getToBeSavedAndToBeDeleted(documentService.findByChargeId(charge.getId()),charge.getDocuments());
            documentService.delete(resultDocuments.get(1));
        emptyIfNull(resultDocuments.get(0)).forEach(e -> e.setCharge(charge));
        documentService.update(resultDocuments.get(0),true);
        }
    }








    public Charge findByReferenceEntity(Charge t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(Charge t){
        if( t != null) {
            t.setProperty(propertyService.findOrSave(t.getProperty()));
            t.setChargeType(chargeTypeService.findOrSave(t.getChargeType()));
            t.setPayment(paymentService.findOrSave(t.getPayment()));
        }
    }



    public List<Charge> findAllOptimized() {
        // La projection findAllOptimized() ne charge pas "property" : on retombe sur
        // la liste complete deja filtree par societe (Chantier 1).
        return findAll();
    }

    @Override
    public List<List<Charge>> getToBeSavedAndToBeDeleted(List<Charge> oldList, List<Charge> newList) {
        List<List<Charge>> result = new ArrayList<>();
        List<Charge> resultDelete = new ArrayList<>();
        List<Charge> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Charge> oldList, List<Charge> newList, List<Charge> resultUpdateOrSave, List<Charge> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Charge myOld = oldList.get(i);
                Charge t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Charge myNew = newList.get(i);
                Charge t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private PaymentCollaboratorService paymentService ;
    @Autowired
    private ChargeTypeCollaboratorService chargeTypeService ;
    @Autowired
    private PropertyCollaboratorService propertyService ;
    @Autowired
    private DocumentCollaboratorService documentService ;
    @Autowired
    private EnterpriseAccessService enterpriseAccessService ;
    @Autowired
    private EffectivePermissionService effectivePermissionService ;

    public ChargeCollaboratorServiceImpl(ChargeDao dao) {
        this.dao = dao;
    }

    private ChargeDao dao;
}
