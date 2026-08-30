"use client";

import {
  Building2,
  CalendarDays,
  FileText,
  LayoutDashboard,
  Receipt,
  Settings,
  Users,
} from "lucide-react";
import { AppShell, type NavSection } from "@/components/app-shell";

const sections: NavSection[] = [
  {
    title: "Gestion",
    items: [
      { label: "Vue d'ensemble", href: "/admin", icon: LayoutDashboard },
      { label: "Réservations", href: "/admin/reservations", icon: CalendarDays, badge: "reservations" },
      { label: "Biens & logements", href: "/admin/property", icon: Building2 },
      { label: "Clients", href: "/admin/client", icon: Users },
      { label: "Charges", href: "/admin/charges", icon: Receipt },
    ],
  },
  {
    title: "Rapports",
    items: [],
    disabled: [
      { label: "Rapports", icon: FileText },
      { label: "Paramètres", icon: Settings },
    ],
  },
];

export default function AdminLayout({ children }: { children: React.ReactNode }) {
  return (
    <AppShell role="admin" sections={sections}>
      {children}
    </AppShell>
  );
}
