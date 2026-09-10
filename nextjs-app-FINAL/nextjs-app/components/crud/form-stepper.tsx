"use client";

import { Check } from "lucide-react";
import { cn } from "@/lib/utils";

export interface FormStep {
  label: string;
}

interface FormStepperProps {
  steps: FormStep[];
  /** Index (0-based) de l'étape actuelle. */
  current: number;
}

/**
 * Indicateur de progression des formulaires en wizard (Property, Collaborator - voir
 * NOTES-formulaires-premium.md) : pastilles numérotées reliées par un trait, l'étape courante
 * en `--primary`, les étapes déjà validées avec une coche. Purement visuel - la navigation
 * (Précédent/Suivant, validation par étape) reste gérée par chaque formulaire, ce composant ne
 * fait qu'afficher `current`.
 */
export function FormStepper({ steps, current }: FormStepperProps) {
  return (
    <div className="flex items-start" role="list" aria-label="Étapes du formulaire">
      {steps.map((step, index) => {
        const isDone = index < current;
        const isActive = index === current;
        return (
          <div key={step.label} className={cn("flex items-start", index < steps.length - 1 && "flex-1")}>
            <div className="flex flex-col items-center gap-1.5" role="listitem">
              <div
                className={cn(
                  "flex size-7 shrink-0 items-center justify-center rounded-full text-xs font-semibold transition-colors",
                  isDone && "bg-primary text-primary-foreground",
                  isActive && "bg-primary/15 text-primary ring-2 ring-primary",
                  !isDone && !isActive && "bg-muted text-muted-foreground"
                )}
                aria-current={isActive ? "step" : undefined}
              >
                {isDone ? <Check className="size-3.5" /> : index + 1}
              </div>
              <span
                className={cn(
                  "text-[11px] font-medium whitespace-nowrap",
                  isActive ? "text-foreground" : "text-muted-foreground"
                )}
              >
                {step.label}
              </span>
            </div>
            {index < steps.length - 1 && (
              <div
                className={cn(
                  "mx-2 mt-[13px] h-0.5 flex-1 rounded-full transition-colors",
                  isDone ? "bg-primary" : "bg-muted"
                )}
              />
            )}
          </div>
        );
      })}
    </div>
  );
}
