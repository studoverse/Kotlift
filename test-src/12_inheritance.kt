open class Base {
  open fun v() { println("ERROR-Open") }
  fun nv() { println("4") }
  init { println("1") }
}

open class Derived() : Base() {
  override open fun v() { println("ERROR-Derived") }
  fun x() { println("6") }
  init { println("2") }
}

class Derived2 : Derived() {
  override fun v() { println("5") }
  fun y() { println("7") }
  init { println("3") }
}

fun main(args: Array<String>) {
  val x = Derived2()
  x.nv()
  x.v()
  x.x()
  x.y()
}
