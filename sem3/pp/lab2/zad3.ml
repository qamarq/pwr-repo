let rec polacz strings separator =
  if strings = [] then
    ""
  else if List.tl strings = [] then
    List.hd strings
  else
    List.hd strings ^ separator ^ polacz (List.tl strings) separator;;

polacz ["To"; "jest"; "napis"] "-";;
polacz ["ocaml"; "jest"; "fajne"] " ";;
polacz [] ",";;
polacz ["sam"] ",";;