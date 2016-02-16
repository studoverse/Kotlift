open class Base {
  open fun f() {}
  fun g() {
    println("g() called")
  }
}

abstract class Derived : Base() {
  override abstract fun f()
}

class Derived2 : Base() {
  override fun f() {
    println("f() called")
  }
}

fun main(args: Array<String>) {
  Derived2().f()
  Derived2().g()
}
