package ma.zyn.app.service.impl.admin.payment;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.payment.PaymentStatus;
import ma.zyn.app.dao.criteria.core.payment.PaymentStatusCriteria;
import ma.zyn.app.dao.facade.core.payment.PaymentStatusDao;
import ma.zyn.app.dao.specification.core.payment.PaymentStatusSpecification;
import ma.zyn.app.service.facade.admin.payment.PaymentStatusAdminService;
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
public class PaymentStatusAdminServiceImpl implements PaymentStatusAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public PaymentStatus update(PaymentStatus t) {
        PaymentStatus loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{PaymentStatus.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public PaymentStatus findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public PaymentStatus findOrSave(PaymentStatus t) {
        if (t != null) {
            PaymentStatus result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<PaymentStatus> findAll() {
        return dao.findAll();
    }

    public List<PaymentStatus> findByCriteria(PaymentStatusCriteria criteria) {
        List<PaymentStatus> content = null;
        if (criteria != null) {
            PaymentStatusSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private PaymentStatusSpecification constructSpecification(PaymentStatusCriteria criteria) {
        PaymentStatusSpecification mySpecification =  (PaymentStatusSpecification) RefelexivityUtil.constructObjectUsingOneParam(PaymentStatusSpecification.class, criteria);
        return mySpecification;
    }

    public List<PaymentStatus> findPaginatedByCriteria(PaymentStatusCriteria criteria, int page, int pageSize, String order, String sortField) {
        PaymentStatusSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(PaymentStatusCriteria criteria) {
        PaymentStatusSpecification mySpecification = constructSpecification(criteria);
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
    public List<PaymentStatus> delete(List<PaymentStatus> list) {
		List<PaymentStatus> result = new ArrayList();
        if (list != null) {
            for (PaymentStatus t : list) {
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
    public PaymentStatus create(PaymentStatus t) {
        PaymentStatus loaded = findByReferenceEntity(t);
        PaymentStatus saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public PaymentStatus findWithAssociatedLists(Long id){
        PaymentStatus result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<PaymentStatus> update(List<PaymentStatus> ts, boolean createIfNotExist) {
        List<PaymentStatus> result = new ArrayList<>();
        if (ts != null) {
            for (PaymentStatus t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    PaymentStatus loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, PaymentStatus t, PaymentStatus loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public PaymentStatus findByReferenceEntity(PaymentStatus t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<PaymentStatus> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<PaymentStatus>> getToBeSavedAndToBeDeleted(List<PaymentStatus> oldList, List<PaymentStatus> newList) {
        List<List<PaymentStatus>> result = new ArrayList<>();
        List<PaymentStatus> resultDelete = new ArrayList<>();
        List<PaymentStatus> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<PaymentStatus> oldList, List<PaymentStatus> newList, List<PaymentStatus> resultUpdateOrSave, List<PaymentStatus> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                PaymentStatus myOld = oldList.get(i);
                PaymentStatus t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                PaymentStatus myNew = newList.get(i);
                PaymentStatus t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public PaymentStatusAdminServiceImpl(PaymentStatusDao dao) {
        this.dao = dao;
    }

    private PaymentStatusDao dao;
}
