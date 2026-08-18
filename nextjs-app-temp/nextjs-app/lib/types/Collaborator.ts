// Auto-generated from Angular model: Collaborator.model.ts
import { AiUsageLogDto } from "./AiUsageLog";
import { CurrencyDto } from "./Currency";
import { EnterpriseMembershipDto } from "./EnterpriseMembership";
import { ReservationRequestDto } from "./ReservationRequest";
import { TaskDto } from "./Task";

export interface CollaboratorDto {
  id: number | null;
  name: string;
  phone: string;
  isActive?: boolean | null;
  email: string;
  enabled?: boolean | null;
  credentialsNonExpired?: boolean | null;
  accountNonExpired?: boolean | null;
  username: string;
  passwordChanged?: boolean | null;
  accountNonLocked?: boolean | null;
  password: string;
  displayCurrency?: CurrencyDto | null;
  enterpriseMemberships: EnterpriseMembershipDto[];
  aiUsageLogs: AiUsageLogDto[];
  tasks: TaskDto[];
  reservationRequests: ReservationRequestDto[];
}

export function newCollaboratorDto(): CollaboratorDto {
  return {
    id: null,
    name: '',
    phone: '',
    isActive: null,
    email: '',
    enabled: null,
    credentialsNonExpired: null,
    accountNonExpired: null,
    username: '',
    passwordChanged: null,
    accountNonLocked: null,
    password: '',
    displayCurrency: null,
    enterpriseMemberships: [],
    aiUsageLogs: [],
    tasks: [],
    reservationRequests: [],
  };
}
