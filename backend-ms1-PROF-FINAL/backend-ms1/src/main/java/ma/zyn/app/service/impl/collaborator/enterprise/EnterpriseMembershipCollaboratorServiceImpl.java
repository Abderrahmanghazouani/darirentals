package ma.zyn.app.service.impl.collaborator.enterprise;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.dao.criteria.core.enterprise.EnterpriseMembershipCriteria;
import ma.zyn.app.dao.facade.core.enterprise.EnterpriseMembershipDao;
import ma.zyn.app.dao.specification.core.enterprise.EnterpriseMembershipSpecification;
import ma.zyn.app.service.facade.collaborator.enterprise.EnterpriseMembershipCollaboratorService;
import ma.zyn.app.service.security.EffectivePermissionService;
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
import ma.zyn.app.service.facade.collaborator.auth.CollaboratorPermissionOverrideCollaboratorService ;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride ;
import ma.zyn.app.service.facade.collaborator.enterprise.EnterpriseCollaboratorService ;
import ma.zyn.app.bean.core.enterprise.Enterprise ;
import ma.zyn.app.service.facade.collaborator.auth.CollaboratorRoleCollaboratorService ;
import ma.zyn.app.bean.core.auth.CollaboratorRole ;

import java.util.List;
@Service
public class EnterpriseMembershipCollaboratorServiceImpl implements EnterpriseMembershipCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public EnterpriseMembership update(EnterpriseMembership t) {
        EnterpriseMembership loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{EnterpriseMembership.class.getSimpleName(), t.getId().toString()});
        } else {
            effectivePermissionService.assertCanManageUsers(t.getEnterprise() != null ? t.getEnterprise().getId() : null);
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    public EnterpriseMembership findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public EnterpriseMembership findOrSave(EnterpriseMembership t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            EnterpriseMembership result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<EnterpriseMembership> findAll() {
        return dao.findAll();
    }

    public List<EnterpriseMembership> findByCriteria(EnterpriseMembershipCriteria criteria) {
        List<EnterpriseMembership> content = null;
        if (criteria != null) {
            EnterpriseMembershipSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private EnterpriseMembershipSpecification constructSpecification(EnterpriseMembershipCriteria criteria) {
        EnterpriseMembershipSpecification mySpecification =  (EnterpriseMembershipSpecification) RefelexivityUtil.constructObjectUsingOneParam(EnterpriseMembershipSpecification.class, criteria);
        return mySpecification;
    }

    public List<EnterpriseMembership> findPaginatedByCriteria(EnterpriseMembershipCriteria criteria, int page, int pageSize, String order, String sortField) {
        EnterpriseMembershipSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(EnterpriseMembershipCriteria criteria) {
        EnterpriseMembershipSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<EnterpriseMembership> findByCollaboratorId(Long id){
        return dao.findByCollaboratorId(id);
    }
    public int deleteByCollaboratorId(Long id){
        return dao.deleteByCollaboratorId(id);
    }
    public long countByCollaboratorEmail(String email){
        return dao.countByCollaboratorEmail(email);
    }
    public List<EnterpriseMembership> findByEnterpriseId(Long id){
        return dao.findByEnterpriseId(id);
    }
    public int deleteByEnterpriseId(Long id){
        return dao.deleteByEnterpriseId(id);
    }
    public long countByEnterpriseId(Long id){
        return dao.countByEnterpriseId(id);
    }
    public List<EnterpriseMembership> findByCollaboratorRoleCode(String code){
        return dao.findByCollaboratorRoleCode(code);
    }
    public List<EnterpriseMembership> findByCollaboratorRoleId(Long id){
        return dao.findByCollaboratorRoleId(id);
    }
    public int deleteByCollaboratorRoleCode(String code){
        return dao.deleteByCollaboratorRoleCode(code);
    }
    public int deleteByCollaboratorRoleId(Long id){
        return dao.deleteByCollaboratorRoleId(id);
    }
    public long countByCollaboratorRoleCode(String code){
        return dao.countByCollaboratorRoleCode(code);
    }
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public boolean deleteById(Long id) {
        boolean condition = (id != null);
        if (condition) {
            EnterpriseMembership target = dao.findById(id).orElse(null);
            if (target != null) {
                effectivePermissionService.assertCanManageUsers(target.getEnterprise() != null ? target.getEnterprise().getId() : null);
            }
            deleteAssociatedLists(id);
            dao.deleteById(id);
        }
        return condition;
    }

    public void deleteAssociatedLists(Long id) {
        collaboratorPermissionOverrideService.deleteByEnterpriseMembershipId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<EnterpriseMembership> delete(List<EnterpriseMembership> list) {
		List<EnterpriseMembership> result = new ArrayList();
        if (list != null) {
            for (EnterpriseMembership t : list) {
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
    public EnterpriseMembership create(EnterpriseMembership t) {
        effectivePermissionService.assertCanManageUsers(t.getEnterprise() != null ? t.getEnterprise().getId() : null);
        EnterpriseMembership loaded = findByReferenceEntity(t);
        EnterpriseMembership saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getCollaboratorPermissionOverrides() != null) {
                t.getCollaboratorPermissionOverrides().forEach(element-> {
                    element.setEnterpriseMembership(saved);
                    collaboratorPermissionOverrideService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public EnterpriseMembership findWithAssociatedLists(Long id){
        EnterpriseMembership result = dao.findById(id).orElse(null);
        if(result!=null && result.getId() != null) {
            result.setCollaboratorPermissionOverrides(collaboratorPermissionOverrideService.findByEnterpriseMembershipId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<EnterpriseMembership> update(List<EnterpriseMembership> ts, boolean createIfNotExist) {
        List<EnterpriseMembership> result = new ArrayList<>();
        if (ts != null) {
            for (EnterpriseMembership t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    EnterpriseMembership loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, EnterpriseMembership t, EnterpriseMembership loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(EnterpriseMembership enterpriseMembership){
    if(enterpriseMembership !=null && enterpriseMembership.getId() != null){
        List<List<CollaboratorPermissionOverride>> resultCollaboratorPermissionOverrides= collaboratorPermissionOverrideService.getToBeSavedAndToBeDeleted(collaboratorPermissionOverrideService.findByEnterpriseMembershipId(enterpriseMembership.getId()),enterpriseMembership.getCollaboratorPermissionOverrides());
            collaboratorPermissionOverrideService.delete(resultCollaboratorPermissionOverrides.get(1));
        emptyIfNull(resultCollaboratorPermissionOverrides.get(0)).forEach(e -> e.setEnterpriseMembership(enterpriseMembership));
        collaboratorPermissionOverrideService.update(resultCollaboratorPermissionOverrides.get(0),true);
        }
    }








    public EnterpriseMembership findByReferenceEntity(EnterpriseMembership t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(EnterpriseMembership t){
        if( t != null) {
            t.setCollaborator(collaboratorService.findOrSave(t.getCollaborator()));
            t.setEnterprise(enterpriseService.findOrSave(t.getEnterprise()));
            t.setCollaboratorRole(collaboratorRoleService.findOrSave(t.getCollaboratorRole()));
        }
    }



    public List<EnterpriseMembership> findAllOptimized() {
        return dao.findAll();
    }

    @Override
    public List<List<EnterpriseMembership>> getToBeSavedAndToBeDeleted(List<EnterpriseMembership> oldList, List<EnterpriseMembership> newList) {
        List<List<EnterpriseMembership>> result = new ArrayList<>();
        List<EnterpriseMembership> resultDelete = new ArrayList<>();
        List<EnterpriseMembership> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<EnterpriseMembership> oldList, List<EnterpriseMembership> newList, List<EnterpriseMembership> resultUpdateOrSave, List<EnterpriseMembership> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                EnterpriseMembership myOld = oldList.get(i);
                EnterpriseMembership t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                EnterpriseMembership myNew = newList.get(i);
                EnterpriseMembership t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private CollaboratorCollaboratorService collaboratorService ;
    @Autowired
    private CollaboratorPermissionOverrideCollaboratorService collaboratorPermissionOverrideService ;
    @Autowired
    private EnterpriseCollaboratorService enterpriseService ;
    @Autowired
    private CollaboratorRoleCollaboratorService collaboratorRoleService ;
    @Autowired
    private EffectivePermissionService effectivePermissionService ;

    public EnterpriseMembershipCollaboratorServiceImpl(EnterpriseMembershipDao dao) {
        this.dao = dao;
    }

    private EnterpriseMembershipDao dao;
}
