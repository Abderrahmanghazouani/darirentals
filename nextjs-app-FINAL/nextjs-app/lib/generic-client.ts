import { createEntityClient, Role } from "./api-client";

export interface GenericDto {
  id: number | null;
  [key: string]: unknown;
}

/** Client CRUD générique pour une entité identifiée par sa clé de ressource (ex: "currency"). */
export function getGenericClient(resource: string, role: Role) {
  return createEntityClient<GenericDto>(resource, role);
}
