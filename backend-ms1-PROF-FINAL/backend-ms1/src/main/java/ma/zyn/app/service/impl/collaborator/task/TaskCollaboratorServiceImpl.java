package ma.zyn.app.service.impl.collaborator.task;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.task.Task;
import ma.zyn.app.dao.criteria.core.task.TaskCriteria;
import ma.zyn.app.dao.facade.core.task.TaskDao;
import ma.zyn.app.dao.specification.core.task.TaskSpecification;
import ma.zyn.app.service.facade.collaborator.task.TaskCollaboratorService;
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

import ma.zyn.app.service.facade.collaborator.auth.CollaboratorCollaboratorService ;
import ma.zyn.app.bean.core.auth.Collaborator ;
import ma.zyn.app.service.facade.collaborator.provider.ServiceProviderCollaboratorService ;
import ma.zyn.app.bean.core.provider.ServiceProvider ;
import ma.zyn.app.service.facade.collaborator.reservation.ReservationCollaboratorService ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.service.facade.collaborator.task.TaskPriorityCollaboratorService ;
import ma.zyn.app.bean.core.task.TaskPriority ;
import ma.zyn.app.service.facade.collaborator.task.TaskTypeCollaboratorService ;
import ma.zyn.app.bean.core.task.TaskType ;
import ma.zyn.app.service.facade.collaborator.property.PropertyCollaboratorService ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.service.facade.collaborator.task.TaskStatusCollaboratorService ;
import ma.zyn.app.bean.core.task.TaskStatus ;

import java.util.List;
@Service
public class TaskCollaboratorServiceImpl implements TaskCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Task update(Task t) {
        Task loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Task.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public Task findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public Task findOrSave(Task t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Task result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Task> findAll() {
        return dao.findAll();
    }

    public List<Task> findByCriteria(TaskCriteria criteria) {
        List<Task> content = null;
        if (criteria != null) {
            TaskSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private TaskSpecification constructSpecification(TaskCriteria criteria) {
        TaskSpecification mySpecification =  (TaskSpecification) RefelexivityUtil.constructObjectUsingOneParam(TaskSpecification.class, criteria);
        return mySpecification;
    }

    public List<Task> findPaginatedByCriteria(TaskCriteria criteria, int page, int pageSize, String order, String sortField) {
        TaskSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(TaskCriteria criteria) {
        TaskSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<Task> findByPropertyId(Long id){
        return dao.findByPropertyId(id);
    }
    public int deleteByPropertyId(Long id){
        return dao.deleteByPropertyId(id);
    }
    public long countByPropertyId(Long id){
        return dao.countByPropertyId(id);
    }
    public List<Task> findByReservationId(Long id){
        return dao.findByReservationId(id);
    }
    public int deleteByReservationId(Long id){
        return dao.deleteByReservationId(id);
    }
    public long countByReservationReference(String reference){
        return dao.countByReservationReference(reference);
    }
    public List<Task> findByServiceProviderId(Long id){
        return dao.findByServiceProviderId(id);
    }
    public int deleteByServiceProviderId(Long id){
        return dao.deleteByServiceProviderId(id);
    }
    public long countByServiceProviderId(Long id){
        return dao.countByServiceProviderId(id);
    }
    public List<Task> findByAssignedToId(Long id){
        return dao.findByAssignedToId(id);
    }
    public int deleteByAssignedToId(Long id){
        return dao.deleteByAssignedToId(id);
    }
    public long countByAssignedToEmail(String email){
        return dao.countByAssignedToEmail(email);
    }
    public List<Task> findByTaskTypeCode(String code){
        return dao.findByTaskTypeCode(code);
    }
    public List<Task> findByTaskTypeId(Long id){
        return dao.findByTaskTypeId(id);
    }
    public int deleteByTaskTypeCode(String code){
        return dao.deleteByTaskTypeCode(code);
    }
    public int deleteByTaskTypeId(Long id){
        return dao.deleteByTaskTypeId(id);
    }
    public long countByTaskTypeCode(String code){
        return dao.countByTaskTypeCode(code);
    }
    public List<Task> findByTaskPriorityCode(String code){
        return dao.findByTaskPriorityCode(code);
    }
    public List<Task> findByTaskPriorityId(Long id){
        return dao.findByTaskPriorityId(id);
    }
    public int deleteByTaskPriorityCode(String code){
        return dao.deleteByTaskPriorityCode(code);
    }
    public int deleteByTaskPriorityId(Long id){
        return dao.deleteByTaskPriorityId(id);
    }
    public long countByTaskPriorityCode(String code){
        return dao.countByTaskPriorityCode(code);
    }
    public List<Task> findByTaskStatusCode(String code){
        return dao.findByTaskStatusCode(code);
    }
    public List<Task> findByTaskStatusId(Long id){
        return dao.findByTaskStatusId(id);
    }
    public int deleteByTaskStatusCode(String code){
        return dao.deleteByTaskStatusCode(code);
    }
    public int deleteByTaskStatusId(Long id){
        return dao.deleteByTaskStatusId(id);
    }
    public long countByTaskStatusCode(String code){
        return dao.countByTaskStatusCode(code);
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
    public List<Task> delete(List<Task> list) {
		List<Task> result = new ArrayList();
        if (list != null) {
            for (Task t : list) {
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
    public Task create(Task t) {
        Task loaded = findByReferenceEntity(t);
        Task saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public Task findWithAssociatedLists(Long id){
        Task result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Task> update(List<Task> ts, boolean createIfNotExist) {
        List<Task> result = new ArrayList<>();
        if (ts != null) {
            for (Task t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Task loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Task t, Task loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public Task findByReferenceEntity(Task t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(Task t){
        if( t != null) {
            t.setProperty(propertyService.findOrSave(t.getProperty()));
            t.setReservation(reservationService.findOrSave(t.getReservation()));
            t.setServiceProvider(serviceProviderService.findOrSave(t.getServiceProvider()));
            t.setAssignedTo(collaboratorService.findOrSave(t.getAssignedTo()));
            t.setTaskType(taskTypeService.findOrSave(t.getTaskType()));
            t.setTaskPriority(taskPriorityService.findOrSave(t.getTaskPriority()));
            t.setTaskStatus(taskStatusService.findOrSave(t.getTaskStatus()));
        }
    }



    public List<Task> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<Task>> getToBeSavedAndToBeDeleted(List<Task> oldList, List<Task> newList) {
        List<List<Task>> result = new ArrayList<>();
        List<Task> resultDelete = new ArrayList<>();
        List<Task> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Task> oldList, List<Task> newList, List<Task> resultUpdateOrSave, List<Task> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Task myOld = oldList.get(i);
                Task t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Task myNew = newList.get(i);
                Task t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private CollaboratorCollaboratorService collaboratorService ;
    @Autowired
    private ServiceProviderCollaboratorService serviceProviderService ;
    @Autowired
    private ReservationCollaboratorService reservationService ;
    @Autowired
    private TaskPriorityCollaboratorService taskPriorityService ;
    @Autowired
    private TaskTypeCollaboratorService taskTypeService ;
    @Autowired
    private PropertyCollaboratorService propertyService ;
    @Autowired
    private TaskStatusCollaboratorService taskStatusService ;

    public TaskCollaboratorServiceImpl(TaskDao dao) {
        this.dao = dao;
    }

    private TaskDao dao;
}
