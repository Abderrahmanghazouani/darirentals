// Auto-generated from Angular model: Charge.model.ts
import { ChargeTypeDto } from "./ChargeType";
import { DocumentDto } from "./Document";
import { PaymentDto } from "./Payment";
import { PropertyDto } from "./Property";

export interface ChargeDto {
  id: number | null;
  label: string;
  amount?: number | null;
  property?: PropertyDto | null;
  chargeType?: ChargeTypeDto | null;
  payment?: PaymentDto | null;
  documents: DocumentDto[];
}

export function newChargeDto(): ChargeDto {
  return {
    id: null,
    label: '',
    amount: null,
    property: null,
    chargeType: null,
    payment: null,
    documents: [],
  };
}
