// Auto-generated from Angular model: ExchangeRate.model.ts
import { CurrencyDto } from "./Currency";

export interface ExchangeRateDto {
  id: number | null;
  rate?: number | null;
  source: string;
  baseCurrency?: CurrencyDto | null;
  targetCurrency?: CurrencyDto | null;
}

export function newExchangeRateDto(): ExchangeRateDto {
  return {
    id: null,
    rate: null,
    source: '',
    baseCurrency: null,
    targetCurrency: null,
  };
}
