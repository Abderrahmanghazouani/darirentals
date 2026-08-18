// Auto-generated from Angular model: ReservationRequestStatus.model.ts
export interface ReservationRequestStatusDto {
  id: number | null;
  description: string;
  code: string;
  label: string;
  style: string;
  isDefault?: boolean | null;
  sortOrder?: number | null;
}

export function newReservationRequestStatusDto(): ReservationRequestStatusDto {
  return {
    id: null,
    description: '',
    code: '',
    label: '',
    style: '',
    isDefault: null,
    sortOrder: null,
  };
}
