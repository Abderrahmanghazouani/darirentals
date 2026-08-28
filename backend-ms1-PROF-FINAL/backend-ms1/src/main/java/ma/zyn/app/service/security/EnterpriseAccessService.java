package ma.zyn.app.service.security;

import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.bean.core.property.Property;
import ma.zyn.app.dao.facade.core.auth.CollaboratorPropertyAccessDao;
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
    @Autowired
    private CollaboratorPropertyAccessDao collaboratorPropertyAccessDao;

    /** Le collaborateur actuellement authentifie, ou null si l'appelant n'est pas un collaborateur. */
    public Collaborator getCurrentCollaborator() {
        User user = SecurityUtil.getCurrentUser();
        return (user instanceof Collaborator) ? (Collaborator) user : null;
    }

    /** Le EnterpriseMembership du collaborateur authentifie pour une societe precise, ou null
     * s'il n'y est pas rattache. Un collaborateur multi-societe peut avoir un role different
     * selon la societe. */
    private EnterpriseMembership membershipFor(Long enterpriseId) {
        Collaborator current = getCurrentCollaborator();
        if (current == null || current.getId() == null || enterpriseId == null) {
            return null;
        }
        return enterpriseMembershipDao.findByCollaboratorId(current.getId()).stream()
                .filter(m -> m.getEnterprise() != null && enterpriseId.equals(m.getEnterprise().getId()))
                .findFirst()
                .orElse(null);
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

    /** Isolation par societe appliquee a Collaborator (trou trouve apres coup - absent de la
     * liste initiale du Chantier 1, qui ne couvrait que Property/Client/ServiceProvider/
     * Reservation/Charge/Task/Payment). Collaborator n'a pas un lien direct unique vers
     * Enterprise (potentiellement multi-societe via EnterpriseMembership) : accessible si AU
     * MOINS une de ses societes est aussi accessible au collaborateur authentifie. */
    public List<Long> getAccessibleCollaboratorIds() {
        List<Long> accessibleEnterpriseIds = getAccessibleEnterpriseIds();
        if (accessibleEnterpriseIds.isEmpty()) {
            return Collections.emptyList();
        }
        return enterpriseMembershipDao.findByEnterpriseIdIn(accessibleEnterpriseIds).stream()
                .map(m -> m.getCollaborator() != null ? m.getCollaborator().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /** Chantier 3 (NOTES-permissions.md) : true si ce collaborateur est restreint a une liste
     * explicite de proprietes pour cette societe. Seul un role de code "SubAdmin" en est
     * exempte - tout le reste (Gestionnaire, ou meme une membership sans role du tout) est
     * restreint par defaut, choix volontairement le plus sur (deny par defaut). */
    public boolean isPropertyRestricted(Long enterpriseId) {
        EnterpriseMembership membership = membershipFor(enterpriseId);
        if (membership == null) {
            return true;
        }
        CollaboratorRole role = membership.getCollaboratorRole();
        return role == null || !"SubAdmin".equals(role.getCode());
    }

    /** IDs des Property explicitement assignees au collaborateur authentifie via
     * CollaboratorPropertyAccess (Chantier 3). Vide si aucune n'a ete assignee. */
    public List<Long> getAccessiblePropertyIds() {
        Collaborator current = getCurrentCollaborator();
        if (current == null || current.getId() == null) {
            return Collections.emptyList();
        }
        return collaboratorPropertyAccessDao.findByCollaboratorId(current.getId()).stream()
                .map(a -> a.getProperty() != null ? a.getProperty().getId() : null)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    /** Combine l'isolation par societe (Chantier 1) et la restriction par propriete
     * (Chantier 3) en un seul point d'entree, a utiliser partout ou une Property (ou une
     * entite liee via Property) doit etre verifiee. */
    public boolean isPropertyAccessible(Property property) {
        if (property == null || property.getEnterprise() == null || property.getEnterprise().getId() == null) {
            return false;
        }
        Long enterpriseId = property.getEnterprise().getId();
        if (!hasAccessToEnterprise(enterpriseId)) {
            return false;
        }
        if (!isPropertyRestricted(enterpriseId)) {
            return true;
        }
        return property.getId() != null && getAccessiblePropertyIds().contains(property.getId());
    }
}
