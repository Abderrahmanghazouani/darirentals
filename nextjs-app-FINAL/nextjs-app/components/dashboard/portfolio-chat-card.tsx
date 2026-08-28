"use client";

import { useState } from "react";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { MessageCircleQuestion, Loader2 } from "lucide-react";
import { AssistantFacts } from "@/lib/dashboard/ai-facts";
import { askPortfolioQuestion, AssistantError } from "@/lib/ai-assistant-api";
import { Role } from "@/lib/api-client";

interface PortfolioChatCardProps {
  facts: AssistantFacts;
  role: Role;
}

interface Exchange {
  question: string;
  answer: string | null;
  error: string | null;
}

/**
 * "Pose une question à ton portefeuille" : Gemini répond UNIQUEMENT à partir du paquet
 * "facts" déjà calculé par le Dashboard (voir NOTES-ai-assistant.md) — jamais de requête
 * libre vers la base, jamais un chiffre inventé.
 */
export function PortfolioChatCard({ facts, role }: PortfolioChatCardProps) {
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
      setExchanges((prev) => [{ question: q, answer: res.message, error: null }, ...prev]);
    } catch (e) {
      const message =
        e instanceof AssistantError ? e.message : "L'assistant n'a pas pu répondre pour le moment.";
      setExchanges((prev) => [{ question: q, answer: null, error: message }, ...prev]);
    } finally {
      setAsking(false);
    }
  }

  return (
    <Card>
      <CardHeader>
        <CardTitle className="flex items-center gap-2 text-base">
          <MessageCircleQuestion className="size-4.5" /> Pose une question à ton portefeuille
        </CardTitle>
      </CardHeader>
      <CardContent className="space-y-4">
        <form onSubmit={handleSubmit} className="flex gap-2">
          <Input
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            placeholder="Ex : combien j'ai gagné ce mois-ci ?"
            disabled={asking}
          />
          <Button type="submit" disabled={asking || !question.trim()}>
            {asking ? <Loader2 className="size-4 animate-spin" /> : "Demander"}
          </Button>
        </form>

        {exchanges.length === 0 ? (
          <p className="text-sm text-muted-foreground">
            Pose une question sur tes revenus, charges, réservations à venir ou tâches en retard —
            l&apos;assistant répond uniquement à partir des vraies données de ton portefeuille.
          </p>
        ) : (
          <div className="space-y-3 max-h-80 overflow-y-auto pr-1">
            {exchanges.map((ex, i) => (
              <div key={i} className="space-y-1 border-b pb-3 last:border-0 last:pb-0">
                <p className="text-sm font-medium">{ex.question}</p>
                {ex.error ? (
                  <p className="text-sm text-destructive">{ex.error}</p>
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
