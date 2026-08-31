"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import {
  ArrowLeft,
  ArrowRight,
  Building2,
  CheckCircle2,
  Eye,
  EyeOff,
  LockKeyhole,
  ShieldCheck,
  Sparkles,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { login, LoginError } from "@/lib/auth";
import { LanguageToggle } from "@/components/i18n/language-toggle";
import { ThemeToggle } from "@/components/theme/theme-toggle";
import { useLanguage } from "@/lib/i18n/language-context";

export default function LoginPage() {
  const router = useRouter();
  const { dict } = useLanguage();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [showPassword, setShowPassword] = useState(false);
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
    <main className="min-h-screen bg-background">
      <div className="grid min-h-screen lg:grid-cols-[1.05fr_0.95fr]">

        {/* =====================================================
            LEFT — BRANDING
        ===================================================== */}

        <section className="relative hidden overflow-hidden bg-primary lg:flex lg:flex-col lg:justify-between">

          {/* Decorative background */}
          <div className="pointer-events-none absolute inset-0">
            <div className="absolute -left-32 -top-32 size-[500px] rounded-full bg-white/[0.06] blur-3xl" />
            <div className="absolute -bottom-40 -right-20 size-[500px] rounded-full bg-white/[0.06] blur-3xl" />

            <div className="absolute inset-0 opacity-[0.035]">
              <svg
                className="h-full w-full"
                viewBox="0 0 100 100"
                preserveAspectRatio="none"
              >
                <defs>
                  <pattern
                    id="grid"
                    width="8"
                    height="8"
                    patternUnits="userSpaceOnUse"
                  >
                    <path
                      d="M 8 0 L 0 0 0 8"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="0.5"
                    />
                  </pattern>
                </defs>

                <rect
                  width="100%"
                  height="100%"
                  fill="url(#grid)"
                />
              </svg>
            </div>
          </div>

          {/* Logo */}
          <div className="relative z-10 px-10 pt-10 xl:px-14 xl:pt-12">
            <div className="flex items-center gap-3">
              <span className="flex size-10 items-center justify-center rounded-xl bg-primary-foreground/10 text-primary-foreground ring-1 ring-primary-foreground/10">
                <Building2 className="size-5" />
              </span>

              <span className="text-xl font-bold tracking-tight text-primary-foreground">
                DariRentals
              </span>
            </div>
          </div>

          {/* Main message */}
          <div className="relative z-10 px-10 xl:px-14">

            <div className="mb-6 inline-flex items-center gap-2 rounded-full bg-primary-foreground/10 px-3.5 py-2 text-xs font-medium text-primary-foreground/90 ring-1 ring-primary-foreground/10">
              <Sparkles className="size-3.5" />
              {dict.login.brandBadge}
            </div>

            <h1 className="max-w-xl text-4xl font-bold leading-[1.08] tracking-tight text-primary-foreground xl:text-5xl">
              {dict.login.brandTitleLine1}
              <br />
              <span className="text-primary-foreground/65">
                {dict.login.brandTitleLine2}
              </span>
            </h1>

            <p className="mt-6 max-w-lg text-base leading-7 text-primary-foreground/70 xl:text-lg">
              {dict.login.brandSubtitle}
            </p>

            {/* Benefits */}
            <div className="mt-9 space-y-4">
              <LoginBenefit text={dict.login.benefitMultiCompany} />
              <LoginBenefit text={dict.login.benefitIsolation} />
              <LoginBenefit text={dict.login.benefitRealtime} />
            </div>
          </div>

          {/* Bottom */}
          <div className="relative z-10 px-10 pb-10 xl:px-14 xl:pb-12">
            <div className="flex items-center gap-3 border-t border-primary-foreground/10 pt-6">
              <ShieldCheck className="size-5 text-primary-foreground/60" />

              <p className="text-xs leading-5 text-primary-foreground/60">
                {dict.login.brandFooter}
              </p>
            </div>
          </div>
        </section>

        {/* =====================================================
            RIGHT — LOGIN
        ===================================================== */}

        <section className="relative flex min-h-screen flex-col">

          {/* Mobile header */}
          <div className="flex items-center justify-between p-5 sm:p-7 lg:hidden">
            <a
              href="/"
              className="flex items-center gap-2.5"
            >
              <span className="flex size-9 items-center justify-center rounded-lg bg-primary text-primary-foreground">
                <Building2 className="size-4.5" />
              </span>

              <span className="font-bold tracking-tight text-primary">
                DariRentals
              </span>
            </a>

            <div className="flex items-center gap-2">
              <ThemeToggle />
              <LanguageToggle />
            </div>
          </div>

          {/* Desktop top-right — langue + retour */}
          <div className="absolute right-8 top-8 hidden items-center gap-4 lg:flex">
            <div className="flex items-center gap-2">
              <ThemeToggle />
              <LanguageToggle />
            </div>

            <a
              href="/"
              className="group flex items-center gap-1.5 text-sm text-muted-foreground transition-colors hover:text-foreground"
            >
              <ArrowLeft className="size-4 transition-transform group-hover:-translate-x-0.5" />
              {dict.login.backHome}
            </a>
          </div>

          {/* Form container */}
          <div className="flex flex-1 items-center justify-center px-5 py-10 sm:px-8">
            <div className="w-full max-w-[420px]">

              {/* Heading */}
              <div className="mb-9">
                <div className="mb-5 flex size-11 items-center justify-center rounded-xl bg-accent text-accent-foreground">
                  <LockKeyhole className="size-5" />
                </div>

                <h2 className="text-3xl font-bold tracking-tight sm:text-4xl">
                  {dict.login.welcomeTitle}
                </h2>

                <p className="mt-3 text-sm leading-6 text-muted-foreground sm:text-base">
                  {dict.login.welcomeSubtitle}
                </p>
              </div>

              {/* Form */}
              <form
                onSubmit={handleSubmit}
                className="space-y-5"
              >
                {/* Username */}
                <div className="space-y-2">
                  <Label
                    htmlFor="username"
                    className="text-sm font-medium"
                  >
                    {dict.login.username}
                  </Label>

                  <Input
                    id="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    autoFocus
                    autoComplete="username"
                    placeholder={dict.login.usernamePlaceholder}
                    required
                    disabled={loading}
                    className="h-12 rounded-xl bg-background px-4 transition-shadow focus-visible:ring-2"
                  />
                </div>

                {/* Password */}
                <div className="space-y-2">
                  <div className="flex items-center justify-between">
                    <Label
                      htmlFor="password"
                      className="text-sm font-medium"
                    >
                      {dict.login.password}
                    </Label>
                  </div>

                  <div className="relative">
                    <Input
                      id="password"
                      type={showPassword ? "text" : "password"}
                      value={password}
                      onChange={(e) =>
                        setPassword(e.target.value)
                      }
                      autoComplete="current-password"
                      placeholder={dict.login.passwordPlaceholder}
                      required
                      disabled={loading}
                      className="h-12 rounded-xl bg-background px-4 pr-12 transition-shadow focus-visible:ring-2"
                    />

                    <button
                      type="button"
                      onClick={() =>
                        setShowPassword((value) => !value)
                      }
                      disabled={loading}
                      aria-label={
                        showPassword
                          ? dict.login.hidePassword
                          : dict.login.showPassword
                      }
                      className="absolute right-3 top-1/2 flex size-8 -translate-y-1/2 items-center justify-center rounded-lg text-muted-foreground transition-colors hover:bg-muted hover:text-foreground disabled:pointer-events-none"
                    >
                      {showPassword ? (
                        <EyeOff className="size-4" />
                      ) : (
                        <Eye className="size-4" />
                      )}
                    </button>
                  </div>
                </div>

                {/* Error */}
                {error && (
                  <div
                    role="alert"
                    className="rounded-xl border border-destructive/20 bg-destructive/5 px-4 py-3"
                  >
                    <p className="text-sm font-medium text-destructive-text">
                      {error}
                    </p>
                  </div>
                )}

                {/* Submit */}
                <Button
                  type="submit"
                  className="group h-12 w-full rounded-xl text-sm font-semibold shadow-sm"
                  disabled={loading}
                >
                  {loading ? (
                    <span className="flex items-center gap-2">
                      <span className="size-4 animate-spin rounded-full border-2 border-current border-t-transparent" />
                      {dict.login.submitting}
                    </span>
                  ) : (
                    <span className="flex items-center gap-2">
                      {dict.login.submit}
                      <ArrowRight className="size-4 transition-transform duration-300 group-hover:translate-x-0.5" />
                    </span>
                  )}
                </Button>
              </form>

              {/* Security message */}
              <div className="mt-8 flex items-start gap-3 rounded-xl border border-border bg-muted/30 p-4">
                <ShieldCheck className="mt-0.5 size-4 shrink-0 text-primary" />

                <p className="text-xs leading-5 text-muted-foreground">
                  {dict.login.securityNote}
                </p>
              </div>

              {/* Footer */}
              <p className="mt-8 text-center text-xs text-muted-foreground">
                © {new Date().getFullYear()} DariRentals
              </p>
            </div>
          </div>
        </section>
      </div>
    </main>
  );
}

/* =========================================================
   LOGIN BENEFIT
========================================================= */

function LoginBenefit({
  text,
}: {
  text: string;
}) {
  return (
    <div className="flex items-center gap-3">
      <span className="flex size-6 shrink-0 items-center justify-center rounded-full bg-primary-foreground/10">
        <CheckCircle2 className="size-3.5 text-primary-foreground/80" />
      </span>

      <span className="text-sm text-primary-foreground/70">
        {text}
      </span>
    </div>
  );
}
