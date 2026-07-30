import re, os

SRC = "/home/claude/frontend_work/frontend/src/app/shared/model"
OUT = "/home/claude/nextjs-app/lib/types"

type_map = {
    "string": "string",
    "number": "number",
    "boolean": "boolean",
}

# regex to capture: public fieldName: null | TypeDto ;   OR   public fieldName: string;  OR  public fieldName: Array<XDto>;
field_re = re.compile(
    r'public\s+(\w+)\s*:\s*(null\s*\|\s*)?([\w<>]+)\s*;'
)

DATE_TYPES = {"Date", "LocalDateTime", "LocalDate", "LocalTime", "Instant"}

def ts_type(java_ts_type: str) -> str:
    m = re.match(r'Array<(\w+)>', java_ts_type)
    if m:
        inner = m.group(1)
        return f"{inner}[]"
    if java_ts_type in DATE_TYPES:
        return "string"  # date ISO string côté JSON
    if java_ts_type in ("string", "number", "boolean"):
        return java_ts_type
    return java_ts_type  # already a *Dto type name, e.g. PropertyDto

entities = []
for root, dirs, files in os.walk(SRC):
    for f in sorted(files):
        if not f.endswith(".model.ts"):
            continue
        path = os.path.join(root, f)
        content = open(path, encoding='utf-8', errors='replace').read()
        cls_m = re.search(r'export class (\w+)Dto extends BaseDto\s*\{', content)
        if not cls_m:
            continue
        name = cls_m.group(1)
        entities.append(name)
        fields = []
        for fm in field_re.finditer(content):
            fname, nullable, ftype = fm.groups()
            is_nullable = bool(nullable) or ftype in DATE_TYPES
            fields.append((fname, ts_type(ftype), is_nullable))
        # dedupe preserving order
        seen = set()
        uniq_fields = []
        for fld in fields:
            if fld[0] in seen:
                continue
            seen.add(fld[0])
            uniq_fields.append(fld)

        # find referenced Dto types for imports
        referenced = sorted(set(re.match(r'(\w+)', t).group(1) for _, t, _ in uniq_fields if t.endswith("Dto") or t.replace("[]","").endswith("Dto")))
        referenced = [r.replace("[]","") for r in referenced]
        referenced = sorted(set(r for r in referenced if r != f"{name}Dto"))

        lines = []
        lines.append(f"// Auto-generated from Angular model: {f}")
        for ref in referenced:
            lines.append(f'import {{ {ref} }} from "./{ref.replace("Dto","")}";')
        if referenced:
            lines.append("")
        lines.append(f"export interface {name}Dto {{")
        lines.append("  id: number | null;")
        for fname, ftype, nullable in uniq_fields:
            opt = "?" if nullable else ""
            lines.append(f"  {fname}{opt}: {ftype} | null;" if nullable else f"  {fname}: {ftype};")
        lines.append("}")
        lines.append("")
        lines.append(f"export function new{name}Dto(): {name}Dto {{")
        lines.append("  return {")
        lines.append("    id: null,")
        for fname, ftype, nullable in uniq_fields:
            if ftype == "string":
                default = "null" if nullable else "''"
            elif ftype == "number":
                default = "null"
            elif ftype == "boolean":
                default = "null" if nullable else "false"
            elif ftype.endswith("[]"):
                default = "[]"
            else:
                default = "null"
            lines.append(f"    {fname}: {default},")
        lines.append("  };")
        lines.append("}")
        lines.append("")

        outpath = os.path.join(OUT, f"{name}.ts")
        open(outpath, "w", encoding='utf-8').write("\n".join(lines))

print(f"{len(entities)} entities converted:")
print(", ".join(entities))
