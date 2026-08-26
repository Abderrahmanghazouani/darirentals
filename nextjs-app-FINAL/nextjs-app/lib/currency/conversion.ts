import { ExchangeRateDto } from "@/lib/types/ExchangeRate";
import { CurrencyDto } from "@/lib/types/Currency";

/**
 * Toutes les valeurs monétaires de l'application (Property.pricePerNight, Reservation.amount,
 * Charge.amount, Payment.amount...) sont stockées en base en MAD, sans conversion. La
 * conversion est purement visuelle côté frontend — voir NOTES-devises.md.
 */
export const BASE_CURRENCY_CODE = "MAD";

/** Convertit un montant exprimé en devise de base (MAD) vers `targetCode`. */
export function convertFromBase(
  amountInBase: number,
  targetCode: string,
  rates: ExchangeRateDto[]
): number {
  if (!Number.isFinite(amountInBase) || targetCode === BASE_CURRENCY_CODE) {
    return amountInBase;
  }
  const rate = rates.find(
    (r) => r.baseCurrency?.code === BASE_CURRENCY_CODE && r.targetCurrency?.code === targetCode
  );
  if (!rate?.rate) {
    // Pas de taux connu pour cette devise : on affiche la valeur brute plutôt que de planter.
    return amountInBase;
  }
  return amountInBase * rate.rate;
}

export function formatMoney(amount: number, currencyCode: string, symbol?: string | null): string {
  const formatted = amount.toLocaleString("fr-FR", {
    minimumFractionDigits: 2,
    maximumFractionDigits: 2,
  });
  return `${formatted} ${symbol || currencyCode}`;
}

/** Devises disponibles pour le sélecteur : MAD (base, toujours dispo) + celles avec un taux connu. */
export function availableCurrencyCodes(currencies: CurrencyDto[], rates: ExchangeRateDto[]): string[] {
  const withRate = new Set(
    rates
      .filter((r) => r.baseCurrency?.code === BASE_CURRENCY_CODE)
      .map((r) => r.targetCurrency?.code)
      .filter((code): code is string => Boolean(code))
  );
  return currencies
    .map((c) => c.code)
    .filter((code): code is string => Boolean(code))
    .filter((code) => code === BASE_CURRENCY_CODE || withRate.has(code));
}
