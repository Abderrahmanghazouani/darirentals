"use client";

import Link from "next/link";
import { CalendarDays, Building2, Receipt, Wallet } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { entityRegistry, entityKeys } from "@/lib/entity-registry";
import { useRequireRole } from "@/lib/use-require-role";
import { logout } from "@/lib/auth";
import { useRouter } from "next/navigation";

const tools = [
  { href: "/admin/property", label: "Propriétés", icon: Building2 },
  { href: "/admin/reservations", label: "Réservations (calendrier)", icon: CalendarDays },
  { href: "/admin/charges", label: "Charges", icon: Receipt },
  { href: "/admin/payments", label: "Paiements aux prestataires", icon: Wallet },
];

export default function AdminHome() {
  const ready = useRequireRole("admin");
  const router = useRouter();

  if (!ready) return null;

  return (
    <div className="p-6 max-w-5xl mx-auto space-y-6">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-semibold">Espace Admin</h1>
        <Button
          variant="outline"
          onClick={() => {
            logout();
            router.push("/login");
          }}
        >
          Déconnexion
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Outils</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-2">
            {tools.map(({ href, label, icon: Icon }) => (
              <Link
                key={href}
                href={href}
                className="flex items-center gap-2 rounded-md border-2 border-primary/20 bg-primary/5 px-4 py-3 text-sm font-medium hover:bg-primary/10 transition-colors"
              >
                <Icon className="size-4" />
                {label}
              </Link>
            ))}
          </div>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Modules</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-2">
            {entityKeys.map((key) => (
              <Link
                key={key}
                href={`/admin/${key}`}
                className="rounded-md border px-3 py-2 text-sm hover:bg-accent transition-colors"
              >
                {entityRegistry[key].label}
              </Link>
            ))}
          </div>
        </CardContent>
      </Card>
    </div>
  );
}