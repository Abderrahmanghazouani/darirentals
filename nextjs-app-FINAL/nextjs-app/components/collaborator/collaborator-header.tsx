"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { Building2, ChevronDown, LogOut } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { logout, getCurrentUser, CurrentUser } from "@/lib/auth";
import { getEntityClients } from "@/lib/api";
import { getSelectedEnterpriseId } from "@/lib/enterprise-context";
import { CollaboratorDto } from "@/lib/types/Collaborator";

function initialsFor(user: CurrentUser | null): string {
  if (!user) return "?";
  if (user.firstName || user.lastName) {
    return `${user.firstName?.[0] ?? ""}${user.lastName?.[0] ?? ""}`.toUpperCase() || "?";
  }
  return user.username.slice(0, 2).toUpperCase();
}

function displayNameFor(user: CurrentUser | null): string {
  if (!user) return "Compte";
  if (user.firstName || user.lastName) {
    return [user.firstName, user.lastName].filter(Boolean).join(" ");
  }
  return user.username;
}

export function CollaboratorHeader() {
  const router = useRouter();
  const [user, setUser] = useState<CurrentUser | null>(null);
  const [enterpriseName, setEnterpriseName] = useState<string | null>(null);
  const [hasMultipleEnterprises, setHasMultipleEnterprises] = useState(false);

  useEffect(() => {
    const u = getCurrentUser();
    setUser(u);
    if (!u) return;
    const enterpriseId = getSelectedEnterpriseId();

    getEntityClients("collaborator")
      .collaborator.findAll()
      .then((all: CollaboratorDto[]) => {
        const me = (all ?? []).find((c) => c.username === u.username);
        const memberships = me?.enterpriseMemberships ?? [];
        setHasMultipleEnterprises(memberships.length > 1);
        const active = memberships.find((m) => m.enterprise?.id === enterpriseId);
        setEnterpriseName(active?.enterprise?.name ?? null);
      })
      .catch(() => {
        // Nom de société non essentiel à l'affichage - échec silencieux, le header reste utilisable.
      });
  }, []);

  function handleLogout() {
    logout();
    router.push("/login");
  }

  return (
    <div className="sticky top-0 z-10 -mx-6 mb-6 animate-in fade-in duration-200 border-b bg-card/95 px-6 py-4 backdrop-blur supports-backdrop-filter:bg-card/80">
      <div className="flex items-center justify-between gap-4">
        <div className="flex items-center gap-2.5">
          <div className="flex size-8 items-center justify-center rounded-md bg-primary text-primary-foreground">
            <Building2 className="size-4.5" />
          </div>
          <span className="text-lg font-semibold tracking-tight">DariRentals</span>
        </div>

        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <button className="flex items-center gap-2 rounded-md border px-2 py-1.5 text-sm hover:bg-accent transition-colors">
              <span className="flex size-6 items-center justify-center rounded-full bg-secondary text-secondary-foreground text-xs font-medium">
                {initialsFor(user)}
              </span>
              <span className="hidden sm:inline max-w-[10rem] truncate">{displayNameFor(user)}</span>
              <ChevronDown className="size-3.5 text-muted-foreground" />
            </button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end">
            <DropdownMenuLabel className="font-normal">
              <p className="text-sm font-medium">{displayNameFor(user)}</p>
              {user?.email && <p className="text-xs text-muted-foreground truncate">{user.email}</p>}
            </DropdownMenuLabel>
            <DropdownMenuSeparator />
            {hasMultipleEnterprises && (
              <DropdownMenuItem onSelect={() => router.push("/select-enterprise")}>
                <Building2 className="size-4" /> Changer de société
              </DropdownMenuItem>
            )}
            <DropdownMenuItem variant="destructive" onSelect={handleLogout}>
              <LogOut className="size-4" /> Déconnexion
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>

      <p className="mt-1 text-sm text-muted-foreground">
        Espace Collaborateur
        {enterpriseName && ` · ${enterpriseName}`}
      </p>
    </div>
  );
}
