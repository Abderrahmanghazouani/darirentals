package ma.zyn.app.service.security;

import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.dao.facade.core.enterprise.EnterpriseMembershipDao;
import ma.zyn.app.zynerator.security.bean.User;
import ma.zyn.app.zynerator.security.common.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Isolation par societe (Chantier 1 - NOTES-permissions.md).
 *
 * Calcule les Enterprise auxquelles le collaborateur AUTHENTIFIE a acces, a partir
 * du contexte de securite Spring (username verifie par le JWT), jamais a partir
 * d'un parametre envoye par le frontend. A utiliser dans tous les
 * *CollaboratorServiceImpl pour filtrer les listes/lectures par entreprise.
 */
@Service
public class EnterpriseAccessService {

    @Autowired
    private EnterpriseMembershipDao enterpriseMembershipDao;

    /** Le collaborateur actuellement authentifie, ou null si l'appelant n'est pas un collaborateur. */
    public Collaborator getCurrentCollaborator() {
        User user = SecurityUtil.getCurrentUser();
        return (user instanceof Collaborator) ? (Collaborator) user : null;
    }

    /** IDs des Enterprise rattachees au collaborateur authentifie via ses EnterpriseMembership. */
    public List<Long> getAccessibleEnterpriseIds() {
        Collaborator current = getCurrentCollaborator();
        if (current == null || current.getId() == null) {
            return Collections.emptyList();
        }
        List<EnterpriseMembership> memberships = enterpriseMembershipDao.findByCollaboratorId(current.getId());
        return memberships.stream()
                .map(m -> m.getEnterprise() != null ? m.getEnterprise().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public boolean hasAccessToEnterprise(Long enterpriseId) {
        return enterpriseId != null && getAccessibleEnterpriseIds().contains(enterpriseId);
    }
}
