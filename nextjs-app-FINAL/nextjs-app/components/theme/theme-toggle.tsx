"use client";

import { useEffect, useState } from "react";
import { useTheme } from "next-themes";
import { Button } from "@/components/ui/button";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Sun, Moon, Monitor } from "lucide-react";

/**
 * Sélecteur de thème Clair/Sombre/Système — même style (groupe de boutons) et même
 * emplacement que LanguageToggle, pour cohérence visuelle. Voir NOTES-mode-sombre.md.
 *
 * `compact` : variante un seul bouton + menu déroulant, pour la sidebar repliée en mode
 * icônes seules (pas la place pour un groupe de 3 boutons sur ~72px de large). Voir
 * NOTES-sidebar-premium.md.
 */
export function ThemeToggle({
  className,
  compact = false,
}: {
  className?: string;
  compact?: boolean;
}) {
  const { theme, resolvedTheme, setTheme } = useTheme();
  // next-themes ne connaît le thème réel qu'après le montage côté client (le serveur ne sait
  // pas quelle préférence système/localStorage l'utilisateur a) - éviter tout rendu qui
  // dépendrait de "theme" avant ce montage, sous peine de mismatch d'hydratation.
  const [mounted, setMounted] = useState(false);
  useEffect(() => setMounted(true), []);

  const current = mounted ? theme : undefined;

  if (compact) {
    const CurrentIcon = mounted && resolvedTheme === "dark" ? Moon : Sun;
    return (
      <DropdownMenu>
        <DropdownMenuTrigger asChild>
          <Button
            type="button"
            variant="ghost"
            size="icon"
            className={className}
            aria-label="Thème"
            title="Thème"
          >
            <CurrentIcon className="size-4" />
          </Button>
        </DropdownMenuTrigger>
        <DropdownMenuContent side="right" align="end">
          <DropdownMenuItem onClick={() => setTheme("light")}>
            <Sun className="size-4" /> Clair
          </DropdownMenuItem>
          <DropdownMenuItem onClick={() => setTheme("dark")}>
            <Moon className="size-4" /> Sombre
          </DropdownMenuItem>
          <DropdownMenuItem onClick={() => setTheme("system")}>
            <Monitor className="size-4" /> Système
          </DropdownMenuItem>
        </DropdownMenuContent>
      </DropdownMenu>
    );
  }

  return (
    <div className={`flex items-center rounded-md border overflow-hidden ${className ?? ""}`}>
      <Button
        type="button"
        variant={current === "light" ? "default" : "ghost"}
        size="sm"
        className="rounded-none px-2"
        title="Clair"
        onClick={() => setTheme("light")}
      >
        <Sun className="size-3.5" />
      </Button>
      <Button
        type="button"
        variant={current === "dark" ? "default" : "ghost"}
        size="sm"
        className="rounded-none px-2"
        title="Sombre"
        onClick={() => setTheme("dark")}
      >
        <Moon className="size-3.5" />
      </Button>
      <Button
        type="button"
        variant={current === "system" ? "default" : "ghost"}
        size="sm"
        className="rounded-none px-2"
        title="Système"
        onClick={() => setTheme("system")}
      >
        <Monitor className="size-3.5" />
      </Button>
    </div>
  );
}
