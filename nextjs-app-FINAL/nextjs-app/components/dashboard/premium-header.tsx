"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { ChevronDown, LogOut, Building2 } from "lucide-react";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { CurrencySelector } from "@/components/currency/currency-selector";
import { LanguageToggle } from "@/components/i18n/language-toggle";
import { useLanguage } from "@/lib/i18n/language-context";
import { Locale } from "@/lib/i18n/translations";
import { logout, getCurrentUser, CurrentUser } from "@/lib/auth";

function initialsFor(user: CurrentUser | null): string {
  if (!user) return "?";
  if (user.firstName || user.lastName) {
    return `${user.firstName?.[0] ?? ""}${user.lastName?.[0] ?? ""}`.toUpperCase() || "?";
  }
  return user.username.slice(0, 2).toUpperCase();
}

function displayNameFor(user: CurrentUser | null, accountFallback: string): string {
  if (!user) return accountFallback;
  if (user.firstName || user.lastName) {
    return [user.firstName, user.lastName].filter(Boolean).join(" ");
  }
  return user.username;
}

// La date reste au format long propre à la langue choisie - "long" ici veut dire
// "vendredi 28 août 2026" / "Friday, August 28, 2026", pas une traduction de libellé fixe.
function longDateToday(locale: Locale): string {
  return new Date().toLocaleDateString(locale === "en" ? "en-US" : "fr-FR", {
    weekday: "long",
    day: "numeric",
    month: "long",
    year: "numeric",
  });
}

interface PremiumHeaderProps {
  activeProperties: number;
  loading: boolean;
}

export function PremiumHeader({ activeProperties, loading }: PremiumHeaderProps) {
  const router = useRouter();
  const { locale, dict } = useLanguage();
  const [user, setUser] = useState<CurrentUser | null>(null);

  useEffect(() => {
    setUser(getCurrentUser());
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

        <div className="flex items-center gap-2">
          <LanguageToggle />
          <CurrencySelector />

          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <button className="flex items-center gap-2 rounded-md border px-2 py-1.5 text-sm hover:bg-accent transition-colors">
                <span className="flex size-6 items-center justify-center rounded-full bg-secondary text-secondary-foreground text-xs font-medium">
                  {initialsFor(user)}
                </span>
                <span className="hidden sm:inline max-w-[10rem] truncate">
                  {displayNameFor(user, dict.dashboardHeader.account)}
                </span>
                <ChevronDown className="size-3.5 text-muted-foreground" />
              </button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end">
              <DropdownMenuLabel className="font-normal">
                <p className="text-sm font-medium">{displayNameFor(user, dict.dashboardHeader.account)}</p>
                {user?.email && <p className="text-xs text-muted-foreground truncate">{user.email}</p>}
              </DropdownMenuLabel>
              <DropdownMenuSeparator />
              <DropdownMenuItem variant="destructive" onSelect={handleLogout}>
                <LogOut className="size-4" /> {dict.dashboardHeader.logout}
              </DropdownMenuItem>
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>

      <p className="mt-1 text-sm text-muted-foreground">
        {dict.dashboardHeader.title}
        {!loading &&
          ` · ${activeProperties} ${activeProperties > 1 ? dict.dashboardHeader.activeProperties : dict.dashboardHeader.activeProperty}`}
        {" · "}
        <span className="capitalize">{longDateToday(locale)}</span>
      </p>
    </div>
  );
}
