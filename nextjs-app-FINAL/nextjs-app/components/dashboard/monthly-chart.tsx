"use client";

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
}

export function MonthlyChart({ data }: MonthlyChartProps) {
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
        <ComposedChart data={data} margin={{ top: 10, right: 20, left: 0, bottom: 0 }}>
          <CartesianGrid strokeDasharray="3 3" vertical={false} />
          <XAxis dataKey="month" tick={{ fontSize: 12 }} />
          <YAxis tick={{ fontSize: 12 }} />
          <Tooltip
            formatter={(value: number) => `${value.toLocaleString("fr-FR")} MAD`}
          />
          <Legend />
          <Bar dataKey="revenue" name="Revenus" fill="#16a34a" radius={[4, 4, 0, 0]} />
          <Bar dataKey="charges" name="Charges" fill="#dc2626" radius={[4, 4, 0, 0]} />
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