"use client";

import { useEffect, useRef } from "react";

import L from "leaflet";

const markerIcon = L.icon({
  iconUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png",
  iconRetinaUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png",
  shadowUrl: "https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png",
  iconSize: [25, 41],
  iconAnchor: [12, 41],
  popupAnchor: [1, -34],
  shadowSize: [41, 41],
});

const DEFAULT_CENTER: [number, number] = [31.6295, -7.9811];

/** Point coloré (pastille pleine + liseré blanc) - mode lecture seule multi-marqueurs. */
function dotIcon(color: string): L.DivIcon {
  return L.divIcon({
    className: "",
    html: `<span style="display:block;width:14px;height:14px;border-radius:9999px;background:${color};border:2px solid white;box-shadow:0 0 0 1px rgba(0,0,0,.25)"></span>`,
    iconSize: [14, 14],
    iconAnchor: [7, 7],
    popupAnchor: [0, -7],
  });
}

export interface LocationMapMarker {
  id: number;
  latitude: number;
  longitude: number;
  /** Couleur CSS de la pastille (ex: "#22c55e"). */
  color: string;
  /** Contenu du popup au clic - construit via DOM (pas de HTML brut, pas d'injection possible). */
  renderPopup: () => HTMLElement;
}

interface LocationMapProps {
  latitude?: number | null;
  longitude?: number | null;
  onPick?: (lat: number, lng: number) => void;
  height?: number;
  /**
   * Mode lecture seule multi-marqueurs (dashboard) : quand fourni, remplace entièrement le
   * marqueur unique latitude/longitude/onPick (ignorés). Un marqueur par entrée, coloré selon
   * `color`, popup construit à la demande via `renderPopup`. Pas de clic pour placer un point
   * dans ce mode - c'est un composant de consultation, pas de sélection.
   */
  markers?: LocationMapMarker[];
}

export function LocationMap({ latitude, longitude, onPick, height = 260, markers }: LocationMapProps) {
  const containerRef = useRef<HTMLDivElement>(null);
  const mapRef = useRef<L.Map | null>(null);
  const markerRef = useRef<L.Marker | null>(null);
  const multiMarkersRef = useRef<L.Marker[]>([]);
  const isMultiMode = markers != null;

  // Mode multi-marqueurs (lecture seule) - carte + marqueurs colorés, montés une seule fois puis
  // resynchronisés si la liste change (rare : les données du dashboard ne changent pas après le
  // chargement initial, mais on reste correct si jamais).
  useEffect(() => {
    if (!isMultiMode || !containerRef.current || mapRef.current) return;

    const map = L.map(containerRef.current, { scrollWheelZoom: false });
    mapRef.current = map;

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      maxZoom: 19,
    }).addTo(map);

    if (markers && markers.length > 0) {
      const bounds = L.latLngBounds(markers.map((m) => [m.latitude, m.longitude]));
      map.fitBounds(bounds, { padding: [24, 24], maxZoom: 14 });
      if (markers.length === 1) map.setView([markers[0].latitude, markers[0].longitude], 13);
    } else {
      map.setView(DEFAULT_CENTER, 6);
    }

    setTimeout(() => map.invalidateSize(), 200);

    return () => {
      map.remove();
      mapRef.current = null;
      multiMarkersRef.current = [];
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isMultiMode]);

  useEffect(() => {
    const map = mapRef.current;
    if (!isMultiMode || !map || !markers) return;

    multiMarkersRef.current.forEach((m) => m.remove());
    multiMarkersRef.current = markers.map((m) => {
      const marker = L.marker([m.latitude, m.longitude], { icon: dotIcon(m.color) }).addTo(map);
      marker.bindPopup(m.renderPopup());
      return marker;
    });
  }, [isMultiMode, markers]);

  // Mode marqueur unique (sélecteur de position à la création/édition d'une propriété).
  useEffect(() => {
    if (isMultiMode || !containerRef.current || mapRef.current) return;

    const center: [number, number] =
      latitude != null && longitude != null ? [latitude, longitude] : DEFAULT_CENTER;

    const map = L.map(containerRef.current).setView(center, latitude != null ? 14 : 6);
    mapRef.current = map;

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>',
      maxZoom: 19,
    }).addTo(map);
    setTimeout(() => {
      map.invalidateSize();
    }, 200);

    if (latitude != null && longitude != null) {
      markerRef.current = L.marker([latitude, longitude], { icon: markerIcon }).addTo(map);
    }

    if (onPick) {
      map.on("click", (e: L.LeafletMouseEvent) => {
        onPick(e.latlng.lat, e.latlng.lng);
      });
    }

    return () => {
      map.remove();
      mapRef.current = null;
      markerRef.current = null;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [isMultiMode]);

  useEffect(() => {
    if (isMultiMode) return;
    const map = mapRef.current;
    if (!map) return;

    if (latitude == null || longitude == null) {
      if (markerRef.current) {
        markerRef.current.remove();
        markerRef.current = null;
      }
      return;
    }

    if (markerRef.current) {
      markerRef.current.setLatLng([latitude, longitude]);
    } else {
      markerRef.current = L.marker([latitude, longitude], { icon: markerIcon }).addTo(map);
    }
    map.setView([latitude, longitude], map.getZoom() < 10 ? 14 : map.getZoom());
  }, [isMultiMode, latitude, longitude]);

  return (
    <div
      ref={containerRef}
      style={{ height, width: "100%", borderRadius: 8, overflow: "hidden" }}
      className="border"
    />
  );
}