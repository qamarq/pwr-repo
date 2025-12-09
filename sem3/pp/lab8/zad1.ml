let rec insert lst x =
  match lst with
  | [] -> [x]
  | h :: t -> if x <= h then x :: lst else h :: (insert t x)
;;

(* testy *)
insert [1;3;5;7] 4;;
insert [] 10;;
insert [2;4;6] 1;;
insert [2;4;6] 8;;
insert [1;2;3] 2;;