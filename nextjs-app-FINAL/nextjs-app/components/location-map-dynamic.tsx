"use client";

import dynamic from "next/dynamic";

export type { LocationMapMarker } from "./location-map";

export const LocationMap = dynamic(
  () => import("./location-map").then((m) => m.LocationMap),
  {
    ssr: false,
    loading: () => (
      <div className="h-[260px] w-full rounded-md border flex items-center justify-center text-sm text-muted-foreground">
        Chargement de la carte...
      </div>
    ),
  }
);