package ma.zyn.app.service.facade.admin.report;

import java.util.List;
import ma.zyn.app.bean.core.report.FinancialReport;
import ma.zyn.app.dao.criteria.core.report.FinancialReportCriteria;
import ma.zyn.app.zynerator.service.IService;



public interface FinancialReportAdminService {



    List<FinancialReport> findByFinancialReportTypeCode(String code);
    List<FinancialReport> findByFinancialReportTypeId(Long id);
    int deleteByFinancialReportTypeId(Long id);
    int deleteByFinancialReportTypeCode(String code);
    long countByFinancialReportTypeCode(String code);
    List<FinancialReport> findByFinancialReportScopeCode(String code);
    List<FinancialReport> findByFinancialReportScopeId(Long id);
    int deleteByFinancialReportScopeId(Long id);
    int deleteByFinancialReportScopeCode(String code);
    long countByFinancialReportScopeCode(String code);
    List<FinancialReport> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);
    List<FinancialReport> findByGeneratedById(Long id);
    int deleteByGeneratedById(Long id);
    long countByGeneratedByEmail(String email);




	FinancialReport create(FinancialReport t);

    FinancialReport update(FinancialReport t);

    List<FinancialReport> update(List<FinancialReport> ts,boolean createIfNotExist);

    FinancialReport findById(Long id);

    FinancialReport findOrSave(FinancialReport t);

    FinancialReport findByReferenceEntity(FinancialReport t);

    FinancialReport findWithAssociatedLists(Long id);

    List<FinancialReport> findAllOptimized();

    List<FinancialReport> findAll();

    List<FinancialReport> findByCriteria(FinancialReportCriteria criteria);

    List<FinancialReport> findPaginatedByCriteria(FinancialReportCriteria criteria, int page, int pageSize, String order, String sortField);

    int getDataSize(FinancialReportCriteria criteria);

    List<FinancialReport> delete(List<FinancialReport> ts);

    boolean deleteById(Long id);

    List<List<FinancialReport>> getToBeSavedAndToBeDeleted(List<FinancialReport> oldList, List<FinancialReport> newList);

}
