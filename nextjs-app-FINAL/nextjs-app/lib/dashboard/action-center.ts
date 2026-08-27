import { TaskDto } from "@/lib/types/Task";
import { ReservationRequestDto } from "@/lib/types/ReservationRequest";

// Codes seedés dans AppApplication.java.
const TASK_DONE_STATUS_CODE = "Terminee";
const REQUEST_PENDING_STATUS_CODE = "EnAttente";

export type ActionItemKind = "overdue-task" | "pending-request";

export interface ActionItem {
  id: string;
  kind: ActionItemKind;
  title: string;
  subtitle: string;
  href: string;
}

function todayIso(): string {
  return new Date().toISOString().slice(0, 10);
}

/** Tâches dont l'échéance est dépassée et qui ne sont pas encore terminées. */
export function computeOverdueTasks(tasks: TaskDto[]): ActionItem[] {
  const today = todayIso();
  return tasks
    .filter((t) => t.dueDate && t.dueDate < today && t.taskStatus?.code !== TASK_DONE_STATUS_CODE)
    .sort((a, b) => (a.dueDate ?? "").localeCompare(b.dueDate ?? ""))
    .map((t) => ({
      id: `task-${t.id}`,
      kind: "overdue-task" as const,
      title: t.title || `Tâche #${t.id}`,
      subtitle: `Échéance dépassée le ${t.dueDate}${t.property?.name ? " · " + t.property.name : ""}`,
      href: "/admin/tasks",
    }));
}

/**
 * Demandes de réservation au statut "EnAttente". Pas de seuil de délai (ex: "depuis plus de
 * 24h") : ReservationRequest n'a aucun champ de date de création en base (ni côté entité, ni
 * via un auditing JPA) - impossible de calculer une durée d'attente réelle. À ajouter plus
 * tard côté backend si le besoin se confirme.
 */
export function computePendingRequests(requests: ReservationRequestDto[]): ActionItem[] {
  return requests
    .filter((r) => r.reservationRequestStatus?.code === REQUEST_PENDING_STATUS_CODE)
    .map((r) => ({
      id: `request-${r.id}`,
      kind: "pending-request" as const,
      title: r.requestedProperty?.name ? `Demande — ${r.requestedProperty.name}` : `Demande #${r.id}`,
      subtitle: r.client?.fullName ? `Client : ${r.client.fullName}` : "En attente de traitement",
      href: "/admin/reservation-requests",
    }));
}

export function computeActionItems(tasks: TaskDto[], requests: ReservationRequestDto[]): ActionItem[] {
  return [...computeOverdueTasks(tasks), ...computePendingRequests(requests)];
}
