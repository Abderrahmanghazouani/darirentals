"use client";

import { useEffect, useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Sparkles, RefreshCw } from "lucide-react";
import { AssistantFacts } from "@/lib/dashboard/ai-facts";
import { getMorningInsights, AssistantError } from "@/lib/ai-assistant-api";
import { Role } from "@/lib/api-client";

interface MorningInsightsCardProps {
  facts: AssistantFacts;
  role: Role;
}

/**
 * Insights du matin : 2-3 phrases générées par Gemini à partir du paquet "facts" déjà calculé
 * par le Dashboard (mêmes données que Health Score / Revenue Intelligence / Action Center).
 * Voir NOTES-ai-assistant.md — aucun chiffre n'est jamais inventé, Gemini ne fait que reformuler.
 */
export function MorningInsightsCard({ facts, role }: MorningInsightsCardProps) {
  const [message, setMessage] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  function load() {
    setLoading(true);
    setError(null);
    getMorningInsights(facts, role)
      .then((res) => setMessage(res.message))
      .catch((e) => {
        setError(
          e instanceof AssistantError
            ? e.message
            : "L'assistant n'a pas pu générer les insights du jour."
        );
      })
      .finally(() => setLoading(false));
  }

  // "facts" est déjà figé par le parent au moment où ce composant est monté (rendu uniquement
  // une fois le chargement du Dashboard terminé) - un seul appel au montage, pas de re-appel à
  // chaque frappe/re-render.
  // eslint-disable-next-line react-hooks/exhaustive-deps
  useEffect(load, []);

  return (
    <Card className="border-primary/20 bg-primary/5">
      <CardContent className="pt-6">
        <div className="flex items-start gap-3">
          <div className="flex size-8 shrink-0 items-center justify-center rounded-md bg-primary text-primary-foreground">
            <Sparkles className="size-4.5" />
          </div>
          <div className="flex-1 min-w-0 space-y-1">
            {loading ? (
              <p className="text-sm text-muted-foreground">L&apos;assistant prépare le résumé du jour...</p>
            ) : error ? (
              <div className="space-y-2">
                <p className="text-sm text-destructive">{error}</p>
                <Button type="button" variant="outline" size="sm" onClick={load}>
                  <RefreshCw className="size-3.5" /> Réessayer
                </Button>
              </div>
            ) : (
              <p className="text-sm leading-relaxed">{message}</p>
            )}
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
