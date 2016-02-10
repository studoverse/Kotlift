class MyClass {
  func a() -> Int {
    return 1
  }
  let d = 4
  var g = 7

    static func b() -> Int {
      return 2
    }
    static let e = 5
    static var h = 8

  func c() -> Int {
    return 3
  }
  let f = 6
  var i = 9
  init() {
  }
}

func main(args: [String]) {
  let obj = MyClass()

  print(obj.a())
  print(MyClass.b())
  print(obj.c())

  print(obj.d)
  print(MyClass.e)
  print(obj.f)

  print(obj.g)
  print(MyClass.h)
  print(obj.i)
}

main([])
