"use client";

import { useMemo } from "react";
import { useRouter } from "next/navigation";
import { ClipboardList, Coffee, LogIn, LogOut } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";
import { ReservationDto } from "@/lib/types/Reservation";
import { TaskDto } from "@/lib/types/Task";
import { computeDayTimeline, type TimelineEvent, type TimelineEventType } from "@/lib/dashboard/day-timeline";
import { useLanguage } from "@/lib/i18n/language-context";

const TYPE_ICON: Record<TimelineEventType, typeof LogIn> = {
  arrival: LogIn,
  departure: LogOut,
  task: ClipboardList,
};

// Couleurs déjà calibrées pour le contraste en sombre (voir NOTES-mode-sombre.md) - une pastille
// + une icône teintée par type d'événement, cohérent avec ActionCenterCard.
const TYPE_DOT_CLASS: Record<TimelineEventType, string> = {
  arrival: "bg-success",
  departure: "bg-info",
  task: "bg-warning",
};

const TYPE_ICON_CLASS: Record<TimelineEventType, string> = {
  arrival: "text-success",
  departure: "text-info",
  task: "text-warning",
};

interface DayTimelineCardProps {
  reservations: ReservationDto[];
  tasks: TaskDto[];
  role?: "admin" | "collaborator";
}

export function DayTimelineCard({ reservations, tasks, role = "admin" }: DayTimelineCardProps) {
  const router = useRouter();
  const { dict } = useLanguage();

  const events = useMemo(
    () => computeDayTimeline(reservations, tasks, role),
    [reservations, tasks, role]
  );

  const typeLabel: Record<TimelineEventType, string> = {
    arrival: dict.dayTimeline.arrivalLabel,
    departure: dict.dayTimeline.departureLabel,
    task: dict.dayTimeline.taskLabel,
  };

  return (
    // min-w-0 : voir le commentaire équivalent dans property-map-card.tsx - même item de grille,
    // même contrainte.
    <Card className="h-full min-w-0">
      <CardContent>
        {events.length === 0 ? (
          <div className="flex h-[220px] flex-col items-center justify-center gap-2 text-center">
            <Coffee className="size-8 text-muted-foreground" />
            <p className="text-sm font-medium">{dict.dayTimeline.emptyTitle}</p>
            <p className="text-xs text-muted-foreground">{dict.dayTimeline.emptySubtitle}</p>
          </div>
        ) : (
          <ul className="space-y-1">
            {events.map((event: TimelineEvent) => {
              const Icon = TYPE_ICON[event.type];
              return (
                <li key={event.id}>
                  <button
                    type="button"
                    onClick={() => router.push(event.href)}
                    className="flex w-full items-center gap-3 rounded-md px-2 py-2 text-left text-sm transition-colors hover:bg-accent"
                  >
                    <span className={`size-1.5 shrink-0 rounded-full ${TYPE_DOT_CLASS[event.type]}`} />
                    <Icon className={`size-4 shrink-0 ${TYPE_ICON_CLASS[event.type]}`} />
                    <span className="min-w-0 flex-1">
                      <span className="block truncate font-medium">{event.propertyName}</span>
                      <span className="block truncate text-xs text-muted-foreground">
                        {typeLabel[event.type]} · {event.title}
                      </span>
                    </span>
                  </button>
                </li>
              );
            })}
          </ul>
        )}
      </CardContent>
    </Card>
  );
}
