"use client";

import { useEffect, useState } from "react";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Sparkles, RefreshCw } from "lucide-react";
import { AssistantFacts } from "@/lib/dashboard/ai-facts";
import { getMorningInsights, AssistantError } from "@/lib/ai-assistant-api";
import { Role } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/language-context";

interface MorningInsightsCardProps {
  facts: AssistantFacts;
  role: Role;
}

/**
 * Insights du matin : 2-3 phrases générées par Gemini à partir du paquet "facts" déjà calculé
 * par le Dashboard (mêmes données que Health Score / Revenue Intelligence / Action Center).
 * Voir NOTES-ai-assistant.md — aucun chiffre n'est jamais inventé, Gemini ne fait que reformuler.
 * Seul le CHROME fixe de cette carte (chargement, erreur, bouton) suit la langue choisie - le
 * message généré par Gemini reste en français (voir NOTES-ai-assistant.md).
 */
export function MorningInsightsCard({ facts, role }: MorningInsightsCardProps) {
  const { dict } = useLanguage();
  const [message, setMessage] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  // Comme pour /login et /reserver : on stocke le message serveur brut à part du texte de
  // secours traduit, pour que ce dernier se retraduise si l'utilisateur change de langue après
  // avoir vu l'erreur (voir NOTES-multi-langue.md).
  const [serverError, setServerError] = useState<string | null>(null);
  const [hasGenericError, setHasGenericError] = useState(false);
  const error = serverError ?? (hasGenericError ? dict.assistant.insightsError : null);

  function load() {
    setLoading(true);
    setServerError(null);
    setHasGenericError(false);
    getMorningInsights(facts, role)
      .then((res) => setMessage(res.message))
      .catch((e) => {
        if (e instanceof AssistantError && e.message) {
          setServerError(e.message);
        } else {
          setHasGenericError(true);
        }
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
              <p className="text-sm text-muted-foreground">{dict.assistant.insightsLoading}</p>
            ) : error ? (
              <div className="space-y-2">
                <p className="text-sm text-destructive">{error}</p>
                <Button type="button" variant="outline" size="sm" onClick={load}>
                  <RefreshCw className="size-3.5" /> {dict.assistant.retry}
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
