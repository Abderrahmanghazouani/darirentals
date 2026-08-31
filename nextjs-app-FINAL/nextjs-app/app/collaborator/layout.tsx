"use client";

import {
  Building2,
  CalendarDays,
  ClipboardList,
  LayoutDashboard,
  Receipt,
  Wallet,
} from "lucide-react";
import { AppShell, type NavSection } from "@/components/app-shell";

// Mêmes groupes que côté admin (NOTES-sidebar-premium.md), réduits aux routes qui existent
// réellement pour ce rôle - pas de "Rapports financiers"/"Taux de change"/"Équipe" : ces
// modules n'ont pas d'équivalent sous /collaborator/** (vérifié, aucune page correspondante).
const sections: NavSection[] = [
  {
    title: "Vue d'ensemble",
    items: [{ label: "Tableau de bord", href: "/collaborator", icon: LayoutDashboard }],
  },
  {
    title: "Opérations",
    items: [
      { label: "Propriétés", href: "/collaborator/property", icon: Building2 },
      {
        label: "Réservations",
        href: "/collaborator/reservations",
        icon: CalendarDays,
        badge: "reservations",
      },
      { label: "Tâches", href: "/collaborator/tasks", icon: ClipboardList },
    ],
  },
  {
    title: "Finances",
    items: [
      { label: "Charges", href: "/collaborator/charges", icon: Receipt },
      { label: "Paiements", href: "/collaborator/payments", icon: Wallet },
    ],
  },
];

export default function CollaboratorLayout({ children }: { children: React.ReactNode }) {
  return (
    <AppShell role="collaborator" sections={sections} modulesHref="/collaborator/modules">
      {children}
    </AppShell>
  );
}
