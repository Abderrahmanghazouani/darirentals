"use client";

import { useMemo, useState } from "react";
import { Minus, TrendingDown, TrendingUp } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { MonthlyChart } from "@/components/dashboard/monthly-chart";
import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import {
  computeRevenueMonthComparison,
  computeRevenueSeries,
  REVENUE_PERIOD_KEYS,
  REVENUE_PERIODS,
  RevenuePeriod,
  RevenueTrend,
} from "@/lib/dashboard/revenue-intelligence";
import { useLanguage } from "@/lib/i18n/language-context";

// Tokens de thème (palette DariRentals - voir app/globals.css) : succès = teal, négatif =
// rouge doux (= --destructive), neutre = gris muted existant.
const TREND_BADGE_CLASS: Record<RevenueTrend, string> = {
  up: "bg-success/10 text-success",
  new: "bg-success/10 text-success",
  down: "bg-destructive/10 text-destructive-text",
  stable: "bg-muted text-muted-foreground",
};

const TREND_ICON: Record<RevenueTrend, typeof TrendingUp> = {
  up: TrendingUp,
  new: TrendingUp,
  down: TrendingDown,
  stable: Minus,
};

interface RevenueIntelligenceCardProps {
  reservations: ReservationDto[];
  charges: ChargeDto[];
  formatValue: (value: number) => string;
}

export function RevenueIntelligenceCard({ reservations, charges, formatValue }: RevenueIntelligenceCardProps) {
  const { dict } = useLanguage();
  const [period, setPeriod] = useState<RevenuePeriod>("12m");

  const comparison = useMemo(
    () => computeRevenueMonthComparison(reservations, charges),
    [reservations, charges]
  );

  const seriesData = useMemo(
    () => computeRevenueSeries(reservations, charges, period),
    [reservations, charges, period]
  );

  const TrendIcon = TREND_ICON[comparison.trend];

  return (
    <Card>
      <CardHeader>
        <div className="flex flex-wrap items-center justify-between gap-3">
          <CardTitle>{dict.revenueIntelligence.title}</CardTitle>
          <Select value={period} onValueChange={(v) => setPeriod(v as RevenuePeriod)}>
            <SelectTrigger size="sm" className="w-[120px]">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {REVENUE_PERIOD_KEYS.map((key) => (
                <SelectItem key={key} value={key}>
                  {REVENUE_PERIODS[key].label}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </CardHeader>
      <CardContent className="space-y-4">
        <div>
          <div className="flex flex-wrap items-end gap-2">
            <p className="text-4xl font-bold tabular-nums">{formatValue(comparison.currentRevenue)}</p>
            <span
              className={`mb-1 flex items-center gap-1 rounded-full px-2 py-0.5 text-sm font-medium ${TREND_BADGE_CLASS[comparison.trend]}`}
            >
              <TrendIcon className="size-3.5" />
              {comparison.percentChange !== null
                ? `${comparison.percentChange >= 0 ? "+" : ""}${comparison.percentChange.toFixed(1)}%`
                : dict.revenueIntelligence.newBadge}
            </span>
            <span className="mb-1.5 text-sm text-muted-foreground">
              {dict.revenueIntelligence.currentMonthRevenue}
            </span>
          </div>
          <p className="mt-2 text-sm text-muted-foreground">{comparison.summary}</p>
        </div>

        {/* min-w-0 : Card est un flex-col (voir components/ui/card.tsx) et un enfant flex ne
            rétrécit jamais sous la largeur intrinsèque de son contenu par défaut - sans ça, le
            ResponsiveContainer de recharts (beaucoup de libellés sur 12 mois) forçait toute la
            page à déborder horizontalement sur mobile. */}
        <div className="min-w-0">
          <MonthlyChart data={seriesData} formatValue={formatValue} />
        </div>
      </CardContent>
    </Card>
  );
}
