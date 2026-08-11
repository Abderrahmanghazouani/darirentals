"use client";

import { useEffect, useState } from "react";
import { getSelectedEnterpriseId } from "./enterprise-context";

/** Retourne l'id de la société actuellement sélectionnée (ou null si aucune / pas encore chargée). */
export function useSelectedEnterpriseId(): number | null {
  const [id, setId] = useState<number | null>(null);

  useEffect(() => {
    setId(getSelectedEnterpriseId());
  }, []);

  return id;
}