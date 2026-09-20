"use client";

import {
  ArrowLeftRight,
  Building2,
  CalendarDays,
  ClipboardList,
  FileBarChart,
  Inbox,
  LayoutDashboard,
  Receipt,
  UserCog,
  Users,
  Wallet,
} from "lucide-react";
import { AppShell, type NavSection } from "@/components/app-shell";
import { CurrencyProvider } from "@/lib/currency/currency-context";
import { getEntityClients } from "@/lib/api";

// Groupes + libellés définis dans NOTES-sidebar-premium.md - chaque route vérifiée existante
// avant d'être ajoutée ici (app/admin/**/page.tsx). "Demandes de réservation" ajoutée en plus
// de la liste d'origine : module réel et fonctionnel (confirmations/refus), absent par oubli
// de la nav précédente - signalé, à retirer si non souhaité.
const sections: NavSection[] = [
  {
    titleKey: "sectionOverview",
    items: [{ labelKey: "dashboard", href: "/admin", icon: LayoutDashboard }],
  },
  {
    titleKey: "sectionOperations",
    items: [
      { labelKey: "properties", href: "/admin/property", icon: Building2 },
      { labelKey: "reservations", href: "/admin/reservations", icon: CalendarDays, badge: "reservations" },
      { labelKey: "reservationRequests", href: "/admin/reservation-requests", icon: Inbox },
      { labelKey: "tasks", href: "/admin/tasks", icon: ClipboardList },
    ],
  },
  {
    titleKey: "sectionFinances",
    items: [
      { labelKey: "charges", href: "/admin/charges", icon: Receipt },
      { labelKey: "payments", href: "/admin/payments", icon: Wallet },
      { labelKey: "financialReports", href: "/admin/financial-reports", icon: FileBarChart },
      { labelKey: "exchangeRates", href: "/admin/exchange-rates", icon: ArrowLeftRight },
    ],
  },
  {
    titleKey: "sectionTeam",
    items: [
      { labelKey: "collaborators", href: "/admin/collaborator", icon: UserCog },
      { labelKey: "clients", href: "/admin/client", icon: Users },
    ],
  },
];

// CurrencyProvider posé ici (niveau layout) plutôt que dans admin/page.tsx (comme avant) - pour
// que le CurrencySelector de la topbar (app-shell.tsx, ancêtre commun à toutes les pages admin)
// puisse le consommer. Voir NOTES-nettoyage.md, point 1.
const fetchCurrencies = () => getEntityClients("admin").currency.findAll();
const fetchRates = () => getEntityClients("admin").exchangeRate.findAll();

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return (
    <CurrencyProvider fetchCurrencies={fetchCurrencies} fetchRates={fetchRates}>
      <AppShell role="admin" sections={sections} modulesHref="/admin/modules">
        {children}
      </AppShell>
    </CurrencyProvider>
  );
}
