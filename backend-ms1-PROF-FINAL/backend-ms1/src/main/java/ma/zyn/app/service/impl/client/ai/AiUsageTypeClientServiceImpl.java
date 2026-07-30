package ma.zyn.app.service.impl.client.ai;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.ai.AiUsageType;
import ma.zyn.app.dao.criteria.core.ai.AiUsageTypeCriteria;
import ma.zyn.app.dao.facade.core.ai.AiUsageTypeDao;
import ma.zyn.app.dao.specification.core.ai.AiUsageTypeSpecification;
import ma.zyn.app.service.facade.client.ai.AiUsageTypeClientService;
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


import java.util.List;
@Service
public class AiUsageTypeClientServiceImpl implements AiUsageTypeClientService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public AiUsageType update(AiUsageType t) {
        AiUsageType loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{AiUsageType.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public AiUsageType findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public AiUsageType findOrSave(AiUsageType t) {
        if (t != null) {
            AiUsageType result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<AiUsageType> findAll() {
        return dao.findAll();
    }

    public List<AiUsageType> findByCriteria(AiUsageTypeCriteria criteria) {
        List<AiUsageType> content = null;
        if (criteria != null) {
            AiUsageTypeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private AiUsageTypeSpecification constructSpecification(AiUsageTypeCriteria criteria) {
        AiUsageTypeSpecification mySpecification =  (AiUsageTypeSpecification) RefelexivityUtil.constructObjectUsingOneParam(AiUsageTypeSpecification.class, criteria);
        return mySpecification;
    }

    public List<AiUsageType> findPaginatedByCriteria(AiUsageTypeCriteria criteria, int page, int pageSize, String order, String sortField) {
        AiUsageTypeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(AiUsageTypeCriteria criteria) {
        AiUsageTypeSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
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
    public List<AiUsageType> delete(List<AiUsageType> list) {
		List<AiUsageType> result = new ArrayList();
        if (list != null) {
            for (AiUsageType t : list) {
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
    public AiUsageType create(AiUsageType t) {
        AiUsageType loaded = findByReferenceEntity(t);
        AiUsageType saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public AiUsageType findWithAssociatedLists(Long id){
        AiUsageType result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<AiUsageType> update(List<AiUsageType> ts, boolean createIfNotExist) {
        List<AiUsageType> result = new ArrayList<>();
        if (ts != null) {
            for (AiUsageType t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    AiUsageType loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, AiUsageType t, AiUsageType loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public AiUsageType findByReferenceEntity(AiUsageType t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<AiUsageType> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<AiUsageType>> getToBeSavedAndToBeDeleted(List<AiUsageType> oldList, List<AiUsageType> newList) {
        List<List<AiUsageType>> result = new ArrayList<>();
        List<AiUsageType> resultDelete = new ArrayList<>();
        List<AiUsageType> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<AiUsageType> oldList, List<AiUsageType> newList, List<AiUsageType> resultUpdateOrSave, List<AiUsageType> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                AiUsageType myOld = oldList.get(i);
                AiUsageType t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                AiUsageType myNew = newList.get(i);
                AiUsageType t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public AiUsageTypeClientServiceImpl(AiUsageTypeDao dao) {
        this.dao = dao;
    }

    private AiUsageTypeDao dao;
}
