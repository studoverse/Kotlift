class Box<T>(t: T) {
  var value: T // value = t won't work, as this must happen in swift inside constructor

  fun doNothing(value2: T) : T {
    return value
  }

  init {
    value = t
  }
}

class BigBox<T>(t: T) {
  var value1: T
  val value2: T

  init {
    value1 = t
    value2 = t
  }
}

fun main(args: Array<String>) {
  val box: Box<Int> = Box<Int>(t = 1)
  println(box)

  val box2 = Box(t = 2)
  println(box2)
    
  println(box.doNothing(4))

  val bigBox = BigBox(t = 3)
  println(bigBox.value1 + bigBox.value2)
}