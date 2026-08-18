// Auto-generated from Angular model: TaskStatus.model.ts
export interface TaskStatusDto {
  id: number | null;
  description: string;
  code: string;
  label: string;
  style: string;
  isDefault?: boolean | null;
  sortOrder?: number | null;
}

export function newTaskStatusDto(): TaskStatusDto {
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
