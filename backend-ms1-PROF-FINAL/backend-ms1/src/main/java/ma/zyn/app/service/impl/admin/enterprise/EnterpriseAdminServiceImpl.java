package ma.zyn.app.service.impl.admin.enterprise;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.enterprise.Enterprise;
import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseCriteria;
import ma.zyn.app.dao.facade.core.enterprise.EnterpriseDao;
import ma.zyn.app.dao.specification.core.enterprise.EnterpriseSpecification;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseAdminService;
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

import ma.zyn.app.service.facade.admin.ai.AiQuotaAdminService ;
import ma.zyn.app.bean.core.ai.AiQuota ;
import ma.zyn.app.service.facade.admin.ai.AiUsageLogAdminService ;
import ma.zyn.app.bean.core.ai.AiUsageLog ;
import ma.zyn.app.service.facade.admin.provider.ServiceProviderAdminService ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.service.facade.admin.currency.CurrencyAdminService ;
import ma.zyn.app.bean.core.currency.Currency ;
import ma.zyn.app.service.facade.admin.report.FinancialReportAdminService ;
import ma.zyn.app.bean.core.report.FinancialReport ;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseMembershipAdminService ;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership ;
import ma.zyn.app.service.facade.admin.property.PropertyAdminService ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.service.facade.admin.client.ClientAdminService ;
import ma.zyn.app.bean.core.client.Client ;

import java.util.List;
@Service
public class EnterpriseAdminServiceImpl implements EnterpriseAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Enterprise update(Enterprise t) {
        Enterprise loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Enterprise.class.getSimpleName(), t.getId().toString()});
        } else {
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    public Enterprise findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public Enterprise findOrSave(Enterprise t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Enterprise result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Enterprise> findAll() {
        return dao.findAll();
    }

    public List<Enterprise> findByCriteria(EnterpriseCriteria criteria) {
        List<Enterprise> content = null;
        if (criteria != null) {
            EnterpriseSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private EnterpriseSpecification constructSpecification(EnterpriseCriteria criteria) {
        EnterpriseSpecification mySpecification =  (EnterpriseSpecification) RefelexivityUtil.constructObjectUsingOneParam(EnterpriseSpecification.class, criteria);
        return mySpecification;
    }

    public List<Enterprise> findPaginatedByCriteria(EnterpriseCriteria criteria, int page, int pageSize, String order, String sortField) {
        EnterpriseSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(EnterpriseCriteria criteria) {
        EnterpriseSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<Enterprise> findByCurrencyCode(String code){
        return dao.findByCurrencyCode(code);
    }
    public List<Enterprise> findByCurrencyId(Long id){
        return dao.findByCurrencyId(id);
    }
    public int deleteByCurrencyCode(String code){
        return dao.deleteByCurrencyCode(code);
    }
    public int deleteByCurrencyId(Long id){
        return dao.deleteByCurrencyId(id);
    }
    public long countByCurrencyCode(String code){
        return dao.countByCurrencyCode(code);
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
        propertyService.deleteByEnterpriseId(id);
        clientService.deleteByEnterpriseId(id);
        serviceProviderService.deleteByEnterpriseId(id);
        enterpriseMembershipService.deleteByEnterpriseId(id);
        aiQuotaService.deleteByEnterpriseId(id);
        aiUsageLogService.deleteByEnterpriseId(id);
        financialReportService.deleteByEnterpriseId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Enterprise> delete(List<Enterprise> list) {
		List<Enterprise> result = new ArrayList();
        if (list != null) {
            for (Enterprise t : list) {
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
    public Enterprise create(Enterprise t) {
        if (t.getCurrency() == null) {
            // Devise de reference par defaut (MAD) si l'admin n'en choisit pas explicitement -
            // voir NOTES-devises.md.
            t.setCurrency(currencyService.findAll().stream()
                    .filter(c -> Boolean.TRUE.equals(c.getIsDefault()))
                    .findFirst()
                    .orElse(null));
        }
        Enterprise loaded = findByReferenceEntity(t);
        Enterprise saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getProperties() != null) {
                t.getProperties().forEach(element-> {
                    element.setEnterprise(saved);
                    propertyService.create(element);
                });
            }
            if (t.getClients() != null) {
                t.getClients().forEach(element-> {
                    element.setEnterprise(saved);
                    clientService.create(element);
                });
            }
            if (t.getServiceProviders() != null) {
                t.getServiceProviders().forEach(element-> {
                    element.setEnterprise(saved);
                    serviceProviderService.create(element);
                });
            }
            if (t.getEnterpriseMemberships() != null) {
                t.getEnterpriseMemberships().forEach(element-> {
                    element.setEnterprise(saved);
                    enterpriseMembershipService.create(element);
                });
            }
            if (t.getAiQuotas() != null) {
                t.getAiQuotas().forEach(element-> {
                    element.setEnterprise(saved);
                    aiQuotaService.create(element);
                });
            }
            if (t.getAiUsageLogs() != null) {
                t.getAiUsageLogs().forEach(element-> {
                    element.setEnterprise(saved);
                    aiUsageLogService.create(element);
                });
            }
            if (t.getFinancialReports() != null) {
                t.getFinancialReports().forEach(element-> {
                    element.setEnterprise(saved);
                    financialReportService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public Enterprise findWithAssociatedLists(Long id){
        Enterprise result = dao.findById(id).orElse(null);
        if(result!=null && result.getId() != null) {
            result.setProperties(propertyService.findByEnterpriseId(id));
            result.setClients(clientService.findByEnterpriseId(id));
            result.setServiceProviders(serviceProviderService.findByEnterpriseId(id));
            result.setEnterpriseMemberships(enterpriseMembershipService.findByEnterpriseId(id));
            result.setAiQuotas(aiQuotaService.findByEnterpriseId(id));
            result.setAiUsageLogs(aiUsageLogService.findByEnterpriseId(id));
            result.setFinancialReports(financialReportService.findByEnterpriseId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Enterprise> update(List<Enterprise> ts, boolean createIfNotExist) {
        List<Enterprise> result = new ArrayList<>();
        if (ts != null) {
            for (Enterprise t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Enterprise loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Enterprise t, Enterprise loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Enterprise enterprise){
    if(enterprise !=null && enterprise.getId() != null){
        List<List<Property>> resultProperties= propertyService.getToBeSavedAndToBeDeleted(propertyService.findByEnterpriseId(enterprise.getId()),enterprise.getProperties());
            propertyService.delete(resultProperties.get(1));
        emptyIfNull(resultProperties.get(0)).forEach(e -> e.setEnterprise(enterprise));
        propertyService.update(resultProperties.get(0),true);
        List<List<Client>> resultClients= clientService.getToBeSavedAndToBeDeleted(clientService.findByEnterpriseId(enterprise.getId()),enterprise.getClients());
            clientService.delete(resultClients.get(1));
        emptyIfNull(resultClients.get(0)).forEach(e -> e.setEnterprise(enterprise));
        clientService.update(resultClients.get(0),true);
        List<List<ServiceProvider>> resultServiceProviders= serviceProviderService.getToBeSavedAndToBeDeleted(serviceProviderService.findByEnterpriseId(enterprise.getId()),enterprise.getServiceProviders());
            serviceProviderService.delete(resultServiceProviders.get(1));
        emptyIfNull(resultServiceProviders.get(0)).forEach(e -> e.setEnterprise(enterprise));
        serviceProviderService.update(resultServiceProviders.get(0),true);
        List<List<EnterpriseMembership>> resultEnterpriseMemberships= enterpriseMembershipService.getToBeSavedAndToBeDeleted(enterpriseMembershipService.findByEnterpriseId(enterprise.getId()),enterprise.getEnterpriseMemberships());
            enterpriseMembershipService.delete(resultEnterpriseMemberships.get(1));
        emptyIfNull(resultEnterpriseMemberships.get(0)).forEach(e -> e.setEnterprise(enterprise));
        enterpriseMembershipService.update(resultEnterpriseMemberships.get(0),true);
        List<List<AiQuota>> resultAiQuotas= aiQuotaService.getToBeSavedAndToBeDeleted(aiQuotaService.findByEnterpriseId(enterprise.getId()),enterprise.getAiQuotas());
            aiQuotaService.delete(resultAiQuotas.get(1));
        emptyIfNull(resultAiQuotas.get(0)).forEach(e -> e.setEnterprise(enterprise));
        aiQuotaService.update(resultAiQuotas.get(0),true);
        List<List<AiUsageLog>> resultAiUsageLogs= aiUsageLogService.getToBeSavedAndToBeDeleted(aiUsageLogService.findByEnterpriseId(enterprise.getId()),enterprise.getAiUsageLogs());
            aiUsageLogService.delete(resultAiUsageLogs.get(1));
        emptyIfNull(resultAiUsageLogs.get(0)).forEach(e -> e.setEnterprise(enterprise));
        aiUsageLogService.update(resultAiUsageLogs.get(0),true);
        List<List<FinancialReport>> resultFinancialReports= financialReportService.getToBeSavedAndToBeDeleted(financialReportService.findByEnterpriseId(enterprise.getId()),enterprise.getFinancialReports());
            financialReportService.delete(resultFinancialReports.get(1));
        emptyIfNull(resultFinancialReports.get(0)).forEach(e -> e.setEnterprise(enterprise));
        financialReportService.update(resultFinancialReports.get(0),true);
        }
    }








    public Enterprise findByReferenceEntity(Enterprise t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(Enterprise t){
        if( t != null) {
            t.setCurrency(currencyService.findOrSave(t.getCurrency()));
        }
    }



    public List<Enterprise> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<Enterprise>> getToBeSavedAndToBeDeleted(List<Enterprise> oldList, List<Enterprise> newList) {
        List<List<Enterprise>> result = new ArrayList<>();
        List<Enterprise> resultDelete = new ArrayList<>();
        List<Enterprise> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Enterprise> oldList, List<Enterprise> newList, List<Enterprise> resultUpdateOrSave, List<Enterprise> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Enterprise myOld = oldList.get(i);
                Enterprise t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Enterprise myNew = newList.get(i);
                Enterprise t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private AiQuotaAdminService aiQuotaService ;
    @Autowired
    private AiUsageLogAdminService aiUsageLogService ;
    @Autowired
    private ServiceProviderAdminService serviceProviderService ;
    @Autowired
    private CurrencyAdminService currencyService ;
    @Autowired
    private FinancialReportAdminService financialReportService ;
    @Autowired
    private EnterpriseMembershipAdminService enterpriseMembershipService ;
    @Autowired
    private PropertyAdminService propertyService ;
    @Autowired
    private ClientAdminService clientService ;

    public EnterpriseAdminServiceImpl(EnterpriseDao dao) {
        this.dao = dao;
    }

    private EnterpriseDao dao;
}
