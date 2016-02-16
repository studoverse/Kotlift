/* Output:
Nested init
234
Nested init
com.moshbit.kotlift.Outer$Nested@4c5e43ee (or similar)
Nested init
5
78910348910
*/

// Uncommented code will not compile due to visibility modifiers

open internal class Outer {
  private val a = 1
  protected val b = 2
  internal val c = 3
  val d = 4  // public by default
  protected val n = Nested()

  protected class Nested {
    internal val e: Int = 5
    init {
      println("Nested init")
    }
  }

  private fun o(): Int {
    return 6
  }
  protected fun p(): Int {
    return 7
  }
  internal fun q(): Int {
    return 8
  }
  fun r(): Int {
    return 9
  }
  public fun s(): Int {
    return 10
  }
}

protected class Subclass : Outer() {
  // a is not visible
  // b, c and d are visible
  // Nested and e are visible

  fun printAll() {
    // println(a)
    print(b)
    print(c)
    println(d)

    println(Outer.Nested())
    println(Nested().e)

    // print(o())
    print(p())
    print(q())
    print(r())
    print(s())
  }
}

class Unrelated(val o: Outer) {
  // o.a, o.b are not visible
  // o.c and o.d are visible (same module)
  // Outer.Nested and Nested::e are not visible. In Swift they are visible, as there is no Protected.

  fun printAll() {
    // println(o.a)
    // println(o.b) // This statement runs in Swift, as there is no Protected.
    print(o.c)
    print(o.d)

    /* // It is OK that the following 3 lines run in Swift:
    val nested = Outer.Nested()
    println(nested)
    println(nested.e)*/

    // print(o.o())
    // print(o.p()) // This statement runs in Swift, as there is no Protected.
    print(o.q())
    print(o.r())
    print(o.s())
  }
}

fun main(args: Array<String>) {
  val x = Subclass()
  x.printAll()

  val y = Unrelated(o: x)
  y.printAll()
}
