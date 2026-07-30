package ma.zyn.app.service.facade.collaborator.report;

import java.util.List;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import ma.zyn.app.dao.criteria.core.report.FinancialReportPropertyCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface FinancialReportPropertyCollaboratorService {



    List<FinancialReportProperty> findByFinancialReportId(Long id);
    int deleteByFinancialReportId(Long id);
    long countByFinancialReportId(Long id);
    List<FinancialReportProperty> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);




	FinancialReportProperty create(FinancialReportProperty t);

    FinancialReportProperty update(FinancialReportProperty t);

    List<FinancialReportProperty> update(List<FinancialReportProperty> ts,boolean createIfNotExist);

    FinancialReportProperty findById(Long id);

    FinancialReportProperty findOrSave(FinancialReportProperty t);

    FinancialReportProperty findByReferenceEntity(FinancialReportProperty t);

    FinancialReportProperty findWithAssociatedLists(Long id);

    List<FinancialReportProperty> findAllOptimized();

    List<FinancialReportProperty> findAll();

    List<FinancialReportProperty> findByCriteria(FinancialReportPropertyCriteria criteria);

    List<FinancialReportProperty> findPaginatedByCriteria(FinancialReportPropertyCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(FinancialReportPropertyCriteria criteria);

    List<FinancialReportProperty> delete(List<FinancialReportProperty> ts);

    boolean deleteById(Long id);

    List<List<FinancialReportProperty>> getToBeSavedAndToBeDeleted(List<FinancialReportProperty> oldList, List<FinancialReportProperty> newList);

}
