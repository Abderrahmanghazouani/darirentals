package ma.zyn.app.service.impl.collaborator.provider;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.zynerator.exception.PermissionDeniedException;
import ma.zyn.app.bean.core.provider.ServiceProvider;
import ma.zyn.app.dao.criteria.core.provider.ServiceProviderCriteria;
import ma.zyn.app.dao.facade.core.provider.ServiceProviderDao;
import ma.zyn.app.dao.specification.core.provider.ServiceProviderSpecification;
import ma.zyn.app.service.facade.collaborator.provider.ServiceProviderCollaboratorService;
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
import ma.zyn.app.service.facade.collaborator.task.TaskCollaboratorService ;
import ma.zyn.app.bean.core.task.Task ;
import ma.zyn.app.service.facade.collaborator.provider.ServiceTypeCollaboratorService ;
import ma.zyn.app.bean.core.provider.ServiceType ;
import ma.zyn.app.service.facade.collaborator.enterprise.EnterpriseCollaboratorService ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;

import java.util.List;
@Service
public class ServiceProviderCollaboratorServiceImpl implements ServiceProviderCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ServiceProvider update(ServiceProvider t) {
        ServiceProvider loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{ServiceProvider.class.getSimpleName(), t.getId().toString()});
        } else {
            assertEnterpriseAssignable(t);
            effectivePermissionService.assertCanManageServiceProviders(t.getEnterprise().getId());
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    /** Chantier 1 (isolation par societe, cote ecriture). Voir NOTES-permissions.md. */
    private void assertEnterpriseAssignable(ServiceProvider t) {
        Long enterpriseId = t.getEnterprise() != null ? t.getEnterprise().getId() : null;
        if (!enterpriseAccessService.hasAccessToEnterprise(enterpriseId)) {
            throw new PermissionDeniedException(
                "Vous n'etes pas rattache a cette societe : impossible de creer ou modifier ce prestataire pour elle.",
                new String[]{"ServiceProvider"});
        }
    }

    public ServiceProvider findById(Long id) {
        ServiceProvider found = dao.findById(id).orElse(null);
        if (found != null && !isAccessible(found)) {
            return null;
        }
        return found;
    }

    /** Chantier 1 (isolation par societe). Voir NOTES-permissions.md. */
    private boolean isAccessible(ServiceProvider provider) {
        if (provider.getEnterprise() == null || provider.getEnterprise().getId() == null) {
            return false;
        }
        return enterpriseAccessService.getAccessibleEnterpriseIds().contains(provider.getEnterprise().getId());
    }

    private List<ServiceProvider> filterAccessible(List<ServiceProvider> items) {
        List<Long> accessibleIds = enterpriseAccessService.getAccessibleEnterpriseIds();
        return emptyIfNull(items).stream()
                .filter(item -> item.getEnterprise() != null && accessibleIds.contains(item.getEnterprise().getId()))
                .collect(java.util.stream.Collectors.toList());
    }


    public ServiceProvider findOrSave(ServiceProvider t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            ServiceProvider result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<ServiceProvider> findAll() {
        return dao.findByEnterpriseIdIn(enterpriseAccessService.getAccessibleEnterpriseIds());
    }

    public List<ServiceProvider> findByCriteria(ServiceProviderCriteria criteria) {
        List<ServiceProvider> content = null;
        if (criteria != null) {
            ServiceProviderSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return filterAccessible(content);

    }


    private ServiceProviderSpecification constructSpecification(ServiceProviderCriteria criteria) {
        ServiceProviderSpecification mySpecification =  (ServiceProviderSpecification) RefelexivityUtil.constructObjectUsingOneParam(ServiceProviderSpecification.class, criteria);
        return mySpecification;
    }

    public List<ServiceProvider> findPaginatedByCriteria(ServiceProviderCriteria criteria, int page, int pageSize, String order, String sortField) {
        ServiceProviderSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return filterAccessible(dao.findAll(mySpecification, pageable).getContent());
    }

    public int getDataSize(ServiceProviderCriteria criteria) {
        return findByCriteria(criteria).size();
    }

    public List<ServiceProvider> findByServiceTypeCode(String code){
        return dao.findByServiceTypeCode(code);
    }
    public List<ServiceProvider> findByServiceTypeId(Long id){
        return dao.findByServiceTypeId(id);
    }
    public int deleteByServiceTypeCode(String code){
        return dao.deleteByServiceTypeCode(code);
    }
    public int deleteByServiceTypeId(Long id){
        return dao.deleteByServiceTypeId(id);
    }
    public long countByServiceTypeCode(String code){
        return dao.countByServiceTypeCode(code);
    }
    public List<ServiceProvider> findByEnterpriseId(Long id){
        if (!enterpriseAccessService.hasAccessToEnterprise(id)) {
            return new ArrayList<>();
        }
        return dao.findByEnterpriseId(id);
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
            ServiceProvider target = dao.findById(id).orElse(null);
            if (target != null) {
                if (!isAccessible(target)) {
                    throw new PermissionDeniedException(
                        "Vous n'etes pas rattache a cette societe : impossible de supprimer ce prestataire.",
                        new String[]{"ServiceProvider"});
                }
                effectivePermissionService.assertCanManageServiceProviders(target.getEnterprise().getId());
            }
            deleteAssociatedLists(id);
            dao.deleteById(id);
        }
        return condition;
    }

    public void deleteAssociatedLists(Long id) {
        paymentService.deleteByServiceProviderId(id);
        taskService.deleteByServiceProviderId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ServiceProvider> delete(List<ServiceProvider> list) {
		List<ServiceProvider> result = new ArrayList();
        if (list != null) {
            for (ServiceProvider t : list) {
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
    public ServiceProvider create(ServiceProvider t) {
        assertEnterpriseAssignable(t);
        effectivePermissionService.assertCanManageServiceProviders(t.getEnterprise().getId());
        ServiceProvider loaded = findByReferenceEntity(t);
        ServiceProvider saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getPayments() != null) {
                t.getPayments().forEach(element-> {
                    element.setServiceProvider(saved);
                    paymentService.create(element);
                });
            }
            if (t.getTasks() != null) {
                t.getTasks().forEach(element-> {
                    element.setServiceProvider(saved);
                    taskService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public ServiceProvider findWithAssociatedLists(Long id){
        ServiceProvider result = dao.findById(id).orElse(null);
        if (result != null && !isAccessible(result)) {
            return null;
        }
        if(result!=null && result.getId() != null) {
            result.setPayments(paymentService.findByServiceProviderId(id));
            result.setTasks(taskService.findByServiceProviderId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ServiceProvider> update(List<ServiceProvider> ts, boolean createIfNotExist) {
        List<ServiceProvider> result = new ArrayList<>();
        if (ts != null) {
            for (ServiceProvider t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    ServiceProvider loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, ServiceProvider t, ServiceProvider loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(ServiceProvider serviceProvider){
    if(serviceProvider !=null && serviceProvider.getId() != null){
        List<List<Payment>> resultPayments= paymentService.getToBeSavedAndToBeDeleted(paymentService.findByServiceProviderId(serviceProvider.getId()),serviceProvider.getPayments());
            paymentService.delete(resultPayments.get(1));
        emptyIfNull(resultPayments.get(0)).forEach(e -> e.setServiceProvider(serviceProvider));
        paymentService.update(resultPayments.get(0),true);
        List<List<Task>> resultTasks= taskService.getToBeSavedAndToBeDeleted(taskService.findByServiceProviderId(serviceProvider.getId()),serviceProvider.getTasks());
            taskService.delete(resultTasks.get(1));
        emptyIfNull(resultTasks.get(0)).forEach(e -> e.setServiceProvider(serviceProvider));
        taskService.update(resultTasks.get(0),true);
        }
    }








    public ServiceProvider findByReferenceEntity(ServiceProvider t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(ServiceProvider t){
        if( t != null) {
            t.setServiceType(serviceTypeService.findOrSave(t.getServiceType()));
            t.setEnterprise(enterpriseService.findOrSave(t.getEnterprise()));
        }
    }



    public List<ServiceProvider> findAllOptimized() {
        // La projection findAllOptimized() ne charge pas "enterprise" : on retombe sur
        // la liste complete deja filtree par societe (Chantier 1).
        return findAll();
    }

    @Override
    public List<List<ServiceProvider>> getToBeSavedAndToBeDeleted(List<ServiceProvider> oldList, List<ServiceProvider> newList) {
        List<List<ServiceProvider>> result = new ArrayList<>();
        List<ServiceProvider> resultDelete = new ArrayList<>();
        List<ServiceProvider> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<ServiceProvider> oldList, List<ServiceProvider> newList, List<ServiceProvider> resultUpdateOrSave, List<ServiceProvider> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                ServiceProvider myOld = oldList.get(i);
                ServiceProvider t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                ServiceProvider myNew = newList.get(i);
                ServiceProvider t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private PaymentCollaboratorService paymentService ;
    @Autowired
    private TaskCollaboratorService taskService ;
    @Autowired
    private ServiceTypeCollaboratorService serviceTypeService ;
    @Autowired
    private EnterpriseCollaboratorService enterpriseService ;
    @Autowired
    private EnterpriseAccessService enterpriseAccessService ;
    @Autowired
    private EffectivePermissionService effectivePermissionService ;

    public ServiceProviderCollaboratorServiceImpl(ServiceProviderDao dao) {
        this.dao = dao;
    }

    private ServiceProviderDao dao;
}
