package ma.zyn.app.service.impl.client.client;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.client.Client;
import ma.zyn.app.dao.criteria.core.client.ClientCriteria;
import ma.zyn.app.dao.facade.core.client.ClientDao;
import ma.zyn.app.dao.specification.core.client.ClientSpecification;
import ma.zyn.app.service.facade.client.client.ClientClientService;
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

import ma.zyn.app.service.facade.client.reservation.ReservationClientService ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.service.facade.client.reservation.ReservationRequestClientService ;
import ma.zyn.app.bean.core.reservation.ReservationRequest ;
import ma.zyn.app.service.facade.client.enterprise.EnterpriseClientService ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;

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
public class ClientClientServiceImpl implements ClientClientService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Client update(Client t) {
        Client loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Client.class.getSimpleName(), t.getId().toString()});
        } else {
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    public Client findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public Client findOrSave(Client t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Client result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Client> findAll() {
        return dao.findAll();
    }

    public List<Client> findByCriteria(ClientCriteria criteria) {
        List<Client> content = null;
        if (criteria != null) {
            ClientSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ClientSpecification constructSpecification(ClientCriteria criteria) {
        ClientSpecification mySpecification =  (ClientSpecification) RefelexivityUtil.constructObjectUsingOneParam(ClientSpecification.class, criteria);
        return mySpecification;
    }

    public List<Client> findPaginatedByCriteria(ClientCriteria criteria, int page, int pageSize, String order, String sortField) {
        ClientSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ClientCriteria criteria) {
        ClientSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<Client> findByEnterpriseId(Long id){
        return dao.findByEnterpriseId(id);
    }
    public int deleteByEnterpriseId(Long id){
        return dao.deleteByEnterpriseId(id);
    }
    public long countByEnterpriseId(Long id){
        return dao.countByEnterpriseId(id);
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
        reservationService.deleteByClientId(id);
        reservationRequestService.deleteByClientId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Client> delete(List<Client> list) {
		List<Client> result = new ArrayList();
        if (list != null) {
            for (Client t : list) {
                if(dao.findById(t.getId()).isEmpty()){
					result.add(t);
				}else{
                    dao.deleteById(t.getId());
                }
            }
        }
		return result;
    }


    public Client findWithAssociatedLists(Long id){
        Client result = dao.findById(id).orElse(null);
        if(result!=null && result.getId() != null) {
            result.setReservations(reservationService.findByClientId(id));
            result.setReservationRequests(reservationRequestService.findByClientId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Client> update(List<Client> ts, boolean createIfNotExist) {
        List<Client> result = new ArrayList<>();
        if (ts != null) {
            for (Client t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Client loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Client t, Client loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Client client){
    if(client !=null && client.getId() != null){
        List<List<Reservation>> resultReservations= reservationService.getToBeSavedAndToBeDeleted(reservationService.findByClientId(client.getId()),client.getReservations());
            reservationService.delete(resultReservations.get(1));
        emptyIfNull(resultReservations.get(0)).forEach(e -> e.setClient(client));
        reservationService.update(resultReservations.get(0),true);
        List<List<ReservationRequest>> resultReservationRequests= reservationRequestService.getToBeSavedAndToBeDeleted(reservationRequestService.findByClientId(client.getId()),client.getReservationRequests());
            reservationRequestService.delete(resultReservationRequests.get(1));
        emptyIfNull(resultReservationRequests.get(0)).forEach(e -> e.setClient(client));
        reservationRequestService.update(resultReservationRequests.get(0),true);
        }
    }








    public Client findByReferenceEntity(Client t){
        return t==null? null : dao.findByEmail(t.getEmail());
    }
    public void findOrSaveAssociatedObject(Client t){
        if( t != null) {
        }
    }



    public List<Client> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<Client>> getToBeSavedAndToBeDeleted(List<Client> oldList, List<Client> newList) {
        List<List<Client>> result = new ArrayList<>();
        List<Client> resultDelete = new ArrayList<>();
        List<Client> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Client> oldList, List<Client> newList, List<Client> resultUpdateOrSave, List<Client> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Client myOld = oldList.get(i);
                Client t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Client myNew = newList.get(i);
                Client t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}


    @Override
    public Client create(Client t) {
        if (findByUsername(t.getUsername()) != null || t.getPassword() == null) return null;
        t.setPassword(userService.cryptPassword(t.getPassword()));
        t.setEnabled(true);
        t.setAccountNonExpired(true);
        t.setAccountNonLocked(true);
        t.setCredentialsNonExpired(true);
        t.setPasswordChanged(false);

        Role role = new Role();
        role.setAuthority(AuthoritiesConstants.CLIENT);
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

        Client mySaved = dao.save(t);

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

        if (t.getReservations() != null) {
        t.getReservations().forEach(element-> {
            element.setClient(mySaved);
            reservationService.create(element);
        });
        }
        if (t.getReservationRequests() != null) {
        t.getReservationRequests().forEach(element-> {
            element.setClient(mySaved);
            reservationRequestService.create(element);
        });
        }
        return mySaved;
     }

    public Client findByUsername(String username){
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
    private ReservationClientService reservationService ;
    @Autowired
    private ReservationRequestClientService reservationRequestService ;
    @Autowired
    private EnterpriseClientService enterpriseService ;

    public ClientClientServiceImpl(ClientDao dao) {
        this.dao = dao;
    }

    private ClientDao dao;
}
