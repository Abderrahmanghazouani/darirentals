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
import { PropertyDto, newPropertyDto } from "@/lib/types/Property";
import { CityDto } from "@/lib/types/City";
import { PropertyTypeDto } from "@/lib/types/PropertyType";
import { PropertyStatusDto } from "@/lib/types/PropertyStatus";
import { EnterpriseDto } from "@/lib/types/Enterprise";
import { getEntityClients } from "@/lib/api";
import { Role } from "@/lib/api-client";
import { LocationMap } from "@/components/location-map-dynamic";

const propertySchema = z.object({
  name: z.string().min(1, "Requis"),
  streetNumber: z.string().optional(),
  streetName: z.string().optional(),
  postalCode: z.string().optional(),
  capacity: z.coerce.number().nullable().optional(),
  pricePerNight: z.coerce.number().nullable().optional(),
  latitude: z.coerce.number().nullable().optional(),
  longitude: z.coerce.number().nullable().optional(),
});

type PropertyFormValues = z.infer<typeof propertySchema>;

interface PropertyFormProps {
  initial: PropertyDto | null;
  saving: boolean;
  role: Role;
  onSubmit: (dto: PropertyDto) => void;
  onCancel: () => void;
}

export function PropertyForm({ initial, saving, role, onSubmit, onCancel }: PropertyFormProps) {
  const base = initial ?? newPropertyDto();

  const [cities, setCities] = useState<CityDto[]>([]);
  const [types, setTypes] = useState<PropertyTypeDto[]>([]);
  const [statuses, setStatuses] = useState<PropertyStatusDto[]>([]);
  const [enterprises, setEnterprises] = useState<EnterpriseDto[]>([]);

  const [cityId, setCityId] = useState<number | null>(base.city?.id ?? null);
  const [typeId, setTypeId] = useState<number | null>(base.propertyType?.id ?? null);
  const [statusId, setStatusId] = useState<number | null>(base.propertyStatus?.id ?? null);
  const [enterpriseId, setEnterpriseId] = useState<number | null>(base.enterprise?.id ?? null);

  useEffect(() => {
    const clients = getEntityClients(role);
    clients.city.findAll().then((data) => setCities(data ?? [])).catch(() => setCities([]));
    clients.propertyType.findAll().then((data) => setTypes(data ?? [])).catch(() => setTypes([]));
    clients.propertyStatus.findAll().then((data) => setStatuses(data ?? [])).catch(() => setStatuses([]));
    clients.enterprise.findAll().then((data) => setEnterprises(data ?? [])).catch(() => setEnterprises([]));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [role]);

  const form = useForm<PropertyFormValues>({
    resolver: zodResolver(propertySchema),
    defaultValues: {
      name: base.name,
      streetNumber: base.streetNumber ?? "",
      streetName: base.streetName ?? "",
      postalCode: base.postalCode ?? "",
      capacity: base.capacity ?? undefined,
      pricePerNight: base.pricePerNight ?? undefined,
      latitude: base.latitude ?? undefined,
      longitude: base.longitude ?? undefined,
    },
  });

  const watchedLat = form.watch("latitude");
  const watchedLng = form.watch("longitude");
  const mapLat = watchedLat != null && !Number.isNaN(watchedLat) ? Number(watchedLat) : null;
  const mapLng = watchedLng != null && !Number.isNaN(watchedLng) ? Number(watchedLng) : null;

  function handleMapPick(lat: number, lng: number) {
    form.setValue("latitude", Number(lat.toFixed(6)), { shouldValidate: true });
    form.setValue("longitude", Number(lng.toFixed(6)), { shouldValidate: true });
  }

  function handleSubmit(values: PropertyFormValues) {
    onSubmit({
      ...base,
      ...values,
      streetNumber: values.streetNumber ?? "",
      streetName: values.streetName ?? "",
      postalCode: values.postalCode ?? "",
      city: cities.find((c) => c.id === cityId) ?? null,
      propertyType: types.find((t) => t.id === typeId) ?? null,
      propertyStatus: statuses.find((s) => s.id === statusId) ?? null,
      enterprise: enterprises.find((e) => e.id === enterpriseId) ?? null,
    });
  }

  return (
    <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4 max-h-[65vh] overflow-y-auto pr-1">
      <div className="space-y-2">
        <Label htmlFor="name">Nom de la propriété</Label>
        <Input id="name" {...form.register("name")} />
        {form.formState.errors.name && (
          <p className="text-sm text-destructive-text">{form.formState.errors.name.message}</p>
        )}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label>Type</Label>
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
        <div className="space-y-2">
          <Label>Statut</Label>
          <Select
            value={statusId != null ? String(statusId) : undefined}
            onValueChange={(v) => setStatusId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
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
      </div>

      <div className="space-y-2">
        <Label>Société (entreprise)</Label>
        <Select
          value={enterpriseId != null ? String(enterpriseId) : undefined}
          onValueChange={(v) => setEnterpriseId(Number(v))}
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
      </div>

      <div className="grid grid-cols-3 gap-4">
        <div className="space-y-2 col-span-1">
          <Label htmlFor="streetNumber">N° rue</Label>
          <Input id="streetNumber" {...form.register("streetNumber")} />
        </div>
        <div className="space-y-2 col-span-2">
          <Label htmlFor="streetName">Rue</Label>
          <Input id="streetName" {...form.register("streetName")} />
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="postalCode">Code postal</Label>
          <Input id="postalCode" {...form.register("postalCode")} />
        </div>
        <div className="space-y-2">
          <Label>Ville</Label>
          <Select
            value={cityId != null ? String(cityId) : undefined}
            onValueChange={(v) => setCityId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {cities.map((c) => (
                <SelectItem key={c.id} value={String(c.id)}>
                  {c.name}
                  {c.country ? " (" + c.country.name + ")" : ""}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="capacity">Capacité (nb. voyageurs)</Label>
          <Input id="capacity" type="number" {...form.register("capacity")} />
        </div>
        <div className="space-y-2">
          <Label htmlFor="pricePerNight">Prix / nuit</Label>
          <Input id="pricePerNight" type="number" step="0.01" {...form.register("pricePerNight")} />
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="latitude">Latitude</Label>
          <Input id="latitude" type="number" step="0.000001" {...form.register("latitude")} />
        </div>
        <div className="space-y-2">
          <Label htmlFor="longitude">Longitude</Label>
          <Input id="longitude" type="number" step="0.000001" {...form.register("longitude")} />
        </div>
      </div>

      <div className="space-y-2">
        <Label>Position sur la carte (clique pour placer / déplacer le point)</Label>
        <LocationMap latitude={mapLat} longitude={mapLng} onPick={handleMapPick} height={240} />
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