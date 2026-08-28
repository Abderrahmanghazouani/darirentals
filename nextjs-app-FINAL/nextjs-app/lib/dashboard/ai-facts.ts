import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import { TaskDto } from "@/lib/types/Task";
import { ReservationRequestDto } from "@/lib/types/ReservationRequest";
import { computeMonthlyFinancials } from "@/lib/compute-monthly-financials";
import { computeRevenueMonthComparison } from "@/lib/dashboard/revenue-intelligence";
import { computeHealthScore, HEALTH_SCORE_LEVEL_LABEL } from "@/lib/dashboard/health-score";
import { computeOverdueTasks, computePendingRequests } from "@/lib/dashboard/action-center";
import { computePropertyPerformance, sortPropertyPerformance } from "@/lib/dashboard/property-performance";

/**
 * PRINCIPE FONDAMENTAL (voir NOTES-ai-assistant.md) : ce paquet est LA SEULE chose que
 * l'assistant IA (Gemini) reçoit sur le portefeuille. Il est construit ICI, côté frontend, à
 * partir des mêmes fonctions de calcul déjà utilisées par les cartes du Dashboard
 * (Health Score, Revenue Intelligence, Property Performance, Action Center) — jamais
 * recalculé différemment, jamais de nouvel accès à des données non affichées ailleurs. Gemini
 * ne voit que ce JSON, ne touche jamais la base, et doit dire "je ne sais pas" pour tout ce
 * qui n'y figure pas.
 */
export interface AssistantFacts {
  asOf: string;
  /** Tous les montants de ce paquet sont dans cette devise - jamais convertis, jamais une
   * autre devise (l'app stocke tout en MAD côté serveur ; la conversion à l'affichage est un
   * détail purement frontend qui n'a pas sa place dans les faits envoyés à l'IA). */
  currency: string;
  revenue: {
    currentMonth: number;
    previousMonth: number;
    percentChange: number | null;
    trend: string;
  };
  currentMonthCharges: number;
  healthScore: {
    total: number;
    level: string;
  };
  upcomingReservations: {
    propertyName: string;
    clientName: string;
    checkInDate: string;
  }[];
  overdueTasks: {
    title: string;
    detail: string;
  }[];
  pendingRequests: {
    title: string;
    detail: string;
  }[];
  topProperties: {
    propertyName: string;
    netProfit: number;
    occupancyPercent: number;
  }[];
}

const MAX_LIST_ITEMS = 5;

export function buildAssistantFacts(
  properties: PropertyDto[],
  reservations: ReservationDto[],
  charges: ChargeDto[],
  tasks: TaskDto[],
  reservationRequests: ReservationRequestDto[]
): AssistantFacts {
  const today = new Date().toISOString().slice(0, 10);

  const revenue = computeRevenueMonthComparison(reservations, charges);
  const [currentMonth] = computeMonthlyFinancials(reservations, charges, 1);
  const health = computeHealthScore(properties, reservations, charges, tasks);
  const overdueTasks = computeOverdueTasks(tasks);
  const pendingRequests = computePendingRequests(reservationRequests);
  const performance = sortPropertyPerformance(
    computePropertyPerformance(properties, reservations, charges),
    "netProfit"
  );

  const upcomingReservations = reservations
    .filter((r) => r.checkInDate && r.checkInDate >= today)
    .sort((a, b) => (a.checkInDate ?? "").localeCompare(b.checkInDate ?? ""))
    .slice(0, MAX_LIST_ITEMS)
    .map((r) => ({
      propertyName: r.property?.name ?? "Propriété inconnue",
      clientName: r.client?.fullName ?? "Client inconnu",
      checkInDate: r.checkInDate as string,
    }));

  return {
    asOf: today,
    currency: "MAD",
    revenue: {
      currentMonth: revenue.currentRevenue,
      previousMonth: revenue.previousRevenue,
      percentChange: revenue.percentChange,
      trend: revenue.trend,
    },
    currentMonthCharges: currentMonth?.charges ?? 0,
    healthScore: {
      total: health.total,
      level: HEALTH_SCORE_LEVEL_LABEL[health.level],
    },
    upcomingReservations,
    overdueTasks: overdueTasks.slice(0, MAX_LIST_ITEMS).map((item) => ({
      title: item.title,
      detail: item.subtitle,
    })),
    pendingRequests: pendingRequests.slice(0, MAX_LIST_ITEMS).map((item) => ({
      title: item.title,
      detail: item.subtitle,
    })),
    topProperties: performance.slice(0, 3).map((p) => ({
      propertyName: p.propertyName,
      netProfit: p.netProfit,
      occupancyPercent: p.occupancyPercent,
    })),
  };
}
