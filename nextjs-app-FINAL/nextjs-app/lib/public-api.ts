const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8036/api/";
const BASE = API_BASE + "open/reservation-request/";

export interface PublicPropertyDto {
  id: number;
  name: string;
  capacity?: number | null;
  pricePerNight?: number | null;
  latitude?: number | null;
  longitude?: number | null;
  propertyType?: { label: string } | null;
  propertyStatus?: { label: string; code: string } | null;
}

export interface PublicRequestInput {
  propertyId: number;
  fullName: string;
  phone: string;
  checkIn: string;
  checkOut: string;
  message: string;
}

export async function fetchPublicProperties(): Promise<PublicPropertyDto[]> {
  const res = await fetch(BASE + "properties", { cache: "no-store" });
  if (!res.ok) throw new Error("Impossible de charger les propriétés");
  const data = await res.json();
  return data ?? [];
}

export async function submitReservationRequest(input: PublicRequestInput): Promise<void> {
  const res = await fetch(BASE, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(input),
  });
  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(text || "Erreur lors de l'envoi de la demande");
  }
}