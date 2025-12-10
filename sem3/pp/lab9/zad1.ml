module type LIST_OPERATIONS = sig
  val empty : int list -> int list
  val sum : int list -> int
  val filter_even : int list -> int -> int list
  val alternate_merge : int list -> int list -> int list
end

module ListOperations : LIST_OPERATIONS = struct
  let empty lst = []

  let sum lst =
    let rec pomocnicza wynik = function
      | [] -> wynik
      | x :: xs ->
          if x < 0 then pomocnicza (wynik + x) xs
          else pomocnicza wynik xs
    in
    pomocnicza 0 lst

  let filter_even lst liczba =
    let rec pomocnicza wynik = function
      | [] -> wynik
      | x :: xs -> 
        match liczba with
        | 0 -> failwith "dssadsa"
        | _ ->
          if x mod liczba = 0 then pomocnicza (x :: wynik) xs
          else pomocnicza wynik xs
      in
      pomocnicza [] lst

  let rec alternate_merge lst1 lst2 =
    match lst1, lst2 with
    | [], [] -> []
    | x :: xs, [] -> x :: alternate_merge xs []
    | [], y :: ys -> y :: alternate_merge [] ys
    | x :: xs, y :: ys -> x :: y :: alternate_merge xs ys
end

open ListOperations;;

(* testy na empty *)
empty [];;
empty [2];;

(* testy na sume *)
sum [1; -2; 3; -4; 5];;
sum [];;
sum [2; 5; 6; 2; 1];;
sum [-3; -2; -7];;

(* testy na filtrowanie *)
filter_even [3; 5; 2; 8; 1; 9; 12; 18; 19] 2;;
filter_even [] 5;;
filter_even [1; 3; 5; 7; 9] 2;;
filter_even [1; 3; 5; 7; 9] 3;;
filter_even [1; 3; 5; 7; 9] 0;;

(* testy na laczenie *)
alternate_merge [1; 2; 3] [3; 2; 1];;
alternate_merge [] [1; 2; 3];;
alternate_merge [1; 2] [5; 6; 7; 8];;
alternate_merge [] [];;
alternate_merge [5; -2; 10; 22] [-6; 3];;