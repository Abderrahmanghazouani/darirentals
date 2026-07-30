import { EntityColumn } from "@/components/crud/entity-table";
import { EntityDescriptor, entityRegistry } from "@/lib/entity-registry";
import { GenericDto } from "@/lib/generic-client";

function humanizeField(name: string) {
  const withSpaces = name.replace(/([A-Z])/g, " $1");
  return withSpaces.charAt(0).toUpperCase() + withSpaces.slice(1);
}

/** Construit jusqu'à 5 colonnes pertinentes (scalaires puis relations) pour la table d'une entité. */
export function buildAutoColumns(descriptor: EntityDescriptor): EntityColumn<GenericDto>[] {
  const columns: EntityColumn<GenericDto>[] = [];

  for (const f of descriptor.scalars) {
    if (columns.length >= 4) break;
    columns.push({
      header: humanizeField(f.name),
      render: (item) => {
        const v = item[f.name];
        if (f.kind === "boolean") return v ? "Oui" : "Non";
        if (v == null) return "—";
        return String(v);
      },
    });
  }

  for (const r of descriptor.relations) {
    if (columns.length >= 5) break;
    const relatedDisplay = entityRegistry[r.relatedEntity]?.displayField;
    columns.push({
      header: humanizeField(r.name),
      render: (item) => {
        const rel = item[r.name] as GenericDto | null | undefined;
        if (!rel) return "—";
        if (relatedDisplay && rel[relatedDisplay] != null) return String(rel[relatedDisplay]);
        return `#${rel.id}`;
      },
    });
  }

  return columns;
}
