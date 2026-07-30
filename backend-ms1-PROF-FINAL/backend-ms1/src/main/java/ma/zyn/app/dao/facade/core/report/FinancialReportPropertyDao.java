package ma.zyn.app.dao.facade.core.report;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.report.FinancialReportProperty;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface FinancialReportPropertyDao extends AbstractRepository<FinancialReportProperty,Long>  {

    List<FinancialReportProperty> findByFinancialReportId(Long id);
    int deleteByFinancialReportId(Long id);
    long countByFinancialReportId(Long id);
    List<FinancialReportProperty> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);


}
