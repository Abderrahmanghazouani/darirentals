package ma.zyn.app.service.facade.admin.report;

import java.util.List;
import ma.zyn.app.bean.core.report.FinancialReportType;
import ma.zyn.app.dao.criteria.core.report.FinancialReportTypeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface FinancialReportTypeAdminService {







	FinancialReportType create(FinancialReportType t);

    FinancialReportType update(FinancialReportType t);

    List<FinancialReportType> update(List<FinancialReportType> ts,boolean createIfNotExist);

    FinancialReportType findById(Long id);

    FinancialReportType findOrSave(FinancialReportType t);

    FinancialReportType findByReferenceEntity(FinancialReportType t);

    FinancialReportType findWithAssociatedLists(Long id);

    List<FinancialReportType> findAllOptimized();

    List<FinancialReportType> findAll();

    List<FinancialReportType> findByCriteria(FinancialReportTypeCriteria criteria);

    List<FinancialReportType> findPaginatedByCriteria(FinancialReportTypeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(FinancialReportTypeCriteria criteria);

    List<FinancialReportType> delete(List<FinancialReportType> ts);

    boolean deleteById(Long id);

    List<List<FinancialReportType>> getToBeSavedAndToBeDeleted(List<FinancialReportType> oldList, List<FinancialReportType> newList);

}
