"use client";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { SheetFooter } from "@/components/ui/sheet";
import { FormStepper } from "@/components/crud/form-stepper";
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

// Wizard en 3 étapes (voir NOTES-formulaires-premium.md) : Property est le formulaire le plus
// long (infos + adresse/carte + tarification), découpé pour ne pas noyer l'utilisateur sous un
// mur de champs. `fields` liste les champs react-hook-form dont la validité est vérifiée avant
// d'autoriser "Suivant" (form.trigger) - seuls les champs réellement contraints par le schéma
// zod bloquent (ici uniquement "name" à l'étape 1) : on ne invente pas de nouvelles règles
// obligatoires sur les champs déjà optionnels aujourd'hui (ex: ville, type).
const STEPS: { label: string; fields: (keyof PropertyFormValues)[] }[] = [
  { label: "Informations générales", fields: ["name"] },
  { label: "Localisation", fields: [] },
  { label: "Détails", fields: ["capacity", "pricePerNight"] },
];

export function PropertyForm({ initial, saving, role, onSubmit, onCancel }: PropertyFormProps) {
  const base = initial ?? newPropertyDto();

  // Remonté à chaque ouverture via `key={crud.formSession}` côté page (voir use-entity-crud.ts
  // et NOTES-formulaires-premium.md) : l'étape repart donc toujours de zéro, y compris quand
  // deux ouvertures s'enchaînent sans laisser le Sheet se démonter.
  const [step, setStep] = useState(0);
  const isLastStep = step === STEPS.length - 1;

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

  async function goNext() {
    const fields = STEPS[step].fields;
    const valid = fields.length === 0 || (await form.trigger(fields));
    if (valid) setStep((s) => Math.min(s + 1, STEPS.length - 1));
  }

  function goBack() {
    setStep((s) => Math.max(s - 1, 0));
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

  // Soumission déclenchée uniquement depuis le bouton "Enregistrer" de la dernière étape - sur
  // les étapes précédentes, Entrée/le bouton "Suivant" ne doivent jamais soumettre le formulaire
  // (voir le type="button" + onClick explicite ci-dessous), d'où ce garde-fou dans onSubmit.
  function handleFormSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!isLastStep) return;
    form.handleSubmit(handleSubmit)(e);
  }

  const selectedType = types.find((t) => t.id === typeId);
  const selectedStatus = statuses.find((s) => s.id === statusId);
  const selectedEnterprise = enterprises.find((e) => e.id === enterpriseId);
  const selectedCity = cities.find((c) => c.id === cityId);
  const values = form.watch();

  return (
    <form onSubmit={handleFormSubmit} className="flex h-full min-h-0 flex-1 flex-col">
      <div className="shrink-0 border-b px-6 py-4">
        <FormStepper steps={STEPS} current={step} />
      </div>

      <div className="flex-1 overflow-y-auto px-6 py-5 space-y-4">
        {step === 0 && (
          <>
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
          </>
        )}

        {step === 1 && (
          <>
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
          </>
        )}

        {step === 2 && (
          <>
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

            <div className="space-y-2 rounded-md border bg-muted/40 p-4">
              <p className="text-sm font-medium">Récapitulatif</p>
              <dl className="grid grid-cols-2 gap-x-4 gap-y-1.5 text-sm">
                <dt className="text-muted-foreground">Nom</dt>
                <dd className="truncate">{values.name || "—"}</dd>
                <dt className="text-muted-foreground">Type</dt>
                <dd className="truncate">{selectedType?.label ?? "—"}</dd>
                <dt className="text-muted-foreground">Statut</dt>
                <dd className="truncate">{selectedStatus?.label ?? "—"}</dd>
                <dt className="text-muted-foreground">Société</dt>
                <dd className="truncate">{selectedEnterprise?.name ?? "—"}</dd>
                <dt className="text-muted-foreground">Adresse</dt>
                <dd className="truncate">
                  {[values.streetNumber, values.streetName].filter(Boolean).join(" ") || "—"}
                </dd>
                <dt className="text-muted-foreground">Ville</dt>
                <dd className="truncate">{selectedCity?.name ?? "—"}</dd>
                <dt className="text-muted-foreground">Capacité</dt>
                <dd className="truncate">{values.capacity ?? "—"}</dd>
                <dt className="text-muted-foreground">Prix / nuit</dt>
                <dd className="truncate">{values.pricePerNight ?? "—"}</dd>
              </dl>
            </div>
          </>
        )}
      </div>

      <SheetFooter>
        {step === 0 ? (
          <Button type="button" variant="outline" onClick={onCancel}>
            Annuler
          </Button>
        ) : (
          <Button type="button" variant="outline" onClick={goBack}>
            Précédent
          </Button>
        )}
        {isLastStep ? (
          <Button type="submit" disabled={saving}>
            {saving ? "Enregistrement..." : "Enregistrer"}
          </Button>
        ) : (
          <Button type="button" onClick={goNext}>
            Suivant
          </Button>
        )}
      </SheetFooter>
    </form>
  );
}
