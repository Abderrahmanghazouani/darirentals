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
import { ChargeDto, newChargeDto } from "@/lib/types/Charge";
import { PropertyDto } from "@/lib/types/Property";
import { ChargeTypeDto } from "@/lib/types/ChargeType";
import { PaymentDto } from "@/lib/types/Payment";
import { getEntityClients } from "@/lib/api";
import { Role } from "@/lib/api-client";
import { useSelectedEnterpriseId } from "@/lib/use-selected-enterprise";
import { filterByEnterprise } from "@/lib/filter-by-enterprise";

const chargeSchema = z.object({
  label: z.string().min(1, "Requis"),
  amount: z.coerce.number().nullable().optional(),
  chargeDate: z.string().optional(),
});

type ChargeFormValues = z.infer<typeof chargeSchema>;

interface ChargeFormProps {
  initial: ChargeDto | null;
  saving: boolean;
  role: Role;
  defaultPropertyId?: number | null;
  onSubmit: (dto: ChargeDto) => void;
  onCancel: () => void;
}

export function ChargeForm({
  initial,
  saving,
  role,
  defaultPropertyId,
  onSubmit,
  onCancel,
}: ChargeFormProps) {
  const base = initial ?? {
    ...newChargeDto(),
    property: defaultPropertyId != null ? ({ id: defaultPropertyId } as PropertyDto) : null,
  };

  const enterpriseId = useSelectedEnterpriseId();
  const [properties, setProperties] = useState<PropertyDto[]>([]);
  const [chargeTypes, setChargeTypes] = useState<ChargeTypeDto[]>([]);
  const [payments, setPayments] = useState<PaymentDto[]>([]);

  // Pas d'effet pour un admin (pas de societe selectionnee, filterByEnterprise ne filtre rien) -
  // evite qu'un collaborateur multi-societe voie les propriétés d'une autre société dans ce menu.
  const scopedProperties = filterByEnterprise(properties, enterpriseId);

  const [propertyId, setPropertyId] = useState<number | null>(base.property?.id ?? null);
  const [chargeTypeId, setChargeTypeId] = useState<number | null>(base.chargeType?.id ?? null);
  const [paymentId, setPaymentId] = useState<number | null>(base.payment?.id ?? null);

  useEffect(() => {
    const clients = getEntityClients(role);
    clients.property.findAll().then((d) => setProperties(d ?? [])).catch(() => setProperties([]));
    clients.chargeType.findAll().then((d) => setChargeTypes(d ?? [])).catch(() => setChargeTypes([]));
    clients.payment.findAll().then((d) => setPayments(d ?? [])).catch(() => setPayments([]));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [role]);

  const form = useForm<ChargeFormValues>({
    resolver: zodResolver(chargeSchema),
    defaultValues: {
      label: base.label,
      amount: base.amount ?? undefined,
      chargeDate: base.chargeDate ?? new Date().toISOString().slice(0, 10),
    },
  });

  function handleSubmit(values: ChargeFormValues) {
    onSubmit({
      ...base,
      ...values,
      chargeDate: values.chargeDate || null,
      property: properties.find((p) => p.id === propertyId) ?? null,
      chargeType: chargeTypes.find((t) => t.id === chargeTypeId) ?? null,
      payment: payments.find((p) => p.id === paymentId) ?? null,
    });
  }

  function paymentLabel(p: PaymentDto): string {
    const provider = p.serviceProvider?.name ?? "Sans prestataire";
    const status = p.paymentStatus?.label ?? "";
    const amount = p.amount != null ? `${p.amount} MAD` : "";
    return [provider, status, amount].filter(Boolean).join(" · ");
  }

  return (
    <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4 max-h-[65vh] overflow-y-auto pr-1">
      <div className="space-y-2">
        <Label htmlFor="label">Libellé (ex: Électricité août)</Label>
        <Input id="label" {...form.register("label")} />
        {form.formState.errors.label && (
          <p className="text-sm text-destructive">{form.formState.errors.label.message}</p>
        )}
      </div>

      <div className="grid grid-cols-2 gap-4">
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
              {scopedProperties.map((p) => (
                <SelectItem key={p.id} value={String(p.id)}>
                  {p.name || `#${p.id}`}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
        <div className="space-y-2">
          <Label>Type de charge</Label>
          <Select
            value={chargeTypeId != null ? String(chargeTypeId) : undefined}
            onValueChange={(v) => setChargeTypeId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {chargeTypes.map((t) => (
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
          <Label htmlFor="amount">Montant</Label>
          <Input id="amount" type="number" step="0.01" {...form.register("amount")} />
        </div>
        <div className="space-y-2">
          <Label htmlFor="chargeDate">Date de la charge</Label>
          <Input id="chargeDate" type="date" {...form.register("chargeDate")} />
        </div>
      </div>

      <div className="space-y-2">
        <Label>Paiement associé (optionnel)</Label>
        <Select
          value={paymentId != null ? String(paymentId) : "none"}
          onValueChange={(v) => setPaymentId(v === "none" ? null : Number(v))}
        >
          <SelectTrigger className="w-full">
            <SelectValue placeholder="— Non payée —" />
          </SelectTrigger>
          <SelectContent>
            <SelectItem value="none">— Non payée —</SelectItem>
            {payments.map((p) => (
              <SelectItem key={p.id} value={String(p.id)}>
                {paymentLabel(p)}
              </SelectItem>
            ))}
          </SelectContent>
        </Select>
        <p className="text-xs text-muted-foreground">
          Si le paiement au prestataire n&apos;existe pas encore, laisse vide et crée-le depuis l&apos;écran Paiements — tu pourras y rattacher cette charge.
        </p>
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