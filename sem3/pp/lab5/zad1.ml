let sum_list list =
  let rec sum_iter lst output =
    match lst with
    | [] -> output
    | head :: tail -> sum_iter tail (output + head)
  in
  sum_iter list 0
;;

let f_d lists value =
  let rec f_d_iter lists value output_lists =
    match lists with
    | [] -> List.rev output_lists
    | list :: lists_tail ->
      if sum_list list == value then f_d_iter lists_tail value (list :: output_lists)
      else f_d_iter lists_tail value output_lists
  in f_d_iter lists value []
;;


f_d [[1;2;3];[2;4];[5;6]] 6;;
f_d [[1;3];[2;3;1];[]] 6;;
f_d [] 6;;
f_d [[1;1;1;1;1;1];[2;4];[5;6]] 6;;