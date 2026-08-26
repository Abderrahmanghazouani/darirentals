"use client";

import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { useCurrency } from "@/lib/currency/currency-context";

/** Sélecteur de devise réutilisable — branché sur le CurrencyProvider ambiant. */
export function CurrencySelector({ className }: { className?: string }) {
  const { selectedCode, setSelectedCode, availableCodes, loading } = useCurrency();

  if (loading || availableCodes.length <= 1) {
    return null;
  }

  return (
    <Select value={selectedCode} onValueChange={setSelectedCode}>
      <SelectTrigger size="sm" className={className}>
        <SelectValue />
      </SelectTrigger>
      <SelectContent>
        {availableCodes.map((code) => (
          <SelectItem key={code} value={code}>
            {code}
          </SelectItem>
        ))}
      </SelectContent>
    </Select>
  );
}
