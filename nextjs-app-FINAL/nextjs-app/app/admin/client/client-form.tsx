"use client";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { z } from "zod";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { DialogFooter } from "@/components/ui/dialog";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { ClientDto, newClientDto } from "@/lib/types/Client";
import { EnterpriseDto } from "@/lib/types/Enterprise";
import { getEntityClients } from "@/lib/api";
import { Role } from "@/lib/api-client";

const clientSchema = z.object({
  fullName: z.string().min(1, "Requis"),
  email: z.string().min(1, "Requis").email("Email invalide"),
  phone: z.string().optional(),
  nationality: z.string().optional(),
  username: z.string().min(1, "Requis"),
  password: z.string().optional(),
});

type ClientFormValues = z.infer<typeof clientSchema>;

interface ClientFormProps {
  initial: ClientDto | null;
  saving: boolean;
  role: Role;
  onSubmit: (dto: ClientDto) => void;
  onCancel: () => void;
}

export function ClientForm({ initial, saving, role, onSubmit, onCancel }: ClientFormProps) {
  const base = initial ?? newClientDto();
  const isEditing = initial != null;

  const [enterprises, setEnterprises] = useState<EnterpriseDto[]>([]);
  const [enterpriseId, setEnterpriseId] = useState<number | null>(base.enterprise?.id ?? null);

  useEffect(() => {
    getEntityClients(role)
      .enterprise.findAll()
      .then((data) => setEnterprises(data ?? []))
      .catch(() => setEnterprises([]));
  }, [role]);

  const form = useForm<ClientFormValues>({
    resolver: zodResolver(clientSchema),
    defaultValues: {
      fullName: base.fullName,
      email: base.email,
      phone: base.phone ?? "",
      nationality: base.nationality ?? "",
      username: base.username,
      password: "",
    },
  });

  function handleSubmit(values: ClientFormValues) {
    onSubmit({
      ...base,
      fullName: values.fullName,
      email: values.email,
      phone: values.phone ?? "",
      nationality: values.nationality ?? "",
      username: values.username,
      password: values.password ? values.password : base.password,
      enterprise: enterprises.find((e) => e.id === enterpriseId) ?? null,
    });
  }

  return (
    <form onSubmit={form.handleSubmit(handleSubmit)} className="space-y-4 max-h-[65vh] overflow-y-auto pr-1">
      <div className="space-y-2">
        <Label htmlFor="fullName">Nom complet</Label>
        <Input id="fullName" {...form.register("fullName")} />
        {form.formState.errors.fullName && (
          <p className="text-sm text-destructive">{form.formState.errors.fullName.message}</p>
        )}
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="email">Email</Label>
          <Input id="email" type="email" {...form.register("email")} />
          {form.formState.errors.email && (
            <p className="text-sm text-destructive">{form.formState.errors.email.message}</p>
          )}
        </div>
        <div className="space-y-2">
          <Label htmlFor="phone">Téléphone</Label>
          <Input id="phone" {...form.register("phone")} />
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="nationality">Nationalité</Label>
          <Input id="nationality" {...form.register("nationality")} />
        </div>
        <div className="space-y-2">
          <Label>Société (entreprise)</Label>
          <Select
            value={enterpriseId != null ? String(enterpriseId) : undefined}
            onValueChange={(v) => setEnterpriseId(Number(v))}
          >
            <SelectTrigger className="w-full">
              <SelectValue placeholder="— Choisir —" />
            </SelectTrigger>
            <SelectContent>
              {enterprises.map((e) => (
                <SelectItem key={e.id} value={String(e.id)}>
                  {e.name}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div className="space-y-2">
          <Label htmlFor="username">Nom d&apos;utilisateur</Label>
          <Input id="username" {...form.register("username")} />
          {form.formState.errors.username && (
            <p className="text-sm text-destructive">{form.formState.errors.username.message}</p>
          )}
        </div>
        <div className="space-y-2">
          <Label htmlFor="password">
            Mot de passe {isEditing && <span className="text-muted-foreground">(laisser vide pour ne pas changer)</span>}
          </Label>
          <Input id="password" type="password" {...form.register("password")} />
        </div>
      </div>

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