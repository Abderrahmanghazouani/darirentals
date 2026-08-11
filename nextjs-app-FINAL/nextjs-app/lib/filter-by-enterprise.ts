interface HasScope {
  enterprise?: { id?: number | null } | null;
  property?: { enterprise?: { id?: number | null } | null } | null;
}

export function filterByEnterprise<T>(items: T[], enterpriseId: number | null): T[] {
  if (enterpriseId == null) return items;

  return items.filter((item) => {
    const scoped = item as unknown as HasScope;
    if (scoped.enterprise !== undefined) {
      return scoped.enterprise?.id === enterpriseId;
    }
    if (scoped.property !== undefined) {
      return scoped.property?.enterprise?.id === enterpriseId;
    }
    return true;
  });
}