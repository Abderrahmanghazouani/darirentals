import re, os, json

SRC = "/home/claude/frontend_work/frontend/src/app/shared/model"
DATE_TYPES = {"Date", "LocalDateTime", "LocalDate", "LocalTime", "Instant"}
field_re = re.compile(r'public\s+(\w+)\s*:\s*(null\s*\|\s*)?([\w<>]+)\s*;')

meta = {}

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

        seen = set()
        scalar_fields = []   # (fieldName, kind, nullable)  kind in text/number/boolean/date
        relation_fields = [] # (fieldName, RelatedDtoName)  single relation (not array)
        list_fields = []     # (fieldName, RelatedDtoName)  array relation

        for fm in field_re.finditer(content):
            fname, nullable, ftype = fm.groups()
            if fname in seen:
                continue
            seen.add(fname)
            arr_m = re.match(r'Array<(\w+)>', ftype)
            if arr_m:
                list_fields.append((fname, arr_m.group(1)))
                continue
            if ftype == "string":
                scalar_fields.append((fname, "text", bool(nullable)))
            elif ftype == "number":
                scalar_fields.append((fname, "number", bool(nullable)))
            elif ftype == "boolean":
                scalar_fields.append((fname, "boolean", bool(nullable)))
            elif ftype in DATE_TYPES:
                scalar_fields.append((fname, "date", True))
            elif ftype.endswith("Dto"):
                relation_fields.append((fname, ftype))
            else:
                # unknown scalar-ish type, treat as text
                scalar_fields.append((fname, "text", bool(nullable)))

        meta[name] = {
            "scalars": scalar_fields,
            "relations": relation_fields,
            "lists": list_fields,
        }

json.dump(meta, open("/home/claude/entity_meta.json", "w"), indent=2)
print(f"{len(meta)} entities described")
# quick sanity print for a couple
for k in ["Currency", "Task", "Property"]:
    print(k, meta[k])
