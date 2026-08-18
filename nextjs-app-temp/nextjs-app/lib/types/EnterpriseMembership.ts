// Auto-generated from Angular model: EnterpriseMembership.model.ts
import { CollaboratorDto } from "./Collaborator";
import { CollaboratorPermissionOverrideDto } from "./CollaboratorPermissionOverride";
import { CollaboratorRoleDto } from "./CollaboratorRole";
import { EnterpriseDto } from "./Enterprise";

export interface EnterpriseMembershipDto {
  id: number | null;
  collaborator?: CollaboratorDto | null;
  enterprise?: EnterpriseDto | null;
  collaboratorRole?: CollaboratorRoleDto | null;
  collaboratorPermissionOverrides: CollaboratorPermissionOverrideDto[];
}

export function newEnterpriseMembershipDto(): EnterpriseMembershipDto {
  return {
    id: null,
    collaborator: null,
    enterprise: null,
    collaboratorRole: null,
    collaboratorPermissionOverrides: [],
  };
}
