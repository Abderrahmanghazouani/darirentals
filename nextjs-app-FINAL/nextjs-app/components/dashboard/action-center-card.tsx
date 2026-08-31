"use client";

import { useMemo } from "react";
import { useRouter } from "next/navigation";
import { AlertTriangle, CalendarClock, ChevronRight, PartyPopper } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { TaskDto } from "@/lib/types/Task";
import { ReservationRequestDto } from "@/lib/types/ReservationRequest";
import { ActionItem, computeActionItems } from "@/lib/dashboard/action-center";
import { useLanguage } from "@/lib/i18n/language-context";

const KIND_ICON: Record<ActionItem["kind"], typeof AlertTriangle> = {
  "overdue-task": AlertTriangle,
  "pending-request": CalendarClock,
};

const KIND_ICON_CLASS: Record<ActionItem["kind"], string> = {
  "overdue-task": "text-destructive-text",
  "pending-request": "text-warning",
};

const MAX_VISIBLE = 6;

interface ActionCenterCardProps {
  tasks: TaskDto[];
  reservationRequests: ReservationRequestDto[];
}

export function ActionCenterCard({ tasks, reservationRequests }: ActionCenterCardProps) {
  const router = useRouter();
  const { dict } = useLanguage();

  const items = useMemo(() => computeActionItems(tasks, reservationRequests), [tasks, reservationRequests]);
  const visibleItems = items.slice(0, MAX_VISIBLE);
  const remaining = items.length - visibleItems.length;

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between gap-3">
          <CardTitle>{dict.actionCenter.title}</CardTitle>
          {items.length > 0 && <Badge variant="outline">{items.length}</Badge>}
        </div>
      </CardHeader>
      <CardContent>
        {items.length === 0 ? (
          <div className="flex flex-col items-center gap-2 py-8 text-center">
            <PartyPopper className="size-8 text-success" />
            <p className="text-sm font-medium">{dict.actionCenter.allDoneTitle}</p>
            <p className="text-xs text-muted-foreground">{dict.actionCenter.allDoneSubtitle}</p>
          </div>
        ) : (
          <div className="space-y-1">
            {visibleItems.map((item) => {
              const Icon = KIND_ICON[item.kind];
              return (
                <button
                  key={item.id}
                  onClick={() => router.push(item.href)}
                  className="flex w-full items-center gap-3 rounded-md px-2 py-2 text-left text-sm hover:bg-accent transition-colors"
                >
                  <Icon className={`size-4 shrink-0 ${KIND_ICON_CLASS[item.kind]}`} />
                  <div className="min-w-0 flex-1">
                    <p className="font-medium truncate">{item.title}</p>
                    <p className="text-xs text-muted-foreground truncate">{item.subtitle}</p>
                  </div>
                  <ChevronRight className="size-4 shrink-0 text-muted-foreground" />
                </button>
              );
            })}
            {remaining > 0 && (
              <p className="pt-1 text-center text-xs text-muted-foreground">
                +{remaining} {remaining > 1 ? dict.actionCenter.andMoreMany : dict.actionCenter.andMoreOne}
              </p>
            )}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
