package ma.zyn.app.service.impl.admin.auth;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.dao.criteria.core.auth.CollaboratorCriteria;
import ma.zyn.app.dao.facade.core.auth.CollaboratorDao;
import ma.zyn.app.dao.specification.core.auth.CollaboratorSpecification;
import ma.zyn.app.service.facade.admin.auth.CollaboratorAdminService;
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

import ma.zyn.app.service.facade.admin.ai.AiUsageLogAdminService ;
import ma.zyn.app.bean.core.ai.AiUsageLog ;
import ma.zyn.app.service.facade.admin.task.TaskAdminService ;
import ma.zyn.app.bean.core.task.Task ;
import ma.zyn.app.service.facade.admin.currency.CurrencyAdminService ;
import ma.zyn.app.bean.core.currency.Currency ;
import ma.zyn.app.service.facade.admin.reservation.ReservationRequestAdminService ;
import ma.zyn.app.bean.core.reservation.ReservationRequest ;
import ma.zyn.app.service.facade.admin.enterprise.EnterpriseMembershipAdminService ;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership ;

import java.time.LocalDateTime;
import ma.zyn.app.zynerator.security.service.facade.UserService;
import ma.zyn.app.zynerator.security.service.facade.RoleService;
import ma.zyn.app.zynerator.security.service.facade.RoleUserService;
import ma.zyn.app.zynerator.security.bean.Role;
import ma.zyn.app.zynerator.security.bean.RoleUser;
import ma.zyn.app.zynerator.security.common.AuthoritiesConstants;
import ma.zyn.app.zynerator.security.service.facade.ModelPermissionUserService;
import java.util.Collection;
import java.util.List;
@Service
public class CollaboratorAdminServiceImpl implements CollaboratorAdminService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Collaborator update(Collaborator t) {
        Collaborator loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Collaborator.class.getSimpleName(), t.getId().toString()});
        } else {
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    public Collaborator findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public Collaborator findOrSave(Collaborator t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Collaborator result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Collaborator> findAll() {
        return dao.findAll();
    }

    public List<Collaborator> findByCriteria(CollaboratorCriteria criteria) {
        List<Collaborator> content = null;
        if (criteria != null) {
            CollaboratorSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private CollaboratorSpecification constructSpecification(CollaboratorCriteria criteria) {
        CollaboratorSpecification mySpecification =  (CollaboratorSpecification) RefelexivityUtil.constructObjectUsingOneParam(CollaboratorSpecification.class, criteria);
        return mySpecification;
    }

    public List<Collaborator> findPaginatedByCriteria(CollaboratorCriteria criteria, int page, int pageSize, String order, String sortField) {
        CollaboratorSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(CollaboratorCriteria criteria) {
        CollaboratorSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<Collaborator> findByDisplayCurrencyCode(String code){
        return dao.findByDisplayCurrencyCode(code);
    }
    public List<Collaborator> findByDisplayCurrencyId(Long id){
        return dao.findByDisplayCurrencyId(id);
    }
    public int deleteByDisplayCurrencyCode(String code){
        return dao.deleteByDisplayCurrencyCode(code);
    }
    public int deleteByDisplayCurrencyId(Long id){
        return dao.deleteByDisplayCurrencyId(id);
    }
    public long countByDisplayCurrencyCode(String code){
        return dao.countByDisplayCurrencyCode(code);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            deleteAssociatedLists(id);
            dao.deleteById(id);
        }
        return condition;
    }

    public void deleteAssociatedLists(Long id) {
        enterpriseMembershipService.deleteByCollaboratorId(id);
        aiUsageLogService.deleteByCollaboratorId(id);
        taskService.deleteByAssignedToId(id);
        reservationRequestService.deleteByReviewedById(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Collaborator> delete(List<Collaborator> list) {
		List<Collaborator> result = new ArrayList();
        if (list != null) {
            for (Collaborator t : list) {
                if(dao.findById(t.getId()).isEmpty()){
					result.add(t);
				}else{
                    dao.deleteById(t.getId());
                }
            }
        }
		return result;
    }


    public Collaborator findWithAssociatedLists(Long id){
        Collaborator result = dao.findById(id).orElse(null);
        if(result!=null && result.getId() != null) {
            result.setEnterpriseMemberships(enterpriseMembershipService.findByCollaboratorId(id));
            result.setAiUsageLogs(aiUsageLogService.findByCollaboratorId(id));
            result.setTasks(taskService.findByAssignedToId(id));
            result.setReservationRequests(reservationRequestService.findByReviewedById(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Collaborator> update(List<Collaborator> ts, boolean createIfNotExist) {
        List<Collaborator> result = new ArrayList<>();
        if (ts != null) {
            for (Collaborator t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Collaborator loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Collaborator t, Collaborator loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Collaborator collaborator){
    if(collaborator !=null && collaborator.getId() != null){
        List<List<EnterpriseMembership>> resultEnterpriseMemberships= enterpriseMembershipService.getToBeSavedAndToBeDeleted(enterpriseMembershipService.findByCollaboratorId(collaborator.getId()),collaborator.getEnterpriseMemberships());
            enterpriseMembershipService.delete(resultEnterpriseMemberships.get(1));
        emptyIfNull(resultEnterpriseMemberships.get(0)).forEach(e -> e.setCollaborator(collaborator));
        enterpriseMembershipService.update(resultEnterpriseMemberships.get(0),true);
        List<List<AiUsageLog>> resultAiUsageLogs= aiUsageLogService.getToBeSavedAndToBeDeleted(aiUsageLogService.findByCollaboratorId(collaborator.getId()),collaborator.getAiUsageLogs());
            aiUsageLogService.delete(resultAiUsageLogs.get(1));
        emptyIfNull(resultAiUsageLogs.get(0)).forEach(e -> e.setCollaborator(collaborator));
        aiUsageLogService.update(resultAiUsageLogs.get(0),true);
        List<List<Task>> resultTasks= taskService.getToBeSavedAndToBeDeleted(taskService.findByAssignedToId(collaborator.getId()),collaborator.getTasks());
            taskService.delete(resultTasks.get(1));
        emptyIfNull(resultTasks.get(0)).forEach(e -> e.setAssignedTo(collaborator));
        taskService.update(resultTasks.get(0),true);
        List<List<ReservationRequest>> resultReservationRequests= reservationRequestService.getToBeSavedAndToBeDeleted(reservationRequestService.findByReviewedById(collaborator.getId()),collaborator.getReservationRequests());
            reservationRequestService.delete(resultReservationRequests.get(1));
        emptyIfNull(resultReservationRequests.get(0)).forEach(e -> e.setReviewedBy(collaborator));
        reservationRequestService.update(resultReservationRequests.get(0),true);
        }
    }








    public Collaborator findByReferenceEntity(Collaborator t){
        return t==null? null : dao.findByEmail(t.getEmail());
    }
    public void findOrSaveAssociatedObject(Collaborator t){
        if( t != null) {
            t.setDisplayCurrency(currencyService.findOrSave(t.getDisplayCurrency()));
        }
    }



    public List<Collaborator> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<Collaborator>> getToBeSavedAndToBeDeleted(List<Collaborator> oldList, List<Collaborator> newList) {
        List<List<Collaborator>> result = new ArrayList<>();
        List<Collaborator> resultDelete = new ArrayList<>();
        List<Collaborator> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Collaborator> oldList, List<Collaborator> newList, List<Collaborator> resultUpdateOrSave, List<Collaborator> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Collaborator myOld = oldList.get(i);
                Collaborator t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Collaborator myNew = newList.get(i);
                Collaborator t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}


    @Override
    public Collaborator create(Collaborator t) {
        if (findByUsername(t.getUsername()) != null || t.getPassword() == null) return null;
        t.setPassword(userService.cryptPassword(t.getPassword()));
        t.setEnabled(true);
        t.setAccountNonExpired(true);
        t.setAccountNonLocked(true);
        t.setCredentialsNonExpired(true);
        t.setPasswordChanged(false);

        Role role = new Role();
        role.setAuthority(AuthoritiesConstants.COLLABORATOR);
        role.setCreatedAt(LocalDateTime.now());
        Role myRole = roleService.create(role);
        RoleUser roleUser = new RoleUser();
        roleUser.setRole(myRole);
        if (t.getRoleUsers() == null)
            t.setRoleUsers(new ArrayList<>());

        t.getRoleUsers().add(roleUser);
        if (t.getModelPermissionUsers() == null)
            t.setModelPermissionUsers(new ArrayList<>());

        t.setModelPermissionUsers(modelPermissionUserService.initModelPermissionUser());

        Collaborator mySaved = dao.save(t);

        if (t.getModelPermissionUsers() != null) {
            t.getModelPermissionUsers().forEach(e -> {
                e.setUserApp(mySaved);
                modelPermissionUserService.create(e);
            });
        }
        if (t.getRoleUsers() != null) {
            t.getRoleUsers().forEach(element-> {
                element.setUserApp(mySaved);
                roleUserService.create(element);
            });
        }

        if (t.getEnterpriseMemberships() != null) {
        t.getEnterpriseMemberships().forEach(element-> {
            element.setCollaborator(mySaved);
            enterpriseMembershipService.create(element);
        });
        }
        if (t.getAiUsageLogs() != null) {
        t.getAiUsageLogs().forEach(element-> {
            element.setCollaborator(mySaved);
            aiUsageLogService.create(element);
        });
        }
        if (t.getTasks() != null) {
        t.getTasks().forEach(element-> {
            element.setAssignedTo(mySaved);
            taskService.create(element);
        });
        }
        if (t.getReservationRequests() != null) {
        t.getReservationRequests().forEach(element-> {
            element.setReviewedBy(mySaved);
            reservationRequestService.create(element);
        });
        }
        return mySaved;
     }

    public Collaborator findByUsername(String username){
        return dao.findByUsername(username);
    }

    public boolean changePassword(String username, String newPassword) {
        return userService.changePassword(username, newPassword);
    }




    private @Autowired UserService userService;
    private @Autowired RoleService roleService;
    private @Autowired ModelPermissionUserService modelPermissionUserService;
    private @Autowired RoleUserService roleUserService;

    @Autowired
    private AiUsageLogAdminService aiUsageLogService ;
    @Autowired
    private TaskAdminService taskService ;
    @Autowired
    private CurrencyAdminService currencyService ;
    @Autowired
    private ReservationRequestAdminService reservationRequestService ;
    @Autowired
    private EnterpriseMembershipAdminService enterpriseMembershipService ;

    public CollaboratorAdminServiceImpl(CollaboratorDao dao) {
        this.dao = dao;
    }

    private CollaboratorDao dao;
}
