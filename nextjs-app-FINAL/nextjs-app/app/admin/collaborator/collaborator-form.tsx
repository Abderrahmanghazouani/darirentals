"use client";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import { DialogFooter } from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { CollaboratorDto, newCollaboratorDto } from "@/lib/types/Collaborator";
import { EnterpriseDto } from "@/lib/types/Enterprise";
import { CollaboratorRoleDto } from "@/lib/types/CollaboratorRole";
import { getEntityClients } from "@/lib/api";
import { Role } from "@/lib/api-client";
import { newEnterpriseMembershipDto } from "@/lib/types/EnterpriseMembership";

const collaboratorSchema = z.object({
  name: z.string().min(1, "Requis"),
  email: z.string().min(1, "Requis").email("Email invalide"),
  phone: z.string().optional(),
  username: z.string().min(1, "Requis"),
  password: z.string().optional(),
  isActive: z.boolean(),
});

type CollaboratorFormValues = z.infer<typeof collaboratorSchema>;

interface CollaboratorFormProps {
  initial: CollaboratorDto | null;
  saving: boolean;
  role: Role;
  onSubmit: (dto: CollaboratorDto, membership: { enterpriseId: number; roleId: number } | null) => void;
  onCancel: () => void;
}

export function CollaboratorForm({ initial, saving, role, onSubmit, onCancel }: CollaboratorFormProps) {
  const base = initial ?? newCollaboratorDto();
  const isEditing = initial != null;

  const [enterprises, setEnterprises] = useState<EnterpriseDto[]>([]);
  const [roles, setRoles] = useState<CollaboratorRoleDto[]>([]);
  const [enterpriseId, setEnterpriseId] = useState<number | null>(null);
  const [roleId, setRoleId] = useState<number | null>(null);

  useEffect(() => {
    const clients = getEntityClients(role);
    clients.enterprise.findAll().then((d) => setEnterprises(d ?? [])).catch(() => setEnterprises([]));
    clients.collaboratorRole.findAll().then((d) => setRoles(d ?? [])).catch(() => setRoles([]));
  }, [role]);

  const form = useForm<CollaboratorFormValues>({
    resolver: zodResolver(collaboratorSchema),
    defaultValues: {
      name: base.name,
      email: base.email,
      phone: base.phone ?? "",
      username: base.username,
      password: "",
      isActive: base.isActive ?? true,
    },
  });

  function handleSubmit(values: CollaboratorFormValues) {
  const dto: CollaboratorDto = {
      ...base,
      name: values.name,
      email: values.email,
      phone: values.phone ?? "",
      username: values.username,
      password: values.password ? values.password : base.password,
      isActive: values.isActive,
      enabled: true,
      accountNonExpired: true,
      accountNonLocked: true,
      credentialsNonExpired: true,
      passwordChanged: true,
    };

    const membership =
      !isEditing && enterpriseId != null && roleId != null
        ? { enterpriseId, roleId }
        : null;

    onSubmit(dto, membership);
  }

  return (
    <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4 max-h-[65vh] overflow-y-auto pr-1">
      <div className="space-y-2">
        <Label htmlFor="name">Nom complet</Label>
        <Input id="name" {...form.register("name")} />
        {form.formState.errors.name && (
          <p className="text-sm text-destructive">{form.formState.errors.name.message}</p>
        )}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="email">Email</Label>
          <Input id="email" type="email" {...form.register("email")} />
          {form.formState.errors.email && (
            <p className="text-sm text-destructive">{form.formState.errors.email.message}</p>
          )}
        </div>
        <div className="space-y-2">
          <Label htmlFor="phone">Téléphone</Label>
          <Input id="phone" {...form.register("phone")} />
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="username">Nom d&apos;utilisateur</Label>
          <Input id="username" {...form.register("username")} />
          {form.formState.errors.username && (
            <p className="text-sm text-destructive">{form.formState.errors.username.message}</p>
          )}
        </div>
        <div className="space-y-2">
          <Label htmlFor="password">
            Mot de passe {isEditing && <span className="text-muted-foreground">(laisser vide pour ne pas changer)</span>}
          </Label>
          <Input id="password" type="password" {...form.register("password")} />
        </div>
      </div>

      <div className="flex items-center gap-2">
        <Checkbox
          id="isActive"
          checked={form.watch("isActive")}
          onCheckedChange={(checked) => form.setValue("isActive", checked === true)}
        />
        <Label htmlFor="isActive">Compte actif</Label>
      </div>

      {!isEditing && (
        <div className="space-y-4 border-t pt-4">
          <p className="text-sm font-medium">Rattachement à une société</p>
          <div className="grid grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label>Société</Label>
              <Select
                value={enterpriseId != null ? String(enterpriseId) : undefined}
                onValueChange={(v) => setEnterpriseId(Number(v))}
              >
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="— Choisir —" />
                </SelectTrigger>
                <SelectContent>
                  {enterprises.map((e) => (
                    <SelectItem key={e.id} value={String(e.id)}>
                      {e.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label>Rôle</Label>
              <Select
                value={roleId != null ? String(roleId) : undefined}
                onValueChange={(v) => setRoleId(Number(v))}
              >
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="— Choisir —" />
                </SelectTrigger>
                <SelectContent>
                  {roles.map((r) => (
                    <SelectItem key={r.id} value={String(r.id)}>
                      {r.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
          </div>
        </div>
      )}

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