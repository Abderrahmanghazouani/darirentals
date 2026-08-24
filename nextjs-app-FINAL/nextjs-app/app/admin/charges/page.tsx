"use client";

import { useEffect, useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Plus, ScanLine } from "lucide-react";
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
import { ChargeDto } from "@/lib/types/Charge";
import { PropertyDto } from "@/lib/types/Property";
import { ChargeTypeDto } from "@/lib/types/ChargeType";
import { ChargeForm } from "@/components/charges/charge-form";
import { InvoiceScanDialog } from "@/components/charges/invoice-scan-dialog";

const ROLE = "admin" as const;

function paymentStatusVariant(style?: string | null): "default" | "secondary" | "destructive" | "outline" {
  const s = (style ?? "").toLowerCase();
  if (s.includes("success") || s.includes("primary")) return "default";
  if (s.includes("danger") || s.includes("destructive")) return "destructive";
  if (s.includes("warning") || s.includes("info")) return "secondary";
  return "outline";
}

export default function ChargesPage() {
  const ready = useRequireRole(ROLE);
  const client = useMemo(() => getEntityClients(ROLE).charge, []);
  const crud = useEntityCrud<ChargeDto>(client);

  const [properties, setProperties] = useState<PropertyDto[]>([]);
  const [chargeTypes, setChargeTypes] = useState<ChargeTypeDto[]>([]);

  useEffect(() => {
    const clients = getEntityClients(ROLE);
    clients.property.findAll().then((d) => setProperties(d ?? [])).catch(() => setProperties([]));
    clients.chargeType.findAll().then((d) => setChargeTypes(d ?? [])).catch(() => setChargeTypes([]));
  }, []);

  const [filterProperty, setFilterProperty] = useState<string>("all");
  const [filterType, setFilterType] = useState<string>("all");
  const [scanOpen, setScanOpen] = useState(false);

  const filteredItems = useMemo(() => {
    return crud.items.filter((c) => {
      if (filterProperty !== "all" && String(c.property?.id ?? "") !== filterProperty) return false;
      if (filterType !== "all" && String(c.chargeType?.id ?? "") !== filterType) return false;
      return true;
    });
  }, [crud.items, filterProperty, filterType]);

  // Vue "charges par propriété" : total dépensé par bien, sur l'ensemble des charges (pas seulement filtrées).
  const totalsByProperty = useMemo(() => {
    const map = new Map<number, { name: string; total: number; count: number }>();
    for (const c of crud.items) {
      if (!c.property?.id) continue;
      const entry = map.get(c.property.id) ?? { name: c.property.name || `#${c.property.id}`, total: 0, count: 0 };
      entry.total += c.amount ?? 0;
      entry.count += 1;
      map.set(c.property.id, entry);
    }
    return Array.from(map.values()).sort((a, b) => b.total - a.total);
  }, [crud.items]);

  const columns: EntityColumn<ChargeDto>[] = [
    { header: "Libellé", render: (c) => c.label },
    { header: "Propriété", render: (c) => c.property?.name ?? "—" },
    {
      header: "Type",
      render: (c) => (c.chargeType ? <Badge variant="outline">{c.chargeType.label}</Badge> : "—"),
    },
    { header: "Montant", render: (c) => (c.amount != null ? `${c.amount} MAD` : "—") },
    { header: "Prestataire", render: (c) => c.payment?.serviceProvider?.name ?? "—" },
    {
      header: "Paiement",
      render: (c) =>
        c.payment?.paymentStatus ? (
          <Badge variant={paymentStatusVariant(c.payment.paymentStatus.style)}>
            {c.payment.paymentStatus.label}
          </Badge>
        ) : (
          <Badge variant="secondary">Non payée</Badge>
        ),
    },
  ];

  if (!ready) return null;

  return (
    <div className="p-6 space-y-4">
      {totalsByProperty.length > 0 && (
        <Card>
          <CardHeader>
            <CardTitle>Charges par propriété</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-3">
              {totalsByProperty.map((entry) => (
                <div key={entry.name} className="rounded-md border p-3">
                  <p className="text-sm font-medium truncate">{entry.name}</p>
                  <p className="text-lg font-semibold">{entry.total.toFixed(2)} MAD</p>
                  <p className="text-xs text-muted-foreground">
                    {entry.count} charge{entry.count > 1 ? "s" : ""}
                  </p>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      )}

      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Charges</CardTitle>
          <div className="flex gap-2">
            <Button variant="outline" onClick={() => setScanOpen(true)}>
              <ScanLine /> Scanner une facture
            </Button>
            <Button onClick={crud.openCreate}>
              <Plus /> Nouvelle charge
            </Button>
          </div>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex flex-wrap gap-3">
            <Select value={filterProperty} onValueChange={setFilterProperty}>
              <SelectTrigger className="w-[200px]">
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

            <Select value={filterType} onValueChange={setFilterType}>
              <SelectTrigger className="w-[180px]">
                <SelectValue placeholder="Type de charge" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tous les types</SelectItem>
                {chargeTypes.map((t) => (
                  <SelectItem key={t.id} value={String(t.id)}>
                    {t.label}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>

          {crud.error && <p className="text-destructive text-sm">{crud.error}</p>}

          <EntityTable<ChargeDto>
            items={filteredItems}
            loading={crud.loading}
            onEdit={crud.openEdit}
            onDelete={crud.setDeleteTarget}
            columns={columns}
          />
        </CardContent>
      </Card>

      <InvoiceScanDialog
        open={scanOpen}
        onOpenChange={setScanOpen}
        role={ROLE}
        defaultPropertyId={filterProperty !== "all" ? Number(filterProperty) : null}
        onCreated={crud.refresh}
      />

      <EntityFormDialog
        open={crud.formOpen}
        onOpenChange={(open) => (open ? undefined : crud.closeForm())}
        title={crud.editingItem ? "Modifier la charge" : "Nouvelle charge"}
      >
        <ChargeForm
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
            <AlertDialogTitle>Supprimer cette charge ?</AlertDialogTitle>
            <AlertDialogDescription>
              Cette action est irréversible. La charge{" "}
              <strong>{crud.deleteTarget?.label}</strong> sera définitivement supprimée.
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