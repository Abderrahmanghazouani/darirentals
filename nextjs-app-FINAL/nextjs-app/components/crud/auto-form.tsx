"use client";

import { useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Checkbox } from "@/components/ui/checkbox";
import { DialogFooter } from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { EntityDescriptor, entityRegistry } from "@/lib/entity-registry";
import { getGenericClient, GenericDto } from "@/lib/generic-client";
import { Role } from "@/lib/api-client";

interface AutoFormProps {
  descriptor: EntityDescriptor;
  role: Role;
  initial: GenericDto | null;
  saving: boolean;
  onSubmit: (dto: GenericDto) => void;
  onCancel: () => void;
}

function humanizeField(name: string) {
  const withSpaces = name.replace(/([A-Z])/g, " $1");
  return withSpaces.charAt(0).toUpperCase() + withSpaces.slice(1);
}

function relatedDisplay(item: GenericDto, relatedResource: string): string {
  const display = entityRegistry[relatedResource]?.displayField;
  if (display && item[display] != null) return String(item[display]);
  return `#${item.id}`;
}

export function AutoForm({
  descriptor,
  role,
  initial,
  saving,
  onSubmit,
  onCancel,
}: AutoFormProps) {
  const [values, setValues] = useState<GenericDto>(() => {
    const base: GenericDto = { id: initial?.id ?? null };
    for (const f of descriptor.scalars) {
      base[f.name] =
        initial?.[f.name] ?? (f.kind === "boolean" ? false : f.kind === "number" ? null : "");
    }
    for (const r of descriptor.relations) {
      base[r.name] = (initial?.[r.name] as GenericDto | undefined) ?? null;
    }
    return base;
  });

  const [relatedOptions, setRelatedOptions] = useState<Record<string, GenericDto[]>>({});
  const [errors, setErrors] = useState<Record<string, string>>({});

  useEffect(() => {
    let cancelled = false;
    async function loadRelations() {
      const entries = await Promise.all(
        descriptor.relations.map(async (r) => {
          try {
            const items = await getGenericClient(r.relatedEntity, role).findAll();
            return [r.relatedEntity, items] as const;
          } catch {
            return [r.relatedEntity, []] as const;
          }
        })
      );
      if (!cancelled) {
        setRelatedOptions(Object.fromEntries(entries));
      }
    }
    loadRelations();
    return () => {
      cancelled = true;
    };
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [descriptor.resource]);

  function setField(name: string, value: unknown) {
    setValues((prev) => ({ ...prev, [name]: value }));
  }

  function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const nextErrors: Record<string, string> = {};
    for (const f of descriptor.scalars) {
      if (!f.nullable && f.kind === "text" && !String(values[f.name] ?? "").trim()) {
        nextErrors[f.name] = "Requis";
      }
    }
    setErrors(nextErrors);
    if (Object.keys(nextErrors).length > 0) return;
    onSubmit(values);
  }

  return (
    <form onSubmit={handleSubmit} className="space-y-4 max-h-[65vh] overflow-y-auto pr-1">
      {descriptor.scalars.map((f) => (
        <div key={f.name} className="space-y-2">
          <Label htmlFor={f.name}>{humanizeField(f.name)}</Label>
          {f.kind === "boolean" ? (
            <div className="flex items-center gap-2">
              <Checkbox
                id={f.name}
                checked={Boolean(values[f.name])}
                onCheckedChange={(checked) => setField(f.name, checked === true)}
              />
            </div>
          ) : f.kind === "date" ? (
            <Input
              id={f.name}
              type="date"
              value={values[f.name] ? String(values[f.name]).slice(0, 10) : ""}
              onChange={(e) => setField(f.name, e.target.value)}
            />
          ) : (
            <Input
              id={f.name}
              type={f.kind === "number" ? "number" : "text"}
              value={(values[f.name] as string | number | null) ?? ""}
              onChange={(e) =>
                setField(
                  f.name,
                  f.kind === "number"
                    ? e.target.value === ""
                      ? null
                      : Number(e.target.value)
                    : e.target.value
                )
              }
            />
          )}
          {errors[f.name] && <p className="text-sm text-destructive-text">{errors[f.name]}</p>}
        </div>
      ))}

      {descriptor.relations.map((r) => {
        const options = relatedOptions[r.relatedEntity] ?? [];
        const currentValue = values[r.name] as GenericDto | null;
        return (
          <div key={r.name} className="space-y-2">
            <Label>{humanizeField(r.name)}</Label>
            <Select
              value={currentValue?.id != null ? String(currentValue.id) : undefined}
              onValueChange={(val) => {
                const found = options.find((o) => String(o.id) === val) ?? null;
                setField(r.name, found);
              }}
            >
              <SelectTrigger className="w-full">
                <SelectValue placeholder="— Aucun —" />
              </SelectTrigger>
              <SelectContent>
                {options.map((o) => (
                  <SelectItem key={o.id} value={String(o.id)}>
                    {relatedDisplay(o, r.relatedEntity)}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        );
      })}

      <DialogFooter>
        <Button type="button" variant="outline" onClick={onCancel}>
          Annuler
        </Button>
        <Button type="submit" disabled={saving}>
          {saving ? "Enregistrement..." : "Enregistrer"}
        </Button>
      </DialogFooter>
    </form>
  );
}
