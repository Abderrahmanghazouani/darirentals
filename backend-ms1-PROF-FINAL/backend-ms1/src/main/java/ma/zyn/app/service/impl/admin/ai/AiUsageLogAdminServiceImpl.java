package ma.zyn.app.service.impl.admin.ai;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.ai.AiUsageLog;
import ma.zyn.app.dao.criteria.core.ai.AiUsageLogCriteria;
import ma.zyn.app.dao.facade.core.ai.AiUsageLogDao;
import ma.zyn.app.dao.specification.core.ai.AiUsageLogSpecification;
import ma.zyn.app.service.facade.admin.ai.AiUsageLogAdminService;
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
import ma.zyn.app.service.facade.admin.ai.AiUsageTypeAdminService ;
import ma.zyn.app.bean.core.ai.AiUsageType ;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseAdminService ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.service.facade.admin.document.DocumentAdminService ;
import ma.zyn.app.bean.core.document.Document ;

import java.util.List;
@Service
public class AiUsageLogAdminServiceImpl implements AiUsageLogAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public AiUsageLog update(AiUsageLog t) {
        AiUsageLog loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{AiUsageLog.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public AiUsageLog findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public AiUsageLog findOrSave(AiUsageLog t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            AiUsageLog result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<AiUsageLog> findAll() {
        return dao.findAll();
    }

    public List<AiUsageLog> findByCriteria(AiUsageLogCriteria criteria) {
        List<AiUsageLog> content = null;
        if (criteria != null) {
            AiUsageLogSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private AiUsageLogSpecification constructSpecification(AiUsageLogCriteria criteria) {
        AiUsageLogSpecification mySpecification =  (AiUsageLogSpecification) RefelexivityUtil.constructObjectUsingOneParam(AiUsageLogSpecification.class, criteria);
        return mySpecification;
    }

    public List<AiUsageLog> findPaginatedByCriteria(AiUsageLogCriteria criteria, int page, int pageSize, String order, String sortField) {
        AiUsageLogSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(AiUsageLogCriteria criteria) {
        AiUsageLogSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<AiUsageLog> findByEnterpriseId(Long id){
        return dao.findByEnterpriseId(id);
    }
    public int deleteByEnterpriseId(Long id){
        return dao.deleteByEnterpriseId(id);
    }
    public long countByEnterpriseId(Long id){
        return dao.countByEnterpriseId(id);
    }
    public List<AiUsageLog> findByAiUsageTypeCode(String code){
        return dao.findByAiUsageTypeCode(code);
    }
    public List<AiUsageLog> findByAiUsageTypeId(Long id){
        return dao.findByAiUsageTypeId(id);
    }
    public int deleteByAiUsageTypeCode(String code){
        return dao.deleteByAiUsageTypeCode(code);
    }
    public int deleteByAiUsageTypeId(Long id){
        return dao.deleteByAiUsageTypeId(id);
    }
    public long countByAiUsageTypeCode(String code){
        return dao.countByAiUsageTypeCode(code);
    }
    public List<AiUsageLog> findByCollaboratorId(Long id){
        return dao.findByCollaboratorId(id);
    }
    public int deleteByCollaboratorId(Long id){
        return dao.deleteByCollaboratorId(id);
    }
    public long countByCollaboratorEmail(String email){
        return dao.countByCollaboratorEmail(email);
    }
    public List<AiUsageLog> findByDocumentId(Long id){
        return dao.findByDocumentId(id);
    }
    public int deleteByDocumentId(Long id){
        return dao.deleteByDocumentId(id);
    }
    public long countByDocumentId(Long id){
        return dao.countByDocumentId(id);
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
    public List<AiUsageLog> delete(List<AiUsageLog> list) {
		List<AiUsageLog> result = new ArrayList();
        if (list != null) {
            for (AiUsageLog t : list) {
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
    public AiUsageLog create(AiUsageLog t) {
        AiUsageLog loaded = findByReferenceEntity(t);
        AiUsageLog saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public AiUsageLog findWithAssociatedLists(Long id){
        AiUsageLog result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<AiUsageLog> update(List<AiUsageLog> ts, boolean createIfNotExist) {
        List<AiUsageLog> result = new ArrayList<>();
        if (ts != null) {
            for (AiUsageLog t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    AiUsageLog loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, AiUsageLog t, AiUsageLog loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public AiUsageLog findByReferenceEntity(AiUsageLog t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(AiUsageLog t){
        if( t != null) {
            t.setEnterprise(enterpriseService.findOrSave(t.getEnterprise()));
            t.setAiUsageType(aiUsageTypeService.findOrSave(t.getAiUsageType()));
            t.setCollaborator(collaboratorService.findOrSave(t.getCollaborator()));
            t.setDocument(documentService.findOrSave(t.getDocument()));
        }
    }



    public List<AiUsageLog> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<AiUsageLog>> getToBeSavedAndToBeDeleted(List<AiUsageLog> oldList, List<AiUsageLog> newList) {
        List<List<AiUsageLog>> result = new ArrayList<>();
        List<AiUsageLog> resultDelete = new ArrayList<>();
        List<AiUsageLog> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<AiUsageLog> oldList, List<AiUsageLog> newList, List<AiUsageLog> resultUpdateOrSave, List<AiUsageLog> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                AiUsageLog myOld = oldList.get(i);
                AiUsageLog t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                AiUsageLog myNew = newList.get(i);
                AiUsageLog t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private CollaboratorAdminService collaboratorService ;
    @Autowired
    private AiUsageTypeAdminService aiUsageTypeService ;
    @Autowired
    private EnterpriseAdminService enterpriseService ;
    @Autowired
    private DocumentAdminService documentService ;

    public AiUsageLogAdminServiceImpl(AiUsageLogDao dao) {
        this.dao = dao;
    }

    private AiUsageLogDao dao;
}
