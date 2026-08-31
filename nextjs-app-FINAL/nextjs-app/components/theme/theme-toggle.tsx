"use client";

import { useEffect, useState } from "react";
import { useTheme } from "next-themes";
import { Button } from "@/components/ui/button";
import { Sun, Moon, Monitor } from "lucide-react";

/**
 * Sélecteur de thème Clair/Sombre/Système — même style (groupe de boutons) et même
 * emplacement que LanguageToggle, pour cohérence visuelle. Voir NOTES-mode-sombre.md.
 */
export function ThemeToggle({ className }: { className?: string }) {
  const { theme, setTheme } = useTheme();
  // next-themes ne connaît le thème réel qu'après le montage côté client (le serveur ne sait
  // pas quelle préférence système/localStorage l'utilisateur a) - éviter tout rendu qui
  // dépendrait de "theme" avant ce montage, sous peine de mismatch d'hydratation.
  const [mounted, setMounted] = useState(false);
  useEffect(() => setMounted(true), []);

  const current = mounted ? theme : undefined;

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
