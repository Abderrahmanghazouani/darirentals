// Auto-generated from Angular model: Payment.model.ts
import { ChargeDto } from "./Charge";
import { PaymentStatusDto } from "./PaymentStatus";
import { PaymentTypeDto } from "./PaymentType";
import { ServiceProviderDto } from "./ServiceProvider";

export interface PaymentDto {
  id: number | null;
  amount?: number | null;
  notes: string;
  serviceProvider?: ServiceProviderDto | null;
  paymentType?: PaymentTypeDto | null;
  paymentStatus?: PaymentStatusDto | null;
  charges: ChargeDto[];
}

export function newPaymentDto(): PaymentDto {
  return {
    id: null,
    amount: null,
    notes: '',
    serviceProvider: null,
    paymentType: null,
    paymentStatus: null,
    charges: [],
  };
}
