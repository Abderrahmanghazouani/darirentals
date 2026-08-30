"use client";

import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import { translations, type Locale, type Dict } from "./translations";

const STORAGE_KEY = "language";
const DEFAULT_LOCALE: Locale = "fr";

interface LanguageContextValue {
  locale: Locale;
  setLocale: (locale: Locale) => void;
  /** Dictionnaire de la langue courante — accès direct typé (dict.reserver.title), pas de
   * lookup par chaîne : une clé manquante dans l'une des deux langues est une erreur de
   * compilation, pas une chaîne cassée découverte à l'exécution. */
  dict: Dict;
}

const LanguageContext = createContext<LanguageContextValue | null>(null);

/**
 * Solution maison volontairement légère (voir NOTES-multi-langue.md) - mirror exact du pattern
 * déjà utilisé pour CurrencyProvider (Context + persistance localStorage, 100% client). Posé
 * une seule fois à la racine (app/layout.tsx) : toute l'app est déjà "use client", pas besoin
 * de routing par locale ni de Server Components traduits.
 */
export function LanguageProvider({ children }: { children: ReactNode }) {
  const [locale, setLocaleState] = useState<Locale>(DEFAULT_LOCALE);

  useEffect(() => {
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      if (stored === "fr" || stored === "en") {
        setLocaleState(stored);
      }
    } catch {
      // localStorage indisponible (SSR/navigation privée) : on garde le défaut FR.
    }
  }, []);

  function setLocale(next: Locale) {
    setLocaleState(next);
    try {
      localStorage.setItem(STORAGE_KEY, next);
    } catch {
      // pas grave : le choix reste actif pour la session en cours via le state React
    }
  }

  const value = useMemo<LanguageContextValue>(
    () => ({ locale, setLocale, dict: translations[locale] }),
    [locale]
  );

  return <LanguageContext.Provider value={value}>{children}</LanguageContext.Provider>;
}

export function useLanguage(): LanguageContextValue {
  const ctx = useContext(LanguageContext);
  if (!ctx) {
    throw new Error("useLanguage() doit être appelé à l'intérieur d'un <LanguageProvider>.");
  }
  return ctx;
}
