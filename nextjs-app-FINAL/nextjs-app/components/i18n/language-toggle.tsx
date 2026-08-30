"use client";

import { Button } from "@/components/ui/button";
import { Languages } from "lucide-react";
import { useLanguage } from "@/lib/i18n/language-context";

/**
 * Sélecteur de langue FR/EN — mirror du style déjà utilisé pour le toggle Calendrier/Liste
 * (app/collaborator/reservations/page.tsx). Texte plutôt que drapeaux : l'anglais n'a pas de
 * drapeau unique et représentatif, un toggle textuel évite toute ambiguïté.
 */
export function LanguageToggle({ className }: { className?: string }) {
  const { locale, setLocale } = useLanguage();

  return (
    <div className={`flex items-center gap-1.5 rounded-md border overflow-hidden ${className ?? ""}`}>
      <Languages className="size-3.5 text-muted-foreground ml-2 shrink-0" />
      <Button
        type="button"
        variant={locale === "fr" ? "default" : "ghost"}
        size="sm"
        className="rounded-none px-2.5"
        onClick={() => setLocale("fr")}
      >
        FR
      </Button>
      <Button
        type="button"
        variant={locale === "en" ? "default" : "ghost"}
        size="sm"
        className="rounded-none px-2.5"
        onClick={() => setLocale("en")}
      >
        EN
      </Button>
    </div>
  );
}
