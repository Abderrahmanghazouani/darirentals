// Auto-generated from Angular model: DocumentType.model.ts
export interface DocumentTypeDto {
  id: number | null;
  description: string;
  code: string;
  label: string;
  style: string;
  isDefault?: boolean | null;
  sortOrder?: number | null;
}

export function newDocumentTypeDto(): DocumentTypeDto {
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
