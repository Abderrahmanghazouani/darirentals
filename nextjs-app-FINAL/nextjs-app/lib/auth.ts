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

/**
 * Erreur de login typée : "unrecognized_role" n'a jamais de texte utilisateur codé en dur ici
 * (voir LanguageProvider) - c'est à l'appelant (ex: /login) d'afficher son propre message
 * traduit pour ce cas précis. "server" porte le message brut renvoyé par le serveur, le cas
 * échéant (peut être vide - voir login() ci-dessous).
 */
export class LoginError extends Error {
  code: "server" | "unrecognized_role";
  constructor(code: "server" | "unrecognized_role", serverMessage = "") {
    super(serverMessage);
    this.code = code;
  }
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
    // Pas de message par défaut ici : un texte codé en dur ne suivrait pas la langue choisie
    // par l'utilisateur (voir LanguageProvider). Message vide = "pas de raison utilisable
    // renvoyée par le serveur" ; c'est à l'appelant (ex: /login) d'afficher son propre texte
    // par défaut traduit dans ce cas.
    let message = "";
    try {
      const data = await res.json();
      message = data?.message || data?.error || (typeof data === "string" ? data : "");
    } catch {
      // réponse non-JSON (ex: message brut de Spring Security) : pas de message utilisable.
    }
    throw new LoginError("server", message);
  }

  const data = await res.json();
  const rawToken: string = data.token ?? data.accessToken;
  const bearer = `Bearer ${rawToken}`;

  const decoded = jwtDecode<DecodedToken>(rawToken);
  const role = decoded.roles?.map(roleFromAuthority).find((r) => r != null);
  if (!role) {
    throw new LoginError("unrecognized_role");
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

export interface CurrentUser {
  username: string;
  email?: string;
  firstName?: string;
  lastName?: string;
}

/** Retourne les infos de l'utilisateur connecté (déduites du token stocké), ou null. */
export function getCurrentUser(): CurrentUser | null {
  const token = getStoredToken();
  if (!token) return null;
  const raw = token.replace(/^Bearer\s+/, "");
  try {
    const decoded = jwtDecode<DecodedToken>(raw);
    if (decoded.exp * 1000 < Date.now()) return null;
    return {
      username: decoded.sub,
      email: decoded.email,
      firstName: decoded.firstName,
      lastName: decoded.lastName,
    };
  } catch {
    return null;
  }
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
