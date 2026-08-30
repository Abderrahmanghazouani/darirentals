"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import {
  AlertTriangle,
  CalendarDays,
  ChevronDown,
  ChevronRight,
  Home,
  ScanLine,
  Wallet,
  ClipboardList,
  ArrowUpRight,
} from "lucide-react";
import { useRequireRole } from "@/lib/use-require-role";
import { getEntityClients } from "@/lib/api";

import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { ClientDto } from "@/lib/types/Client";
import { ChargeDto } from "@/lib/types/Charge";
import { TaskDto } from "@/lib/types/Task";
import { ReservationRequestDto } from "@/lib/types/ReservationRequest";
import { CANCELLED_STATUS_CODE } from "@/lib/compute-monthly-financials";
import { computeHealthScore } from "@/lib/dashboard/health-score";
import { isDueTodayOrOverdue, isOverdue } from "@/lib/tasks/is-overdue";
import { HealthScoreCard } from "@/components/dashboard/health-score-card";
import { RevenueIntelligenceCard } from "@/components/dashboard/revenue-intelligence-card";
import { PropertyPerformanceCard } from "@/components/dashboard/property-performance-card";
import { ActionCenterCard } from "@/components/dashboard/action-center-card";
import { MorningInsightsCard } from "@/components/dashboard/morning-insights-card";
import { PortfolioChatCard } from "@/components/dashboard/portfolio-chat-card";
import { buildAssistantFacts } from "@/lib/dashboard/ai-facts";
import { CurrencyProvider, useCurrency } from "@/lib/currency/currency-context";
import { StatCard } from "@/components/stat-card";
import { StatusBadge } from "@/components/status-badge";
import { ReservationCalendar } from "@/components/reservations/reservation-calendar";
import { getCurrentUser, CurrentUser } from "@/lib/auth";

const ROLE = "admin" as const;

const fetchCurrencies = () => getEntityClients(ROLE).currency.findAll();
const fetchRates = () => getEntityClients(ROLE).exchangeRate.findAll();

function todayIso() {
  return new Date().toISOString().slice(0, 10);
}

function longDateToday(): string {
  return new Date().toLocaleDateString("fr-FR", {
    weekday: "long",
    day: "numeric",
    month: "long",
    year: "numeric",
  });
}

function currentMonthLabel(month: Date): string {
  return month.toLocaleDateString("fr-FR", { month: "long", year: "numeric" });
}

// Animation d'apparition discrète, partagée par toutes les sections du dashboard.
const ENTRANCE = "animate-in fade-in slide-in-from-bottom-2 duration-200 fill-mode-both";

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
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [calendarMonth, setCalendarMonth] = useState(() => new Date());

  useEffect(() => {
    setUser(getCurrentUser());
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
    const chs = charges ?? [];
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

    // Revenu net = revenus - charges, même logique que la page de rentabilité par propriété
    // (app/admin/property/[id]/rentabilite/page.tsx), agrégée ici sur toutes les propriétés à
    // partir des mêmes données déjà chargées.
    const totalCharges = chs.reduce((sum, c) => sum + (c.amount ?? 0), 0);
    const netRevenue = totalRevenue - totalCharges;

    const recent = [...resas]
      .sort((a, b) => (b.checkInDate ?? "").localeCompare(a.checkInDate ?? ""))
      .slice(0, 6);

    return {
      totalProperties: props.length,
      activeProperties: activeCount,
      totalReservations: resas.length,
      upcomingReservations: upcoming,
      totalRevenue,
      netRevenue,
      totalClients: (clients ?? []).length,
      recentReservations: recent,
    };
  }, [properties, reservations, clients, charges]);

  const todoTasks = useMemo(() => {
    return (tasks ?? [])
      .filter(isDueTodayOrOverdue)
      .sort((a, b) => (a.dueDate ?? "").localeCompare(b.dueDate ?? ""));
  }, [tasks]);

  const healthScore = useMemo(() => {
    return computeHealthScore(properties ?? [], reservations ?? [], charges ?? [], tasks ?? []);
  }, [properties, reservations, charges, tasks]);

  const assistantFacts = useMemo(() => {
    return buildAssistantFacts(
      properties ?? [],
      reservations ?? [],
      charges ?? [],
      tasks ?? [],
      reservationRequests ?? []
    );
  }, [properties, reservations, charges, tasks, reservationRequests]);

  const loading =
    properties === null ||
    reservations === null ||
    clients === null ||
    charges === null ||
    tasks === null ||
    reservationRequests === null;

  const firstName = user?.firstName?.trim();

  return (
    <div className="w-full min-w-0 space-y-6">
      {/* En-tête */}
      <div className={`flex flex-wrap items-start justify-between gap-4 ${ENTRANCE}`}>
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
            {longDateToday()}
          </p>
          <h1 className="mt-1 text-3xl font-bold tracking-tight">
            Bonjour{firstName ? ` ${firstName}` : ""},{" "}
            <span className="font-normal text-muted-foreground">ravi de vous revoir.</span>
          </h1>
          <p className="mt-1 text-sm text-muted-foreground">
            Voici ce qui se passe dans votre portefeuille aujourd&apos;hui.
          </p>
        </div>

        <div className="flex items-center gap-2">
          {/* Sélecteur de mois (visuel) */}
          <span className="flex items-center gap-2 rounded-lg border border-border bg-card px-3 py-2 text-sm capitalize">
            <CalendarDays className="size-4 text-muted-foreground" />
            {currentMonthLabel(new Date())}
            <ChevronDown className="size-3.5 text-muted-foreground" />
          </span>
          <Button asChild>
            <Link href="/admin/charges">
              <ScanLine className="size-4" /> Scanner une facture
            </Link>
          </Button>
        </div>
      </div>

      {/* Cartes de stats */}
      <div className={`grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4 ${ENTRANCE}`}>
        <StatCard
          label="Chiffre d'affaires"
          value={loading ? "…" : format(stats.totalRevenue)}
          icon={Wallet}
          iconTone="primary"
          hint="Hors réservations annulées"
        />
        <StatCard
          label="Taux d'occupation"
          // TODO: aucun calcul de taux d'occupation n'existe encore côté client
          // (il faudrait le total de nuits réservées / nuits disponibles sur la période).
          value="—"
          icon={Home}
          iconTone="success"
        />
        <StatCard
          label="Réservations"
          value={loading ? "…" : stats.totalReservations}
          icon={ClipboardList}
          iconTone="warning"
          hint={loading ? undefined : `${stats.upcomingReservations.length} à venir`}
        />
        <StatCard
          label="Revenu net"
          value={loading ? "…" : format(stats.netRevenue)}
          icon={Wallet}
          iconTone="info"
          valueTone={loading ? "default" : stats.netRevenue >= 0 ? "success" : "destructive"}
          hint="Revenus − charges"
        />
      </div>

      {/* Graphique + à faire aujourd'hui */}
      <div className={`grid grid-cols-1 gap-4 lg:grid-cols-3 ${ENTRANCE}`}>
        <div className="lg:col-span-2">
          {loading ? (
            <Card>
              <CardContent>
                <p className="py-12 text-center text-sm text-muted-foreground">Chargement...</p>
              </CardContent>
            </Card>
          ) : (
            <RevenueIntelligenceCard
              reservations={reservations ?? []}
              charges={charges ?? []}
              formatValue={format}
            />
          )}
        </div>

        <Card>
          <CardHeader>
            <CardTitle className="text-base">À faire aujourd&apos;hui</CardTitle>
            <p className="text-sm text-muted-foreground">Les tâches qui nécessitent votre attention</p>
          </CardHeader>
          <CardContent className="space-y-3">
            {loading ? (
              <p className="text-sm text-muted-foreground">Chargement...</p>
            ) : todoTasks.length === 0 ? (
              <p className="text-sm text-muted-foreground">Rien d&apos;urgent aujourd&apos;hui.</p>
            ) : (
              todoTasks.slice(0, 5).map((t) => (
                <Link
                  key={t.id}
                  href="/admin/tasks"
                  className="flex items-center gap-3 rounded-lg border border-border px-3 py-2.5 text-sm transition-colors hover:bg-accent/60"
                >
                  <AlertTriangle
                    className={`size-4 shrink-0 ${isOverdue(t) ? "text-destructive" : "text-warning"}`}
                  />
                  <div className="min-w-0 flex-1">
                    <p className="truncate font-medium">{t.title}</p>
                    <p className="truncate text-xs text-muted-foreground">
                      {t.property?.name ?? t.taskType?.label ?? "Tâche"}
                      {t.dueDate ? ` · ${t.dueDate}` : ""}
                    </p>
                  </div>
                  <ChevronRight className="size-4 shrink-0 text-muted-foreground" />
                </Link>
              ))
            )}
            <Link
              href="/admin/tasks"
              className="inline-flex items-center gap-1 pt-1 text-sm font-medium text-primary hover:underline"
            >
              Voir toutes les tâches <ArrowUpRight className="size-3.5" />
            </Link>
          </CardContent>
        </Card>
      </div>

      {/* Réservations récentes + calendrier */}
      <div className={`grid grid-cols-1 gap-4 lg:grid-cols-2 ${ENTRANCE}`}>
        <Card>
          <CardHeader>
            <div className="flex items-center justify-between">
              <div>
                <CardTitle className="text-base">Réservations récentes</CardTitle>
                <p className="text-sm text-muted-foreground">Les dernières réservations de vos logements</p>
              </div>
              <Link
                href="/admin/reservations"
                className="inline-flex items-center gap-1 text-sm font-medium text-primary hover:underline"
              >
                Voir toutes <ArrowUpRight className="size-3.5" />
              </Link>
            </div>
          </CardHeader>
          <CardContent>
            {loading ? (
              <p className="text-sm text-muted-foreground">Chargement...</p>
            ) : stats.recentReservations.length === 0 ? (
              <p className="text-sm text-muted-foreground">Aucune réservation.</p>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-border text-left text-xs uppercase tracking-wide text-muted-foreground">
                      <th className="pb-2 font-medium">Logement</th>
                      <th className="pb-2 font-medium">Client</th>
                      <th className="pb-2 font-medium">Dates</th>
                      <th className="pb-2 text-right font-medium">Montant</th>
                      <th className="pb-2 text-right font-medium">Statut</th>
                    </tr>
                  </thead>
                  <tbody>
                    {stats.recentReservations.map((r) => (
                      <tr key={r.id} className="border-b border-border last:border-0">
                        <td className="py-2.5 font-medium">{r.property?.name ?? "—"}</td>
                        <td className="py-2.5 text-muted-foreground">{r.client?.fullName ?? "—"}</td>
                        <td className="py-2.5 text-muted-foreground">
                          {r.checkInDate ?? "—"}
                          {r.checkOutDate ? ` — ${r.checkOutDate}` : ""}
                        </td>
                        <td className="py-2.5 text-right font-mono">
                          {r.amount != null ? format(r.amount) : "—"}
                        </td>
                        <td className="py-2.5 text-right">
                          <StatusBadge status={r.reservationStatus} />
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            )}
          </CardContent>
        </Card>

        <Card>
          <CardHeader>
            <CardTitle className="text-base">Calendrier</CardTitle>
            <p className="text-sm text-muted-foreground">Occupation des logements</p>
          </CardHeader>
          <CardContent className="text-xs">
            <ReservationCalendar
              month={calendarMonth}
              onMonthChange={setCalendarMonth}
              reservations={reservations ?? []}
              onReservationClick={() => {}}
              loading={loading}
            />
          </CardContent>
        </Card>
      </div>

      {/* Analyses & assistant (fonctionnalités existantes) */}
      {!loading && (
        <div className={`space-y-4 ${ENTRANCE}`}>
          <MorningInsightsCard facts={assistantFacts} role={ROLE} />
          <HealthScoreCard score={healthScore} />
          <div className="grid grid-cols-1 gap-4 lg:grid-cols-2">
            <ActionCenterCard tasks={tasks ?? []} reservationRequests={reservationRequests ?? []} />
            <PortfolioChatCard facts={assistantFacts} role={ROLE} />
          </div>
          <PropertyPerformanceCard
            properties={properties ?? []}
            reservations={reservations ?? []}
            charges={charges ?? []}
            formatValue={format}
          />
        </div>
      )}
    </div>
  );
}
