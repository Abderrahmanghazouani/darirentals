"use client";

import { useEffect, useState } from "react";
import { jwtDecode } from "jwt-decode";
import { getEntityClients } from "./api";
import { getStoredToken, DecodedToken } from "./auth";
import { Role } from "./api-client";
import { CollaboratorDto } from "./types/Collaborator";

/**
 * Retourne l'id du collaborateur connecté (ou null tant que non résolu / si non applicable).
 * Le backend n'expose pas encore d'endpoint "/me" : on retrouve le profil en filtrant
 * la liste des collaborateurs par le username décodé du token (même approche que
 * select-enterprise/page.tsx).
 */
export function useCurrentCollaboratorId(role: Role): number | null {
  const [id, setId] = useState<number | null>(null);

  useEffect(() => {
    if (role !== "collaborator") return;
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
        if (me?.id != null) setId(me.id);
      })
      .catch(() => {
        // pas bloquant : l'usage IA sera juste journalisé sans collaborateur attribué
      });

    return () => {
      cancelled = true;
    };
  }, [role]);

  return id;
}
