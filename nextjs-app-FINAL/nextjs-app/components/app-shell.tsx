"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import {
  Building2,
  Bell,
  ChevronRight,
  ChevronsUpDown,
  LogOut,
  Search,
  type LucideIcon,
} from "lucide-react";
import { cn } from "@/lib/utils";
import { logout, getCurrentUser, CurrentUser } from "@/lib/auth";
import { getEntityClients } from "@/lib/api";
import { getSelectedEnterpriseId } from "@/lib/enterprise-context";
import type { Role } from "@/lib/api-client";

export interface NavItem {
  label: string;
  href: string;
  icon: LucideIcon;
  /** "reservations" -> badge = nb de réservations à venir (calcul réel). */
  badge?: "reservations";
}

export interface NavSection {
  title: string;
  items: NavItem[];
  /** Liens affichés en gris, non cliquables (pages inexistantes). */
  disabled?: { label: string; icon: LucideIcon }[];
}

interface AppShellProps {
  role: Role;
  sections: NavSection[];
  children: React.ReactNode;
}

function initialsFor(user: CurrentUser | null): string {
  if (!user) return "?";
  if (user.firstName || user.lastName) {
    return `${user.firstName?.[0] ?? ""}${user.lastName?.[0] ?? ""}`.toUpperCase() || "?";
  }
  return user.username.slice(0, 2).toUpperCase();
}

function displayNameFor(user: CurrentUser | null): string {
  if (!user) return "Compte";
  if (user.firstName || user.lastName) {
    return [user.firstName, user.lastName].filter(Boolean).join(" ");
  }
  return user.username;
}

function breadcrumbLabel(pathname: string): string {
  const seg = pathname.split("/").filter(Boolean);
  const last = seg[seg.length - 1] ?? "";
  const map: Record<string, string> = {
    admin: "Vue d'ensemble",
    collaborator: "Vue d'ensemble",
    property: "Biens & logements",
    reservations: "Réservations",
    "reservation-requests": "Demandes de réservation",
    client: "Clients",
    charges: "Charges",
    payments: "Paiements",
    tasks: "Tâches",
    collaborator2: "Collaborateurs",
    "exchange-rates": "Taux de change",
    "financial-reports": "Rapports financiers",
    rentabilite: "Rentabilité",
  };
  if (last === "admin" || last === "collaborator") return "Vue d'ensemble";
  return map[last] ?? last.charAt(0).toUpperCase() + last.slice(1);
}

export function AppShell({ role, sections, children }: AppShellProps) {
  const pathname = usePathname() || "";
  const router = useRouter();
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [upcomingCount, setUpcomingCount] = useState<number | null>(null);
  const [enterpriseName, setEnterpriseName] = useState<string | null>(null);

  useEffect(() => {
    const u = getCurrentUser();
    setUser(u);

    const clients = getEntityClients(role);

    // Badge "Réservations à venir" - même filtre que le dashboard (checkInDate >= aujourd'hui).
    clients.reservation
      .findAll()
      .then((resas) => {
        const today = new Date().toISOString().slice(0, 10);
        const count = (resas ?? []).filter(
          (r) => r.checkInDate && r.checkInDate >= today
        ).length;
        setUpcomingCount(count);
      })
      .catch(() => setUpcomingCount(null));

    // Nom de la société active - non bloquant.
    if (role === "collaborator") {
      const enterpriseId = getSelectedEnterpriseId();
      clients.collaborator
        .findAll()
        .then((all) => {
          const me = (all ?? []).find((c) => c.username === u?.username);
          const memberships = me?.enterpriseMemberships ?? [];
          const active =
            memberships.find((m) => m.enterprise?.id === enterpriseId) ?? memberships[0];
          setEnterpriseName(active?.enterprise?.name ?? null);
        })
        .catch(() => setEnterpriseName(null));
    } else {
      clients.enterprise
        .findAll()
        .then((all) => setEnterpriseName((all ?? [])[0]?.name ?? null))
        .catch(() => setEnterpriseName(null));
    }
  }, [role]);

  const enterpriseInitials = useMemo(() => {
    if (!enterpriseName) return "—";
    return enterpriseName
      .split(/\s+/)
      .slice(0, 2)
      .map((w) => w[0])
      .join("")
      .toUpperCase();
  }, [enterpriseName]);

  function handleLogout() {
    logout();
    router.push("/login");
  }

  function isActive(href: string): boolean {
    if (href === `/${role}`) return pathname === href;
    return pathname === href || pathname.startsWith(href + "/");
  }

  return (
    <div className="flex min-h-screen w-full bg-background text-foreground">
      {/* Sidebar */}
      <aside className="sticky top-0 hidden h-screen w-[248px] shrink-0 flex-col border-r border-border bg-[#faf8f6] px-3 py-4 lg:flex">
        <Link href={`/${role}`} className="flex items-center gap-2.5 px-2">
          <span className="flex size-8 items-center justify-center rounded-lg bg-primary text-primary-foreground">
            <Building2 className="size-4.5" />
          </span>
          <span className="text-lg font-bold tracking-tight text-primary">DariRentals</span>
        </Link>

        {/* Encart société active (visuel) */}
        <div className="mt-4 flex items-center gap-2.5 rounded-lg border border-border bg-card px-2.5 py-2">
          <span className="flex size-8 shrink-0 items-center justify-center rounded-md bg-secondary text-xs font-semibold text-secondary-foreground">
            {enterpriseInitials}
          </span>
          <div className="min-w-0 flex-1">
            <p className="truncate text-sm font-semibold">{enterpriseName ?? "—"}</p>
            <p className="truncate text-xs text-muted-foreground">Agence principale</p>
          </div>
          <ChevronsUpDown className="size-3.5 shrink-0 text-muted-foreground" />
        </div>

        <nav className="mt-5 flex flex-1 flex-col gap-5 overflow-y-auto">
          {sections.map((section) => (
            <div key={section.title}>
              <p className="px-2 pb-1.5 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground">
                {section.title}
              </p>
              <ul className="space-y-0.5">
                {section.items.map((item) => {
                  const active = isActive(item.href);
                  const Icon = item.icon;
                  return (
                    <li key={item.label}>
                      <Link
                        href={item.href}
                        className={cn(
                          "flex items-center gap-2.5 rounded-lg px-2.5 py-2 text-sm transition-colors",
                          active
                            ? "bg-accent font-semibold text-primary"
                            : "text-muted-foreground hover:bg-accent/60 hover:text-foreground"
                        )}
                      >
                        <Icon className="size-4.5 shrink-0" />
                        <span className="flex-1 truncate">{item.label}</span>
                        {item.badge === "reservations" && upcomingCount != null && upcomingCount > 0 && (
                          <span className="rounded-full bg-secondary px-1.5 py-0.5 text-xs font-medium text-secondary-foreground">
                            {upcomingCount}
                          </span>
                        )}
                      </Link>
                    </li>
                  );
                })}
                {section.disabled?.map(({ label, icon: Icon }) => (
                  <li key={label}>
                    <span className="pointer-events-none flex items-center gap-2.5 rounded-lg px-2.5 py-2 text-sm text-muted-foreground opacity-50">
                      <Icon className="size-4.5 shrink-0" />
                      <span className="flex-1 truncate">{label}</span>
                    </span>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </nav>

        <div className="mt-auto flex items-center gap-2.5 border-t border-border px-1 pt-3">
          <span className="flex size-8 shrink-0 items-center justify-center rounded-full bg-secondary text-xs font-semibold text-secondary-foreground">
            {initialsFor(user)}
          </span>
          <div className="min-w-0 flex-1">
            <p className="truncate text-sm font-medium">{displayNameFor(user)}</p>
            {user?.email && (
              <p className="truncate text-xs text-muted-foreground">{user.email}</p>
            )}
          </div>
          <button
            type="button"
            onClick={handleLogout}
            aria-label="Déconnexion"
            className="flex size-8 shrink-0 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:bg-accent hover:text-destructive"
          >
            <LogOut className="size-4" />
          </button>
        </div>
      </aside>

      {/* Colonne principale */}
      <div className="flex min-w-0 flex-1 flex-col">
        {/* Topbar */}
        <header className="sticky top-0 z-20 flex h-16 items-center justify-between gap-4 border-b border-border bg-card/80 px-4 backdrop-blur supports-backdrop-filter:bg-card/60 sm:px-6">
          <div className="flex items-center gap-1.5 text-sm">
            <span className="text-muted-foreground">Accueil</span>
            <ChevronRight className="size-3.5 text-muted-foreground" />
            <span className="font-medium">{breadcrumbLabel(pathname)}</span>
          </div>

          <div className="flex items-center gap-2 sm:gap-3">
            <div className="hidden items-center gap-2 rounded-lg border border-border bg-background px-2.5 py-1.5 text-sm text-muted-foreground sm:flex">
              <Search className="size-3.5" />
              <span>Rechercher</span>
              <kbd className="rounded border border-border bg-muted px-1 text-[10px]">⌘K</kbd>
            </div>
            <button
              type="button"
              aria-label="Notifications"
              className="flex size-9 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:bg-accent"
            >
              <Bell className="size-4.5" />
            </button>
            <span className="flex size-9 items-center justify-center rounded-full bg-secondary text-xs font-semibold text-secondary-foreground">
              {initialsFor(user)}
            </span>
          </div>
        </header>

        <main className="page-content flex-1">{children}</main>
      </div>
    </div>
  );
}
