"use client";

import { useCallback, useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { UnauthorizedError } from "./api-client";
import { logout } from "./auth";

interface EntityClientLike<TDto extends { id: number | null }> {
  findAll(): Promise<TDto[]>;
  create(dto: TDto): Promise<TDto>;
  update(dto: TDto): Promise<TDto>;
  remove(id: number): Promise<void>;
}

/**
 * Hook générique : liste + création + édition + suppression pour une entité.
 * Remplace les *.service.ts + logique des composants list/create/edit Angular.
 */
export function useEntityCrud<TDto extends { id: number | null }>(
  client: EntityClientLike<TDto>
) {
  const router = useRouter();
  const [items, setItems] = useState<TDto[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  const [formOpen, setFormOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<TDto | null>(null);

  const [deleteTarget, setDeleteTarget] = useState<TDto | null>(null);
  const [saving, setSaving] = useState(false);

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
      const data = await client.findAll();
      setItems(data ?? []);
    } catch (e) {
      if (!handleAuthError(e)) {
        setError(e instanceof Error ? e.message : "Erreur de chargement");
      }
    } finally {
      setLoading(false);
    }
  }, [client, handleAuthError]);

  useEffect(() => {
    refresh();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const openCreate = useCallback(() => {
    setEditingItem(null);
    setFormOpen(true);
  }, []);

  const openEdit = useCallback((item: TDto) => {
    setEditingItem(item);
    setFormOpen(true);
  }, []);

  const closeForm = useCallback(() => {
    setFormOpen(false);
    setEditingItem(null);
  }, []);

  const submit = useCallback(
    async (dto: TDto) => {
      setSaving(true);
      try {
        if (dto.id != null) {
          await client.update(dto);
        } else {
          await client.create(dto);
        }
        await refresh();
        closeForm();
      } catch (e) {
        if (!handleAuthError(e)) {
          setError(e instanceof Error ? e.message : "Erreur d'enregistrement");
        }
      } finally {
        setSaving(false);
      }
    },
    [client, refresh, closeForm, handleAuthError]
  );

  const confirmDelete = useCallback(async () => {
    if (deleteTarget?.id == null) return;
    setSaving(true);
    try {
      await client.remove(deleteTarget.id);
      setDeleteTarget(null);
      await refresh();
    } catch (e) {
      if (!handleAuthError(e)) {
        setError(e instanceof Error ? e.message : "Erreur de suppression");
      }
    } finally {
      setSaving(false);
    }
  }, [client, deleteTarget, refresh, handleAuthError]);

  return {
    items,
    loading,
    error,
    saving,
    refresh,
    formOpen,
    editingItem,
    openCreate,
    openEdit,
    closeForm,
    submit,
    deleteTarget,
    setDeleteTarget,
    confirmDelete,
  };
}
