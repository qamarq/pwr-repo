let rec suma xs =
  if xs = [] then
    0.0
  else
    List.hd xs +. suma (List.tl xs);;

suma [5.;3.;2.];;
suma [];;
suma [0.;0.;0.;0.];;
suma [1.5;2.5;3.5];;
suma [2.];;