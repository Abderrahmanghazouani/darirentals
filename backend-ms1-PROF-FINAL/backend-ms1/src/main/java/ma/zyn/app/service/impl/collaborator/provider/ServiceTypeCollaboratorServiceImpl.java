package ma.zyn.app.service.impl.collaborator.provider;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.provider.ServiceType;
import ma.zyn.app.dao.criteria.core.provider.ServiceTypeCriteria;
import ma.zyn.app.dao.facade.core.provider.ServiceTypeDao;
import ma.zyn.app.dao.specification.core.provider.ServiceTypeSpecification;
import ma.zyn.app.service.facade.collaborator.provider.ServiceTypeCollaboratorService;
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
public class ServiceTypeCollaboratorServiceImpl implements ServiceTypeCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public ServiceType update(ServiceType t) {
        ServiceType loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{ServiceType.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public ServiceType findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public ServiceType findOrSave(ServiceType t) {
        if (t != null) {
            ServiceType result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<ServiceType> findAll() {
        return dao.findAll();
    }

    public List<ServiceType> findByCriteria(ServiceTypeCriteria criteria) {
        List<ServiceType> content = null;
        if (criteria != null) {
            ServiceTypeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ServiceTypeSpecification constructSpecification(ServiceTypeCriteria criteria) {
        ServiceTypeSpecification mySpecification =  (ServiceTypeSpecification) RefelexivityUtil.constructObjectUsingOneParam(ServiceTypeSpecification.class, criteria);
        return mySpecification;
    }

    public List<ServiceType> findPaginatedByCriteria(ServiceTypeCriteria criteria, int page, int pageSize, String order, String sortField) {
        ServiceTypeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ServiceTypeCriteria criteria) {
        ServiceTypeSpecification mySpecification = constructSpecification(criteria);
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
    public List<ServiceType> delete(List<ServiceType> list) {
		List<ServiceType> result = new ArrayList();
        if (list != null) {
            for (ServiceType t : list) {
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
    public ServiceType create(ServiceType t) {
        ServiceType loaded = findByReferenceEntity(t);
        ServiceType saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public ServiceType findWithAssociatedLists(Long id){
        ServiceType result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<ServiceType> update(List<ServiceType> ts, boolean createIfNotExist) {
        List<ServiceType> result = new ArrayList<>();
        if (ts != null) {
            for (ServiceType t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    ServiceType loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, ServiceType t, ServiceType loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public ServiceType findByReferenceEntity(ServiceType t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<ServiceType> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<ServiceType>> getToBeSavedAndToBeDeleted(List<ServiceType> oldList, List<ServiceType> newList) {
        List<List<ServiceType>> result = new ArrayList<>();
        List<ServiceType> resultDelete = new ArrayList<>();
        List<ServiceType> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<ServiceType> oldList, List<ServiceType> newList, List<ServiceType> resultUpdateOrSave, List<ServiceType> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                ServiceType myOld = oldList.get(i);
                ServiceType t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                ServiceType myNew = newList.get(i);
                ServiceType t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public ServiceTypeCollaboratorServiceImpl(ServiceTypeDao dao) {
        this.dao = dao;
    }

    private ServiceTypeDao dao;
}
