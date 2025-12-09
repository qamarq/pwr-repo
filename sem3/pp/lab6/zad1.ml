type eval = Num of float | Add | Sub | Mul | Div;;

let eval instr =
  let rec process stos cmds = match cmds with
    | [] -> (match stos with
        | [Num wynik] -> wynik
        | _ -> failwith "brak dzialania")
    | cmd :: rest -> match cmd with
        | Num n -> process (Num n :: stos) rest
        | Add -> 
            (match stos with
              | Num b :: Num a :: tail -> process (Num (a +. b) :: tail) rest
              | _ -> failwith "za malo argumentow dla Add")
        | Sub -> 
            (match stos with
              | Num b :: Num a :: tail -> process (Num (a -. b) :: tail) rest
              | _ -> failwith "za malo argumentow dla Sub")
        | Mul -> 
            (match stos with
              | Num b :: Num a :: tail -> process (Num (a *. b) :: tail) rest
              | _ -> failwith "za malo argumentow dla Mul")
        | Div -> 
            (match stos with
              | Num b :: Num a :: tail -> (match b with
                  | 0. -> failwith "nie wolno dzielic przez 0"
                  | _ -> process (Num (a /. b) :: tail) rest)
              | _ -> failwith "za malo argumentow dla Div")

  in process [] instr
;;

eval [Num 5.; Num 3.; Add; Num 2.; Add];; 
eval [Num 3.; Num 5.; Sub];;
eval [Num 5.; Num 4.; Mul];;
eval [Num 6.; Num 3.; Div];;
eval [Num 6.; Num 0.; Div];;
eval [];;
eval [Num 5.; Num 3.; Add; Add];;
eval [Num 5.; Num 3.; Sub; Sub];;
eval [Num 5.; Num 3.; Num 6.];;
(* (2 + 3 - 8) * 2 = -6 *)
eval [Num 2.; Num 3.; Add; Num 8.; Sub; Num 2.; Mul];;