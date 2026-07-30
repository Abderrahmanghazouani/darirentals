package ma.zyn.app.dao.facade.core.auth;

import org.springframework.data.jpa.repository.Query;
import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import org.springframework.stereotype.Repository;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import java.util.List;


@Repository
public interface CollaboratorRoleDao extends AbstractRepository<CollaboratorRole,Long>  {
    CollaboratorRole findByCode(String code);
    int deleteByCode(String code);


    @Query("SELECT NEW CollaboratorRole(item.id,item.label) FROM CollaboratorRole item")
    List<CollaboratorRole> findAllOptimized();

}
