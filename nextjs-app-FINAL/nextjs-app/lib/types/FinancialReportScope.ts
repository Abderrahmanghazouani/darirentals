// Auto-generated from Angular model: FinancialReportScope.model.ts
export interface FinancialReportScopeDto {
  id: number | null;
  description: string;
  code: string;
  label: string;
  style: string;
  isDefault?: boolean | null;
  sortOrder?: number | null;
}

export function newFinancialReportScopeDto(): FinancialReportScopeDto {
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
