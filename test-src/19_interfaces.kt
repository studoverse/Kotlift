// Simple Interface

interface MyInterface {
  fun bar(): String
  /*fun foo(): Int {
    return 1
  }*/
}

class Implementation : MyInterface {
  override fun bar(): String {
    return "2-Implementation"
  }
}

// Interface + Inheritance

open class Parent {
  fun three(): Int {
    return 3
  }

  var four = 4
}

class Child: Parent(), MyInterface {
  val five = 5
  fun six(): String {
    return "6"
  }
  override fun bar(): String {
    return "2-Child"
  }
}

// Abstract Interface + Inheritance

abstract class AbstractParent {
  abstract fun three(): Int

  var four = 4
}

abstract class AbstractChild: Parent() {
}

class NonAbstractChild: AbstractChild(), MyInterface {
  override fun bar(): String {
    return "2-NonAbstractChild"
  }
}

fun main(args: Array<String>) {
  // Simple Interface
  val obj = Implementation()
  println(obj.foo())
  println(obj.bar())

  // Interface + Inheritance
  val child = Child()
  println(child.foo())
  println(child.bar())
  println(child.three())
  println(child.four)
  println(child.five)
  println(child.six())

  // Abstract Interface + Inheritance
  val naChild = NonAbstractChild()
  println(naChild.foo())
  println(naChild.bar())
  println(naChild.three())
  println(naChild.four)
}
