package ma.zyn.app.dao.facade.core.enterprise;

import ma.zyn.app.zynerator.repository.AbstractRepository;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface EnterpriseMembershipDao extends AbstractRepository<EnterpriseMembership,Long>  {

    List<EnterpriseMembership> findByCollaboratorId(Long id);
    int deleteByCollaboratorId(Long id);
    long countByCollaboratorEmail(String email);
    List<EnterpriseMembership> findByEnterpriseId(Long id);
    int deleteByEnterpriseId(Long id);
    long countByEnterpriseId(Long id);
    List<EnterpriseMembership> findByCollaboratorRoleCode(String code);
    List<EnterpriseMembership> findByCollaboratorRoleId(Long id);
    int deleteByCollaboratorRoleId(Long id);
    int deleteByCollaboratorRoleCode(String code);
    long countByCollaboratorRoleCode(String code);


}
