trait Debug {
  def debugName(): Unit = {
    println(this.getClass.getName)
  }
  def debugVars(): Unit = {
    val fields = this.getClass.getDeclaredFields
    for (f <- fields) {
      f.setAccessible(true)
      println(
        s"${f.getName}, ${f.getType.getSimpleName}, ${f.get(this)}"
      )
    }
  }
}

class Samochod(
  val marka: String,
  val model: String,
  val rok: Int,
  val cena: Double
) extends Debug

val s1 = new Samochod("Toyota", "Corolla", 2020, 75000)
s1.debugName()
s1.debugVars()

val s2 = new Samochod("BMW", "X5", 2018, 150000)
s2.debugName()
s2.debugVars()

val s3 = new Samochod("Audi", "A4", 2019, 120000)
s3.debugName()
s3.debugVars()