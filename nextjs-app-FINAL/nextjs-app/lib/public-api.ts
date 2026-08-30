import { CurrencyDto } from "@/lib/types/Currency";
import { ExchangeRateDto } from "@/lib/types/ExchangeRate";

const API_BASE = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8036/api/";
const BASE = API_BASE + "open/reservation-request/";
const CURRENCY_BASE = API_BASE + "open/currency/";

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

export async function fetchPublicCurrencies(): Promise<CurrencyDto[]> {
  const res = await fetch(CURRENCY_BASE + "currencies", { cache: "no-store" });
  if (!res.ok) throw new Error("Impossible de charger les devises");
  const data = await res.json();
  return data ?? [];
}

export async function fetchPublicExchangeRates(): Promise<ExchangeRateDto[]> {
  const res = await fetch(CURRENCY_BASE + "exchange-rates", { cache: "no-store" });
  if (!res.ok) throw new Error("Impossible de charger les taux de change");
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
    // Pas de message par défaut codé en dur ici (voir LoginError dans lib/auth.ts pour la même
    // idée) : un texte français fixe ne suivrait pas la langue choisie par l'utilisateur.
    // Message vide = à l'appelant (/reserver) d'afficher son propre texte traduit.
    const text = await res.text().catch(() => "");
    throw new Error(text);
  }
}