// Auto-generated from Angular model: ServiceType.model.ts
export interface ServiceTypeDto {
  id: number | null;
  description: string;
  code: string;
  label: string;
  style: string;
  isDefault?: boolean | null;
  sortOrder?: number | null;
}

export function newServiceTypeDto(): ServiceTypeDto {
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
