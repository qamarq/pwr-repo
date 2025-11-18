(* bez rekurencji ogonowej *)
let rec podziel_nie_ogonowa lista dzielnik =
  match lista with
  | [] -> ([], [])
  | h :: t ->
      let (wieksze, mniejsze) = podziel_nie_ogonowa t dzielnik in
      if h > dzielnik then
        (h :: wieksze, mniejsze)
      else if h < dzielnik then
        (wieksze, h :: mniejsze)
      else
        (wieksze, mniejsze)

(* rekurencja ogonowa *)
let podziel lista dzielnik =
  let rec odwroc lista acc =
    match lista with
    | [] -> acc
    | h :: t -> odwroc t (h :: acc)
  in
  let rec podziel_pom lista dzielnik wynik_wieksze wynik_mniejsze =
    match lista with
    | [] -> (odwroc wynik_wieksze [], odwroc wynik_mniejsze [])
    | h :: t ->
        if h > dzielnik then
          podziel_pom t dzielnik (h :: wynik_wieksze) wynik_mniejsze
        else if h < dzielnik then
          podziel_pom t dzielnik wynik_wieksze (h :: wynik_mniejsze)
        else
          podziel_pom t dzielnik wynik_wieksze wynik_mniejsze
  in
  podziel_pom lista dzielnik [] []

podziel_nie_ogonowa [5;4;3;2;1] 3;;
podziel_nie_ogonowa [1;2;3;4;5] 3;;
podziel_nie_ogonowa [5;4] 3;;
podziel_nie_ogonowa [5;4] 4;;
podziel_nie_ogonowa [5;4;3] 4;;
podziel_nie_ogonowa [3;4;5] 4;;
podziel_nie_ogonowa [1;7;4;9;3;2;56;33] 6;;
podziel_nie_ogonowa [] 4;;

podziel [5;4;3;2;1] 3;;
podziel [1;2;3;4;5] 3;;
podziel [5;4] 3;;
podziel [5;4] 4;;
podziel [5;4;3] 4;;
podziel [3;4;5] 4;;
podziel [1;7;4;9;3;2;56;33] 6;;
podziel [] 4;;