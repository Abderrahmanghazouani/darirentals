import { API_BASE, authHeaders, Role } from "./api-client";

export interface AvailabilityRequest {
  propertyId: number;
  checkInDate: string; // "yyyy-MM-dd"
  checkOutDate: string; // "yyyy-MM-dd"
  excludeReservationId?: number | null;
}

/** Vérifie côté serveur qu'aucune réservation existante ne chevauche la période demandée. */
export async function checkAvailability(
  request: AvailabilityRequest,
  role: Role = "admin"
): Promise<boolean> {
  const res = await fetch(`${API_BASE}${role}/reservation/check-availability`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify(request),
  });
  if (!res.ok) {
    // En cas d'erreur réseau/serveur, on laisse la validation finale au submit
    // (le backend revalidera de toute façon à la création/modification).
    return true;
  }
  const data = await res.json();
  return Boolean(data?.available);
}
