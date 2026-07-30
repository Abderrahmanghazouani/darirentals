// Auto-generated from Angular model: Currency.model.ts
import { CollaboratorDto } from "./Collaborator";
import { EnterpriseDto } from "./Enterprise";
import { ExchangeRateDto } from "./ExchangeRate";

export interface CurrencyDto {
  id: number | null;
  description: string;
  code: string;
  label: string;
  style: string;
  isDefault?: boolean | null;
  sortOrder?: number | null;
  symbol: string;
  exchangeRatesAsBase: ExchangeRateDto[];
  exchangeRatesAsTarget: ExchangeRateDto[];
  enterprises: EnterpriseDto[];
  collaborators: CollaboratorDto[];
}

export function newCurrencyDto(): CurrencyDto {
  return {
    id: null,
    description: '',
    code: '',
    label: '',
    style: '',
    isDefault: null,
    sortOrder: null,
    symbol: '',
    exchangeRatesAsBase: [],
    exchangeRatesAsTarget: [],
    enterprises: [],
    collaborators: [],
  };
}
