"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { getCurrentRole } from "@/lib/auth";

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    const role = getCurrentRole();
    router.replace(role ? `/${role}` : "/login");
  }, [router]);

  return null;
}
