package ma.zyn.app.dao.facade.core.currency;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.currency.ExchangeRate;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ExchangeRateDao extends AbstractRepository<ExchangeRate,Long>  {

    List<ExchangeRate> findByBaseCurrencyCode(String code);
    List<ExchangeRate> findByBaseCurrencyId(Long id);
    int deleteByBaseCurrencyId(Long id);
    int deleteByBaseCurrencyCode(String code);
    long countByBaseCurrencyCode(String code);
    List<ExchangeRate> findByTargetCurrencyCode(String code);
    List<ExchangeRate> findByTargetCurrencyId(Long id);
    int deleteByTargetCurrencyId(Long id);
    int deleteByTargetCurrencyCode(String code);
    long countByTargetCurrencyCode(String code);


}
