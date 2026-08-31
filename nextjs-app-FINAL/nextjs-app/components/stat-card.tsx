import * as React from "react";
import { ArrowUpRight, ArrowDownRight, type LucideIcon } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { cn } from "@/lib/utils";

type IconTone = "primary" | "success" | "warning" | "info" | "destructive";

const iconToneClass: Record<IconTone, string> = {
  primary: "bg-secondary text-primary",
  success: "bg-success/15 text-success",
  warning: "bg-warning/15 text-warning",
  info: "bg-info/15 text-info",
  destructive: "bg-destructive/15 text-destructive-text",
};

export interface StatCardChange {
  /** Texte de variation déjà formaté par l'appelant - jamais recalculé ici. */
  label: string;
  positive: boolean;
  /** Complément gris, ex "vs. mois dernier". */
  note?: string;
}

interface StatCardProps {
  label: string;
  /** Valeur déjà formatée. Passer "—" quand elle n'est pas calculable. */
  value: React.ReactNode;
  icon: LucideIcon;
  iconTone?: IconTone;
  /** Couleur de la valeur (rentabilité : vert si positif, rouge si négatif). */
  valueTone?: "default" | "success" | "destructive";
  /** Variation. Omise -> rien ne s'affiche sous la valeur. */
  change?: StatCardChange;
  hint?: string;
  className?: string;
}

export function StatCard({
  label,
  value,
  icon: Icon,
  iconTone = "primary",
  valueTone = "default",
  change,
  hint,
  className,
}: StatCardProps) {
  return (
    <Card className={cn("gap-0 py-0", className)}>
      <CardContent className="p-5">
        <div className="flex items-start justify-between gap-3">
          <p className="text-sm text-muted-foreground">{label}</p>
          <span
            className={cn(
              "flex size-9 shrink-0 items-center justify-center rounded-lg",
              iconToneClass[iconTone]
            )}
          >
            <Icon className="size-4.5" />
          </span>
        </div>
        <p
          className={cn(
            "stat-card-value mt-2 text-3xl",
            valueTone === "success" && "text-success",
            valueTone === "destructive" && "text-destructive-text"
          )}
        >
          {value}
        </p>
        {change ? (
          <p className="mt-2 flex items-center gap-1 text-xs">
            <span
              className={cn(
                "flex items-center gap-0.5 font-medium",
                change.positive ? "text-success" : "text-destructive-text"
              )}
            >
              {change.positive ? (
                <ArrowUpRight className="size-3.5" />
              ) : (
                <ArrowDownRight className="size-3.5" />
              )}
              {change.label}
            </span>
            {change.note && <span className="text-muted-foreground">{change.note}</span>}
          </p>
        ) : hint ? (
          <p className="mt-2 text-xs text-muted-foreground">{hint}</p>
        ) : null}
      </CardContent>
    </Card>
  );
}
