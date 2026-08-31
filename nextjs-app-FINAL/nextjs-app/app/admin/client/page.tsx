"use client";

import { useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Input } from "@/components/ui/input";
import { Plus, Search } from "lucide-react";

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
import { ClientDto } from "@/lib/types/Client";
import { ClientForm } from "./client-form";

const ROLE = "admin" as const;

export default function ClientPage() {
  const ready = useRequireRole(ROLE);

  const client = useMemo(() => getEntityClients(ROLE).client, []);
  const crud = useEntityCrud<ClientDto>(client);

  const [search, setSearch] = useState("");

  const filteredItems = useMemo(() => {
    const q = search.trim().toLowerCase();
    if (!q) return crud.items;
    return crud.items.filter((c) => {
      return (
        c.fullName?.toLowerCase().includes(q) ||
        c.email?.toLowerCase().includes(q) ||
        c.phone?.toLowerCase().includes(q) ||
        c.username?.toLowerCase().includes(q)
      );
    });
  }, [crud.items, search]);

  const columns: EntityColumn<ClientDto>[] = [
    { header: "Nom", render: (c) => c.fullName },
    { header: "Email", render: (c) => c.email },
    { header: "Téléphone", render: (c) => c.phone || "—" },
    { header: "Nationalité", render: (c) => c.nationality || "—" },
    { header: "Société", render: (c) => c.enterprise?.name ?? "—" },
    {
      header: "Réservations",
      render: (c) => (
        <Badge variant="outline">{c.reservations?.length ?? 0}</Badge>
      ),
    },
  ];

  if (!ready) return null;

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Clients</CardTitle>
          <Button onClick={crud.openCreate}>
            <Plus /> Nouveau client
          </Button>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="relative max-w-sm">
            <Search className="absolute left-2.5 top-2.5 size-4 text-muted-foreground" />
            <Input
              placeholder="Rechercher par nom, email, téléphone..."
              className="pl-8"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>

          {crud.error && <p className="text-destructive text-sm">{crud.error}</p>}

          <EntityTable<ClientDto>
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
        title={crud.editingItem ? "Modifier le client" : "Nouveau client"}
      >
        <ClientForm
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
            <AlertDialogTitle>Supprimer ce client ?</AlertDialogTitle>
            <AlertDialogDescription>
              Cette action est irréversible. Le client{" "}
              <strong>{crud.deleteTarget?.fullName}</strong> sera définitivement supprimé.
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