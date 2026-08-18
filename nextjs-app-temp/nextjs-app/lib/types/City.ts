// Auto-generated from Angular model: City.model.ts
import { CountryDto } from "./Country";
import { PropertyDto } from "./Property";

export interface CityDto {
  id: number | null;
  name: string;
  country?: CountryDto | null;
  properties: PropertyDto[];
}

export function newCityDto(): CityDto {
  return {
    id: null,
    name: '',
    country: null,
    properties: [],
  };
}
