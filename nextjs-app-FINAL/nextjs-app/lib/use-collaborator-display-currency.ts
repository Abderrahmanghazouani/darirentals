"use client";

import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { getEntityClients } from "./api";
import { getStoredToken, DecodedToken } from "./auth";
import { CollaboratorDto } from "./types/Collaborator";

/**
 * Devise d'affichage préférée (Collaborator.displayCurrency.code) du collaborateur
 * actuellement connecté, ou null tant que non résolu / non applicable (admin, invité,
 * collaborateur sans préférence définie). À passer en `defaultCode` à <CurrencyProvider> :
 * elle ne prime que si l'utilisateur n'a pas déjà choisi une devise explicitement
 * (voir lib/currency/currency-context.tsx).
 *
 * Même limitation que useCurrentCollaboratorId : pas d'endpoint "/me" côté backend,
 * on retrouve le profil en filtrant par le username décodé du token.
 */
export function useCollaboratorDisplayCurrencyCode(): string | null {
  const [code, setCode] = useState<string | null>(null);

  useEffect(() => {
    const token = getStoredToken();
    if (!token) return;

    let username: string;
    try {
      username = jwtDecode<DecodedToken>(token.replace(/^Bearer\s+/, "")).sub;
    } catch {
      return;
    }

    let cancelled = false;
    getEntityClients("collaborator")
      .collaborator.findAll()
      .then((all: CollaboratorDto[]) => {
        if (cancelled) return;
        const me = (all ?? []).find((c) => c.username === username);
        const preferred = me?.displayCurrency?.code;
        if (preferred) setCode(preferred);
      })
      .catch(() => {
        // pas bloquant : le sélecteur retombe simplement sur MAD par défaut
      });

    return () => {
      cancelled = true;
    };
  }, []);

  return code;
}
