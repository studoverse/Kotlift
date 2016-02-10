open class Base {
  open fun v() { println("ERROR") }
  fun nv() { println("3") }
  init { println("1") }
}

class Derived() : Base() {
  override fun v() { println("4") }
  init { println("2") }
}

class Derived2 : Base() {
  override fun v() { println("4") }
  init { println("2") }
}

fun main(args: Array<String>) {
  val x = Derived()
  x.nv()
  x.v()
}