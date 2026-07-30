package ma.zyn.app.dao.facade.core.currency;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.currency.Currency;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.currency.Currency;
import java.util.List;


@Repository
public interface CurrencyDao extends AbstractRepository<Currency,Long>  {
    Currency findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW Currency(item.id,item.label) FROM Currency item")
    List<Currency> findAllOptimized();

}
