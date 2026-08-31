"use client";

import { useEffect, useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Plus, AlertTriangle } from "lucide-react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

import { EntityTable, EntityColumn } from "@/components/crud/entity-table";
import { EntityFormDialog } from "@/components/crud/entity-form-dialog";
import { useEntityCrud } from "@/lib/use-entity-crud";
import { useRequireRole } from "@/lib/use-require-role";
import { getEntityClients } from "@/lib/api";
import { TaskDto } from "@/lib/types/Task";
import { PropertyDto } from "@/lib/types/Property";
import { CollaboratorDto } from "@/lib/types/Collaborator";
import { TaskStatusDto } from "@/lib/types/TaskStatus";
import { TaskPriorityDto } from "@/lib/types/TaskPriority";
import { TaskForm } from "@/components/tasks/task-form";
import { isOverdue } from "@/lib/tasks/is-overdue";
import { StatusBadge } from "@/components/status-badge";

const ROLE = "admin" as const;

// Priorités : urgente=destructive, normale=info, basse=secondary (mapping plateforme).
function priorityVariant(
  style?: string | null
): "default" | "secondary" | "destructive" | "outline" | "info" {
  const s = (style ?? "").toLowerCase();
  if (s.includes("danger") || s.includes("destructive") || s.includes("urgent") || s.includes("haut") || s.includes("high"))
    return "destructive";
  if (s.includes("warning") || s.includes("bas") || s.includes("low")) return "secondary";
  if (s.includes("success") || s.includes("primary") || s.includes("normal") || s.includes("info"))
    return "info";
  return "outline";
}

export default function TasksPage() {
  const ready = useRequireRole(ROLE);
  const client = useMemo(() => getEntityClients(ROLE).task, []);
  const crud = useEntityCrud<TaskDto>(client);

  const [properties, setProperties] = useState<PropertyDto[]>([]);
  const [collaborators, setCollaborators] = useState<CollaboratorDto[]>([]);
  const [statuses, setStatuses] = useState<TaskStatusDto[]>([]);
  const [priorities, setPriorities] = useState<TaskPriorityDto[]>([]);

  useEffect(() => {
    const clients = getEntityClients(ROLE);
    clients.property.findAll().then((d) => setProperties(d ?? [])).catch(() => setProperties([]));
    clients.collaborator.findAll().then((d) => setCollaborators(d ?? [])).catch(() => setCollaborators([]));
    clients.taskStatus.findAll().then((d) => setStatuses(d ?? [])).catch(() => setStatuses([]));
    clients.taskPriority.findAll().then((d) => setPriorities(d ?? [])).catch(() => setPriorities([]));
  }, []);

  const [filterProperty, setFilterProperty] = useState<string>("all");
  const [filterStatus, setFilterStatus] = useState<string>("all");
  const [filterPriority, setFilterPriority] = useState<string>("all");
  const [filterAssignee, setFilterAssignee] = useState<string>("all");
  const [onlyOverdue, setOnlyOverdue] = useState(false);

  const filteredItems = useMemo(() => {
    return crud.items.filter((t) => {
      if (filterProperty !== "all" && String(t.property?.id ?? "") !== filterProperty) return false;
      if (filterStatus !== "all" && String(t.taskStatus?.id ?? "") !== filterStatus) return false;
      if (filterPriority !== "all" && String(t.taskPriority?.id ?? "") !== filterPriority) return false;
      if (filterAssignee !== "all" && String(t.assignedTo?.id ?? "") !== filterAssignee) return false;
      if (onlyOverdue && !isOverdue(t)) return false;
      return true;
    });
  }, [crud.items, filterProperty, filterStatus, filterPriority, filterAssignee, onlyOverdue]);

  const overdueCount = useMemo(() => crud.items.filter(isOverdue).length, [crud.items]);

  const columns: EntityColumn<TaskDto>[] = [
    {
      header: "Titre",
      render: (t) => (
        <span className="flex items-center gap-1.5">
          {isOverdue(t) && <AlertTriangle className="size-3.5 text-destructive-text shrink-0" />}
          {t.title}
        </span>
      ),
    },
    { header: "Propriété", render: (t) => t.property?.name ?? "—" },
    {
      header: "Échéance",
      render: (t) =>
        t.dueDate ? (
          <span className={isOverdue(t) ? "text-destructive-text font-medium" : ""}>{t.dueDate}</span>
        ) : (
          "—"
        ),
    },
    { header: "Assignée à", render: (t) => t.assignedTo?.name ?? "—" },
    {
      header: "Type",
      render: (t) => (t.taskType ? <Badge variant="outline">{t.taskType.label}</Badge> : "—"),
    },
    {
      header: "Priorité",
      render: (t) =>
        t.taskPriority ? (
          <Badge variant={priorityVariant(t.taskPriority.style)}>{t.taskPriority.label}</Badge>
        ) : (
          "—"
        ),
    },
    {
      header: "Statut",
      render: (t) => <StatusBadge status={t.taskStatus} />,
    },
  ];

  if (!ready) return null;

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Tâches</CardTitle>
          <Button onClick={crud.openCreate}>
            <Plus /> Nouvelle tâche
          </Button>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex flex-wrap items-center gap-3">
            <Select value={filterProperty} onValueChange={setFilterProperty}>
              <SelectTrigger className="w-[180px]">
                <SelectValue placeholder="Propriété" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Toutes les propriétés</SelectItem>
                {properties.map((p) => (
                  <SelectItem key={p.id} value={String(p.id)}>
                    {p.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Select value={filterStatus} onValueChange={setFilterStatus}>
              <SelectTrigger className="w-[160px]">
                <SelectValue placeholder="Statut" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tous les statuts</SelectItem>
                {statuses.map((s) => (
                  <SelectItem key={s.id} value={String(s.id)}>
                    {s.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Select value={filterPriority} onValueChange={setFilterPriority}>
              <SelectTrigger className="w-[160px]">
                <SelectValue placeholder="Priorité" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Toutes priorités</SelectItem>
                {priorities.map((p) => (
                  <SelectItem key={p.id} value={String(p.id)}>
                    {p.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Select value={filterAssignee} onValueChange={setFilterAssignee}>
              <SelectTrigger className="w-[180px]">
                <SelectValue placeholder="Assignée à" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tous les collaborateurs</SelectItem>
                {collaborators.map((c) => (
                  <SelectItem key={c.id} value={String(c.id)}>
                    {c.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Button
              type="button"
              variant={onlyOverdue ? "destructive" : "outline"}
              size="sm"
              onClick={() => setOnlyOverdue((v) => !v)}
            >
              <AlertTriangle className="size-4" />
              En retard {overdueCount > 0 ? `(${overdueCount})` : ""}
            </Button>
          </div>

          {crud.error && <p className="text-destructive-text text-sm">{crud.error}</p>}

          <EntityTable<TaskDto>
            items={filteredItems}
            loading={crud.loading}
            onEdit={crud.openEdit}
            onDelete={crud.setDeleteTarget}
            columns={columns}
          />
        </CardContent>
      </Card>

      <EntityFormDialog
        open={crud.formOpen}
        onOpenChange={(open) => (open ? undefined : crud.closeForm())}
        title={crud.editingItem ? "Modifier la tâche" : "Nouvelle tâche"}
      >
        <TaskForm
          initial={crud.editingItem}
          saving={crud.saving}
          role={ROLE}
          onSubmit={crud.submit}
          onCancel={crud.closeForm}
        />
      </EntityFormDialog>

      <AlertDialog
        open={crud.deleteTarget != null}
        onOpenChange={(open) => (open ? undefined : crud.setDeleteTarget(null))}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Supprimer cette tâche ?</AlertDialogTitle>
            <AlertDialogDescription>
              Cette action est irréversible. La tâche{" "}
              <strong>{crud.deleteTarget?.title}</strong> sera définitivement supprimée.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Annuler</AlertDialogCancel>
            <AlertDialogAction onClick={crud.confirmDelete}>Supprimer</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}