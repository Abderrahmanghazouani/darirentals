"use client";

import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { MessageCircleQuestion, Loader2 } from "lucide-react";
import { AssistantFacts } from "@/lib/dashboard/ai-facts";
import { askPortfolioQuestion, AssistantError } from "@/lib/ai-assistant-api";
import { Role } from "@/lib/api-client";
import { useLanguage } from "@/lib/i18n/language-context";

interface PortfolioChatCardProps {
  facts: AssistantFacts;
  role: Role;
}

interface Exchange {
  question: string;
  answer: string | null;
  // Comme pour /login, /reserver et MorningInsightsCard : la nature de l'erreur est stockée,
  // pas un texte déjà traduit, pour qu'un changement de langue retraduise aussi l'historique
  // (voir NOTES-multi-langue.md).
  serverError: string | null;
  hasGenericError: boolean;
}

/**
 * "Pose une question à ton portefeuille" : Gemini répond UNIQUEMENT à partir du paquet
 * "facts" déjà calculé par le Dashboard (voir NOTES-ai-assistant.md) — jamais de requête
 * libre vers la base, jamais un chiffre inventé. Seul le CHROME fixe (titre, placeholder,
 * bouton, messages système) suit la langue choisie - les réponses de Gemini restent en
 * français (voir NOTES-ai-assistant.md).
 */
export function PortfolioChatCard({ facts, role }: PortfolioChatCardProps) {
  const { dict } = useLanguage();
  const [question, setQuestion] = useState("");
  const [asking, setAsking] = useState(false);
  const [exchanges, setExchanges] = useState<Exchange[]>([]);

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    const q = question.trim();
    if (!q || asking) return;
    setAsking(true);
    setQuestion("");
    try {
      const res = await askPortfolioQuestion(facts, q, role);
      setExchanges((prev) => [
        { question: q, answer: res.message, serverError: null, hasGenericError: false },
        ...prev,
      ]);
    } catch (e) {
      const serverError = e instanceof AssistantError && e.message ? e.message : null;
      setExchanges((prev) => [
        { question: q, answer: null, serverError, hasGenericError: !serverError },
        ...prev,
      ]);
    } finally {
      setAsking(false);
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-base">
          <MessageCircleQuestion className="size-4.5" /> {dict.assistant.chatTitle}
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <form onSubmit={handleSubmit} className="flex gap-2">
          <Input
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            placeholder={dict.assistant.chatPlaceholder}
            disabled={asking}
          />
          <Button type="submit" disabled={asking || !question.trim()}>
            {asking ? <Loader2 className="size-4 animate-spin" /> : dict.assistant.chatButton}
          </Button>
        </form>

        {exchanges.length === 0 ? (
          <p className="text-sm text-muted-foreground">{dict.assistant.chatHint}</p>
        ) : (
          <div className="space-y-3 max-h-80 overflow-y-auto pr-1">
            {exchanges.map((ex, i) => (
              <div key={i} className="space-y-1 border-b pb-3 last:border-0 last:pb-0">
                <p className="text-sm font-medium">{ex.question}</p>
                {ex.serverError || ex.hasGenericError ? (
                  <p className="text-sm text-destructive-text">
                    {ex.serverError ?? dict.assistant.chatError}
                  </p>
                ) : (
                  <p className="text-sm text-muted-foreground leading-relaxed">{ex.answer}</p>
                )}
              </div>
            ))}
          </div>
        )}
      </CardContent>
    </Card>
  );
}
