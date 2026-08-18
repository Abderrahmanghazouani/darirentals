// Auto-generated from Angular model: Enterprise.model.ts
import { AiQuotaDto } from "./AiQuota";
import { AiUsageLogDto } from "./AiUsageLog";
import { ClientDto } from "./Client";
import { CurrencyDto } from "./Currency";
import { EnterpriseMembershipDto } from "./EnterpriseMembership";
import { FinancialReportDto } from "./FinancialReport";
import { PropertyDto } from "./Property";
import { ServiceProviderDto } from "./ServiceProvider";

export interface EnterpriseDto {
  id: number | null;
  name: string;
  phone: string;
  address: string;
  currency?: CurrencyDto | null;
  properties: PropertyDto[];
  clients: ClientDto[];
  serviceProviders: ServiceProviderDto[];
  enterpriseMemberships: EnterpriseMembershipDto[];
  aiQuotas: AiQuotaDto[];
  aiUsageLogs: AiUsageLogDto[];
  financialReports: FinancialReportDto[];
}

export function newEnterpriseDto(): EnterpriseDto {
  return {
    id: null,
    name: '',
    phone: '',
    address: '',
    currency: null,
    properties: [],
    clients: [],
    serviceProviders: [],
    enterpriseMemberships: [],
    aiQuotas: [],
    aiUsageLogs: [],
    financialReports: [],
  };
}
