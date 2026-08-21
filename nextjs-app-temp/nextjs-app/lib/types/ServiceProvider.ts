// Auto-generated from Angular model: ServiceProvider.model.ts
import { EnterpriseDto } from "./Enterprise";
import { PaymentDto } from "./Payment";
import { ServiceTypeDto } from "./ServiceType";
import { TaskDto } from "./Task";

export interface ServiceProviderDto {
  id: number | null;
  name: string;
  phone: string;
  isActive?: boolean | null;
  serviceType?: ServiceTypeDto | null;
  enterprise?: EnterpriseDto | null;
  payments: PaymentDto[];
  tasks: TaskDto[];
}

export function newServiceProviderDto(): ServiceProviderDto {
  return {
    id: null,
    name: '',
    phone: '',
    isActive: null,
    serviceType: null,
    enterprise: null,
    payments: [],
    tasks: [],
  };
}
