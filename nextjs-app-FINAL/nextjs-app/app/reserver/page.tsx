"use client";

import { createElement, useEffect, useState } from "react";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Card, CardContent } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Building2, Home, MapPin, Phone } from "lucide-react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogFooter,
} from "@/components/ui/dialog";
import {
  fetchPublicProperties,
  fetchPublicCurrencies,
  fetchPublicExchangeRates,
  submitReservationRequest,
  PublicPropertyDto,
} from "@/lib/public-api";
import { CurrencyProvider, useCurrency } from "@/lib/currency/currency-context";
import { CurrencySelector } from "@/components/currency/currency-selector";
import { LanguageToggle } from "@/components/i18n/language-toggle";
import { ThemeToggle } from "@/components/theme/theme-toggle";
import { useLanguage } from "@/lib/i18n/language-context";

const CONTACT_PHONE = "+212 6 XX XX XX XX";

function MapLink(props: { lat: number; lng: number; label: string }) {
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
    " " + props.label
  );
}

export default function ReserverPage() {
  return (
    <CurrencyProvider fetchCurrencies={fetchPublicCurrencies} fetchRates={fetchPublicExchangeRates}>
      <ReserverContent />
    </CurrencyProvider>
  );
}

function ReserverContent() {
  const { format } = useCurrency();
  const { dict } = useLanguage();
  const [properties, setProperties] = useState<PublicPropertyDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [selected, setSelected] = useState<PublicPropertyDto | null>(null);
  const [submitting, setSubmitting] = useState(false);
  const [done, setDone] = useState(false);
  // Stocke la NATURE de l'erreur, pas un texte déjà résolu : si l'utilisateur bascule la langue
  // après avoir vu une erreur, le message doit se retraduire, pas rester figé dans l'ancienne
  // langue (le message serveur brut, lui, reste tel quel - pas de traduction possible côté
  // frontend pour un texte qui vient du backend).
  const [errorKind, setErrorKind] = useState<"missingFields" | "server" | "generic" | null>(null);
  const [serverErrorMessage, setServerErrorMessage] = useState("");
  const error =
    errorKind === "missingFields"
      ? dict.reserver.minimumFieldsError
      : errorKind === "server"
        ? serverErrorMessage
        : errorKind === "generic"
          ? dict.reserver.genericSubmitError
          : null;

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
    setErrorKind(null);
    setFullName("");
    setPhone("");
    setCheckIn("");
    setCheckOut("");
    setMessage("");
  }

  async function handleSubmit() {
    if (!selected) return;
    if (!fullName.trim() || !phone.trim()) {
      setErrorKind("missingFields");
      return;
    }
    setSubmitting(true);
    setErrorKind(null);
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
      if (e instanceof Error && e.message) {
        setErrorKind("server");
        setServerErrorMessage(e.message);
      } else {
        setErrorKind("generic");
      }
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="max-w-5xl mx-auto p-6 space-y-6">
      <div className="flex justify-end gap-2">
        <ThemeToggle />
        <LanguageToggle />
      </div>

      <div className="text-center space-y-2 py-6">
        <div className="flex items-center justify-center gap-2.5">
          <div className="flex size-8 items-center justify-center rounded-md bg-primary text-primary-foreground">
            <Building2 className="size-4.5" />
          </div>
          <span className="text-lg font-semibold tracking-tight">DariRentals</span>
        </div>
        <h1 className="text-2xl font-bold">{dict.reserver.heroTitle}</h1>
        <p className="text-muted-foreground">{dict.reserver.heroSubtitle}</p>
        <p className="text-sm text-muted-foreground flex items-center justify-center gap-1">
          <Phone className="size-4" /> {CONTACT_PHONE}
        </p>
        <div className="flex items-center justify-center gap-2 pt-1">
          <span className="text-xs text-muted-foreground">{dict.reserver.displayPricesIn}</span>
          <CurrencySelector />
        </div>
      </div>

      {loading ? (
        <p className="text-center text-muted-foreground">{dict.reserver.loadingProperties}</p>
      ) : properties.length === 0 ? (
        <p className="text-center text-muted-foreground">{dict.reserver.noProperties}</p>
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
                  {p.capacity != null && (
                    <p>
                      {p.capacity} {dict.reserver.people}
                    </p>
                  )}
                  {p.latitude != null && p.longitude != null && (
                    <MapLink lat={p.latitude} lng={p.longitude} label={dict.reserver.viewPosition} />
                  )}
                </div>
                <p className="text-lg font-semibold">
                  {p.pricePerNight != null ? format(p.pricePerNight) : "—"}
                  <span className="text-xs text-muted-foreground font-normal"> {dict.reserver.perNight}</span>
                </p>
                <Button className="w-full" onClick={() => openRequestForm(p)}>
                  {dict.reserver.request}
                </Button>
              </CardContent>
            </Card>
          ))}
        </div>
      )}

      <Dialog open={selected != null} onOpenChange={(open) => (open ? undefined : setSelected(null))}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>
              {done ? dict.reserver.requestSentTitle : `${dict.reserver.requestForPrefix} ${selected?.name ?? ""}`}
            </DialogTitle>
          </DialogHeader>

          {done ? (
            <div className="text-center py-6 space-y-2">
              <p className="text-lg font-medium">{dict.reserver.thanksTitle}</p>
              <p className="text-sm text-muted-foreground">
                {dict.reserver.requestSentBody} {phone}.
              </p>
              <Button onClick={() => setSelected(null)} className="mt-4">
                {dict.common.close}
              </Button>
            </div>
          ) : (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <div className="space-y-2">
                  <Label>{dict.reserver.checkIn}</Label>
                  <Input type="date" value={checkIn} onChange={(e) => setCheckIn(e.target.value)} />
                </div>
                <div className="space-y-2">
                  <Label>{dict.reserver.checkOut}</Label>
                  <Input type="date" value={checkOut} onChange={(e) => setCheckOut(e.target.value)} />
                </div>
              </div>
              <div className="space-y-2">
                <Label>{dict.reserver.fullName}</Label>
                <Input value={fullName} onChange={(e) => setFullName(e.target.value)} />
              </div>
              <div className="space-y-2">
                <Label>{dict.reserver.phone}</Label>
                <Input value={phone} onChange={(e) => setPhone(e.target.value)} placeholder="+212 6..." />
              </div>
              <div className="space-y-2">
                <Label>{dict.reserver.messageOptional}</Label>
                <Input value={message} onChange={(e) => setMessage(e.target.value)} />
              </div>
              {error && <p className="text-sm text-destructive-text">{error}</p>}
              <DialogFooter>
                <Button variant="outline" onClick={() => setSelected(null)}>
                  {dict.common.cancel}
                </Button>
                <Button onClick={handleSubmit} disabled={submitting}>
                  {submitting ? dict.reserver.sending : dict.reserver.sendRequest}
                </Button>
              </DialogFooter>
            </div>
          )}
        </DialogContent>
      </Dialog>
    </div>
  );
}
