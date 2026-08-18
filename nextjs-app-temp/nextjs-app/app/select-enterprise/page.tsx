"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { jwtDecode } from "jwt-decode";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { ChevronRight, LogOut, Building2 } from "lucide-react";
import { getEntityClients } from "@/lib/api";
import { getStoredToken, logout, DecodedToken } from "@/lib/auth";
import { setSelectedEnterpriseId } from "@/lib/enterprise-context";
import { CollaboratorDto } from "@/lib/types/Collaborator";
import { EnterpriseMembershipDto } from "@/lib/types/EnterpriseMembership";

export default function SelectEnterprisePage() {
  const router = useRouter();
  const [memberships, setMemberships] = useState<EnterpriseMembershipDto[] | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    async function load() {
      const token = getStoredToken();
      if (!token) {
        router.replace("/login");
        return;
      }

      let username: string;
      try {
        const decoded = jwtDecode<DecodedToken>(token.replace(/^Bearer\s+/, ""));
        username = decoded.sub;
      } catch {
        router.replace("/login");
        return;
      }

      try {
        const all: CollaboratorDto[] = await getEntityClients("collaborator").collaborator.findAll();
        const me = (all ?? []).find((c) => c.username === username);
        const list = me?.enterpriseMemberships ?? [];

        if (list.length === 0) {
          router.replace("/collaborator");
          return;
        }
        if (list.length === 1 && list[0].enterprise?.id != null) {
          setSelectedEnterpriseId(list[0].enterprise.id);
          router.replace("/collaborator");
          return;
        }

        setMemberships(list);
      } catch {
        setError("Impossible de charger vos sociétés. Réessayez.");
      }
    }
    load();
  }, [router]);

  function choose(m: EnterpriseMembershipDto) {
    if (m.enterprise?.id == null) return;
    setSelectedEnterpriseId(m.enterprise.id);
    router.push("/collaborator");
  }

  return (
    <div className="min-h-screen flex items-center justify-center p-6 bg-muted/30">
      <div className="w-full max-w-lg space-y-6">
        <div className="text-center space-y-2">
          <div className="inline-flex items-center justify-center size-12 rounded-lg bg-primary text-primary-foreground">
            <Building2 className="size-6" />
          </div>
          <h1 className="text-2xl font-bold">Choisir une société</h1>
          <p className="text-sm text-muted-foreground">
            Vous avez accès à plusieurs sociétés. Dans laquelle voulez-vous travailler ?
          </p>
        </div>

        {error && <p className="text-sm text-destructive text-center">{error}</p>}

        <div className="space-y-3">
          {memberships === null && !error && (
            <p className="text-sm text-muted-foreground text-center">Chargement...</p>
          )}
          {memberships?.map((m) => (
            <Card
              key={m.id}
              className="cursor-pointer hover:bg-accent transition-colors"
              onClick={() => choose(m)}
            >
              <CardContent className="flex items-center justify-between py-4">
                <div>
                  <p className="font-semibold">{m.enterprise?.name ?? "Société"}</p>
                  {m.enterprise?.address && (
                    <p className="text-sm text-muted-foreground">{m.enterprise.address}</p>
                  )}
                </div>
                <div className="flex items-center gap-2">
                  {m.collaboratorRole && <Badge>{m.collaboratorRole.label}</Badge>}
                  <ChevronRight className="size-4 text-muted-foreground" />
                </div>
              </CardContent>
            </Card>
          ))}
        </div>

        <div className="text-center">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => {
              logout();
              router.push("/login");
            }}
          >
            <LogOut className="size-4" /> Se déconnecter
          </Button>
        </div>
      </div>
    </div>
  );
}