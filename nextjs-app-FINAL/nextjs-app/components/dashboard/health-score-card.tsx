"use client";

import { RadialBar, RadialBarChart, PolarAngleAxis } from "recharts";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  HealthScore,
  HealthScoreLevel,
  HEALTH_SCORE_LEVEL_LABEL,
} from "@/lib/dashboard/health-score";

const LEVEL_COLOR: Record<HealthScoreLevel, string> = {
  excellent: "#16a34a", // green-600
  good: "#84cc16", // lime-500
  watch: "#f59e0b", // amber-500
  critical: "#dc2626", // red-600
};

const LEVEL_BADGE_CLASS: Record<HealthScoreLevel, string> = {
  excellent: "bg-green-600/10 text-green-700 dark:text-green-400",
  good: "bg-lime-500/10 text-lime-700 dark:text-lime-400",
  watch: "bg-amber-500/10 text-amber-700 dark:text-amber-400",
  critical: "bg-red-600/10 text-red-700 dark:text-red-400",
};

function componentColor(score: number): string {
  if (score >= 85) return LEVEL_COLOR.excellent;
  if (score >= 65) return LEVEL_COLOR.good;
  if (score >= 45) return LEVEL_COLOR.watch;
  return LEVEL_COLOR.critical;
}

interface HealthScoreCardProps {
  score: HealthScore;
}

export function HealthScoreCard({ score }: HealthScoreCardProps) {
  const color = LEVEL_COLOR[score.level];
  const gaugeData = [{ name: "score", value: score.total, fill: color }];

  return (
    <Card>
      <CardHeader>
        <CardTitle>Santé du portefeuille</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col sm:flex-row items-center gap-6">
          <div className="relative shrink-0" style={{ width: 180, height: 180 }}>
            <RadialBarChart
              width={180}
              height={180}
              cx="50%"
              cy="50%"
              innerRadius="72%"
              outerRadius="100%"
              barSize={14}
              data={gaugeData}
              startAngle={90}
              endAngle={-270}
            >
              <PolarAngleAxis type="number" domain={[0, 100]} angleAxisId={0} tick={false} />
              <RadialBar
                background={{ fill: "var(--muted)" }}
                dataKey="value"
                cornerRadius={10}
                angleAxisId={0}
              />
            </RadialBarChart>
            <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
              <span className="text-4xl font-bold tabular-nums">{score.total}</span>
              <span
                className={`mt-1 rounded-full px-2 py-0.5 text-xs font-medium ${LEVEL_BADGE_CLASS[score.level]}`}
              >
                {HEALTH_SCORE_LEVEL_LABEL[score.level]}
              </span>
            </div>
          </div>

          <div className="w-full space-y-3">
            {score.components.map((c) => (
              <div key={c.key} title={c.detail}>
                <div className="flex items-center justify-between text-sm mb-1">
                  <span className="font-medium">
                    {c.label} <span className="text-muted-foreground font-normal">({Math.round(c.weight * 100)}%)</span>
                  </span>
                  <span className="tabular-nums text-muted-foreground">{c.score}/100</span>
                </div>
                <div className="h-2 w-full rounded-full bg-muted overflow-hidden">
                  <div
                    className="h-full rounded-full transition-[width]"
                    style={{ width: `${c.score}%`, backgroundColor: componentColor(c.score) }}
                  />
                </div>
                <p className="text-xs text-muted-foreground mt-1">{c.detail}</p>
              </div>
            ))}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
