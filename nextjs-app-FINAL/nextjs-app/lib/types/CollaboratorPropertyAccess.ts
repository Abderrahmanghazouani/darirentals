// Chantier 3 (permissions reelles) : liaison Collaborator <-> Property.
import { CollaboratorDto } from "./Collaborator";
import { PropertyDto } from "./Property";

export interface CollaboratorPropertyAccessDto {
  id: number | null;
  collaborator?: CollaboratorDto | null;
  property?: PropertyDto | null;
}

export function newCollaboratorPropertyAccessDto(): CollaboratorPropertyAccessDto {
  return {
    id: null,
    collaborator: null,
    property: null,
  };
}
