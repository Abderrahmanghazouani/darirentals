// Auto-generated from Angular model: Document.model.ts
import { ChargeDto } from "./Charge";
import { DocumentTypeDto } from "./DocumentType";
import { ReservationDto } from "./Reservation";

export interface DocumentDto {
  id: number | null;
  fileName: string;
  file: string;
  extractedVendor: string;
  extractedAmount?: number | null;
  extractedDate?: string | null;
  documentType?: DocumentTypeDto | null;
  reservation?: ReservationDto | null;
  charge?: ChargeDto | null;
}

export function newDocumentDto(): DocumentDto {
  return {
    id: null,
    fileName: '',
    file: '',
    extractedVendor: '',
    extractedAmount: null,
    extractedDate: null,
    documentType: null,
    reservation: null,
    charge: null,
  };
}
