import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import { TaskDto } from "@/lib/types/Task";
import { computeMonthlyFinancials, CANCELLED_STATUS_CODE } from "@/lib/compute-monthly-financials";

// Codes seedés dans AppApplication.java - voir aussi CANCELLED_STATUS_CODE.
const ACTIVE_PROPERTY_STATUS_CODE = "Active";
const TASK_DONE_STATUS_CODE = "Terminee";

/** Marge bénéficiaire cible (30%) : pas de champ dédié en base pour un objectif métier,
 * valeur de référence ajustable ici si besoin. */
const TARGET_PROFIT_MARGIN = 0.3;

/** Fenêtre glissante pour le taux d'annulation des réservations. */
const CANCELLATION_WINDOW_DAYS = 90;

export type HealthScoreLevel = "excellent" | "good" | "watch" | "critical";

export interface HealthScoreComponent {
  key: "financial" | "portfolio" | "reliability" | "responsiveness";
  label: string;
  /** Poids dans le score global, entre 0 et 1 (somme des 4 = 1). */
  weight: number;
  /** Sous-score déjà arrondi, entre 0 et 100. */
  score: number;
  /** Explication chiffrée à partir des données réelles - jamais une boîte noire. */
  detail: string;
}

export interface HealthScore {
  total: number;
  level: HealthScoreLevel;
  components: HealthScoreComponent[];
}

function clamp(n: number, min = 0, max = 100): number {
  return Math.max(min, Math.min(max, n));
}

function levelFor(total: number): HealthScoreLevel {
  if (total >= 85) return "excellent";
  if (total >= 65) return "good";
  if (total >= 45) return "watch";
  return "critical";
}

export const HEALTH_SCORE_LEVEL_LABEL: Record<HealthScoreLevel, string> = {
  excellent: "Excellent",
  good: "Bon",
  watch: "À surveiller",
  critical: "Critique",
};

/**
 * Score de santé du portefeuille (0-100), composé de 4 sous-scores pondérés,
 * calculés uniquement à partir de données réellement disponibles (aucune valeur inventée) :
 *
 * - Performance financière (35%) : marge bénéficiaire du mois en cours vs objectif de 30%,
 *   sur les réservations non annulées (voir computeMonthlyFinancials).
 * - Santé du portefeuille (25%) : proportion de propriétés au statut "Active".
 * - Fiabilité des réservations (25%) : 100 - taux d'annulation sur les 90 derniers jours.
 * - Réactivité opérationnelle (15%) : 100 - proportion de tâches ouvertes en retard.
 */
export function computeHealthScore(
  properties: PropertyDto[],
  reservations: ReservationDto[],
  charges: ChargeDto[],
  tasks: TaskDto[]
): HealthScore {
  // A. Performance financière (35%)
  const [current] = computeMonthlyFinancials(reservations, charges, 1);
  const revenue = current?.revenue ?? 0;
  const chargesTotal = current?.charges ?? 0;
  const profit = current?.profit ?? 0;
  const margin = revenue > 0 ? profit / revenue : 0;
  const financialScore = revenue > 0 ? clamp(Math.round((margin / TARGET_PROFIT_MARGIN) * 100)) : 0;
  const financialDetail =
    revenue > 0
      ? `Marge du mois en cours : ${(margin * 100).toFixed(1)}% (objectif ${(TARGET_PROFIT_MARGIN * 100).toFixed(0)}%) — revenus ${revenue.toLocaleString("fr-FR")} MAD, charges ${chargesTotal.toLocaleString("fr-FR")} MAD`
      : "Aucun revenu enregistré ce mois-ci (réservations non annulées)";

  // B. Santé du portefeuille (25%)
  const totalProperties = properties.length;
  const activeProperties = properties.filter((p) => p.propertyStatus?.code === ACTIVE_PROPERTY_STATUS_CODE).length;
  const portfolioScore = totalProperties > 0 ? clamp(Math.round((activeProperties / totalProperties) * 100)) : 100;
  const portfolioDetail =
    totalProperties > 0
      ? `${activeProperties}/${totalProperties} propriété(s) au statut "Active"`
      : "Aucune propriété enregistrée";

  // C. Fiabilité des réservations (25%)
  const windowStart = new Date();
  windowStart.setDate(windowStart.getDate() - CANCELLATION_WINDOW_DAYS);
  const windowStartIso = windowStart.toISOString().slice(0, 10);
  const recentReservations = reservations.filter((r) => r.checkInDate && r.checkInDate >= windowStartIso);
  const cancelledRecent = recentReservations.filter((r) => r.reservationStatus?.code === CANCELLED_STATUS_CODE).length;
  const reliabilityScore =
    recentReservations.length > 0
      ? clamp(Math.round(100 - (cancelledRecent / recentReservations.length) * 100))
      : 100;
  const reliabilityDetail =
    recentReservations.length > 0
      ? `${cancelledRecent}/${recentReservations.length} réservation(s) annulée(s) sur les ${CANCELLATION_WINDOW_DAYS} derniers jours`
      : `Aucune réservation sur les ${CANCELLATION_WINDOW_DAYS} derniers jours`;

  // D. Réactivité opérationnelle (15%)
  const today = new Date().toISOString().slice(0, 10);
  const openTasks = tasks.filter((t) => t.taskStatus?.code !== TASK_DONE_STATUS_CODE);
  const overdueTasks = openTasks.filter((t) => t.dueDate && t.dueDate < today);
  const responsivenessScore =
    openTasks.length > 0 ? clamp(Math.round(100 - (overdueTasks.length / openTasks.length) * 100)) : 100;
  const responsivenessDetail =
    openTasks.length > 0
      ? `${overdueTasks.length}/${openTasks.length} tâche(s) ouverte(s) en retard`
      : "Aucune tâche ouverte";

  const components: HealthScoreComponent[] = [
    {
      key: "financial",
      label: "Performance financière",
      weight: 0.35,
      score: financialScore,
      detail: financialDetail,
    },
    {
      key: "portfolio",
      label: "Santé du portefeuille",
      weight: 0.25,
      score: portfolioScore,
      detail: portfolioDetail,
    },
    {
      key: "reliability",
      label: "Fiabilité des réservations",
      weight: 0.25,
      score: reliabilityScore,
      detail: reliabilityDetail,
    },
    {
      key: "responsiveness",
      label: "Réactivité opérationnelle",
      weight: 0.15,
      score: responsivenessScore,
      detail: responsivenessDetail,
    },
  ];

  const total = clamp(Math.round(components.reduce((sum, c) => sum + c.score * c.weight, 0)));

  return { total, level: levelFor(total), components };
}
