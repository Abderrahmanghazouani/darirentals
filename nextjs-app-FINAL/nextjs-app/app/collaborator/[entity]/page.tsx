"use client";

import { use, useMemo } from "react";
import { notFound } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Plus } from "lucide-react";
import { useSelectedEnterpriseId } from "@/lib/use-selected-enterprise";
import { filterByEnterprise } from "@/lib/filter-by-enterprise";

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

import { EntityTable } from "@/components/crud/entity-table";
import { EntityFormDialog } from "@/components/crud/entity-form-dialog";
import { AutoForm } from "@/components/crud/auto-form";
import { buildAutoColumns } from "@/components/crud/auto-columns";
import { useEntityCrud } from "@/lib/use-entity-crud";
import { getGenericClient, GenericDto } from "@/lib/generic-client";
import { entityRegistry } from "@/lib/entity-registry";
import { useRequireRole } from "@/lib/use-require-role";

export default function CollaboratorEntityPage({
  params,
}: {
  params: Promise<{ entity: string }>;
}) {
  const ready = useRequireRole("collaborator");
  const enterpriseId = useSelectedEnterpriseId();
  const { entity } = use(params);
  const descriptor = entityRegistry[entity];
  if (!descriptor) notFound();

  const client = useMemo(() => getGenericClient(entity, "collaborator"), [entity]);
  const crud = useEntityCrud<GenericDto>(client);
  const columns = useMemo(() => buildAutoColumns(descriptor), [descriptor]);
  const scopedItems = useMemo(
    () => filterByEnterprise(crud.items, enterpriseId),
    [crud.items, enterpriseId]
  );

  if (!ready) return null;

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>{descriptor.label}</CardTitle>
          <Button onClick={crud.openCreate}>
            <Plus /> Nouveau
          </Button>
        </CardHeader>
        <CardContent>
          {crud.error && <p className="text-destructive text-sm mb-4">{crud.error}</p>}

          <EntityTable<GenericDto>
            items={scopedItems}
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
        title={crud.editingItem ? `Modifier : ${descriptor.label}` : `Nouveau : ${descriptor.label}`}
      >
        <AutoForm
          descriptor={descriptor}
          role="collaborator"
          initial={crud.editingItem}
          saving={crud.saving}
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
            <AlertDialogTitle>Supprimer cet élément ?</AlertDialogTitle>
            <AlertDialogDescription>
              Cette action est irréversible.
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
