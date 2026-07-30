package ma.zyn.app.service.impl.collaborator.charge;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.charge.ChargeType;
import ma.zyn.app.dao.criteria.core.charge.ChargeTypeCriteria;
import ma.zyn.app.dao.facade.core.charge.ChargeTypeDao;
import ma.zyn.app.dao.specification.core.charge.ChargeTypeSpecification;
import ma.zyn.app.service.facade.collaborator.charge.ChargeTypeCollaboratorService;
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
public class ChargeTypeCollaboratorServiceImpl implements ChargeTypeCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ChargeType update(ChargeType t) {
        ChargeType loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{ChargeType.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public ChargeType findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public ChargeType findOrSave(ChargeType t) {
        if (t != null) {
            ChargeType result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<ChargeType> findAll() {
        return dao.findAll();
    }

    public List<ChargeType> findByCriteria(ChargeTypeCriteria criteria) {
        List<ChargeType> content = null;
        if (criteria != null) {
            ChargeTypeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ChargeTypeSpecification constructSpecification(ChargeTypeCriteria criteria) {
        ChargeTypeSpecification mySpecification =  (ChargeTypeSpecification) RefelexivityUtil.constructObjectUsingOneParam(ChargeTypeSpecification.class, criteria);
        return mySpecification;
    }

    public List<ChargeType> findPaginatedByCriteria(ChargeTypeCriteria criteria, int page, int pageSize, String order, String sortField) {
        ChargeTypeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ChargeTypeCriteria criteria) {
        ChargeTypeSpecification mySpecification = constructSpecification(criteria);
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
    public List<ChargeType> delete(List<ChargeType> list) {
		List<ChargeType> result = new ArrayList();
        if (list != null) {
            for (ChargeType t : list) {
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
    public ChargeType create(ChargeType t) {
        ChargeType loaded = findByReferenceEntity(t);
        ChargeType saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public ChargeType findWithAssociatedLists(Long id){
        ChargeType result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ChargeType> update(List<ChargeType> ts, boolean createIfNotExist) {
        List<ChargeType> result = new ArrayList<>();
        if (ts != null) {
            for (ChargeType t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    ChargeType loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, ChargeType t, ChargeType loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public ChargeType findByReferenceEntity(ChargeType t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<ChargeType> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<ChargeType>> getToBeSavedAndToBeDeleted(List<ChargeType> oldList, List<ChargeType> newList) {
        List<List<ChargeType>> result = new ArrayList<>();
        List<ChargeType> resultDelete = new ArrayList<>();
        List<ChargeType> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<ChargeType> oldList, List<ChargeType> newList, List<ChargeType> resultUpdateOrSave, List<ChargeType> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                ChargeType myOld = oldList.get(i);
                ChargeType t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                ChargeType myNew = newList.get(i);
                ChargeType t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public ChargeTypeCollaboratorServiceImpl(ChargeTypeDao dao) {
        this.dao = dao;
    }

    private ChargeTypeDao dao;
}
