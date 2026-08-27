"use client";

import { useEffect, useMemo, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { ArrowLeft, TrendingUp, TrendingDown, Wallet, Receipt, Scale } from "lucide-react";
import { useRequireRole } from "@/lib/use-require-role";
import { getEntityClients } from "@/lib/api";
import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import { CANCELLED_STATUS_CODE } from "@/lib/compute-monthly-financials";

const ROLE = "admin" as const;

type Period = "month" | "year" | "all";

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}

function periodStart(period: Period): string | null {
  const now = new Date();
  if (period === "month") {
    return new Date(now.getFullYear(), now.getMonth(), 1).toISOString().slice(0, 10);
  }
  if (period === "year") {
    return new Date(now.getFullYear(), 0, 1).toISOString().slice(0, 10);
  }
  return null; // "all" : pas de borne
}

function formatMoney(n: number): string {
  return n.toLocaleString("fr-FR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

export default function PropertyRentabilitePage() {
  const ready = useRequireRole(ROLE);
  const router = useRouter();
  const params = useParams<{ id: string }>();
  const propertyId = Number(params.id);

  const [property, setProperty] = useState<PropertyDto | null>(null);
  const [reservations, setReservations] = useState<ReservationDto[]>([]);
  const [charges, setCharges] = useState<ChargeDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [period, setPeriod] = useState<Period>("all");

  useEffect(() => {
    if (!propertyId) return;
    setLoading(true);
    setError(null);
    const clients = getEntityClients(ROLE);
    Promise.all([
      clients.property.findById(propertyId),
      clients.reservation.findAll(),
      clients.charge.findAll(),
    ])
      .then(([prop, resas, chs]) => {
        setProperty(prop);
        setReservations(resas ?? []);
        setCharges(chs ?? []);
      })
      .catch((e) => setError(e instanceof Error ? e.message : "Erreur de chargement"))
      .finally(() => setLoading(false));
  }, [propertyId]);

  const start = useMemo(() => periodStart(period), [period]);
  const today = todayIso();

  const matchedReservations = useMemo(() => {
    return reservations
      .filter((r) => r.property?.id === propertyId)
      // Une réservation annulée n'est pas un revenu réel - voir aussi computeMonthlyFinancials
      // et app/admin/page.tsx, qui appliquent le même filtre.
      .filter((r) => r.reservationStatus?.code !== CANCELLED_STATUS_CODE)
      .filter((r) => !start || (r.checkInDate && r.checkInDate >= start))
      .sort((a, b) => (b.checkInDate ?? "").localeCompare(a.checkInDate ?? ""));
  }, [reservations, propertyId, start]);

  const matchedCharges = useMemo(() => {
    return charges
      .filter((c) => c.property?.id === propertyId)
      // Une charge sans date (créée avant l'ajout de ce champ) reste visible en "Tout l'historique"
      // mais est exclue des filtres "Ce mois-ci"/"Cette année" faute d'information suffisante.
      .filter((c) => !start || (c.chargeDate && c.chargeDate >= start))
      .sort((a, b) => (b.chargeDate ?? "").localeCompare(a.chargeDate ?? ""));
  }, [charges, propertyId, start]);

  const totalRevenue = useMemo(
    () => matchedReservations.reduce((sum, r) => sum + (r.amount ?? 0), 0),
    [matchedReservations]
  );
  const totalCharges = useMemo(
    () => matchedCharges.reduce((sum, c) => sum + (c.amount ?? 0), 0),
    [matchedCharges]
  );
  const netProfit = totalRevenue - totalCharges;

  if (!ready) return null;

  return (
    <div className="p-6 max-w-5xl mx-auto space-y-4">
      <div className="flex items-center gap-3">
        <Button variant="ghost" size="icon" onClick={() => router.push("/admin/property")}>
          <ArrowLeft className="size-4" />
        </Button>
        <div>
          <h1 className="text-2xl font-semibold">
            Rentabilité — {loading ? "…" : property?.name ?? `Propriété #${propertyId}`}
          </h1>
          <Link href="/admin/property" className="text-sm text-muted-foreground hover:underline">
            Retour aux propriétés
          </Link>
        </div>
      </div>

      {error && <p className="text-destructive text-sm">{error}</p>}

      {/* Sélecteur de période */}
      <div className="flex gap-2">
        <Button variant={period === "month" ? "default" : "outline"} size="sm" onClick={() => setPeriod("month")}>
          Ce mois-ci
        </Button>
        <Button variant={period === "year" ? "default" : "outline"} size="sm" onClick={() => setPeriod("year")}>
          Cette année
        </Button>
        <Button variant={period === "all" ? "default" : "outline"} size="sm" onClick={() => setPeriod("all")}>
          Tout l&apos;historique
        </Button>
      </div>

      {/* Cartes de synthèse */}
      <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
        <Card>
          <CardContent className="pt-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Revenus</p>
                <p className="text-3xl font-bold text-emerald-600">
                  {loading ? "…" : formatMoney(totalRevenue)}
                </p>
                <p className="text-xs text-muted-foreground mt-1">
                  MAD · {matchedReservations.length} réservation{matchedReservations.length > 1 ? "s" : ""}
                </p>
              </div>
              <TrendingUp className="size-8 text-emerald-600/60" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Charges</p>
                <p className="text-3xl font-bold text-rose-600">{loading ? "…" : formatMoney(totalCharges)}</p>
                <p className="text-xs text-muted-foreground mt-1">
                  MAD · {matchedCharges.length} charge{matchedCharges.length > 1 ? "s" : ""}
                </p>
              </div>
              <TrendingDown className="size-8 text-rose-600/60" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Bénéfice net</p>
                <p className={`text-3xl font-bold ${netProfit >= 0 ? "text-emerald-600" : "text-rose-600"}`}>
                  {loading ? "…" : formatMoney(netProfit)}
                </p>
                <p className="text-xs text-muted-foreground mt-1">MAD</p>
              </div>
              <Scale className={`size-8 ${netProfit >= 0 ? "text-emerald-600/60" : "text-rose-600/60"}`} />
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Détail réservations */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-base">
            <Wallet className="size-4" /> Réservations prises en compte
          </CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <p className="text-sm text-muted-foreground py-4 text-center">Chargement...</p>
          ) : matchedReservations.length === 0 ? (
            <p className="text-sm text-muted-foreground py-4 text-center">
              Aucune réservation sur cette période.
            </p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Référence</TableHead>
                  <TableHead>Client</TableHead>
                  <TableHead>Check-in</TableHead>
                  <TableHead>Check-out</TableHead>
                  <TableHead>Statut</TableHead>
                  <TableHead className="text-right">Montant</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {matchedReservations.map((r) => (
                  <TableRow key={r.id}>
                    <TableCell>{r.reference || "—"}</TableCell>
                    <TableCell>{r.client?.fullName ?? "—"}</TableCell>
                    <TableCell>{r.checkInDate ?? "—"}</TableCell>
                    <TableCell>{r.checkOutDate ?? "—"}</TableCell>
                    <TableCell>
                      {r.reservationStatus ? <Badge>{r.reservationStatus.label}</Badge> : "—"}
                    </TableCell>
                    <TableCell className="text-right">
                      {r.amount != null ? formatMoney(r.amount) : "—"}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      {/* Détail charges */}
      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2 text-base">
            <Receipt className="size-4" /> Charges prises en compte
          </CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <p className="text-sm text-muted-foreground py-4 text-center">Chargement...</p>
          ) : matchedCharges.length === 0 ? (
            <p className="text-sm text-muted-foreground py-4 text-center">
              Aucune charge sur cette période.
            </p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Libellé</TableHead>
                  <TableHead>Type</TableHead>
                  <TableHead>Date</TableHead>
                  <TableHead className="text-right">Montant</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {matchedCharges.map((c) => (
                  <TableRow key={c.id}>
                    <TableCell>{c.label}</TableCell>
                    <TableCell>
                      {c.chargeType ? <Badge variant="outline">{c.chargeType.label}</Badge> : "—"}
                    </TableCell>
                    <TableCell>{c.chargeDate ?? "—"}</TableCell>
                    <TableCell className="text-right">
                      {c.amount != null ? formatMoney(c.amount) : "—"}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      {!loading && period !== "all" && charges.some((c) => c.property?.id === propertyId && !c.chargeDate) && (
        <p className="text-xs text-muted-foreground">
          Note : certaines charges de cette propriété n&apos;ont pas de date renseignée — elles sont
          incluses dans &quot;Tout l&apos;historique&quot; mais pas dans les filtres par période.
        </p>
      )}
    </div>
  );
}
