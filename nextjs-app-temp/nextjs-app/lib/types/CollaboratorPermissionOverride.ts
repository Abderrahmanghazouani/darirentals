// Auto-generated from Angular model: CollaboratorPermissionOverride.model.ts
import { EnterpriseMembershipDto } from "./EnterpriseMembership";

export interface CollaboratorPermissionOverrideDto {
  id: number | null;
  canManageFinancials?: boolean | null;
  canManageUsers?: boolean | null;
  canDeleteProperty?: boolean | null;
  canManageServiceProviders?: boolean | null;
  canManageAiUsage?: boolean | null;
  enterpriseMembership?: EnterpriseMembershipDto | null;
}

export function newCollaboratorPermissionOverrideDto(): CollaboratorPermissionOverrideDto {
  return {
    id: null,
    canManageFinancials: null,
    canManageUsers: null,
    canDeleteProperty: null,
    canManageServiceProviders: null,
    canManageAiUsage: null,
    enterpriseMembership: null,
  };
}
