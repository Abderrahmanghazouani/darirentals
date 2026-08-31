"use client";

import { ThemeProvider as NextThemesProvider } from "next-themes";
import type { ComponentProps } from "react";

/**
 * Mode sombre/clair — voir NOTES-mode-sombre.md. Mirror du wrapper standard next-themes :
 * attribute="class" bascule la classe .dark sur <html> (déjà câblée dans app/globals.css via
 * @custom-variant dark (&:is(.dark *))), defaultTheme="system" détecte prefers-color-scheme au
 * premier chargement, enableSystem active la 3e option "Système", et next-themes persiste lui-
 * même le choix explicite dans localStorage (clé "theme") - pas besoin de le refaire à la main.
 */
export function ThemeProvider({ children, ...props }: ComponentProps<typeof NextThemesProvider>) {
  return (
    <NextThemesProvider attribute="class" defaultTheme="system" enableSystem {...props}>
      {children}
    </NextThemesProvider>
  );
}
