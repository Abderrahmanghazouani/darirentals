"use client";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { DialogFooter } from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { TaskDto, newTaskDto } from "@/lib/types/Task";
import { PropertyDto } from "@/lib/types/Property";
import { CollaboratorDto } from "@/lib/types/Collaborator";
import { TaskTypeDto } from "@/lib/types/TaskType";
import { TaskPriorityDto } from "@/lib/types/TaskPriority";
import { TaskStatusDto } from "@/lib/types/TaskStatus";
import { getEntityClients } from "@/lib/api";
import { Role } from "@/lib/api-client";

const taskSchema = z.object({
  title: z.string().min(1, "Requis"),
  description: z.string().optional(),
  dueDate: z.string().optional(),
});

type TaskFormValues = z.infer<typeof taskSchema>;

interface TaskFormProps {
  initial: TaskDto | null;
  saving: boolean;
  role: Role;
  defaultPropertyId?: number | null;
  onSubmit: (dto: TaskDto) => void;
  onCancel: () => void;
}

export function TaskForm({
  initial,
  saving,
  role,
  defaultPropertyId,
  onSubmit,
  onCancel,
}: TaskFormProps) {
  const base = initial ?? {
    ...newTaskDto(),
    property: defaultPropertyId != null ? ({ id: defaultPropertyId } as PropertyDto) : null,
  };

  const [properties, setProperties] = useState<PropertyDto[]>([]);
  const [collaborators, setCollaborators] = useState<CollaboratorDto[]>([]);
  const [types, setTypes] = useState<TaskTypeDto[]>([]);
  const [priorities, setPriorities] = useState<TaskPriorityDto[]>([]);
  const [statuses, setStatuses] = useState<TaskStatusDto[]>([]);

  const [propertyId, setPropertyId] = useState<number | null>(base.property?.id ?? null);
  const [assignedToId, setAssignedToId] = useState<number | null>(base.assignedTo?.id ?? null);
  const [typeId, setTypeId] = useState<number | null>(base.taskType?.id ?? null);
  const [priorityId, setPriorityId] = useState<number | null>(base.taskPriority?.id ?? null);
  const [statusId, setStatusId] = useState<number | null>(base.taskStatus?.id ?? null);

  useEffect(() => {
    const clients = getEntityClients(role);
    clients.property.findAll().then((d) => setProperties(d ?? [])).catch(() => setProperties([]));
    clients.collaborator.findAll().then((d) => setCollaborators(d ?? [])).catch(() => setCollaborators([]));
    clients.taskType.findAll().then((d) => setTypes(d ?? [])).catch(() => setTypes([]));
    clients.taskPriority.findAll().then((d) => setPriorities(d ?? [])).catch(() => setPriorities([]));
    clients.taskStatus.findAll().then((d) => setStatuses(d ?? [])).catch(() => setStatuses([]));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [role]);

  const form = useForm<TaskFormValues>({
    resolver: zodResolver(taskSchema),
    defaultValues: {
      title: base.title,
      description: base.description ?? "",
      dueDate: base.dueDate ?? "",
    },
  });

  function handleSubmit(values: TaskFormValues) {
    onSubmit({
      ...base,
      ...values,
      description: values.description ?? "",
      dueDate: values.dueDate || null,
      property: properties.find((p) => p.id === propertyId) ?? null,
      assignedTo: collaborators.find((c) => c.id === assignedToId) ?? null,
      taskType: types.find((t) => t.id === typeId) ?? null,
      taskPriority: priorities.find((p) => p.id === priorityId) ?? null,
      taskStatus: statuses.find((s) => s.id === statusId) ?? null,
    });
  }

  return (
    <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4 max-h-[65vh] overflow-y-auto pr-1">
      <div className="space-y-2">
        <Label htmlFor="title">Titre (ex: Ménage avant arrivée client)</Label>
        <Input id="title" {...form.register("title")} />
        {form.formState.errors.title && (
          <p className="text-sm text-destructive">{form.formState.errors.title.message}</p>
        )}
      </div>

      <div className="space-y-2">
        <Label htmlFor="description">Description</Label>
        <Input id="description" {...form.register("description")} />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="dueDate">Date d&apos;échéance</Label>
          <Input id="dueDate" type="date" {...form.register("dueDate")} />
        </div>
        <div className="space-y-2">
          <Label>Propriété</Label>
          <Select
            value={propertyId != null ? String(propertyId) : undefined}
            onValueChange={(v) => setPropertyId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {properties.map((p) => (
                <SelectItem key={p.id} value={String(p.id)}>
                  {p.name || `#${p.id}`}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      <div className="space-y-2">
        <Label>Assignée à</Label>
        <Select
          value={assignedToId != null ? String(assignedToId) : undefined}
          onValueChange={(v) => setAssignedToId(Number(v))}
        >
          <SelectTrigger className="w-full">
            <SelectValue placeholder="— Choisir un collaborateur —" />
          </SelectTrigger>
          <SelectContent>
            {collaborators.map((c) => (
              <SelectItem key={c.id} value={String(c.id)}>
                {c.name || `#${c.id}`}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
      </div>

      <div className="grid grid-cols-3 gap-4">
        <div className="space-y-2">
          <Label>Type</Label>
          <Select
            value={typeId != null ? String(typeId) : undefined}
            onValueChange={(v) => setTypeId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {types.map((t) => (
                <SelectItem key={t.id} value={String(t.id)}>
                  {t.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-2">
          <Label>Priorité</Label>
          <Select
            value={priorityId != null ? String(priorityId) : undefined}
            onValueChange={(v) => setPriorityId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {priorities.map((p) => (
                <SelectItem key={p.id} value={String(p.id)}>
                  {p.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-2">
          <Label>Statut</Label>
          <Select
            value={statusId != null ? String(statusId) : undefined}
            onValueChange={(v) => setStatusId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {statuses.map((s) => (
                <SelectItem key={s.id} value={String(s.id)}>
                  {s.label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      <DialogFooter>
        <Button type="button" variant="outline" onClick={onCancel}>
          Annuler
        </Button>
        <Button type="submit" disabled={saving}>
          {saving ? "Enregistrement..." : "Enregistrer"}
        </Button>
      </DialogFooter>
    </form>
  );
}