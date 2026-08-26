"use client";

import { createElement, useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Home, MapPin, Phone } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import {
  fetchPublicProperties,
  submitReservationRequest,
  PublicPropertyDto,
} from "@/lib/public-api";

const CONTACT_PHONE = "+212 6 XX XX XX XX";

function MapLink(props: { lat: number; lng: number }) {
  const url = "https://www.google.com/maps?q=" + props.lat + "," + props.lng;
  return createElement(
    "a",
    {
      href: url,
      target: "_blank",
      rel: "noopener noreferrer",
      className: "inline-flex items-center gap-1 text-primary hover:underline",
    },
    createElement(MapPin, { className: "size-3.5" }),
    " Voir la position"
  );
}

export default function ReserverPage() {
  const [properties, setProperties] = useState<PublicPropertyDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState<PublicPropertyDto | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [done, setDone] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const [fullName, setFullName] = useState("");
  const [phone, setPhone] = useState("");
  const [checkIn, setCheckIn] = useState("");
  const [checkOut, setCheckOut] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    fetchPublicProperties()
      .then((data) => setProperties(data ?? []))
      .catch(() => setProperties([]))
      .finally(() => setLoading(false));
  }, []);

  function openRequestForm(p: PublicPropertyDto) {
    setSelected(p);
    setDone(false);
    setError(null);
    setFullName("");
    setPhone("");
    setCheckIn("");
    setCheckOut("");
    setMessage("");
  }

  async function handleSubmit() {
    if (!selected) return;
    if (!fullName.trim() || !phone.trim()) {
      setError("Renseigne au moins ton nom et ton téléphone.");
      return;
    }
    setSubmitting(true);
    setError(null);
    try {
      await submitReservationRequest({
        propertyId: selected.id,
        fullName,
        phone,
        checkIn,
        checkOut,
        message,
      });
      setDone(true);
    } catch (e) {
      setError(e instanceof Error ? e.message : "Erreur lors de l'envoi");
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="max-w-5xl mx-auto p-6 space-y-6">
      <div className="text-center space-y-2 py-6">
        <h1 className="text-2xl font-bold">Nos logements disponibles</h1>
        <p className="text-muted-foreground">
          Choisissez un logement et envoyez-nous votre demande — nous vous répondons rapidement.
        </p>
        <p className="text-sm text-muted-foreground flex items-center justify-center gap-1">
          <Phone className="size-4" /> {CONTACT_PHONE}
        </p>
      </div>

      {loading ? (
        <p className="text-center text-muted-foreground">Chargement...</p>
      ) : properties.length === 0 ? (
        <p className="text-center text-muted-foreground">Aucun logement disponible pour le moment.</p>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4">
          {properties.map((p) => (
            <Card key={p.id}>
              <CardContent className="pt-6 space-y-3">
                <div className="h-32 rounded-md bg-muted flex items-center justify-center">
                  <Home className="size-8 text-muted-foreground" />
                </div>
                <div>
                  <p className="font-semibold">{p.name}</p>
                  {p.propertyType && (
                    <Badge variant="outline" className="mt-1">
                      {p.propertyType.label}
                    </Badge>
                  )}
                </div>
                <div className="text-sm text-muted-foreground space-y-1">
                  {p.capacity != null && <p>{p.capacity} personnes</p>}
                  {p.latitude != null && p.longitude != null && (
                    <MapLink lat={p.latitude} lng={p.longitude} />
                  )}
                </div>
                <p className="text-lg font-semibold">
                  {p.pricePerNight != null ? p.pricePerNight + " MAD" : "—"}
                  <span className="text-xs text-muted-foreground font-normal"> / nuit</span>
                </p>
                <Button className="w-full" onClick={() => openRequestForm(p)}>
                  Demander
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      <Dialog open={selected != null} onOpenChange={(open) => (open ? undefined : setSelected(null))}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{done ? "Demande envoyée" : "Demande pour : " + (selected?.name ?? "")}</DialogTitle>
          </DialogHeader>

          {done ? (
            <div className="text-center py-6 space-y-2">
              <p className="text-lg font-medium">Merci !</p>
              <p className="text-sm text-muted-foreground">
                Votre demande a bien été envoyée. Nous vous recontactons rapidement au {phone}.
              </p>
              <Button onClick={() => setSelected(null)} className="mt-4">
                Fermer
              </Button>
            </div>
          ) : (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>Arrivée</Label>
                  <Input type="date" value={checkIn} onChange={(e) => setCheckIn(e.target.value)} />
                </div>
                <div className="space-y-2">
                  <Label>Départ</Label>
                  <Input type="date" value={checkOut} onChange={(e) => setCheckOut(e.target.value)} />
                </div>
              </div>
              <div className="space-y-2">
                <Label>Nom complet</Label>
                <Input value={fullName} onChange={(e) => setFullName(e.target.value)} />
              </div>
              <div className="space-y-2">
                <Label>Téléphone</Label>
                <Input value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="+212 6..." />
              </div>
              <div className="space-y-2">
                <Label>Message (optionnel)</Label>
                <Input value={message} onChange={(e) => setMessage(e.target.value)} />
              </div>
              {error && <p className="text-sm text-destructive">{error}</p>}
              <DialogFooter>
                <Button variant="outline" onClick={() => setSelected(null)}>
                  Annuler
                </Button>
                <Button onClick={handleSubmit} disabled={submitting}>
                  {submitting ? "Envoi..." : "Envoyer la demande"}
                </Button>
              </DialogFooter>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}