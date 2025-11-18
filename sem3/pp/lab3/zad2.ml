let polacz lista1 lista2 =
  let rec laczenie wynik l1 l2 = 
    match (l1, l2) with
    | ([], _) -> wynik @ l2
    | (_, []) -> wynik @ l1
    | (x1::xs1, x2::xs2) -> laczenie (wynik @ [x1; x2]) xs1 xs2
  in laczenie [] lista1 lista2

polacz [5;4;3;2] [1;2;3;4;5;6];;
polacz [1; 2; 3] [4; 5; 6];;
polacz [] [1; 2; 3];;
polacz [] [];;
