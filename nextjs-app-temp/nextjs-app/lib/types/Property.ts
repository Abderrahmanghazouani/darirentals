// Auto-generated from Angular model: Property.model.ts
import { ChargeDto } from "./Charge";
import { CityDto } from "./City";
import { EnterpriseDto } from "./Enterprise";
import { FinancialReportPropertyDto } from "./FinancialReportProperty";
import { PropertyStatusDto } from "./PropertyStatus";
import { PropertyTypeDto } from "./PropertyType";
import { ReservationDto } from "./Reservation";
import { ReservationRequestDto } from "./ReservationRequest";
import { TaskDto } from "./Task";

export interface PropertyDto {
  id: number | null;
  name: string;
  streetNumber: string;
  streetName: string;
  postalCode: string;
  capacity?: number | null;
  pricePerNight?: number | null;
  latitude?: number | null;
  longitude?: number | null;
  propertyType?: PropertyTypeDto | null;
  propertyStatus?: PropertyStatusDto | null;
  city?: CityDto | null;
  enterprise?: EnterpriseDto | null;
  reservations: ReservationDto[];
  charges: ChargeDto[];
  tasks: TaskDto[];
  financialReportProperties: FinancialReportPropertyDto[];
  reservationRequests: ReservationRequestDto[];
  alternativeRequests: ReservationRequestDto[];
}

export function newPropertyDto(): PropertyDto {
  return {
    id: null,
    name: '',
    streetNumber: '',
    streetName: '',
    postalCode: '',
    capacity: null,
    pricePerNight: null,
    latitude: null,
    longitude: null,
    propertyType: null,
    propertyStatus: null,
    city: null,
    enterprise: null,
    reservations: [],
    charges: [],
    tasks: [],
    financialReportProperties: [],
    reservationRequests: [],
    alternativeRequests: [],
  };
}
