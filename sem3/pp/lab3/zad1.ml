let rec podziel lista =
  match lista with
  | [] -> ([], [])
  | x :: xs ->
    let (parzyste, nieparzyste) = podziel xs in
    match x mod 2 with
      | 0 -> (x :: parzyste, nieparzyste)
      | _ -> (parzyste, x :: nieparzyste)

podziel [3;6;8;9;13];;
podziel [10; 15; 23; 40; 7; 8; 5; 12];;
podziel [];;
podziel [1; 2; 3; 4];;
podziel [5; 10; 15; 20];;
podziel [-5; -2; 0; 3; 4];;