//bez rekurencji ogonowej
def polacz_nie_ogonowa(lst1: List[Int], lst2: List[Int]): List[Int] = {
  lst1 match {
    case head :: tail => head :: polacz_nie_ogonowa(tail, lst2)
    case Nil => lst2
  }
}

// z rekurencja ogonowa
def odwroc(lst: List[Int]): List[Int] = {
  @annotation.tailrec
  def odwroc_pom(l: List[Int], wynik: List[Int]): List[Int] = {
    l match {
      case Nil => wynik
      case head :: tail => odwroc_pom(tail, head :: wynik)
    }
  }
  odwroc_pom(lst, Nil)
}

def polacz(lst1: List[Int], lst2: List[Int]): List[Int] = {
  @annotation.tailrec
  def polacz_pom(l1: List[Int], l2: List[Int], wynik: List[Int]): List[Int] = {
    l1 match {
      case Nil => odwroc(wynik) ++ l2
      case head :: tail => polacz_pom(tail, l2, head :: wynik)
    }
  }
  polacz_pom(lst1, lst2, Nil)
}

polacz_nie_ogonowa(List(5,4,3,2), List(1,0))
polacz_nie_ogonowa(List(1,2,3,4,5), List(7,8,9,10,0))
polacz_nie_ogonowa(List(6,3,5,2,1), List(0))
polacz_nie_ogonowa(List(5,4,3,2), List())
polacz_nie_ogonowa(List(), List(1,0))

polacz(List(5,4,3,2), List(1,0))
polacz(List(1,2,3,4,5), List(7,8,9,10,0))
polacz(List(6,3,5,2,1), List(0))
polacz(List(5,4,3,2), List())
polacz(List(), List(1,0))