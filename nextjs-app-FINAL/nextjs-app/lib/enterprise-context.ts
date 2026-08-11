"use client";

const KEY = "selectedEnterpriseId";

export function getSelectedEnterpriseId(): number | null {
  if (typeof window === "undefined") return null;
  const raw = localStorage.getItem(KEY);
  return raw ? Number(raw) : null;
}

export function setSelectedEnterpriseId(id: number) {
  localStorage.setItem(KEY, String(id));
}

export function clearSelectedEnterprise() {
  localStorage.removeItem(KEY);
}