package ma.zyn.app.service.facade.client.report;

import java.util.List;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import ma.zyn.app.dao.criteria.core.report.FinancialReportScopeCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface FinancialReportScopeClientService {







	FinancialReportScope create(FinancialReportScope t);

    FinancialReportScope update(FinancialReportScope t);

    List<FinancialReportScope> update(List<FinancialReportScope> ts,boolean createIfNotExist);

    FinancialReportScope findById(Long id);

    FinancialReportScope findOrSave(FinancialReportScope t);

    FinancialReportScope findByReferenceEntity(FinancialReportScope t);

    FinancialReportScope findWithAssociatedLists(Long id);

    List<FinancialReportScope> findAllOptimized();

    List<FinancialReportScope> findAll();

    List<FinancialReportScope> findByCriteria(FinancialReportScopeCriteria criteria);

    List<FinancialReportScope> findPaginatedByCriteria(FinancialReportScopeCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(FinancialReportScopeCriteria criteria);

    List<FinancialReportScope> delete(List<FinancialReportScope> ts);

    boolean deleteById(Long id);

    List<List<FinancialReportScope>> getToBeSavedAndToBeDeleted(List<FinancialReportScope> oldList, List<FinancialReportScope> newList);

}
