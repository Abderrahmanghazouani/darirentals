// Auto-generated from Angular model: CollaboratorRole.model.ts
export interface CollaboratorRoleDto {
  id: number | null;
  description: string;
  code: string;
  label: string;
  style: string;
  isDefault?: boolean | null;
  sortOrder?: number | null;
  canManageFinancials?: boolean | null;
  canManageUsers?: boolean | null;
  canDeleteProperty?: boolean | null;
  canManageServiceProviders?: boolean | null;
  canManageAiUsage?: boolean | null;
}

export function newCollaboratorRoleDto(): CollaboratorRoleDto {
  return {
    id: null,
    description: '',
    code: '',
    label: '',
    style: '',
    isDefault: null,
    sortOrder: null,
    canManageFinancials: null,
    canManageUsers: null,
    canDeleteProperty: null,
    canManageServiceProviders: null,
    canManageAiUsage: null,
  };
}
