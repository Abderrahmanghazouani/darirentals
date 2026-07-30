package ma.zyn.app.dao.facade.core.report;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.report.FinancialReportType;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.report.FinancialReportType;
import java.util.List;


@Repository
public interface FinancialReportTypeDao extends AbstractRepository<FinancialReportType,Long>  {
    FinancialReportType findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW FinancialReportType(item.id,item.label) FROM FinancialReportType item")
    List<FinancialReportType> findAllOptimized();

}
