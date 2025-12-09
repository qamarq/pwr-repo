type 'a nlist = Koniec | Element of 'a * ('a nlist);;
type 'a llist = LKoniec | LElement of 'a * (unit -> 'a llist);;

let dzialanie lista_x lista_y dzialanie =
  let rec pomocnicza lx ly acc =
    match (lx, ly) with
    | ([], []) -> List.rev acc
    | (xh::xt, yh::yt) ->
        let wynik = dzialanie xh yh in
        pomocnicza xt yt (wynik :: acc)
    | ([], yh::yt) ->
        pomocnicza [] yt (yh :: acc)
    | (xh::xt, []) ->
        pomocnicza xt [] (xh :: acc)
  in
  pomocnicza lista_x lista_y []


(* leniwa implementacja *)
let ldzialanie lista_x lista_y dzialanie =
  let rec pomocnicza lx ly =
    match (lx, ly) with
    | (LKoniec, LKoniec) -> LKoniec
    | (LElement (xh, xt), LElement (yh, yt)) ->
        LElement (dzialanie xh yh, fun () -> pomocnicza (xt ()) (yt ()))
    | (LKoniec, LElement (yh, yt)) ->
        LElement (yh, fun () -> pomocnicza LKoniec (yt ()))
    | (LElement (xh, xt), LKoniec) ->
        LElement (xh, fun () -> pomocnicza (xt ()) LKoniec)
  in
  pomocnicza lista_x lista_y

let rec lfrom k = LElement (k, function () -> lfrom (k+1));;  
let rec ltake (n, lxs) =
  match (n, lxs) with
    (0, _) -> []
    | (_, LKoniec) -> []
    | (n, LElement(x,xf)) -> x::ltake(n-1, xf())
;;
let rec lmap f lxs =
  match lxs with
    LKoniec -> LKoniec
    | LElement(x, xf) -> LElement(f x, function () -> lmap f (xf()) )
;;

let rec toLazyList xs =
  match xs with
    [] -> LKoniec
    | h::t -> LElement(h, function () -> toLazyList t);;



dzialanie [1;2;3] [2;3;4;5] ( + );;
dzialanie [1;2] [2;3] ( - );;
dzialanie [-5;-6] [-2;3;4;5] ( * );;
dzialanie [] [2;3;4;5] ( + );;
dzialanie [2;3;4;5] [] ( + );;
dzialanie [] [] ( + );;
dzialanie [5;10] [5;5] ( / );;

ltake (5, ldzialanie (toLazyList [1;2]) (toLazyList [3;4]) ( + ));;
ltake (5, ldzialanie (toLazyList [4;5]) (toLazyList [5;10]) ( - ));;
ltake (5, ldzialanie LKoniec LKoniec ( + ));;
ltake (5, ldzialanie LKoniec (toLazyList [4;5]) ( + ));;
ltake (5, ldzialanie (toLazyList [4;5]) LKoniec ( + ));;
ltake (5, ldzialanie (toLazyList [4]) (toLazyList [2;3]) ( * ));;