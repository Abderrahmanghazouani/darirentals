"use client";

import { useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { ChevronRight } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { PropertyDto } from "@/lib/types/Property";
import { ReservationDto } from "@/lib/types/Reservation";
import { ChargeDto } from "@/lib/types/Charge";
import {
  computePropertyPerformance,
  sortPropertyPerformance,
  PropertyPerformanceSort,
} from "@/lib/dashboard/property-performance";
import { useLanguage } from "@/lib/i18n/language-context";
import { Dict } from "@/lib/i18n/translations";

function sortLabels(dict: Dict): Record<PropertyPerformanceSort, string> {
  return {
    netProfit: dict.propertyPerformance.sortNetProfit,
    revenue: dict.propertyPerformance.sortRevenue,
    occupancy: dict.propertyPerformance.sortOccupancy,
  };
}

const ACTIVE_STATUS_CODE = "Active";

interface PropertyPerformanceCardProps {
  properties: PropertyDto[];
  reservations: ReservationDto[];
  charges: ChargeDto[];
  formatValue: (value: number) => string;
}

export function PropertyPerformanceCard({
  properties,
  reservations,
  charges,
  formatValue,
}: PropertyPerformanceCardProps) {
  const router = useRouter();
  const { dict } = useLanguage();
  const [sort, setSort] = useState<PropertyPerformanceSort>("netProfit");

  const rows = useMemo(() => {
    const computed = computePropertyPerformance(properties, reservations, charges);
    return sortPropertyPerformance(computed, sort);
  }, [properties, reservations, charges, sort]);

  const sortLabel = sortLabels(dict);

  return (
    <Card>
      <CardHeader>
        <div className="flex flex-wrap items-center justify-between gap-3">
          <CardTitle>{dict.propertyPerformance.title}</CardTitle>
          <Select value={sort} onValueChange={(v) => setSort(v as PropertyPerformanceSort)}>
            <SelectTrigger size="sm" className="w-[160px]">
              <SelectValue />
            </SelectTrigger>
            <SelectContent>
              {(Object.keys(sortLabel) as PropertyPerformanceSort[]).map((key) => (
                <SelectItem key={key} value={key}>
                  {dict.propertyPerformance.sortByPrefix} {sortLabel[key]}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
        </div>
      </CardHeader>
      <CardContent>
        {rows.length === 0 ? (
          <p className="text-sm text-muted-foreground text-center py-8">
            {dict.propertyPerformance.noProperties}
          </p>
        ) : (
          <div className="overflow-x-auto">
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>{dict.propertyPerformance.columnProperty}</TableHead>
                  <TableHead className="text-right">{dict.propertyPerformance.columnRevenue}</TableHead>
                  <TableHead className="text-right">{dict.propertyPerformance.columnCharges}</TableHead>
                  <TableHead className="text-right">{dict.propertyPerformance.columnNetProfit}</TableHead>
                  <TableHead className="text-right">{dict.propertyPerformance.columnOccupancy}</TableHead>
                  <TableHead className="w-8" />
                </TableRow>
              </TableHeader>
              <TableBody>
                {rows.map((r) => (
                  <TableRow
                    key={r.propertyId}
                    className="cursor-pointer hover:bg-accent/50"
                    onClick={() => router.push(`/admin/property/${r.propertyId}/rentabilite`)}
                  >
                    <TableCell className="font-medium">
                      <div className="flex min-w-0 items-center gap-2">
                        <span className="max-w-[160px] truncate sm:max-w-[240px]" title={r.propertyName}>
                          {r.propertyName}
                        </span>
                        {r.propertyStatusCode !== ACTIVE_STATUS_CODE && (
                          <Badge variant="outline" className="shrink-0 text-xs font-normal">
                            {r.propertyStatusLabel ?? dict.propertyPerformance.unknownStatus}
                          </Badge>
                        )}
                      </div>
                    </TableCell>
                    <TableCell className="text-right whitespace-nowrap">{formatValue(r.revenue)}</TableCell>
                    <TableCell className="text-right whitespace-nowrap">{formatValue(r.charges)}</TableCell>
                    <TableCell
                      className={`text-right whitespace-nowrap font-semibold ${r.netProfit < 0 ? "text-destructive-text" : ""}`}
                    >
                      {formatValue(r.netProfit)}
                    </TableCell>
                    <TableCell className="text-right whitespace-nowrap">
                      <div className="flex items-center justify-end gap-2">
                        <div className="h-1.5 w-14 rounded-full bg-muted overflow-hidden hidden sm:block">
                          <div
                            className="h-full rounded-full bg-primary"
                            style={{ width: `${r.occupancyPercent}%` }}
                          />
                        </div>
                        <span className="tabular-nums text-muted-foreground text-sm">{r.occupancyPercent}%</span>
                      </div>
                    </TableCell>
                    <TableCell className="text-muted-foreground">
                      <ChevronRight className="size-4" />
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </div>
        )}
      </CardContent>
    </Card>
  );
}
