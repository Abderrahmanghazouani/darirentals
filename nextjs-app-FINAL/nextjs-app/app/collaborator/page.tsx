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
  ScanLine,
  ArrowUpRight,
} from "lucide-react";
import { useRequireRole } from "@/lib/use-require-role";
import { getEntityClients } from "@/lib/api";

import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { ClientDto } from "@/lib/types/Client";
import { ChargeDto } from "@/lib/types/Charge";
import { TaskDto } from "@/lib/types/Task";
import { isDueTodayOrOverdue, isOverdue } from "@/lib/tasks/is-overdue";
import { useCurrency } from "@/lib/currency/currency-context";
import { StatusBadge } from "@/components/status-badge";
import { ReservationCalendar } from "@/components/reservations/reservation-calendar";
import { RevenueIntelligenceCard } from "@/components/dashboard/revenue-intelligence-card";
import { PropertyMapCard } from "@/components/dashboard/property-map-card";
import { DayTimelineCard } from "@/components/dashboard/day-timeline-card";
import { getCurrentUser, CurrentUser } from "@/lib/auth";
import { getSelectedEnterpriseId } from "@/lib/enterprise-context";
import { useSelectedEnterpriseId } from "@/lib/use-selected-enterprise";
import { filterByEnterprise } from "@/lib/filter-by-enterprise";
import { entityRegistry, entityKeys } from "@/lib/entity-registry";
import { useLanguage } from "@/lib/i18n/language-context";
import type { Locale } from "@/lib/i18n/translations";

const ROLE = "collaborator" as const;

// La date reste au format long propre à la langue choisie (même logique que app/admin/page.tsx).
function localeTag(locale: Locale) {
  return locale === "en" ? "en-US" : "fr-FR";
}

function longDateToday(locale: Locale): string {
  return new Date().toLocaleDateString(localeTag(locale), {
    weekday: "long",
    day: "numeric",
    month: "long",
    year: "numeric",
  });
}

function currentMonthLabel(month: Date, locale: Locale): string {
  return month.toLocaleDateString(localeTag(locale), { month: "long", year: "numeric" });
}

// Animation d'apparition discrète, partagée par toutes les sections du dashboard.
const ENTRANCE = "animate-in fade-in slide-in-from-bottom-2 duration-200 fill-mode-both";

export default function CollaboratorHome() {
  const ready = useRequireRole(ROLE);
  if (!ready) return null;

  // CurrencyProvider posé au niveau du layout (app/collaborator/layout.tsx) depuis le chantier
  // nettoyage-finitions - plus besoin ici, useCurrency() dans CollaboratorDashboard le trouve
  // via l'ancêtre. Voir NOTES-nettoyage.md, point 1.
  return <CollaboratorDashboard />;
}

function CollaboratorDashboard() {
  const { format } = useCurrency();
  const { dict, locale } = useLanguage();

  // Société active : même source que toutes les autres pages collaborateur filtrées.
  const enterpriseId = useSelectedEnterpriseId();

  const [properties, setProperties] = useState<PropertyDto[] | null>(null);
  const [reservations, setReservations] = useState<ReservationDto[] | null>(null);
  const [clients, setClients] = useState<ClientDto[] | null>(null);
  const [charges, setCharges] = useState<ChargeDto[] | null>(null);
  const [tasks, setTasks] = useState<TaskDto[] | null>(null);
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [enterpriseName, setEnterpriseName] = useState<string | null>(null);
  const [calendarMonth, setCalendarMonth] = useState(() => new Date());

  useEffect(() => {
    const u = getCurrentUser();
    setUser(u);

    const clients_ = getEntityClients(ROLE);
    clients_.property.findAll().then((d) => setProperties(d ?? [])).catch(() => setProperties([]));
    clients_.reservation.findAll().then((d) => setReservations(d ?? [])).catch(() => setReservations([]));
    clients_.client.findAll().then((d) => setClients(d ?? [])).catch(() => setClients([]));
    clients_.charge.findAll().then((d) => setCharges(d ?? [])).catch(() => setCharges([]));
    clients_.task.findAll().then((d) => setTasks(d ?? [])).catch(() => setTasks([]));

    // Nom de la société active - même résolution que la sidebar (app-shell.tsx) : le backend
    // n'expose pas de "/me", on retrouve le profil via le username puis la bonne adhésion.
    const activeId = getSelectedEnterpriseId();
    clients_.collaborator
      .findAll()
      .then((all) => {
        const me = (all ?? []).find((c) => c.username === u?.username);
        const memberships = me?.enterpriseMemberships ?? [];
        const active = memberships.find((m) => m.enterprise?.id === activeId) ?? memberships[0];
        setEnterpriseName(active?.enterprise?.name ?? null);
      })
      .catch(() => setEnterpriseName(null));
  }, []);

  // Données cloisonnées sur la société active - exactement le même passage par
  // filterByEnterprise que app/collaborator/charges/page.tsx, avant tout calcul.
  const scopedProperties = useMemo(
    () => filterByEnterprise(properties ?? [], enterpriseId),
    [properties, enterpriseId]
  );
  const scopedReservations = useMemo(
    () => filterByEnterprise(reservations ?? [], enterpriseId),
    [reservations, enterpriseId]
  );
  const scopedCharges = useMemo(
    () => filterByEnterprise(charges ?? [], enterpriseId),
    [charges, enterpriseId]
  );
  const scopedTasks = useMemo(
    () => filterByEnterprise(tasks ?? [], enterpriseId),
    [tasks, enterpriseId]
  );

  // Le calcul de revenu/occupation/clients qui alimentait les 4 cartes de stats a été retiré
  // avec elles (voir NOTES-dashboard-carte-timeline.md) - seule la liste "Réservations
  // récentes" plus bas dans la page utilise encore des données agrégées ici.
  const recentReservations = useMemo(() => {
    return [...scopedReservations]
      .sort((a, b) => (b.checkInDate ?? "").localeCompare(a.checkInDate ?? ""))
      .slice(0, 6);
  }, [scopedReservations]);

  const todoTasks = useMemo(() => {
    return scopedTasks
      .filter(isDueTodayOrOverdue)
      .sort((a, b) => (a.dueDate ?? "").localeCompare(b.dueDate ?? ""));
  }, [scopedTasks]);

  const loading =
    properties === null ||
    reservations === null ||
    clients === null ||
    charges === null ||
    tasks === null;

  const firstName = user?.firstName?.trim();

  return (
    <div className="w-full min-w-0 space-y-6">
      {/* En-tête */}
      <div className={`flex flex-wrap items-start justify-between gap-4 ${ENTRANCE}`}>
        <div>
          <p className="text-xs font-semibold uppercase tracking-wider text-muted-foreground">
            {longDateToday(locale)}
          </p>
          <h1 className="mt-1 text-3xl font-bold tracking-tight">
            {dict.dashboardHome.greeting}{firstName ? ` ${firstName}` : ""},{" "}
            <span className="font-normal text-muted-foreground">
              {dict.dashboardHome.greetingReturning}
            </span>
          </h1>
          <p className="mt-1 text-sm text-muted-foreground">
            {dict.dashboardHome.subtitleCompanyPrefix}{" "}
            <span className="font-medium text-foreground">
              {enterpriseName ?? dict.dashboardHome.subtitleCompanyFallback}
            </span>{" "}
            {dict.dashboardHome.subtitleCompanySuffix}
          </p>
        </div>

        <div className="flex items-center gap-2">
          {/* Sélecteur de mois (visuel) */}
          <span className="flex items-center gap-2 rounded-lg border border-border bg-card px-3 py-2 text-sm capitalize">
            <CalendarDays className="size-4 text-muted-foreground" />
            {currentMonthLabel(new Date(), locale)}
            <ChevronDown className="size-3.5 text-muted-foreground" />
          </span>
          <Button asChild>
            <Link href="/collaborator/charges">
              <ScanLine className="size-4" /> {dict.dashboardHome.scanInvoice}
            </Link>
          </Button>
        </div>
      </div>

      {/* Carte des propriétés + Timeline du jour - remplace les 4 cartes de stats, calculées
          uniquement sur la société active (scopedProperties/scopedReservations/scopedTasks,
          déjà filtrées par filterByEnterprise plus haut - même cloisonnement que le reste du
          dashboard collaborateur). Voir NOTES-dashboard-carte-timeline.md. */}
      <div className={`grid grid-cols-1 gap-4 sm:grid-cols-2 ${ENTRANCE}`}>
        {loading ? (
          <Card>
            <CardContent>
              <p className="py-12 text-center text-sm text-muted-foreground">{dict.common.loading}</p>
            </CardContent>
          </Card>
        ) : (
          <PropertyMapCard
            properties={scopedProperties}
            reservations={scopedReservations}
            role="collaborator"
          />
        )}
        {loading ? (
          <Card>
            <CardContent>
              <p className="py-12 text-center text-sm text-muted-foreground">{dict.common.loading}</p>
            </CardContent>
          </Card>
        ) : (
          <DayTimelineCard reservations={scopedReservations} tasks={scopedTasks} role="collaborator" />
        )}
      </div>

      {/* Graphique + à faire aujourd'hui */}
      <div className={`grid grid-cols-1 gap-4 lg:grid-cols-3 ${ENTRANCE}`}>
        {/* min-w-0 : un item de grille CSS a par défaut min-width:auto (taille min basée sur
            son contenu), pas 0 - même sur grid-cols-1 en mobile. Le min-w-0 déjà présent DANS
            RevenueIntelligenceCard (autour de MonthlyChart) ne suffit pas seul : sans celui-ci
            sur l'item de grille lui-même, le graphique recharts forçait toute la page à déborder
            horizontalement sur mobile (voir NOTES-nettoyage.md, point 2). */}
        <div className="lg:col-span-2 min-w-0">
          {loading ? (
            <Card>
              <CardContent>
                <p className="py-12 text-center text-sm text-muted-foreground">{dict.common.loading}</p>
              </CardContent>
            </Card>
          ) : (
            <RevenueIntelligenceCard
              reservations={scopedReservations}
              charges={scopedCharges}
              formatValue={format}
            />
          )}
        </div>

        <Card>
          <CardHeader>
            <CardTitle className="text-base">{dict.dashboardHome.todoTitle}</CardTitle>
            <p className="text-sm text-muted-foreground">{dict.dashboardHome.todoSubtitle}</p>
          </CardHeader>
          <CardContent className="space-y-3">
            {loading ? (
              <p className="text-sm text-muted-foreground">{dict.common.loading}</p>
            ) : todoTasks.length === 0 ? (
              <p className="text-sm text-muted-foreground">{dict.dashboardHome.todoEmpty}</p>
            ) : (
              todoTasks.slice(0, 5).map((t) => (
                <Link
                  key={t.id}
                  href="/collaborator/tasks"
                  className="flex items-center gap-3 rounded-lg border border-border px-3 py-2.5 text-sm transition-colors hover:bg-accent/60"
                >
                  <AlertTriangle
                    className={`size-4 shrink-0 ${isOverdue(t) ? "text-destructive-text" : "text-warning"}`}
                  />
                  <div className="min-w-0 flex-1">
                    <p className="truncate font-medium">{t.title}</p>
                    <p className="truncate text-xs text-muted-foreground">
                      {t.property?.name ?? t.taskType?.label ?? dict.dashboardHome.taskFallback}
                      {t.dueDate ? ` · ${t.dueDate}` : ""}
                    </p>
                  </div>
                  <ChevronRight className="size-4 shrink-0 text-muted-foreground" />
                </Link>
              ))
            )}
            <Link
              href="/collaborator/tasks"
              className="inline-flex items-center gap-1 pt-1 text-sm font-medium text-primary hover:underline"
            >
              {dict.dashboardHome.seeAllTasks} <ArrowUpRight className="size-3.5" />
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
                <CardTitle className="text-base">{dict.dashboardHome.recentReservationsTitle}</CardTitle>
                <p className="text-sm text-muted-foreground">{dict.dashboardHome.recentReservationsSubtitle}</p>
              </div>
              <Link
                href="/collaborator/reservations"
                className="inline-flex items-center gap-1 text-sm font-medium text-primary hover:underline"
              >
                {dict.dashboardHome.seeAll} <ArrowUpRight className="size-3.5" />
              </Link>
            </div>
          </CardHeader>
          <CardContent>
            {loading ? (
              <p className="text-sm text-muted-foreground">{dict.common.loading}</p>
            ) : recentReservations.length === 0 ? (
              <p className="text-sm text-muted-foreground">{dict.dashboardHome.noReservations}</p>
            ) : (
              <div className="overflow-x-auto">
                <table className="w-full text-sm">
                  <thead>
                    <tr className="border-b border-border text-left text-xs uppercase tracking-wide text-muted-foreground">
                      <th className="pb-2 font-medium">{dict.dashboardHome.colProperty}</th>
                      <th className="pb-2 font-medium">{dict.dashboardHome.colClient}</th>
                      <th className="pb-2 font-medium">{dict.dashboardHome.colDates}</th>
                      <th className="pb-2 text-right font-medium">{dict.dashboardHome.colAmount}</th>
                      <th className="pb-2 text-right font-medium">{dict.dashboardHome.colStatus}</th>
                    </tr>
                  </thead>
                  <tbody>
                    {recentReservations.map((r) => (
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
            <CardTitle className="text-base">{dict.dashboardHome.calendarTitle}</CardTitle>
            <p className="text-sm text-muted-foreground">{dict.dashboardHome.calendarSubtitle}</p>
          </CardHeader>
          <CardContent className="text-xs">
            <ReservationCalendar
              month={calendarMonth}
              onMonthChange={setCalendarMonth}
              reservations={scopedReservations}
              onReservationClick={() => {}}
              loading={loading}
            />
          </CardContent>
        </Card>
      </div>

      {/* Accès de repli aux modules génériques - discret, pas le contenu principal. */}
      <details className={`group ${ENTRANCE}`}>
        <summary className="inline-flex cursor-pointer items-center gap-1 text-sm text-muted-foreground hover:text-foreground">
          <ChevronRight className="size-4 transition-transform group-open:rotate-90" />
          {dict.allModules.title}
        </summary>
        <div className="mt-3 grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4">
          {entityKeys.map((key) => (
            <Link
              key={key}
              href={`/collaborator/${key}`}
              className="rounded-lg border border-border px-3 py-2 text-sm transition-colors hover:bg-accent"
            >
              {entityRegistry[key].label}
            </Link>
          ))}
        </div>
      </details>
    </div>
  );
}
