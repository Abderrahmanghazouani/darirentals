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
import { CurrencyProvider } from "@/lib/currency/currency-context";
import { getEntityClients } from "@/lib/api";

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

// CurrencyProvider posé ici (niveau layout) plutôt que dans collaborator/page.tsx (comme avant)
// - pour que le CurrencySelector de la topbar (app-shell.tsx, ancêtre commun à toutes les pages
// collaborateur) puisse le consommer. Voir NOTES-nettoyage.md, point 1.
const fetchCurrencies = () => getEntityClients("collaborator").currency.findAll();
const fetchRates = () => getEntityClients("collaborator").exchangeRate.findAll();

export default function CollaboratorLayout({ children }: { children: React.ReactNode }) {
  return (
    <CurrencyProvider fetchCurrencies={fetchCurrencies} fetchRates={fetchRates}>
      <AppShell role="collaborator" sections={sections} modulesHref="/collaborator/modules">
        {children}
      </AppShell>
    </CurrencyProvider>
  );
}
