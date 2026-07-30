package ma.zyn.app.dao.facade.core.report;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.report.FinancialReportScope;
import java.util.List;


@Repository
public interface FinancialReportScopeDao extends AbstractRepository<FinancialReportScope,Long>  {
    FinancialReportScope findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW FinancialReportScope(item.id,item.label) FROM FinancialReportScope item")
    List<FinancialReportScope> findAllOptimized();

}
