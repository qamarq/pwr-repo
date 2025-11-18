let f_k krotki =  
  List.map (fun (a,b,c,d) -> a ^ b ^ c ^ d) krotki
;;

f_k [("ala","ma","kot","a");("kot","nie","ma","ali")];;
f_k [("test"," ", "spacji","");("kot","nie","ma","ali")];;
f_k [("ala","ma","kot","a");("a","b","c","d")];;
f_k [];;
f_k [("ala","ma","kot","a");("kot","nie","ma","ali");("moje","nowe","slowo","koniec")];;