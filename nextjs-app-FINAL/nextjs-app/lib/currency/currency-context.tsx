"use client";

import { createContext, useContext, useEffect, useMemo, useState, type ReactNode } from "react";
import { CurrencyDto } from "@/lib/types/Currency";
import { ExchangeRateDto } from "@/lib/types/ExchangeRate";
import { BASE_CURRENCY_CODE, availableCurrencyCodes, convertFromBase, formatMoney } from "./conversion";

const STORAGE_KEY = "displayCurrencyCode";

interface CurrencyContextValue {
  currencies: CurrencyDto[];
  availableCodes: string[];
  selectedCode: string;
  setSelectedCode: (code: string) => void;
  /** Convertit un montant en MAD vers la devise sélectionnée. */
  convert: (amountInMad: number) => number;
  /** Convertit ET formate (avec symbole) un montant en MAD. */
  format: (amountInMad: number) => string;
  loading: boolean;
}

const CurrencyContext = createContext<CurrencyContextValue | null>(null);

interface CurrencyProviderProps {
  children: ReactNode;
  fetchCurrencies: () => Promise<CurrencyDto[]>;
  fetchRates: () => Promise<ExchangeRateDto[]>;
  /** Devise à préselectionner tant que l'utilisateur n'a jamais choisi explicitement
   * (ex: Collaborator.displayCurrency.code). Peut arriver après le montage (fetch async). */
  defaultCode?: string | null;
}

export function CurrencyProvider({ children, fetchCurrencies, fetchRates, defaultCode }: CurrencyProviderProps) {
  const [currencies, setCurrencies] = useState<CurrencyDto[]>([]);
  const [rates, setRates] = useState<ExchangeRateDto[]>([]);
  const [selectedCode, setSelectedCodeState] = useState<string>(BASE_CURRENCY_CODE);
  const [loading, setLoading] = useState(true);
  const [hasAppliedDefault, setHasAppliedDefault] = useState(false);

  useEffect(() => {
    let cancelled = false;
    Promise.all([fetchCurrencies(), fetchRates()])
      .then(([c, r]) => {
        if (cancelled) return;
        setCurrencies(c ?? []);
        setRates(r ?? []);
      })
      .catch(() => {
        if (!cancelled) {
          setCurrencies([]);
          setRates([]);
        }
      })
      .finally(() => {
        if (!cancelled) setLoading(false);
      });
    return () => {
      cancelled = true;
    };
    // fetchCurrencies/fetchRates sont censées être stables (définies au niveau module ou memoïsées).
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  // Une préférence déjà enregistrée (choix explicite précédent) prime toujours.
  useEffect(() => {
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      if (stored) {
        setSelectedCodeState(stored);
        setHasAppliedDefault(true);
      }
    } catch {
      // localStorage indisponible (SSR/navigation privée) : on garde le défaut MAD.
    }
  }, []);

  // À défaut de préférence stockée, applique la devise d'affichage du collaborateur
  // dès qu'elle devient disponible (chargement async côté page appelante).
  useEffect(() => {
    if (hasAppliedDefault || !defaultCode) return;
    setSelectedCodeState(defaultCode);
    setHasAppliedDefault(true);
  }, [defaultCode, hasAppliedDefault]);

  function setSelectedCode(code: string) {
    setSelectedCodeState(code);
    setHasAppliedDefault(true);
    try {
      localStorage.setItem(STORAGE_KEY, code);
    } catch {
      // pas grave : le choix reste actif pour la session en cours via le state React
    }
  }

  const value = useMemo<CurrencyContextValue>(() => {
    const convert = (amountInMad: number) => convertFromBase(amountInMad, selectedCode, rates);
    const format = (amountInMad: number) => {
      const currency = currencies.find((c) => c.code === selectedCode);
      return formatMoney(convert(amountInMad), selectedCode, currency?.symbol);
    };
    return {
      currencies,
      availableCodes: availableCurrencyCodes(currencies, rates),
      selectedCode,
      setSelectedCode,
      convert,
      format,
      loading,
    };
  }, [currencies, rates, selectedCode, loading]);

  return <CurrencyContext.Provider value={value}>{children}</CurrencyContext.Provider>;
}

export function useCurrency(): CurrencyContextValue {
  const ctx = useContext(CurrencyContext);
  if (!ctx) {
    throw new Error("useCurrency() doit être appelé à l'intérieur d'un <CurrencyProvider>.");
  }
  return ctx;
}
