"use client";

import { useEffect, useMemo, useState } from "react";
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
import { getGenericClient } from "@/lib/generic-client";
import { Role } from "@/lib/api-client";
import { checkAvailability } from "@/lib/reservation-api";
import { ReservationDto, newReservationDto } from "@/lib/types/Reservation";
import { ClientDto } from "@/lib/types/Client";
import { PropertyDto } from "@/lib/types/Property";
import { ReservationPlatformDto } from "@/lib/types/ReservationPlatform";
import { ReservationStatusDto } from "@/lib/types/ReservationStatus";
import { useSelectedEnterpriseId } from "@/lib/use-selected-enterprise";
import { filterByEnterprise } from "@/lib/filter-by-enterprise";

interface ReservationFormProps {
  role: Role;
  initial: ReservationDto | null;
  saving: boolean;
  /** Propriété présélectionnée (ex: quand on crée depuis la vue calendrier). */
  defaultPropertyId?: number | null;
  /** Date de check-in présélectionnée (ex: clic sur un jour du calendrier). */
  defaultCheckInDate?: string | null;
  onSubmit: (dto: ReservationDto) => void;
  onCancel: () => void;
}

function nightsBetween(checkIn: string | null | undefined, checkOut: string | null | undefined): number {
  if (!checkIn || !checkOut) return 0;
  const start = new Date(checkIn);
  const end = new Date(checkOut);
  const diff = Math.round((end.getTime() - start.getTime()) / (1000 * 60 * 60 * 24));
  return diff > 0 ? diff : 0;
}

export function ReservationForm({
  role,
  initial,
  saving,
  defaultPropertyId,
  defaultCheckInDate,
  onSubmit,
  onCancel,
}: ReservationFormProps) {
  const [values, setValues] = useState<ReservationDto>(() => {
    const base = initial ? { ...initial } : newReservationDto();
    if (!initial && defaultPropertyId != null) {
      base.property = { id: defaultPropertyId } as PropertyDto;
    }
    if (!initial && defaultCheckInDate) {
      base.checkInDate = defaultCheckInDate;
    }
    if (!initial) {
      base.reference = `RES-${Date.now().toString().slice(-8)}`;
    }
    return base;
  });

  const enterpriseId = useSelectedEnterpriseId();
  const [clients, setClients] = useState<ClientDto[]>([]);
  const [properties, setProperties] = useState<PropertyDto[]>([]);

  // Pas d'effet pour un admin (pas de societe selectionnee, filterByEnterprise ne filtre rien) -
  // evite qu'un collaborateur multi-societe voie les propriétés d'une autre société dans ce menu.
  const scopedProperties = filterByEnterprise(properties, enterpriseId);
  const [platforms, setPlatforms] = useState<ReservationPlatformDto[]>([]);
  const [statuses, setStatuses] = useState<ReservationStatusDto[]>([]);
  const [errors, setErrors] = useState<Record<string, string>>({});
  const [checkingAvailability, setCheckingAvailability] = useState(false);

  useEffect(() => {
    let cancelled = false;
    async function load() {
      try {
        const [c, p, pl, st] = await Promise.all([
          getGenericClient("client", role).findAll(),
          getGenericClient("property", role).findAll(),
          getGenericClient("reservationPlatform", role).findAll(),
          getGenericClient("reservationStatus", role).findAll(),
        ]);
        if (!cancelled) {
          setClients((c as unknown as ClientDto[]) ?? []);
          setProperties((p as unknown as PropertyDto[]) ?? []);
          setPlatforms((pl as unknown as ReservationPlatformDto[]) ?? []);
          setStatuses((st as unknown as ReservationStatusDto[]) ?? []);
        }
      } catch {
        // Si une des listes échoue (ex: aucune donnée), on garde les tableaux vides déjà initialisés
        // plutôt que de casser le formulaire.
      }
    }
    load();
    return () => {
      cancelled = true;
    };
  }, [role]);

  const nights = useMemo(
    () => nightsBetween(values.checkInDate, values.checkOutDate),
    [values.checkInDate, values.checkOutDate]
  );

  function setField<K extends keyof ReservationDto>(name: K, value: ReservationDto[K]) {
    setValues((prev) => ({ ...prev, [name]: value }));
  }

  // Recalcule le montant total automatiquement tant que l'utilisateur ne l'a pas modifié à la main.
  function handlePricePerNightChange(value: number | null) {
    setValues((prev) => {
      const next = { ...prev, pricePerNight: value };
      const n = nightsBetween(prev.checkInDate, prev.checkOutDate);
      if (value != null && n > 0) {
        next.amount = Math.round(value * n * 100) / 100;
      }
      return next;
    });
  }

  function handleDateChange(field: "checkInDate" | "checkOutDate", value: string) {
    setValues((prev) => {
      const next = { ...prev, [field]: value || null };
      const n = nightsBetween(next.checkInDate, next.checkOutDate);
      if (prev.pricePerNight != null && n > 0) {
        next.amount = Math.round(prev.pricePerNight * n * 100) / 100;
      }
      return next;
    });
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const nextErrors: Record<string, string> = {};
    if (!values.property?.id) nextErrors.property = "Requis";
    if (!values.client?.id) nextErrors.client = "Requis";
    if (!values.checkInDate) nextErrors.checkInDate = "Requis";
    if (!values.checkOutDate) nextErrors.checkOutDate = "Requis";
    if (
      values.checkInDate &&
      values.checkOutDate &&
      !(values.checkInDate < values.checkOutDate)
    ) {
      nextErrors.checkOutDate = "Doit être après le check-in";
    }
    setErrors(nextErrors);
    if (Object.keys(nextErrors).length > 0) return;

    setCheckingAvailability(true);
    try {
      const available = await checkAvailability(
        {
          propertyId: values.property!.id as number,
          checkInDate: values.checkInDate as string,
          checkOutDate: values.checkOutDate as string,
          excludeReservationId: values.id,
        },
        role
      );
      if (!available) {
        setErrors({ checkOutDate: "Cette propriété est déjà réservée sur cette période." });
        return;
      }
    } finally {
      setCheckingAvailability(false);
    }

    onSubmit(values);
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-h-[70vh] overflow-y-auto pr-1">
      <div className="space-y-2">
        <Label htmlFor="reference">Référence</Label>
        <Input
          id="reference"
          value={values.reference ?? ""}
          onChange={(e) => setField("reference", e.target.value)}
        />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label>Client</Label>
          <Select
            value={values.client?.id != null ? String(values.client.id) : undefined}
            onValueChange={(val) => {
              const found = clients.find((c) => String(c.id) === val) ?? null;
              setField("client", found);
            }}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {clients.map((c) => (
                <SelectItem key={c.id} value={String(c.id)}>
                  {c.fullName || c.email || `#${c.id}`}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
          {errors.client && <p className="text-sm text-destructive-text">{errors.client}</p>}
        </div>

        <div className="space-y-2">
          <Label>Propriété</Label>
          <Select
            value={values.property?.id != null ? String(values.property.id) : undefined}
            onValueChange={(val) => {
              const found = scopedProperties.find((p) => String(p.id) === val) ?? null;
              setValues((prev) => ({
                ...prev,
                property: found,
                pricePerNight: prev.pricePerNight ?? found?.pricePerNight ?? null,
              }));
            }}
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
          {errors.property && <p className="text-sm text-destructive-text">{errors.property}</p>}
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="checkInDate">Check-in</Label>
          <Input
            id="checkInDate"
            type="date"
            value={values.checkInDate ?? ""}
            onChange={(e) => handleDateChange("checkInDate", e.target.value)}
          />
          {errors.checkInDate && <p className="text-sm text-destructive-text">{errors.checkInDate}</p>}
        </div>
        <div className="space-y-2">
          <Label htmlFor="checkOutDate">Check-out</Label>
          <Input
            id="checkOutDate"
            type="date"
            value={values.checkOutDate ?? ""}
            onChange={(e) => handleDateChange("checkOutDate", e.target.value)}
          />
          {errors.checkOutDate && <p className="text-sm text-destructive-text">{errors.checkOutDate}</p>}
        </div>
      </div>

      {nights > 0 && (
        <p className="text-sm text-muted-foreground">{nights} nuit{nights > 1 ? "s" : ""}</p>
      )}

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="pricePerNight">Prix / nuit</Label>
          <Input
            id="pricePerNight"
            type="number"
            step="0.01"
            value={values.pricePerNight ?? ""}
            onChange={(e) =>
              handlePricePerNightChange(e.target.value === "" ? null : Number(e.target.value))
            }
          />
        </div>
        <div className="space-y-2">
          <Label htmlFor="amount">Montant total</Label>
          <Input
            id="amount"
            type="number"
            step="0.01"
            value={values.amount ?? ""}
            onChange={(e) =>
              setField("amount", e.target.value === "" ? null : Number(e.target.value))
            }
          />
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label>Plateforme</Label>
          <Select
            value={values.reservationPlatform?.id != null ? String(values.reservationPlatform.id) : undefined}
            onValueChange={(val) => {
              const found = platforms.find((p) => String(p.id) === val) ?? null;
              setField("reservationPlatform", found);
            }}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {platforms.map((p) => (
                <SelectItem key={p.id} value={String(p.id)}>
                  {p.label || `#${p.id}`}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>

        <div className="space-y-2">
          <Label>Statut</Label>
          <Select
            value={values.reservationStatus?.id != null ? String(values.reservationStatus.id) : undefined}
            onValueChange={(val) => {
              const found = statuses.find((s) => String(s.id) === val) ?? null;
              setField("reservationStatus", found);
            }}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {statuses.map((s) => (
                <SelectItem key={s.id} value={String(s.id)}>
                  {s.label || `#${s.id}`}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      <DialogFooter>
        <Button type="button" variant="outline" onClick={onCancel}>
          Annuler
        </Button>
        <Button type="submit" disabled={saving || checkingAvailability}>
          {checkingAvailability
            ? "Vérification..."
            : saving
              ? "Enregistrement..."
              : "Enregistrer"}
        </Button>
      </DialogFooter>
    </form>
  );
}