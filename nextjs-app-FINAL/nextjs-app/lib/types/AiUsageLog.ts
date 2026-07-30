// Auto-generated from Angular model: AiUsageLog.model.ts
import { AiUsageTypeDto } from "./AiUsageType";
import { CollaboratorDto } from "./Collaborator";
import { DocumentDto } from "./Document";
import { EnterpriseDto } from "./Enterprise";

export interface AiUsageLogDto {
  id: number | null;
  tokensUsed?: number | null;
  date?: string | null;
  enterprise?: EnterpriseDto | null;
  aiUsageType?: AiUsageTypeDto | null;
  collaborator?: CollaboratorDto | null;
  document?: DocumentDto | null;
}

export function newAiUsageLogDto(): AiUsageLogDto {
  return {
    id: null,
    tokensUsed: null,
    date: null,
    enterprise: null,
    aiUsageType: null,
    collaborator: null,
    document: null,
  };
}
