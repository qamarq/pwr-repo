import scala.annotation.tailrec

sealed trait BT[+A]
case object Empty extends BT[Nothing]
case class Node[A](elem: A, left: BT[A], right: BT[A]) extends BT[A]

def tree_my(tree: BT[Int]): Int = {
  @tailrec
  def loop(nodes: List[BT[Int]], sum: Int): Int = nodes match {
    case Nil => sum
    case head :: tail => head match {
      case Empty =>
        loop(tail, sum)
      case Node(value, left, right) =>
        loop(left :: right :: tail, sum * value)
    }
  }

  loop(List(tree), 1)
}

def main (): Unit = {
  val tt: Node[Int] =
    Node(1,Node(2,Node(4,Empty,Empty),Empty),Node(3,Node(5,Empty,Node(6,Empty,Empty)),Empty))
  println(tree_my(tt))

  //              1
  //            /  \
  //           2    6
  //          / \  / \
  //         _  10 9  _
  // powinno byc 1080
  val t1: Node[Int] =
    Node(1, Node(2, Empty, Node(10, Empty, Empty)), Node(6, Node(9, Empty, Empty), Empty))

  println(tree_my(t1))

  //            7
  //          /   \
  //         9     3
  //       /  \   /  \
  //      2   3  1   _
  //     / \ / \/ \
  //    45 __ 33_ _
  // powinno wyjsc 1683990
  val t2: Node[Int] =
    Node(7,Node(9, Node(2, Node(45, Empty, Empty), Empty), Node(3, Empty, Node(33, Empty, Empty))),Node(3, Node(1, Empty, Empty), Empty))

  println(tree_my(t2))

  println(tree_my(Empty))
}

main()