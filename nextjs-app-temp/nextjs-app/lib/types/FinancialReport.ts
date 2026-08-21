// Auto-generated from Angular model: FinancialReport.model.ts
import { CollaboratorDto } from "./Collaborator";
import { EnterpriseDto } from "./Enterprise";
import { FinancialReportPropertyDto } from "./FinancialReportProperty";
import { FinancialReportScopeDto } from "./FinancialReportScope";
import { FinancialReportTypeDto } from "./FinancialReportType";

export interface FinancialReportDto {
  id: number | null;
  totalRevenue?: number | null;
  totalCharges?: number | null;
  netProfit?: number | null;
  generatedAt?: string | null;
  file: string;
  financialReportType?: FinancialReportTypeDto | null;
  financialReportScope?: FinancialReportScopeDto | null;
  enterprise?: EnterpriseDto | null;
  generatedBy?: CollaboratorDto | null;
  financialReportProperties: FinancialReportPropertyDto[];
}

export function newFinancialReportDto(): FinancialReportDto {
  return {
    id: null,
    totalRevenue: null,
    totalCharges: null,
    netProfit: null,
    generatedAt: null,
    file: '',
    financialReportType: null,
    financialReportScope: null,
    enterprise: null,
    generatedBy: null,
    financialReportProperties: [],
  };
}
