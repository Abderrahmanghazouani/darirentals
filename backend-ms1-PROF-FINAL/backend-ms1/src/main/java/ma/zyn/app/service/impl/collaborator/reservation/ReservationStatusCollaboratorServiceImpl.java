package ma.zyn.app.service.impl.collaborator.reservation;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.reservation.ReservationStatus;
import ma.zyn.app.dao.criteria.core.reservation.ReservationStatusCriteria;
import ma.zyn.app.dao.facade.core.reservation.ReservationStatusDao;
import ma.zyn.app.dao.specification.core.reservation.ReservationStatusSpecification;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationStatusCollaboratorService;
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
public class ReservationStatusCollaboratorServiceImpl implements ReservationStatusCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ReservationStatus update(ReservationStatus t) {
        ReservationStatus loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{ReservationStatus.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public ReservationStatus findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public ReservationStatus findOrSave(ReservationStatus t) {
        if (t != null) {
            ReservationStatus result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<ReservationStatus> findAll() {
        return dao.findAll();
    }

    public List<ReservationStatus> findByCriteria(ReservationStatusCriteria criteria) {
        List<ReservationStatus> content = null;
        if (criteria != null) {
            ReservationStatusSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ReservationStatusSpecification constructSpecification(ReservationStatusCriteria criteria) {
        ReservationStatusSpecification mySpecification =  (ReservationStatusSpecification) RefelexivityUtil.constructObjectUsingOneParam(ReservationStatusSpecification.class, criteria);
        return mySpecification;
    }

    public List<ReservationStatus> findPaginatedByCriteria(ReservationStatusCriteria criteria, int page, int pageSize, String order, String sortField) {
        ReservationStatusSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ReservationStatusCriteria criteria) {
        ReservationStatusSpecification mySpecification = constructSpecification(criteria);
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
    public List<ReservationStatus> delete(List<ReservationStatus> list) {
		List<ReservationStatus> result = new ArrayList();
        if (list != null) {
            for (ReservationStatus t : list) {
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
    public ReservationStatus create(ReservationStatus t) {
        ReservationStatus loaded = findByReferenceEntity(t);
        ReservationStatus saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public ReservationStatus findWithAssociatedLists(Long id){
        ReservationStatus result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ReservationStatus> update(List<ReservationStatus> ts, boolean createIfNotExist) {
        List<ReservationStatus> result = new ArrayList<>();
        if (ts != null) {
            for (ReservationStatus t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    ReservationStatus loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, ReservationStatus t, ReservationStatus loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public ReservationStatus findByReferenceEntity(ReservationStatus t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<ReservationStatus> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<ReservationStatus>> getToBeSavedAndToBeDeleted(List<ReservationStatus> oldList, List<ReservationStatus> newList) {
        List<List<ReservationStatus>> result = new ArrayList<>();
        List<ReservationStatus> resultDelete = new ArrayList<>();
        List<ReservationStatus> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<ReservationStatus> oldList, List<ReservationStatus> newList, List<ReservationStatus> resultUpdateOrSave, List<ReservationStatus> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                ReservationStatus myOld = oldList.get(i);
                ReservationStatus t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                ReservationStatus myNew = newList.get(i);
                ReservationStatus t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public ReservationStatusCollaboratorServiceImpl(ReservationStatusDao dao) {
        this.dao = dao;
    }

    private ReservationStatusDao dao;
}
