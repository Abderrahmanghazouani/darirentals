"use client";

import { useMemo } from "react";
import {
  ResponsiveContainer,
  ComposedChart,
  Bar,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";

export interface MonthlyFinancials {
  month: string; // ex: "Août 2026"
  revenue: number;
  charges: number;
  profit: number;
}

interface MonthlyChartProps {
  data: MonthlyFinancials[];
  /** Personnalise l'affichage des montants (ex: conversion devise). Par défaut : MAD brut. */
  formatValue?: (value: number) => string;
  /** Convertit un montant MAD vers la devise affichée. Sert à tracer barres/courbe (et donc l'axe Y)
   * dans l'échelle de la devise choisie ; le tooltip reste formaté via formatValue sur la valeur MAD. */
  convertValue?: (value: number) => number;
}

type ChartRow = MonthlyFinancials & { raw: MonthlyFinancials };

export function MonthlyChart({ data, formatValue, convertValue }: MonthlyChartProps) {
  const format = formatValue ?? ((value: number) => `${value.toLocaleString("fr-FR")} MAD`);

  // Valeurs tracées = converties ; on garde les valeurs MAD d'origine dans `raw` pour le tooltip.
  const chartData = useMemo<ChartRow[]>(() => {
    const convert = convertValue ?? ((v: number) => v);
    return data.map((d) => ({
      ...d,
      revenue: convert(d.revenue),
      charges: convert(d.charges),
      profit: convert(d.profit),
      raw: d,
    }));
  }, [data, convertValue]);

  if (data.length === 0) {
    return (
      <p className="text-sm text-muted-foreground text-center py-12">
        Pas encore assez de données pour afficher un graphique.
      </p>
    );
  }

  return (
    <div style={{ width: "100%", height: 320 }}>
      <ResponsiveContainer>
        <ComposedChart data={chartData} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} />
          <XAxis dataKey="month" tick={{ fontSize: 12 }} />
          <YAxis
            tick={{ fontSize: 12 }}
            tickFormatter={(v: number) => v.toLocaleString("fr-FR", { maximumFractionDigits: 2 })}
          />
          <Tooltip
            formatter={(value, _name, item) => {
              const key = item?.dataKey as keyof MonthlyFinancials | undefined;
              const original = key ? (item?.payload as ChartRow | undefined)?.raw?.[key] : undefined;
              return format(typeof original === "number" ? original : Number(value));
            }}
          />
          <Legend />
          <Bar dataKey="revenue" name="Revenus" fill="var(--color-success)" radius={[4, 4, 0, 0]} />
          <Bar dataKey="charges" name="Charges" fill="var(--color-destructive)" radius={[4, 4, 0, 0]} />
          <Line
            type="monotone"
            dataKey="profit"
            name="Bénéfice net"
            stroke="#171717"
            strokeWidth={2}
            dot={{ r: 3 }}
          />
        </ComposedChart>
      </ResponsiveContainer>
    </div>
  );
}