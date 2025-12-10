module type TUPLE_OPERATIONS = sig
  val element_count : 'a * 'a * 'a -> 'a
  val all_greater_than : int * int * int -> int -> bool
  val sum_element : int * int * int -> int
  val subtract : int * int * int -> int * int * int -> int * int * int
  val all_negative : int * int * int -> bool
end

module TupleOperations : TUPLE_OPERATIONS = struct

  let element_count (_, _, c) = c

  let all_greater_than (a, b, c) x =
    a > x && b > x && c > x

  let sum_element (a, b, c) =
    a + b + c

  let subtract (a1, b1, c1) (a2, b2, c2) =
    (a1 - a2, b1 - b2, c1 - c2)

  let all_negative (a, b, c) =
    a < 0 && b < 0 && c < 0

end

open TupleOperations;;

element_count (1, 2, 3);;
element_count (0, 0, 0);;
element_count (-1, -2, -3);;
element_count (999999, 5, 123456);;
element_count ("a", "b", "c");;

all_negative (-1, -5, -10);;
all_negative (-1, 0, -3);;
all_negative (0, 0, 0);;
all_negative (1, 2, 3);;
all_negative (-1, -2, 5);;
all_negative (-999999, -1, -12345);;

all_greater_than (5, 7, 9) 3;;
all_greater_than (5, 3, 9) 3;;
all_greater_than (5, 7, 1) 3;;
all_greater_than (0, 1, 2) (-5);;
all_greater_than (-1, -2, -3) (-10);;
all_greater_than (-1, -2, -3) 0;;
all_greater_than (999999, 888888, 777777) 100000;;

subtract (10, 5, 3) (1, 2, 3);;
subtract (0, 0, 0) (0, 0, 0);;
subtract (-1, -2, -3) (5, 6, 7);;
subtract (5, 6, 7) (-1, -2, -3);;
subtract (1000000, 2000000, 3000000) (999999, 1999999, 2999999);;
subtract (1, 1, 1) (2, 2, 2);;

sum_element (1, 2, 3);;
sum_element (0, 0, 0);;
sum_element (-1, -2, -3);;
sum_element (5, -5, 10);;
sum_element (1000000, 2000000, 3000000);;
sum_element (-1000000, 500000, 250000);;

