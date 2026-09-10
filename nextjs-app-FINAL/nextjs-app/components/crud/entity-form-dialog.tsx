"use client";

import {
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
} from "@/components/ui/sheet";

interface EntityFormDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  title: string;
  children: React.ReactNode;
}

/**
 * Conteneur commun de tous les formulaires de création/édition (voir
 * NOTES-formulaires-premium.md) - panneau latéral (Sheet) plutôt qu'une fenêtre centrée
 * (Dialog), pour garder la liste visible en arrière-plan et laisser plus de place aux
 * formulaires les plus longs (Property, Collaborator) qui s'organisent en wizard à
 * l'intérieur. Nom du composant et signature des props inchangés (open/onOpenChange/title/
 * children) : tous les appelants (7 formulaires + écrans génériques /admin/[entity]) restent
 * identiques, seul l'intérieur change.
 *
 * Largeur : 520px en desktop (dans la fourchette 480-560 demandée, une seule taille pour tous
 * plutôt que deux tailles différentes selon le formulaire - cohérence visuelle). Plein écran
 * sous le breakpoint `sm` (640px) - un panneau de 520px sur un écran de téléphone serait soit
 * tronqué soit minuscule, `w-full` prend alors le dessus.
 *
 * `flex flex-col p-0` : chaque formulaire gère lui-même sa mise en page interne en 3 zones
 * (SheetHeader ici pour le titre, une zone de champs `flex-1 overflow-y-auto`, un SheetFooter
 * sticky pour Annuler/Enregistrer) - p-0 pour ne pas imposer de padding qui entrerait en
 * conflit avec celui de chaque zone.
 */
export function EntityFormDialog({
  open,
  onOpenChange,
  title,
  children,
}: EntityFormDialogProps) {
  return (
    <Sheet open={open} onOpenChange={onOpenChange}>
      <SheetContent side="right" className="w-full sm:max-w-[520px] flex flex-col p-0 gap-0">
        <SheetHeader>
          <SheetTitle>{title}</SheetTitle>
        </SheetHeader>
        {children}
      </SheetContent>
    </Sheet>
  );
}
