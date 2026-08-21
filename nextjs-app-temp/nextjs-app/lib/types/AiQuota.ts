// Auto-generated from Angular model: AiQuota.model.ts
import { EnterpriseDto } from "./Enterprise";

export interface AiQuotaDto {
  id: number | null;
  tokensAllocated?: number | null;
  enterprise?: EnterpriseDto | null;
}

export function newAiQuotaDto(): AiQuotaDto {
  return {
    id: null,
    tokensAllocated: null,
    enterprise: null,
  };
}
