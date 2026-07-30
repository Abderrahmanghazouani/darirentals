package ma.zyn.app.service.impl.admin.reservation;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.reservation.ReservationPlatform;
import ma.zyn.app.dao.criteria.core.reservation.ReservationPlatformCriteria;
import ma.zyn.app.dao.facade.core.reservation.ReservationPlatformDao;
import ma.zyn.app.dao.specification.core.reservation.ReservationPlatformSpecification;
import ma.zyn.app.service.facade.admin.reservation.ReservationPlatformAdminService;
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
public class ReservationPlatformAdminServiceImpl implements ReservationPlatformAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ReservationPlatform update(ReservationPlatform t) {
        ReservationPlatform loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{ReservationPlatform.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public ReservationPlatform findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public ReservationPlatform findOrSave(ReservationPlatform t) {
        if (t != null) {
            ReservationPlatform result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<ReservationPlatform> findAll() {
        return dao.findAll();
    }

    public List<ReservationPlatform> findByCriteria(ReservationPlatformCriteria criteria) {
        List<ReservationPlatform> content = null;
        if (criteria != null) {
            ReservationPlatformSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ReservationPlatformSpecification constructSpecification(ReservationPlatformCriteria criteria) {
        ReservationPlatformSpecification mySpecification =  (ReservationPlatformSpecification) RefelexivityUtil.constructObjectUsingOneParam(ReservationPlatformSpecification.class, criteria);
        return mySpecification;
    }

    public List<ReservationPlatform> findPaginatedByCriteria(ReservationPlatformCriteria criteria, int page, int pageSize, String order, String sortField) {
        ReservationPlatformSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ReservationPlatformCriteria criteria) {
        ReservationPlatformSpecification mySpecification = constructSpecification(criteria);
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
    public List<ReservationPlatform> delete(List<ReservationPlatform> list) {
		List<ReservationPlatform> result = new ArrayList();
        if (list != null) {
            for (ReservationPlatform t : list) {
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
    public ReservationPlatform create(ReservationPlatform t) {
        ReservationPlatform loaded = findByReferenceEntity(t);
        ReservationPlatform saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public ReservationPlatform findWithAssociatedLists(Long id){
        ReservationPlatform result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ReservationPlatform> update(List<ReservationPlatform> ts, boolean createIfNotExist) {
        List<ReservationPlatform> result = new ArrayList<>();
        if (ts != null) {
            for (ReservationPlatform t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    ReservationPlatform loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, ReservationPlatform t, ReservationPlatform loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public ReservationPlatform findByReferenceEntity(ReservationPlatform t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<ReservationPlatform> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<ReservationPlatform>> getToBeSavedAndToBeDeleted(List<ReservationPlatform> oldList, List<ReservationPlatform> newList) {
        List<List<ReservationPlatform>> result = new ArrayList<>();
        List<ReservationPlatform> resultDelete = new ArrayList<>();
        List<ReservationPlatform> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<ReservationPlatform> oldList, List<ReservationPlatform> newList, List<ReservationPlatform> resultUpdateOrSave, List<ReservationPlatform> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                ReservationPlatform myOld = oldList.get(i);
                ReservationPlatform t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                ReservationPlatform myNew = newList.get(i);
                ReservationPlatform t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public ReservationPlatformAdminServiceImpl(ReservationPlatformDao dao) {
        this.dao = dao;
    }

    private ReservationPlatformDao dao;
}
