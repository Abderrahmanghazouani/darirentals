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
import { PaymentDto, newPaymentDto } from "@/lib/types/Payment";
import { ChargeDto } from "@/lib/types/Charge";
import { ServiceProviderDto } from "@/lib/types/ServiceProvider";
import { PaymentTypeDto } from "@/lib/types/PaymentType";
import { PaymentStatusDto } from "@/lib/types/PaymentStatus";
import { getEntityClients } from "@/lib/api";
import { Role } from "@/lib/api-client";

const paymentSchema = z.object({
  amount: z.coerce.number().nullable().optional(),
  notes: z.string().optional(),
});

type PaymentFormValues = z.infer<typeof paymentSchema>;

export interface PaymentFormResult {
  dto: PaymentDto;
  /** IDs des charges à rattacher à ce paiement (les autres charges liées seront détachées). */
  chargeIds: number[];
}

interface PaymentFormProps {
  initial: PaymentDto | null;
  saving: boolean;
  role: Role;
  onSubmit: (result: PaymentFormResult) => void;
  onCancel: () => void;
}

export function PaymentForm({ initial, saving, role, onSubmit, onCancel }: PaymentFormProps) {
  const base = initial ?? newPaymentDto();

  const [providers, setProviders] = useState<ServiceProviderDto[]>([]);
  const [types, setTypes] = useState<PaymentTypeDto[]>([]);
  const [statuses, setStatuses] = useState<PaymentStatusDto[]>([]);
  const [allCharges, setAllCharges] = useState<ChargeDto[]>([]);

  const [providerId, setProviderId] = useState<number | null>(base.serviceProvider?.id ?? null);
  const [typeId, setTypeId] = useState<number | null>(base.paymentType?.id ?? null);
  const [statusId, setStatusId] = useState<number | null>(base.paymentStatus?.id ?? null);
  const [selectedChargeIds, setSelectedChargeIds] = useState<number[]>(
    (base.charges ?? []).map((c) => c.id).filter((id): id is number => id != null)
  );

  useEffect(() => {
    const clients = getEntityClients(role);
    clients.serviceProvider.findAll().then((d) => setProviders(d ?? [])).catch(() => setProviders([]));
    clients.paymentType.findAll().then((d) => setTypes(d ?? [])).catch(() => setTypes([]));
    clients.paymentStatus.findAll().then((d) => setStatuses(d ?? [])).catch(() => setStatuses([]));
    clients.charge.findAll().then((d) => setAllCharges(d ?? [])).catch(() => setAllCharges([]));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [role]);

  const form = useForm<PaymentFormValues>({
    resolver: zodResolver(paymentSchema),
    defaultValues: {
      amount: base.amount ?? undefined,
      notes: base.notes ?? "",
    },
  });

  // Charges rattachables : celles pas encore payées, + celles déjà rattachées à CE paiement (en édition).
  const selectableCharges = allCharges.filter(
    (c) => c.payment == null || c.payment.id === base.id || selectedChargeIds.includes(c.id as number)
  );

  function toggleCharge(id: number, checked: boolean) {
    setSelectedChargeIds((prev) => (checked ? [...prev, id] : prev.filter((x) => x !== id)));
  }

  function handleSubmit(values: PaymentFormValues) {
    onSubmit({
      dto: {
        ...base,
        ...values,
        notes: values.notes ?? "",
        serviceProvider: providers.find((p) => p.id === providerId) ?? null,
        paymentType: types.find((t) => t.id === typeId) ?? null,
        paymentStatus: statuses.find((s) => s.id === statusId) ?? null,
      },
      chargeIds: selectedChargeIds,
    });
  }

  const selectedTotal = allCharges
    .filter((c) => selectedChargeIds.includes(c.id as number))
    .reduce((sum, c) => sum + (c.amount ?? 0), 0);

  return (
    <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4 max-h-[65vh] overflow-y-auto pr-1">
      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label>Prestataire</Label>
          <Select
            value={providerId != null ? String(providerId) : undefined}
            onValueChange={(v) => setProviderId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {providers.map((p) => (
                <SelectItem key={p.id} value={String(p.id)}>
                  {p.name}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-2">
          <Label>Type de paiement</Label>
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
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label>Statut</Label>
          <Select
            value={statusId != null ? String(statusId) : undefined}
            onValueChange={(v) => setStatusId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="Payé / Partiel / En attente..." />
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
        <div className="space-y-2">
          <Label htmlFor="amount">Montant versé</Label>
          <Input id="amount" type="number" step="0.01" {...form.register("amount")} />
        </div>
      </div>

      <div className="space-y-2">
        <Label htmlFor="notes">Notes</Label>
        <Input id="notes" {...form.register("notes")} />
      </div>

      <div className="space-y-2">
        <Label>Charges couvertes par ce paiement</Label>
        <div className="border rounded-md max-h-48 overflow-y-auto divide-y">
          {selectableCharges.length === 0 && (
            <p className="text-sm text-muted-foreground p-3">Aucune charge non payée disponible.</p>
          )}
          {selectableCharges.map((c) => (
            <label key={c.id} className="flex items-center gap-2 p-2 text-sm hover:bg-accent cursor-pointer">
              <Checkbox
                checked={selectedChargeIds.includes(c.id as number)}
                onCheckedChange={(checked) => toggleCharge(c.id as number, checked === true)}
              />
              <span className="flex-1">
                {c.label} — {c.property?.name ?? "?"}
              </span>
              <span className="text-muted-foreground">{c.amount != null ? `${c.amount} MAD` : "—"}</span>
            </label>
          ))}
        </div>
        {selectedChargeIds.length > 0 && (
          <p className="text-xs text-muted-foreground">
            Total des charges sélectionnées : {selectedTotal.toFixed(2)} MAD
          </p>
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