"use client";

import { jwtDecode } from "jwt-decode";
import { Role } from "./api-client";

const TOKEN_KEY = "token"; // stocké sous la forme "Bearer <jwt>", comme l'ancien frontend Angular

// L'endpoint /login est à la racine du backend (pas sous /api/), ex: http://localhost:8036/login
const AUTH_HOST = (process.env.NEXT_PUBLIC_AUTH_HOST ?? "http://localhost:8036").replace(/\/$/, "");

export interface DecodedToken {
  sub: string; // username
  roles: string[]; // ex: ["ROLE_ADMIN"]
  email?: string;
  firstName?: string;
  lastName?: string;
  exp: number;
}

export interface LoginResult {
  token: string; // "Bearer xxx"
  role: Role;
  username: string;
}

/** Convertit une autorité Spring ("ROLE_ADMIN") en clé de route ("admin"). */
function roleFromAuthority(authority: string): Role | null {
  const key = authority.replace("ROLE_", "").toLowerCase();
  if (key === "admin" || key === "collaborator") return key;
  return null;
}

export async function login(username: string, password: string): Promise<LoginResult> {
  const res = await fetch(`${AUTH_HOST}/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ username, password }),
  });

  if (!res.ok) {
    let message = "Échec de la connexion";
    try {
      const data = await res.json();
      message = data?.message || data?.error || (typeof data === "string" ? data : message);
    } catch {
      // réponse non-JSON, on garde le message par défaut
    }
    throw new Error(message);
  }

  const data = await res.json();
  const rawToken: string = data.token ?? data.accessToken;
  const bearer = `Bearer ${rawToken}`;

  const decoded = jwtDecode<DecodedToken>(rawToken);
  const role = decoded.roles?.map(roleFromAuthority).find((r) => r != null);
  if (!role) {
    throw new Error("Rôle non reconnu pour cet utilisateur.");
  }

  localStorage.setItem(TOKEN_KEY, bearer);
  return { token: bearer, role, username: decoded.sub };
}

export function logout() {
  localStorage.removeItem(TOKEN_KEY);
}

export function getStoredToken(): string | null {
  if (typeof window === "undefined") return null;
  return localStorage.getItem(TOKEN_KEY);
}

/** Retourne le rôle courant (déduit du token stocké), ou null si non connecté / token expiré. */
export function getCurrentRole(): Role | null {
  const token = getStoredToken();
  if (!token) return null;
  const raw = token.replace(/^Bearer\s+/, "");
  try {
    const decoded = jwtDecode<DecodedToken>(raw);
    if (decoded.exp * 1000 < Date.now()) {
      logout();
      return null;
    }
    const role = decoded.roles?.map(roleFromAuthority).find((r) => r != null);
    return role ?? null;
  } catch {
    return null;
  }
}
