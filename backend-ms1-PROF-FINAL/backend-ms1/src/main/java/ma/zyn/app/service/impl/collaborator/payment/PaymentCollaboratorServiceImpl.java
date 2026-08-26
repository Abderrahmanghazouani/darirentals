package ma.zyn.app.service.impl.collaborator.payment;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.zynerator.exception.PermissionDeniedException;
import ma.zyn.app.bean.core.payment.Payment;
import ma.zyn.app.dao.criteria.core.payment.PaymentCriteria;
import ma.zyn.app.dao.facade.core.payment.PaymentDao;
import ma.zyn.app.dao.specification.core.payment.PaymentSpecification;
import ma.zyn.app.service.facade.collaborator.payment.PaymentCollaboratorService;
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

import ma.zyn.app.service.facade.collaborator.payment.PaymentStatusCollaboratorService ;
import ma.zyn.app.bean.core.payment.PaymentStatus ;
import ma.zyn.app.service.facade.collaborator.provider.ServiceProviderCollaboratorService ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.service.facade.collaborator.payment.PaymentTypeCollaboratorService ;
import ma.zyn.app.bean.core.payment.PaymentType ;
import ma.zyn.app.service.facade.collaborator.charge.ChargeCollaboratorService ;
import ma.zyn.app.bean.core.charge.Charge ;

import java.util.List;
@Service
public class PaymentCollaboratorServiceImpl implements PaymentCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Payment update(Payment t) {
        Payment loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Payment.class.getSimpleName(), t.getId().toString()});
        } else {
            assertServiceProviderAssignable(t);
            effectivePermissionService.assertCanManageFinancials(enterpriseIdOfServiceProvider(t.getServiceProvider().getId()));
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    /** Chantier 1 (isolation par societe, cote ecriture). Voir NOTES-permissions.md. */
    private void assertServiceProviderAssignable(Payment t) {
        Long serviceProviderId = t.getServiceProvider() != null ? t.getServiceProvider().getId() : null;
        if (serviceProviderId == null || !accessibleServiceProviderIds().contains(serviceProviderId)) {
            throw new PermissionDeniedException(
                "Vous n'etes pas rattache a la societe de ce prestataire : impossible de creer ou modifier ce paiement.",
                new String[]{"Payment"});
        }
    }

    /** Chantier 2 : societe du ServiceProvider, pour verifier canManageFinancials. */
    private Long enterpriseIdOfServiceProvider(Long serviceProviderId) {
        ServiceProvider provider = serviceProviderService.findById(serviceProviderId);
        return provider != null && provider.getEnterprise() != null ? provider.getEnterprise().getId() : null;
    }

    public Payment findById(Long id) {
        Payment found = dao.findById(id).orElse(null);
        if (found != null && !isAccessible(found)) {
            return null;
        }
        return found;
    }

    /** Chantier 1 (isolation par societe) : Payment n'a pas de lien direct vers
     * Enterprise ni vers Property, on passe par son ServiceProvider. Voir NOTES-permissions.md. */
    private boolean isAccessible(Payment payment) {
        if (payment.getServiceProvider() == null || payment.getServiceProvider().getEnterprise() == null
                || payment.getServiceProvider().getEnterprise().getId() == null) {
            return false;
        }
        return enterpriseAccessService.getAccessibleEnterpriseIds().contains(payment.getServiceProvider().getEnterprise().getId());
    }

    private List<Payment> filterAccessible(List<Payment> items) {
        List<Long> accessibleServiceProviderIds = accessibleServiceProviderIds();
        return emptyIfNull(items).stream()
                .filter(item -> item.getServiceProvider() != null && accessibleServiceProviderIds.contains(item.getServiceProvider().getId()))
                .collect(java.util.stream.Collectors.toList());
    }

    /** serviceProviderService (ServiceProviderCollaboratorServiceImpl) est deja filtre par societe (Chantier 1). */
    private List<Long> accessibleServiceProviderIds() {
        return serviceProviderService.findAll().stream()
                .map(ServiceProvider::getId)
                .collect(java.util.stream.Collectors.toList());
    }


    public Payment findOrSave(Payment t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Payment result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Payment> findAll() {
        return dao.findByServiceProviderIdIn(accessibleServiceProviderIds());
    }

    public List<Payment> findByCriteria(PaymentCriteria criteria) {
        List<Payment> content = null;
        if (criteria != null) {
            PaymentSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return filterAccessible(content);

    }


    private PaymentSpecification constructSpecification(PaymentCriteria criteria) {
        PaymentSpecification mySpecification =  (PaymentSpecification) RefelexivityUtil.constructObjectUsingOneParam(PaymentSpecification.class, criteria);
        return mySpecification;
    }

    public List<Payment> findPaginatedByCriteria(PaymentCriteria criteria, int page, int pageSize, String order, String sortField) {
        PaymentSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return filterAccessible(dao.findAll(mySpecification, pageable).getContent());
    }

    public int getDataSize(PaymentCriteria criteria) {
        return findByCriteria(criteria).size();
    }

    public List<Payment> findByServiceProviderId(Long id){
        if (!accessibleServiceProviderIds().contains(id)) {
            return new ArrayList<>();
        }
        return dao.findByServiceProviderId(id);
    }
    public int deleteByServiceProviderId(Long id){
        return dao.deleteByServiceProviderId(id);
    }
    public long countByServiceProviderId(Long id){
        return dao.countByServiceProviderId(id);
    }
    public List<Payment> findByPaymentTypeCode(String code){
        return dao.findByPaymentTypeCode(code);
    }
    public List<Payment> findByPaymentTypeId(Long id){
        return dao.findByPaymentTypeId(id);
    }
    public int deleteByPaymentTypeCode(String code){
        return dao.deleteByPaymentTypeCode(code);
    }
    public int deleteByPaymentTypeId(Long id){
        return dao.deleteByPaymentTypeId(id);
    }
    public long countByPaymentTypeCode(String code){
        return dao.countByPaymentTypeCode(code);
    }
    public List<Payment> findByPaymentStatusCode(String code){
        return dao.findByPaymentStatusCode(code);
    }
    public List<Payment> findByPaymentStatusId(Long id){
        return dao.findByPaymentStatusId(id);
    }
    public int deleteByPaymentStatusCode(String code){
        return dao.deleteByPaymentStatusCode(code);
    }
    public int deleteByPaymentStatusId(Long id){
        return dao.deleteByPaymentStatusId(id);
    }
    public long countByPaymentStatusCode(String code){
        return dao.countByPaymentStatusCode(code);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            Payment target = dao.findById(id).orElse(null);
            if (target != null) {
                if (!isAccessible(target)) {
                    throw new PermissionDeniedException(
                        "Vous n'etes pas rattache a la societe de ce prestataire : impossible de supprimer ce paiement.",
                        new String[]{"Payment"});
                }
                effectivePermissionService.assertCanManageFinancials(target.getServiceProvider().getEnterprise().getId());
            }
            deleteAssociatedLists(id);
            dao.deleteById(id);
        }
        return condition;
    }

    public void deleteAssociatedLists(Long id) {
        chargeService.deleteByPaymentId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Payment> delete(List<Payment> list) {
		List<Payment> result = new ArrayList();
        if (list != null) {
            for (Payment t : list) {
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
    public Payment create(Payment t) {
        assertServiceProviderAssignable(t);
        effectivePermissionService.assertCanManageFinancials(enterpriseIdOfServiceProvider(t.getServiceProvider().getId()));
        Payment loaded = findByReferenceEntity(t);
        Payment saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getCharges() != null) {
                t.getCharges().forEach(element-> {
                    element.setPayment(saved);
                    chargeService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public Payment findWithAssociatedLists(Long id){
        Payment result = dao.findById(id).orElse(null);
        if (result != null && !isAccessible(result)) {
            return null;
        }
        if(result!=null && result.getId() != null) {
            result.setCharges(chargeService.findByPaymentId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Payment> update(List<Payment> ts, boolean createIfNotExist) {
        List<Payment> result = new ArrayList<>();
        if (ts != null) {
            for (Payment t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Payment loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Payment t, Payment loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Payment payment){
    if(payment !=null && payment.getId() != null){
        List<List<Charge>> resultCharges= chargeService.getToBeSavedAndToBeDeleted(chargeService.findByPaymentId(payment.getId()),payment.getCharges());
            chargeService.delete(resultCharges.get(1));
        emptyIfNull(resultCharges.get(0)).forEach(e -> e.setPayment(payment));
        chargeService.update(resultCharges.get(0),true);
        }
    }








    public Payment findByReferenceEntity(Payment t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(Payment t){
        if( t != null) {
            t.setServiceProvider(serviceProviderService.findOrSave(t.getServiceProvider()));
            t.setPaymentType(paymentTypeService.findOrSave(t.getPaymentType()));
            t.setPaymentStatus(paymentStatusService.findOrSave(t.getPaymentStatus()));
        }
    }



    public List<Payment> findAllOptimized() {
        return findAll();
    }

    @Override
    public List<List<Payment>> getToBeSavedAndToBeDeleted(List<Payment> oldList, List<Payment> newList) {
        List<List<Payment>> result = new ArrayList<>();
        List<Payment> resultDelete = new ArrayList<>();
        List<Payment> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Payment> oldList, List<Payment> newList, List<Payment> resultUpdateOrSave, List<Payment> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Payment myOld = oldList.get(i);
                Payment t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Payment myNew = newList.get(i);
                Payment t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private PaymentStatusCollaboratorService paymentStatusService ;
    @Autowired
    private ServiceProviderCollaboratorService serviceProviderService ;
    @Autowired
    private PaymentTypeCollaboratorService paymentTypeService ;
    @Autowired
    private ChargeCollaboratorService chargeService ;
    @Autowired
    private EnterpriseAccessService enterpriseAccessService ;
    @Autowired
    private EffectivePermissionService effectivePermissionService ;

    public PaymentCollaboratorServiceImpl(PaymentDao dao) {
        this.dao = dao;
    }

    private PaymentDao dao;
}
