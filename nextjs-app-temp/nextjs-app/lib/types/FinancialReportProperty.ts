// Auto-generated from Angular model: FinancialReportProperty.model.ts
import { FinancialReportDto } from "./FinancialReport";
import { PropertyDto } from "./Property";

export interface FinancialReportPropertyDto {
  id: number | null;
  financialReport?: FinancialReportDto | null;
  property?: PropertyDto | null;
}

export function newFinancialReportPropertyDto(): FinancialReportPropertyDto {
  return {
    id: null,
    financialReport: null,
    property: null,
  };
}
