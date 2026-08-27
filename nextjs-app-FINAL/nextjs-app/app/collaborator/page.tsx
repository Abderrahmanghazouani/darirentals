"use client";

import Link from "next/link";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { entityRegistry, entityKeys } from "@/lib/entity-registry";
import { useRequireRole } from "@/lib/use-require-role";
import { CollaboratorHeader } from "@/components/collaborator/collaborator-header";

export default function CollaboratorHome() {
  const ready = useRequireRole("collaborator");

  if (!ready) return null;

  return (
    <div className="w-full min-w-0 p-6 max-w-5xl mx-auto space-y-6">
      <CollaboratorHeader />

      <Card>
        <CardHeader>
          <CardTitle>Modules</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-2">
            {entityKeys.map((key) => (
              <Link
                key={key}
                href={`/collaborator/${key}`}
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
