import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import { MonthlyFinancials } from "@/components/dashboard/monthly-chart";

const MONTH_LABELS = [
  "Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
  "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc",
];

// Code seedé dans AppApplication.createReservationStatus() - voir aussi totalRevenue dans
// app/admin/page.tsx, qui doit exclure les mêmes réservations annulées.
export const CANCELLED_STATUS_CODE = "Annulee";

export type FinancialsGranularity = "day" | "month";

function monthKey(dateStr: string): string {
  return dateStr.slice(0, 7); // "YYYY-MM"
}

function dayKey(dateStr: string): string {
  return dateStr.slice(0, 10); // "YYYY-MM-DD"
}

function monthLabel(key: string): string {
  const [year, month] = key.split("-");
  const index = Number(month) - 1;
  return `${MONTH_LABELS[index] ?? month} ${year}`;
}

function dayLabel(key: string): string {
  const [year, month, day] = key.split("-").map(Number);
  return new Date(year, month - 1, day).toLocaleDateString("fr-FR", { day: "2-digit", month: "short" });
}

/**
 * Construit une série revenus/charges/bénéfice sur les `count` dernières unités de temps
 * (mois par défaut, jours si granularity="day"), unité en cours incluse.
 *
 * Compatibilité : le comportement par défaut (3 arguments, granularity="month") reste
 * strictement identique à avant - voir les appels existants dans app/admin/page.tsx et
 * lib/dashboard/health-score.ts, qui ne passent jamais de 4e argument.
 */
export function computeMonthlyFinancials(
  reservations: ReservationDto[],
  charges: ChargeDto[],
  count = 6,
  granularity: FinancialsGranularity = "month"
): MonthlyFinancials[] {
  const now = new Date();
  const keys: string[] = [];
  for (let i = count - 1; i >= 0; i--) {
    if (granularity === "day") {
      const d = new Date(now.getFullYear(), now.getMonth(), now.getDate() - i);
      keys.push(
        `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}-${String(d.getDate()).padStart(2, "0")}`
      );
    } else {
      const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
      keys.push(`${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}`);
    }
  }

  const keyOf = granularity === "day" ? dayKey : monthKey;
  const labelOf = granularity === "day" ? dayLabel : monthLabel;

  const revenueByKey: Record<string, number> = {};
  const chargesByKey: Record<string, number> = {};

  for (const r of reservations) {
    if (!r.checkInDate || r.amount == null) continue;
    if (r.reservationStatus?.code === CANCELLED_STATUS_CODE) continue;
    const key = keyOf(r.checkInDate);
    revenueByKey[key] = (revenueByKey[key] ?? 0) + r.amount;
  }

  for (const c of charges) {
    if (!c.chargeDate || c.amount == null) continue;
    const key = keyOf(c.chargeDate);
    chargesByKey[key] = (chargesByKey[key] ?? 0) + c.amount;
  }

  return keys.map((key) => {
    const revenue = revenueByKey[key] ?? 0;
    const chargesTotal = chargesByKey[key] ?? 0;
    return {
      month: labelOf(key),
      revenue,
      charges: chargesTotal,
      profit: revenue - chargesTotal,
    };
  });
}
