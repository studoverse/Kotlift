class MyClass {
  fun a(): Int {
    return 1
  }
  val d = 4
  var g = 7

  companion object {
    fun b(): Int {
      return 2
    }
    val e = 5
    var h = 8
  }

  fun c(): Int {
    return 3
  }
  val f = 6
  var i = 9
}

fun main(args: Array<String>) {
  val obj = MyClass()

  println(obj.a())
  println(MyClass.b())
  println(obj.c())

  println(obj.d)
  println(MyClass.e)
  println(obj.f)

  println(obj.g)
  println(MyClass.h)
  println(obj.i)
}