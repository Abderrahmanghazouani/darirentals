"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { format } from "date-fns";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
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
import { Pencil, Trash2, Plus, CalendarDays, List as ListIcon } from "lucide-react";

import { EntityFormDialog } from "@/components/crud/entity-form-dialog";
import { ReservationForm } from "@/components/reservations/reservation-form";
import { ReservationCalendar } from "@/components/reservations/reservation-calendar";
import { getGenericClient } from "@/lib/generic-client";
import { UnauthorizedError } from "@/lib/api-client";
import { logout } from "@/lib/auth";
import { useRequireRole } from "@/lib/use-require-role";
import { ReservationDto } from "@/lib/types/Reservation";
import { PropertyDto } from "@/lib/types/Property";
import { ReservationStatusDto } from "@/lib/types/ReservationStatus";
import { useSelectedEnterpriseId } from "@/lib/use-selected-enterprise";
import { filterByEnterprise } from "@/lib/filter-by-enterprise";

const ROLE = "collaborator" as const;

function nights(checkIn?: string | null, checkOut?: string | null) {
  if (!checkIn || !checkOut) return null;
  const diff = Math.round(
    (new Date(checkOut).getTime() - new Date(checkIn).getTime()) / (1000 * 60 * 60 * 24)
  );
  return diff > 0 ? diff : null;
}

export default function ReservationsPage() {
  const ready = useRequireRole("collaborator");
  const router = useRouter();
  const enterpriseId = useSelectedEnterpriseId();

  const reservationClient = useMemo(() => getGenericClient("reservation", ROLE), []);

  const [reservations, setReservations] = useState<ReservationDto[]>([]);
  const [properties, setProperties] = useState<PropertyDto[]>([]);
  const [statuses, setStatuses] = useState<ReservationStatusDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [view, setView] = useState<"calendar" | "list">("calendar");
  const [calendarPropertyId, setCalendarPropertyId] = useState<number | null>(null);
  const [calendarMonth, setCalendarMonth] = useState(new Date());

  const [filterPropertyId, setFilterPropertyId] = useState<string>("all");
  const [filterStatusId, setFilterStatusId] = useState<string>("all");
  const [filterFrom, setFilterFrom] = useState<string>("");
  const [filterTo, setFilterTo] = useState<string>("");

  const [formOpen, setFormOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<ReservationDto | null>(null);
  const [prefillPropertyId, setPrefillPropertyId] = useState<number | null>(null);
  const [prefillDate, setPrefillDate] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);
  const [deleteTarget, setDeleteTarget] = useState<ReservationDto | null>(null);

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
      const [res, props, sts] = await Promise.all([
        reservationClient.findAll() as unknown as Promise<ReservationDto[]>,
        getGenericClient("property", ROLE).findAll() as unknown as Promise<PropertyDto[]>,
        getGenericClient("reservationStatus", ROLE).findAll() as unknown as Promise<ReservationStatusDto[]>,
      ]);
      setReservations(res ?? []);
      setProperties(props ?? []);
      setStatuses(sts ?? []);
      // Le defaut de propriete affichee doit respecter la societe actuellement selectionnee
      // (collaborateur multi-societe) - sinon le calendrier peut s'ouvrir sur une propriete
      // d'une autre societe que celle en cours.
      const scopedProps = filterByEnterprise(props ?? [], enterpriseId);
      setCalendarPropertyId((prev) => prev ?? (scopedProps[0]?.id ?? null));
    } catch (e) {
      if (!handleAuthError(e)) {
        setError(e instanceof Error ? e.message : "Erreur de chargement");
      }
    } finally {
      setLoading(false);
    }
  }, [reservationClient, handleAuthError, enterpriseId]);

  useEffect(() => {
    refresh();
  }, [refresh]);

  const scopedReservations = useMemo(
    () => filterByEnterprise(reservations, enterpriseId),
    [reservations, enterpriseId]
  );

  // Meme filtre que pour les reservations : evite qu'un collaborateur multi-societe voie les
  // propriétés d'une autre société que celle actuellement sélectionnée dans les menus ci-dessous.
  const scopedProperties = useMemo(
    () => filterByEnterprise(properties, enterpriseId),
    [properties, enterpriseId]
  );

  const filtered = useMemo(() => {
    return scopedReservations.filter((r) => {
      if (filterPropertyId !== "all" && String(r.property?.id ?? "") !== filterPropertyId) {
        return false;
      }
      if (filterStatusId !== "all" && String(r.reservationStatus?.id ?? "") !== filterStatusId) {
        return false;
      }
      if (filterFrom && (!r.checkInDate || r.checkInDate < filterFrom)) return false;
      if (filterTo && (!r.checkOutDate || r.checkOutDate > filterTo)) return false;
      return true;
    });
  }, [scopedReservations, filterPropertyId, filterStatusId, filterFrom, filterTo]);

  const calendarReservations = useMemo(
    () => scopedReservations.filter((r) => r.property?.id === calendarPropertyId),
    [scopedReservations, calendarPropertyId]
  );

  function openCreate(propertyId?: number | null, date?: Date | null) {
    setEditingItem(null);
    setPrefillPropertyId(propertyId ?? null);
    setPrefillDate(date ? format(date, "yyyy-MM-dd") : null);
    setFormOpen(true);
  }

  function openEdit(item: ReservationDto) {
    setEditingItem(item);
    setPrefillPropertyId(null);
    setPrefillDate(null);
    setFormOpen(true);
  }

  function closeForm() {
    setFormOpen(false);
    setEditingItem(null);
  }

  async function submit(dto: ReservationDto) {
    setSaving(true);
    try {
      if (dto.id != null) {
        await reservationClient.update(dto as never);
      } else {
        await reservationClient.create(dto as never);
      }
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
      await reservationClient.remove(deleteTarget.id);
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
          <CardTitle>Réservations</CardTitle>
          <div className="flex items-center gap-2">
            <div className="flex rounded-md border overflow-hidden">
              <Button
                type="button"
                variant={view === "calendar" ? "default" : "ghost"}
                className="rounded-none"
                onClick={() => setView("calendar")}
              >
                <CalendarDays className="size-4" /> Calendrier
              </Button>
              <Button
                type="button"
                variant={view === "list" ? "default" : "ghost"}
                className="rounded-none"
                onClick={() => setView("list")}
              >
                <ListIcon className="size-4" /> Liste
              </Button>
            </div>
            <Button onClick={() => openCreate(calendarPropertyId)}>
              <Plus /> Nouvelle réservation
            </Button>
          </div>
        </CardHeader>

        <CardContent className="space-y-4">
          {error && <p className="text-destructive text-sm">{error}</p>}

          {/* Filtres */}
          <div className="flex flex-wrap items-end gap-3 pb-2 border-b">
            <div className="space-y-1">
              <Label className="text-xs text-muted-foreground">Propriété</Label>
              <Select value={filterPropertyId} onValueChange={setFilterPropertyId}>
                <SelectTrigger className="w-[180px]">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Toutes</SelectItem>
                  {scopedProperties.map((p) => (
                    <SelectItem key={p.id} value={String(p.id)}>
                      {p.name || `#${p.id}`}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-1">
              <Label className="text-xs text-muted-foreground">Statut</Label>
              <Select value={filterStatusId} onValueChange={setFilterStatusId}>
                <SelectTrigger className="w-[160px]">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="all">Tous</SelectItem>
                  {statuses.map((s) => (
                    <SelectItem key={s.id} value={String(s.id)}>
                      {s.label || `#${s.id}`}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-1">
              <Label className="text-xs text-muted-foreground">Du</Label>
              <Input
                type="date"
                className="w-[150px]"
                value={filterFrom}
                onChange={(e) => setFilterFrom(e.target.value)}
              />
            </div>
            <div className="space-y-1">
              <Label className="text-xs text-muted-foreground">Au</Label>
              <Input
                type="date"
                className="w-[150px]"
                value={filterTo}
                onChange={(e) => setFilterTo(e.target.value)}
              />
            </div>
            {(filterPropertyId !== "all" || filterStatusId !== "all" || filterFrom || filterTo) && (
              <Button
                type="button"
                variant="ghost"
                size="sm"
                onClick={() => {
                  setFilterPropertyId("all");
                  setFilterStatusId("all");
                  setFilterFrom("");
                  setFilterTo("");
                }}
              >
                Réinitialiser
              </Button>
            )}
          </div>

          {view === "calendar" ? (
            <div className="space-y-3">
              <div className="max-w-xs space-y-1">
                <Label className="text-xs text-muted-foreground">Propriété affichée</Label>
                <Select
                  value={calendarPropertyId != null ? String(calendarPropertyId) : undefined}
                  onValueChange={(val) => setCalendarPropertyId(Number(val))}
                >
                  <SelectTrigger className="w-full">
                    <SelectValue placeholder="Choisir une propriété" />
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

              {scopedProperties.length === 0 ? (
                <p className="text-muted-foreground text-sm py-8 text-center">
                  Crée d&apos;abord au moins une propriété (CRUD générique) pour voir le calendrier.
                </p>
              ) : (
                <ReservationCalendar
                  month={calendarMonth}
                  onMonthChange={setCalendarMonth}
                  reservations={calendarReservations}
                  onReservationClick={openEdit}
                  onDayClick={(day: Date) => openCreate(calendarPropertyId, day)}
                  loading={loading}
                />
              )}
            </div>
          ) : loading ? (
            <p className="text-muted-foreground text-sm py-8 text-center">Chargement...</p>
          ) : filtered.length === 0 ? (
            <p className="text-muted-foreground text-sm py-8 text-center">
              Aucune réservation pour ces filtres.
            </p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Référence</TableHead>
                  <TableHead>Client</TableHead>
                  <TableHead>Propriété</TableHead>
                  <TableHead>Check-in</TableHead>
                  <TableHead>Check-out</TableHead>
                  <TableHead>Nuits</TableHead>
                  <TableHead>Montant</TableHead>
                  <TableHead>Statut</TableHead>
                  <TableHead className="text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filtered.map((r) => (
                  <TableRow key={r.id}>
                    <TableCell>{r.reference || "—"}</TableCell>
                    <TableCell>{r.client?.fullName ?? "—"}</TableCell>
                    <TableCell>{r.property?.name ?? "—"}</TableCell>
                    <TableCell>{r.checkInDate ?? "—"}</TableCell>
                    <TableCell>{r.checkOutDate ?? "—"}</TableCell>
                    <TableCell>{nights(r.checkInDate, r.checkOutDate) ?? "—"}</TableCell>
                    <TableCell>{r.amount != null ? r.amount : "—"}</TableCell>
                    <TableCell>{r.reservationStatus?.label ?? "—"}</TableCell>
                    <TableCell className="text-right space-x-1">
                      <Button variant="ghost" size="icon" onClick={() => openEdit(r)}>
                        <Pencil className="size-4" />
                      </Button>
                      <Button variant="ghost" size="icon" onClick={() => setDeleteTarget(r)}>
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
        title={editingItem ? "Modifier la réservation" : "Nouvelle réservation"}
      >
        <ReservationForm
          role={ROLE}
          initial={editingItem}
          defaultPropertyId={prefillPropertyId}
          defaultCheckInDate={prefillDate}
          saving={saving}
          onSubmit={submit}
          onCancel={closeForm}
        />
      </EntityFormDialog>

      <AlertDialog
        open={deleteTarget != null}
        onOpenChange={(open) => (open ? undefined : setDeleteTarget(null))}
      >
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Supprimer cette réservation ?</AlertDialogTitle>
            <AlertDialogDescription>Cette action est irréversible.</AlertDialogDescription>
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