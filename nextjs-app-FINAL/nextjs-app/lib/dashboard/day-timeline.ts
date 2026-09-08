import { ReservationDto } from "@/lib/types/Reservation";
import { TaskDto } from "@/lib/types/Task";
import { CANCELLED_STATUS_CODE } from "@/lib/compute-monthly-financials";

// Code seedé dans AppApplication.java (même convention que action-center.ts/health-score.ts).
const TASK_DONE_STATUS_CODE = "Terminee";

export type TimelineEventType = "arrival" | "departure" | "task";

export interface TimelineEvent {
  id: string;
  type: TimelineEventType;
  propertyName: string;
  title: string;
  href: string;
}

function todayIso(): string {
  return new Date().toISOString().slice(0, 10);
}

/**
 * Événements du jour pour la timeline du dashboard, calculés à partir des réservations et
 * tâches déjà chargées par la page (aucun appel API supplémentaire) :
 * - Arrivées : checkInDate == aujourd'hui, réservation non annulée.
 * - Départs : checkOutDate == aujourd'hui, réservation non annulée.
 * - Tâches à échéance : dueDate == aujourd'hui, statut != Terminee.
 *
 * Ordre : arrivées, puis départs, puis tâches - PAS de tri par heure, contrairement à ce que le
 * cahier des charges envisageait "si disponible" : aucune des trois entités n'a de composante
 * horaire en base (Reservation.checkInDate/checkOutDate et Task.dueDate sont des dates seules,
 * type LocalDate côté backend) - voir NOTES-dashboard-carte-timeline.md.
 */
export function computeDayTimeline(
  reservations: ReservationDto[],
  tasks: TaskDto[],
  role: "admin" | "collaborator" = "admin"
): TimelineEvent[] {
  const today = todayIso();
  const events: TimelineEvent[] = [];
  const reservationsHref = `/${role}/reservations`;
  const tasksHref = `/${role}/tasks`;

  reservations
    .filter((r) => r.checkInDate === today && r.reservationStatus?.code !== CANCELLED_STATUS_CODE)
    .forEach((r) => {
      events.push({
        id: `arrival-${r.id}`,
        type: "arrival",
        propertyName: r.property?.name ?? "—",
        title: r.reference || `Réservation #${r.id}`,
        href: reservationsHref,
      });
    });

  reservations
    .filter((r) => r.checkOutDate === today && r.reservationStatus?.code !== CANCELLED_STATUS_CODE)
    .forEach((r) => {
      events.push({
        id: `departure-${r.id}`,
        type: "departure",
        propertyName: r.property?.name ?? "—",
        title: r.reference || `Réservation #${r.id}`,
        href: reservationsHref,
      });
    });

  tasks
    .filter((t) => t.dueDate === today && t.taskStatus?.code !== TASK_DONE_STATUS_CODE)
    .forEach((t) => {
      events.push({
        id: `task-${t.id}`,
        type: "task",
        propertyName: t.property?.name ?? "—",
        title: t.title || `Tâche #${t.id}`,
        href: tasksHref,
      });
    });

  return events;
}
