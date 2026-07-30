// Auto-generated from Angular model: ReservationRequest.model.ts
import { ClientDto } from "./Client";
import { CollaboratorDto } from "./Collaborator";
import { PropertyDto } from "./Property";
import { ReservationDto } from "./Reservation";
import { ReservationRequestStatusDto } from "./ReservationRequestStatus";

export interface ReservationRequestDto {
  id: number | null;
  clientNote: string;
  staffNote: string;
  client?: ClientDto | null;
  requestedProperty?: PropertyDto | null;
  alternativeProperty?: PropertyDto | null;
  reviewedBy?: CollaboratorDto | null;
  reservationRequestStatus?: ReservationRequestStatusDto | null;
  reservation?: ReservationDto | null;
}

export function newReservationRequestDto(): ReservationRequestDto {
  return {
    id: null,
    clientNote: '',
    staffNote: '',
    client: null,
    requestedProperty: null,
    alternativeProperty: null,
    reviewedBy: null,
    reservationRequestStatus: null,
    reservation: null,
  };
}
