class FunkyClass {
  var internalString = ""

  var wrappedProperty: String
    get() { return "My string is $internalString" }
    set(value) {
      internalString = "$value - previous=\"$internalString\""
    }
}

val computedProperty1: Int
  get() {
    var a = 0
    a++
    return a
  }

val computedProperty2: Int
  get() { return 2 }

/*val computedProperty3: Int
  get() = 3*/


var _backingProperty: Int = 0
var computedProperty4: Int
  get() {
      return 4 + _backingProperty
  }
  set(value) {
      _backingProperty = value
  }

var computedProperty5: Int
  get() { return 5 + _backingProperty }
  set(value) { _backingProperty = value }

/*var computedProperty6: Int
  get() = 6 + _backingProperty
  set(value) { _backingProperty = value }*/

fun main(args: Array<String>) {
  val x = FunkyClass()

  println(x.wrappedProperty)
  x.wrappedProperty = "abc"
  println(x.wrappedProperty)
  x.wrappedProperty = "123"
  println(x.wrappedProperty)

  println(computedProperty1)
  println(computedProperty2)
  //println(computedProperty3)

  println(computedProperty4)
  println(computedProperty5)
  //println(computedProperty6)
  computedProperty4 = 1000
  println(computedProperty4)
  println(computedProperty5)
  //println(computedProperty6)
}
