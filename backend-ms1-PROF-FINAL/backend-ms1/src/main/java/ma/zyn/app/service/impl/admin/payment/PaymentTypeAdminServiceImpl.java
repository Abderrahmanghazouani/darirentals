package ma.zyn.app.service.impl.admin.payment;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.payment.PaymentType;
import ma.zyn.app.dao.criteria.core.payment.PaymentTypeCriteria;
import ma.zyn.app.dao.facade.core.payment.PaymentTypeDao;
import ma.zyn.app.dao.specification.core.payment.PaymentTypeSpecification;
import ma.zyn.app.service.facade.admin.payment.PaymentTypeAdminService;
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
public class PaymentTypeAdminServiceImpl implements PaymentTypeAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public PaymentType update(PaymentType t) {
        PaymentType loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{PaymentType.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public PaymentType findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public PaymentType findOrSave(PaymentType t) {
        if (t != null) {
            PaymentType result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<PaymentType> findAll() {
        return dao.findAll();
    }

    public List<PaymentType> findByCriteria(PaymentTypeCriteria criteria) {
        List<PaymentType> content = null;
        if (criteria != null) {
            PaymentTypeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private PaymentTypeSpecification constructSpecification(PaymentTypeCriteria criteria) {
        PaymentTypeSpecification mySpecification =  (PaymentTypeSpecification) RefelexivityUtil.constructObjectUsingOneParam(PaymentTypeSpecification.class, criteria);
        return mySpecification;
    }

    public List<PaymentType> findPaginatedByCriteria(PaymentTypeCriteria criteria, int page, int pageSize, String order, String sortField) {
        PaymentTypeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(PaymentTypeCriteria criteria) {
        PaymentTypeSpecification mySpecification = constructSpecification(criteria);
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
    public List<PaymentType> delete(List<PaymentType> list) {
		List<PaymentType> result = new ArrayList();
        if (list != null) {
            for (PaymentType t : list) {
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
    public PaymentType create(PaymentType t) {
        PaymentType loaded = findByReferenceEntity(t);
        PaymentType saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public PaymentType findWithAssociatedLists(Long id){
        PaymentType result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<PaymentType> update(List<PaymentType> ts, boolean createIfNotExist) {
        List<PaymentType> result = new ArrayList<>();
        if (ts != null) {
            for (PaymentType t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    PaymentType loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, PaymentType t, PaymentType loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public PaymentType findByReferenceEntity(PaymentType t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<PaymentType> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<PaymentType>> getToBeSavedAndToBeDeleted(List<PaymentType> oldList, List<PaymentType> newList) {
        List<List<PaymentType>> result = new ArrayList<>();
        List<PaymentType> resultDelete = new ArrayList<>();
        List<PaymentType> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<PaymentType> oldList, List<PaymentType> newList, List<PaymentType> resultUpdateOrSave, List<PaymentType> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                PaymentType myOld = oldList.get(i);
                PaymentType t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                PaymentType myNew = newList.get(i);
                PaymentType t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public PaymentTypeAdminServiceImpl(PaymentTypeDao dao) {
        this.dao = dao;
    }

    private PaymentTypeDao dao;
}
