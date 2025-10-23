def mniejsze(lista: List[Int], liczba: Int): Boolean =
  if lista == Nil then
    true
  else
    if lista.head < liczba then
      mniejsze(lista.tail, liczba)
    else
      false


mniejsze(List(1, 2, 3, 4), 5)
mniejsze(List(6, 1, 3, 2), 6)
mniejsze(List(), 5)
mniejsze(List(10, 20, 30), 5)