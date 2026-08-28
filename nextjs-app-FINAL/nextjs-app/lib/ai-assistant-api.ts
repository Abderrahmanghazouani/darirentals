import { API_BASE, authHeaders, Role } from "./api-client";
import { AssistantFacts } from "./dashboard/ai-facts";

export interface AssistantResponse {
  message: string;
  tokensUsed?: number | null;
}

export class AssistantError extends Error {
  status: number;
  constructor(message: string, status: number) {
    super(message);
    this.status = status;
  }
}

async function post(path: string, role: Role, body: unknown): Promise<AssistantResponse> {
  const res = await fetch(`${API_BASE}${role}/property-assistant/${path}`, {
    method: "POST",
    headers: { "Content-Type": "application/json", ...authHeaders() },
    body: JSON.stringify(body),
  });

  if (!res.ok) {
    let message = `Erreur ${res.status}`;
    try {
      const data = await res.json();
      if (data?.message) message = data.message;
    } catch {
      // corps non-JSON, on garde le message générique
    }
    throw new AssistantError(message, res.status);
  }

  return res.json();
}

/** Génère les 2-3 phrases d'insights du matin à partir du paquet de faits déjà calculé. */
export async function getMorningInsights(
  facts: AssistantFacts,
  role: Role,
  opts: { enterpriseId?: number | null } = {}
): Promise<AssistantResponse> {
  return post("insights", role, { facts, enterpriseId: opts.enterpriseId ?? null });
}

/** Pose une question sur le portefeuille — Gemini répond UNIQUEMENT à partir de "facts". */
export async function askPortfolioQuestion(
  facts: AssistantFacts,
  question: string,
  role: Role,
  opts: { enterpriseId?: number | null } = {}
): Promise<AssistantResponse> {
  return post("chat", role, { facts, question, enterpriseId: opts.enterpriseId ?? null });
}
