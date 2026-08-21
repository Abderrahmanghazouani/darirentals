import re

entities = ["AiQuota","AiUsageLog","AiUsageType","Charge","ChargeType","ServiceProvider","ServiceType",
"Enterprise","EnterpriseMembership","Collaborator","CollaboratorPermissionOverride","CollaboratorRole",
"Task","TaskPriority","TaskStatus","TaskType","Document","DocumentType","Currency","ExchangeRate",
"FinancialReport","FinancialReportProperty","FinancialReportScope","FinancialReportType","Payment",
"PaymentStatus","PaymentType","City","Country","Property","PropertyStatus","PropertyType","Client",
"Reservation","ReservationPlatform","ReservationRequest","ReservationRequestStatus","ReservationStatus"]

def camel(name):
    return name[0].lower() + name[1:]

lines = []
lines.append('// Auto-généré : un client API par entité, basé sur createEntityClient')
lines.append('import { createEntityClient, Role } from "../api-client";')
for e in entities:
    lines.append(f'import type {{ {e}Dto }} from "../types/{e}";')
lines.append("")
lines.append("export function getEntityClients(role: Role) {")
lines.append("  return {")
for e in entities:
    resource = camel(e)
    lines.append(f'    {resource}: createEntityClient<{e}Dto>("{resource}", role),')
lines.append("  };")
lines.append("}")
lines.append("")

open("/home/claude/nextjs-app/lib/api/index.ts", "w", encoding='utf-8').write("\n".join(lines))
print("done,", len(entities), "entities")
