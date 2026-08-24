import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import { MonthlyFinancials } from "@/components/dashboard/monthly-chart";

const MONTH_LABELS = [
  "Jan", "Fév", "Mar", "Avr", "Mai", "Jun",
  "Jul", "Aoû", "Sep", "Oct", "Nov", "Déc",
];

function monthKey(dateStr: string): string {
  return dateStr.slice(0, 7); // "YYYY-MM"
}

function monthLabel(key: string): string {
  const [year, month] = key.split("-");
  const index = Number(month) - 1;
  return `${MONTH_LABELS[index] ?? month} ${year}`;
}

/** Construit les 6 derniers mois (y compris le mois en cours), avec revenus/charges/bénéfice. */
export function computeMonthlyFinancials(
  reservations: ReservationDto[],
  charges: ChargeDto[],
  monthsCount = 6
): MonthlyFinancials[] {
  const now = new Date();
  const keys: string[] = [];
  for (let i = monthsCount - 1; i >= 0; i--) {
    const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
    const key = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, "0")}`;
    keys.push(key);
  }

  const revenueByMonth: Record<string, number> = {};
  const chargesByMonth: Record<string, number> = {};

  for (const r of reservations) {
    if (!r.checkInDate || r.amount == null) continue;
    const key = monthKey(r.checkInDate);
    revenueByMonth[key] = (revenueByMonth[key] ?? 0) + r.amount;
  }

  for (const c of charges) {
    if (!c.chargeDate || c.amount == null) continue;
    const key = monthKey(c.chargeDate);
    chargesByMonth[key] = (chargesByMonth[key] ?? 0) + c.amount;
  }

  return keys.map((key) => {
    const revenue = revenueByMonth[key] ?? 0;
    const chargesTotal = chargesByMonth[key] ?? 0;
    return {
      month: monthLabel(key),
      revenue,
      charges: chargesTotal,
      profit: revenue - chargesTotal,
    };
  });
}