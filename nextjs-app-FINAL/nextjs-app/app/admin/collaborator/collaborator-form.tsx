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
import { PropertyDto } from "@/lib/types/Property";
import { getEntityClients } from "@/lib/api";
import { Role } from "@/lib/api-client";

const collaboratorSchema = z.object({
  name: z.string().min(1, "Requis"),
  email: z.string().min(1, "Requis").email("Email invalide"),
  phone: z.string().optional(),
  username: z.string().min(1, "Requis"),
  password: z.string().optional(),
  isActive: z.boolean(),
});

type CollaboratorFormValues = z.infer<typeof collaboratorSchema>;

const GESTIONNAIRE_CODE = "Gestionnaire";

export interface MembershipChange {
  /** null si aucune EnterpriseMembership n'existait encore pour ce collaborateur. */
  existingMembershipId: number | null;
  enterpriseId: number;
  roleId: number;
  /** Propriétés autorisées si le rôle choisi est Gestionnaire, sinon null (SubAdmin :
   * pas de restriction, la liste n'a pas de sens). */
  selectedPropertyIds: number[] | null;
  /** IDs des lignes CollaboratorPropertyAccess déjà en base pour ce collaborateur, à
   * réconcilier (créer les nouvelles, supprimer celles retirées). */
  existingAccessRows: { id: number; propertyId: number | null }[];
}

interface CollaboratorFormProps {
  initial: CollaboratorDto | null;
  saving: boolean;
  role: Role;
  onSubmit: (dto: CollaboratorDto, membership: MembershipChange | null) => void;
  onCancel: () => void;
}

export function CollaboratorForm({ initial, saving, role, onSubmit, onCancel }: CollaboratorFormProps) {
  const base = initial ?? newCollaboratorDto();
  const isEditing = initial != null;

  const [enterprises, setEnterprises] = useState<EnterpriseDto[]>([]);
  const [roles, setRoles] = useState<CollaboratorRoleDto[]>([]);
  const [properties, setProperties] = useState<PropertyDto[]>([]);
  const [enterpriseId, setEnterpriseId] = useState<number | null>(null);
  const [roleId, setRoleId] = useState<number | null>(null);
  const [selectedPropertyIds, setSelectedPropertyIds] = useState<number[]>([]);

  const [existingMembershipId, setExistingMembershipId] = useState<number | null>(null);
  const [existingAccessRows, setExistingAccessRows] = useState<{ id: number; propertyId: number | null }[]>([]);
  const [loadingMembership, setLoadingMembership] = useState(isEditing);

  useEffect(() => {
    const clients = getEntityClients(role);
    clients.enterprise.findAll().then((d) => setEnterprises(d ?? [])).catch(() => setEnterprises([]));
    clients.collaboratorRole.findAll().then((d) => setRoles(d ?? [])).catch(() => setRoles([]));
    clients.property.findAll().then((d) => setProperties(d ?? [])).catch(() => setProperties([]));
  }, [role]);

  // En édition : recharge la membership existante (société + rôle) et les propriétés déjà
  // autorisées, puisque la liste allégée (findAll) qui alimente le tableau ne les inclut pas.
  useEffect(() => {
    if (!isEditing || initial?.id == null) {
      setLoadingMembership(false);
      return;
    }
    setLoadingMembership(true);
    const clients = getEntityClients(role);
    const collaboratorId = initial.id;

    Promise.all([
      clients.enterpriseMembership.findByCriteria({ collaborator: { id: collaboratorId } }),
      clients.collaboratorPropertyAccess.findByCriteria({ collaborator: { id: collaboratorId } }),
    ])
      .then(([memberships, accessRows]) => {
        const membership = (memberships ?? [])[0] ?? null;
        setExistingMembershipId(membership?.id ?? null);
        setEnterpriseId(membership?.enterprise?.id ?? null);
        setRoleId(membership?.collaboratorRole?.id ?? null);

        const rows = (accessRows ?? []).map((a) => ({ id: a.id as number, propertyId: a.property?.id ?? null }));
        setExistingAccessRows(rows);
        setSelectedPropertyIds(rows.map((r) => r.propertyId).filter((id): id is number => id != null));
      })
      .catch(() => {
        // Pas bloquant : le collaborateur reste modifiable, seule la section
        // rattachement/propriétés ne sera pas pré-remplie.
      })
      .finally(() => setLoadingMembership(false));
  }, [isEditing, initial?.id, role]);

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

  const selectedRole = roles.find((r) => r.id === roleId) ?? null;
  const isGestionnaire = selectedRole?.code === GESTIONNAIRE_CODE;
  const enterpriseIsEditable = !isEditing || existingMembershipId == null;
  const propertiesForEnterprise = properties.filter((p) => p.enterprise?.id === enterpriseId);

  function togglePropertyId(id: number, checked: boolean) {
    setSelectedPropertyIds((prev) => (checked ? [...prev, id] : prev.filter((x) => x !== id)));
  }

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

    const membership: MembershipChange | null =
      enterpriseId != null && roleId != null
        ? {
            existingMembershipId,
            enterpriseId,
            roleId,
            selectedPropertyIds: isGestionnaire ? selectedPropertyIds : null,
            existingAccessRows,
          }
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

      <div className="space-y-4 border-t pt-4">
        <p className="text-sm font-medium">Rattachement à une société</p>
        {loadingMembership ? (
          <p className="text-sm text-muted-foreground">Chargement...</p>
        ) : (
          <>
            <div className="grid grid-cols-2 gap-4">
              <div className="space-y-2">
                <Label>Société</Label>
                {enterpriseIsEditable ? (
                  <Select
                    value={enterpriseId != null ? String(enterpriseId) : undefined}
                    onValueChange={(v) => {
                      setEnterpriseId(Number(v));
                      setSelectedPropertyIds([]);
                    }}
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
                ) : (
                  <p className="text-sm py-2">
                    {enterprises.find((e) => e.id === enterpriseId)?.name ?? "—"}
                  </p>
                )}
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

            {isGestionnaire && (
              <div className="space-y-2">
                <Label>Propriétés autorisées</Label>
                <p className="text-xs text-muted-foreground">
                  Un Gestionnaire ne voit et ne gère que les propriétés cochées ci-dessous. Aucune
                  coche = aucun accès.
                </p>
                {enterpriseId == null ? (
                  <p className="text-sm text-muted-foreground">Choisis d&apos;abord une société.</p>
                ) : propertiesForEnterprise.length === 0 ? (
                  <p className="text-sm text-muted-foreground">
                    Aucune propriété dans cette société.
                  </p>
                ) : (
                  <div className="max-h-48 overflow-y-auto rounded-md border p-2 space-y-1">
                    {propertiesForEnterprise.map((p) => (
                      <div key={p.id} className="flex items-center gap-2 py-1">
                        <Checkbox
                          id={`property-${p.id}`}
                          checked={p.id != null && selectedPropertyIds.includes(p.id)}
                          onCheckedChange={(checked) => p.id != null && togglePropertyId(p.id, checked === true)}
                        />
                        <Label htmlFor={`property-${p.id}`} className="font-normal cursor-pointer">
                          {p.name}
                        </Label>
                      </div>
                    ))}
                  </div>
                )}
              </div>
            )}
          </>
        )}
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
