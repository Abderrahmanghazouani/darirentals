"use client";

import { useMemo } from "react";
import {
  addMonths,
  eachDayOfInterval,
  endOfMonth,
  endOfWeek,
  format,
  isSameMonth,
  isWithinInterval,
  parseISO,
  startOfMonth,
  startOfWeek,
  subMonths,
} from "date-fns";
import { fr } from "date-fns/locale";
import { Button } from "@/components/ui/button";
import { ChevronLeft, ChevronRight, Plus } from "lucide-react";
import { ReservationDto } from "@/lib/types/Reservation";

interface ReservationCalendarProps {
  month: Date;
  onMonthChange: (month: Date) => void;
  reservations: ReservationDto[];
  onReservationClick: (reservation: ReservationDto) => void;
  onDayClick?: (day: Date) => void;
  loading?: boolean;
}

// Pastilles en couleur "primary" (palette DariRentals), avec de légères variations
// d'intensité pour distinguer les réservations sans sortir de la teinte de la marque.
const PALETTE = [
  "bg-primary/10 text-primary border-primary/20",
  "bg-primary/15 text-primary border-primary/25",
  "bg-secondary text-secondary-foreground border-primary/20",
  "bg-accent text-accent-foreground border-primary/25",
  "bg-primary/20 text-primary border-primary/30",
];

function colorFor(reservation: ReservationDto): string {
  const seed = reservation.reservationStatus?.id ?? reservation.id ?? 0;
  return PALETTE[seed % PALETTE.length];
}

function coversDay(reservation: ReservationDto, day: Date): boolean {
  if (!reservation.checkInDate || !reservation.checkOutDate) return false;
  const start = parseISO(reservation.checkInDate);
  const end = parseISO(reservation.checkOutDate);
  // Le jour de check-out n'est pas occupé (nuit non facturée ce jour-là).
  return day >= start && day < end;
}

export function ReservationCalendar({
  month,
  onMonthChange,
  reservations,
  onReservationClick,
  onDayClick,
  loading,
}: ReservationCalendarProps) {
  const days = useMemo(() => {
    const start = startOfWeek(startOfMonth(month), { weekStartsOn: 1 });
    const end = endOfWeek(endOfMonth(month), { weekStartsOn: 1 });
    return eachDayOfInterval({ start, end });
  }, [month]);

  const weekdayLabels = ["Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"];

  return (
    <div className="space-y-3">
      <div className="flex items-center justify-between">
        <Button variant="outline" size="icon" onClick={() => onMonthChange(subMonths(month, 1))}>
          <ChevronLeft className="size-4" />
        </Button>
        <h3 className="font-medium capitalize">{format(month, "MMMM yyyy", { locale: fr })}</h3>
        <Button variant="outline" size="icon" onClick={() => onMonthChange(addMonths(month, 1))}>
          <ChevronRight className="size-4" />
        </Button>
      </div>

      {loading ? (
        <p className="text-muted-foreground text-sm py-8 text-center">Chargement...</p>
      ) : (
        <div className="grid grid-cols-7 gap-px bg-border rounded-md overflow-hidden border">
          {weekdayLabels.map((w) => (
            <div key={w} className="bg-muted text-muted-foreground text-xs font-medium p-2 text-center">
              {w}
            </div>
          ))}
          {days.map((day) => {
            const dayReservations = reservations.filter((r) => coversDay(r, day));
            const inMonth = isSameMonth(day, month);
            return (
              <div
                key={day.toISOString()}
                onClick={() => onDayClick?.(day)}
                className={`bg-background min-h-[92px] p-1.5 space-y-1 ${
                  inMonth ? "" : "opacity-40"
                } ${onDayClick ? "cursor-pointer hover:bg-muted/50" : ""}`}
              >
                <div className="flex items-center justify-between">
                  <span className="text-xs text-muted-foreground">{format(day, "d")}</span>
                  {onDayClick && (
                    <Plus className="size-3 text-muted-foreground opacity-0 hover:opacity-100" />
                  )}
                </div>
                {dayReservations.map((r) => (
                  <button
                    key={r.id}
                    type="button"
                    onClick={(e) => {
                      e.stopPropagation();
                      onReservationClick(r);
                    }}
                    title={`${r.client?.fullName ?? "Client"} · ${r.checkInDate} → ${r.checkOutDate}`}
                    className={`w-full truncate text-left text-[11px] leading-tight rounded border px-1 py-0.5 ${colorFor(
                      r
                    )}`}
                  >
                    {r.client?.fullName ?? r.reference ?? "Réservation"}
                  </button>
                ))}
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
}
