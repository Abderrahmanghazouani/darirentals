"use client";

import { useCallback, useEffect, useMemo, useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Plus, Pencil, Trash2, ArrowLeft, RefreshCw } from "lucide-react";
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
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle,
} from "@/components/ui/alert-dialog";

import { getEntityClients } from "@/lib/api";
import { API_BASE, authHeaders, UnauthorizedError, Role } from "@/lib/api-client";
import { logout } from "@/lib/auth";
import { useRequireRole } from "@/lib/use-require-role";
import { CurrencyDto } from "@/lib/types/Currency";
import { ExchangeRateDto, newExchangeRateDto } from "@/lib/types/ExchangeRate";
import { BASE_CURRENCY_CODE } from "@/lib/currency/conversion";

const ROLE: Role = "admin";

export default function ExchangeRatesPage() {
  const ready = useRequireRole(ROLE);
  const router = useRouter();
  const clients = useMemo(() => getEntityClients(ROLE), []);

  const [currencies, setCurrencies] = useState<CurrencyDto[]>([]);
  const [rates, setRates] = useState<ExchangeRateDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [formOpen, setFormOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<ExchangeRateDto | null>(null);
  const [targetCode, setTargetCode] = useState<string>("");
  const [rateValue, setRateValue] = useState<string>("");
  const [source, setSource] = useState<string>("");
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState<string | null>(null);
  const [deleteTarget, setDeleteTarget] = useState<ExchangeRateDto | null>(null);

  const [syncing, setSyncing] = useState(false);
  const [syncMessage, setSyncMessage] = useState<{ text: string; ok: boolean } | null>(null);

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
      const [c, r] = await Promise.all([
        clients.currency.findAll(),
        clients.exchangeRate.findAll(),
      ]);
      setCurrencies(c ?? []);
      setRates(r ?? []);
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

  async function handleSyncNow() {
    setSyncing(true);
    setSyncMessage(null);
    try {
      const res = await fetch(`${API_BASE}admin/currency/exchange-rates/sync`, {
        method: "POST",
        headers: authHeaders(),
      });
      if (res.status === 401 || res.status === 403) {
        handleAuthError(new UnauthorizedError(res.status));
        return;
      }
      const body = await res.json().catch(() => null);
      setSyncMessage({
        text: body?.message ?? (res.ok ? "Taux mis à jour." : "Échec de la synchronisation."),
        ok: res.ok,
      });
      if (res.ok) {
        await refresh();
      }
    } catch (e) {
      setSyncMessage({
        text: e instanceof Error ? e.message : "Échec de la synchronisation.",
        ok: false,
      });
    } finally {
      setSyncing(false);
    }
  }

  const baseCurrency = currencies.find((c) => c.code === BASE_CURRENCY_CODE) ?? null;
  const targetCurrencies = currencies.filter((c) => c.code !== BASE_CURRENCY_CODE);

  function openCreate() {
    setEditingItem(null);
    setTargetCode("");
    setRateValue("");
    setSource("");
    setFormError(null);
    setFormOpen(true);
  }

  function openEdit(item: ExchangeRateDto) {
    setEditingItem(item);
    setTargetCode(item.targetCurrency?.code ?? "");
    setRateValue(item.rate != null ? String(item.rate) : "");
    setSource(item.source ?? "");
    setFormError(null);
    setFormOpen(true);
  }

  async function handleSave() {
    const parsedRate = Number(rateValue.replace(",", "."));
    if (!targetCode) {
      setFormError("Choisis une devise cible.");
      return;
    }
    if (!Number.isFinite(parsedRate) || parsedRate <= 0) {
      setFormError("Le taux doit être un nombre positif.");
      return;
    }
    if (!baseCurrency) {
      setFormError("Devise de base (MAD) introuvable — vérifie la configuration des devises.");
      return;
    }
    const target = currencies.find((c) => c.code === targetCode);
    if (!target) {
      setFormError("Devise cible introuvable.");
      return;
    }

    setSaving(true);
    setFormError(null);
    try {
      const dto: ExchangeRateDto = {
        ...(editingItem ?? newExchangeRateDto()),
        baseCurrency: { ...(baseCurrency as CurrencyDto) },
        targetCurrency: { ...(target as CurrencyDto) },
        rate: parsedRate,
        source: source.trim() || "Saisie manuelle",
      };
      if (editingItem?.id) {
        await clients.exchangeRate.update(dto);
      } else {
        await clients.exchangeRate.create(dto);
      }
      setFormOpen(false);
      await refresh();
    } catch (e) {
      if (!handleAuthError(e)) {
        setFormError(e instanceof Error ? e.message : "Erreur lors de l'enregistrement");
      }
    } finally {
      setSaving(false);
    }
  }

  async function handleDelete() {
    if (!deleteTarget?.id) return;
    try {
      await clients.exchangeRate.remove(deleteTarget.id);
      setDeleteTarget(null);
      await refresh();
    } catch (e) {
      if (!handleAuthError(e)) {
        setError(e instanceof Error ? e.message : "Erreur lors de la suppression");
      }
    }
  }

  if (!ready) return null;

  return (
    <div className="mx-auto max-w-4xl space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <Button variant="ghost" size="sm" onClick={() => router.push("/admin")} className="mb-1 -ml-2">
            <ArrowLeft className="size-4" /> Retour au tableau de bord
          </Button>
          <h1 className="text-2xl font-semibold">Taux de change</h1>
          <p className="text-sm text-muted-foreground">
            Devise de référence : {BASE_CURRENCY_CODE}. Mis à jour automatiquement chaque jour
            depuis ExchangeRate-API, et modifiable manuellement à tout moment ci-dessous
            (voir NOTES-devises.md).
          </p>
        </div>
        <div className="flex items-center gap-2">
          <Button variant="outline" onClick={handleSyncNow} disabled={syncing}>
            <RefreshCw className={`size-4 ${syncing ? "animate-spin" : ""}`} />
            {syncing ? "Actualisation..." : "Actualiser maintenant"}
          </Button>
          <Button onClick={openCreate}>
            <Plus className="size-4" /> Nouveau taux
          </Button>
        </div>
      </div>

      {syncMessage && (
        <p className={`text-sm ${syncMessage.ok ? "text-success" : "text-destructive-text"}`}>
          {syncMessage.text}
        </p>
      )}
      {error && <p className="text-sm text-destructive-text">{error}</p>}

      <Card>
        <CardHeader>
          <CardTitle>Taux actuels</CardTitle>
        </CardHeader>
        <CardContent>
          {loading ? (
            <p className="text-sm text-muted-foreground py-6 text-center">Chargement...</p>
          ) : rates.length === 0 ? (
            <p className="text-sm text-muted-foreground py-6 text-center">
              Aucun taux de change enregistré pour l&apos;instant.
            </p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Conversion</TableHead>
                  <TableHead>1 {BASE_CURRENCY_CODE} =</TableHead>
                  <TableHead>Source</TableHead>
                  <TableHead className="text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {rates.map((r) => (
                  <TableRow key={r.id}>
                    <TableCell className="font-medium">
                      {r.baseCurrency?.code ?? "?"} → {r.targetCurrency?.code ?? "?"}
                    </TableCell>
                    <TableCell>
                      {r.rate} {r.targetCurrency?.symbol ?? r.targetCurrency?.code}
                    </TableCell>
                    <TableCell className="text-muted-foreground">{r.source}</TableCell>
                    <TableCell className="text-right">
                      <Button variant="ghost" size="icon" onClick={() => openEdit(r)}>
                        <Pencil className="size-4" />
                      </Button>
                      <Button variant="ghost" size="icon" onClick={() => setDeleteTarget(r)}>
                        <Trash2 className="size-4 text-destructive-text" />
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>

      <Dialog open={formOpen} onOpenChange={setFormOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{editingItem ? "Modifier le taux" : "Nouveau taux de change"}</DialogTitle>
          </DialogHeader>
          <div className="space-y-4">
            <div className="space-y-2">
              <Label>Devise de base</Label>
              <Input value={BASE_CURRENCY_CODE} disabled />
            </div>
            <div className="space-y-2">
              <Label>Devise cible</Label>
              <Select value={targetCode} onValueChange={setTargetCode} disabled={!!editingItem}>
                <SelectTrigger className="w-full">
                  <SelectValue placeholder="Choisir une devise" />
                </SelectTrigger>
                <SelectContent>
                  {targetCurrencies.map((c) => (
                    <SelectItem key={c.code} value={c.code ?? ""}>
                      {c.code} — {c.label}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>
            <div className="space-y-2">
              <Label htmlFor="rate">
                Taux (1 {BASE_CURRENCY_CODE} = combien de {targetCode || "..."})
              </Label>
              <Input
                id="rate"
                type="number"
                step="0.0001"
                min="0"
                value={rateValue}
                onChange={(e) => setRateValue(e.target.value)}
                placeholder="ex: 0.092"
              />
            </div>
            <div className="space-y-2">
              <Label htmlFor="source">Source</Label>
              <Input
                id="source"
                value={source}
                onChange={(e) => setSource(e.target.value)}
                placeholder="ex: Saisie manuelle, Bank Al-Maghrib..."
              />
            </div>
            {formError && <p className="text-sm text-destructive-text">{formError}</p>}
          </div>
          <DialogFooter>
            <Button variant="outline" onClick={() => setFormOpen(false)} disabled={saving}>
              Annuler
            </Button>
            <Button onClick={handleSave} disabled={saving}>
              {saving ? "Enregistrement..." : "Enregistrer"}
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      <AlertDialog open={!!deleteTarget} onOpenChange={(open) => !open && setDeleteTarget(null)}>
        <AlertDialogContent>
          <AlertDialogHeader>
            <AlertDialogTitle>Supprimer ce taux de change ?</AlertDialogTitle>
            <AlertDialogDescription>
              {deleteTarget?.baseCurrency?.code} → {deleteTarget?.targetCurrency?.code} sera
              supprimé. Cette action est irréversible.
            </AlertDialogDescription>
          </AlertDialogHeader>
          <AlertDialogFooter>
            <AlertDialogCancel>Annuler</AlertDialogCancel>
            <AlertDialogAction onClick={handleDelete}>Supprimer</AlertDialogAction>
          </AlertDialogFooter>
        </AlertDialogContent>
      </AlertDialog>
    </div>
  );
}
