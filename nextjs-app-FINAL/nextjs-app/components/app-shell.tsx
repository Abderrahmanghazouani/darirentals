"use client";

import { useEffect, useMemo, useState } from "react";
import Link from "next/link";
import { usePathname, useRouter } from "next/navigation";
import {
  Building2,
  Bell,
  ChevronLeft,
  ChevronRight,
  ChevronsUpDown,
  LayoutGrid,
  LogOut,
  Menu,
  Search,
  type LucideIcon,
} from "lucide-react";
import { cn } from "@/lib/utils";
import { logout, getCurrentUser, CurrentUser } from "@/lib/auth";
import { getEntityClients } from "@/lib/api";
import { getSelectedEnterpriseId } from "@/lib/enterprise-context";
import type { Role } from "@/lib/api-client";
import { ThemeToggle } from "@/components/theme/theme-toggle";
import { LanguageToggle } from "@/components/i18n/language-toggle";
import { CurrencySelector } from "@/components/currency/currency-selector";
import { Tooltip, TooltipContent, TooltipTrigger } from "@/components/ui/tooltip";
import { Sheet, SheetContent, SheetTitle } from "@/components/ui/sheet";

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
  /** Lien discret en bas de la nav, vers la vue technique listant les entités de référence. */
  modulesHref: string;
  children: React.ReactNode;
}

const COLLAPSE_STORAGE_KEY = "sidebar-collapsed";
const COLLAPSED_WIDTH = "w-[76px]";
const EXPANDED_WIDTH = "w-[248px]";

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
    modules: "Tous les modules",
  };
  if (last === "admin" || last === "collaborator") return "Vue d'ensemble";
  return map[last] ?? last.charAt(0).toUpperCase() + last.slice(1);
}

/** Enrobe un item de nav dans un Tooltip (nom du module) uniquement en mode replié. */
function NavLabel({
  collapsed,
  label,
  children,
}: {
  collapsed: boolean;
  label: string;
  children: React.ReactNode;
}) {
  if (!collapsed) return <>{children}</>;
  return (
    <Tooltip>
      <TooltipTrigger asChild>{children}</TooltipTrigger>
      <TooltipContent side="right">{label}</TooltipContent>
    </Tooltip>
  );
}

interface SidebarNavProps {
  role: Role;
  sections: NavSection[];
  modulesHref: string;
  collapsed: boolean;
  pathname: string;
  upcomingCount: number | null;
}

function SidebarNav({ role, sections, modulesHref, collapsed, pathname, upcomingCount }: SidebarNavProps) {
  function isActive(href: string): boolean {
    if (href === `/${role}`) return pathname === href;
    return pathname === href || pathname.startsWith(href + "/");
  }

  return (
    <nav className="mt-5 flex flex-1 flex-col gap-5 overflow-y-auto overflow-x-hidden">
      {sections.map((section) => (
        <div key={section.title}>
          {!collapsed && (
            <p className="px-2 pb-1.5 text-[11px] font-semibold uppercase tracking-wider text-muted-foreground">
              {section.title}
            </p>
          )}
          <ul className="space-y-0.5">
            {section.items.map((item) => {
              const active = isActive(item.href);
              const Icon = item.icon;
              const showBadge = item.badge === "reservations" && upcomingCount != null && upcomingCount > 0;
              return (
                <li key={item.label}>
                  <NavLabel collapsed={collapsed} label={item.label}>
                    <Link
                      href={item.href}
                      className={cn(
                        "relative flex items-center gap-2.5 rounded-lg py-2 text-sm transition-colors",
                        collapsed ? "justify-center px-0" : "px-2.5",
                        active
                          ? "bg-primary/10 font-semibold text-primary"
                          : "text-muted-foreground hover:bg-accent/60 hover:text-foreground"
                      )}
                    >
                      {active && (
                        <span className="absolute left-0 top-1/2 h-5 w-1 -translate-y-1/2 rounded-r bg-primary" />
                      )}
                      <span className="relative shrink-0">
                        <Icon className="size-4.5" />
                        {collapsed && showBadge && (
                          <span className="absolute -right-0.5 -top-0.5 size-1.5 rounded-full bg-secondary ring-2 ring-background" />
                        )}
                      </span>
                      {!collapsed && <span className="flex-1 truncate">{item.label}</span>}
                      {!collapsed && showBadge && (
                        <span className="rounded-full bg-secondary px-1.5 py-0.5 text-xs font-medium text-secondary-foreground">
                          {upcomingCount}
                        </span>
                      )}
                    </Link>
                  </NavLabel>
                </li>
              );
            })}
            {section.disabled?.map(({ label, icon: Icon }) => (
              <li key={label}>
                <NavLabel collapsed={collapsed} label={label}>
                  <span
                    className={cn(
                      "pointer-events-none flex items-center gap-2.5 rounded-lg py-2 text-sm text-muted-foreground opacity-50",
                      collapsed ? "justify-center px-0" : "px-2.5"
                    )}
                  >
                    <Icon className="size-4.5 shrink-0" />
                    {!collapsed && <span className="flex-1 truncate">{label}</span>}
                  </span>
                </NavLabel>
              </li>
            ))}
          </ul>
        </div>
      ))}

      {/* Accès technique discret aux entités de référence - pas un groupe, pas mis en avant. */}
      <div className="mt-auto pt-1">
        <NavLabel collapsed={collapsed} label="Tous les modules">
          <Link
            href={modulesHref}
            className={cn(
              "relative flex items-center gap-2.5 rounded-lg py-2 text-sm transition-colors",
              collapsed ? "justify-center px-0" : "px-2.5",
              isActive(modulesHref)
                ? "bg-primary/10 font-semibold text-primary"
                : "text-muted-foreground/70 hover:bg-accent/60 hover:text-foreground"
            )}
          >
            {isActive(modulesHref) && (
              <span className="absolute left-0 top-1/2 h-5 w-1 -translate-y-1/2 rounded-r bg-primary" />
            )}
            <LayoutGrid className="size-4 shrink-0" />
            {!collapsed && <span className="flex-1 truncate">Tous les modules</span>}
          </Link>
        </NavLabel>
      </div>
    </nav>
  );
}

interface SidebarFooterProps {
  collapsed: boolean;
  user: CurrentUser | null;
  onLogout: () => void;
}

function SidebarFooter({ collapsed, user, onLogout }: SidebarFooterProps) {
  return (
    <div className="mt-3 space-y-2 border-t border-border pt-3">
      <NavLabel collapsed={collapsed} label={`${displayNameFor(user)}${user?.email ? " · " + user.email : ""}`}>
        <div className={cn("flex items-center gap-2.5 px-1", collapsed && "justify-center px-0")}>
          <span className="flex size-8 shrink-0 items-center justify-center rounded-full bg-secondary text-xs font-semibold text-secondary-foreground">
            {initialsFor(user)}
          </span>
          {!collapsed && (
            <div className="min-w-0 flex-1">
              <p className="truncate text-sm font-medium">{displayNameFor(user)}</p>
              {user?.email && <p className="truncate text-xs text-muted-foreground">{user.email}</p>}
            </div>
          )}
          {!collapsed && (
            <button
              type="button"
              onClick={onLogout}
              aria-label="Déconnexion"
              className="flex size-8 shrink-0 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:bg-accent hover:text-destructive-text"
            >
              <LogOut className="size-4" />
            </button>
          )}
        </div>
      </NavLabel>

      {collapsed && (
        <button
          type="button"
          onClick={onLogout}
          aria-label="Déconnexion"
          className="flex w-full items-center justify-center rounded-lg py-1.5 text-muted-foreground transition-colors hover:bg-accent hover:text-destructive-text"
        >
          <LogOut className="size-4" />
        </button>
      )}

      <div className={cn("flex items-center px-1", collapsed ? "justify-center" : "justify-start")}>
        <ThemeToggle compact={collapsed} />
      </div>
    </div>
  );
}

export function AppShell({ role, sections, modulesHref, children }: AppShellProps) {
  const pathname = usePathname() || "";
  const router = useRouter();
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [upcomingCount, setUpcomingCount] = useState<number | null>(null);
  const [enterpriseName, setEnterpriseName] = useState<string | null>(null);
  const [collapsed, setCollapsed] = useState(false);
  const [mobileOpen, setMobileOpen] = useState(false);

  // Préférence de repli persistée - lue après montage pour éviter tout mismatch d'hydratation
  // (même logique que ThemeToggle : le serveur ne connaît pas le choix stocké côté client).
  useEffect(() => {
    try {
      setCollapsed(localStorage.getItem(COLLAPSE_STORAGE_KEY) === "true");
    } catch {
      // localStorage indisponible (navigation privée stricte...) - reste déplié, sans casser.
    }
  }, []);

  function toggleCollapsed() {
    setCollapsed((prev) => {
      const next = !prev;
      try {
        localStorage.setItem(COLLAPSE_STORAGE_KEY, String(next));
      } catch {
        // non bloquant.
      }
      return next;
    });
  }

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

  // Ferme le menu mobile à chaque navigation.
  useEffect(() => {
    setMobileOpen(false);
  }, [pathname]);

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

  return (
    <div className="flex min-h-screen w-full bg-background text-foreground">
      {/* Sidebar desktop */}
      <aside
        className={cn(
          "sticky top-0 hidden h-screen shrink-0 flex-col border-r border-border bg-background px-3 py-4 transition-[width] duration-200 lg:flex",
          collapsed ? COLLAPSED_WIDTH : EXPANDED_WIDTH
        )}
      >
        <div className={cn("flex items-center", collapsed ? "flex-col gap-2" : "justify-between")}>
          <Link href={`/${role}`} className="flex items-center gap-2.5 px-2">
            <span className="flex size-8 shrink-0 items-center justify-center rounded-lg bg-primary text-primary-foreground">
              <Building2 className="size-4.5" />
            </span>
            {!collapsed && (
              <span className="text-lg font-bold tracking-tight text-primary">DariRentals</span>
            )}
          </Link>
          <Tooltip>
            <TooltipTrigger asChild>
              <button
                type="button"
                onClick={toggleCollapsed}
                aria-label={collapsed ? "Déplier le menu" : "Replier le menu"}
                className="flex size-7 shrink-0 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:bg-accent hover:text-foreground"
              >
                {collapsed ? <ChevronRight className="size-4" /> : <ChevronLeft className="size-4" />}
              </button>
            </TooltipTrigger>
            <TooltipContent side="right">{collapsed ? "Déplier" : "Replier"}</TooltipContent>
          </Tooltip>
        </div>

        {/* Encart société active (visuel) */}
        <NavLabel collapsed={collapsed} label={enterpriseName ?? "Société"}>
          <div
            className={cn(
              "mt-4 flex items-center gap-2.5 rounded-lg border border-border bg-card px-2.5 py-2",
              collapsed && "justify-center px-0"
            )}
          >
            <span className="flex size-8 shrink-0 items-center justify-center rounded-md bg-secondary text-xs font-semibold text-secondary-foreground">
              {enterpriseInitials}
            </span>
            {!collapsed && (
              <>
                <div className="min-w-0 flex-1">
                  <p className="truncate text-sm font-semibold">{enterpriseName ?? "—"}</p>
                  <p className="truncate text-xs text-muted-foreground">Agence principale</p>
                </div>
                <ChevronsUpDown className="size-3.5 shrink-0 text-muted-foreground" />
              </>
            )}
          </div>
        </NavLabel>

        <SidebarNav
          role={role}
          sections={sections}
          modulesHref={modulesHref}
          collapsed={collapsed}
          pathname={pathname}
          upcomingCount={upcomingCount}
        />

        <SidebarFooter collapsed={collapsed} user={user} onLogout={handleLogout} />
      </aside>

      {/* Sidebar mobile (drawer) - en dessous de lg, même seuil que la sidebar desktop. */}
      <Sheet open={mobileOpen} onOpenChange={setMobileOpen}>
        <SheetContent side="left" className="flex flex-col px-3 py-4">
          <SheetTitle className="sr-only">Navigation</SheetTitle>
          <Link href={`/${role}`} className="flex items-center gap-2.5 px-2">
            <span className="flex size-8 shrink-0 items-center justify-center rounded-lg bg-primary text-primary-foreground">
              <Building2 className="size-4.5" />
            </span>
            <span className="text-lg font-bold tracking-tight text-primary">DariRentals</span>
          </Link>

          <div className="mt-4 flex items-center gap-2.5 rounded-lg border border-border bg-card px-2.5 py-2">
            <span className="flex size-8 shrink-0 items-center justify-center rounded-md bg-secondary text-xs font-semibold text-secondary-foreground">
              {enterpriseInitials}
            </span>
            <div className="min-w-0 flex-1">
              <p className="truncate text-sm font-semibold">{enterpriseName ?? "—"}</p>
              <p className="truncate text-xs text-muted-foreground">Agence principale</p>
            </div>
          </div>

          <SidebarNav
            role={role}
            sections={sections}
            modulesHref={modulesHref}
            collapsed={false}
            pathname={pathname}
            upcomingCount={upcomingCount}
          />

          <SidebarFooter collapsed={false} user={user} onLogout={handleLogout} />
        </SheetContent>
      </Sheet>

      {/* Colonne principale */}
      <div className="flex min-w-0 flex-1 flex-col">
        {/* Topbar */}
        <header className="sticky top-0 z-20 flex h-16 items-center justify-between gap-4 border-b border-border bg-card/80 px-4 backdrop-blur supports-backdrop-filter:bg-card/60 sm:px-6">
          <div className="flex items-center gap-2">
            <button
              type="button"
              onClick={() => setMobileOpen(true)}
              aria-label="Ouvrir le menu"
              className="flex size-9 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:bg-accent lg:hidden"
            >
              <Menu className="size-5" />
            </button>
            <div className="flex items-center gap-1.5 text-sm">
              <span className="hidden text-muted-foreground sm:inline">Accueil</span>
              <ChevronRight className="hidden size-3.5 text-muted-foreground sm:inline" />
              <span className="font-medium">{breadcrumbLabel(pathname)}</span>
            </div>
          </div>

          <div className="flex items-center gap-2 sm:gap-3">
            <div className="hidden items-center gap-2 rounded-lg border border-border bg-background px-2.5 py-1.5 text-sm text-muted-foreground sm:flex">
              <Search className="size-3.5" />
              <span>Rechercher</span>
              <kbd className="rounded border border-border bg-muted px-1 text-[10px]">⌘K</kbd>
            </div>
            {/* Masqué sous sm (640px), même seuil que la barre de recherche ci-dessus - à 375px
                la topbar (hamburger + libellé + ce cluster) débordait horizontalement avec le
                sélecteur de devise en plus (voir NOTES-nettoyage.md, point 2). */}
            <CurrencySelector className="hidden w-[76px] sm:flex" />
            <LanguageToggle />
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
