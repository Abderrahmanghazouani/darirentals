"use client";

import { Fragment, useEffect, useSyncExternalStore } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import {
  ArrowRight,
  Building2,
  CalendarDays,
  CheckCircle2,
  ChevronRight,
  FileText,
  ListTodo,
  Receipt,
  ScanLine,
  ShieldCheck,
  Sparkles,
  TrendingUp,
  Upload,
  Users,
  WalletCards,
  type LucideIcon,
} from "lucide-react";

import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { getCurrentRole } from "@/lib/auth";

const CARD_SHADOW =
  "shadow-[0_8px_30px_rgba(90,59,93,0.06)]";

const steps: {
  icon: LucideIcon;
  title: string;
  text: string;
}[] = [
  {
    icon: Building2,
    title: "Centralisez vos biens",
    text: "Ajoutez vos propriétés et sociétés depuis un seul espace, avec des données strictement séparées.",
  },
  {
    icon: Receipt,
    title: "Gérez vos opérations",
    text: "Réservations, charges, paiements et documents sont réunis dans une interface unique.",
  },
  {
    icon: ListTodo,
    title: "Organisez vos équipes",
    text: "Assignez les tâches, suivez leur avancement et identifiez rapidement les retards.",
  },
  {
    icon: TrendingUp,
    title: "Pilotez votre rentabilité",
    text: "Visualisez revenus, charges et bénéfices pour comprendre la performance de chaque bien.",
  },
];

const features: {
  icon: LucideIcon;
  title: string;
  text: string;
}[] = [
  {
    icon: CalendarDays,
    title: "Réservations & calendrier",
    text: "Visualisez les séjours de chaque propriété et détectez automatiquement les chevauchements.",
  },
  {
    icon: Receipt,
    title: "Charges & paiements",
    text: "Centralisez les dépenses, fournisseurs et paiements sans multiplier les fichiers Excel.",
  },
  {
    icon: ListTodo,
    title: "Gestion des tâches",
    text: "Attribuez les interventions aux collaborateurs et gardez une vision claire des échéances.",
  },
  {
    icon: ScanLine,
    title: "Scan de facture par IA",
    text: "L’IA extrait automatiquement les informations importantes de vos factures.",
  },
  {
    icon: TrendingUp,
    title: "Rentabilité par propriété",
    text: "Analysez revenus, charges et bénéfice net sur la période de votre choix.",
  },
  {
    icon: Building2,
    title: "Multi-société",
    text: "Gérez plusieurs sociétés depuis un seul compte tout en gardant leurs données cloisonnées.",
  },
];

const scanFlow = [
  {
    icon: Upload,
    title: "Déposez",
    text: "Photo ou PDF",
  },
  {
    icon: ScanLine,
    title: "Analysez",
    text: "L’IA extrait les données",
  },
  {
    icon: CheckCircle2,
    title: "Validez",
    text: "Vous gardez le contrôle",
  },
];

export default function Home() {
  const router = useRouter();
  const year = new Date().getFullYear();

  const role = useSyncExternalStore(
    () => () => {},
    () => getCurrentRole(),
    () => null,
  );

  const loggedIn =
    role === "admin" || role === "collaborator";

  useEffect(() => {
    if (loggedIn) {
      router.replace(`/${role}`);
    }
  }, [loggedIn, role, router]);

  if (loggedIn) return null;

  return (
    <div className="min-h-screen bg-background text-foreground">
      {/* =======================================================
          HEADER
      ======================================================= */}

      <header className="sticky top-0 z-50 border-b border-border/70 bg-background/80 backdrop-blur-xl">
        <div className="mx-auto flex h-[72px] w-full max-w-7xl items-center justify-between px-4 sm:px-6 lg:px-8">
          
          <Link
            href="/"
            className="group flex items-center gap-2.5"
          >
            <span className="flex size-9 items-center justify-center rounded-xl bg-primary text-primary-foreground shadow-sm transition-transform duration-300 group-hover:scale-105">
              <Building2 className="size-[18px]" />
            </span>

            <span className="text-[19px] font-bold tracking-tight text-primary">
              DariRentals
            </span>
          </Link>

          <nav className="hidden items-center gap-8 md:flex">
            <a
              href="#fonctionnalites"
              className="text-sm font-medium text-muted-foreground transition-colors hover:text-foreground"
            >
              Fonctionnalités
            </a>

            <a
              href="#comment-ca-marche"
              className="text-sm font-medium text-muted-foreground transition-colors hover:text-foreground"
            >
              Comment ça marche
            </a>

            <a
              href="#ia"
              className="text-sm font-medium text-muted-foreground transition-colors hover:text-foreground"
            >
              Intelligence artificielle
            </a>
          </nav>

          <Button
            asChild
            size="sm"
            className="rounded-lg px-5"
          >
            <Link href="/login">
              Se connecter
            </Link>
          </Button>
        </div>
      </header>

      <main>
        {/* =======================================================
            HERO
        ======================================================= */}

        <section className="relative overflow-hidden">
          {/* Background glow */}
          <div className="pointer-events-none absolute inset-0">
            <div className="absolute left-[8%] top-20 size-72 rounded-full bg-primary/5 blur-3xl" />
            <div className="absolute right-[5%] top-32 size-96 rounded-full bg-secondary/10 blur-3xl" />
          </div>

          <div className="relative mx-auto grid min-h-[680px] w-full max-w-7xl items-center gap-14 px-4 py-20 sm:px-6 lg:grid-cols-[1fr_0.95fr] lg:px-8 lg:py-24">
            
            {/* Hero content */}
            <div className="max-w-2xl">
              
              <div className="mb-6 inline-flex items-center gap-2 rounded-full border border-border bg-card px-3.5 py-2 text-xs font-medium text-muted-foreground shadow-sm">
                <span className="flex size-5 items-center justify-center rounded-full bg-accent">
                  <Sparkles className="size-3 text-accent-foreground" />
                </span>
                Gestion locative nouvelle génération
              </div>

              <h1 className="text-balance text-5xl font-bold leading-[1.05] tracking-[-0.035em] sm:text-6xl lg:text-[68px]">
                Gérez vos biens.
                <br />

                <span className="text-primary">
                  Simplement.
                </span>
              </h1>

              <p className="mt-7 max-w-xl text-lg leading-8 text-muted-foreground sm:text-xl">
                DariRentals centralise vos réservations, charges,
                paiements, tâches et indicateurs de rentabilité dans
                une seule plateforme pensée pour les conciergeries.
              </p>

              <div className="mt-9 flex flex-col gap-3 sm:flex-row">
                <Button
                  asChild
                  size="lg"
                  className="group h-12 rounded-xl px-6 shadow-md"
                >
                  <Link href="/login">
                    Accéder à mon espace
                    <ArrowRight className="ml-1 size-4 transition-transform duration-300 group-hover:translate-x-1" />
                  </Link>
                </Button>

                <Button
                  asChild
                  size="lg"
                  variant="outline"
                  className="h-12 rounded-xl px-6"
                >
                  <a href="#fonctionnalites">
                    Découvrir la plateforme
                  </a>
                </Button>
              </div>

              <div className="mt-9 flex flex-wrap gap-x-6 gap-y-3">
                {[
                  "Multi-société",
                  "Données cloisonnées",
                  "Scan IA",
                ].map((item) => (
                  <div
                    key={item}
                    className="flex items-center gap-2 text-sm text-muted-foreground"
                  >
                    <CheckCircle2 className="size-4 text-success" />
                    {item}
                  </div>
                ))}
              </div>

              <div className="mt-10 flex items-center gap-3 border-t border-border pt-6">
                <div className="flex -space-x-2">
                  {["A", "M", "Y"].map((letter) => (
                    <div
                      key={letter}
                      className="flex size-8 items-center justify-center rounded-full border-2 border-background bg-accent text-xs font-semibold text-accent-foreground"
                    >
                      {letter}
                    </div>
                  ))}
                </div>

                <div>
                  <div className="flex items-center gap-1">
                    <span className="text-sm font-semibold">
                      Pensé pour les professionnels
                    </span>
                  </div>
                  <p className="text-xs text-muted-foreground">
                    Une gestion claire, sans complexité inutile.
                  </p>
                </div>
              </div>
            </div>

            {/* Dashboard */}
            <DashboardPreview />
          </div>
        </section>

        {/* =======================================================
            TRUST BAR
        ======================================================= */}

        <section className="border-y border-border bg-muted/30">
          <div className="mx-auto flex max-w-7xl flex-wrap items-center justify-center gap-x-12 gap-y-4 px-4 py-6 sm:px-6 lg:px-8">
            <TrustItem
              icon={ShieldCheck}
              text="Données cloisonnées"
            />

            <TrustItem
              icon={Building2}
              text="Multi-société"
            />

            <TrustItem
              icon={WalletCards}
              text="Suivi financier"
            />

            <TrustItem
              icon={FileText}
              text="Documents centralisés"
            />
          </div>
        </section>

        {/* =======================================================
            HOW IT WORKS
        ======================================================= */}

        <section
          id="comment-ca-marche"
          className="scroll-mt-24 border-b border-border py-20 lg:py-28"
        >
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            
            <SectionHeading
              eyebrow="COMMENT ÇA MARCHE"
              title="Une gestion qui suit votre quotidien"
              text="DariRentals transforme les tâches dispersées de votre conciergerie en un flux de travail simple et centralisé."
            />

            <div className="mt-14 grid gap-5 md:grid-cols-2 lg:grid-cols-4">
              {steps.map((step, index) => {
                const Icon = step.icon;

                return (
                  <div
                    key={step.title}
                    className="group relative rounded-2xl border border-border bg-card p-6 transition-all duration-300 hover:-translate-y-1 hover:border-primary/20 hover:shadow-lg"
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex size-11 items-center justify-center rounded-xl bg-accent text-accent-foreground">
                        <Icon className="size-5" />
                      </div>

                      <span className="font-mono text-xs font-semibold text-muted-foreground/50">
                        0{index + 1}
                      </span>
                    </div>

                    <h3 className="mt-6 font-semibold leading-snug">
                      {step.title}
                    </h3>

                    <p className="mt-2 text-sm leading-6 text-muted-foreground">
                      {step.text}
                    </p>

                    {index < steps.length - 1 && (
                      <ChevronRight className="absolute -right-4 top-1/2 hidden size-5 -translate-y-1/2 text-border lg:block" />
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        </section>

        {/* =======================================================
            FEATURES
        ======================================================= */}

        <section
          id="fonctionnalites"
          className="scroll-mt-24 bg-muted/30 py-20 lg:py-28"
        >
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            
            <div className="flex flex-col justify-between gap-6 md:flex-row md:items-end">
              <SectionHeading
                eyebrow="FONCTIONNALITÉS"
                title="Tout votre parc au même endroit"
                text="Les outils essentiels pour gérer efficacement vos propriétés, vos opérations et vos équipes."
              />

              <Button
                asChild
                variant="ghost"
                className="group w-fit"
              >
                <Link href="/login">
                  Accéder à la plateforme
                  <ArrowRight className="ml-1 size-4 transition-transform group-hover:translate-x-1" />
                </Link>
              </Button>
            </div>

            <div className="mt-14 grid gap-5 sm:grid-cols-2 lg:grid-cols-3">
              {features.map((feature) => {
                const Icon = feature.icon;

                return (
                  <Card
                    key={feature.title}
                    className="group relative overflow-hidden rounded-2xl border-border bg-card p-7 transition-all duration-300 hover:-translate-y-1 hover:border-primary/20 hover:shadow-xl"
                  >
                    <div className="absolute right-0 top-0 size-28 translate-x-10 -translate-y-10 rounded-full bg-primary/5 blur-2xl transition-all duration-500 group-hover:scale-150" />

                    <div className="relative">
                      <span className="flex size-11 items-center justify-center rounded-xl bg-accent text-accent-foreground transition-transform duration-300 group-hover:scale-105">
                        <Icon className="size-5" />
                      </span>

                      <h3 className="mt-6 font-semibold">
                        {feature.title}
                      </h3>

                      <p className="mt-2 text-sm leading-6 text-muted-foreground">
                        {feature.text}
                      </p>

                      <div className="mt-5 flex items-center text-xs font-medium text-primary opacity-0 transition-opacity duration-300 group-hover:opacity-100">
                        En savoir plus
                        <ArrowRight className="ml-1 size-3.5" />
                      </div>
                    </div>
                  </Card>
                );
              })}
            </div>
          </div>
        </section>

        {/* =======================================================
            AI SECTION
        ======================================================= */}

        <section
          id="ia"
          className="scroll-mt-24 border-y border-border py-20 lg:py-28"
        >
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            
            <div className="grid items-center gap-14 lg:grid-cols-[0.9fr_1.1fr]">
              
              <div>
                <div className="inline-flex items-center gap-2 rounded-full bg-accent px-3.5 py-2 text-xs font-semibold text-accent-foreground">
                  <Sparkles className="size-3.5" />
                  INTELLIGENCE ARTIFICIELLE
                </div>

                <h2 className="mt-5 text-4xl font-bold tracking-tight sm:text-5xl">
                  La saisie des factures,
                  <span className="text-primary">
                    {" "}simplifiée.
                  </span>
                </h2>

                <p className="mt-5 text-lg leading-8 text-muted-foreground">
                  Une facture ne devrait pas vous obliger à recopier
                  manuellement chaque information. Déposez-la,
                  laissez l’IA préparer la saisie et gardez simplement
                  la validation finale.
                </p>

                <div className="mt-7 space-y-4">
                  <Benefit
                    title="Extraction automatique"
                    text="Montant, date et fournisseur sont identifiés automatiquement."
                  />

                  <Benefit
                    title="Validation humaine"
                    text="Vous vérifiez chaque information avant son enregistrement."
                  />

                  <Benefit
                    title="Gain de temps"
                    text="Moins de saisie manuelle et moins d'erreurs dans vos charges."
                  />
                </div>
              </div>

              {/* AI workflow card */}
              <div className={`rounded-3xl border border-border bg-card p-6 sm:p-8 ${CARD_SHADOW}`}>
                
                <div className="flex items-center justify-between border-b border-border pb-5">
                  <div>
                    <p className="text-sm font-semibold">
                      Scan de facture
                    </p>
                    <p className="mt-1 text-xs text-muted-foreground">
                      Assistant intelligent
                    </p>
                  </div>

                  <span className="flex size-10 items-center justify-center rounded-xl bg-accent text-accent-foreground">
                    <ScanLine className="size-5" />
                  </span>
                </div>

                <div className="mt-7 space-y-5">
                  {scanFlow.map((step, index) => {
                    const Icon = step.icon;

                    return (
                      <Fragment key={step.title}>
                        <div className="flex items-center gap-4 rounded-xl border border-border bg-muted/30 p-4">
                          <span className="flex size-11 shrink-0 items-center justify-center rounded-xl bg-background text-primary shadow-sm">
                            <Icon className="size-5" />
                          </span>

                          <div className="flex-1">
                            <p className="text-sm font-semibold">
                              {step.title}
                            </p>

                            <p className="mt-1 text-xs text-muted-foreground">
                              {step.text}
                            </p>
                          </div>

                          <span className="font-mono text-[10px] text-muted-foreground">
                            0{index + 1}
                          </span>
                        </div>

                        {index < scanFlow.length - 1 && (
                          <div className="ml-[27px] h-4 border-l border-dashed border-border" />
                        )}
                      </Fragment>
                    );
                  })}
                </div>

                <div className="mt-6 flex items-center gap-2 rounded-xl bg-accent/60 px-4 py-3 text-xs font-medium text-accent-foreground">
                  <ShieldCheck className="size-4" />
                  Aucun enregistrement sans votre validation
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* =======================================================
            FINAL CTA
        ======================================================= */}

        <section className="py-20 lg:py-28">
          <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
            <div className="relative overflow-hidden rounded-3xl bg-primary px-6 py-16 text-center text-primary-foreground sm:px-12 lg:py-20">
              
              <div className="pointer-events-none absolute -left-20 -top-20 size-64 rounded-full bg-white/10 blur-3xl" />
              <div className="pointer-events-none absolute -bottom-32 -right-10 size-80 rounded-full bg-white/10 blur-3xl" />

              <div className="relative">
                <div className="mx-auto flex size-12 items-center justify-center rounded-2xl bg-primary-foreground/10">
                  <Building2 className="size-5" />
                </div>

                <h2 className="mx-auto mt-6 max-w-2xl text-3xl font-bold tracking-tight sm:text-5xl">
                  Reprenez le contrôle de votre gestion locative.
                </h2>

                <p className="mx-auto mt-5 max-w-xl text-base leading-7 text-primary-foreground/75 sm:text-lg">
                  Toutes vos propriétés, sociétés, opérations et
                  indicateurs réunis dans un espace pensé pour vous.
                </p>

                <div className="mt-8">
                  <Button
                    asChild
                    size="lg"
                    variant="secondary"
                    className="group h-12 rounded-xl px-7"
                  >
                    <Link href="/login">
                      Accéder à mon espace
                      <ArrowRight className="ml-1 size-4 transition-transform group-hover:translate-x-1" />
                    </Link>
                  </Button>
                </div>
              </div>
            </div>
          </div>
        </section>
      </main>

      {/* =======================================================
          FOOTER
      ======================================================= */}

      <footer className="border-t border-border bg-muted/30">
        <div className="mx-auto flex max-w-7xl flex-col gap-6 px-4 py-8 sm:px-6 md:flex-row md:items-center md:justify-between lg:px-8">
          
          <Link
            href="/"
            className="flex items-center gap-2.5"
          >
            <span className="flex size-8 items-center justify-center rounded-lg bg-primary text-primary-foreground">
              <Building2 className="size-4" />
            </span>

            <span className="font-bold tracking-tight text-primary">
              DariRentals
            </span>
          </Link>

          <nav className="flex items-center gap-6 text-sm text-muted-foreground">
            <a
              href="#fonctionnalites"
              className="transition-colors hover:text-foreground"
            >
              Fonctionnalités
            </a>

            <a
              href="#comment-ca-marche"
              className="transition-colors hover:text-foreground"
            >
              Comment ça marche
            </a>

            <Link
              href="/login"
              className="transition-colors hover:text-foreground"
            >
              Connexion
            </Link>
          </nav>

          <p className="text-sm text-muted-foreground">
            © {year} DariRentals
          </p>
        </div>
      </footer>
    </div>
  );
}

/* =========================================================
   DASHBOARD PREVIEW
========================================================= */

function DashboardPreview() {
  return (
    <div className="relative">
      {/* Decorative background */}
      <div className="absolute -inset-5 rounded-[32px] bg-primary/[0.035] blur-2xl" />

      <div
        className={`relative overflow-hidden rounded-3xl border border-border bg-card p-4 sm:p-5 ${CARD_SHADOW}`}
      >
        {/* Browser top */}
        <div className="flex items-center gap-2 border-b border-border pb-4">
          <span className="size-2.5 rounded-full bg-muted-foreground/30" />
          <span className="size-2.5 rounded-full bg-muted-foreground/30" />
          <span className="size-2.5 rounded-full bg-muted-foreground/30" />

          <div className="ml-3 h-7 flex-1 rounded-lg bg-muted/60" />
        </div>

        <div className="p-2 sm:p-3">
          {/* Dashboard header */}
          <div className="flex items-center justify-between">
            <div>
              <p className="text-sm font-semibold">
                Vue d’ensemble
              </p>
              <p className="mt-1 text-[11px] text-muted-foreground">
                Aujourd’hui · Toutes les propriétés
              </p>
            </div>

            <span className="flex size-9 items-center justify-center rounded-xl bg-accent text-accent-foreground">
              <TrendingUp className="size-4" />
            </span>
          </div>

          {/* KPIs */}
          <div className="mt-5 grid grid-cols-2 gap-3">
            <PreviewKpi
              label="Revenus du mois"
              value="142 500 MAD"
              delta="+12,4 %"
            />

            <PreviewKpi
              label="Occupation"
              value="87 %"
              delta="+3,1 %"
            />
          </div>

          {/* Chart */}
          <div className="mt-3 rounded-2xl border border-border bg-muted/30 p-4">
            <div className="flex items-center justify-between">
              <p className="text-[11px] font-medium text-muted-foreground">
                Revenus sur 6 mois
              </p>

              <span className="text-[10px] text-muted-foreground">
                MAD
              </span>
            </div>

            <svg
              viewBox="0 0 360 120"
              className="mt-4 h-28 w-full"
              preserveAspectRatio="none"
              aria-hidden="true"
            >
              <defs>
                <linearGradient
                  id="preview-area"
                  x1="0"
                  y1="0"
                  x2="0"
                  y2="1"
                >
                  <stop
                    offset="0%"
                    stopColor="var(--primary)"
                    stopOpacity="0.22"
                  />

                  <stop
                    offset="100%"
                    stopColor="var(--primary)"
                    stopOpacity="0"
                  />
                </linearGradient>
              </defs>

              <path
                d="M0 92 L60 76 L120 82 L180 57 L240 63 L300 34 L360 22 L360 120 L0 120 Z"
                fill="url(#preview-area)"
              />

              <path
                d="M0 92 L60 76 L120 82 L180 57 L240 63 L300 34 L360 22"
                fill="none"
                stroke="var(--primary)"
                strokeWidth="2.5"
                strokeLinecap="round"
                strokeLinejoin="round"
              />

              <circle
                cx="300"
                cy="34"
                r="4"
                fill="var(--primary)"
              />

              <circle
                cx="360"
                cy="22"
                r="4"
                fill="var(--primary)"
              />
            </svg>

            <div className="mt-1 flex justify-between text-[9px] text-muted-foreground">
              <span>Jan</span>
              <span>Fév</span>
              <span>Mar</span>
              <span>Avr</span>
              <span>Mai</span>
              <span>Juin</span>
            </div>
          </div>

          {/* Bottom cards */}
          <div className="mt-3 grid grid-cols-2 gap-3">
            <div className="rounded-2xl border border-border p-3">
              <div className="flex items-center gap-2">
                <span className="flex size-7 items-center justify-center rounded-lg bg-accent">
                  <Building2 className="size-3.5 text-accent-foreground" />
                </span>

                <span className="text-[10px] text-muted-foreground">
                  Propriétés
                </span>
              </div>

              <p className="mt-3 text-xl font-bold">
                24
              </p>
            </div>

            <div className="rounded-2xl border border-border p-3">
              <div className="flex items-center gap-2">
                <span className="flex size-7 items-center justify-center rounded-lg bg-accent">
                  <ListTodo className="size-3.5 text-accent-foreground" />
                </span>

                <span className="text-[10px] text-muted-foreground">
                  Tâches
                </span>
              </div>

              <p className="mt-3 text-xl font-bold">
                18
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

/* =========================================================
   SMALL COMPONENTS
========================================================= */

function PreviewKpi({
  label,
  value,
  delta,
}: {
  label: string;
  value: string;
  delta: string;
}) {
  return (
    <div className="rounded-2xl border border-border bg-muted/30 p-4">
      <p className="text-[10px] text-muted-foreground">
        {label}
      </p>

      <p className="mt-2 text-base font-bold tracking-tight sm:text-lg">
        {value}
      </p>

      <div className="mt-1 flex items-center gap-1 text-[10px] font-medium text-success">
        <TrendingUp className="size-3" />
        {delta}
      </div>
    </div>
  );
}

function TrustItem({
  icon: Icon,
  text,
}: {
  icon: LucideIcon;
  text: string;
}) {
  return (
    <div className="flex items-center gap-2 text-sm text-muted-foreground">
      <Icon className="size-4 text-primary" />
      {text}
    </div>
  );
}

function Benefit({
  title,
  text,
}: {
  title: string;
  text: string;
}) {
  return (
    <div className="flex gap-3">
      <CheckCircle2 className="mt-0.5 size-5 shrink-0 text-success" />

      <div>
        <p className="text-sm font-semibold">
          {title}
        </p>

        <p className="mt-1 text-sm leading-6 text-muted-foreground">
          {text}
        </p>
      </div>
    </div>
  );
}

function SectionHeading({
  eyebrow,
  title,
  text,
}: {
  eyebrow: string;
  title: string;
  text: string;
}) {
  return (
    <div className="max-w-2xl">
      <p className="text-xs font-semibold tracking-[0.18em] text-primary">
        {eyebrow}
      </p>

      <h2 className="mt-3 text-3xl font-bold tracking-tight sm:text-4xl lg:text-[42px]">
        {title}
      </h2>

      <p className="mt-4 text-base leading-7 text-muted-foreground sm:text-lg">
        {text}
      </p>
    </div>
  );
}