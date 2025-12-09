sealed trait LList[+A]
case object LKoniec extends LList[Nothing]
case class LElement[A](head: A, tail: () => LList[A]) extends LList[A]

def lwybierz[A](lista: LList[A], n: Int, m: Int): LList[A] = {

  def wywalPierwsza(ll: LList[A], k: Int): LList[A] = ll match {
    case LKoniec => LKoniec
    case LElement(_, tail) =>
      if (k <= 1) ll
      else wywalPierwsza(tail(), k - 1)
  }

  def wybierz(ll: LList[A]): LList[A] = ll match {
    case LKoniec => LKoniec
    case LElement(h, tail) =>
      LElement(h, () => wybierz(skip(tail(), n - 1)))
  }

  def skip(ll: LList[A], k: Int): LList[A] = ll match {
    case LKoniec => LKoniec
    case LElement(_, tail) =>
      if (k <= 0) ll
      else skip(tail(), k - 1)
  }

  if (n <= 0 || m <= 0) LKoniec
  else wybierz(wywalPierwsza(lista, m))
}


def fromList[A](xs: List[A]): LList[A] = xs match {
  case Nil => LKoniec
  case h :: t => LElement(h, () => fromList(t))
}
def toList[A](ll: LList[A], limit: Int = 100): List[A] = ll match {
  case LKoniec => Nil
  case LElement(h, tail) =>
    if (limit <= 0) Nil
    else h :: toList(tail(), limit - 1)
}

toList(lwybierz(fromList(List(5,6,3,2,1)), 2, 1))
toList(lwybierz(LKoniec, 2, 1))
toList(lwybierz(fromList(List(10)), 3, 1))
toList(lwybierz(fromList(List(1,2,3)), -2, 1))
toList(lwybierz(fromList(List(1,2,3)), 2, 0))
toList(lwybierz(fromList(List("a","b","c","d","e")), 2, 2))
