"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Building2 } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { login, LoginError } from "@/lib/auth";
import { LanguageToggle } from "@/components/i18n/language-toggle";
import { useLanguage } from "@/lib/i18n/language-context";

export default function LoginPage() {
  const router = useRouter();
  const { dict } = useLanguage();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  // Stocke la NATURE de l'erreur, pas un texte déjà résolu : si l'utilisateur bascule la
  // langue après avoir vu une erreur, le message doit se retraduire, pas rester figé dans
  // l'ancienne langue (le message serveur brut, lui, reste tel quel - pas de traduction
  // possible côté frontend pour un texte qui vient du backend).
  const [errorKind, setErrorKind] = useState<"unrecognizedRole" | "server" | "generic" | null>(null);
  const [serverErrorMessage, setServerErrorMessage] = useState("");
  const error =
    errorKind === "unrecognizedRole"
      ? dict.login.unrecognizedRoleError
      : errorKind === "server"
        ? serverErrorMessage
        : errorKind === "generic"
          ? dict.login.genericError
          : null;

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    setLoading(true);
    setErrorKind(null);
    try {
   const result = await login(username, password);
      if (result.role === "collaborator") {
        router.push("/select-enterprise");
      } else {
        router.push(`/${result.role}`);
      }
    } catch (err) {
      if (err instanceof LoginError) {
        if (err.code === "unrecognized_role") {
          setErrorKind("unrecognizedRole");
        } else if (err.message) {
          setErrorKind("server");
          setServerErrorMessage(err.message);
        } else {
          setErrorKind("generic");
        }
      } else {
        setErrorKind("generic");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="relative min-h-screen flex flex-col items-center justify-center gap-6 p-6">
      <div className="absolute top-6 right-6">
        <LanguageToggle />
      </div>
      <div className="flex items-center gap-2.5">
        <div className="flex size-8 items-center justify-center rounded-md bg-primary text-primary-foreground">
          <Building2 className="size-4.5" />
        </div>
        <span className="text-lg font-semibold tracking-tight">DariRentals</span>
      </div>
      <Card className="w-full max-w-sm">
        <CardHeader>
          <CardTitle>{dict.login.title}</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <Label htmlFor="username">{dict.login.username}</Label>
              <Input
                id="username"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                autoFocus
                required
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="password">{dict.login.password}</Label>
              <Input
                id="password"
                type="password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                required
              />
            </div>
            {error && <p className="text-sm text-destructive">{error}</p>}
            <Button type="submit" className="w-full" disabled={loading}>
              {loading ? dict.login.submitting : dict.login.submit}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  );
}
