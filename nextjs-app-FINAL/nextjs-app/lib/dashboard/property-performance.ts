import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import { CANCELLED_STATUS_CODE } from "@/lib/compute-monthly-financials";

export interface PropertyPerformanceRow {
  propertyId: number;
  propertyName: string;
  propertyStatusCode: string | null;
  propertyStatusLabel: string | null;
  revenue: number;
  charges: number;
  netProfit: number;
  /** 0-100, arrondi, jamais > 100 même si des réservations se chevauchent. */
  occupancyPercent: number;
  reservationCount: number;
}

export type PropertyPerformanceSort = "netProfit" | "revenue" | "occupancy";

function currentMonthRange(): { start: Date; end: Date; daysInMonth: number } {
  const now = new Date();
  const start = new Date(now.getFullYear(), now.getMonth(), 1);
  const end = new Date(now.getFullYear(), now.getMonth() + 1, 1); // borne exclusive
  const daysInMonth = new Date(now.getFullYear(), now.getMonth() + 1, 0).getDate();
  return { start, end, daysInMonth };
}

/**
 * Nombre de nuits de l'intervalle [checkIn, checkOut) qui tombent dans [monthStart, monthEnd) -
 * 0 si aucun chevauchement. Une réservation qui déborde largement du mois (commencée le mois
 * d'avant, finissant le mois d'après) ne compte donc que les nuits réellement dans le mois en
 * cours, jamais plus.
 */
function nightsOverlapMonth(checkIn: Date, checkOut: Date, monthStart: Date, monthEnd: Date): number {
  const start = checkIn > monthStart ? checkIn : monthStart;
  const end = checkOut < monthEnd ? checkOut : monthEnd;
  const diffDays = (end.getTime() - start.getTime()) / 86400000;
  return diffDays > 0 ? diffDays : 0;
}

/**
 * Performance du mois en cours pour chaque propriété : revenus/charges/bénéfice net (mêmes
 * règles que computeMonthlyFinancials - réservations non annulées, dates dans le mois en
 * cours) et taux d'occupation (nuits réellement occupées ce mois-ci / jours du mois, plafonné
 * à 100% - voir nightsOverlapMonth pour la protection contre le débordement de mois).
 */
export function computePropertyPerformance(
  properties: PropertyDto[],
  reservations: ReservationDto[],
  charges: ChargeDto[]
): PropertyPerformanceRow[] {
  const { start, end, daysInMonth } = currentMonthRange();

  return properties
    .filter((p): p is PropertyDto & { id: number } => p.id != null)
    .map((p) => {
      const propertyReservations = reservations.filter(
        (r) => r.property?.id === p.id && r.reservationStatus?.code !== CANCELLED_STATUS_CODE
      );

      let revenue = 0;
      let nightsBooked = 0;
      let reservationCount = 0;
      for (const r of propertyReservations) {
        if (!r.checkInDate) continue;
        const checkIn = new Date(r.checkInDate);
        const checkInInMonth = checkIn >= start && checkIn < end;
        if (checkInInMonth && r.amount != null) {
          revenue += r.amount;
          reservationCount += 1;
        }
        if (r.checkOutDate) {
          const checkOut = new Date(r.checkOutDate);
          nightsBooked += nightsOverlapMonth(checkIn, checkOut, start, end);
        }
      }

      let chargesTotal = 0;
      for (const c of charges) {
        if (c.property?.id !== p.id || !c.chargeDate || c.amount == null) continue;
        const chargeDate = new Date(c.chargeDate);
        if (chargeDate >= start && chargeDate < end) {
          chargesTotal += c.amount;
        }
      }

      const occupancyPercent =
        daysInMonth > 0 ? Math.min(100, Math.max(0, Math.round((nightsBooked / daysInMonth) * 100))) : 0;

      return {
        propertyId: p.id,
        propertyName: p.name || `Propriété #${p.id}`,
        propertyStatusCode: p.propertyStatus?.code ?? null,
        propertyStatusLabel: p.propertyStatus?.label ?? null,
        revenue,
        charges: chargesTotal,
        netProfit: revenue - chargesTotal,
        occupancyPercent,
        reservationCount,
      };
    });
}

export function sortPropertyPerformance(
  rows: PropertyPerformanceRow[],
  sort: PropertyPerformanceSort
): PropertyPerformanceRow[] {
  const sorted = [...rows];
  if (sort === "revenue") {
    sorted.sort((a, b) => b.revenue - a.revenue);
  } else if (sort === "occupancy") {
    sorted.sort((a, b) => b.occupancyPercent - a.occupancyPercent);
  } else {
    sorted.sort((a, b) => b.netProfit - a.netProfit);
  }
  return sorted;
}
