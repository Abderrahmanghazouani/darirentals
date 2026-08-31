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

// Groupes + libellés définis dans NOTES-sidebar-premium.md - chaque route vérifiée existante
// avant d'être ajoutée ici (app/admin/**/page.tsx). "Demandes de réservation" ajoutée en plus
// de la liste d'origine : module réel et fonctionnel (confirmations/refus), absent par oubli
// de la nav précédente - signalé, à retirer si non souhaité.
const sections: NavSection[] = [
  {
    title: "Vue d'ensemble",
    items: [{ label: "Tableau de bord", href: "/admin", icon: LayoutDashboard }],
  },
  {
    title: "Opérations",
    items: [
      { label: "Propriétés", href: "/admin/property", icon: Building2 },
      { label: "Réservations", href: "/admin/reservations", icon: CalendarDays, badge: "reservations" },
      { label: "Demandes de réservation", href: "/admin/reservation-requests", icon: Inbox },
      { label: "Tâches", href: "/admin/tasks", icon: ClipboardList },
    ],
  },
  {
    title: "Finances",
    items: [
      { label: "Charges", href: "/admin/charges", icon: Receipt },
      { label: "Paiements", href: "/admin/payments", icon: Wallet },
      { label: "Rapports financiers", href: "/admin/financial-reports", icon: FileBarChart },
      { label: "Taux de change", href: "/admin/exchange-rates", icon: ArrowLeftRight },
    ],
  },
  {
    title: "Équipe",
    items: [
      { label: "Collaborateurs", href: "/admin/collaborator", icon: UserCog },
      { label: "Clients", href: "/admin/client", icon: Users },
    ],
  },
];

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return (
    <AppShell role="admin" sections={sections} modulesHref="/admin/modules">
      {children}
    </AppShell>
  );
}
