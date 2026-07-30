// Auto-generated from Angular model: Reservation.model.ts
import { ClientDto } from "./Client";
import { DocumentDto } from "./Document";
import { PropertyDto } from "./Property";
import { ReservationPlatformDto } from "./ReservationPlatform";
import { ReservationRequestDto } from "./ReservationRequest";
import { ReservationStatusDto } from "./ReservationStatus";
import { TaskDto } from "./Task";

export interface ReservationDto {
  id: number | null;
  reference: string;
  amount?: number | null;
  pricePerNight?: number | null;
  client?: ClientDto | null;
  property?: PropertyDto | null;
  reservationPlatform?: ReservationPlatformDto | null;
  reservationStatus?: ReservationStatusDto | null;
  documents: DocumentDto[];
  tasks: TaskDto[];
  reservationRequests: ReservationRequestDto[];
}

export function newReservationDto(): ReservationDto {
  return {
    id: null,
    reference: '',
    amount: null,
    pricePerNight: null,
    client: null,
    property: null,
    reservationPlatform: null,
    reservationStatus: null,
    documents: [],
    tasks: [],
    reservationRequests: [],
  };
}
