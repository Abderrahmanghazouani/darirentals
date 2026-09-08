"use client";

import { useMemo } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { LocationMap, type LocationMapMarker } from "@/components/location-map-dynamic";
import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { computePropertyMapPoints, type PropertyMapStatus } from "@/lib/dashboard/property-map";
import { useLanguage } from "@/lib/i18n/language-context";

// var(--x) plutôt qu'une couleur figée : reste correct si l'utilisateur bascule clair/sombre
// pendant que le dashboard est déjà affiché, sans re-calcul JS (le navigateur résout la variable
// CSS à chaque peinture).
const STATUS_COLOR: Record<PropertyMapStatus, string> = {
  active: "var(--success)",
  "ending-soon": "var(--warning)",
  idle: "var(--muted-foreground)",
};

interface PropertyMapCardProps {
  properties: PropertyDto[];
  reservations: ReservationDto[];
  role?: "admin" | "collaborator";
}

export function PropertyMapCard({ properties, reservations, role = "admin" }: PropertyMapCardProps) {
  const { dict } = useLanguage();

  const points = useMemo(
    () => computePropertyMapPoints(properties, reservations),
    [properties, reservations]
  );

  // Pas de fiche de rentabilité par propriété côté collaborateur (aucune route
  // /collaborator/property/[id]/rentabilite - vérifié avant d'écrire ce lien) - on renvoie vers
  // la liste des propriétés à la place, avec un libellé qui ne promet pas la rentabilité.
  const linkHref = (id: number) =>
    role === "admin" ? `/admin/property/${id}/rentabilite` : "/collaborator/property";
  const linkLabel = role === "admin" ? dict.propertyMap.viewProfitability : dict.propertyMap.viewProperty;

  const markers = useMemo<LocationMapMarker[]>(
    () =>
      points.map((p) => ({
        id: p.id,
        latitude: p.latitude,
        longitude: p.longitude,
        color: STATUS_COLOR[p.status],
        renderPopup: () => {
          const wrapper = document.createElement("div");
          wrapper.className = "text-sm space-y-1.5 min-w-[140px]";

          const title = document.createElement("p");
          title.className = "font-semibold";
          title.textContent = p.name; // textContent - jamais de HTML brut, pas d'injection possible.
          wrapper.appendChild(title);

          const link = document.createElement("a");
          link.href = linkHref(p.id);
          link.className = "text-primary hover:underline text-xs font-medium";
          link.textContent = linkLabel;
          wrapper.appendChild(link);

          return wrapper;
        },
      })),
    // eslint-disable-next-line react-hooks/exhaustive-deps
    [points, dict, role]
  );

  return (
    // min-w-0 : item direct de la grille 2 colonnes du dashboard - un item de grille CSS a
    // min-width:auto par défaut (taille basée sur son contenu), pas 0, même en grid-cols-1 sur
    // mobile. Sans ça, la carte Leaflet (comme le graphique recharts avant elle, voir
    // NOTES-nettoyage.md point 2) forçait la page à déborder horizontalement à 375px - trouvé et
    // corrigé pendant les tests mobile de ce chantier, voir NOTES-dashboard-carte-timeline.md.
    <Card className="h-full min-w-0">
      <CardContent className="space-y-3">
        {points.length === 0 ? (
          <div className="flex h-[220px] flex-col items-center justify-center gap-1 rounded-md border border-dashed text-center">
            <p className="text-sm font-medium">{dict.propertyMap.emptyTitle}</p>
            <p className="text-xs text-muted-foreground">{dict.propertyMap.emptySubtitle}</p>
          </div>
        ) : (
          <LocationMap markers={markers} height={220} />
        )}

        <div className="flex flex-wrap items-center gap-x-4 gap-y-1.5 text-xs text-muted-foreground">
          <span className="flex items-center gap-1.5">
            <span className="size-2.5 shrink-0 rounded-full" style={{ background: STATUS_COLOR.active }} />
            {dict.propertyMap.legendActive}
          </span>
          <span className="flex items-center gap-1.5">
            <span
              className="size-2.5 shrink-0 rounded-full"
              style={{ background: STATUS_COLOR["ending-soon"] }}
            />
            {dict.propertyMap.legendEndingSoon}
          </span>
          <span className="flex items-center gap-1.5">
            <span className="size-2.5 shrink-0 rounded-full" style={{ background: STATUS_COLOR.idle }} />
            {dict.propertyMap.legendIdle}
          </span>
        </div>
      </CardContent>
    </Card>
  );
}
