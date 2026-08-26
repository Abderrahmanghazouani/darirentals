package ma.zyn.app.service.impl.collaborator.report;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.dao.criteria.core.report.FinancialReportCriteria;
import ma.zyn.app.dao.facade.core.report.FinancialReportDao;
import ma.zyn.app.dao.specification.core.report.FinancialReportSpecification;
import ma.zyn.app.service.facade.collaborator.report.FinancialReportCollaboratorService;
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
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import ma.zyn.app.service.facade.collaborator.auth.CollaboratorCollaboratorService ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.service.facade.collaborator.report.FinancialReportTypeCollaboratorService ;
import ma.zyn.app.bean.core.report.FinancialReportType ;
import ma.zyn.app.service.facade.collaborator.report.FinancialReportPropertyCollaboratorService ;
import ma.zyn.app.bean.core.report.FinancialReportProperty ;
import ma.zyn.app.service.facade.collaborator.enterprise.EnterpriseCollaboratorService ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.service.facade.collaborator.report.FinancialReportScopeCollaboratorService ;
import ma.zyn.app.bean.core.report.FinancialReportScope ;

import java.util.List;
@Service
public class FinancialReportCollaboratorServiceImpl implements FinancialReportCollaboratorService {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Un FinancialReport est FIGE une fois genere (voir FinancialReportGenerationService et
     * NOTES-rapports-financiers.md) : toute tentative de modification est silencieusement
     * ignoree, l'entite existante est renvoyee inchangee. Seul un id inconnu produit une erreur.
     *
     * Voir FinancialReportAdminServiceImpl.update() pour l'explication complete : le controleur
     * generique mute deja l'entite geree (meme instance, cache de 1er niveau Hibernate) avant
     * d'appeler cette methode, donc un simple "retourner l'entite rechargee" ne suffit pas -
     * seul entityManager.refresh() ecrase l'etat memoire par l'etat reel en base.
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public FinancialReport update(FinancialReport t) {
        FinancialReport loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{FinancialReport.class.getSimpleName(), t.getId().toString()});
        }
        entityManager.refresh(loadedItem);
        return loadedItem;
    }

    public FinancialReport findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public FinancialReport findOrSave(FinancialReport t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            FinancialReport result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<FinancialReport> findAll() {
        return dao.findAll();
    }

    public List<FinancialReport> findByCriteria(FinancialReportCriteria criteria) {
        List<FinancialReport> content = null;
        if (criteria != null) {
            FinancialReportSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private FinancialReportSpecification constructSpecification(FinancialReportCriteria criteria) {
        FinancialReportSpecification mySpecification =  (FinancialReportSpecification) RefelexivityUtil.constructObjectUsingOneParam(FinancialReportSpecification.class, criteria);
        return mySpecification;
    }

    public List<FinancialReport> findPaginatedByCriteria(FinancialReportCriteria criteria, int page, int pageSize, String order, String sortField) {
        FinancialReportSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(FinancialReportCriteria criteria) {
        FinancialReportSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<FinancialReport> findByFinancialReportTypeCode(String code){
        return dao.findByFinancialReportTypeCode(code);
    }
    public List<FinancialReport> findByFinancialReportTypeId(Long id){
        return dao.findByFinancialReportTypeId(id);
    }
    public int deleteByFinancialReportTypeCode(String code){
        return dao.deleteByFinancialReportTypeCode(code);
    }
    public int deleteByFinancialReportTypeId(Long id){
        return dao.deleteByFinancialReportTypeId(id);
    }
    public long countByFinancialReportTypeCode(String code){
        return dao.countByFinancialReportTypeCode(code);
    }
    public List<FinancialReport> findByFinancialReportScopeCode(String code){
        return dao.findByFinancialReportScopeCode(code);
    }
    public List<FinancialReport> findByFinancialReportScopeId(Long id){
        return dao.findByFinancialReportScopeId(id);
    }
    public int deleteByFinancialReportScopeCode(String code){
        return dao.deleteByFinancialReportScopeCode(code);
    }
    public int deleteByFinancialReportScopeId(Long id){
        return dao.deleteByFinancialReportScopeId(id);
    }
    public long countByFinancialReportScopeCode(String code){
        return dao.countByFinancialReportScopeCode(code);
    }
    public List<FinancialReport> findByEnterpriseId(Long id){
        return dao.findByEnterpriseId(id);
    }
    public int deleteByEnterpriseId(Long id){
        return dao.deleteByEnterpriseId(id);
    }
    public long countByEnterpriseId(Long id){
        return dao.countByEnterpriseId(id);
    }
    public List<FinancialReport> findByGeneratedById(Long id){
        return dao.findByGeneratedById(id);
    }
    public int deleteByGeneratedById(Long id){
        return dao.deleteByGeneratedById(id);
    }
    public long countByGeneratedByEmail(String email){
        return dao.countByGeneratedByEmail(email);
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
        financialReportPropertyService.deleteByFinancialReportId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<FinancialReport> delete(List<FinancialReport> list) {
		List<FinancialReport> result = new ArrayList();
        if (list != null) {
            for (FinancialReport t : list) {
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
    public FinancialReport create(FinancialReport t) {
        FinancialReport loaded = findByReferenceEntity(t);
        FinancialReport saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getFinancialReportProperties() != null) {
                t.getFinancialReportProperties().forEach(element-> {
                    element.setFinancialReport(saved);
                    financialReportPropertyService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public FinancialReport findWithAssociatedLists(Long id){
        FinancialReport result = dao.findById(id).orElse(null);
        if(result!=null && result.getId() != null) {
            result.setFinancialReportProperties(financialReportPropertyService.findByFinancialReportId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<FinancialReport> update(List<FinancialReport> ts, boolean createIfNotExist) {
        List<FinancialReport> result = new ArrayList<>();
        if (ts != null) {
            for (FinancialReport t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    FinancialReport loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, FinancialReport t, FinancialReport loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(FinancialReport financialReport){
    if(financialReport !=null && financialReport.getId() != null){
        List<List<FinancialReportProperty>> resultFinancialReportProperties= financialReportPropertyService.getToBeSavedAndToBeDeleted(financialReportPropertyService.findByFinancialReportId(financialReport.getId()),financialReport.getFinancialReportProperties());
            financialReportPropertyService.delete(resultFinancialReportProperties.get(1));
        emptyIfNull(resultFinancialReportProperties.get(0)).forEach(e -> e.setFinancialReport(financialReport));
        financialReportPropertyService.update(resultFinancialReportProperties.get(0),true);
        }
    }








    public FinancialReport findByReferenceEntity(FinancialReport t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(FinancialReport t){
        if( t != null) {
            t.setFinancialReportType(financialReportTypeService.findOrSave(t.getFinancialReportType()));
            t.setFinancialReportScope(financialReportScopeService.findOrSave(t.getFinancialReportScope()));
            t.setEnterprise(enterpriseService.findOrSave(t.getEnterprise()));
            t.setGeneratedBy(collaboratorService.findOrSave(t.getGeneratedBy()));
        }
    }



    public List<FinancialReport> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<FinancialReport>> getToBeSavedAndToBeDeleted(List<FinancialReport> oldList, List<FinancialReport> newList) {
        List<List<FinancialReport>> result = new ArrayList<>();
        List<FinancialReport> resultDelete = new ArrayList<>();
        List<FinancialReport> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<FinancialReport> oldList, List<FinancialReport> newList, List<FinancialReport> resultUpdateOrSave, List<FinancialReport> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                FinancialReport myOld = oldList.get(i);
                FinancialReport t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                FinancialReport myNew = newList.get(i);
                FinancialReport t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private CollaboratorCollaboratorService collaboratorService ;
    @Autowired
    private FinancialReportTypeCollaboratorService financialReportTypeService ;
    @Autowired
    private FinancialReportPropertyCollaboratorService financialReportPropertyService ;
    @Autowired
    private EnterpriseCollaboratorService enterpriseService ;
    @Autowired
    private FinancialReportScopeCollaboratorService financialReportScopeService ;

    public FinancialReportCollaboratorServiceImpl(FinancialReportDao dao) {
        this.dao = dao;
    }

    private FinancialReportDao dao;
}
