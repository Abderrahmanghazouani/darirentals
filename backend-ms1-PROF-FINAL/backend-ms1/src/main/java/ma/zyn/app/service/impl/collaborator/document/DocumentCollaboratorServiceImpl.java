package ma.zyn.app.service.impl.collaborator.document;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.document.Document;
import ma.zyn.app.dao.criteria.core.document.DocumentCriteria;
import ma.zyn.app.dao.facade.core.document.DocumentDao;
import ma.zyn.app.dao.specification.core.document.DocumentSpecification;
import ma.zyn.app.service.facade.collaborator.document.DocumentCollaboratorService;
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

import ma.zyn.app.service.facade.collaborator.reservation.ReservationCollaboratorService ;
import ma.zyn.app.bean.core.reservation.Reservation ;
import ma.zyn.app.service.facade.collaborator.document.DocumentTypeCollaboratorService ;
import ma.zyn.app.bean.core.document.DocumentType ;
import ma.zyn.app.service.facade.collaborator.charge.ChargeCollaboratorService ;
import ma.zyn.app.bean.core.charge.Charge ;

import java.util.List;
@Service
public class DocumentCollaboratorServiceImpl implements DocumentCollaboratorService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public Document update(Document t) {
        Document loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{Document.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public Document findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public Document findOrSave(Document t) {
        if (t != null) {
            findOrSaveAssociatedObject(t);
            Document result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<Document> findAll() {
        return dao.findAll();
    }

    public List<Document> findByCriteria(DocumentCriteria criteria) {
        List<Document> content = null;
        if (criteria != null) {
            DocumentSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private DocumentSpecification constructSpecification(DocumentCriteria criteria) {
        DocumentSpecification mySpecification =  (DocumentSpecification) RefelexivityUtil.constructObjectUsingOneParam(DocumentSpecification.class, criteria);
        return mySpecification;
    }

    public List<Document> findPaginatedByCriteria(DocumentCriteria criteria, int page, int pageSize, String order, String sortField) {
        DocumentSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(DocumentCriteria criteria) {
        DocumentSpecification mySpecification = constructSpecification(criteria);
        mySpecification.setDistinct(true);
        return ((Long) dao.count(mySpecification)).intValue();
    }

    public List<Document> findByDocumentTypeCode(String code){
        return dao.findByDocumentTypeCode(code);
    }
    public List<Document> findByDocumentTypeId(Long id){
        return dao.findByDocumentTypeId(id);
    }
    public int deleteByDocumentTypeCode(String code){
        return dao.deleteByDocumentTypeCode(code);
    }
    public int deleteByDocumentTypeId(Long id){
        return dao.deleteByDocumentTypeId(id);
    }
    public long countByDocumentTypeCode(String code){
        return dao.countByDocumentTypeCode(code);
    }
    public List<Document> findByReservationId(Long id){
        return dao.findByReservationId(id);
    }
    public int deleteByReservationId(Long id){
        return dao.deleteByReservationId(id);
    }
    public long countByReservationReference(String reference){
        return dao.countByReservationReference(reference);
    }
    public List<Document> findByChargeId(Long id){
        return dao.findByChargeId(id);
    }
    public int deleteByChargeId(Long id){
        return dao.deleteByChargeId(id);
    }
    public long countByChargeId(Long id){
        return dao.countByChargeId(id);
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
    public List<Document> delete(List<Document> list) {
		List<Document> result = new ArrayList();
        if (list != null) {
            for (Document t : list) {
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
    public Document create(Document t) {
        Document loaded = findByReferenceEntity(t);
        Document saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public Document findWithAssociatedLists(Long id){
        Document result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<Document> update(List<Document> ts, boolean createIfNotExist) {
        List<Document> result = new ArrayList<>();
        if (ts != null) {
            for (Document t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    Document loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, Document t, Document loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public Document findByReferenceEntity(Document t) {
        return t == null || t.getId() == null ? null : findById(t.getId());
    }
    public void findOrSaveAssociatedObject(Document t){
        if( t != null) {
            t.setDocumentType(documentTypeService.findOrSave(t.getDocumentType()));
            t.setReservation(reservationService.findOrSave(t.getReservation()));
            t.setCharge(chargeService.findOrSave(t.getCharge()));
        }
    }



    public List<Document> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<Document>> getToBeSavedAndToBeDeleted(List<Document> oldList, List<Document> newList) {
        List<List<Document>> result = new ArrayList<>();
        List<Document> resultDelete = new ArrayList<>();
        List<Document> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<Document> oldList, List<Document> newList, List<Document> resultUpdateOrSave, List<Document> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                Document myOld = oldList.get(i);
                Document t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                Document myNew = newList.get(i);
                Document t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}







    @Autowired
    private ReservationCollaboratorService reservationService ;
    @Autowired
    private DocumentTypeCollaboratorService documentTypeService ;
    @Autowired
    private ChargeCollaboratorService chargeService ;

    public DocumentCollaboratorServiceImpl(DocumentDao dao) {
        this.dao = dao;
    }

    private DocumentDao dao;
}
