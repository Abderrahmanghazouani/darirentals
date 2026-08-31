"use client";

import { createElement, useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Plus, MapPin, TrendingUp } from "lucide-react";
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
import { PropertyDto } from "@/lib/types/Property";
import { CityDto } from "@/lib/types/City";
import { PropertyTypeDto } from "@/lib/types/PropertyType";
import { PropertyStatusDto } from "@/lib/types/PropertyStatus";
import { PropertyForm } from "./property-form";
import { StatusBadge } from "@/components/status-badge";

const ROLE = "admin" as const;

function PositionLink(props: { lat: number; lng: number }) {
  const url = "https://www.google.com/maps?q=" + props.lat + "," + props.lng;
  return createElement(
    "a",
    {
      href: url,
      target: "_blank",
      rel: "noopener noreferrer",
      className: "inline-flex items-center gap-1 text-primary hover:underline",
      onClick: (e: React.MouseEvent) => e.stopPropagation(),
    },
    createElement(MapPin, { className: "size-3.5" }),
    " Voir"
  );
}

export default function PropertyPage() {
  const ready = useRequireRole(ROLE);

  const client = useMemo(() => getEntityClients(ROLE).property, []);
  const crud = useEntityCrud<PropertyDto>(client);

  const [cities, setCities] = useState<CityDto[]>([]);
  const [types, setTypes] = useState<PropertyTypeDto[]>([]);
  const [statuses, setStatuses] = useState<PropertyStatusDto[]>([]);

  const [filterCity, setFilterCity] = useState<string>("all");
  const [filterType, setFilterType] = useState<string>("all");
  const [filterStatus, setFilterStatus] = useState<string>("all");

  useEffect(() => {
    const clients = getEntityClients(ROLE);
    clients.city.findAll().then((data) => setCities(data ?? [])).catch(() => setCities([]));
    clients.propertyType.findAll().then((data) => setTypes(data ?? [])).catch(() => setTypes([]));
    clients.propertyStatus.findAll().then((data) => setStatuses(data ?? [])).catch(() => setStatuses([]));
  }, []);

  const filteredItems = useMemo(() => {
    return crud.items.filter((p) => {
      if (filterCity !== "all" && String(p.city?.id) !== filterCity) return false;
      if (filterType !== "all" && String(p.propertyType?.id) !== filterType) return false;
      if (filterStatus !== "all" && String(p.propertyStatus?.id) !== filterStatus) return false;
      return true;
    });
  }, [crud.items, filterCity, filterType, filterStatus]);

  const columns: EntityColumn<PropertyDto>[] = [
    { header: "Nom", render: (p) => p.name },
    { header: "Ville", render: (p) => p.city?.name ?? "—" },
    {
      header: "Type",
      render: (p) =>
        p.propertyType ? <Badge variant="outline">{p.propertyType.label}</Badge> : "—",
    },
    {
      header: "Statut",
      render: (p) => <StatusBadge status={p.propertyStatus} />,
    },
    {
      header: "Capacité",
      render: (p) => (p.capacity != null ? p.capacity + " pers." : "—"),
    },
    {
      header: "Prix/nuit",
      render: (p) => (p.pricePerNight != null ? p.pricePerNight + " MAD" : "—"),
    },
    {
      header: "Position",
      render: (p) =>
        p.latitude != null && p.longitude != null ? (
          <PositionLink lat={p.latitude} lng={p.longitude} />
        ) : (
          "—"
        ),
    },
    {
      header: "Rentabilité",
      render: (p) =>
        p.id != null ? (
          <Link
            href={`/admin/property/${p.id}/rentabilite`}
            onClick={(e) => e.stopPropagation()}
            className="inline-flex items-center gap-1 rounded-md bg-secondary px-2 py-1 text-xs font-medium text-primary transition-colors hover:bg-accent"
          >
            <TrendingUp className="size-3.5" /> Voir
          </Link>
        ) : (
          "—"
        ),
    },
  ];

  if (!ready) return null;

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Propriétés</CardTitle>
          <Button onClick={crud.openCreate}>
            <Plus /> Nouvelle propriété
          </Button>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex flex-wrap gap-3">
            <Select value={filterCity} onValueChange={setFilterCity}>
              <SelectTrigger className="w-[180px]">
                <SelectValue placeholder="Ville" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Toutes les villes</SelectItem>
                {cities.map((c) => (
                  <SelectItem key={c.id} value={String(c.id)}>
                    {c.name}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Select value={filterType} onValueChange={setFilterType}>
              <SelectTrigger className="w-[180px]">
                <SelectValue placeholder="Type" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tous les types</SelectItem>
                {types.map((t) => (
                  <SelectItem key={t.id} value={String(t.id)}>
                    {t.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>

            <Select value={filterStatus} onValueChange={setFilterStatus}>
              <SelectTrigger className="w-[180px]">
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
          </div>

          {crud.error && <p className="text-destructive-text text-sm">{crud.error}</p>}

          <EntityTable<PropertyDto>
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
        title={crud.editingItem ? "Modifier la propriété" : "Nouvelle propriété"}
      >
        <PropertyForm
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
            <AlertDialogTitle>Supprimer cette propriété ?</AlertDialogTitle>
            <AlertDialogDescription>
              Cette action est irréversible. La propriété{" "}
              <strong>{crud.deleteTarget?.name}</strong> sera définitivement supprimée.
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