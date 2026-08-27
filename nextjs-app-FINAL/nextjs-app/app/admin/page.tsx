"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import {
  Building2,
  CalendarCheck,
  CalendarDays,
  Coins,
  FileBarChart,
  Grid3x3,
  Receipt,
  Users,
  Wallet,
  ListTodo,
} from "lucide-react";
import { useRequireRole } from "@/lib/use-require-role";
import { getEntityClients } from "@/lib/api";
import { entityRegistry, entityKeys } from "@/lib/entity-registry";

import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { ClientDto } from "@/lib/types/Client";
import { ChargeDto } from "@/lib/types/Charge";
import { TaskDto } from "@/lib/types/Task";
import { ReservationRequestDto } from "@/lib/types/ReservationRequest";
import { CANCELLED_STATUS_CODE } from "@/lib/compute-monthly-financials";
import { computeHealthScore } from "@/lib/dashboard/health-score";
import { HealthScoreCard } from "@/components/dashboard/health-score-card";
import { PremiumHeader } from "@/components/dashboard/premium-header";
import { RevenueIntelligenceCard } from "@/components/dashboard/revenue-intelligence-card";
import { PropertyPerformanceCard } from "@/components/dashboard/property-performance-card";
import { ActionCenterCard } from "@/components/dashboard/action-center-card";
import { CurrencyProvider, useCurrency } from "@/lib/currency/currency-context";

const ROLE = "admin" as const;

const fetchCurrencies = () => getEntityClients(ROLE).currency.findAll();
const fetchRates = () => getEntityClients(ROLE).exchangeRate.findAll();

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}

const tools = [
  { href: "/admin/property", label: "Propriétés", icon: Building2 },
  { href: "/admin/reservations", label: "Réservations (calendrier)", icon: CalendarDays },
  { href: "/admin/charges", label: "Charges", icon: Receipt },
  { href: "/admin/payments", label: "Paiements aux prestataires", icon: Wallet },
  { href: "/admin/tasks", label: "Tâches", icon: ListTodo },
  { href: "/admin/exchange-rates", label: "Taux de change", icon: Coins },
  { href: "/admin/financial-reports", label: "Rapports financiers", icon: FileBarChart },
];

export default function AdminHome() {
  const ready = useRequireRole(ROLE);
  if (!ready) return null;

  return (
    <CurrencyProvider fetchCurrencies={fetchCurrencies} fetchRates={fetchRates}>
      <AdminDashboard />
    </CurrencyProvider>
  );
}

function AdminDashboard() {
  const { format } = useCurrency();

  const [properties, setProperties] = useState<PropertyDto[] | null>(null);
  const [reservations, setReservations] = useState<ReservationDto[] | null>(null);
  const [clients, setClients] = useState<ClientDto[] | null>(null);
  const [charges, setCharges] = useState<ChargeDto[] | null>(null);
  const [tasks, setTasks] = useState<TaskDto[] | null>(null);
  const [reservationRequests, setReservationRequests] = useState<ReservationRequestDto[] | null>(null);
  const [showAllModules, setShowAllModules] = useState(false);

  useEffect(() => {
    const clients_ = getEntityClients(ROLE);
    clients_.property.findAll().then((d) => setProperties(d ?? [])).catch(() => setProperties([]));
    clients_.reservation.findAll().then((d) => setReservations(d ?? [])).catch(() => setReservations([]));
    clients_.client.findAll().then((d) => setClients(d ?? [])).catch(() => setClients([]));
    clients_.charge.findAll().then((d) => setCharges(d ?? [])).catch(() => setCharges([]));
    clients_.task.findAll().then((d) => setTasks(d ?? [])).catch(() => setTasks([]));
    clients_.reservationRequest
      .findAll()
      .then((d) => setReservationRequests(d ?? []))
      .catch(() => setReservationRequests([]));
  }, []);

  const stats = useMemo(() => {
    const props = properties ?? [];
    const resas = reservations ?? [];
    const today = todayIso();

    const activeCount = props.filter((p) => p.propertyStatus?.code === "Active").length;

    const upcoming = resas
      .filter((r) => r.checkInDate && r.checkInDate >= today)
      .sort((a, b) => (a.checkInDate ?? "").localeCompare(b.checkInDate ?? ""));

    // Une réservation annulée n'est pas un revenu réel - voir aussi computeMonthlyFinancials,
    // qui applique le même filtre pour le graphique.
    const totalRevenue = resas
      .filter((r) => r.reservationStatus?.code !== CANCELLED_STATUS_CODE)
      .reduce((sum, r) => sum + (r.amount ?? 0), 0);

    return {
      totalProperties: props.length,
      activeProperties: activeCount,
      totalReservations: resas.length,
      upcomingReservations: upcoming,
      totalRevenue,
      totalClients: (clients ?? []).length,
    };
  }, [properties, reservations, clients]);

  const healthScore = useMemo(() => {
    return computeHealthScore(properties ?? [], reservations ?? [], charges ?? [], tasks ?? []);
  }, [properties, reservations, charges, tasks]);

  const loading =
    properties === null ||
    reservations === null ||
    clients === null ||
    charges === null ||
    tasks === null ||
    reservationRequests === null;

  return (
    <div className="p-6 max-w-6xl mx-auto space-y-6">
      <PremiumHeader activeProperties={stats.activeProperties} loading={loading} />

      {loading ? (
        <p className="text-sm text-muted-foreground text-center py-8">Chargement...</p>
      ) : (
        <HealthScoreCard score={healthScore} />
      )}

      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
        <Card>
          <CardContent className="pt-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Propriétés</p>
                <p className="text-3xl font-bold">
                  {loading ? "…" : stats.totalProperties}
                </p>
                <p className="text-xs text-muted-foreground mt-1">
                  {loading ? "" : `${stats.activeProperties} active(s)`}
                </p>
              </div>
              <Building2 className="size-8 text-muted-foreground" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Réservations</p>
                <p className="text-3xl font-bold">
                  {loading ? "…" : stats.totalReservations}
                </p>
                <p className="text-xs text-muted-foreground mt-1">
                  {loading ? "" : `${stats.upcomingReservations.length} à venir`}
                </p>
              </div>
              <CalendarCheck className="size-8 text-muted-foreground" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Clients</p>
                <p className="text-3xl font-bold">
                  {loading ? "…" : stats.totalClients}
                </p>
              </div>
              <Users className="size-8 text-muted-foreground" />
            </div>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="pt-6">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm text-muted-foreground">Revenu (réservations)</p>
                <p className="text-3xl font-bold">
                  {loading ? "…" : format(stats.totalRevenue)}
                </p>
              </div>
              <Wallet className="size-8 text-muted-foreground" />
            </div>
          </CardContent>
        </Card>
      </div>

      {loading ? (
        <Card>
          <CardContent>
            <p className="text-sm text-muted-foreground text-center py-12">Chargement...</p>
          </CardContent>
        </Card>
      ) : (
        <RevenueIntelligenceCard reservations={reservations ?? []} charges={charges ?? []} formatValue={format} />
      )}

      {loading ? (
        <Card>
          <CardContent>
            <p className="text-sm text-muted-foreground text-center py-12">Chargement...</p>
          </CardContent>
        </Card>
      ) : (
        <PropertyPerformanceCard
          properties={properties ?? []}
          reservations={reservations ?? []}
          charges={charges ?? []}
          formatValue={format}
        />
      )}

      <Card>
        <CardHeader>
          <CardTitle>Outils</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-2">
            {tools.map(({ href, label, icon: Icon }) => (
              <Link
                key={href}
                href={href}
                className="flex items-center gap-2 rounded-md border-2 border-primary/20 bg-primary/5 px-4 py-3 text-sm font-medium hover:bg-primary/10 transition-colors"
              >
                <Icon className="size-4" />
                {label}
              </Link>
            ))}
          </div>
        </CardContent>
      </Card>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
        <Card>
          <CardHeader>
            <CardTitle>Prochaines arrivées</CardTitle>
          </CardHeader>
          <CardContent>
            {loading ? (
              <p className="text-sm text-muted-foreground">Chargement...</p>
            ) : stats.upcomingReservations.length === 0 ? (
              <p className="text-sm text-muted-foreground">Aucune arrivée à venir.</p>
            ) : (
              <div className="space-y-2">
                {stats.upcomingReservations.slice(0, 5).map((r) => (
                  <div
                    key={r.id}
                    className="flex items-center justify-between border-b pb-2 last:border-0 last:pb-0"
                  >
                    <div>
                      <p className="font-medium">
                        {r.property?.name ?? "Propriété inconnue"}
                      </p>
                      <p className="text-sm text-muted-foreground">
                        {r.client?.fullName ?? "Client inconnu"} — arrivée le {r.checkInDate}
                      </p>
                    </div>
                    {r.reservationStatus && <Badge>{r.reservationStatus.label}</Badge>}
                  </div>
                ))}
              </div>
            )}
          </CardContent>
        </Card>

        {loading ? (
          <Card>
            <CardContent>
              <p className="text-sm text-muted-foreground text-center py-12">Chargement...</p>
            </CardContent>
          </Card>
        ) : (
          <ActionCenterCard tasks={tasks ?? []} reservationRequests={reservationRequests ?? []} />
        )}
      </div>

      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2">
              <Grid3x3 className="size-5" /> Tous les modules
            </CardTitle>
            <Button variant="ghost" size="sm" onClick={() => setShowAllModules((v) => !v)}>
              {showAllModules ? "Masquer" : "Afficher"}
            </Button>
          </div>
        </CardHeader>
        {showAllModules && (
          <CardContent>
            <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-2">
              {entityKeys.map((key) => (
                <Link
                  key={key}
                  href={`/admin/${key}`}
                  className="rounded-md border px-3 py-2 text-sm hover:bg-accent transition-colors"
                >
                  {entityRegistry[key].label}
                </Link>
              ))}
            </div>
          </CardContent>
        )}
      </Card>
    </div>
  );
}