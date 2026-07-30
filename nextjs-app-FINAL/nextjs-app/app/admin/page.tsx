"use client";

import Link from "next/link";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { entityRegistry, entityKeys } from "@/lib/entity-registry";
import { useRequireRole } from "@/lib/use-require-role";
import { logout } from "@/lib/auth";
import { useRouter } from "next/navigation";

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
