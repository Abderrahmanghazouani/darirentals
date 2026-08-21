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

interface LocationMapProps {
  latitude: number | null;
  longitude: number | null;
  onPick?: (lat: number, lng: number) => void;
  height?: number;
}

export function LocationMap({ latitude, longitude, onPick, height = 260 }: LocationMapProps) {
  const containerRef = useRef<HTMLDivElement>(null);
  const mapRef = useRef<L.Map | null>(null);
  const markerRef = useRef<L.Marker | null>(null);

  useEffect(() => {
    if (!containerRef.current || mapRef.current) return;

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
  }, []);

  useEffect(() => {
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
  }, [latitude, longitude]);

  return (
    <div
      ref={containerRef}
      style={{ height, width: "100%", borderRadius: 8, overflow: "hidden" }}
      className="border"
    />
  );
}