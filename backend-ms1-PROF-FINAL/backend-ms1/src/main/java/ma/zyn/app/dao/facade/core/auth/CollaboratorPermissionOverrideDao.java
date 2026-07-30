package ma.zyn.app.dao.facade.core.auth;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface CollaboratorPermissionOverrideDao extends AbstractRepository<CollaboratorPermissionOverride,Long>  {

    List<CollaboratorPermissionOverride> findByEnterpriseMembershipId(Long id);
    int deleteByEnterpriseMembershipId(Long id);
    long countByEnterpriseMembershipId(Long id);


}
