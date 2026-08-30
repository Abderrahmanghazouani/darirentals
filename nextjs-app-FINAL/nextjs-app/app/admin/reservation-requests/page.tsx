"use client";

import { useEffect, useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { StatusBadge } from "@/components/status-badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import { useRequireRole } from "@/lib/use-require-role";
import { useEntityCrud } from "@/lib/use-entity-crud";
import { getEntityClients } from "@/lib/api";
import { ReservationRequestDto } from "@/lib/types/ReservationRequest";

const ROLE = "admin" as const;

const STATUS_IDS: Record<"Confirmee" | "Rejetee", number> = {
  Confirmee: 1,
  Rejetee: 2,
};

export default function ReservationRequestsPage() {
  const ready = useRequireRole(ROLE);

  const client = useMemo(() => getEntityClients(ROLE).reservationRequest, []);
  const crud = useEntityCrud<ReservationRequestDto>(client);

  const [filterStatus, setFilterStatus] = useState<string>("EnAttente");
  const [actingId, setActingId] = useState<number | null>(null);
  const [detailed, setDetailed] = useState<ReservationRequestDto[]>([]);

  useEffect(() => {
    if (crud.items.length === 0) {
      setDetailed([]);
      return;
    }
    Promise.all(
      crud.items
        .filter((r) => r.id != null)
        .map((r) => client.findById(r.id as number).catch(() => r))
    ).then(setDetailed);
  }, [crud.items, client]);

  const filteredItems = useMemo(() => {
    if (filterStatus === "all") return detailed;
    return detailed.filter((r) => r.reservationRequestStatus?.code === filterStatus);
  }, [detailed, filterStatus]);

  async function changeStatus(item: ReservationRequestDto, code: "Confirmee" | "Rejetee") {
    if (item.id == null) return;
    setActingId(item.id);
    try {
      await client.update({
        ...item,
        reservationRequestStatus: { id: STATUS_IDS[code] } as ReservationRequestDto["reservationRequestStatus"],
      });
      await crud.refresh();
    } finally {
      setActingId(null);
    }
  }

  if (!ready) return null;

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between">
          <CardTitle>Demandes de réservation</CardTitle>
          <Select value={filterStatus} onValueChange={setFilterStatus}>
            <SelectTrigger className="w-[200px]">
              <SelectValue placeholder="Statut" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="all">Tous les statuts</SelectItem>
              <SelectItem value="EnAttente">En attente</SelectItem>
              <SelectItem value="Confirmee">Confirmées</SelectItem>
              <SelectItem value="Rejetee">Rejetées</SelectItem>
            </SelectContent>
          </Select>
        </CardHeader>
        <CardContent>
          {crud.error && <p className="text-destructive text-sm mb-4">{crud.error}</p>}
          {crud.loading ? (
            <p className="text-muted-foreground text-sm py-8 text-center">Chargement...</p>
          ) : filteredItems.length === 0 ? (
            <p className="text-muted-foreground text-sm py-8 text-center">Aucune demande.</p>
          ) : (
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>Client</TableHead>
                  <TableHead>Téléphone</TableHead>
                  <TableHead>Propriété</TableHead>
                  <TableHead>Note</TableHead>
                  <TableHead>Statut</TableHead>
                  <TableHead className="text-right">Actions</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {filteredItems.map((r) => (
                  <TableRow key={r.id}>
                    <TableCell>{r.client?.fullName ?? "—"}</TableCell>
                    <TableCell>{r.client?.phone ?? "—"}</TableCell>
                    <TableCell>{r.requestedProperty?.name ?? "—"}</TableCell>
                    <TableCell className="max-w-[220px] whitespace-normal text-sm text-muted-foreground">
                      {r.clientNote || "—"}
                    </TableCell>
                    <TableCell>
                      <StatusBadge status={r.reservationRequestStatus} />
                    </TableCell>
                    <TableCell className="text-right space-x-1">
                      {r.reservationRequestStatus?.code === "EnAttente" && (
                        <>
                          <Button
                            size="sm"
                            variant="outline"
                            disabled={actingId === r.id}
                            onClick={() => changeStatus(r, "Rejetee")}
                          >
                            Refuser
                          </Button>
                          <Button
                            size="sm"
                            disabled={actingId === r.id}
                            onClick={() => changeStatus(r, "Confirmee")}
                          >
                            Confirmer
                          </Button>
                        </>
                      )}
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          )}
        </CardContent>
      </Card>
    </div>
  );
}