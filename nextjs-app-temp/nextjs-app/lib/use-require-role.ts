"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { getCurrentRole } from "./auth";
import { Role } from "./api-client";

/** Protège une page : redirige vers /login si non connecté ou si le rôle ne correspond pas. */
export function useRequireRole(expectedRole: Role) {
  const router = useRouter();
  const [checked, setChecked] = useState(false);

  useEffect(() => {
    const role = getCurrentRole();
    if (role !== expectedRole) {
      router.replace("/login");
      return;
    }
    setChecked(true);
  }, [expectedRole, router]);

  return checked;
}
