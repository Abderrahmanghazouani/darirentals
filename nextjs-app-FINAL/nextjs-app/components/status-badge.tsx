import * as React from "react";
import { Badge } from "@/components/ui/badge";
import { cn } from "@/lib/utils";

type BadgeVariant = "success" | "warning" | "destructive" | "info" | "secondary";

export interface StatusLike {
  label?: string | null;
  code?: string | null;
  /** Style libre saisi dans le CRUD (ex "success", "warning", "danger"). */
  style?: string | null;
}

/**
 * Mapping unique des statuts vers une couleur de badge, identique sur toute la plateforme.
 * On s'appuie d'abord sur `style` (mot-clé type Bootstrap), puis sur des heuristiques FR
 * sur le libellé / code. Aucune logique métier ici, juste de l'habillage.
 */
export function statusBadgeVariant(status?: StatusLike | null): BadgeVariant {
  if (!status) return "secondary";
  const probe = `${status.style ?? ""} ${status.code ?? ""} ${status.label ?? ""}`
    .toLowerCase()
    .trim();

  if (
    /(danger|destructive|error|erreur|annul|refus|reject|echou|échou|retard|overdue|expir|impay|impayé)/.test(
      probe
    )
  )
    return "destructive";
  if (
    /(success|paid|payé|paye|confirm|termin|complet|fini|done|valid|actif|active|disponible)/.test(
      probe
    )
  )
    return "success";
  if (
    /(warning|warn|pending|attente|partiel|partial|surveil|brouillon|draft|en cours|progress)/.test(
      probe
    )
  )
    return "warning";
  if (/(info|primary|nouveau|new|reserv)/.test(probe)) return "info";
  return "secondary";
}

interface StatusBadgeProps {
  status?: StatusLike | null;
  /** Texte affiché si `status` est absent. */
  fallback?: React.ReactNode;
  className?: string;
}

export function StatusBadge({ status, fallback = "—", className }: StatusBadgeProps) {
  if (!status?.label) return <>{fallback}</>;
  return (
    <Badge variant={statusBadgeVariant(status)} className={cn(className)}>
      {status.label}
    </Badge>
  );
}
