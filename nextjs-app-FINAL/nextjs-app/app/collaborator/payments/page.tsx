"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Plus, Pencil, Trash2 } from "lucide-react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
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

import { EntityFormDialog } from "@/components/crud/entity-form-dialog";
import { PaymentForm, PaymentFormResult } from "@/components/payments/payment-form";
import { getEntityClients } from "@/lib/api";
import { UnauthorizedError, Role } from "@/lib/api-client";
import { logout } from "@/lib/auth";
import { useRequireRole } from "@/lib/use-require-role";
import { PaymentDto } from "@/lib/types/Payment";
import { ChargeDto } from "@/lib/types/Charge";
import { ServiceProviderDto } from "@/lib/types/ServiceProvider";
import { PaymentStatusDto } from "@/lib/types/PaymentStatus";
import { useSelectedEnterpriseId } from "@/lib/use-selected-enterprise";

const ROLE: Role = "collaborator";

function statusVariant(style?: string | null): "default" | "secondary" | "destructive" | "outline" {
  const s = (style ?? "").toLowerCase();
  if (s.includes("success") || s.includes("primary")) return "default";
  if (s.includes("danger") || s.includes("destructive")) return "destructive";
  if (s.includes("warning") || s.includes("info")) return "secondary";
  return "outline";
}

export default function PaymentsPage() {
  const ready = useRequireRole(ROLE);
  const enterpriseId = useSelectedEnterpriseId();
  const router = useRouter();
  const clients = useMemo(() => getEntityClients(ROLE), []);

  const [payments, setPayments] = useState<PaymentDto[]>([]);
  const [charges, setCharges] = useState<ChargeDto[]>([]);
  const [providers, setProviders] = useState<ServiceProviderDto[]>([]);
  const [statuses, setStatuses] = useState<PaymentStatusDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [filterProvider, setFilterProvider] = useState<string>("all");
  const [filterStatus, setFilterStatus] = useState<string>("all");

  const [formOpen, setFormOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<PaymentDto | null>(null);
  const [saving, setSaving] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<PaymentDto | null>(null);

  const handleAuthError = useCallback(
    (e: unknown) => {
      if (e instanceof UnauthorizedError) {
        logout();
        router.push("/login");
        return true;
      }
      return false;
    },
    [router]
  );

  const refresh = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [p, c, prov, st] = await Promise.all([
        clients.payment.findAll(),
        clients.charge.findAll(),
        clients.serviceProvider.findAll(),
        clients.paymentStatus.findAll(),
      ]);
      setPayments(p ?? []);
      setCharges(c ?? []);
      setProviders(prov ?? []);
      setStatuses(st ?? []);
    } catch (e) {
      if (!handleAuthError(e)) {
        setError(e instanceof Error ? e.message : "Erreur de chargement");
      }
    } finally {
      setLoading(false);
    }
  }, [clients, handleAuthError]);

  useEffect(() => {
    refresh();
  }, [refresh]);

  const chargeCountByPayment = useMemo(() => {
    const map = new Map<number, number>();
    for (const c of charges) {
      if (c.payment?.id) map.set(c.payment.id, (map.get(c.payment.id) ?? 0) + 1);
    }
    return map;
  }, [charges]);

  // Payment n'a ni champ .enterprise ni .property direct (contrairement à Reservation/Charge/Task),
  // donc le helper partagé filterByEnterprise ne le filtrerait pas du tout. On filtre ici via le
  // prestataire, qui lui a un lien direct vers l'entreprise.
  const scopedPayments = useMemo(() => {
    if (enterpriseId == null) return payments;
    return payments.filter((p) => p.serviceProvider?.enterprise?.id === enterpriseId);
  }, [payments, enterpriseId]);

  const filteredPayments = useMemo(() => {
    return scopedPayments.filter((p) => {
      if (filterProvider !== "all" && String(p.serviceProvider?.id ?? "") !== filterProvider) return false;
      if (filterStatus !== "all" && String(p.paymentStatus?.id ?? "") !== filterStatus) return false;
      return true;
    });
  }, [scopedPayments, filterProvider, filterStatus]);

  function openCreate() {
    setEditingItem(null);
    setFormOpen(true);
  }
  function openEdit(item: PaymentDto) {
    setEditingItem(item);
    setFormOpen(true);
  }
  function closeForm() {
    setFormOpen(false);
    setEditingItem(null);
  }

  async function handleSubmit({ dto, chargeIds }: PaymentFormResult) {
    setSaving(true);
    try {
      const saved = dto.id != null ? await clients.payment.update(dto) : await clients.payment.create(dto);

      // Synchronise les charges : rattache les nouvelles sélections, détache celles décochées.
      const previouslyLinked = charges.filter((c) => c.payment?.id === saved.id).map((c) => c.id as number);
      const toLink = chargeIds.filter((id) => !previouslyLinked.includes(id));
      const toUnlink = previouslyLinked.filter((id) => !chargeIds.includes(id));

      await Promise.all([
        ...toLink.map((id) => {
          const c = charges.find((ch) => ch.id === id);
          return c
            ? clients.charge.update({ ...c, payment: { id: saved.id } as PaymentDto })
            : Promise.resolve();
        }),
        ...toUnlink.map((id) => {
          const c = charges.find((ch) => ch.id === id);
          return c ? clients.charge.update({ ...c, payment: null }) : Promise.resolve();
        }),
      ]);

      await refresh();
      closeForm();
    } catch (e) {
      if (!handleAuthError(e)) {
        setError(e instanceof Error ? e.message : "Erreur d'enregistrement");
      }
    } finally {
      setSaving(false);
    }
  }

  async function confirmDelete() {
    if (deleteTarget?.id == null) return;
    setSaving(true);
    try {
      // Détache d'abord les charges liées pour ne pas laisser de référence orpheline.
      const linked = charges.filter((c) => c.payment?.id === deleteTarget.id);
      await Promise.all(linked.map((c) => clients.charge.update({ ...c, payment: null })));
      await clients.payment.remove(deleteTarget.id);
      setDeleteTarget(null);
      await refresh();
    } catch (e) {
      if (!handleAuthError(e)) {
        setError(e instanceof Error ? e.message : "Erreur de suppression");
      }
    } finally {
      setSaving(false);
    }
  }

  if (!ready) return null;

  return (
    <div className="p-6 space-y-4">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Paiements aux prestataires</CardTitle>
          <Button onClick={openCreate}>
            <Plus /> Nouveau paiement
          </Button>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="flex flex-wrap gap-3">
            <Select value={filterProvider} onValueChange={setFilterProvider}>
              <SelectTrigger className="w-[200px]">
                <SelectValue placeholder="Prestataire" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">Tous les prestataires</SelectItem>
                {providers.map((p) => (
                  <SelectItem key={p.id} value={String(p.id)}>
                    {p.name}
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

          {error && <p className="text-destructive text-sm">{error}</p>}

          {loading ? (
            <p className="text-muted-foreground text-sm py-8 text-center">Chargement...</p>
          ) : filteredPayments.length === 0 ? (
            <p className="text-muted-foreground text-sm py-8 text-center">Aucun paiement.</p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Prestataire</TableHead>
                  <TableHead>Type</TableHead>
                  <TableHead>Statut</TableHead>
                  <TableHead>Montant</TableHead>
                  <TableHead>Charges couvertes</TableHead>
                  <TableHead>Notes</TableHead>
                  <TableHead className="text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredPayments.map((p) => (
                  <TableRow key={p.id}>
                    <TableCell>{p.serviceProvider?.name ?? "—"}</TableCell>
                    <TableCell>{p.paymentType?.label ?? "—"}</TableCell>
                    <TableCell>
                      {p.paymentStatus ? (
                        <Badge variant={statusVariant(p.paymentStatus.style)}>{p.paymentStatus.label}</Badge>
                      ) : (
                        "—"
                      )}
                    </TableCell>
                    <TableCell>{p.amount != null ? `${p.amount} MAD` : "—"}</TableCell>
                    <TableCell>{chargeCountByPayment.get(p.id as number) ?? 0}</TableCell>
                    <TableCell className="max-w-[200px] truncate">{p.notes || "—"}</TableCell>
                    <TableCell className="text-right space-x-1">
                      <Button variant="ghost" size="icon" onClick={() => openEdit(p)}>
                        <Pencil className="size-4" />
                      </Button>
                      <Button variant="ghost" size="icon" onClick={() => setDeleteTarget(p)}>
                        <Trash2 className="size-4 text-destructive" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      <EntityFormDialog
        open={formOpen}
        onOpenChange={(open) => (open ? undefined : closeForm())}
        title={editingItem ? "Modifier le paiement" : "Nouveau paiement"}
      >
        <PaymentForm
          initial={editingItem}
          saving={saving}
          role={ROLE}
          onSubmit={handleSubmit}
          onCancel={closeForm}
        />
      </EntityFormDialog>

      <AlertDialog
        open={deleteTarget != null}
        onOpenChange={(open) => (open ? undefined : setDeleteTarget(null))}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Supprimer ce paiement ?</AlertDialogTitle>
            <AlertDialogDescription>
              Les charges couvertes seront détachées (redeviennent &quot;non payées&quot;) mais pas supprimées.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Annuler</AlertDialogCancel>
            <AlertDialogAction onClick={confirmDelete}>Supprimer</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}