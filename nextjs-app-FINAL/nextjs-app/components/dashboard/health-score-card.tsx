"use client";

import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  HealthScore,
  HealthScoreLevel,
  HEALTH_SCORE_LEVEL_LABEL,
} from "@/lib/dashboard/health-score";
import { useLanguage } from "@/lib/i18n/language-context";

// Tokens de thème (palette DariRentals - voir app/globals.css) : Excellent = succès (teal),
// Bon et À surveiller = avertissement (ambre, 2 intensités de fond pour les distinguer),
// Critique = destructif (rouge doux).
const LEVEL_COLOR: Record<HealthScoreLevel, string> = {
  excellent: "var(--color-success)",
  good: "var(--color-warning)",
  watch: "var(--color-warning)",
  critical: "var(--color-destructive)",
};

const LEVEL_BADGE_CLASS: Record<HealthScoreLevel, string> = {
  excellent: "bg-success/10 text-success",
  good: "bg-warning/10 text-warning",
  watch: "bg-warning/20 text-warning",
  critical: "bg-destructive/10 text-destructive",
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

// Anneau de progression en SVG pur (stroke-dasharray/dashoffset) - technique standard,
// indépendante de recharts. Choisie après avoir constaté que le RadialBarChart de recharts
// v3 (layout "centric", axe d'angle numérique) ne rendait aucun secteur pour ce cas d'usage
// précis (jauge circulaire complète, valeur = angle) - comportement interne non documenté
// et trop fragile pour un simple indicateur visuel.
const GAUGE_SIZE = 180;
const GAUGE_STROKE = 14;
const GAUGE_RADIUS = (GAUGE_SIZE - GAUGE_STROKE) / 2;
const GAUGE_CIRCUMFERENCE = 2 * Math.PI * GAUGE_RADIUS;

export function HealthScoreCard({ score }: HealthScoreCardProps) {
  const { dict } = useLanguage();
  const color = LEVEL_COLOR[score.level];
  const dashOffset = GAUGE_CIRCUMFERENCE * (1 - score.total / 100);

  return (
    <Card>
      <CardHeader>
        <CardTitle>{dict.healthScore.title}</CardTitle>
      </CardHeader>
      <CardContent>
        <div className="flex flex-col sm:flex-row items-center gap-6">
          <div className="relative shrink-0" style={{ width: GAUGE_SIZE, height: GAUGE_SIZE }}>
            <svg width={GAUGE_SIZE} height={GAUGE_SIZE} viewBox={`0 0 ${GAUGE_SIZE} ${GAUGE_SIZE}`}>
              <circle
                cx={GAUGE_SIZE / 2}
                cy={GAUGE_SIZE / 2}
                r={GAUGE_RADIUS}
                fill="none"
                stroke="var(--muted)"
                strokeWidth={GAUGE_STROKE}
              />
              <circle
                cx={GAUGE_SIZE / 2}
                cy={GAUGE_SIZE / 2}
                r={GAUGE_RADIUS}
                fill="none"
                stroke={color}
                strokeWidth={GAUGE_STROKE}
                strokeLinecap="round"
                strokeDasharray={GAUGE_CIRCUMFERENCE}
                strokeDashoffset={dashOffset}
                transform={`rotate(-90 ${GAUGE_SIZE / 2} ${GAUGE_SIZE / 2})`}
                className="transition-[stroke-dashoffset] duration-500"
              />
            </svg>
            <div className="absolute inset-0 flex flex-col items-center justify-center pointer-events-none">
              <span className="text-4xl font-bold tabular-nums">{score.total}</span>
              <span
                className={`mt-1 rounded-full px-2 py-0.5 text-xs font-medium ${LEVEL_BADGE_CLASS[score.level]}`}
              >
                {HEALTH_SCORE_LEVEL_LABEL[score.level]}
              </span>
            </div>
          </div>

          {/* min-w-0 : cette colonne est un enfant flex (voir le conteneur parent) qui ne
              rétrécirait pas sous la largeur de son texte le plus long sans ça, cassant le
              layout mobile (même famille de bug que MonthlyChart dans RevenueIntelligenceCard). */}
          <div className="w-full min-w-0 space-y-3">
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
