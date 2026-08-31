"use client";

import Link from "next/link";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { entityRegistry, entityKeys } from "@/lib/entity-registry";
import { useRequireRole } from "@/lib/use-require-role";

/**
 * Accès technique aux entités CRUD génériques, volontairement absent de la navigation
 * principale (voir NOTES-sidebar-premium.md) - un seul lien discret "Tous les modules" tout en
 * bas de la sidebar y mène. Remplace le bloc <details> équivalent qui existait en bas du
 * tableau de bord collaborateur (toujours présent, redondant mais inoffensif).
 */
export default function CollaboratorModulesPage() {
  const ready = useRequireRole("collaborator");
  if (!ready) return null;

  return (
    <Card>
      <CardHeader>
        <CardTitle>Tous les modules</CardTitle>
        <p className="text-sm text-muted-foreground">
          Accès technique à l&apos;ensemble des entités du modèle de données. Les modules
          courants (propriétés, réservations, charges...) ont leur propre écran dédié dans le
          menu à gauche - cette liste couvre les entités de référence restantes.
        </p>
      </CardHeader>
      <CardContent>
        <div className="grid grid-cols-2 gap-2 sm:grid-cols-3 md:grid-cols-4">
          {entityKeys.map((key) => (
            <Link
              key={key}
              href={`/collaborator/${key}`}
              className="rounded-lg border border-border px-3 py-2 text-sm transition-colors hover:bg-accent"
            >
              {entityRegistry[key].label}
            </Link>
          ))}
        </div>
      </CardContent>
    </Card>
  );
}
