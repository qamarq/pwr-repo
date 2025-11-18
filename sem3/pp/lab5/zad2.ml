let f_d number_in_dec =
  let rec f_d_iter n output_in_hex_numbers =
    match n with
      | 0 -> output_in_hex_numbers
      | _ -> f_d_iter (n / 16) ((n mod 16) :: output_in_hex_numbers)
  in f_d_iter number_in_dec [] 
;;

f_d 31;;
f_d 255;;
f_d 16;;
f_d 0;;
f_d 4095;;
f_d 123456;;
f_d (-257);;
f_d (-31);;
f_d (-255);;
f_d (-16);;
f_d (-4095);;
f_d (-123456);;