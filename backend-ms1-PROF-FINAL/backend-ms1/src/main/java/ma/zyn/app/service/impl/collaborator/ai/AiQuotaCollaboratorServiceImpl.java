package ma.zyn.app.service.impl.collaborator.ai;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.ai.AiQuota;
import ma.zyn.app.dao.criteria.core.ai.AiQuotaCriteria;
import ma.zyn.app.dao.facade.core.ai.AiQuotaDao;
import ma.zyn.app.dao.specification.core.ai.AiQuotaSpecification;
import ma.zyn.app.service.facade.collaborator.ai.AiQuotaCollaboratorService;
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

import ma.zyn.app.service.facade.collaborator.enterprise.EnterpriseCollaboratorService ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;

import java.util.List;
@Service
public class AiQuotaCollaboratorServiceImpl implements AiQuotaCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public AiQuota update(AiQuota t) {
        AiQuota loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{AiQuota.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public AiQuota findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public AiQuota findOrSave(AiQuota t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            AiQuota result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<AiQuota> findAll() {
        return dao.findAll();
    }

    public List<AiQuota> findByCriteria(AiQuotaCriteria criteria) {
        List<AiQuota> content = null;
        if (criteria != null) {
            AiQuotaSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private AiQuotaSpecification constructSpecification(AiQuotaCriteria criteria) {
        AiQuotaSpecification mySpecification =  (AiQuotaSpecification) RefelexivityUtil.constructObjectUsingOneParam(AiQuotaSpecification.class, criteria);
        return mySpecification;
    }

    public List<AiQuota> findPaginatedByCriteria(AiQuotaCriteria criteria, int page, int pageSize, String order, String sortField) {
        AiQuotaSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(AiQuotaCriteria criteria) {
        AiQuotaSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<AiQuota> findByEnterpriseId(Long id){
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
            dao.deleteById(id);
        }
        return condition;
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<AiQuota> delete(List<AiQuota> list) {
		List<AiQuota> result = new ArrayList();
        if (list != null) {
            for (AiQuota t : list) {
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
    public AiQuota create(AiQuota t) {
        AiQuota loaded = findByReferenceEntity(t);
        AiQuota saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public AiQuota findWithAssociatedLists(Long id){
        AiQuota result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<AiQuota> update(List<AiQuota> ts, boolean createIfNotExist) {
        List<AiQuota> result = new ArrayList<>();
        if (ts != null) {
            for (AiQuota t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    AiQuota loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, AiQuota t, AiQuota loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public AiQuota findByReferenceEntity(AiQuota t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(AiQuota t){
        if( t != null) {
            t.setEnterprise(enterpriseService.findOrSave(t.getEnterprise()));
        }
    }



    public List<AiQuota> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<AiQuota>> getToBeSavedAndToBeDeleted(List<AiQuota> oldList, List<AiQuota> newList) {
        List<List<AiQuota>> result = new ArrayList<>();
        List<AiQuota> resultDelete = new ArrayList<>();
        List<AiQuota> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<AiQuota> oldList, List<AiQuota> newList, List<AiQuota> resultUpdateOrSave, List<AiQuota> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                AiQuota myOld = oldList.get(i);
                AiQuota t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                AiQuota myNew = newList.get(i);
                AiQuota t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private EnterpriseCollaboratorService enterpriseService ;

    public AiQuotaCollaboratorServiceImpl(AiQuotaDao dao) {
        this.dao = dao;
    }

    private AiQuotaDao dao;
}
