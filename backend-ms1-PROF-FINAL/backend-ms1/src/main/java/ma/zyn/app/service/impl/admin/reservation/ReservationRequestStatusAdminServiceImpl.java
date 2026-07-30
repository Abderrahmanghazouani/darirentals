package ma.zyn.app.service.impl.admin.reservation;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.reservation.ReservationRequestStatus;
import ma.zyn.app.dao.criteria.core.reservation.ReservationRequestStatusCriteria;
import ma.zyn.app.dao.facade.core.reservation.ReservationRequestStatusDao;
import ma.zyn.app.dao.specification.core.reservation.ReservationRequestStatusSpecification;
import ma.zyn.app.service.facade.admin.reservation.ReservationRequestStatusAdminService;
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
public class ReservationRequestStatusAdminServiceImpl implements ReservationRequestStatusAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ReservationRequestStatus update(ReservationRequestStatus t) {
        ReservationRequestStatus loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{ReservationRequestStatus.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public ReservationRequestStatus findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public ReservationRequestStatus findOrSave(ReservationRequestStatus t) {
        if (t != null) {
            ReservationRequestStatus result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<ReservationRequestStatus> findAll() {
        return dao.findAll();
    }

    public List<ReservationRequestStatus> findByCriteria(ReservationRequestStatusCriteria criteria) {
        List<ReservationRequestStatus> content = null;
        if (criteria != null) {
            ReservationRequestStatusSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ReservationRequestStatusSpecification constructSpecification(ReservationRequestStatusCriteria criteria) {
        ReservationRequestStatusSpecification mySpecification =  (ReservationRequestStatusSpecification) RefelexivityUtil.constructObjectUsingOneParam(ReservationRequestStatusSpecification.class, criteria);
        return mySpecification;
    }

    public List<ReservationRequestStatus> findPaginatedByCriteria(ReservationRequestStatusCriteria criteria, int page, int pageSize, String order, String sortField) {
        ReservationRequestStatusSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ReservationRequestStatusCriteria criteria) {
        ReservationRequestStatusSpecification mySpecification = constructSpecification(criteria);
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
    public List<ReservationRequestStatus> delete(List<ReservationRequestStatus> list) {
		List<ReservationRequestStatus> result = new ArrayList();
        if (list != null) {
            for (ReservationRequestStatus t : list) {
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
    public ReservationRequestStatus create(ReservationRequestStatus t) {
        ReservationRequestStatus loaded = findByReferenceEntity(t);
        ReservationRequestStatus saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public ReservationRequestStatus findWithAssociatedLists(Long id){
        ReservationRequestStatus result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ReservationRequestStatus> update(List<ReservationRequestStatus> ts, boolean createIfNotExist) {
        List<ReservationRequestStatus> result = new ArrayList<>();
        if (ts != null) {
            for (ReservationRequestStatus t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    ReservationRequestStatus loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, ReservationRequestStatus t, ReservationRequestStatus loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public ReservationRequestStatus findByReferenceEntity(ReservationRequestStatus t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<ReservationRequestStatus> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<ReservationRequestStatus>> getToBeSavedAndToBeDeleted(List<ReservationRequestStatus> oldList, List<ReservationRequestStatus> newList) {
        List<List<ReservationRequestStatus>> result = new ArrayList<>();
        List<ReservationRequestStatus> resultDelete = new ArrayList<>();
        List<ReservationRequestStatus> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<ReservationRequestStatus> oldList, List<ReservationRequestStatus> newList, List<ReservationRequestStatus> resultUpdateOrSave, List<ReservationRequestStatus> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                ReservationRequestStatus myOld = oldList.get(i);
                ReservationRequestStatus t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                ReservationRequestStatus myNew = newList.get(i);
                ReservationRequestStatus t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public ReservationRequestStatusAdminServiceImpl(ReservationRequestStatusDao dao) {
        this.dao = dao;
    }

    private ReservationRequestStatusDao dao;
}
