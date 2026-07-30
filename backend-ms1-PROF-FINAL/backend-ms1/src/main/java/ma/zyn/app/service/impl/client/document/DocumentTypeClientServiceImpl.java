package ma.zyn.app.service.impl.client.document;



import ma.zyn.app.zynerator.exception.EntityNotFoundException;
import ma.zyn.app.bean.core.document.DocumentType;
import ma.zyn.app.dao.criteria.core.document.DocumentTypeCriteria;
import ma.zyn.app.dao.facade.core.document.DocumentTypeDao;
import ma.zyn.app.dao.specification.core.document.DocumentTypeSpecification;
import ma.zyn.app.service.facade.client.document.DocumentTypeClientService;
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
public class DocumentTypeClientServiceImpl implements DocumentTypeClientService {

    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public DocumentType update(DocumentType t) {
        DocumentType loadedItem = dao.findById(t.getId()).orElse(null);
        if (loadedItem == null) {
            throw new EntityNotFoundException("errors.notFound", new String[]{DocumentType.class.getSimpleName(), t.getId().toString()});
        } else {
            dao.save(t);
            return loadedItem;
        }
    }

    public DocumentType findById(Long id) {
        return dao.findById(id).orElse(null);
    }


    public DocumentType findOrSave(DocumentType t) {
        if (t != null) {
            DocumentType result = findByReferenceEntity(t);
            if (result == null) {
                return dao.save(t);
            } else {
                return result;
            }
        }
        return null;
    }

    public List<DocumentType> findAll() {
        return dao.findAll();
    }

    public List<DocumentType> findByCriteria(DocumentTypeCriteria criteria) {
        List<DocumentType> content = null;
        if (criteria != null) {
            DocumentTypeSpecification mySpecification = constructSpecification(criteria);
            content = dao.findAll(mySpecification);
        } else {
            content = dao.findAll();
        }
        return content;

    }


    private DocumentTypeSpecification constructSpecification(DocumentTypeCriteria criteria) {
        DocumentTypeSpecification mySpecification =  (DocumentTypeSpecification) RefelexivityUtil.constructObjectUsingOneParam(DocumentTypeSpecification.class, criteria);
        return mySpecification;
    }

    public List<DocumentType> findPaginatedByCriteria(DocumentTypeCriteria criteria, int page, int pageSize, String order, String sortField) {
        DocumentTypeSpecification mySpecification = constructSpecification(criteria);
        order = (order != null && !order.isEmpty()) ? order : "desc";
        sortField = (sortField != null && !sortField.isEmpty()) ? sortField : "id";
        Pageable pageable = PageRequest.of(page, pageSize, Sort.Direction.fromString(order), sortField);
        return dao.findAll(mySpecification, pageable).getContent();
    }

    public int getDataSize(DocumentTypeCriteria criteria) {
        DocumentTypeSpecification mySpecification = constructSpecification(criteria);
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
    public List<DocumentType> delete(List<DocumentType> list) {
		List<DocumentType> result = new ArrayList();
        if (list != null) {
            for (DocumentType t : list) {
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
    public DocumentType create(DocumentType t) {
        DocumentType loaded = findByReferenceEntity(t);
        DocumentType saved;
        if (loaded == null) {
            saved = dao.save(t);
        }else {
            saved = null;
        }
        return saved;
    }

    public DocumentType findWithAssociatedLists(Long id){
        DocumentType result = dao.findById(id).orElse(null);
        return result;
    }

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
    public List<DocumentType> update(List<DocumentType> ts, boolean createIfNotExist) {
        List<DocumentType> result = new ArrayList<>();
        if (ts != null) {
            for (DocumentType t : ts) {
                if (t.getId() == null) {
                    dao.save(t);
                } else {
                    DocumentType loadedItem = dao.findById(t.getId()).orElse(null);
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


    private boolean isEligibleForCreateOrUpdate(boolean createIfNotExist, DocumentType t, DocumentType loadedItem) {
        boolean eligibleForCreateCrud = t.getId() == null;
        boolean eligibleForCreate = (createIfNotExist && (t.getId() == null || loadedItem == null));
        boolean eligibleForUpdate = (t.getId() != null && loadedItem != null);
        return (eligibleForCreateCrud || eligibleForCreate || eligibleForUpdate);
    }









    public DocumentType findByReferenceEntity(DocumentType t){
        return t==null? null : dao.findByCode(t.getCode());
    }



    public List<DocumentType> findAllOptimized() {
        return dao.findAllOptimized();
    }

    @Override
    public List<List<DocumentType>> getToBeSavedAndToBeDeleted(List<DocumentType> oldList, List<DocumentType> newList) {
        List<List<DocumentType>> result = new ArrayList<>();
        List<DocumentType> resultDelete = new ArrayList<>();
        List<DocumentType> resultUpdateOrSave = new ArrayList<>();
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

    private void extractToBeSaveOrDelete(List<DocumentType> oldList, List<DocumentType> newList, List<DocumentType> resultUpdateOrSave, List<DocumentType> resultDelete) {
		for (int i = 0; i < oldList.size(); i++) {
                DocumentType myOld = oldList.get(i);
                DocumentType t = newList.stream().filter(e -> myOld.equals(e)).findFirst().orElse(null);
                if (t != null) {
                    resultUpdateOrSave.add(t); // update
                } else {
                    resultDelete.add(myOld);
                }
            }
            for (int i = 0; i < newList.size(); i++) {
                DocumentType myNew = newList.get(i);
                DocumentType t = oldList.stream().filter(e -> myNew.equals(e)).findFirst().orElse(null);
                if (t == null) {
                    resultUpdateOrSave.add(myNew); // create
                }
            }
	}








    public DocumentTypeClientServiceImpl(DocumentTypeDao dao) {
        this.dao = dao;
    }

    private DocumentTypeDao dao;
}
