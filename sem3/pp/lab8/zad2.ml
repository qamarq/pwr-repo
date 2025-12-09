type 'a llist = LKoniec | LElement of 'a * (unit -> 'a llist);;

let rec lfrom k = LElement (k, function () -> lfrom (k+1));;  
let rec ltake (n, lxs) =
  match (n, lxs) with
    (0, _) -> []
    | (_, LKoniec) -> []
    | (n, LElement(x,xf)) -> x::ltake(n-1, xf())
;;

let rec toLazyList xs =
  match xs with
    [] -> LKoniec
    | h::t -> LElement(h, function () -> toLazyList t);;


let lpolacz lst =
  let rec pomocnicza lxs =
    match lxs with
      LKoniec -> LKoniec
      | LElement (x, xf) ->
          let rec repeat n =
            if n <= 0 then LKoniec
            else LElement (x, function () -> repeat (n - 1))
          in
          let rest = pomocnicza (xf ()) in
          let rec append l1 l2 =
            match l1 with
              LKoniec -> l2
              | LElement (y, yf) -> LElement (y, function () -> append (yf ()) l2)
          in
          append (repeat x) rest
  in
  pomocnicza lst
;;

let polacz lst =
  let rec pomocnicza lxs =
    match lxs with
      [] -> []
      | (x :: xf) ->
          let rec repeat n =
            if n <= 0 then []
            else x :: repeat (n - 1)
          in
          let rest = pomocnicza xf in
          let rec append l1 l2 =
            match l1 with
              [] -> l2
              | (y :: yf) -> y :: append yf l2
          in
          if (x < 0) then
            failwith "ujemna"
          else
            append (repeat x) rest
  in
  pomocnicza lst
;;


polacz [1;2;-3];;
polacz [0;4;2];;
polacz [];;
polacz [2;2;2];;

ltake (10, lpolacz (toLazyList [1;2;3]));;
ltake (10, lpolacz (toLazyList [0;4;2]));;
ltake (10, lpolacz (toLazyList []));;
ltake (10, lpolacz (toLazyList [2;2;2]));;
