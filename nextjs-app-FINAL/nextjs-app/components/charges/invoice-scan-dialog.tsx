"use client";

import { useEffect, useRef, useState } from "react";
import { Button } from "@/components/ui/button";
import { Dialog, DialogContent, DialogHeader, DialogTitle } from "@/components/ui/dialog";
import { Loader2, ScanLine, Upload, AlertTriangle, ImageIcon } from "lucide-react";
import { getEntityClients } from "@/lib/api";
import { Role } from "@/lib/api-client";
import { scanInvoice, InvoiceScanError, InvoiceScanResult } from "@/lib/invoice-scan-api";
import { useSelectedEnterpriseId } from "@/lib/use-selected-enterprise";
import { useCurrentCollaboratorId } from "@/lib/use-current-collaborator";
import { ChargeForm } from "@/components/charges/charge-form";
import { ChargeDto } from "@/lib/types/Charge";
import { DocumentDto } from "@/lib/types/Document";
import { ChargeTypeDto } from "@/lib/types/ChargeType";
import { DocumentTypeDto } from "@/lib/types/DocumentType";
import { PropertyDto } from "@/lib/types/Property";

interface InvoiceScanDialogProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  role: Role;
  defaultPropertyId?: number | null;
  onCreated: () => void;
}

type Step = "upload" | "scanning" | "verify" | "error";

export function InvoiceScanDialog({
  open,
  onOpenChange,
  role,
  defaultPropertyId,
  onCreated,
}: InvoiceScanDialogProps) {
  const enterpriseId = useSelectedEnterpriseId();
  const collaboratorId = useCurrentCollaboratorId(role);

  const [step, setStep] = useState<Step>("upload");
  const [file, setFile] = useState<File | null>(null);
  const [previewUrl, setPreviewUrl] = useState<string | null>(null);
  const [scanResult, setScanResult] = useState<InvoiceScanResult | null>(null);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [saving, setSaving] = useState(false);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const [chargeTypes, setChargeTypes] = useState<ChargeTypeDto[]>([]);
  const [documentTypes, setDocumentTypes] = useState<DocumentTypeDto[]>([]);
  const [properties, setProperties] = useState<PropertyDto[]>([]);

  useEffect(() => {
    if (!open) return;
    const clients = getEntityClients(role);
    clients.chargeType.findAll().then((d) => setChargeTypes(d ?? [])).catch(() => setChargeTypes([]));
    clients.documentType.findAll().then((d) => setDocumentTypes(d ?? [])).catch(() => setDocumentTypes([]));
    clients.property.findAll().then((d) => setProperties(d ?? [])).catch(() => setProperties([]));
  }, [open, role]);

  // Réinitialise proprement à chaque ouverture.
  useEffect(() => {
    if (open) {
      setStep("upload");
      setFile(null);
      setPreviewUrl(null);
      setScanResult(null);
      setErrorMessage(null);
    }
  }, [open]);

  function handlePickFile(f: File | null) {
    setFile(f);
    if (previewUrl) URL.revokeObjectURL(previewUrl);
    setPreviewUrl(f ? URL.createObjectURL(f) : null);
  }

  async function handleScan() {
    if (!file) return;
    setStep("scanning");
    setErrorMessage(null);
    try {
      const result = await scanInvoice(file, role, { enterpriseId, collaboratorId });
      setScanResult(result);
      setStep("verify");
    } catch (e) {
      const message =
        e instanceof InvoiceScanError
          ? e.message
          : "Le scan a échoué. Tu peux remplir le formulaire manuellement.";
      setErrorMessage(message);
      setStep("error");
    }
  }

  function findChargeTypeByLabel(label?: string | null): ChargeTypeDto | null {
    if (!label) return null;
    const needle = label.toLowerCase();
    return chargeTypes.find((t) => t.label?.toLowerCase().includes(needle) || needle.includes((t.label ?? "").toLowerCase())) ?? null;
  }

  function findInvoiceDocumentType(): DocumentTypeDto | null {
    return (
      documentTypes.find((t) => {
        const probe = `${t.code ?? ""} ${t.label ?? ""}`.toLowerCase();
        return probe.includes("facture") || probe.includes("invoice");
      }) ?? null
    );
  }

  async function handleValidate(dto: ChargeDto) {
    setSaving(true);
    try {
      const clients = getEntityClients(role);
      const savedCharge = await clients.charge.create(dto);

      // Archive le document original (preuve) rattaché à la charge fraîchement créée,
      // uniquement si on vient bien du flux scan (scanResult présent).
      if (scanResult && savedCharge?.id != null) {
        const documentPayload: DocumentDto = {
          id: null,
          fileName: scanResult.fileName || file?.name || "facture",
          file: scanResult.documentToken,
          extractedVendor: scanResult.extractedVendor ?? "",
          extractedAmount: scanResult.extractedAmount ?? null,
          extractedDate: scanResult.extractedDate ?? null,
          documentType: findInvoiceDocumentType(),
          charge: { id: savedCharge.id } as ChargeDto,
        };
        await clients.document.create(documentPayload);
      }

      onCreated();
      onOpenChange(false);
    } catch {
      setErrorMessage("La charge a été créée mais l'archivage du document a échoué. Tu peux le rattacher manuellement plus tard.");
    } finally {
      setSaving(false);
    }
  }

  const prefillCharge: ChargeDto | null =
    step === "verify" && scanResult
      ? ({
          id: null,
          label: scanResult.extractedVendor
            ? `Facture ${scanResult.extractedVendor}`
            : "Facture scannée",
          amount: scanResult.extractedAmount ?? null,
          property:
            defaultPropertyId != null
              ? (properties.find((p) => p.id === defaultPropertyId) ?? ({ id: defaultPropertyId } as PropertyDto))
              : null,
          chargeType: findChargeTypeByLabel(scanResult.suggestedChargeTypeLabel),
          payment: null,
        } as ChargeDto)
      : null;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle className="flex items-center gap-2">
            <ScanLine className="size-5" /> Scanner une facture
          </DialogTitle>
        </DialogHeader>

        {step === "upload" && (
          <div className="space-y-4">
            <p className="text-sm text-muted-foreground">
              Upload une photo de la facture (JPG, PNG, WEBP). L&apos;IA propose un montant, une date et un
              prestataire — tu vérifies et corriges avant de valider, rien n&apos;est enregistré automatiquement.
            </p>

            <input
              ref={fileInputRef}
              type="file"
              accept="image/png,image/jpeg,image/webp"
              className="hidden"
              onChange={(e) => handlePickFile(e.target.files?.[0] ?? null)}
            />

            <button
              type="button"
              onClick={() => fileInputRef.current?.click()}
              className="w-full border-2 border-dashed rounded-md p-6 flex flex-col items-center gap-2 hover:bg-accent transition-colors"
            >
              {previewUrl ? (
                // eslint-disable-next-line @next/next/no-img-element
                <img src={previewUrl} alt="Aperçu facture" className="max-h-48 rounded object-contain" />
              ) : (
                <>
                  <ImageIcon className="size-8 text-muted-foreground" />
                  <span className="text-sm text-muted-foreground">Clique pour choisir une image</span>
                </>
              )}
            </button>
            {file && <p className="text-xs text-muted-foreground truncate">{file.name}</p>}

            <div className="flex justify-end gap-2">
              <Button type="button" variant="outline" onClick={() => onOpenChange(false)}>
                Annuler
              </Button>
              <Button type="button" onClick={handleScan} disabled={!file}>
                <Upload className="size-4" /> Analyser avec l&apos;IA
              </Button>
            </div>
          </div>
        )}

        {step === "scanning" && (
          <div className="py-10 flex flex-col items-center gap-3 text-muted-foreground">
            <Loader2 className="size-8 animate-spin" />
            <p className="text-sm">Analyse de la facture en cours...</p>
          </div>
        )}

        {step === "error" && (
          <div className="space-y-4">
            <div className="flex items-start gap-2 rounded-md border border-destructive/30 bg-destructive/5 p-3">
              <AlertTriangle className="size-4 text-destructive shrink-0 mt-0.5" />
              <p className="text-sm text-destructive">{errorMessage}</p>
            </div>
            <div className="flex justify-end gap-2">
              <Button type="button" variant="outline" onClick={() => setStep("upload")}>
                Réessayer
              </Button>
              <Button
                type="button"
                onClick={() => {
                  setScanResult(null);
                  setStep("verify");
                }}
              >
                Remplir manuellement
              </Button>
            </div>
          </div>
        )}

        {step === "verify" && (
          <div className="space-y-3">
            {scanResult?.warning && (
              <div className="flex items-start gap-2 rounded-md border border-amber-300 bg-amber-50 p-3">
                <AlertTriangle className="size-4 text-amber-600 shrink-0 mt-0.5" />
                <p className="text-sm text-amber-800">{scanResult.warning}</p>
              </div>
            )}
            {!scanResult && (
              <p className="text-sm text-muted-foreground">
                Saisie manuelle — aucune donnée extraite automatiquement.
              </p>
            )}
            <p className="text-xs text-muted-foreground">
              Vérifie et corrige les champs ci-dessous avant de valider. Rien n&apos;est enregistré tant que tu
              n&apos;as pas cliqué sur Enregistrer.
            </p>
            <ChargeForm
              initial={prefillCharge}
              defaultPropertyId={defaultPropertyId}
              saving={saving}
              role={role}
              onSubmit={handleValidate}
              onCancel={() => onOpenChange(false)}
            />
          </div>
        )}
      </DialogContent>
    </Dialog>
  );
}
