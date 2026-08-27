import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import { MonthlyFinancials } from "@/components/dashboard/monthly-chart";
import { computeMonthlyFinancials, FinancialsGranularity } from "@/lib/compute-monthly-financials";

export type RevenuePeriod = "7d" | "30d" | "3m" | "12m";

interface PeriodConfig {
  count: number;
  granularity: FinancialsGranularity;
  label: string;
}

export const REVENUE_PERIODS: Record<RevenuePeriod, PeriodConfig> = {
  "7d": { count: 7, granularity: "day", label: "7 jours" },
  "30d": { count: 30, granularity: "day", label: "30 jours" },
  "3m": { count: 3, granularity: "month", label: "3 mois" },
  "12m": { count: 12, granularity: "month", label: "12 mois" },
};

export const REVENUE_PERIOD_KEYS = Object.keys(REVENUE_PERIODS) as RevenuePeriod[];

/** Série revenus/charges/bénéfice pour la période choisie (7j/30j en jours, 3/12 mois en mois). */
export function computeRevenueSeries(
  reservations: ReservationDto[],
  charges: ChargeDto[],
  period: RevenuePeriod
): MonthlyFinancials[] {
  const { count, granularity } = REVENUE_PERIODS[period];
  return computeMonthlyFinancials(reservations, charges, count, granularity);
}

export type RevenueTrend = "up" | "down" | "stable" | "new";

export interface RevenueMonthComparison {
  currentRevenue: number;
  previousRevenue: number;
  /** null uniquement quand il n'y a aucune base de comparaison (mois précédent à 0 mais mois
   * courant > 0) - un pourcentage de variation n'aurait alors pas de sens (division par 0). */
  percentChange: number | null;
  trend: RevenueTrend;
  /** Phrase construite uniquement à partir des chiffres ci-dessus - pas de prévision, pas d'IA. */
  summary: string;
}

/** En dessous de ce seuil (en valeur absolue), la variation est annoncée comme "stable". */
const STABLE_THRESHOLD_PERCENT = 1;

export function computeRevenueMonthComparison(
  reservations: ReservationDto[],
  charges: ChargeDto[]
): RevenueMonthComparison {
  const [previous, current] = computeMonthlyFinancials(reservations, charges, 2);
  const currentRevenue = current?.revenue ?? 0;
  const previousRevenue = previous?.revenue ?? 0;

  if (previousRevenue === 0) {
    if (currentRevenue === 0) {
      return {
        currentRevenue,
        previousRevenue,
        percentChange: 0,
        trend: "stable",
        summary: "Le revenu est stable ce mois-ci (aucun revenu ce mois-ci ni le mois dernier).",
      };
    }
    return {
      currentRevenue,
      previousRevenue,
      percentChange: null,
      trend: "new",
      summary: "Le revenu est en hausse ce mois-ci (aucun revenu enregistré le mois dernier pour comparer).",
    };
  }

  const percentChange = ((currentRevenue - previousRevenue) / previousRevenue) * 100;

  if (Math.abs(percentChange) < STABLE_THRESHOLD_PERCENT) {
    return {
      currentRevenue,
      previousRevenue,
      percentChange,
      trend: "stable",
      summary: `Le revenu est stable ce mois-ci (${percentChange >= 0 ? "+" : ""}${percentChange.toFixed(1)}% vs le mois dernier).`,
    };
  }

  if (percentChange > 0) {
    return {
      currentRevenue,
      previousRevenue,
      percentChange,
      trend: "up",
      summary: `Le revenu est en hausse de ${percentChange.toFixed(1)}% ce mois-ci par rapport au mois dernier.`,
    };
  }

  return {
    currentRevenue,
    previousRevenue,
    percentChange,
    trend: "down",
    summary: `Le revenu est en baisse de ${Math.abs(percentChange).toFixed(1)}% ce mois-ci par rapport au mois dernier.`,
  };
}
