import { API_BASE, authHeaders, Role } from "./api-client";

export interface InvoiceScanResult {
  documentToken: string;
  fileName: string;
  extractedAmount?: number | null;
  extractedDate?: string | null; // "yyyy-MM-dd"
  extractedVendor?: string | null;
  suggestedChargeTypeLabel?: string | null;
  tokensUsed?: number | null;
  warning?: string | null;
}

export class InvoiceScanError extends Error {
  status: number;
  constructor(message: string, status: number) {
    super(message);
    this.status = status;
  }
}

/**
 * Envoie une photo/scan de facture à l'IA pour extraction automatique.
 * Ne crée rien en base — l'utilisateur doit valider via le formulaire de charge ensuite.
 */
export async function scanInvoice(
  file: File,
  role: Role,
  opts: { enterpriseId?: number | null; collaboratorId?: number | null } = {}
): Promise<InvoiceScanResult> {
  const formData = new FormData();
  formData.append("file", file);
  if (opts.enterpriseId != null) formData.append("enterpriseId", String(opts.enterpriseId));
  if (opts.collaboratorId != null) formData.append("collaboratorId", String(opts.collaboratorId));

  const res = await fetch(`${API_BASE}${role}/invoice-scan/analyze`, {
    method: "POST",
    headers: { ...authHeaders() }, // ne pas fixer Content-Type : le navigateur gère le boundary multipart
    body: formData,
  });

  if (!res.ok) {
    let message = `Erreur ${res.status}`;
    try {
      const data = await res.json();
      if (data?.message) message = data.message;
    } catch {
      // corps non-JSON, on garde le message générique
    }
    throw new InvoiceScanError(message, res.status);
  }

  return res.json();
}
