package ma.zyn.app.dao.facade.core.auth;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.auth.Collaborator;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.auth.Collaborator;
import java.util.List;


@Repository
public interface CollaboratorDao extends AbstractRepository<Collaborator,Long>  {
    Collaborator findByEmail(String email);
    int deleteByEmail(String email);

    List<Collaborator> findByDisplayCurrencyCode(String code);
    List<Collaborator> findByDisplayCurrencyId(Long id);
    int deleteByDisplayCurrencyId(Long id);
    int deleteByDisplayCurrencyCode(String code);
    long countByDisplayCurrencyCode(String code);
    Collaborator findByUsername(String username);

    @Query("SELECT NEW Collaborator(item.id,item.name) FROM Collaborator item")
    List<Collaborator> findAllOptimized();

}
