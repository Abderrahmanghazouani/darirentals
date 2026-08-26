package ma.zyn.app.service.security;

import ma.zyn.app.bean.core.auth.Collaborator;
import ma.zyn.app.bean.core.auth.CollaboratorPermissionOverride;
import ma.zyn.app.bean.core.auth.CollaboratorRole;
import ma.zyn.app.bean.core.enterprise.EnterpriseMembership;
import ma.zyn.app.dao.facade.core.auth.CollaboratorPermissionOverrideDao;
import ma.zyn.app.dao.facade.core.enterprise.EnterpriseMembershipDao;
import ma.zyn.app.zynerator.exception.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

/**
 * Chantier 2 (NOTES-permissions.md) : calcule la permission EFFECTIVE d'un collaborateur
 * pour une societe donnee = valeur du CollaboratorRole de son EnterpriseMembership pour
 * cette societe, sauf si un CollaboratorPermissionOverride existe pour cette membership,
 * auquel cas la permission est accordee si le ROLE OU L'OVERRIDE l'accorde (logique
 * additive/OR).
 *
 * Pourquoi OR et pas remplacement complet : CollaboratorPermissionOverride a ses 5 champs
 * initialises a false par defaut (aussi bien en Java qu'en colonne DB "default false"), et
 * CollaboratorPermissionOverrideConverter.toItem() ne touche que les champs explicitement
 * envoyes par le client - les autres restent a false, jamais null. Il est donc impossible de
 * distinguer de maniere fiable "ce champ n'a pas ete touche par l'override" de "ce champ a ete
 * explicitement mis a false par l'override". Traiter l'override comme un remplacement complet
 * ferait courir le risque qu'un override cree pour accorder UNE SEULE permission revoque
 * silencieusement les 4 autres si le role les accordait. Le OR est le choix sur qui echoue en
 * granted (jamais de perte de droit accidentelle) : un override sert a ACCORDER une exception,
 * jamais a la retirer.
 */
@Service
public class EffectivePermissionService {

    @Autowired
    private EnterpriseAccessService enterpriseAccessService;
    @Autowired
    private EnterpriseMembershipDao enterpriseMembershipDao;
    @Autowired
    private CollaboratorPermissionOverrideDao permissionOverrideDao;

    private EnterpriseMembership membershipFor(Long enterpriseId) {
        Collaborator current = enterpriseAccessService.getCurrentCollaborator();
        if (current == null || current.getId() == null || enterpriseId == null) {
            return null;
        }
        return enterpriseMembershipDao.findByCollaboratorId(current.getId()).stream()
                .filter(m -> m.getEnterprise() != null && enterpriseId.equals(m.getEnterprise().getId()))
                .findFirst()
                .orElse(null);
    }

    private boolean effective(Long enterpriseId, Function<CollaboratorRole, Boolean> roleGetter,
                               Function<CollaboratorPermissionOverride, Boolean> overrideGetter) {
        EnterpriseMembership membership = membershipFor(enterpriseId);
        if (membership == null) {
            return false;
        }
        boolean roleValue = membership.getCollaboratorRole() != null
                && Boolean.TRUE.equals(roleGetter.apply(membership.getCollaboratorRole()));
        if (roleValue) {
            return true;
        }
        List<CollaboratorPermissionOverride> overrides = permissionOverrideDao.findByEnterpriseMembershipId(membership.getId());
        if (overrides != null) {
            for (CollaboratorPermissionOverride override : overrides) {
                if (Boolean.TRUE.equals(overrideGetter.apply(override))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canManageUsers(Long enterpriseId) {
        return effective(enterpriseId, CollaboratorRole::getCanManageUsers, CollaboratorPermissionOverride::getCanManageUsers);
    }

    public boolean canManageFinancials(Long enterpriseId) {
        return effective(enterpriseId, CollaboratorRole::getCanManageFinancials, CollaboratorPermissionOverride::getCanManageFinancials);
    }

    public boolean canDeleteProperty(Long enterpriseId) {
        return effective(enterpriseId, CollaboratorRole::getCanDeleteProperty, CollaboratorPermissionOverride::getCanDeleteProperty);
    }

    public boolean canManageServiceProviders(Long enterpriseId) {
        return effective(enterpriseId, CollaboratorRole::getCanManageServiceProviders, CollaboratorPermissionOverride::getCanManageServiceProviders);
    }

    public boolean canManageAiUsage(Long enterpriseId) {
        return effective(enterpriseId, CollaboratorRole::getCanManageAiUsage, CollaboratorPermissionOverride::getCanManageAiUsage);
    }

    private void assertPermission(boolean granted, String actionLabel) {
        if (!granted) {
            throw new PermissionDeniedException(
                "Votre rôle ne vous autorise pas à " + actionLabel + ".",
                new String[]{actionLabel}
            );
        }
    }

    public void assertCanManageUsers(Long enterpriseId) {
        assertPermission(canManageUsers(enterpriseId), "gérer les utilisateurs de cette société");
    }

    public void assertCanManageFinancials(Long enterpriseId) {
        assertPermission(canManageFinancials(enterpriseId), "gérer les finances de cette société");
    }

    public void assertCanDeleteProperty(Long enterpriseId) {
        assertPermission(canDeleteProperty(enterpriseId), "supprimer une propriété de cette société");
    }

    public void assertCanManageServiceProviders(Long enterpriseId) {
        assertPermission(canManageServiceProviders(enterpriseId), "gérer les prestataires de cette société");
    }

    public void assertCanManageAiUsage(Long enterpriseId) {
        assertPermission(canManageAiUsage(enterpriseId), "utiliser le scan de facture par IA pour cette société");
    }
}
