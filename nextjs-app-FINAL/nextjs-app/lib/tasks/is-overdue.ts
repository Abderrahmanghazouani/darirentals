import { TaskDto } from "@/lib/types/Task";
import { TaskStatusDto } from "@/lib/types/TaskStatus";

// Le générateur ne fige pas les codes de statut (créés librement via le CRUD TaskStatus), donc on
// détecte "Terminée" par heuristique sur le libellé/code plutôt que de dépendre d'un code fixe
// (même approche que pour les réservations annulées).
export function looksDone(status?: TaskStatusDto | null): boolean {
  if (!status) return false;
  const probe = `${status.code ?? ""} ${status.label ?? ""}`.toLowerCase();
  return (
    probe.includes("done") ||
    probe.includes("termin") ||
    probe.includes("complet") ||
    probe.includes("fini")
  );
}

export function isOverdue(task: TaskDto): boolean {
  if (!task.dueDate || looksDone(task.taskStatus)) return false;
  const today = new Date().toISOString().slice(0, 10);
  return task.dueDate < today;
}

/** Tâche à traiter aujourd'hui : due aujourd'hui ou déjà en retard, et non terminée. */
export function isDueTodayOrOverdue(task: TaskDto): boolean {
  if (!task.dueDate || looksDone(task.taskStatus)) return false;
  const today = new Date().toISOString().slice(0, 10);
  return task.dueDate <= today;
}
