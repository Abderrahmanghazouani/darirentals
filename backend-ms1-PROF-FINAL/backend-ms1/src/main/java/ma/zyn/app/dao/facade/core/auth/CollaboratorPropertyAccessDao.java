package ma.zyn.app.dao.facade.core.auth;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.auth.CollaboratorPropertyAccess;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CollaboratorPropertyAccessDao extends AbstractRepository<CollaboratorPropertyAccess,Long>  {

    List<CollaboratorPropertyAccess> findByCollaboratorId(Long id);
    int deleteByCollaboratorId(Long id);
    long countByCollaboratorId(Long id);
    List<CollaboratorPropertyAccess> findByPropertyId(Long id);
    int deleteByPropertyId(Long id);
    long countByPropertyId(Long id);


}
