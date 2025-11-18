def dlugosc[A](lista: List[A]): Int = {
  def licz(l: List[A], wynik: Int): Int =
    l match {
      case Nil => wynik
      case _ :: tail => licz(tail, wynik + 1)
    }
  licz(lista, 0)
}

dlugosc(List(5, 4, 3, 2))
dlugosc(List())
dlugosc(List(1, 2, 3, 4, 5))
dlugosc(List("a", "b", "c"))
dlugosc(List(true, false, true, false, true))
