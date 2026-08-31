"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Label } from "@/components/ui/label";
import { Badge } from "@/components/ui/badge";
import { ArrowLeft, FileBarChart, FileDown, FileSpreadsheet, Sparkles } from "lucide-react";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";

import { getEntityClients } from "@/lib/api";
import { API_BASE, authHeaders, UnauthorizedError, Role } from "@/lib/api-client";
import { logout } from "@/lib/auth";
import { useRequireRole } from "@/lib/use-require-role";
import { EnterpriseDto } from "@/lib/types/Enterprise";
import { PropertyDto } from "@/lib/types/Property";
import { FinancialReportDto } from "@/lib/types/FinancialReport";
import { FinancialReportTypeDto } from "@/lib/types/FinancialReportType";
import { FinancialReportScopeDto } from "@/lib/types/FinancialReportScope";

const ROLE: Role = "admin";
const TYPE_MONTHLY_CODE = "Mensuel";
const SCOPE_PROPERTY_CODE = "Proprietes";

const MONTHS = [
  "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
  "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre",
];

function currentYear() {
  return new Date().getFullYear();
}

function formatAmount(value?: number | null) {
  return value != null ? `${value.toFixed(2)} MAD` : "—";
}

function formatDate(value?: string | null) {
  if (!value) return "—";
  const d = new Date(value);
  return Number.isNaN(d.getTime()) ? value : d.toLocaleDateString("fr-FR");
}

export default function FinancialReportsPage() {
  const ready = useRequireRole(ROLE);
  const router = useRouter();
  const clients = useMemo(() => getEntityClients(ROLE), []);

  const [enterprises, setEnterprises] = useState<EnterpriseDto[]>([]);
  const [properties, setProperties] = useState<PropertyDto[]>([]);
  const [types, setTypes] = useState<FinancialReportTypeDto[]>([]);
  const [scopes, setScopes] = useState<FinancialReportScopeDto[]>([]);
  const [reports, setReports] = useState<FinancialReportDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Formulaire de génération
  const [enterpriseId, setEnterpriseId] = useState<number | null>(null);
  const [typeCode, setTypeCode] = useState<string>("");
  const [scopeCode, setScopeCode] = useState<string>("");
  const [propertyId, setPropertyId] = useState<number | null>(null);
  const [year, setYear] = useState<number>(currentYear());
  const [month, setMonth] = useState<number>(new Date().getMonth() + 1);
  const [generating, setGenerating] = useState(false);
  const [generateError, setGenerateError] = useState<string | null>(null);
  const [generateSuccess, setGenerateSuccess] = useState<string | null>(null);

  const [exportingId, setExportingId] = useState<number | null>(null);
  const [exportError, setExportError] = useState<string | null>(null);

  const handleAuthError = useCallback(
    (e: unknown) => {
      if (e instanceof UnauthorizedError) {
        logout();
        router.push("/login");
        return true;
      }
      return false;
    },
    [router]
  );

  const refresh = useCallback(async () => {
    setLoading(true);
    setError(null);
    try {
      const [ent, prop, ty, sc, repRes] = await Promise.all([
        clients.enterprise.findAll(),
        clients.property.findAll(),
        clients.financialReportType.findAll(),
        clients.financialReportScope.findAll(),
        // Endpoint dedie (pas le CRUD generique /api/admin/financialReport/, qui desactive
        // financialReportProperties) : necessaire pour afficher la propriete ciblee sur les
        // rapports a portee "Proprietes".
        fetch(`${API_BASE}admin/financial-reports/history`, { cache: "no-store", headers: authHeaders() }),
      ]);
      if (repRes.status === 401 || repRes.status === 403) {
        handleAuthError(new UnauthorizedError(repRes.status));
        return;
      }
      const rep: FinancialReportDto[] = repRes.ok ? await repRes.json() : [];
      setEnterprises(ent ?? []);
      setProperties(prop ?? []);
      setTypes(ty ?? []);
      setScopes(sc ?? []);
      // Les rapports les plus récents en premier.
      setReports((rep ?? []).slice().sort((a, b) => (b.id ?? 0) - (a.id ?? 0)));
    } catch (e) {
      if (!handleAuthError(e)) {
        setError(e instanceof Error ? e.message : "Erreur de chargement");
      }
    } finally {
      setLoading(false);
    }
  }, [clients, handleAuthError]);

  useEffect(() => {
    refresh();
  }, [refresh]);

  const propertiesForEnterprise = useMemo(
    () => properties.filter((p) => p.enterprise?.id === enterpriseId),
    [properties, enterpriseId]
  );

  const isMonthly = typeCode === TYPE_MONTHLY_CODE;
  const isPropertyScope = scopeCode === SCOPE_PROPERTY_CODE;

  async function handleGenerate() {
    setGenerateError(null);
    setGenerateSuccess(null);

    if (!enterpriseId) {
      setGenerateError("Choisis une société.");
      return;
    }
    if (!typeCode) {
      setGenerateError("Choisis une période (mensuelle ou annuelle).");
      return;
    }
    if (!scopeCode) {
      setGenerateError("Choisis une portée (société entière ou propriété).");
      return;
    }
    if (isPropertyScope && !propertyId) {
      setGenerateError("Choisis la propriété concernée.");
      return;
    }

    setGenerating(true);
    try {
      const res = await fetch(`${API_BASE}admin/financial-reports/generate`, {
        method: "POST",
        headers: { "Content-Type": "application/json", ...authHeaders() },
        body: JSON.stringify({
          enterpriseId,
          financialReportTypeCode: typeCode,
          financialReportScopeCode: scopeCode,
          propertyId: isPropertyScope ? propertyId : null,
          year,
          month: isMonthly ? month : null,
        }),
      });
      if (res.status === 401 || res.status === 403) {
        handleAuthError(new UnauthorizedError(res.status));
        return;
      }
      const body = await res.json().catch(() => null);
      if (!res.ok) {
        setGenerateError(body?.message ?? "Échec de la génération du rapport.");
        return;
      }
      setGenerateSuccess("Rapport généré et figé avec succès.");
      await refresh();
    } catch (e) {
      setGenerateError(e instanceof Error ? e.message : "Échec de la génération du rapport.");
    } finally {
      setGenerating(false);
    }
  }

  async function handleExport(report: FinancialReportDto, format: "pdf" | "csv") {
    if (!report.id) return;
    setExportError(null);
    setExportingId(report.id);
    try {
      const res = await fetch(`${API_BASE}admin/financial-reports/${report.id}/${format}`, {
        headers: authHeaders(),
      });
      if (res.status === 401 || res.status === 403) {
        handleAuthError(new UnauthorizedError(res.status));
        return;
      }
      if (!res.ok) {
        throw new Error(`Échec de l'export ${format.toUpperCase()}.`);
      }
      const blob = await res.blob();
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = `rapport-financier-${report.id}.${format}`;
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    } catch (e) {
      if (!handleAuthError(e)) {
        setExportError(e instanceof Error ? e.message : "Échec de l'export.");
      }
    } finally {
      setExportingId(null);
    }
  }

  if (!ready) return null;

  return (
    <div className="mx-auto max-w-5xl space-y-6">
      <div>
        <Button variant="ghost" size="sm" onClick={() => router.push("/admin")} className="mb-1 -ml-2">
          <ArrowLeft className="size-4" /> Retour au tableau de bord
        </Button>
        <h1 className="text-2xl font-semibold">Rapports financiers</h1>
        <p className="text-sm text-muted-foreground">
          Chaque rapport généré fige les revenus, charges et bénéfice net calculés à l&apos;instant
          de la génération : il n&apos;est plus jamais recalculé, même si des réservations ou
          charges sont ajoutées ensuite.
        </p>
      </div>

      {error && <p className="text-sm text-destructive-text">{error}</p>}

      <Card>
        <CardHeader>
          <CardTitle className="flex items-center gap-2">
            <Sparkles className="size-4" /> Générer un nouveau rapport
          </CardTitle>
        </CardHeader>
        <CardContent className="space-y-4">
          <div className="grid grid-cols-1 sm:grid-cols-2 gap-4">
            <div className="space-y-2">
              <Label>Société</Label>
              <Select
                value={enterpriseId != null ? String(enterpriseId) : undefined}
                onValueChange={(v) => {
                  setEnterpriseId(Number(v));
                  setPropertyId(null);
                }}
              >
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Choisir une société" />
                </SelectTrigger>
                <SelectContent>
                  {enterprises.map((e) => (
                    <SelectItem key={e.id} value={String(e.id)}>
                      {e.name}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label>Type de période</Label>
              <Select value={typeCode} onValueChange={setTypeCode}>
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Mensuel ou annuel" />
                </SelectTrigger>
                <SelectContent>
                  {types.map((t) => (
                    <SelectItem key={t.id} value={t.code}>
                      {t.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            <div className="space-y-2">
              <Label>Année</Label>
              <Select value={String(year)} onValueChange={(v) => setYear(Number(v))}>
                <SelectTrigger className="w-full">
                  <SelectValue />
                </SelectTrigger>
                <SelectContent>
                  {Array.from({ length: 6 }, (_, i) => currentYear() - i).map((y) => (
                    <SelectItem key={y} value={String(y)}>
                      {y}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {isMonthly && (
              <div className="space-y-2">
                <Label>Mois</Label>
                <Select value={String(month)} onValueChange={(v) => setMonth(Number(v))}>
                  <SelectTrigger className="w-full">
                    <SelectValue />
                  </SelectTrigger>
                  <SelectContent>
                    {MONTHS.map((label, idx) => (
                      <SelectItem key={label} value={String(idx + 1)}>
                        {label}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            )}

            <div className="space-y-2">
              <Label>Portée</Label>
              <Select
                value={scopeCode}
                onValueChange={(v) => {
                  setScopeCode(v);
                  setPropertyId(null);
                }}
              >
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Société entière ou propriété" />
                </SelectTrigger>
                <SelectContent>
                  {scopes.map((s) => (
                    <SelectItem key={s.id} value={s.code}>
                      {s.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {isPropertyScope && (
              <div className="space-y-2">
                <Label>Propriété</Label>
                <Select
                  value={propertyId != null ? String(propertyId) : undefined}
                  onValueChange={(v) => setPropertyId(Number(v))}
                  disabled={!enterpriseId}
                >
                  <SelectTrigger className="w-full">
                    <SelectValue
                      placeholder={enterpriseId ? "Choisir une propriété" : "Choisis d'abord une société"}
                    />
                  </SelectTrigger>
                  <SelectContent>
                    {propertiesForEnterprise.map((p) => (
                      <SelectItem key={p.id} value={String(p.id)}>
                        {p.name}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              </div>
            )}
          </div>

          {generateError && <p className="text-sm text-destructive-text">{generateError}</p>}
          {generateSuccess && <p className="text-sm text-green-600">{generateSuccess}</p>}

          <Button onClick={handleGenerate} disabled={generating}>
            <FileBarChart className="size-4" />
            {generating ? "Génération..." : "Générer le rapport"}
          </Button>
        </CardContent>
      </Card>

      <Card>
        <CardHeader>
          <CardTitle>Historique des rapports</CardTitle>
        </CardHeader>
        <CardContent>
          {exportError && <p className="text-sm text-destructive-text mb-3">{exportError}</p>}
          {loading ? (
            <p className="text-sm text-muted-foreground py-6 text-center">Chargement...</p>
          ) : reports.length === 0 ? (
            <p className="text-sm text-muted-foreground py-6 text-center">
              Aucun rapport généré pour l&apos;instant.
            </p>
          ) : (
            <div className="overflow-x-auto">
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Société</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead>Portée</TableHead>
                    <TableHead>Période</TableHead>
                    <TableHead>Revenus</TableHead>
                    <TableHead>Charges</TableHead>
                    <TableHead>Bénéfice net</TableHead>
                    <TableHead>Généré le</TableHead>
                    <TableHead>Par</TableHead>
                    <TableHead className="text-right">Export</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {reports.map((r) => {
                    const scopedProperty = r.financialReportProperties?.[0]?.property;
                    return (
                      <TableRow key={r.id}>
                        <TableCell className="font-medium">{r.enterprise?.name ?? "—"}</TableCell>
                        <TableCell>
                          <Badge variant="outline">{r.financialReportType?.label ?? "—"}</Badge>
                        </TableCell>
                        <TableCell>
                          {r.financialReportScope?.label ?? "—"}
                          {scopedProperty && (
                            <span className="text-muted-foreground"> · {scopedProperty.name}</span>
                          )}
                        </TableCell>
                        <TableCell className="whitespace-nowrap">
                          {formatDate(r.periodStart)} → {formatDate(r.periodEnd)}
                        </TableCell>
                        <TableCell className="whitespace-nowrap">{formatAmount(r.totalRevenue)}</TableCell>
                        <TableCell className="whitespace-nowrap">{formatAmount(r.totalCharges)}</TableCell>
                        <TableCell className="whitespace-nowrap font-semibold">
                          {formatAmount(r.netProfit)}
                        </TableCell>
                        <TableCell className="whitespace-nowrap text-muted-foreground">
                          {r.generatedAt ?? "—"}
                        </TableCell>
                        <TableCell className="text-muted-foreground">
                          {r.generatedBy?.name ?? "Administrateur"}
                        </TableCell>
                        <TableCell className="text-right whitespace-nowrap">
                          <Button
                            variant="ghost"
                            size="icon"
                            title="Exporter en PDF"
                            disabled={exportingId === r.id}
                            onClick={() => handleExport(r, "pdf")}
                          >
                            <FileDown className="size-4" />
                          </Button>
                          <Button
                            variant="ghost"
                            size="icon"
                            title="Exporter en CSV"
                            disabled={exportingId === r.id}
                            onClick={() => handleExport(r, "csv")}
                          >
                            <FileSpreadsheet className="size-4" />
                          </Button>
                        </TableCell>
                      </TableRow>
                    );
                  })}
                </TableBody>
              </Table>
            </div>
          )}
        </CardContent>
      </Card>
    </div>
  );
}
