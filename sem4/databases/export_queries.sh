#!/bin/bash

output="L3_kwerendy.sql"
> "$output"

for num in 01 03 08 10 14 21 23 30 33 36; do
    echo "-- ============ L3z${num} ============" >> "$output"
    mdb-queries ./baza.accdb "L2z${num}" | sed "s/L2z${num}/L3z${num}/g" >> "$output" 2>/dev/null
    echo "" >> "$output"
done

echo "Gotowe: $output"
