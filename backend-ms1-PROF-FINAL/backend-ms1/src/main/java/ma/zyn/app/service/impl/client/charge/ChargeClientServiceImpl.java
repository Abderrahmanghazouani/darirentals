package ma.zyn.app.service.impl.client.charge;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.charge.Charge;
import ma.zyn.app.dao.criteria.core.charge.ChargeCriteria;
import ma.zyn.app.dao.facade.core.charge.ChargeDao;
import ma.zyn.app.dao.specification.core.charge.ChargeSpecification;
import ma.zyn.app.service.facade.client.charge.ChargeClientService;
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

import ma.zyn.app.service.facade.client.payment.PaymentClientService ;
import ma.zyn.app.bean.core.payment.Payment ;
import ma.zyn.app.service.facade.client.charge.ChargeTypeClientService ;
import ma.zyn.app.bean.core.charge.ChargeType ;
import ma.zyn.app.service.facade.client.property.PropertyClientService ;
import ma.zyn.app.bean.core.property.Property ;
import ma.zyn.app.service.facade.client.document.DocumentClientService ;
import ma.zyn.app.bean.core.document.Document ;

import java.util.List;
@Service
public class ChargeClientServiceImpl implements ChargeClientService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Charge update(Charge t) {
        Charge loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Charge.class.getSimpleName(), t.getId().toString()});
        } else {
            updateWithAssociatedLists(t);
            dao.save(t);
            return loadedItem;
        }
    }

    public Charge findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public Charge findOrSave(Charge t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Charge result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Charge> findAll() {
        return dao.findAll();
    }

    public List<Charge> findByCriteria(ChargeCriteria criteria) {
        List<Charge> content = null;
        if (criteria != null) {
            ChargeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private ChargeSpecification constructSpecification(ChargeCriteria criteria) {
        ChargeSpecification mySpecification =  (ChargeSpecification) RefelexivityUtil.constructObjectUsingOneParam(ChargeSpecification.class, criteria);
        return mySpecification;
    }

    public List<Charge> findPaginatedByCriteria(ChargeCriteria criteria, int page, int pageSize, String order, String sortField) {
        ChargeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(ChargeCriteria criteria) {
        ChargeSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<Charge> findByPropertyId(Long id){
        return dao.findByPropertyId(id);
    }
    public int deleteByPropertyId(Long id){
        return dao.deleteByPropertyId(id);
    }
    public long countByPropertyId(Long id){
        return dao.countByPropertyId(id);
    }
    public List<Charge> findByChargeTypeCode(String code){
        return dao.findByChargeTypeCode(code);
    }
    public List<Charge> findByChargeTypeId(Long id){
        return dao.findByChargeTypeId(id);
    }
    public int deleteByChargeTypeCode(String code){
        return dao.deleteByChargeTypeCode(code);
    }
    public int deleteByChargeTypeId(Long id){
        return dao.deleteByChargeTypeId(id);
    }
    public long countByChargeTypeCode(String code){
        return dao.countByChargeTypeCode(code);
    }
    public List<Charge> findByPaymentId(Long id){
        return dao.findByPaymentId(id);
    }
    public int deleteByPaymentId(Long id){
        return dao.deleteByPaymentId(id);
    }
    public long countByPaymentId(Long id){
        return dao.countByPaymentId(id);
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
        documentService.deleteByChargeId(id);
    }




    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Charge> delete(List<Charge> list) {
		List<Charge> result = new ArrayList();
        if (list != null) {
            for (Charge t : list) {
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
    public Charge create(Charge t) {
        Charge loaded = findByReferenceEntity(t);
        Charge saved;
        if (loaded == null) {
            saved = dao.save(t);
            if (t.getDocuments() != null) {
                t.getDocuments().forEach(element-> {
                    element.setCharge(saved);
                    documentService.create(element);
                });
            }
        }else {
            saved = null;
        }
        return saved;
    }

    public Charge findWithAssociatedLists(Long id){
        Charge result = dao.findById(id).orElse(null);
        if(result!=null && result.getId() != null) {
            result.setDocuments(documentService.findByChargeId(id));
        }
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Charge> update(List<Charge> ts, boolean createIfNotExist) {
        List<Charge> result = new ArrayList<>();
        if (ts != null) {
            for (Charge t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Charge loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Charge t, Charge loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }

    public void updateWithAssociatedLists(Charge charge){
    if(charge !=null && charge.getId() != null){
        List<List<Document>> resultDocuments= documentService.getToBeSavedAndToBeDeleted(documentService.findByChargeId(charge.getId()),charge.getDocuments());
            documentService.delete(resultDocuments.get(1));
        emptyIfNull(resultDocuments.get(0)).forEach(e -> e.setCharge(charge));
        documentService.update(resultDocuments.get(0),true);
        }
    }








    public Charge findByReferenceEntity(Charge t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(Charge t){
        if( t != null) {
        }
    }



    public List<Charge> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<Charge>> getToBeSavedAndToBeDeleted(List<Charge> oldList, List<Charge> newList) {
        List<List<Charge>> result = new ArrayList<>();
        List<Charge> resultDelete = new ArrayList<>();
        List<Charge> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Charge> oldList, List<Charge> newList, List<Charge> resultUpdateOrSave, List<Charge> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Charge myOld = oldList.get(i);
                Charge t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Charge myNew = newList.get(i);
                Charge t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private PaymentClientService paymentService ;
    @Autowired
    private ChargeTypeClientService chargeTypeService ;
    @Autowired
    private PropertyClientService propertyService ;
    @Autowired
    private DocumentClientService documentService ;

    public ChargeClientServiceImpl(ChargeDao dao) {
        this.dao = dao;
    }

    private ChargeDao dao;
}
