// Registre auto-généré : métadonnées de toutes les entités pour les pages CRUD génériques
export type FieldKind = "text" | "number" | "boolean" | "date";

export interface ScalarField {
  name: string;
  kind: FieldKind;
  nullable: boolean;
}

export interface RelationField {
  name: string;
  relatedEntity: string; // resource key, e.g. "propertyType"
}

export interface EntityDescriptor {
  resource: string;    // clé utilisée dans les routes API, ex: "currency"
  label: string;       // libellé humain, ex: "Devise"
  displayField: string | null;
  scalars: ScalarField[];
  relations: RelationField[];
}

export const entityRegistry: Record<string, EntityDescriptor> = {
  aiQuota: {
    resource: "aiQuota",
    label: "Ai Quota",
    displayField: "tokensAllocated",
    scalars: [
      { name: "tokensAllocated", kind: "number", nullable: true },
    ],
    relations: [
      { name: "enterprise", relatedEntity: "enterprise" },
    ],
  },
  aiUsageLog: {
    resource: "aiUsageLog",
    label: "Ai Usage Log",
    displayField: "tokensUsed",
    scalars: [
      { name: "tokensUsed", kind: "number", nullable: true },
      { name: "date", kind: "date", nullable: true },
    ],
    relations: [
      { name: "enterprise", relatedEntity: "enterprise" },
      { name: "aiUsageType", relatedEntity: "aiUsageType" },
      { name: "collaborator", relatedEntity: "collaborator" },
      { name: "document", relatedEntity: "document" },
    ],
  },
  aiUsageType: {
    resource: "aiUsageType",
    label: "Ai Usage Type",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  charge: {
    resource: "charge",
    label: "Charge",
    displayField: "label",
    scalars: [
      { name: "label", kind: "text", nullable: false },
      { name: "amount", kind: "number", nullable: true },
    ],
    relations: [
      { name: "property", relatedEntity: "property" },
      { name: "chargeType", relatedEntity: "chargeType" },
      { name: "payment", relatedEntity: "payment" },
    ],
  },
  chargeType: {
    resource: "chargeType",
    label: "Charge Type",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  serviceProvider: {
    resource: "serviceProvider",
    label: "Service Provider",
    displayField: "name",
    scalars: [
      { name: "name", kind: "text", nullable: false },
      { name: "phone", kind: "text", nullable: false },
      { name: "isActive", kind: "boolean", nullable: true },
    ],
    relations: [
      { name: "serviceType", relatedEntity: "serviceType" },
      { name: "enterprise", relatedEntity: "enterprise" },
    ],
  },
  serviceType: {
    resource: "serviceType",
    label: "Service Type",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  enterprise: {
    resource: "enterprise",
    label: "Enterprise",
    displayField: "name",
    scalars: [
      { name: "name", kind: "text", nullable: false },
      { name: "phone", kind: "text", nullable: false },
      { name: "address", kind: "text", nullable: false },
    ],
    relations: [
      { name: "currency", relatedEntity: "currency" },
    ],
  },
  enterpriseMembership: {
    resource: "enterpriseMembership",
    label: "Enterprise Membership",
    displayField: null,
    scalars: [
    ],
    relations: [
      { name: "collaborator", relatedEntity: "collaborator" },
      { name: "enterprise", relatedEntity: "enterprise" },
      { name: "collaboratorRole", relatedEntity: "collaboratorRole" },
    ],
  },
  collaborator: {
    resource: "collaborator",
    label: "Collaborator",
    displayField: "name",
    scalars: [
      { name: "name", kind: "text", nullable: false },
      { name: "phone", kind: "text", nullable: false },
      { name: "isActive", kind: "boolean", nullable: true },
      { name: "email", kind: "text", nullable: false },
      { name: "enabled", kind: "boolean", nullable: true },
      { name: "credentialsNonExpired", kind: "boolean", nullable: true },
      { name: "accountNonExpired", kind: "boolean", nullable: true },
      { name: "username", kind: "text", nullable: false },
      { name: "passwordChanged", kind: "boolean", nullable: true },
      { name: "accountNonLocked", kind: "boolean", nullable: true },
      { name: "password", kind: "text", nullable: false },
    ],
    relations: [
      { name: "displayCurrency", relatedEntity: "currency" },
    ],
  },
  collaboratorPermissionOverride: {
    resource: "collaboratorPermissionOverride",
    label: "Collaborator Permission Override",
    displayField: "canManageFinancials",
    scalars: [
      { name: "canManageFinancials", kind: "boolean", nullable: true },
      { name: "canManageUsers", kind: "boolean", nullable: true },
      { name: "canDeleteProperty", kind: "boolean", nullable: true },
      { name: "canManageServiceProviders", kind: "boolean", nullable: true },
      { name: "canManageAiUsage", kind: "boolean", nullable: true },
    ],
    relations: [
      { name: "enterpriseMembership", relatedEntity: "enterpriseMembership" },
    ],
  },
  collaboratorRole: {
    resource: "collaboratorRole",
    label: "Collaborator Role",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
      { name: "canManageFinancials", kind: "boolean", nullable: true },
      { name: "canManageUsers", kind: "boolean", nullable: true },
      { name: "canDeleteProperty", kind: "boolean", nullable: true },
      { name: "canManageServiceProviders", kind: "boolean", nullable: true },
      { name: "canManageAiUsage", kind: "boolean", nullable: true },
    ],
    relations: [
    ],
  },
  task: {
    resource: "task",
    label: "Task",
    displayField: "title",
    scalars: [
      { name: "title", kind: "text", nullable: false },
      { name: "description", kind: "text", nullable: false },
      { name: "dueDate", kind: "date", nullable: true },
    ],
    relations: [
      { name: "property", relatedEntity: "property" },
      { name: "reservation", relatedEntity: "reservation" },
      { name: "serviceProvider", relatedEntity: "serviceProvider" },
      { name: "assignedTo", relatedEntity: "collaborator" },
      { name: "taskType", relatedEntity: "taskType" },
      { name: "taskPriority", relatedEntity: "taskPriority" },
      { name: "taskStatus", relatedEntity: "taskStatus" },
    ],
  },
  taskPriority: {
    resource: "taskPriority",
    label: "Task Priority",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  taskStatus: {
    resource: "taskStatus",
    label: "Task Status",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  taskType: {
    resource: "taskType",
    label: "Task Type",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  document: {
    resource: "document",
    label: "Document",
    displayField: "fileName",
    scalars: [
      { name: "fileName", kind: "text", nullable: false },
      { name: "file", kind: "text", nullable: false },
      { name: "extractedVendor", kind: "text", nullable: false },
      { name: "extractedAmount", kind: "number", nullable: true },
    ],
    relations: [
      { name: "documentType", relatedEntity: "documentType" },
      { name: "reservation", relatedEntity: "reservation" },
      { name: "charge", relatedEntity: "charge" },
    ],
  },
  documentType: {
    resource: "documentType",
    label: "Document Type",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  currency: {
    resource: "currency",
    label: "Currency",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
      { name: "symbol", kind: "text", nullable: false },
    ],
    relations: [
    ],
  },
  exchangeRate: {
    resource: "exchangeRate",
    label: "Exchange Rate",
    displayField: "rate",
    scalars: [
      { name: "rate", kind: "number", nullable: true },
      { name: "source", kind: "text", nullable: false },
    ],
    relations: [
      { name: "baseCurrency", relatedEntity: "currency" },
      { name: "targetCurrency", relatedEntity: "currency" },
    ],
  },
  financialReport: {
    resource: "financialReport",
    label: "Financial Report",
    displayField: "totalRevenue",
    scalars: [
      { name: "totalRevenue", kind: "number", nullable: true },
      { name: "totalCharges", kind: "number", nullable: true },
      { name: "netProfit", kind: "number", nullable: true },
      { name: "generatedAt", kind: "date", nullable: true },
      { name: "file", kind: "text", nullable: false },
    ],
    relations: [
      { name: "financialReportType", relatedEntity: "financialReportType" },
      { name: "financialReportScope", relatedEntity: "financialReportScope" },
      { name: "enterprise", relatedEntity: "enterprise" },
      { name: "generatedBy", relatedEntity: "collaborator" },
    ],
  },
  financialReportProperty: {
    resource: "financialReportProperty",
    label: "Financial Report Property",
    displayField: null,
    scalars: [
    ],
    relations: [
      { name: "financialReport", relatedEntity: "financialReport" },
      { name: "property", relatedEntity: "property" },
    ],
  },
  financialReportScope: {
    resource: "financialReportScope",
    label: "Financial Report Scope",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  financialReportType: {
    resource: "financialReportType",
    label: "Financial Report Type",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  payment: {
    resource: "payment",
    label: "Payment",
    displayField: "amount",
    scalars: [
      { name: "amount", kind: "number", nullable: true },
      { name: "notes", kind: "text", nullable: false },
    ],
    relations: [
      { name: "serviceProvider", relatedEntity: "serviceProvider" },
      { name: "paymentType", relatedEntity: "paymentType" },
      { name: "paymentStatus", relatedEntity: "paymentStatus" },
    ],
  },
  paymentStatus: {
    resource: "paymentStatus",
    label: "Payment Status",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  paymentType: {
    resource: "paymentType",
    label: "Payment Type",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  city: {
    resource: "city",
    label: "City",
    displayField: "name",
    scalars: [
      { name: "name", kind: "text", nullable: false },
    ],
    relations: [
      { name: "country", relatedEntity: "country" },
    ],
  },
  country: {
    resource: "country",
    label: "Country",
    displayField: "name",
    scalars: [
      { name: "name", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
    ],
    relations: [
    ],
  },
  property: {
    resource: "property",
    label: "Property",
    displayField: "name",
    scalars: [
      { name: "name", kind: "text", nullable: false },
      { name: "streetNumber", kind: "text", nullable: false },
      { name: "streetName", kind: "text", nullable: false },
      { name: "postalCode", kind: "text", nullable: false },
      { name: "capacity", kind: "number", nullable: true },
      { name: "pricePerNight", kind: "number", nullable: true },
      { name: "latitude", kind: "number", nullable: true },
      { name: "longitude", kind: "number", nullable: true },
    ],
    relations: [
      { name: "propertyType", relatedEntity: "propertyType" },
      { name: "propertyStatus", relatedEntity: "propertyStatus" },
      { name: "city", relatedEntity: "city" },
      { name: "enterprise", relatedEntity: "enterprise" },
    ],
  },
  propertyStatus: {
    resource: "propertyStatus",
    label: "Property Status",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  propertyType: {
    resource: "propertyType",
    label: "Property Type",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  client: {
    resource: "client",
    label: "Client",
    displayField: "fullName",
    scalars: [
      { name: "fullName", kind: "text", nullable: false },
      { name: "phone", kind: "text", nullable: false },
      { name: "nationality", kind: "text", nullable: false },
      { name: "email", kind: "text", nullable: false },
      { name: "enabled", kind: "boolean", nullable: true },
      { name: "credentialsNonExpired", kind: "boolean", nullable: true },
      { name: "accountNonExpired", kind: "boolean", nullable: true },
      { name: "username", kind: "text", nullable: false },
      { name: "passwordChanged", kind: "boolean", nullable: true },
      { name: "accountNonLocked", kind: "boolean", nullable: true },
      { name: "password", kind: "text", nullable: false },
    ],
    relations: [
      { name: "enterprise", relatedEntity: "enterprise" },
    ],
  },
  reservation: {
    resource: "reservation",
    label: "Reservation",
    displayField: "reference",
    scalars: [
      { name: "reference", kind: "text", nullable: false },
      { name: "amount", kind: "number", nullable: true },
      { name: "pricePerNight", kind: "number", nullable: true },
      { name: "checkInDate", kind: "date", nullable: true },
      { name: "checkOutDate", kind: "date", nullable: true },
    ],
    relations: [
      { name: "client", relatedEntity: "client" },
      { name: "property", relatedEntity: "property" },
      { name: "reservationPlatform", relatedEntity: "reservationPlatform" },
      { name: "reservationStatus", relatedEntity: "reservationStatus" },
    ],
  },
  reservationPlatform: {
    resource: "reservationPlatform",
    label: "Reservation Platform",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  reservationRequest: {
    resource: "reservationRequest",
    label: "Reservation Request",
    displayField: "clientNote",
    scalars: [
      { name: "clientNote", kind: "text", nullable: false },
      { name: "staffNote", kind: "text", nullable: false },
    ],
    relations: [
      { name: "client", relatedEntity: "client" },
      { name: "requestedProperty", relatedEntity: "property" },
      { name: "alternativeProperty", relatedEntity: "property" },
      { name: "reviewedBy", relatedEntity: "collaborator" },
      { name: "reservationRequestStatus", relatedEntity: "reservationRequestStatus" },
      { name: "reservation", relatedEntity: "reservation" },
    ],
  },
  reservationRequestStatus: {
    resource: "reservationRequestStatus",
    label: "Reservation Request Status",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
  reservationStatus: {
    resource: "reservationStatus",
    label: "Reservation Status",
    displayField: "label",
    scalars: [
      { name: "description", kind: "text", nullable: false },
      { name: "code", kind: "text", nullable: false },
      { name: "label", kind: "text", nullable: false },
      { name: "style", kind: "text", nullable: false },
      { name: "isDefault", kind: "boolean", nullable: true },
      { name: "sortOrder", kind: "number", nullable: true },
    ],
    relations: [
    ],
  },
};

export const entityKeys = Object.keys(entityRegistry);