public class Base {
  func v() { print("ERROR-Open") }
  func nv() { print("4") }
  init() { print("1") }
}

public class Derived: Base {
  override func v() { print("ERROR-Derived") }
  func x() { print("6") }
  override init() { print("2") }
}

public class Derived2: Derived {
  override func v() { print("5") }
  func y() { print("7") }
  override init() { print("3") }
}

func main(args: [String]) {
  let x = Derived2()
  x.nv()
  x.v()
  x.x()
  x.y()
}

main([])
