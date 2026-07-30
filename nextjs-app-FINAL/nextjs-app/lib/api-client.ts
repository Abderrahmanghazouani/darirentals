// Client API générique, calqué sur le contrat REST du backend Java existant.
// Chaque entité utilise ces mêmes routes : voir shared/service/*.ts côté Angular d'origine.

export const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8036/api/";

export type Role = "admin" | "collaborator";

export interface PaginatedList<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  number: number;
  size: number;
}

/** Erreur levée quand le serveur répond 401/403 : le token est absent/expiré/invalide. */
export class UnauthorizedError extends Error {
  constructor(status: number) {
    super(`Non autorisé (${status})`);
    this.name = "UnauthorizedError";
  }
}

function resourceUrl(role: Role, resource: string) {
  // ex: http://localhost:8036/api/admin/currency/
  return `${API_BASE}${role}/${resource}/`;
}

function authHeaders(): Record<string, string> {
  if (typeof window === "undefined") return {};
  const token = localStorage.getItem("token"); // déjà au format "Bearer xxx"
  return token ? { Authorization: token } : {};
}

async function handle<T>(res: Response): Promise<T> {
  if (res.status === 401 || res.status === 403) {
    throw new UnauthorizedError(res.status);
  }
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`API error ${res.status}: ${text || res.statusText}`);
  }
  // DELETE endpoints sometimes return no body
  const text = await res.text();
  return (text ? JSON.parse(text) : (undefined as unknown)) as T;
}

/**
 * Fabrique un client CRUD pour une entité donnée (ex: "currency", "task"...).
 * Reproduit les mêmes appels que les services Angular (*.service.ts), avec le
 * token JWT automatiquement attaché (équivalent du JwtInterceptor Angular).
 */
export function createEntityClient<TDto extends { id: number | null }, TCriteria = Record<string, unknown>>(
  resource: string,
  role: Role = "admin"
) {
  const base = resourceUrl(role, resource);

  return {
    async findAll(): Promise<TDto[]> {
      const res = await fetch(base, { cache: "no-store", headers: authHeaders() });
      return handle<TDto[]>(res);
    },

    async findAllOptimized(): Promise<TDto[]> {
      const res = await fetch(`${base}optimized`, { cache: "no-store", headers: authHeaders() });
      return handle<TDto[]>(res);
    },

    async findPaginatedByCriteria(
      criteria: Partial<TCriteria> & Record<string, unknown>
    ): Promise<PaginatedList<TDto>> {
      const res = await fetch(`${base}find-paginated-by-criteria`, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(criteria),
        cache: "no-store",
      });
      return handle<PaginatedList<TDto>>(res);
    },

    async findByCriteria(
      criteria: Partial<TCriteria> & Record<string, unknown>
    ): Promise<TDto[]> {
      const res = await fetch(`${base}find-by-criteria`, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(criteria),
        cache: "no-store",
      });
      return handle<TDto[]>(res);
    },

    async findById(id: number): Promise<TDto> {
      const res = await fetch(`${base}id/${id}`, { cache: "no-store", headers: authHeaders() });
      return handle<TDto>(res);
    },

    async create(dto: TDto): Promise<TDto> {
      const res = await fetch(base, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(dto),
      });
      return handle<TDto>(res);
    },

    async update(dto: TDto): Promise<TDto> {
      const res = await fetch(base, {
        method: "PUT",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(dto),
      });
      return handle<TDto>(res);
    },

    async remove(id: number): Promise<void> {
      const res = await fetch(`${base}id/${id}`, { method: "DELETE", headers: authHeaders() });
      await handle<void>(res);
    },

    async removeMultiple(ids: number[]): Promise<void> {
      const res = await fetch(`${base}multiple`, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify(ids),
      });
      await handle<void>(res);
    },

    async importExcel(file: File): Promise<TDto[]> {
      const formData = new FormData();
      formData.append("file", file, file.name);
      const res = await fetch(`${base}import-excel`, {
        method: "POST",
        headers: authHeaders(),
        body: formData,
      });
      return handle<TDto[]>(res);
    },
  };
}
