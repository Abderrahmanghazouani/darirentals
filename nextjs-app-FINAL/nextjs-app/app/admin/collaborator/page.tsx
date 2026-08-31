"use client";

import { useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Plus } from "lucide-react";

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
import { CollaboratorDto } from "@/lib/types/Collaborator";
import { PropertyDto } from "@/lib/types/Property";
import { newEnterpriseMembershipDto } from "@/lib/types/EnterpriseMembership";
import { CollaboratorForm, MembershipChange } from "./collaborator-form";

const ROLE = "admin" as const;

export default function CollaboratorPage() {
  const ready = useRequireRole(ROLE);

  const client = useMemo(() => getEntityClients(ROLE).collaborator, []);
  const crud = useEntityCrud<CollaboratorDto>(client);

  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);

  async function handleFormSubmit(dto: CollaboratorDto, membership: MembershipChange | null) {
    setSaving(true);
    setFormError(null);
    try {
      const clients = getEntityClients(ROLE);
      let collaboratorId: number | null = dto.id;

      if (dto.id != null) {
        await clients.collaborator.update(dto);
      } else {
        const created = await clients.collaborator.create(dto);
        collaboratorId = created.id;
      }

      if (membership && collaboratorId != null) {
        const enterprises = await clients.enterprise.findAll();
        const roles = await clients.collaboratorRole.findAll();
        const enterprise = enterprises.find((e) => e.id === membership.enterpriseId) ?? null;
        const collaboratorRole = roles.find((r) => r.id === membership.roleId) ?? null;
        const collaboratorRef = { id: collaboratorId } as CollaboratorDto;

        if (membership.existingMembershipId == null) {
          const membershipDto = {
            ...newEnterpriseMembershipDto(),
            collaborator: collaboratorRef,
            enterprise,
            collaboratorRole,
          };
          await clients.enterpriseMembership.create(membershipDto);
        } else {
          await clients.enterpriseMembership.update({
            ...newEnterpriseMembershipDto(),
            id: membership.existingMembershipId,
            collaborator: collaboratorRef,
            enterprise,
            collaboratorRole,
          });
        }

        // Chantier 3 : réconcilie les propriétés autorisées (Gestionnaire uniquement).
        // selectedPropertyIds === null => rôle non-Gestionnaire (SubAdmin) : on retire toute
        // restriction existante, elle n'a plus de sens pour ce rôle.
        const currentRows = membership.existingAccessRows;
        const desiredIds = new Set(membership.selectedPropertyIds ?? []);
        const toDelete =
          membership.selectedPropertyIds === null
            ? currentRows
            : currentRows.filter((r) => r.propertyId == null || !desiredIds.has(r.propertyId));
        const existingPropertyIds = new Set(
          currentRows.map((r) => r.propertyId).filter((id): id is number => id != null)
        );
        const toCreate =
          membership.selectedPropertyIds === null
            ? []
            : membership.selectedPropertyIds.filter((id) => !existingPropertyIds.has(id));

        await Promise.all(toDelete.map((r) => clients.collaboratorPropertyAccess.remove(r.id)));
        await Promise.all(
          toCreate.map((propertyId) =>
            clients.collaboratorPropertyAccess.create({
              id: null,
              collaborator: collaboratorRef,
              property: { id: propertyId } as PropertyDto,
            })
          )
        );
      }

      await crud.refresh();
      crud.closeForm();
    } catch (e) {
      setFormError(e instanceof Error ? e.message : "Erreur lors de l'enregistrement");
    } finally {
      setSaving(false);
    }
  }

  const columns: EntityColumn<CollaboratorDto>[] = [
    { header: "Nom", render: (c) => c.name },
    { header: "Email", render: (c) => c.email },
    { header: "Téléphone", render: (c) => c.phone || "—" },
    { header: "Username", render: (c) => c.username },
    {
      header: "Actif",
      render: (c) =>
        c.isActive ? (
          <Badge variant="success">Oui</Badge>
        ) : (
          <Badge variant="secondary">Non</Badge>
        ),
    },
    {
      header: "Sociétés",
      render: (c) => (
        <Badge variant="outline">{c.enterpriseMemberships?.length ?? 0}</Badge>
      ),
    },
  ];

  if (!ready) return null;

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Collaborateurs</CardTitle>
          <Button onClick={crud.openCreate}>
            <Plus /> Nouveau collaborateur
          </Button>
        </CardHeader>
        <CardContent className="space-y-4">
          {(crud.error || formError) && (
            <p className="text-destructive-text text-sm">{crud.error || formError}</p>
          )}

          <EntityTable<CollaboratorDto>
            items={crud.items}
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
        title={crud.editingItem ? "Modifier le collaborateur" : "Nouveau collaborateur"}
      >
        <CollaboratorForm
          initial={crud.editingItem}
          saving={saving}
          role={ROLE}
          onSubmit={handleFormSubmit}
          onCancel={crud.closeForm}
        />
      </EntityFormDialog>

      <AlertDialog
        open={crud.deleteTarget != null}
        onOpenChange={(open) => (open ? undefined : crud.setDeleteTarget(null))}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Supprimer ce collaborateur ?</AlertDialogTitle>
            <AlertDialogDescription>
              Cette action est irréversible. Le collaborateur{" "}
              <strong>{crud.deleteTarget?.name}</strong> sera définitivement supprimé.
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