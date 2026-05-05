# fix_schema.py
import re

with open('schema.sql', 'r', encoding='utf-8') as f:
    content = f.read()

# Zamień ALTER TABLE ... ADD PRIMARY KEY na wersję z nazwą
def fix_pk(match):
    table = match.group(1)
    col = match.group(2)
    return f'ALTER TABLE `{table}` ADD CONSTRAINT `pk_{table}` PRIMARY KEY (`{col}`);'

content = re.sub(
    r'ALTER TABLE `(\w+)` ADD PRIMARY KEY \(`(\w+)`\);',
    fix_pk,
    content
)

with open('schema_fixed.sql', 'w', encoding='utf-8') as f:
    f.write(content)

print("Gotowe! Użyj schema_fixed.sql")
