def mniejsze(lista: List[Int], liczba: Int): Boolean = {
  if (lista.isEmpty) {
    true
  } else {
    if (lista.head < liczba) {
      mniejsze(lista.tail, liczba)
    } else {
      false
    }
  }
}

@main
def main() = {
  println(mniejsze(List(1, 2, 3, 4), 5))
  println(mniejsze(List(1, 2, 6, 4), 5))
  println(mniejsze(List(), 5))
  println(mniejsze(List(10, 20, 30), 5))
}