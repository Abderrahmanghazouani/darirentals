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

const sections: NavSection[] = [
  {
    title: "Gestion",
    items: [
      { label: "Vue d'ensemble", href: "/collaborator", icon: LayoutDashboard },
      {
        label: "Réservations",
        href: "/collaborator/reservations",
        icon: CalendarDays,
        badge: "reservations",
      },
      { label: "Biens & logements", href: "/collaborator/property", icon: Building2 },
      { label: "Charges", href: "/collaborator/charges", icon: Receipt },
      { label: "Paiements", href: "/collaborator/payments", icon: Wallet },
      { label: "Tâches", href: "/collaborator/tasks", icon: ClipboardList },
    ],
  },
];

export default function CollaboratorLayout({ children }: { children: React.ReactNode }) {
  return (
    <AppShell role="collaborator" sections={sections}>
      {children}
    </AppShell>
  );
}
