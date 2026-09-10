import * as React from "react";

/**
 * Regroupement visuel de champs dans un formulaire (voir NOTES-formulaires-premium.md) : un
 * petit titre en capitales discrètes + un espacement cohérent. Utilisé par les formulaires
 * courts (Client, Charge, Payment, Task, Reservation) restés à une seule étape, pour les
 * structurer sans passer par un wizard.
 */
export function FormSection({
  title,
  children,
}: {
  title: string;
  children: React.ReactNode;
}) {
  return (
    <section className="space-y-4">
      <h4 className="text-xs font-semibold uppercase tracking-wide text-muted-foreground">
        {title}
      </h4>
      {children}
    </section>
  );
}
