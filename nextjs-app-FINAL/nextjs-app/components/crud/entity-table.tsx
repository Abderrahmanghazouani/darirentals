"use client";

import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Pencil, Trash2 } from "lucide-react";

export interface EntityColumn<TDto> {
  header: string;
  render: (item: TDto) => React.ReactNode;
}

interface EntityTableProps<TDto extends { id: number | null }> {
  items: TDto[];
  columns: EntityColumn<TDto>[];
  loading?: boolean;
  onEdit: (item: TDto) => void;
  onDelete: (item: TDto) => void;
}

export function EntityTable<TDto extends { id: number | null }>({
  items,
  columns,
  loading,
  onEdit,
  onDelete,
}: EntityTableProps<TDto>) {
  if (loading) {
    return <p className="text-muted-foreground text-sm py-8 text-center">Chargement...</p>;
  }

  if (items.length === 0) {
    return (
      <p className="text-muted-foreground text-sm py-8 text-center">
        Aucun élément pour le moment.
      </p>
    );
  }

  return (
    <Table>
      <TableHeader>
        <TableRow>
          {columns.map((col) => (
            <TableHead key={col.header}>{col.header}</TableHead>
          ))}
          <TableHead className="text-right">Actions</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {items.map((item) => (
          <TableRow key={item.id ?? Math.random()}>
            {columns.map((col) => (
              <TableCell key={col.header}>{col.render(item)}</TableCell>
            ))}
            <TableCell className="text-right space-x-1">
              <Button variant="ghost" size="icon" onClick={() => onEdit(item)}>
                <Pencil className="size-4" />
              </Button>
              <Button variant="ghost" size="icon" onClick={() => onDelete(item)}>
                <Trash2 className="size-4 text-destructive-text" />
              </Button>
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
}
