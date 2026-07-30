package ma.zyn.app.dao.facade.core.report;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.report.FinancialReport;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface FinancialReportDao extends AbstractRepository<FinancialReport,Long>  {

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


}
