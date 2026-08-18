// Auto-generated from Angular model: Country.model.ts
import { CityDto } from "./City";

export interface CountryDto {
  id: number | null;
  name: string;
  code: string;
  cities: CityDto[];
}

export function newCountryDto(): CountryDto {
  return {
    id: null,
    name: '',
    code: '',
    cities: [],
  };
}
