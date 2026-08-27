// Auto-généré : un client API par entité, basé sur createEntityClient
import { createEntityClient, Role } from "../api-client";
import type { AiQuotaDto } from "../types/AiQuota";
import type { AiUsageLogDto } from "../types/AiUsageLog";
import type { AiUsageTypeDto } from "../types/AiUsageType";
import type { ChargeDto } from "../types/Charge";
import type { ChargeTypeDto } from "../types/ChargeType";
import type { ServiceProviderDto } from "../types/ServiceProvider";
import type { ServiceTypeDto } from "../types/ServiceType";
import type { EnterpriseDto } from "../types/Enterprise";
import type { EnterpriseMembershipDto } from "../types/EnterpriseMembership";
import type { CollaboratorDto } from "../types/Collaborator";
import type { CollaboratorPermissionOverrideDto } from "../types/CollaboratorPermissionOverride";
import type { CollaboratorPropertyAccessDto } from "../types/CollaboratorPropertyAccess";
import type { CollaboratorRoleDto } from "../types/CollaboratorRole";
import type { TaskDto } from "../types/Task";
import type { TaskPriorityDto } from "../types/TaskPriority";
import type { TaskStatusDto } from "../types/TaskStatus";
import type { TaskTypeDto } from "../types/TaskType";
import type { DocumentDto } from "../types/Document";
import type { DocumentTypeDto } from "../types/DocumentType";
import type { CurrencyDto } from "../types/Currency";
import type { ExchangeRateDto } from "../types/ExchangeRate";
import type { FinancialReportDto } from "../types/FinancialReport";
import type { FinancialReportPropertyDto } from "../types/FinancialReportProperty";
import type { FinancialReportScopeDto } from "../types/FinancialReportScope";
import type { FinancialReportTypeDto } from "../types/FinancialReportType";
import type { PaymentDto } from "../types/Payment";
import type { PaymentStatusDto } from "../types/PaymentStatus";
import type { PaymentTypeDto } from "../types/PaymentType";
import type { CityDto } from "../types/City";
import type { CountryDto } from "../types/Country";
import type { PropertyDto } from "../types/Property";
import type { PropertyStatusDto } from "../types/PropertyStatus";
import type { PropertyTypeDto } from "../types/PropertyType";
import type { ClientDto } from "../types/Client";
import type { ReservationDto } from "../types/Reservation";
import type { ReservationPlatformDto } from "../types/ReservationPlatform";
import type { ReservationRequestDto } from "../types/ReservationRequest";
import type { ReservationRequestStatusDto } from "../types/ReservationRequestStatus";
import type { ReservationStatusDto } from "../types/ReservationStatus";

export function getEntityClients(role: Role) {
  return {
    aiQuota: createEntityClient<AiQuotaDto>("aiQuota", role),
    aiUsageLog: createEntityClient<AiUsageLogDto>("aiUsageLog", role),
    aiUsageType: createEntityClient<AiUsageTypeDto>("aiUsageType", role),
    charge: createEntityClient<ChargeDto>("charge", role),
    chargeType: createEntityClient<ChargeTypeDto>("chargeType", role),
    serviceProvider: createEntityClient<ServiceProviderDto>("serviceProvider", role),
    serviceType: createEntityClient<ServiceTypeDto>("serviceType", role),
    enterprise: createEntityClient<EnterpriseDto>("enterprise", role),
    enterpriseMembership: createEntityClient<EnterpriseMembershipDto>("enterpriseMembership", role),
    collaborator: createEntityClient<CollaboratorDto>("collaborator", role),
    collaboratorPermissionOverride: createEntityClient<CollaboratorPermissionOverrideDto>("collaboratorPermissionOverride", role),
    collaboratorPropertyAccess: createEntityClient<CollaboratorPropertyAccessDto>("collaboratorPropertyAccess", role),
    collaboratorRole: createEntityClient<CollaboratorRoleDto>("collaboratorRole", role),
    task: createEntityClient<TaskDto>("task", role),
    taskPriority: createEntityClient<TaskPriorityDto>("taskPriority", role),
    taskStatus: createEntityClient<TaskStatusDto>("taskStatus", role),
    taskType: createEntityClient<TaskTypeDto>("taskType", role),
    document: createEntityClient<DocumentDto>("document", role),
    documentType: createEntityClient<DocumentTypeDto>("documentType", role),
    currency: createEntityClient<CurrencyDto>("currency", role),
    exchangeRate: createEntityClient<ExchangeRateDto>("exchangeRate", role),
    financialReport: createEntityClient<FinancialReportDto>("financialReport", role),
    financialReportProperty: createEntityClient<FinancialReportPropertyDto>("financialReportProperty", role),
    financialReportScope: createEntityClient<FinancialReportScopeDto>("financialReportScope", role),
    financialReportType: createEntityClient<FinancialReportTypeDto>("financialReportType", role),
    payment: createEntityClient<PaymentDto>("payment", role),
    paymentStatus: createEntityClient<PaymentStatusDto>("paymentStatus", role),
    paymentType: createEntityClient<PaymentTypeDto>("paymentType", role),
    city: createEntityClient<CityDto>("city", role),
    country: createEntityClient<CountryDto>("country", role),
    property: createEntityClient<PropertyDto>("property", role),
    propertyStatus: createEntityClient<PropertyStatusDto>("propertyStatus", role),
    propertyType: createEntityClient<PropertyTypeDto>("propertyType", role),
    client: createEntityClient<ClientDto>("client", role),
    reservation: createEntityClient<ReservationDto>("reservation", role),
    reservationPlatform: createEntityClient<ReservationPlatformDto>("reservationPlatform", role),
    reservationRequest: createEntityClient<ReservationRequestDto>("reservationRequest", role),
    reservationRequestStatus: createEntityClient<ReservationRequestStatusDto>("reservationRequestStatus", role),
    reservationStatus: createEntityClient<ReservationStatusDto>("reservationStatus", role),
  };
}
