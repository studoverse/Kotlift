public class FunkyClass {
  var internalString = ""

  var wrappedProperty: String {
    get { return "My string is \(internalString)" }
    set(value) {
      internalString = "\(value) - previous=\"\(internalString)\""
    }
}
  init() {
  }
}

var computedProperty1: Int32 {
  get {
    var a = 0
    a++
    return a
  }
}

var computedProperty2: Int32 {
  get { return 2 }
}

/*val computedProperty3: Int
  get() = 3*/


var _backingProperty: Int32 = 0
var computedProperty4: Int32 {
  get {
      return 4 + _backingProperty
  }
  set(value) {
      _backingProperty = value
  }
}

var computedProperty5: Int32 {
  get { return 5 + _backingProperty }
  set(value) { _backingProperty = value }
}

/*var computedProperty6: Int
  get() = 6 + _backingProperty
  set(value) { _backingProperty = value }*/

func main(args: [String]) {
  let x = FunkyClass()

  print(x.wrappedProperty)
  x.wrappedProperty = "abc"
  print(x.wrappedProperty)
  x.wrappedProperty = "123"
  print(x.wrappedProperty)

  print(computedProperty1)
  print(computedProperty2)
  //print(computedProperty3)

  print(computedProperty4)
  print(computedProperty5)
  //print(computedProperty6)
  computedProperty4 = 1000
  print(computedProperty4)
  print(computedProperty5)
  //print(computedProperty6)
}

main([])
