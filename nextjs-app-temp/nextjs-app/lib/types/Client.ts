// Auto-generated from Angular model: Client.model.ts
import { EnterpriseDto } from "./Enterprise";
import { ReservationDto } from "./Reservation";
import { ReservationRequestDto } from "./ReservationRequest";

export interface ClientDto {
  id: number | null;
  fullName: string;
  phone: string;
  nationality: string;
  email: string;
  enabled?: boolean | null;
  credentialsNonExpired?: boolean | null;
  accountNonExpired?: boolean | null;
  username: string;
  passwordChanged?: boolean | null;
  accountNonLocked?: boolean | null;
  password: string;
  enterprise?: EnterpriseDto | null;
  reservations: ReservationDto[];
  reservationRequests: ReservationRequestDto[];
}

export function newClientDto(): ClientDto {
  return {
    id: null,
    fullName: '',
    phone: '',
    nationality: '',
    email: '',
    enabled: null,
    credentialsNonExpired: null,
    accountNonExpired: null,
    username: '',
    passwordChanged: null,
    accountNonLocked: null,
    password: '',
    enterprise: null,
    reservations: [],
    reservationRequests: [],
  };
}
