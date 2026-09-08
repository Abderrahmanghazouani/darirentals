import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";

// Codes seedés dans AppApplication.java (même convention que action-center.ts/health-score.ts).
const CONFIRMED_STATUS_CODE = "Confirmee";
const ENDING_SOON_WINDOW_DAYS = 3;

export type PropertyMapStatus = "active" | "ending-soon" | "idle";

export interface PropertyMapPoint {
  id: number;
  name: string;
  latitude: number;
  longitude: number;
  status: PropertyMapStatus;
}

function todayIso(): string {
  return new Date().toISOString().slice(0, 10);
}

function addDaysIso(iso: string, days: number): string {
  const d = new Date(iso + "T00:00:00Z");
  d.setUTCDate(d.getUTCDate() + days);
  return d.toISOString().slice(0, 10);
}

/**
 * Statut visuel de chaque propriété pour la carte du dashboard - déduit des réservations
 * réelles (aucun nouveau champ en base) :
 * - "active" (vert) : une réservation confirmée couvre aujourd'hui (checkInDate <= aujourd'hui
 *   <= checkOutDate).
 * - "ending-soon" (ambre) : une réservation active se termine dans les ENDING_SOON_WINDOW_DAYS
 *   prochains jours (aujourd'hui inclus).
 * - "idle" (gris) : aucune réservation confirmée en cours.
 *
 * Les propriétés sans latitude/longitude sont exclues du résultat (pas affichées sur la carte,
 * jamais d'erreur) - c'est la responsabilité de cette fonction de filtrer, pas du composant.
 */
export function computePropertyMapPoints(
  properties: PropertyDto[],
  reservations: ReservationDto[]
): PropertyMapPoint[] {
  const today = todayIso();
  const soonLimit = addDaysIso(today, ENDING_SOON_WINDOW_DAYS);

  return properties
    .filter(
      (p): p is PropertyDto & { id: number; latitude: number; longitude: number } =>
        p.id != null &&
        p.latitude != null &&
        p.longitude != null &&
        // (0, 0) = "Null Island", au large du Golfe de Guinée - jamais une vraie position de
        // propriété. En pratique le backend semble stocker 0/0 par défaut plutôt que null tant
        // que la position n'a jamais été renseignée (constaté sur les données réelles : 4
        // propriétés sur 5 sont à 0/0) - traité comme "pas de position" au même titre que null,
        // sinon la carte zoome jusqu'à englober l'Atlantique pour rien.
        !(p.latitude === 0 && p.longitude === 0)
    )
    .map((p) => {
      const activeReservations = reservations.filter(
        (r) =>
          r.property?.id === p.id &&
          r.reservationStatus?.code === CONFIRMED_STATUS_CODE &&
          r.checkInDate != null &&
          r.checkOutDate != null &&
          r.checkInDate <= today &&
          r.checkOutDate >= today
      );

      let status: PropertyMapStatus = "idle";
      if (activeReservations.length > 0) {
        const endingSoon = activeReservations.some((r) => (r.checkOutDate as string) <= soonLimit);
        status = endingSoon ? "ending-soon" : "active";
      }

      return {
        id: p.id,
        name: p.name,
        latitude: p.latitude,
        longitude: p.longitude,
        status,
      };
    });
}
