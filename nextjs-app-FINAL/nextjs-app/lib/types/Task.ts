// Auto-generated from Angular model: Task.model.ts
import { CollaboratorDto } from "./Collaborator";
import { PropertyDto } from "./Property";
import { ReservationDto } from "./Reservation";
import { ServiceProviderDto } from "./ServiceProvider";
import { TaskPriorityDto } from "./TaskPriority";
import { TaskStatusDto } from "./TaskStatus";
import { TaskTypeDto } from "./TaskType";

export interface TaskDto {
  id: number | null;
  title: string;
  description: string;
  property?: PropertyDto | null;
  reservation?: ReservationDto | null;
  serviceProvider?: ServiceProviderDto | null;
  assignedTo?: CollaboratorDto | null;
  taskType?: TaskTypeDto | null;
  taskPriority?: TaskPriorityDto | null;
  taskStatus?: TaskStatusDto | null;
}

export function newTaskDto(): TaskDto {
  return {
    id: null,
    title: '',
    description: '',
    property: null,
    reservation: null,
    serviceProvider: null,
    assignedTo: null,
    taskType: null,
    taskPriority: null,
    taskStatus: null,
  };
}
